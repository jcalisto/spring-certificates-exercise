package com.multicert.CertExercise.bsl.crypto;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

public class CSRSigner implements ICSRSigner{
	
	
	public CSRSigner() {}
	
	public X509Certificate signCertificationRequest(String csrBase64, String serialNumber) {
		
		KeyManager keyManager = new KeyManager();
		PublicKey publicKey = keyManager.getPublicKey();
		PrivateKey privateKey = keyManager.getPrivateKey();
		
		try {
			PKCS10CertificationRequest csr = new CSRObject(csrBase64).getCsr();
			return  sign(privateKey, publicKey, csr, serialNumber);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (OperatorCreationException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}


	public X509Certificate sign(PrivateKey privateKey, PublicKey publicKey, PKCS10CertificationRequest csr, String serialNumber)
	        throws InvalidKeyException, NoSuchAlgorithmException,
	        NoSuchProviderException, SignatureException, IOException,
	        OperatorCreationException, CertificateException {   

	    AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder()
	            .find("SHA1withRSA");
	    AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder()
	            .find(sigAlgId);

	    AsymmetricKeyParameter foo = PrivateKeyFactory.createKey(privateKey
	            .getEncoded());
	    SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
	    
	    CertificateManager caManager = new CertificateManager();
	    X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
	            caManager.getCARootSubject(), 
	            new BigInteger(serialNumber), 
	            new Date(System.currentTimeMillis()), 
	            new Date(System.currentTimeMillis() + 30 * 365 * 24 * 60 * 60* 1000),
	            csr.getSubject(), keyInfo);

	    ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
	            .build(foo);        

	    X509CertificateHolder holder = myCertificateGenerator.build(sigGen);

	    Certificate eeX509CertificateStructure = holder.toASN1Structure(); 

	    CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

	    // Read Certificate
	    InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
	    X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
	    is1.close();
	    return theCert;
	}


	public String convertCertificateToPEM(X509Certificate signedCertificate) throws IOException {
	    StringWriter signedCertificatePEMDataStringWriter = new StringWriter();
		JcaPEMWriter pemWriter = new JcaPEMWriter(signedCertificatePEMDataStringWriter);
		pemWriter.writeObject(signedCertificate);
		pemWriter.close();
		return signedCertificatePEMDataStringWriter.toString();
	}
}
