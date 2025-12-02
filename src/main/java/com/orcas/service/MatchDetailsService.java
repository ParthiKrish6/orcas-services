package com.orcas.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.MatchDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.MatchDetailsRepository;

@Transactional
@Service
public class MatchDetailsService {
	@Autowired
	private MatchDetailsRepository matchDetailsRepository;
	
	private String NOT_FOUND = "MatchDetails not found for this id :: ";

	public List<MatchDetails> getAllMatchDetails() {
		return matchDetailsRepository.findAll();
	}

	public MatchDetails getMatchDetailsById(Long matchId) {
		MatchDetails matchDetails = null;
		try {
			matchDetails = matchDetailsRepository.findById(matchId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + matchId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchDetails;
	}
	
	public List<MatchDetails> getMatchDetailsByDates(LocalDate fromDate, LocalDate toDate) {
		return matchDetailsRepository.getData_between(fromDate, toDate);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public MatchDetails createMatchDetails(MatchDetails matchDetails) {
		matchDetails = matchDetailsRepository.save(matchDetails);
		return matchDetails;
	}
	@Transactional(rollbackFor = Exception.class)
	public MatchDetails updateMatchDetails(Long matchId, MatchDetails reqMatchDetails) {
		MatchDetails matchDetails = null;
		MatchDetails updateMatchDetails = null;
		try {
			matchDetails = matchDetailsRepository.findById(matchId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + matchId));
			matchDetails.setBatFirst(reqMatchDetails.getBatFirst());
			matchDetails.setCaptain(reqMatchDetails.getCaptain());
			matchDetails.setMargin(reqMatchDetails.getMargin());
			matchDetails.setMatchDate(reqMatchDetails.getMatchDate());
			matchDetails.setMatchResult(reqMatchDetails.getMatchResult());
			matchDetails.setOpponent(reqMatchDetails.getOpponent());
			matchDetails.setOpponentScore(reqMatchDetails.getOpponentScore());
			matchDetails.setTeamDetails(reqMatchDetails.getTeamDetails());
			matchDetails.setTeamScore(reqMatchDetails.getTeamScore());
			matchDetails.setViceCaptain(reqMatchDetails.getViceCaptain());
			updateMatchDetails = matchDetailsRepository.save(matchDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateMatchDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteMatchDetails(Long matchId) {
		MatchDetails matchDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			matchDetails = matchDetailsRepository.findById(matchId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + matchId));
			matchDetailsRepository.delete(matchDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
