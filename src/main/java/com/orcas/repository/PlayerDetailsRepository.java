package com.orcas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.entity.PlayerDetails;

@Repository
public interface PlayerDetailsRepository extends JpaRepository<PlayerDetails, Long> {

	@Query("select s from PlayerDetails s where s.playerName = :playerName OR s.nickName = :playerName")
	PlayerDetails getPlayerFromName(@Param("playerName") String playerName);

	@Query("select s  from PlayerDetails s where s.playerName = :playerName OR s.nickName = :playerName OR s.playerName = :nickName OR s.nickName = :nickName ")
	PlayerDetails getPlayerFromNameOrNickName(@Param("playerName") String playerName,
			@Param("nickName") String nickName);
}