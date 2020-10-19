package com.multicert.CertExercise.dao.repositories;

import org.springframework.data.repository.CrudRepository;

import com.multicert.CertExercise.dao.models.CertificateData;


public interface SignedCertificatesRepository extends CrudRepository<CertificateData, Long> {

}
