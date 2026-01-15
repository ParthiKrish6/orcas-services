package com.orcas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.FieldingDetails;
import com.orcas.entity.FieldingStats;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.FieldingDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class FieldingDetailsService {
	@Autowired
	private FieldingDetailsRepository fieldingDetailsRepository;
	
	@Autowired
	private PlayerDetailsService playerDetailsService;
	
	@Autowired
	CacheAdminService cacheAdminService;

	private String NOT_FOUND = "FieldingDetails not found for this id :: ";

	@Cacheable(value = CacheNames.ALL_FIELDING_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<FieldingDetails> getAllFieldingDetails() {
		return fieldingDetailsRepository.findAll();
	}
	
	@Cacheable(value = CacheNames.ALL_FIELDING_DETAILS_PLAYER_DATES, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #playerId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<FieldingDetails> fetchAllByPlayerDates(LocalDate fromDate, LocalDate toDate, Long playerId) {
		return fieldingDetailsRepository.fetchAllByPlayerDates(playerId, fromDate, toDate);
	}
	
	@Cacheable(value = CacheNames.ALL_FIELDING_DETAILS_PLAYER_DATES_TEAM, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #playerId.toString() + '_' + #teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<FieldingDetails> fetchAllByPlayerDatesTeam(LocalDate fromDate, LocalDate toDate, Long playerId, Long teamId) {
		return fieldingDetailsRepository.fetchAllByPlayerAndTeamDates(playerId, teamId, fromDate, toDate);
	}

	@Cacheable(value = CacheNames.FIELDING_DETAILS_BY_ID, key = "#fieldingId.toString()", unless = "#result == null")
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

	@Cacheable(value = CacheNames.FIELDING_DETAILS_BY_MATCH, key = "#matchId.toString()", unless = "#result == null || #result.isEmpty()")
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
		cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_FIELDING_DETAILS, CacheNames.ALL_FIELDING_STATS,
				CacheNames.FIELDING_DETAILS_BY_ID, CacheNames.FIELDING_DETAILS_BY_MATCH,
				CacheNames.FIELDING_STATS_BY_DATE, CacheNames.FIELDING_STATS_BY_DATE_TEAM,
				CacheNames.FIELDING_STATS_BY_TEAM));
		return fieldingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public FieldingDetails updateFieldingDetails(Long id, FieldingDetails reqFieldingDetails) {
		FieldingDetails fieldingDetails = null;
		FieldingDetails updateFieldingDetails = null;
		try {
			fieldingDetails = getFieldingDetailsById(id);
			fieldingDetails.setCatches(reqFieldingDetails.getCatches());
			fieldingDetails.setCatchesDropped(reqFieldingDetails.getCatchesDropped());
			fieldingDetails.setRunOuts(reqFieldingDetails.getRunOuts());
			fieldingDetails.setRunsMissed(reqFieldingDetails.getRunsMissed());
			fieldingDetails.setRunsSaved(reqFieldingDetails.getRunsSaved());
			fieldingDetails.setPlayerDetails(reqFieldingDetails.getPlayerDetails());
			updateFieldingDetails = fieldingDetailsRepository.save(fieldingDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_FIELDING_DETAILS, CacheNames.ALL_FIELDING_STATS,
					CacheNames.FIELDING_DETAILS_BY_ID, CacheNames.FIELDING_DETAILS_BY_MATCH,
					CacheNames.FIELDING_STATS_BY_DATE, CacheNames.FIELDING_STATS_BY_DATE_TEAM,
					CacheNames.FIELDING_STATS_BY_TEAM));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateFieldingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteFieldingDetails(Long id) {
		FieldingDetails fieldingDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			fieldingDetails = getFieldingDetailsById(id);
			fieldingDetailsRepository.delete(fieldingDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_FIELDING_DETAILS, CacheNames.ALL_FIELDING_STATS,
					CacheNames.FIELDING_DETAILS_BY_ID, CacheNames.FIELDING_DETAILS_BY_MATCH,
					CacheNames.FIELDING_STATS_BY_DATE, CacheNames.FIELDING_STATS_BY_DATE_TEAM,
					CacheNames.FIELDING_STATS_BY_TEAM));
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Cacheable(value = CacheNames.ALL_FIELDING_STATS, unless = "#result == null || #result.isEmpty()")
	public List<FieldingStats> getAllFieldingStats() {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository.fetchAllByPlayer(player.getPlayerId());
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.FIELDING_STATS_BY_DATE, key = "#fromDate.toString() + '_' + #toDate.toString()", unless = "#result == null || #result.isEmpty()")
	public List<FieldingStats> getFieldingStatsByDates(LocalDate fromDate, LocalDate toDate) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository
					.fetchAllByPlayerDates(player.getPlayerId(), fromDate, toDate);
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.FIELDING_STATS_BY_TEAM, key = "#teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<FieldingStats> getFieldingStatsByTeam(Long teamId) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<FieldingStats> fieldingStatsList = new ArrayList<FieldingStats>();
		for (PlayerDetails player : players) {
			List<FieldingDetails> fieldingDetails = fieldingDetailsRepository
					.fetchAllByPlayerAndTeam(player.getPlayerId(), teamId);
			fieldingStatsList.add(getFieldingStats(fieldingDetails, player));
		}
		return fieldingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.FIELDING_STATS_BY_DATE_TEAM, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<FieldingStats> getFieldingStatsByDatesAndTeam(LocalDate fromDate, LocalDate toDate, Long teamId) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
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
		fieldingStats.setPlayerId(player.getPlayerId() + "");
		fieldingStats.setCatches(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getCatches).mapToLong(Long::parseLong).sum()));
		fieldingStats.setRunOuts(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunOuts).mapToLong(Long::parseLong).sum()));
		fieldingStats.setDropped(String.valueOf(
				fieldingDetails.stream().map(FieldingDetails::getCatchesDropped).mapToLong(Long::parseLong).sum()));
		fieldingStats.setRunOuts(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunOuts).mapToLong(Long::parseLong).sum()));
		fieldingStats.setSaved(String
				.valueOf(fieldingDetails.stream().map(FieldingDetails::getRunsSaved).mapToLong(Long::parseLong).sum()));
		fieldingStats.setMissed(String.valueOf(
				fieldingDetails.stream().map(FieldingDetails::getRunsMissed).mapToLong(Long::parseLong).sum()));
		return fieldingStats;
	}

}
