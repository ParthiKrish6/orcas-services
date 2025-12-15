package com.orcas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.FieldingDetails;
import com.orcas.entity.FieldingStats;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.FieldingDetailsRepository;
import com.orcas.repository.PlayerDetailsRepository;

@Transactional
@Service
public class FieldingDetailsService {
	@Autowired
	private FieldingDetailsRepository fieldingDetailsRepository;
	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;

	private String NOT_FOUND = "FieldingDetails not found for this id :: ";

	public List<FieldingDetails> getAllFieldingDetails() {
		return fieldingDetailsRepository.findAll();
	}

	public FieldingDetails getFieldingDetailsById(Long fieldingId) {
		FieldingDetails fieldingDetails = null;
		try {
			fieldingDetails = fieldingDetailsRepository.findById(fieldingId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + fieldingId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldingDetails;
	}

	public List<FieldingDetails> getFieldingDetailsByMatchId(Long matchId) {
		List<FieldingDetails> fieldingDetails = null;
		try {
			fieldingDetails = fieldingDetailsRepository.findByMatchId(matchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public FieldingDetails createFieldingDetails(FieldingDetails fieldingDetails) {
		fieldingDetails = fieldingDetailsRepository.save(fieldingDetails);
		return fieldingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public FieldingDetails updateFieldingDetails(Long id, FieldingDetails reqFieldingDetails) {
		FieldingDetails fieldingDetails = null;
		FieldingDetails updateFieldingDetails = null;
		try {
			fieldingDetails = fieldingDetailsRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
			fieldingDetails.setCatches(reqFieldingDetails.getCatches());
			fieldingDetails.setCatchesDropped(reqFieldingDetails.getCatchesDropped());
			fieldingDetails.setRunOuts(reqFieldingDetails.getRunOuts());
			fieldingDetails.setRunsMissed(reqFieldingDetails.getRunsMissed());
			fieldingDetails.setRunsSaved(reqFieldingDetails.getRunsSaved());
			fieldingDetails.setPlayerDetails(reqFieldingDetails.getPlayerDetails());
			updateFieldingDetails = fieldingDetailsRepository.save(fieldingDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateFieldingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteFieldingDetails(Long anniversary) {
		FieldingDetails fieldingDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			fieldingDetails = fieldingDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			fieldingDetailsRepository.delete(fieldingDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public List<FieldingStats> getAllFieldingStats() {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository.fetchAllByPlayer(player.getPlayerId());
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<FieldingStats> getFieldingStatsByDates(LocalDate fromDate, LocalDate toDate) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository.fetchAllByPlayerDates(player.getPlayerId(),
					fromDate, toDate);
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<FieldingStats> getFieldingStatsByTeam(Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository.fetchAllByPlayerAndTeam(player.getPlayerId(),
					teamId);
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<FieldingStats> getFieldingStatsByDatesAndTeam(LocalDate fromDate, LocalDate toDate, Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository
					.fetchAllByPlayerAndTeamDates(player.getPlayerId(), teamId, fromDate, toDate);
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public FieldingStats getFieldingStats(List<FieldingDetails> fieldingDetails, PlayerDetails player) {
		FieldingStats fieldingStats = new FieldingStats();
		fieldingStats.setInnings(String.valueOf(fieldingDetails.size()));
		fieldingStats.setPlayer(player.getPlayerName());
		fieldingStats.setImage(player.getImage());
		fieldingStats.setCatches(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getCatches).mapToLong(Long::parseLong).sum()));
		fieldingStats.setRunOuts(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunOuts).mapToLong(Long::parseLong).sum()));	
		fieldingStats.setDropped(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getCatchesDropped).mapToLong(Long::parseLong).sum()));
		fieldingStats.setRunOuts(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunOuts).mapToLong(Long::parseLong).sum()));
		fieldingStats.setSaved(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunsSaved).mapToLong(Long::parseLong).sum()));
		fieldingStats.setMissed(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunsMissed).mapToLong(Long::parseLong).sum()));
		return fieldingStats;
	}

}
