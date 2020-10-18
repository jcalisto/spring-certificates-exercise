package com.multicert.CertExercise.dao.models;

import javax.persistence.*;

@Entity
@Table(name = "certificate_requests", schema = "public")
public class CertificateRequestData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "entity")
	private EntityData entity;
	
	private String subjectDN;
	private String serialNumber;
	
	@Column(name = "csrbase64")
	private String csrBase64;
	
	public CertificateRequestData() {

	}

	public CertificateRequestData(EntityData entity, String subjectDN, String serialNumber, String csrBase64) {
		this.entity = entity;
		this.subjectDN = subjectDN;
		this.serialNumber = serialNumber;
		this.csrBase64 = csrBase64;
	}
	
	public Long getId() {
		return id;
	}
	public EntityData getEntity() {
		return entity;
	}
	public String getSubjectDN() {
		return subjectDN;
	}
	public void setSubjectDN(String subjectDN) {
		this.subjectDN = subjectDN;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getCertificate() {
		return csrBase64;
	}
	public void setCertificate(String certificate) {
		this.csrBase64 = certificate;
	}
	
	

	
	
	
	
}
