package com.multicert.CertExercise.web.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.multicert.CertExercise.bsl.crypto.CSRObject;
import com.multicert.CertExercise.bsl.crypto.CSRSigner;
import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.bsl.services.IReqCertificateService;
import com.multicert.CertExercise.bsl.services.ISignedCertificateService;
import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;

@Controller
public class WebEntityController {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private IReqCertificateService reqCertificateService;
	
	@Autowired ISignedCertificateService signedCertificateService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/entities")
	public String listEntities(Model model) {
		List<EntityData> entities;
		try {
			entities = (List<EntityData>) entityService.listEntities();
		} catch(Exception e) {	//Todo specify?
			System.out.println("listEntities Exception: " + e.getMessage());
			entities = new ArrayList<EntityData>();
			model.addAttribute("errorMessage", "Can't fetch Entities.");
		}
		model.addAttribute("entities", entities);
		return "entities";
	}
	
	@PostMapping("/entities/add")
	public String addEntity(@RequestParam("name") String name, 
					@RequestParam("nif") String nif, 
					@RequestParam("countryCode") String countryCode, 
					@RequestParam("entityType") String entityType, 
					@RequestParam("companyName") String companyName, 
					Model model, RedirectAttributes redirectAttributes) {
		try {
			entityService.addEntity(name, nif, countryCode, entityType, companyName);
		} catch(Exception e) {	//Todo specify?
			redirectAttributes.addAttribute("errorMessage", "Error adding client, invalid arguments");
		}
		return "redirect:/entities";
	}
	
	@GetMapping("/entities/{id}/details")
	public String detailEntity(@PathVariable(value="id") Long id, Model model) {
		Optional<EntityData> wEntity = entityService.getEntityById(id);
		if(wEntity.isPresent()) {
			model.addAttribute("entity", wEntity.get());
			model.addAttribute("requestedCertificates", wEntity.get().getRequestedCertificates());
			model.addAttribute("signedCertificates", wEntity.get().getSignedCertificates());
		} else {
			model.addAttribute("errorMessage", "Entity with ID=" + id + " not found.");
		}
		return "entity_detailed";
	}
	
	@PostMapping("/upload-certificate")
	public String handleFileUpload(@RequestParam("entityId") Long entityId, @RequestParam("file") MultipartFile file) {
		
		try {
			byte[] fileContent = file.getBytes();
	        String csrBase64 = Base64.getEncoder().encodeToString(fileContent);
	        CSRObject csr = new CSRObject(csrBase64);
	        String subjectDN = csr.getSubjectDN();
	        String serialNumber = "testSN"; //TODO ADD REAL SN
	        entityService.addCertificateRequest(entityId, subjectDN, serialNumber, csrBase64);
		}catch(IOException e) {
			System.out.print("upload-certificate, read file bytes error: " + e.getMessage());
		}
        
		return "redirect:/entities/" + entityId + "/details";
	}
	
	@PostMapping("/sign-certificate")
	public String handleFileUpload(@RequestParam("entityId") Long entityId, @RequestParam("reqCertificateId") Long reqCertificateId) {
		try {
			Optional<CertificateRequestData> csr = reqCertificateService.findById(reqCertificateId);
			if(csr.isPresent()) {
				CSRObject csrObject = new CSRObject(csr.get().getCertificate());
				CSRSigner csrSigner = new CSRSigner();
				X509Certificate certificate = csrSigner.signCertificationRequest(csrObject.getCsr());
				String certificateBase64 = csrSigner.convertCertificateToPEM(certificate);
				String subjectDN = certificate.getSubjectX500Principal().getName().toString();
				String serialNumber = certificate.getSerialNumber().toString();
				System.out.println("Saving signed certificate");
				EntityData entity = entityService.addSignedCertificate(entityId, subjectDN, serialNumber, certificateBase64);
				System.out.println(entity.getName());
				reqCertificateService.deleteCertificateRequest(entityId);	
			}
		}catch(Exception e) {
			System.out.print("sign-certificate, read file bytes error: " + e.getMessage());
		}
        
		return "redirect:/entities/" + entityId + "/details";
	}
	
	@PostMapping("/download-certificate")
	public void  downloadCertificate(@RequestParam("entityId") Long entityId, @RequestParam("certificateId") Long certificateId, HttpServletResponse response) {
		Optional<CertificateData> certificate = signedCertificateService.findById(certificateId);
		
		if(!certificate.isPresent()) return;
		
		String filename = "signed-certificate.crt";
		String certificateBase64 = certificate.get().getCertificate();
		try {
			OutputStream out = response.getOutputStream();
			response.setContentType("application/x-x509-user-cert");
			response.setHeader("Content-Disposition", "signed-certificate.crt");
			response.setHeader("Access-Control-Expose-Headers","Authorization, Content-Disposition");
			try (PrintWriter pw = new PrintWriter(response.getOutputStream())) {
				pw.write(certificateBase64);
			}
			out.close();
					
		} catch (IOException e) {
		
		}
	 }

	
}


