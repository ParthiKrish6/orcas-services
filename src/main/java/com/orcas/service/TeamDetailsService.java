package com.orcas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.TeamDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.TeamDetailsRepository;

@Transactional
@Service
public class TeamDetailsService {
	@Autowired
	private TeamDetailsRepository teamDetailsRepository;

	private String NOT_FOUND = "TeamDetails not found for this id :: ";

	public List<TeamDetails> getAllTeamDetails() {
		return teamDetailsRepository.findAll();
	}

	public TeamDetails getTeamDetailsById(Long teamId) {
		TeamDetails teamDetails = null;
		try {
			teamDetails = teamDetailsRepository.findById(teamId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + teamId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teamDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public TeamDetails createTeamDetails(TeamDetails teamDetails) {
		teamDetails = teamDetailsRepository.save(teamDetails);
		return teamDetails;
	}
	@Transactional(rollbackFor = Exception.class)
	public TeamDetails updateTeamDetails(Long anniversary, TeamDetails reqTeamDetails) {
		TeamDetails teamDetails = null;
		TeamDetails updateTeamDetails = null;
		try {
			teamDetails = teamDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			teamDetails.setTeamName(reqTeamDetails.getTeamName());
			updateTeamDetails = teamDetailsRepository.save(teamDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateTeamDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteTeamDetails(Long anniversary) {
		TeamDetails teamDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			teamDetails = teamDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			teamDetailsRepository.delete(teamDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
