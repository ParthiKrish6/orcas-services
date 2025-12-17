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
import com.orcas.entity.BattingDetails;
import com.orcas.entity.BattingStats;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.BattingDetailsRepository;
import com.orcas.repository.PlayerDetailsRepository;

@Transactional
@Service
public class BattingDetailsService {
	@Autowired
	private BattingDetailsRepository battingDetailsRepository;

	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;

	private String NOT_FOUND = "BattingDetails not found for this id :: ";

	public List<BattingDetails> getAllBattingDetails() {
		return battingDetailsRepository.findAll();
	}

	public BattingDetails getBattingDetailsById(Long battingId) {
		BattingDetails battingDetails = null;
		try {
			battingDetails = battingDetailsRepository.findById(battingId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + battingId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return battingDetails;
	}

	public List<BattingDetails> getBattingDetailsByMatchId(Long matchId) {
		List<BattingDetails> battingDetails = null;
		try {
			battingDetails = battingDetailsRepository.findByMatchId(matchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return battingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public BattingDetails createBattingDetails(BattingDetails battingDetails) {
		battingDetails = battingDetailsRepository.save(battingDetails);
		return battingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public BattingDetails updateBattingDetails(Long id, BattingDetails reqBattingDetails) {
		BattingDetails battingDetails = null;
		BattingDetails updateBattingDetails = null;
		try {
			battingDetails = battingDetailsRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
			battingDetails.setBalls(reqBattingDetails.getBalls());
			battingDetails.setFours(reqBattingDetails.getFours());
			battingDetails.setRuns(reqBattingDetails.getRuns());
			battingDetails.setSixes(reqBattingDetails.getSixes());
			battingDetails.setStrikeRate(reqBattingDetails.getStrikeRate());
			battingDetails.setTimeSpent(reqBattingDetails.getTimeSpent());
			battingDetails.setNotOut(reqBattingDetails.getNotOut());
			battingDetails.setPlayerDetails(reqBattingDetails.getPlayerDetails());
			updateBattingDetails = battingDetailsRepository.save(battingDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateBattingDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteBattingDetails(Long anniversary) {
		BattingDetails battingDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			battingDetails = battingDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			battingDetailsRepository.delete(battingDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public List<BattingStats> getAllBattingStats() {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BattingStats> battingStatsList = new ArrayList<BattingStats>();
		for (PlayerDetails player : players) {
			List<BattingDetails> battingDetails = battingDetailsRepository.fetchAllByPlayer(player.getPlayerId());
			battingStatsList.add(getBattingStats(battingDetails, player));
		}
		return battingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}
	
	public List<BattingStats> getBattingStatsByDates(LocalDate fromDate, LocalDate toDate) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BattingStats> battingStatsList = new ArrayList<BattingStats>();
		for (PlayerDetails player : players) {
			List<BattingDetails> battingDetails = battingDetailsRepository.fetchAllByPlayerDates(player.getPlayerId(), fromDate, toDate);
			battingStatsList.add(getBattingStats(battingDetails, player));
		}
		return battingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}
	
	public List<BattingStats> getBattingStatsByTeam(Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BattingStats> battingStatsList = new ArrayList<BattingStats>();
		for (PlayerDetails player : players) {
			List<BattingDetails> battingDetails = battingDetailsRepository.fetchAllByPlayerAndTeam(player.getPlayerId(), teamId);
			battingStatsList.add(getBattingStats(battingDetails, player));
		}
		return battingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}
	
	public List<BattingStats> getBattingStatsByDatesAndTeam(LocalDate fromDate, LocalDate toDate, Long teamId) {
		List<PlayerDetails> players = playerDetailsRepository.findAll();
		List<BattingStats> battingStatsList = new ArrayList<BattingStats>();
		for (PlayerDetails player : players) {
			List<BattingDetails> battingDetails = battingDetailsRepository.fetchAllByPlayerAndTeamDates(player.getPlayerId(), teamId, fromDate, toDate);
			battingStatsList.add(getBattingStats(battingDetails, player));
		}
		return battingStatsList.stream().filter(obj -> !obj.getInnings().equals("0")).collect(Collectors.toList());
	}

	public BattingStats getBattingStats(List<BattingDetails> battingDetails, PlayerDetails player) {
		BattingStats battingStats = new BattingStats();
		battingStats.setMatches(String.valueOf(battingDetails.size()));
		battingStats.setInnings(battingDetails.stream().filter(n -> !n.getRuns().equals("DNB")).collect(Collectors.toList()).size()+"");
		battingStats.setPlayer(player.getPlayerName());
		battingStats.setPlayerId(player.getPlayerId()+"");
		battingStats.setRuns(
				String.valueOf(battingDetails.stream().map(BattingDetails::getRuns).filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		battingStats.setBalls(
				String.valueOf(battingDetails.stream().map(BattingDetails::getBalls).filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		battingStats.setFours(
				String.valueOf(battingDetails.stream().map(BattingDetails::getFours).filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		battingStats.setSixes(
				String.valueOf(battingDetails.stream().map(BattingDetails::getSixes).filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum()));
		
		if(!battingStats.getBalls().equals("0") && !battingStats.getBalls().equals("DNB")) {
			double strike = (double) Long.parseLong(battingStats.getRuns()) / Long.parseLong(battingStats.getBalls());
			battingStats.setStrikeRate(String.valueOf(strike * 100));	
		} else {
			battingStats.setStrikeRate("0.00");
		}

		List<BattingDetails> notOutList = battingDetails.stream().filter(obj -> obj.getNotOut().equals("Y"))
				.collect(Collectors.toList());
		battingStats.setNotOut(String.valueOf(notOutList.size()));

		Long timeSpent = (Long) battingDetails.stream().map(BattingDetails::getTimeSpent).filter(n -> !n.equals("DNB")).mapToLong(Long::parseLong).sum();
		
		long hours = timeSpent / 60; 
        long minutes = timeSpent % 60; 
        
		battingStats.setTimeSpent(hours+" : "+minutes);

		int inningsCount = battingDetails.size() - notOutList.size();
		if (inningsCount > 0) {
			double average = (double) Long.parseLong(battingStats.getRuns()) / inningsCount;
			battingStats.setAverage(String.valueOf(average));
		} else {
			battingStats.setAverage("NA");
		}
		return battingStats;
	}

}
