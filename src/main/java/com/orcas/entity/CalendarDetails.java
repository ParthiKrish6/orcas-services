package com.orcas.entity;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.CALANDAR_DETAILS)
public class CalendarDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = DbConstants.CALANDAR_START_DATE, nullable = false)
	private Date startDate;

	@Column(name = DbConstants.CALANDAR_END_DATE, nullable = false)
	private Date endDate;
	
	@Id
	@Column(name = DbConstants.CALANDAR_ANNIVERSARY, nullable = false)
	private Long anniversary;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(Long anniversary) {
		this.anniversary = anniversary;
	}

}