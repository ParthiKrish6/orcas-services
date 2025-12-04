package com.orcas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.entity.BattingDetails;

@Repository
public interface BattingDetailsRepository extends JpaRepository<BattingDetails, Long> {

	@Query("select b from BattingDetails b where b.matchDetails.matchId = :matchId")
	List<BattingDetails> findByMatchId(@Param("matchId") Long matchId);

	@Query("select b from BattingDetails  b where b.playerDetails.playerId = :playerId")
	List<BattingDetails> fetchAllByPlayer(@Param("playerId") Long playerId);

	@Query("select b from BattingDetails b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId " + "join TeamDetails t on t.teamId = m.teamDetails.teamId "
			+ "where t.teamId = :teamId and b.playerDetails.playerId = :playerId")
	List<BattingDetails> fetchAllByPlayerAndTeam(@Param("playerId") Long playerId, @Param("teamId") Long teamId);

	@Query("select b from BattingDetails  b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId and m.matchDate between :startDate and :endDate  "
			+ "where b.playerDetails.playerId = :playerId")
	List<BattingDetails> fetchAllByPlayerDates(@Param("playerId") Long playerId, @Param("startDate") LocalDate date,
			@Param("endDate") LocalDate date2);

	@Query("select b from BattingDetails b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId and m.matchDate between :startDate and :endDate "
			+ "join TeamDetails t on t.teamId = m.teamDetails.teamId " + "where t.teamId = :teamId and b.playerDetails.playerId = :playerId")
	List<BattingDetails> fetchAllByPlayerAndTeamDates(@Param("playerId") Long playerId, @Param("teamId") Long teamId,
			@Param("startDate") LocalDate date, @Param("endDate") LocalDate date2);

}