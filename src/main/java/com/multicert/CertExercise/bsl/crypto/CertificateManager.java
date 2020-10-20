package com.multicert.CertExercise.bsl.crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;


public class CertificateManager {
	
	public CertificateManager() {
		
	}
	
	public X500Name getCARootSubject() {
		File keyFile;
		try {
			keyFile = ResourceUtils.getFile("classpath:keystore/ca.cert.pem");
			
			PEMParser pemParser = new PEMParser(new FileReader(keyFile));
			X509CertificateHolder holder = (X509CertificateHolder) pemParser.readObject();
			
			return holder.getSubject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getDefaultX500Name();
	}
	
	private X500Name getDefaultX500Name() {
		return new X500Name("CN=default issuer");
	}
}
