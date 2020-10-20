package com.multicert.CertExercise.web.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.multicert.CertExercise.bsl.crypto.CSRObject;
import com.multicert.CertExercise.bsl.crypto.CSRSigner;
import com.multicert.CertExercise.bsl.crypto.ICSRSigner;
import com.multicert.CertExercise.bsl.services.ICertificateService;
import com.multicert.CertExercise.bsl.services.IEntityService;
import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;
import com.multicert.CertExercise.utils.RandomUtils;

@Controller
public class WebCertificateController {
	
	@Autowired
	private IEntityService entityService;
	
	@Autowired
	private ICertificateService certificateService;

	@PostMapping("/upload-certificate")
	public String handleFileUpload(@RequestParam("entityId") Long entityId, @RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes) {
		
		try {
			byte[] fileContent = file.getBytes();
	        String csrBase64 = Base64.getEncoder().encodeToString(fileContent);
	        CSRObject csr = new CSRObject(csrBase64);
	        String subjectDN = csr.getSubjectDN();
	        String serialNumber = RandomUtils.randomBigInt(20).toString();
	        entityService.addCertificateRequest(entityId, subjectDN, serialNumber, csrBase64);
		}catch(Exception e) {
			System.out.print("upload-certificate, error: " + e.getMessage());
			redirectAttributes.addAttribute("errorMessage", "Error uploading certificate, invalid format");
		}
        
		return "redirect:/entities/" + entityId + "/details";
	}
	
	@PostMapping("/sign-certificate")
	public String handleFileUpload(@RequestParam("entityId") Long entityId, @RequestParam("reqCertificateId") Long reqCertificateId,
			RedirectAttributes redirectAttributes) {
		try {
			Optional<CertificateRequestData> crd = certificateService.findCertificateRequestById(reqCertificateId);
			if(crd.isPresent()) {
				String serialNumber = crd.get().getSerialNumber();
				ICSRSigner csrSigner = new CSRSigner();
				X509Certificate certificate = csrSigner.signCertificationRequest(crd.get().getCertificate(), serialNumber);
				String certificateBase64 = csrSigner.convertCertificateToPEM(certificate);
				String subjectDN = certificate.getSubjectX500Principal().getName().toString();
				EntityData entity = entityService.addSignedCertificate(entityId, subjectDN, serialNumber, certificateBase64);
				certificateService.deleteCertificateRequest(reqCertificateId);	
			}
		}catch(Exception e) {
			System.out.print("sign-certificate, error: " + e.getMessage());
			redirectAttributes.addAttribute("errorMessage", "Error signing certificate");
		}
        
		return "redirect:/entities/" + entityId + "/details";
	}
	
	@PostMapping("/download/signed-certificate")
	public void  downloadCertificate(@RequestParam("entityId") Long entityId, @RequestParam("certificateId") Long certificateId, HttpServletResponse response) {
		Optional<CertificateData> certificate = certificateService.findCertificateSignedById(certificateId);
		
		if(!certificate.isPresent()) return;
		
		String certificateBase64 = certificate.get().getCertificate();
		try {
			OutputStream out = response.getOutputStream();
			response.setContentType("application/x-x509-user-cert");
			response.setHeader("Content-Disposition", "attachment; filename=\"signed-certificate.crt\"" );
			response.setHeader("Access-Control-Expose-Headers","Authorization, Content-Disposition");
			try (PrintWriter pw = new PrintWriter(response.getOutputStream())) {
				pw.write(certificateBase64);
			}
			out.close();
					
		} catch (IOException e) {
		
		}
	 }
}
