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
import com.orcas.entity.TeamDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.TeamDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class TeamDetailsService {
	
	@Autowired
	private TeamDetailsRepository teamDetailsRepository;
	
	@Autowired
	CacheAdminService cacheAdminService;

	private String NOT_FOUND = "TeamDetails not found for this id :: ";

	@Cacheable(value = CacheNames.ALL_TEAM_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<TeamDetails> getAllTeamDetails() {
		return teamDetailsRepository.findAll();
	}

	@Cacheable(value = CacheNames.TEAM_DETAILS_BY_ID, key = "#teamId.toString()", unless = "#result == null")
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
	
	@Cacheable(value = CacheNames.TEAM_DETAILS_BY_NAME, key = "#teamName.toString()", unless = "#result == null")
	public TeamDetails getTeamFromName(String teamName) {
		TeamDetails teamDetails = null;
		try {
			teamDetails = teamDetailsRepository.getTeamFromName(teamName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teamDetails;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public TeamDetails createTeamDetails(TeamDetails teamDetails) {
		teamDetails = teamDetailsRepository.save(teamDetails);
		cacheAdminService.clearCache(Arrays.asList(CacheNames.TEAM_DETAILS_BY_ID, CacheNames.ALL_TEAM_DETAILS, CacheNames.TEAM_DETAILS_BY_NAME));
		return teamDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public TeamDetails updateTeamDetails(Long teamId, TeamDetails reqTeamDetails) {
		TeamDetails teamDetails = null;
		TeamDetails updateTeamDetails = null;
		try {
			teamDetails = getTeamDetailsById(teamId);
			teamDetails.setTeamName(reqTeamDetails.getTeamName());
			updateTeamDetails = teamDetailsRepository.save(teamDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.TEAM_DETAILS_BY_ID, CacheNames.ALL_TEAM_DETAILS, CacheNames.TEAM_DETAILS_BY_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateTeamDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteTeamDetails(Long teamId) {
		TeamDetails teamDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			teamDetails = getTeamDetailsById(teamId);
			teamDetailsRepository.delete(teamDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.TEAM_DETAILS_BY_ID, CacheNames.ALL_TEAM_DETAILS, CacheNames.TEAM_DETAILS_BY_NAME));
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
