package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.repositories.SignedCertificatesRepository;

@Service
public class SignedCertificateService implements ISignedCertificateService {
	
	@Autowired
	private SignedCertificatesRepository signedCertificatesRepository;
	
	@Override
	public Optional<CertificateData> findById(Long id) {
		return signedCertificatesRepository.findById(id);
	}
	
}
