package com.orcas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.constants.DbConstants;
import com.orcas.entity.PlayerDetails;

@Repository
public interface PlayerDetailsRepository extends JpaRepository<PlayerDetails, Long> {

	@Query(nativeQuery = true, value = "select * from " + DbConstants.PLAYER_DETAILS + " s where s."
			+ DbConstants.PLAYER_NAME + " = :playerName OR s." + DbConstants.NICK_NAME + " = :playerName")
	PlayerDetails getPlayerFromName(@Param("playerName") String playerName);

	@Query(nativeQuery = true, value = "select * from " + DbConstants.PLAYER_DETAILS + " s where s."
			+ DbConstants.PLAYER_NAME + " = :playerName OR s." + DbConstants.NICK_NAME + " = :playerName OR s."
			+ DbConstants.NICK_NAME + " = :nickName OR s." + DbConstants.PLAYER_NAME + " = :nickName ")
	PlayerDetails getPlayerFromNameOrNickName(@Param("playerName") String playerName,
			@Param("nickName") String nickName);
}