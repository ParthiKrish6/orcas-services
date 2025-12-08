package com.orcas.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.HEALTH_CHECK)
public class HealthCheck implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}