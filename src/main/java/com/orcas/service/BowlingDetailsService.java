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
import com.orcas.entity.BowlingDetails;
import com.orcas.entity.BowlingStats;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.BowlingDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class BowlingDetailsService {
	@Autowired
	private BowlingDetailsRepository bowlingDetailsRepository;

	@Autowired
	private PlayerDetailsService playerDetailsService;

	@Autowired
	CacheAdminService cacheAdminService;

	private String NOT_FOUND = "BowlingDetails not found for this id :: ";

	@Cacheable(value = CacheNames.ALL_BOWLING_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<BowlingDetails> getAllBowlingDetails() {
		return bowlingDetailsRepository.findAll();
	}

	@Cacheable(value = CacheNames.BOWLING_DETAILS_BY_ID, key = "#bowlingId.toString()", unless = "#result == null")
	public BowlingDetails getBowlingDetailsById(Long bowlingId) {
		BowlingDetails bowlingDetails = null;
		try {
			bowlingDetails = bowlingDetailsRepository.findById(bowlingId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + bowlingId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bowlingDetails;
	}
	
	@Cacheable(value = CacheNames.ALL_BOWLING_DETAILS_PLAYER_DATES, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #playerId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingDetails> fetchAllByPlayerDates(LocalDate fromDate, LocalDate toDate, Long playerId) {
		return bowlingDetailsRepository.fetchAllByPlayerDates(playerId, fromDate, toDate);
	}
	
	@Cacheable(value = CacheNames.ALL_BOWLING_DETAILS_PLAYER_DATES_TEAM, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #playerId.toString() + '_' + #teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingDetails> fetchAllByPlayerDatesTeam(LocalDate fromDate, LocalDate toDate, Long playerId, Long teamId) {
		return bowlingDetailsRepository.fetchAllByPlayerAndTeamDates(playerId, teamId, fromDate, toDate);
	}

	@Cacheable(value = CacheNames.BOWLING_DETAILS_BY_MATCH, key = "#matchId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingDetails> getBowlingDetailsByMatchId(Long matchId) {
		List<BowlingDetails> bowlingDetails = null;
		try {
			bowlingDetails = bowlingDetailsRepository.findByMatchId(matchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bowlingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public BowlingDetails createBowlingDetails(BowlingDetails bowlingDetails) {
		bowlingDetails = bowlingDetailsRepository.save(bowlingDetails);
		cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_BOWLING_DETAILS, CacheNames.ALL_BOWLING_STATS,
				CacheNames.BOWLING_DETAILS_BY_ID, CacheNames.BOWLING_DETAILS_BY_MATCH, CacheNames.BOWLING_STATS_BY_DATE,
				CacheNames.BOWLING_STATS_BY_DATE_TEAM, CacheNames.BOWLING_STATS_BY_TEAM));
		return bowlingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public BowlingDetails updateBowlingDetails(Long id, BowlingDetails reqBowlingDetails) {
		BowlingDetails bowlingDetails = null;
		BowlingDetails updateBowlingDetails = null;
		try {
			bowlingDetails = getBowlingDetailsById(id);
			bowlingDetails.setDots(reqBowlingDetails.getDots());
			bowlingDetails.setEconomy(reqBowlingDetails.getEconomy());
			bowlingDetails.setFours(reqBowlingDetails.getFours());
			bowlingDetails.setRuns(reqBowlingDetails.getRuns());
			bowlingDetails.setSixes(reqBowlingDetails.getSixes());
			bowlingDetails.setMaidens(reqBowlingDetails.getMaidens());
			bowlingDetails.setNoballs(reqBowlingDetails.getNoballs());
			bowlingDetails.setOvers(reqBowlingDetails.getOvers());
			bowlingDetails.setWickets(reqBowlingDetails.getWickets());
			bowlingDetails.setWides(reqBowlingDetails.getWides());
			bowlingDetails.setPlayerDetails(reqBowlingDetails.getPlayerDetails());
			updateBowlingDetails = bowlingDetailsRepository.save(bowlingDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_BOWLING_DETAILS, CacheNames.ALL_BOWLING_STATS,
					CacheNames.BOWLING_DETAILS_BY_ID, CacheNames.BOWLING_DETAILS_BY_MATCH,
					CacheNames.BOWLING_STATS_BY_DATE, CacheNames.BOWLING_STATS_BY_DATE_TEAM,
					CacheNames.BOWLING_STATS_BY_TEAM));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateBowlingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteBowlingDetails(Long id) {
		BowlingDetails bowlingDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			bowlingDetails = getBowlingDetailsById(id);
			bowlingDetailsRepository.delete(bowlingDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.ALL_BOWLING_DETAILS, CacheNames.ALL_BOWLING_STATS,
					CacheNames.BOWLING_DETAILS_BY_ID, CacheNames.BOWLING_DETAILS_BY_MATCH,
					CacheNames.BOWLING_STATS_BY_DATE, CacheNames.BOWLING_STATS_BY_DATE_TEAM,
					CacheNames.BOWLING_STATS_BY_TEAM));
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Cacheable(value = CacheNames.ALL_BOWLING_STATS, unless = "#result == null || #result.isEmpty()")
	public List<BowlingStats> getAllBowlingStats() {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayer(player.getPlayerId());
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.BOWLING_STATS_BY_DATE, key = "#fromDate.toString() + '_' + #toDate.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingStats> getBowlingStatsByDates(LocalDate fromDate, LocalDate toDate) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayerDates(player.getPlayerId(),
					fromDate, toDate);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.BOWLING_STATS_BY_TEAM, key = "#teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingStats> getBowlingStatsByTeam(Long teamId) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayerAndTeam(player.getPlayerId(),
					teamId);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	@Cacheable(value = CacheNames.BOWLING_STATS_BY_DATE_TEAM, key = "#fromDate.toString() + '_' + #toDate.toString() + '_' + #teamId.toString()", unless = "#result == null || #result.isEmpty()")
	public List<BowlingStats> getBowlingStatsByDatesAndTeam(LocalDate fromDate, LocalDate toDate, Long teamId) {
		List<PlayerDetails> players = playerDetailsService.getAllPlayerDetails();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository
					.fetchAllByPlayerAndTeamDates(player.getPlayerId(), teamId, fromDate, toDate);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public BowlingStats getBowlingStats(List<BowlingDetails> bowlingDetails, PlayerDetails player) {
		BowlingStats bowlingStats = new BowlingStats();
		bowlingStats.setMatches(String.valueOf(bowlingDetails.size()));
		bowlingStats.setInnings(
				bowlingDetails.stream().filter(n -> !n.getOvers().equals("DNB")).collect(Collectors.toList()).size()
						+ "");
		bowlingStats.setPlayer(player.getPlayerName());
		bowlingStats.setPlayerId(player.getPlayerId() + "");
		bowlingStats.setRuns(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getRuns)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setFours(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getFours)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setSixes(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getSixes)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setMaidens(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getMaidens)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setWickets(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getWickets)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setDots(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getDots)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setWides(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getWides)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		bowlingStats.setNoBalls(String.valueOf(bowlingDetails.stream().map(BowlingDetails::getNoballs)
				.filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));

		int inningsCount = bowlingDetails.stream().filter(n -> !n.getOvers().equals("DNB")).collect(Collectors.toList())
				.size();
		if (inningsCount > 0) {
			double average = (double) Long.parseLong(bowlingStats.getRuns())
					/ Long.parseLong(bowlingStats.getWickets());
			bowlingStats.setAverage(String.valueOf(average));

			double economy = (double) bowlingDetails.stream().map(BowlingDetails::getEconomy)
					.filter(n -> !n.equals("DNB")).mapToDouble(Double::parseDouble).sum() / inningsCount;
			bowlingStats.setEconomy(String.valueOf(economy));

			Long totalBalls = 0L;
			for (BowlingDetails bowl : bowlingDetails) {
				int ball = 0;
				if (bowl.getOvers() != null && !trimReplaceSpl(bowl.getOvers()).isEmpty()
						&& !bowl.getOvers().equals("DNB")) {
					String overs[] = trimReplaceSpl(bowl.getOvers()).split("\\.");
					if (overs.length > 0) {
						ball = (Integer.parseInt(overs[0]) * 6);
						if (overs.length > 1) {
							ball = ball + Integer.parseInt(overs[1]);
						}
					}
				}
				totalBalls = totalBalls + ball;
			}
			double strike = (double) totalBalls / Long.parseLong(bowlingStats.getWickets());
			bowlingStats.setStrikeRate(String.valueOf(strike));
			Long completeOvers = totalBalls / 6;
			Long remainingBalls = totalBalls % 6;
			bowlingStats.setOvers(completeOvers + "." + remainingBalls);

		} else {
			bowlingStats.setAverage("NA");
			bowlingStats.setEconomy("0.00");
			bowlingStats.setStrikeRate("0.00");
		}

		return bowlingStats;
	}

	private static String trimReplaceSpl(String st) {
		return st.replace("\r", "").replace("\n", "").replace("†", "").trim();
	}

}
