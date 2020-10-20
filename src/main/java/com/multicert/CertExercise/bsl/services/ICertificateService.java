package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;

public interface ICertificateService {
	Optional<CertificateRequestData> findCertificateRequestById(Long id);
	void deleteCertificateRequest(Long id);
	Optional<CertificateData> findCertificateSignedById(Long id);
}
