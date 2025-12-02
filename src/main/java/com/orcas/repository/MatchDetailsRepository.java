package com.orcas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.orcas.entity.MatchDetails;
import com.orcas.constants.DbConstants;

@Repository
public interface MatchDetailsRepository extends JpaRepository<MatchDetails, Long> {
	
	@Query(nativeQuery = true, value = "select * from " + DbConstants.MATCH_DETAILS + " s where s."
			+ DbConstants.MATCH_DATE + " between :startDate and :endDate")
	List<MatchDetails> getData_between(@Param("startDate") LocalDate date, @Param("endDate") LocalDate date2);
	
}