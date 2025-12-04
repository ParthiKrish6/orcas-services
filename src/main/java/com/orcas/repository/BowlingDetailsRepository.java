package com.orcas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.entity.BowlingDetails;

@Repository
public interface BowlingDetailsRepository extends JpaRepository<BowlingDetails, Long> {

	@Query("select b from BowlingDetails b where b.matchDetails.matchId = :matchId")
	List<BowlingDetails> findByMatchId(@Param("matchId") Long matchId);

	@Query("select b from BowlingDetails  b where b.playerDetails.playerId = :playerId")
	List<BowlingDetails> fetchAllByPlayer(@Param("playerId") Long playerId);

	@Query("select b from BowlingDetails b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId " + "join TeamDetails t on t.teamId = m.teamDetails.teamId "
			+ "where t.teamId = :teamId and b.playerDetails.playerId = :playerId")
	List<BowlingDetails> fetchAllByPlayerAndTeam(@Param("playerId") Long playerId, @Param("teamId") Long teamId);

	@Query("select b from BowlingDetails  b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId and m.matchDate between :startDate and :endDate  "
			+ "where b.playerDetails.playerId = :playerId")
	List<BowlingDetails> fetchAllByPlayerDates(@Param("playerId") Long playerId, @Param("startDate") LocalDate date,
			@Param("endDate") LocalDate date2);

	@Query("select b from BowlingDetails b "
			+ "join MatchDetails m on m.matchId = b.matchDetails.matchId and m.matchDate between :startDate and :endDate "
			+ "join TeamDetails t on t.teamId = m.teamDetails.teamId " + "where t.teamId = :teamId and b.playerDetails.playerId = :playerId")
	List<BowlingDetails> fetchAllByPlayerAndTeamDates(@Param("playerId") Long playerId, @Param("teamId") Long teamId,
			@Param("startDate") LocalDate date, @Param("endDate") LocalDate date2);

}