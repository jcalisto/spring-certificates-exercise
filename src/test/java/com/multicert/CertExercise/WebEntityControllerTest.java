package com.multicert.CertExercise;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;
import com.multicert.CertExercise.web.controllers.WebEntityController;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles({ "test" })
@WebMvcTest(WebEntityController.class)
public class WebEntityControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private IEntityService service;
	
	@Test
	public void testHomePageDisplaysGetStarted() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Get Started")));
	}
	
	@Test
	public void testAllEntitiesAreListed() throws Exception {
		List<EntityData> entities = new ArrayList<EntityData>();
		entities.add(new EntityData("Company A", "100200300", "PT", "Collective", ""));
		entities.add(new EntityData("Company B", "100200301", "PT", "Collective", ""));
		entities.add(new EntityData("Person A", "100200302", "PT", "Singular", "Company A"));
		when(service.listEntities()).thenReturn(entities);
		this.mockMvc.perform(get("/entities")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Company A")))
				.andExpect(content().string(containsString("Company B")))
				.andExpect(content().string(containsString("Person A")));
	}
	
	@Test
	public void testEntityAddedRedirectsToEntitiesWithoutErrors() throws Exception {
		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("name", "Company A");
		requestParams.add("nif", "100200300");
		requestParams.add("countryCode", "PT");
		requestParams.add("entityType", "Collective");
		requestParams.add("companyName", "");
		
		List<EntityData> entities = new ArrayList<EntityData>();
		EntityData entity = new EntityData("Company A", "100200300", "PT", "Collective", "");
		entities.add(entity);
		
		when(service.addEntity("Company A", "100200300", "PT", "Collective", "")).thenReturn(entity);
		when(service.listEntities()).thenReturn(entities);
		this.mockMvc.perform(post("/entities/add").params(requestParams))
				.andExpect(redirectedUrl("/entities"))
				.andExpect(status().isFound());
	}
	
	@Test
	public void testBadEntityAddedRedirectsToEntitiesWithErrors() throws Exception {
		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("name", "Company A");
		requestParams.add("nif", "100200300");
		requestParams.add("countryCode", "PT");
		requestParams.add("entityType", "Collective");
		requestParams.add("companyName", "");
		
		when(service.addEntity("Company A", "100200300", "PT", "Collective", "")).thenThrow(MockitoException.class);
		this.mockMvc.perform(post("/entities/add").params(requestParams))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/entities"))
				.andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Error adding client, invalid arguments"));
	}
	
	
	@Test
	public void testEntityDetailsShowRequestedCertificates() throws Exception {
		EntityData entity = new EntityData("Company A", "100200300", "PT", "Collective", "");
		CertificateRequestData reqCertificate = new CertificateRequestData(entity, "CN=Joao,OU=mTr,O=Mcert,L=LX,ST=LX,C=PT", "1000000000", "dummy cert");
		entity.addCertificateRequest(reqCertificate);
		Optional<EntityData> wEntity = Optional.of(entity);
		
		when(service.getEntityById(any())).thenReturn(wEntity);
		this.mockMvc.perform(get("/entities/1/details")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Company A")))
				.andExpect(content().string(containsString("Collective")))
				.andExpect(content().string(containsString("CN=Joao,OU=mTr,O=Mcert,L=LX,ST=LX,C=PT")))
				.andExpect(content().string(containsString("1000000000")));
	}
}
