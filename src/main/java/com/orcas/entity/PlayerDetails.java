package com.orcas.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.PLAYER_DETAILS)
public class PlayerDetails implements Serializable {

	public PlayerDetails() {
	}
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.PLAYER_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = DbConstants.PLAYER_NAME, nullable = false)
	private String playerName;

	@Column(name = DbConstants.NICK_NAME, nullable = true)
	private String nickName;

	@OneToOne
	@JoinColumn(name = DbConstants.BATTING_ID)
	private BattingDetails battingDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public BattingDetails getBattingDetails() {
		return battingDetails;
	}

	public void setBattingDetails(BattingDetails battingDetails) {
		this.battingDetails = battingDetails;
	}



}

