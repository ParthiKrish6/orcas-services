package com.orcas.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.PlayerDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.PlayerDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class PlayerDetailsService {
	
	@Autowired
	private PlayerDetailsRepository playerDetailsRepository;
	
	@Autowired
	CacheAdminService cacheAdminService;
	
	private String NOT_FOUND = "PlayerDetails not found for this id :: ";

	@Cacheable(value = CacheNames.ALL_PLAYER_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<PlayerDetails> getAllPlayerDetails() {
		return playerDetailsRepository.findAll();
	}

	@Cacheable(value = CacheNames.PLAYER_DETAILS_BY_ID, key = "#playerId.toString()", unless = "#result == null")
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
	
	@Cacheable(value = CacheNames.PLAYER_DETAILS_BY_NAME, key = "#playerName.toString()) + '_' + #nickName.toString()", unless = "#result == null")
    public PlayerDetails getPlayer(String playerName, String nickName) {
        if (nickName != null && !nickName.trim().isEmpty()) {
            return playerDetailsRepository.getPlayerFromNameOrNickName(playerName, nickName);
        } else {
            return playerDetailsRepository.getPlayerFromName(playerName);
        }
    }

    @Transactional
    public PlayerDetails createPlayerDetails(PlayerDetails playerDetails) throws IOException {
        PlayerDetails existing = getPlayer(
                playerDetails.getPlayerName(),
                playerDetails.getNickName()
        );
        if (existing == null) {
            playerDetails = playerDetailsRepository.save(playerDetails);
            cacheAdminService.clearCache(Arrays.asList(CacheNames.PLAYER_DETAILS_BY_ID, CacheNames.PLAYER_DETAILS_BY_NAME,
    				CacheNames.ALL_PLAYER_DETAILS));
        } else {
            throw new IOException("Player already exists");
        }
        return playerDetails;
    }
	
	@Transactional(rollbackFor = Exception.class)
	public PlayerDetails updatePlayerDetails(Long playerId, PlayerDetails reqPlayerDetails) {
		PlayerDetails playerDetails = null;
		PlayerDetails updatePlayerDetails = null;
		try {
			playerDetails = getPlayerDetailsById(playerId);
			playerDetails.setPlayerName(reqPlayerDetails.getPlayerName());
			playerDetails.setNickName(reqPlayerDetails.getNickName());
			updatePlayerDetails = playerDetailsRepository.save(playerDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.PLAYER_DETAILS_BY_ID, CacheNames.PLAYER_DETAILS_BY_NAME,
    				CacheNames.ALL_PLAYER_DETAILS));
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
			playerDetails = getPlayerDetailsById(playerId);
			playerDetailsRepository.delete(playerDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.PLAYER_DETAILS_BY_ID, CacheNames.PLAYER_DETAILS_BY_NAME,
    				CacheNames.ALL_PLAYER_DETAILS));
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
