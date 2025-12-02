package com.orcas.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import com.orcas.entity.TeamDetails;
import com.orcas.service.TeamDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class TeamDetailsController {
	@Autowired
	private TeamDetailsService teamDetailsServices;

	@GetMapping(MappingConstants.URL_TEAM_DETAILS)
	public List<TeamDetails> getAllTeamDetails() {
		return teamDetailsServices.getAllTeamDetails();
	}

	@GetMapping(MappingConstants.URL_TEAM_DETAILS_ID)
	public ResponseEntity<TeamDetails> getTeamDetailsById(@PathVariable(value = AppConstants.ID) Long teamId) {
		TeamDetails teamDetails = teamDetailsServices.getTeamDetailsById(teamId);
		return ResponseEntity.ok().body(teamDetails);
	}

	@PostMapping(MappingConstants.URL_TEAM_DETAILS)
	public TeamDetails createTeamDetails(@Valid @RequestBody TeamDetails teamDetails) {
		return teamDetailsServices.createTeamDetails(teamDetails);
	}
	
	@PutMapping(MappingConstants.URL_TEAM_DETAILS_ID)
	public ResponseEntity<TeamDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody TeamDetails reqTeamDetails) {
		TeamDetails teamDetails = teamDetailsServices.updateTeamDetails(anniversary, reqTeamDetails);
		return ResponseEntity.ok(teamDetails);
	}

	@DeleteMapping(MappingConstants.URL_TEAM_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long anniversary) {
		return teamDetailsServices.deleteTeamDetails(anniversary);
	}

}