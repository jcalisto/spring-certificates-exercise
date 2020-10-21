package com.multicert.CertExercise;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;


import org.apache.tomcat.util.codec.binary.Base64;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import com.multicert.CertExercise.bsl.services.ICertificateService;
import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;
import com.multicert.CertExercise.web.controllers.WebCertificateController;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles({ "test" })
@WebMvcTest(WebCertificateController.class)
public class WebCertificateControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private IEntityService entityService;
	
	@MockBean
	private ICertificateService certificateService;
	
	private static File testFile;
	private static String csrBase64;
	private static String subjectDN;
	
	@BeforeAll
	public static void initTestCSR() throws IOException {
		testFile = ResourceUtils.getFile("classpath:testdata/testcsr.csr");
		csrBase64 = new String(Base64.encodeBase64(Files.readAllBytes(testFile.toPath())), StandardCharsets.US_ASCII);
		subjectDN = "C=PT,ST=LX,L=LX,O=Mcert,OU=mTr,CN=Joao";
	}
	
	@Test
	public void whenUploadGoodCertificate_thenRedirectWithoutErrors() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.crt", "text/plain", Files.readAllBytes(testFile.toPath()));
		
		this.mockMvc.perform(multipart("/upload-certificate")
				.file(file)
				.param("entityId", "1"))
		.andExpect(redirectedUrl("/entities/1/details"))
		.andExpect(status().isFound());
		verify(entityService).addCertificateRequest(eq(1L), eq(subjectDN), anyString(), anyString());
	}
	
	@Test
	public void whenUploadBadCertificate_thenRedirectWithErrors () throws Exception {
		
		MockMultipartFile file = new MockMultipartFile("file", "test.crt", "text/plain", "dummy".getBytes());
		
		this.mockMvc.perform(multipart("/upload-certificate")
				.file(file)
				.param("entityId", "1"))
		.andExpect(MockMvcResultMatchers.view().name("redirect:/entities/1/details"))
		.andExpect(status().isFound())
		.andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Error uploading certificate, invalid format"));
	}
	
	@Test
	public void whenSignatureSuccessfull_thenRedirectWithoutErrors () throws Exception {
		EntityData entity = new EntityData("Company A", "100200300", "PT", "Collective", "");
		CertificateRequestData crd = new CertificateRequestData(entity, subjectDN, "100000001", csrBase64);
		
		when(certificateService.findCertificateRequestById(any())).thenReturn(Optional.of(crd));
		
		this.mockMvc.perform(post("/sign-certificate")
				.param("entityId", "1")
				.param("reqCertificateId", "1"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/entities/1/details"))
			.andExpect(status().isFound());
		
		verify(entityService).addSignedCertificate(anyLong(), anyString(), eq("100000001"), anyString());
		verify(certificateService).deleteCertificateRequest(anyLong());
	}
	
	@Test
	public void whenSignatureFails_thenRedirectWithErrors () throws Exception {
		String badCsrBase64 = new String("dummy cert".getBytes(), StandardCharsets.US_ASCII);
		EntityData entity = new EntityData("Company A", "100200300", "PT", "Collective", "");
		CertificateRequestData crd = new CertificateRequestData(entity, subjectDN, "100000001", badCsrBase64);
		
		when(certificateService.findCertificateRequestById(any())).thenReturn(Optional.of(crd));
		
		this.mockMvc.perform(post("/sign-certificate")
				.param("entityId", "1")
				.param("reqCertificateId", "1"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/entities/1/details"))
			.andExpect(status().isFound())
			.andExpect(MockMvcResultMatchers.model().attribute("errorMessage", "Error signing certificate"));
			
	}
	
	
}
