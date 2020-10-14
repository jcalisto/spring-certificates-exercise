package com.multicert.CertExercise.bsl.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	

}
