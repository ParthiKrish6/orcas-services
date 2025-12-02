package com.orcas.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orcas.constants.DbConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = DbConstants.MATCH_DETAILS)
public class MatchDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DbConstants.MATCH_ID, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long matchId;

	@Column(name = DbConstants.MATCH_DATE, nullable = false)
	private LocalDate matchDate;

	@Column(name = DbConstants.MATCH_OPPONENT, nullable = false)
	private String opponent;

	@Column(name = DbConstants.MATCH_TEAM_SCORE, nullable = false)
	private String teamScore;

	@Column(name = DbConstants.MATCH_OPPONENT_SCORE, nullable = false)
	private String opponentScore;

	@Column(name = DbConstants.MATCH_RESULT, nullable = false)
	private String matchResult;

	@Column(name = DbConstants.MARGIN, nullable = false)
	private String margin;
	
	@Column(name = DbConstants.BAT_FIRST, nullable = false)
	@ColumnDefault("'N'")
	private String batFirst;

	@Column(name = DbConstants.CAPTAIN, nullable = true)
	private String captain;

	@Column(name = DbConstants.VICE_CAPTAIN, nullable = true)
	private String viceCaptain;

	@ManyToOne
	@JoinColumn(name = DbConstants.TEAM_ID)
	private TeamDetails teamDetails;

	@OneToMany(mappedBy = "matchDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BattingDetails> battingDetails;
	
	@OneToMany(mappedBy = "matchDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BowlingDetails> bowlingDetails;
	
	@OneToMany(mappedBy = "matchDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FieldingDetails> fieldingDetails;

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public LocalDate getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDate matchDate) {
		this.matchDate = matchDate;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public String getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(String teamScore) {
		this.teamScore = teamScore;
	}

	public String getOpponentScore() {
		return opponentScore;
	}

	public void setOpponentScore(String opponentScore) {
		this.opponentScore = opponentScore;
	}

	public String getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}
	
	public String getBatFirst() {
		return batFirst;
	}

	public void setBatFirst(String batFirst) {
		this.batFirst = batFirst;
	}

	public String getCaptain() {
		return captain;
	}

	public void setCaptain(String captain) {
		this.captain = captain;
	}

	public String getViceCaptain() {
		return viceCaptain;
	}

	public void setViceCaptain(String viceCaptain) {
		this.viceCaptain = viceCaptain;
	}

	public void setTeamDetails(TeamDetails teamDetails) {
		this.teamDetails = teamDetails;
	}
	
	public TeamDetails getTeamDetails() {
		return teamDetails;
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