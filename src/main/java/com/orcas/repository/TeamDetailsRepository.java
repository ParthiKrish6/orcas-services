package com.orcas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.constants.DbConstants;
import com.orcas.entity.TeamDetails;

@Repository
public interface TeamDetailsRepository extends JpaRepository<TeamDetails, Long> {
	@Query(nativeQuery = true, value = "select * from " + DbConstants.TEAM_DETAILS + " s where s."
			+ DbConstants.TEAM_NAME + " = :teamName")
	TeamDetails getTeamFromName(@Param("teamName") String teamName);
}