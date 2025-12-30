package com.orcas.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.CalendarDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.CalendarDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class CalendarDetailsService {
	@Autowired
	private CalendarDetailsRepository calendarDetailsRepository;
	
	@Autowired
	CacheAdminService cacheAdminService;

	private String NOT_FOUND = "CalendarDetails not found for this id :: ";

	@Cacheable(value = CacheNames.ALL_CALENDAR_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<CalendarDetails> getAllCalendarDetails() {
		return calendarDetailsRepository.findAll();
	}

	@Cacheable(value = CacheNames.CALENDAR_DETAILS_BY_ID, key = "#calendarId.toString()", unless = "#result == null")
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
		cacheAdminService.clearCache(Arrays.asList(CacheNames.CALENDAR_DETAILS_BY_ID, CacheNames.ALL_CALENDAR_DETAILS));
		return calendarDetails;
	}
	@Transactional(rollbackFor = Exception.class)
	public CalendarDetails updateCalendarDetails(Long calendarId, CalendarDetails reqCalendarDetails) {
		CalendarDetails calendarDetails = null;
		CalendarDetails updateCalendarDetails = null;
		try {
			calendarDetails = getCalendarDetailsById(calendarId);
			calendarDetails.setStartDate(reqCalendarDetails.getStartDate());
			calendarDetails.setEndDate(reqCalendarDetails.getEndDate());
			updateCalendarDetails = calendarDetailsRepository.save(calendarDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.CALENDAR_DETAILS_BY_ID, CacheNames.ALL_CALENDAR_DETAILS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateCalendarDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteCalendarDetails(Long calendarId) {
		CalendarDetails calendarDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			calendarDetails = getCalendarDetailsById(calendarId);
			calendarDetailsRepository.delete(calendarDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.CALENDAR_DETAILS_BY_ID, CacheNames.ALL_CALENDAR_DETAILS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
