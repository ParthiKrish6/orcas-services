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
import com.orcas.entity.BowlingDetails;
import com.orcas.entity.BowlingStats;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.BowlingDetailsRepository;
import com.orcas.repository.PlayerDetailsRepository;

@Transactional
@Service
public class BowlingDetailsService {
	@Autowired
	private BowlingDetailsRepository bowlingDetailsRepository;

	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;

	private String NOT_FOUND = "BowlingDetails not found for this id :: ";

	public List<BowlingDetails> getAllBowlingDetails() {
		return bowlingDetailsRepository.findAll();
	}

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
		return bowlingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public BowlingDetails updateBowlingDetails(Long id, BowlingDetails reqBowlingDetails) {
		BowlingDetails bowlingDetails = null;
		BowlingDetails updateBowlingDetails = null;
		try {
			bowlingDetails = bowlingDetailsRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateBowlingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteBowlingDetails(Long anniversary) {
		BowlingDetails bowlingDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			bowlingDetails = bowlingDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			bowlingDetailsRepository.delete(bowlingDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public List<BowlingStats> getAllBowlingStats() {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayer(player.getPlayerId());
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<BowlingStats> getBowlingStatsByDates(LocalDate fromDate, LocalDate toDate) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayerDates(player.getPlayerId(),
					fromDate, toDate);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<BowlingStats> getBowlingStatsByTeam(Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayerAndTeam(player.getPlayerId(),
					teamId);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public List<BowlingStats> getBowlingStatsByDatesAndTeam(LocalDate fromDate, LocalDate toDate, Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BowlingStats> bowlingStatsList = new ArrayList<BowlingStats>();
		for (PlayerDetails player : players) {
			List<BowlingDetails> bowlingDetails = bowlingDetailsRepository.fetchAllByPlayerAndTeamDates(player.getPlayerId(),
					teamId, fromDate, toDate);
			bowlingStatsList.add(getBowlingStats(bowlingDetails, player));
		}
		return bowlingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public BowlingStats getBowlingStats(List<BowlingDetails> bowlingDetails, PlayerDetails player) {
		BowlingStats bowlingStats = new BowlingStats();
		bowlingStats.setInnings(String.valueOf(bowlingDetails.size()));
		bowlingStats.setPlayer(player.getPlayerName());
		bowlingStats.setPlayerId(player.getPlayerId()+"");
		bowlingStats.setRuns(
				String.valueOf(bowlingDetails.stream().map(BowlingDetails::getRuns).mapToLong(Long::parseLong).sum()));
		bowlingStats.setFours(
				String.valueOf(bowlingDetails.stream().map(BowlingDetails::getFours).mapToLong(Long::parseLong).sum()));
		bowlingStats.setSixes(
				String.valueOf(bowlingDetails.stream().map(BowlingDetails::getSixes).mapToLong(Long::parseLong).sum()));
		bowlingStats.setMaidens(String
				.valueOf(bowlingDetails.stream().map(BowlingDetails::getMaidens).mapToLong(Long::parseLong).sum()));
		bowlingStats.setWickets(String
				.valueOf(bowlingDetails.stream().map(BowlingDetails::getWickets).mapToLong(Long::parseLong).sum()));
		bowlingStats.setDots(
				String.valueOf(bowlingDetails.stream().map(BowlingDetails::getDots).mapToLong(Long::parseLong).sum()));
		bowlingStats.setWides(
				String.valueOf(bowlingDetails.stream().map(BowlingDetails::getWides).mapToLong(Long::parseLong).sum()));
		bowlingStats.setNoBalls(String
				.valueOf(bowlingDetails.stream().map(BowlingDetails::getNoballs).mapToLong(Long::parseLong).sum()));

		int inningsCount = bowlingDetails.size();
		if (inningsCount > 0) {
			double average = (double) Long.parseLong(bowlingStats.getRuns())
					/ Long.parseLong(bowlingStats.getWickets());
			bowlingStats.setAverage(String.valueOf(average));

			double economy = (double) bowlingDetails.stream().map(BowlingDetails::getEconomy)
					.mapToDouble(Double::parseDouble).sum() / inningsCount;
			bowlingStats.setEconomy(String.valueOf(economy));

			Long totalBalls = 0L;
			int ball = 0;
			for (BowlingDetails bowl : bowlingDetails) {
				if (bowl.getOvers() != null && !trimReplaceSpl(bowl.getOvers()).isEmpty()) {
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
