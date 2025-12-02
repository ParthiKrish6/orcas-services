package com.orcas.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.FIELDING_DETAILS)
public class FieldingDetails implements Serializable {

	public FieldingDetails() {
	}
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.FIELDING_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = DbConstants.CATCHES, nullable = false)
	private String catches;

	@Column(name = DbConstants.CATCHES_DROPPED, nullable = false)
	private String catchesDropped;

	@Column(name = DbConstants.RUN_OUTS, nullable = false)
	private String runOuts;

	@Column(name = DbConstants.RUNS_SAVED, nullable = false)
	private String runsSaved;

	@Column(name = DbConstants.RUNS_MISSED, nullable = false)
	private String runsMissed;

	@ManyToOne
	@JoinColumn(name = DbConstants.MATCH_ID)
	private MatchDetails matchDetails;

	@OneToOne
	@JoinColumn(name = DbConstants.PLAYER_ID)
	private PlayerDetails playerDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatches() {
		return catches;
	}

	public void setCatches(String catches) {
		this.catches = catches;
	}

	public String getCatchesDropped() {
		return catchesDropped;
	}

	public void setCatchesDropped(String catchesDropped) {
		this.catchesDropped = catchesDropped;
	}

	public String getRunOuts() {
		return runOuts;
	}

	public void setRunOuts(String runOuts) {
		this.runOuts = runOuts;
	}

	public String getRunsSaved() {
		return runsSaved;
	}

	public void setRunsSaved(String runsSaved) {
		this.runsSaved = runsSaved;
	}

	public String getRunsMissed() {
		return runsMissed;
	}

	public void setRunsMissed(String runsMissed) {
		this.runsMissed = runsMissed;
	}

	public MatchDetails getMatchDetails() {
		return matchDetails;
	}

	public void setMatchDetails(MatchDetails matchDetails) {
		this.matchDetails = matchDetails;
	}

	public void setPlayerDetails(PlayerDetails playerDetails) {
		this.playerDetails = playerDetails;
	}

	public PlayerDetails getPlayerDetails() {
		return playerDetails;
	}


}

