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
import com.orcas.entity.MatchDetails;
import com.orcas.service.MatchDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class MatchDetailsController {
	@Autowired
	private MatchDetailsService matchDetailsServices;

	@GetMapping(MappingConstants.URL_MATCH_DETAILS)
	public List<MatchDetails> getAllMatchDetails() {
		return matchDetailsServices.getAllMatchDetails();
	}

	@GetMapping(MappingConstants.URL_MATCH_DETAILS_ID)
	public ResponseEntity<MatchDetails> getMatchDetailsById(@PathVariable(value = AppConstants.ID) Long matchId) {
		MatchDetails matchDetails = matchDetailsServices.getMatchDetailsById(matchId);
		return ResponseEntity.ok().body(matchDetails);
	}
	
	@GetMapping(MappingConstants.URL_MATCH_DETAILS_DATES)
	public List<MatchDetails> getMatchDetailsByDates(
			@PathVariable(value = AppConstants.FROM_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate fromDate,
			@PathVariable(value = AppConstants.TO_DATE) @DateTimeFormat(pattern = AppConstants.DATE_FORMAT_YYYY_MM_DD) LocalDate toDate) {
		return matchDetailsServices.getMatchDetailsByDates(fromDate, toDate);
	}

	@PostMapping(MappingConstants.URL_MATCH_DETAILS)
	public MatchDetails createMatchDetails(@Valid @RequestBody MatchDetails matchDetails) {
		return matchDetailsServices.createMatchDetails(matchDetails);
	}

	@PutMapping(MappingConstants.URL_MATCH_DETAILS_ID)
	public ResponseEntity<MatchDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody MatchDetails reqMatchDetails) {
		MatchDetails matchDetails = matchDetailsServices.updateMatchDetails(anniversary, reqMatchDetails);
		return ResponseEntity.ok(matchDetails);
	}

	@DeleteMapping(MappingConstants.URL_MATCH_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return matchDetailsServices.deleteMatchDetails(anniversary);
	}

}