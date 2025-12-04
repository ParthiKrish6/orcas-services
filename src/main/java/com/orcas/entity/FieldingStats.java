package com.orcas.entity;

import java.io.Serializable;

public class FieldingStats implements Serializable {

	private static final long serialVersionUID = 1L;

	private String innings;

	private String player;

	private String catches;

	private String runOuts;

	private String dropped;

	private String saved;

	private String missed;

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

	public String getCatches() {
		return catches;
	}

	public void setCatches(String catches) {
		this.catches = catches;
	}

	public String getDropped() {
		return dropped;
	}

	public void setDropped(String dropped) {
		this.dropped = dropped;
	}

	public String getSaved() {
		return saved;
	}

	public void setSaved(String saved) {
		this.saved = saved;
	}

	public String getMissed() {
		return missed;
	}

	public void setMissed(String missed) {
		this.missed = missed;
	}

	public String getRunOuts() {
		return runOuts;
	}

	public void setRunOuts(String runOuts) {
		this.runOuts = runOuts;
	}

}