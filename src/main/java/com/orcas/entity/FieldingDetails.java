package com.orcas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.FIELDING_DETAILS)
public class FieldingDetails implements Serializable {

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