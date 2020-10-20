package com.multicert.CertExercise.bsl.crypto;

import java.io.*;
import java.security.*;

import org.bouncycastle.openssl.*;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

public class KeyManager {
	private PublicKey publicKey;
	private PrivateKey privateKey;

	private final String secret = "joao123"; //TODO protect 
	
	public KeyManager() {
		loadKeys();
	}
	
	private void loadKeys() {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			File keyFile = ResourceUtils.getFile("classpath:keystore/ca.key.pem");
			
			PEMParser pemParser = new PEMParser(new FileReader(keyFile));
			Object object = pemParser.readObject();
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
			KeyPair kp;

		    // Encrypted key - using provided password
		    PEMEncryptedKeyPair ckp = (PEMEncryptedKeyPair) object;
		    PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build("joao123".toCharArray());
		    kp = converter.getKeyPair(ckp.decryptKeyPair(decProv));
		    	
			// RSA
			KeyFactory keyFac = KeyFactory.getInstance("RSA");
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	
}
