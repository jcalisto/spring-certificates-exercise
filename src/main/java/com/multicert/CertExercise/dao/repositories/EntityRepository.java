package com.multicert.CertExercise.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.multicert.CertExercise.dao.models.EntityData;

@Repository
public interface EntityRepository  extends CrudRepository<EntityData, Long>{

}
