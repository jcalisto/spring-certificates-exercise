package com.multicert.CertExercise.bsl.crypto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface ICSRSigner {
	public X509Certificate signCertificationRequest(String csrBase64, String serialNumber);

	public X509Certificate sign(PrivateKey privateKey, PublicKey publicKey, PKCS10CertificationRequest csr, String serialNumber) 
			throws InvalidKeyException, NoSuchAlgorithmException,
				    NoSuchProviderException, SignatureException, IOException,
				    OperatorCreationException, CertificateException ;

	public String convertCertificateToPEM(X509Certificate signedCertificate)
			throws IOException;
}
