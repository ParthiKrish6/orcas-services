package com.orcas.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.LOGIN_DETAILS)
public class LoginDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.USER, nullable = false)
	private String userId;

	@Column(name = DbConstants.PWD, nullable = false)
	private String pwd;

	@Column(name = DbConstants.TYPE, nullable = false)
	private String type;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}