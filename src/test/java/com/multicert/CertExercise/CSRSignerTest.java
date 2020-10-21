package com.multicert.CertExercise;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import com.multicert.CertExercise.bsl.crypto.CSRSigner;
import com.multicert.CertExercise.bsl.crypto.KeyManager;

public class CSRSignerTest {
	
	static final String DIGEST_SHA1 = "SHA1withRSA";
    static final String BC_PROVIDER = "BC";
	
	@Test
	public void testValidCSRSignature() throws IOException {
		
		KeyManager keyManager = new KeyManager();
		PublicKey publicKey = keyManager.getPublicKey();
			
		File testFile = ResourceUtils.getFile("classpath:testdata/testcsr.csr");
		String csrBase64 = new String(Base64.encodeBase64(Files.readAllBytes(testFile.toPath())), StandardCharsets.US_ASCII);
		
		CSRSigner csrSigner = new CSRSigner();
		X509Certificate certificate = csrSigner.signCertificationRequest(csrBase64, "100000001");
		
		assertDoesNotThrow(() -> certificate.verify(publicKey));
	}

}
