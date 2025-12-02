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

	@Query(nativeQuery = true, value = "select * from batting_details b where b.match_id = :matchId")
	List<BattingDetails> findByMatchId(@Param("matchId") Long matchId);

	@Query(nativeQuery = true, value = "select * from batting_details  b where b.player_id = :playerId")
	List<BattingDetails> fetchAllByPlayer(@Param("playerId") Long playerId);

	@Query(nativeQuery = true, value = "select * from batting_details b "
			+ "join match_details m on m.match_id = b.match_id " + "join team_details t on t.team_id = m.team_id "
			+ "where t.team_id = :teamId and b.player_id = :playerId")
	List<BattingDetails> fetchAllByPlayerAndTeam(@Param("playerId") Long playerId, @Param("teamId") Long teamId);

	@Query(nativeQuery = true, value = "select * from batting_details  b "
			+ "join match_details m on m.match_id = b.match_id and m.match_date between :startDate and :endDate  "
			+ "where b.player_id = :playerId")
	List<BattingDetails> fetchAllByPlayerDates(@Param("playerId") Long playerId, @Param("startDate") LocalDate date,
			@Param("endDate") LocalDate date2);

	@Query(nativeQuery = true, value = "select * from batting_details b "
			+ "join match_details m on m.match_id = b.match_id and m.match_date between :startDate and :endDate "
			+ "join team_details t on t.team_id = m.team_id " + "where t.team_id = :teamId and b.player_id = :playerId")
	List<BattingDetails> fetchAllByPlayerAndTeamDates(@Param("playerId") Long playerId, @Param("teamId") Long teamId,
			@Param("startDate") LocalDate date, @Param("endDate") LocalDate date2);

}