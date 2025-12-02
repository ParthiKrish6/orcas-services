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
@Table(name = DbConstants.BOWLING_DETAILS)
public class BowlingDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.BOWLING_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = DbConstants.RUNS, nullable = false)
	private String runs;

	@Column(name = DbConstants.OVERS, nullable = false)
	private String overs;

	@Column(name = DbConstants.MAIDENS, nullable = false)
	private String maidens;

	@Column(name = DbConstants.FOURS, nullable = false)
	private String fours;

	@Column(name = DbConstants.SIXES, nullable = false)
	private String sixes;

	@Column(name = DbConstants.WICKETS, nullable = false)
	private String wickets;

	@Column(name = DbConstants.DOTS, nullable = false)
	private String dots;

	@Column(name = DbConstants.WIDES, nullable = false)
	private String wides;

	@Column(name = DbConstants.NO_BALLS, nullable = false)
	private String noballs;

	@Column(name = DbConstants.ECONOMY, nullable = false)
	private String economy;

	@ManyToOne
	@JoinColumn(name = DbConstants.MATCH_ID)
	private MatchDetails matchDetails;
	
	@OneToOne
	@JoinColumn(name = DbConstants.PLAYER_ID)
	private PlayerDetails playerDetails;

	public String getRuns() {
		return runs;
	}

	public void setRuns(String runs) {
		this.runs = runs;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOvers() {
		return overs;
	}

	public void setOvers(String overs) {
		this.overs = overs;
	}

	public String getMaidens() {
		return maidens;
	}

	public void setMaidens(String maidens) {
		this.maidens = maidens;
	}

	public String getWickets() {
		return wickets;
	}

	public void setWickets(String wickets) {
		this.wickets = wickets;
	}

	public String getDots() {
		return dots;
	}

	public void setDots(String dots) {
		this.dots = dots;
	}

	public String getWides() {
		return wides;
	}

	public void setWides(String wides) {
		this.wides = wides;
	}

	public String getNoballs() {
		return noballs;
	}

	public void setNoballs(String noballs) {
		this.noballs = noballs;
	}

	public String getEconomy() {
		return economy;
	}

	public void setEconomy(String economy) {
		this.economy = economy;
	}

	public MatchDetails getMatchDetails() {
		return matchDetails;
	}

	public void setMatchDetails(MatchDetails matchDetails) {
		this.matchDetails = matchDetails;
	}

	public PlayerDetails getPlayerDetails() {
		return playerDetails;
	}

	public void setPlayerDetails(PlayerDetails playerDetails) {
		this.playerDetails = playerDetails;
	}

}