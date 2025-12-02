package com.orcas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orcas.entity.CalendarDetails;

@Repository
public interface CalendarDetailsRepository extends JpaRepository<CalendarDetails, Long> {
	
}