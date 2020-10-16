package com.multicert.CertExercise.bsl.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.*;

public class CSRObject {
	private final X500Name x500Name;
	private final String csrBase64;
	
	public CSRObject(final String csr) throws IOException {
        final Reader pemReader = new StringReader(csr);
        final PEMParser pemParser = new PEMParser(pemReader);
        x500Name = ((PKCS10CertificationRequest) pemParser.readObject()).getSubject();
        this.csrBase64 = csr;
    }
	
    public String getCsrBase64() {
		return csrBase64;
	}

	public String get(final CSRObjectEnum field) {
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