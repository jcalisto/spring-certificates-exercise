package com.multicert.CertExercise.bsl.services;

import java.util.List;

import com.multicert.CertExercise.dao.models.EntityData;

public interface IEntityService {
	
	List<EntityData> listEntities();
	EntityData addEntity(String name, String nif, String countryCode, String entityType, String companyName);
}
