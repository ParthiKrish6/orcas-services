package com.orcas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.CalendarDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.CalendarDetailsRepository;

@Transactional
@Service
public class CalendarDetailsService {
	@Autowired
	private CalendarDetailsRepository calendarDetailsRepository;

	private String NOT_FOUND = "CalendarDetails not found for this id :: ";

	public List<CalendarDetails> getAllCalendarDetails() {
		return calendarDetailsRepository.findAll();
	}

	public CalendarDetails getCalendarDetailsById(Long calendarId) {
		CalendarDetails calendarDetails = null;
		try {
			calendarDetails = calendarDetailsRepository.findById(calendarId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + calendarId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendarDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public CalendarDetails createCalendarDetails(CalendarDetails calendarDetails) {
		calendarDetails = calendarDetailsRepository.save(calendarDetails);
		return calendarDetails;
	}
	@Transactional(rollbackFor = Exception.class)
	public CalendarDetails updateCalendarDetails(Long anniversary, CalendarDetails reqCalendarDetails) {
		CalendarDetails calendarDetails = null;
		CalendarDetails updateCalendarDetails = null;
		try {
			calendarDetails = calendarDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			calendarDetails.setStartDate(reqCalendarDetails.getStartDate());
			calendarDetails.setEndDate(reqCalendarDetails.getEndDate());
			updateCalendarDetails = calendarDetailsRepository.save(calendarDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateCalendarDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteCalendarDetails(Long anniversary) {
		CalendarDetails calendarDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			calendarDetails = calendarDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			calendarDetailsRepository.delete(calendarDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
