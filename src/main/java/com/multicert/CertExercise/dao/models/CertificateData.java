package com.multicert.CertExercise.dao.models;

import javax.persistence.*;

@Entity
@Table(name = "certificates", schema = "public")
public class CertificateData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "certificate_id")
	private EntityData entity;
	
	private String subjectDN;
	private String serialNumber;
	private String certificate;
	
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
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
}
