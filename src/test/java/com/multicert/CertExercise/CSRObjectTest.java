package com.multicert.CertExercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import com.multicert.CertExercise.bsl.crypto.CSRObject;
import com.multicert.CertExercise.bsl.crypto.CSRObject.CSRObjectEnum;

public class CSRObjectTest {
	
	@Test
	public void testLoadedFields() throws IOException {
		File testFile = ResourceUtils.getFile("classpath:testdata/testcsr.csr");
		String csrBase64 = new String(Base64.encodeBase64(Files.readAllBytes(testFile.toPath())), StandardCharsets.US_ASCII);
		
		CSRObject csrObject = new CSRObject(csrBase64);

		assertEquals(csrObject.get(CSRObjectEnum.ORGANIZATION), "Mcert");
		assertEquals(csrObject.get(CSRObjectEnum.ORGANIZATION_UNIT), "mTr");
		assertEquals(csrObject.get(CSRObjectEnum.LOCALE), "LX");
		assertEquals(csrObject.get(CSRObjectEnum.STATE), "LX");
		assertEquals(csrObject.get(CSRObjectEnum.COMMON_NAME), "Joao");
	}
	
}
