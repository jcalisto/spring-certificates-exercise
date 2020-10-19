package com.multicert.CertExercise.bsl.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multicert.CertExercise.dao.models.CertificateData;
import com.multicert.CertExercise.dao.models.CertificateRequestData;
import com.multicert.CertExercise.dao.models.EntityData;
import com.multicert.CertExercise.dao.repositories.EntityRepository;

@Service
public class EntityService implements IEntityService{
	
	@Autowired
	private EntityRepository entityRepository;
	
	public List<EntityData> listEntities() {
		return (List<EntityData>) entityRepository.findAll();
	}

	
	public EntityData addEntity(String name, String nif, String countryCode, String entityType, String companyName) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(nif);
		Objects.requireNonNull(countryCode);
		Objects.requireNonNull(entityType);
		Objects.requireNonNull(companyName);
		EntityData entity = new EntityData(name, nif, countryCode, entityType, companyName);
		return entityRepository.save(entity);
	}


	@Override
	public Optional<EntityData> getEntityById(Long id) {
		return entityRepository.findById(id);
	}
	
	


	@Override
	public EntityData addCertificateRequest(Long entityId, String subjectDN, String serialNumber,
			String csrBase64) {
		Optional<EntityData> entity = getEntityById(entityId);
		if(entity.isPresent()) {
			CertificateRequestData crd = new CertificateRequestData(entity.get(), subjectDN, serialNumber, csrBase64);
			entity.get().addCertificateRequest(crd);
			return entityRepository.save(entity.get());
		}
		return null;
	}
	
	@Override
	public EntityData addSignedCertificate(Long entityId, String subjectDN, String serialNumber,
			String certificate) {
		Optional<EntityData> entity = getEntityById(entityId);
		if(entity.isPresent()) {
			System.out.println("Adding signed certificate to entity");
			CertificateData crd = new CertificateData(entity.get(), subjectDN, serialNumber, certificate);
			System.out.println("Creating certData: " + crd.getSubjectDN());
			entity.get().addSignedCertificate(crd);
			return entityRepository.save(entity.get());
		}
		return null;
	}
	

	@Override
	public List<CertificateRequestData> getReqCertificatesForEntity(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CertificateData> getSignedCertificatesForEntity(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	

}
