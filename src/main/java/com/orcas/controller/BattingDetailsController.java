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
import com.orcas.entity.BattingStats;
import com.orcas.service.BattingDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class BattingDetailsController {
	@Autowired
	private BattingDetailsService battingDetailsServices;

	@GetMapping(MappingConstants.URL_BATTING_DETAILS)
	public List<BattingDetails> getAllBattingDetails() {
		return battingDetailsServices.getAllBattingDetails();
	}
	
	@GetMapping(MappingConstants.URL_BATTING_DETAILS_PLAYER)
	public List<BattingDetails> fetchAllByPlayerDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId) {
		return battingDetailsServices.fetchAllByPlayerDates(fromDate, toDate, playerId);
	}
	
	@GetMapping(MappingConstants.URL_BATTING_DETAILS_PLAYER_TEAM)
	public List<BattingDetails> fetchAllByPlayerDatesTeam(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long playerId,
			@PathVariable(value = AppConstants.ID_1) Long teamId) {
		return battingDetailsServices.fetchAllByPlayerDatesTeam(fromDate, toDate, playerId, teamId);
	}
	
	
	@GetMapping(MappingConstants.URL_BATTING_STATS)
	public List<BattingStats> getAllBattingStats() {
		return battingDetailsServices.getAllBattingStats();
	}
	
	@GetMapping(MappingConstants.URL_BATTING_STATS_DATES)
	public List<BattingStats> getBattingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate) {
		return battingDetailsServices.getBattingStatsByDates(fromDate, toDate);
	}
	
	@GetMapping(MappingConstants.URL_BATTING_STATS_TEAM)
	public List<BattingStats> getBattingStatsByTeam(@PathVariable(value = AppConstants.ID) Long teamId) {
		return battingDetailsServices.getBattingStatsByTeam(teamId);
	}
	
	@GetMapping(MappingConstants.URL_BATTING_STATS_DATES_TEAM)
	public List<BattingStats> getBattingStatsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate,
			@PathVariable(value = AppConstants.ID) Long teamId) {
		return battingDetailsServices.getBattingStatsByDatesAndTeam(fromDate, toDate, teamId);
	}

	@GetMapping(MappingConstants.URL_BATTING_DETAILS_ID)
	public ResponseEntity<BattingDetails> getBattingDetailsById(@PathVariable(value = AppConstants.ID) Long battingId) {
		BattingDetails battingDetails = battingDetailsServices.getBattingDetailsById(battingId);
		return ResponseEntity.ok().body(battingDetails);
	}
	
	@GetMapping(MappingConstants.URL_BATTING_DETAILS_MATCH_ID)
	public ResponseEntity<List<BattingDetails>> getBattingDetailsByMatchId(@PathVariable(value = AppConstants.ID) Long battingId) {
		List<BattingDetails> battingDetails = battingDetailsServices.getBattingDetailsByMatchId(battingId);
		return ResponseEntity.ok().body(battingDetails);
	}

	@PostMapping(MappingConstants.URL_BATTING_DETAILS)
	public BattingDetails createBattingDetails(@Valid @RequestBody BattingDetails battingDetails) {
		return battingDetailsServices.createBattingDetails(battingDetails);
	}
	
	@PutMapping(MappingConstants.URL_BATTING_DETAILS_ID)
	public ResponseEntity<BattingDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody BattingDetails reqBattingDetails) {
		BattingDetails battingDetails = battingDetailsServices.updateBattingDetails(anniversary, reqBattingDetails);
		return ResponseEntity.ok(battingDetails);
	}

	@DeleteMapping(MappingConstants.URL_BATTING_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return battingDetailsServices.deleteBattingDetails(anniversary);
	}

}