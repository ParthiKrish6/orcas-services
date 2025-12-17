package com.orcas.entity;

import java.io.Serializable;

public class BattingStats implements Serializable {

	private static final long serialVersionUID = 1L;

	private String innings;

	private String runs;
	
	private String balls;

	private String player;

	private String fours;

	private String sixes;

	private String strikeRate;

	private String notOut;

	private String average;

	private String timeSpent;
	
	private String playerId;
	
	private String matches;
	

	public String getMatches() {
		return matches;
	}

	public void setMatches(String matches) {
		this.matches = matches;
	}

	public String getInnings() {
		return innings;
	}

	public void setInnings(String innings) {
		this.innings = innings;
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

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
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

	public String getNotOut() {
		return notOut;
	}

	public void setNotOut(String notOut) {
		this.notOut = notOut;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

}