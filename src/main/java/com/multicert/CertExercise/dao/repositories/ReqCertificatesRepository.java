package com.multicert.CertExercise.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.multicert.CertExercise.dao.models.CertificateRequestData;

@Repository
public interface ReqCertificatesRepository extends CrudRepository<CertificateRequestData, Long>{

}
