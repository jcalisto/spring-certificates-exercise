package com.multicert.CertExercise.dao.models;

import javax.validation.constraints.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "entities", schema = "public")
public class EntityData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotBlank(message = "NIF is mandatory")
	@Pattern(regexp="\\d{9}")
	private String nif;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "country_code")
	private String countryCode;
	
	@Column(name = "entity_type")
	private String entityType;
	
	@OneToMany(mappedBy="entity", cascade=CascadeType.ALL)
	private Set<CertificateRequestData> certificate_requests = new HashSet<CertificateRequestData>();
	
	@OneToMany(mappedBy="entity", cascade=CascadeType.ALL)
	private Set<CertificateData> certificates = new HashSet<CertificateData>();
	
	public EntityData() {
		
	}
	
	public EntityData(String name, String nif, String countryCode, String entityType, String companyName) {
		this.name = name;
		this.nif = nif;
		this.companyName = companyName;
		this.countryCode = countryCode;
		this.entityType = entityType;
		System.out.println("Entity constructor: " + name + ", " + nif + " , " + countryCode + " , " + companyName + ", " + entityType);
	}
	
	public Set<CertificateRequestData> getRequestedCertificates(){
		return Collections.unmodifiableSet(this.certificate_requests);
	}
	
	public Set<CertificateData> getSignedCertificates(){
		return Collections.unmodifiableSet(this.certificates);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Long getId() {
		return id;
	}

	
	
	
}
