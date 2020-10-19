package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import com.multicert.CertExercise.dao.models.CertificateRequestData;


public interface IReqCertificateService {
	Optional<CertificateRequestData> findById(Long id);
	boolean deleteCertificateRequest(Long id);
}
