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
import com.orcas.entity.BattingDetails;
import com.orcas.entity.BowlingDetails;
import com.orcas.entity.BowlingStats;
import com.orcas.service.BowlingDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class BowlingDetailsController {
	@Autowired
	private BowlingDetailsService bowlingDetailsServices;

	@GetMapping(MappingConstants.URL_BOWLING_DETAILS)
	public List<BowlingDetails> getAllBowlingDetails() {
		return bowlingDetailsServices.getAllBowlingDetails();
	}

	@GetMapping(MappingConstants.URL_BOWLING_DETAILS_ID)
	public ResponseEntity<BowlingDetails> getBowlingDetailsById(@PathVariable(value = AppConstants.ID) Long bowlingId) {
		BowlingDetails bowlingDetails = bowlingDetailsServices.getBowlingDetailsById(bowlingId);
		return ResponseEntity.ok().body(bowlingDetails);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_DETAILS_PLAYER)
	public List<BowlingDetails> fetchAllByPlayerDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId) {
		return bowlingDetailsServices.fetchAllByPlayerDates(fromDate, toDate, playerId);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_DETAILS_PLAYER_TEAM)
	public List<BowlingDetails> fetchAllByPlayerDatesTeam(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId,
			@PathVariable(value = AppConstants.ID_1) Long teamId) {
		return bowlingDetailsServices.fetchAllByPlayerDatesTeam(fromDate, toDate, playerId, teamId);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_STATS)
	public List<BowlingStats> getAllBowlingStats() {
		return bowlingDetailsServices.getAllBowlingStats();
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_STATS_DATES)
	public List<BowlingStats> getBowlingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate) {
		return bowlingDetailsServices.getBowlingStatsByDates(fromDate, toDate);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_STATS_TEAM)
	public List<BowlingStats> getBowlingStatsByTeam(@PathVariable(value = AppConstants.ID) Long teamId) {
		return bowlingDetailsServices.getBowlingStatsByTeam(teamId);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_STATS_DATES_TEAM)
	public List<BowlingStats> getBowlingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long teamId) {
		return bowlingDetailsServices.getBowlingStatsByDatesAndTeam(fromDate, toDate, teamId);
		
	}

	@PostMapping(MappingConstants.URL_BOWLING_DETAILS)
	public BowlingDetails createBowlingDetails(@Valid @RequestBody BowlingDetails bowlingDetails) {
		return bowlingDetailsServices.createBowlingDetails(bowlingDetails);
	}
	
	@GetMapping(MappingConstants.URL_BOWLING_DETAILS_MATCH_ID)
	public ResponseEntity<List<BowlingDetails>> getBowlingDetailsByMatchId(@PathVariable(value = AppConstants.ID) Long bowlingId) {
		List<BowlingDetails> bowlingDetails = bowlingDetailsServices.getBowlingDetailsByMatchId(bowlingId);
		return ResponseEntity.ok().body(bowlingDetails);
	}

	
	@PutMapping(MappingConstants.URL_BOWLING_DETAILS_ID)
	public ResponseEntity<BowlingDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody BowlingDetails reqBowlingDetails) {
		BowlingDetails bowlingDetails = bowlingDetailsServices.updateBowlingDetails(anniversary, reqBowlingDetails);
		return ResponseEntity.ok(bowlingDetails);
	}

	@DeleteMapping(MappingConstants.URL_BOWLING_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return bowlingDetailsServices.deleteBowlingDetails(anniversary);
	}

}