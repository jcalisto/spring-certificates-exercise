package com.multicert.CertExercise.bsl.services;

import java.util.List;
import java.util.Optional;

import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;

public interface IEntityService {
	
	List<EntityData> listEntities();
	EntityData addEntity(String name, String nif, String countryCode, String entityType, String companyName);
	Optional<EntityData> getEntityById(Long id);
	List<CertificateRequestData> getReqCertificatesForEntity(Long id); 
	List<CertificateData> getSignedCertificatesForEntity(Long id); 
}
