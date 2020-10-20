package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.repositories.ReqCertificatesRepository;
import com.multicert.CertExercise.dao.repositories.SignedCertificatesRepository;

@Service
public class CertificateService implements ICertificateService{
	
	@Autowired
	private ReqCertificatesRepository reqCertificatesRepository;
	
	@Autowired
	private SignedCertificatesRepository signedCertificatesRepository;
	
	
	@Override
	public Optional<CertificateRequestData> findCertificateRequestById(Long id) {
		return reqCertificatesRepository.findById(id);
	}

	@Override
	public void deleteCertificateRequest(Long id) {
		reqCertificatesRepository.deleteById(id);
	}

	@Override
	public Optional<CertificateData> findCertificateSignedById(Long id) {
		return signedCertificatesRepository.findById(id);
	}

}
