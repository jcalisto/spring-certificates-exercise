package com.multicert.CertExercise.dao.models;

import javax.validation.constraints.*;
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
	
	private String companyName;
	private String countryCode;
	private String entityType;
	
	//TODO certificate request list
	//TODO certificate list
	
	public EntityData() {
		
	}
	
	public EntityData(String name, String nif, String companyName, String countryCode, String entityType) {
		this.name = name;
		this.nif = nif;
		this.companyName = companyName;
		this.countryCode = countryCode;
		this.entityType = entityType;
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
