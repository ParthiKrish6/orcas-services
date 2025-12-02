package com.orcas.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orcas.constants.AppConstants;
import com.orcas.constants.MappingConstants;
import com.orcas.entity.PlayerDetails;
import com.orcas.service.PlayerDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class PlayerDetailsController {
	@Autowired
	private PlayerDetailsService playerDetailsServices;

	@GetMapping(MappingConstants.URL_PLAYER_DETAILS)
	public List<PlayerDetails> getAllPlayerDetails() {
		return playerDetailsServices.getAllPlayerDetails();
	}

	@GetMapping(MappingConstants.URL_PLAYER_DETAILS_ID)
	public ResponseEntity<PlayerDetails> getPlayerDetailsById(@PathVariable(value = AppConstants.ID) Long id) {
		PlayerDetails playerDetails = playerDetailsServices.getPlayerDetailsById(id);
		return ResponseEntity.ok().body(playerDetails);
	}

	@PostMapping(MappingConstants.URL_PLAYER_DETAILS)
	public PlayerDetails createPlayerDetails(@Valid @RequestBody PlayerDetails playerDetails) throws IOException {
		return playerDetailsServices.createPlayerDetails(playerDetails);
	}
	
	@PutMapping(MappingConstants.URL_PLAYER_DETAILS_ID)
	public ResponseEntity<PlayerDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long id,
			@Valid @RequestBody PlayerDetails reqPlayerDetails) {
		PlayerDetails playerDetails = playerDetailsServices.updatePlayerDetails(id, reqPlayerDetails);
		return ResponseEntity.ok(playerDetails);
	}

	@DeleteMapping(MappingConstants.URL_PLAYER_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long id) {
		return playerDetailsServices.deletePlayerDetails(id);
	}


}
