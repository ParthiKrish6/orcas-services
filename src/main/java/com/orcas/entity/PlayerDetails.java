package com.orcas.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.PLAYER_DETAILS)
public class PlayerDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.PLAYER_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long playerId;

	@Column(name = DbConstants.PLAYER_NAME, nullable = false)
	private String playerName;

	@Column(name = DbConstants.NICK_NAME, nullable = true)
	private String nickName;

	@OneToMany(mappedBy = "playerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BattingDetails> battingDetails;

	@OneToMany(mappedBy = "playerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BowlingDetails> bowlingDetails;

	@OneToMany(mappedBy = "playerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FieldingDetails> fieldingDetails;

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
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
	
	public void setBattingDetails(List<BattingDetails> battingDetails) {
		this.battingDetails = battingDetails;
	}

	public void setBowlingDetails(List<BowlingDetails> bowlingDetails) {
		this.bowlingDetails = bowlingDetails;
	}

	public void setFieldingDetails(List<FieldingDetails> fieldingDetails) {
		this.fieldingDetails = fieldingDetails;
	}

}