package com.orcas.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orcas.constants.AppConstants;
import com.orcas.constants.MappingConstants;
import com.orcas.entity.CalendarDetails;
import com.orcas.service.CalendarDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class CalendarDetailsController {
	@Autowired
	private CalendarDetailsService calendarDetailsServices;

	@GetMapping(MappingConstants.URL_CALANDAR_DETAILS)
	public List<CalendarDetails> getAllCalendarDetails() {
		return calendarDetailsServices.getAllCalendarDetails();
	}

	@GetMapping(MappingConstants.URL_CALANDAR_DETAILS_ID)
	public ResponseEntity<CalendarDetails> getCalendarDetailsById(@PathVariable(value = AppConstants.ID) Long calendarId) {
		CalendarDetails calendarDetails = calendarDetailsServices.getCalendarDetailsById(calendarId);
		return ResponseEntity.ok().body(calendarDetails);
	}

	@PostMapping(MappingConstants.URL_CALANDAR_DETAILS)
	public CalendarDetails createCalendarDetails(@Valid @RequestBody CalendarDetails calendarDetails) {
		return calendarDetailsServices.createCalendarDetails(calendarDetails);
	}
	
	@PutMapping(MappingConstants.URL_CALANDAR_DETAILS_ID)
	public ResponseEntity<CalendarDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody CalendarDetails reqCalendarDetails) {
		CalendarDetails calendarDetails = calendarDetailsServices.updateCalendarDetails(anniversary, reqCalendarDetails);
		return ResponseEntity.ok(calendarDetails);
	}

	@DeleteMapping(MappingConstants.URL_CALANDAR_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return calendarDetailsServices.deleteCalendarDetails(anniversary);
	}


}
