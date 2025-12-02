package com.orcas.entity;

import java.io.Serializable;

public class BowlingStats implements Serializable {

	public BowlingStats() {
	}
	
	private static final long serialVersionUID = 1L;

	private String innings;

	private String player;

	private String runs;

	private String overs;

	private String maidens;

	private String fours;

	private String sixes;

	private String wickets;

	private String dots;

	private String wides;

	private String noBalls;

	private String economy;

	private String average;

	private String strikeRate;

	public String getInnings() {
		return innings;
	}

	public void setInnings(String innings) {
		this.innings = innings;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getRuns() {
		return runs;
	}

	public void setRuns(String runs) {
		this.runs = runs;
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

	public String getNoBalls() {
		return noBalls;
	}

	public void setNoBalls(String noBalls) {
		this.noBalls = noBalls;
	}

	public String getEconomy() {
		return economy;
	}

	public void setEconomy(String economy) {
		this.economy = economy;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(String strikeRate) {
		this.strikeRate = strikeRate;
	}


}
