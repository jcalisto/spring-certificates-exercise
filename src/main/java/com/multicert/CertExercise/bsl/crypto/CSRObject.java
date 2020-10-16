package com.multicert.CertExercise.bsl.crypto;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.*;
import java.math.BigInteger;

public class CSRObject {
	
	private final X509CertificateHolder x509crtHolder;
	private final String csrBase64;
	
	public CSRObject(final String csrBase64) throws IOException {
		this.csrBase64 = csrBase64;
		
		byte[] decoded = Base64.decodeBase64(csrBase64);
		String decodedCsr = new String(decoded, "UTF-8");
        final Reader pemReader = new StringReader(decodedCsr);
        final PEMParser pemParser = new PEMParser(pemReader);
        
        x509crtHolder = (X509CertificateHolder) pemParser.readObject();
    }
	
    public String getCsrBase64() {
		return csrBase64;
	}
    
    public String getSerialNumber() {
    	return this.x509crtHolder.getSerialNumber().toString();
    }

	public String get(final CSRObjectEnum field) {
		X500Name x500Name = this.x509crtHolder.getSubject();
        RDN[] rdnArray = x500Name.getRDNs(new ASN1ObjectIdentifier(field.code));
        String retVal = null;
        for (RDN item : rdnArray) {
            retVal = item.getFirst().getValue().toString();
        }
        return retVal;
    }

    public enum CSRObjectEnum {
        COUNTRY("2.5.4.6"),
        STATE("2.5.4.8"),
        LOCALE("2.5.4.7"),
        ORGANIZATION("2.5.4.10"),
        ORGANIZATION_UNIT("2.5.4.11"),
        COMMON_NAME("2.5.4.3"),//
        ;
        private final String code;

        CSRObjectEnum(final String sCode) {
            code = sCode;
        }
    }
}