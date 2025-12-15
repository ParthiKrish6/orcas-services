package com.orcas.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.PlayerDetailsRepository;

@Transactional
@Service
public class PlayerDetailsService {
	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;

	private String NOT_FOUND = "PlayerDetails not found for this id :: ";

	public List<PlayerDetails> getAllPlayerDetails() {
		return playerDetailsRepository.findAll();
	}

	public PlayerDetails getPlayerDetailsById(Long playerId) {
		PlayerDetails playerDetails = null;
		try {
			playerDetails = playerDetailsRepository.findById(playerId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + playerId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return playerDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public PlayerDetails createPlayerDetails(PlayerDetails playerDetails) throws IOException {
		PlayerDetails existing = null;
		if(playerDetails.getNickName() != null && !playerDetails.getNickName().trim().isEmpty()) {
			existing = playerDetailsRepository.getPlayerFromNameOrNickName(playerDetails.getPlayerName(), playerDetails.getNickName());
		} else {
			existing = playerDetailsRepository.getPlayerFromName(playerDetails.getPlayerName());
		}
		System.out.println("existing" +existing);
		if(existing == null) {
			playerDetails = playerDetailsRepository.save(playerDetails);
		} else {
			throw new IOException("Player already exists");
		}
		
		return playerDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public PlayerDetails updatePlayerDetails(Long id, PlayerDetails reqPlayerDetails) {
		PlayerDetails playerDetails = null;
		PlayerDetails updatePlayerDetails = null;
		try {
			playerDetails = playerDetailsRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + id));
			playerDetails.setPlayerName(reqPlayerDetails.getPlayerName());
			playerDetails.setNickName(reqPlayerDetails.getNickName());
			playerDetails.setImage(reqPlayerDetails.getImage());
			updatePlayerDetails = playerDetailsRepository.save(playerDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatePlayerDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deletePlayerDetails(Long playerId) {
		PlayerDetails playerDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			playerDetails = playerDetailsRepository.findById(playerId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + playerId));
			playerDetailsRepository.delete(playerDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
