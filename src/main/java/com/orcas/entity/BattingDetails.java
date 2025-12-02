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

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.BATTING_DETAILS)
public class BattingDetails implements Serializable {

	public BattingDetails() {  
    }
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.BATTING_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = DbConstants.RUNS, nullable = false)
	private String runs;

	@Column(name = DbConstants.BALLS, nullable = false)
	private String balls;

	@Column(name = DbConstants.TIME_SPENT, nullable = false)
	private String timeSpent;

	@Column(name = DbConstants.FOURS, nullable = false)
	private String fours;

	@Column(name = DbConstants.SIXES, nullable = false)
	private String sixes;

	@Column(name = DbConstants.STRIKE_RATE, nullable = false)
	private String strikeRate;
	
	@Column(name = DbConstants.NOT_OUT, nullable = false)
	@ColumnDefault("'N'")
	private String notOut;

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

	public String getRuns() {
		return runs;
	}

	public void setRuns(String runs) {
		this.runs = runs;
	}

	public String getBalls() {
		return balls;
	}

	public void setBalls(String balls) {
		this.balls = balls;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getFours() {
		return fours;
	}

	public void setFours(String fours) {
		this.fours = fours;
	}

	public String getSixes() {
		return sixes;
	}

	public void setSixes(String sixes) {
		this.sixes = sixes;
	}

	public String getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(String strikeRate) {
		this.strikeRate = strikeRate;
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

	public String getNotOut() {
		return notOut;
	}

	public void setNotOut(String notOut) {
		this.notOut = notOut;
	}
	

}
