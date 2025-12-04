package com.orcas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.TEAM_DETAILS)
public class TeamDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = DbConstants.TEAM_NAME, nullable = false)
	private String teamName;
	
	@OneToMany(mappedBy = "teamDetails")
    private List<MatchDetails> matches = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.TEAM_ID, nullable = false)
	private Long teamId;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public void setMatches(List<MatchDetails> matches) {
		this.matches = matches;
	}
	
}