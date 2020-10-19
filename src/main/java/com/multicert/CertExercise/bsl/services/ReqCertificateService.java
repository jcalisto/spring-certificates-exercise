package com.multicert.CertExercise.bsl.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.repositories.ReqCertificatesRepository;

@Service
public class ReqCertificateService implements IReqCertificateService {

	@Autowired
	private ReqCertificatesRepository reqCertificatesRepository;
	
	@Override
	public Optional<CertificateRequestData> findById(Long id) {
		return reqCertificatesRepository.findById(id);
	}

	@Override
	public boolean deleteCertificateRequest(Long id) {
		reqCertificatesRepository.deleteById(id);
		return false;
	}
	
	

}
