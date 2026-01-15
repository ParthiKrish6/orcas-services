package com.orcas.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.orcas.entity.FieldingDetails;
import com.orcas.entity.FieldingStats;
import com.orcas.service.FieldingDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class FieldingDetailsController {
	@Autowired
	private FieldingDetailsService fieldingDetailsServices;

	@GetMapping(MappingConstants.URL_FIELDING_DETAILS)
	public List<FieldingDetails> getAllFieldingDetails() {
		return fieldingDetailsServices.getAllFieldingDetails();
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_DETAILS_PLAYER)
	public List<FieldingDetails> fetchAllByPlayerDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId) {
		return fieldingDetailsServices.fetchAllByPlayerDates(fromDate, toDate, playerId);
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_DETAILS_PLAYER_TEAM)
	public List<FieldingDetails> fetchAllByPlayerDatesTeam(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId,
			@PathVariable(value = AppConstants.ID_1) Long teamId) {
		return fieldingDetailsServices.fetchAllByPlayerDatesTeam(fromDate, toDate, playerId, teamId);
	}

	@GetMapping(MappingConstants.URL_FIELDING_DETAILS_ID)
	public ResponseEntity<FieldingDetails> getFieldingDetailsById(@PathVariable(value = AppConstants.ID) Long fieldingId) {
		FieldingDetails fieldingDetails = fieldingDetailsServices.getFieldingDetailsById(fieldingId);
		return ResponseEntity.ok().body(fieldingDetails);
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_DETAILS_MATCH_ID)
	public ResponseEntity<List<FieldingDetails>> getFieldingDetailsByMatchId(@PathVariable(value = AppConstants.ID) Long fieldingId) {
		List<FieldingDetails> fieldingDetails = fieldingDetailsServices.getFieldingDetailsByMatchId(fieldingId);
		return ResponseEntity.ok().body(fieldingDetails);
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_STATS)
	public List<FieldingStats> getAllFieldingStats() {
		return fieldingDetailsServices.getAllFieldingStats();
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_STATS_DATES)
	public List<FieldingStats> getFieldingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate) {
		return fieldingDetailsServices.getFieldingStatsByDates(fromDate, toDate);
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_STATS_TEAM)
	public List<FieldingStats> getFieldingStatsByTeam(@PathVariable(value = AppConstants.ID) Long teamId) {
		return fieldingDetailsServices.getFieldingStatsByTeam(teamId);
	}
	
	@GetMapping(MappingConstants.URL_FIELDING_STATS_DATES_TEAM)
	public List<FieldingStats> getFieldingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long teamId) {
		return fieldingDetailsServices.getFieldingStatsByDatesAndTeam(fromDate, toDate, teamId);
	}

	@PostMapping(MappingConstants.URL_FIELDING_DETAILS)
	public FieldingDetails createFieldingDetails(@Valid @RequestBody FieldingDetails fieldingDetails) {
		return fieldingDetailsServices.createFieldingDetails(fieldingDetails);
	}
	
	@PutMapping(MappingConstants.URL_FIELDING_DETAILS_ID)
	public ResponseEntity<FieldingDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody FieldingDetails reqFieldingDetails) {
		FieldingDetails fieldingDetails = fieldingDetailsServices.updateFieldingDetails(anniversary, reqFieldingDetails);
		return ResponseEntity.ok(fieldingDetails);
	}

	@DeleteMapping(MappingConstants.URL_FIELDING_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return fieldingDetailsServices.deleteFieldingDetails(anniversary);
	}

}