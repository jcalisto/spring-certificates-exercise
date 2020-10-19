package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import com.multicert.CertExercise.dao.models.CertificateData;

public interface ISignedCertificateService {
	Optional<CertificateData> findById(Long id);
}
