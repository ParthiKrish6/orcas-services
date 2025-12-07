package com.orcas.controller;

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
import com.orcas.entity.LoginDetails;
import com.orcas.service.LoginDetailsService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class LoginDetailsController {
	@Autowired
	private LoginDetailsService loginDetailsServices;

	@GetMapping(MappingConstants.URL_LOGIN_DETAILS)
	public List<LoginDetails> getAllLoginDetails() {
		return loginDetailsServices.getLoginAllDetails();
	}

	@GetMapping(MappingConstants.URL_LOGIN_DETAILS_ID)
	public ResponseEntity<LoginDetails> getLoginDetailsById(@PathVariable(value = AppConstants.ID) Long loginId) {
		LoginDetails loginDetails = loginDetailsServices.getLoginDetailsById(loginId);
		return ResponseEntity.ok().body(loginDetails);
	}

	@PostMapping(MappingConstants.URL_LOGIN_DETAILS)
	public LoginDetails createLoginDetails(@Valid @RequestBody LoginDetails loginDetails) {
		return loginDetailsServices.createLoginDetails(loginDetails);
	}
	
	@PostMapping(MappingConstants.URL_LOGIN)
	public LoginDetails login(@Valid @RequestBody LoginDetails loginDetails) {
		return loginDetailsServices.login(loginDetails);
	}
	
	@PutMapping(MappingConstants.URL_LOGIN_DETAILS_ID)
	public ResponseEntity<LoginDetails> updateDetails(@PathVariable(value = AppConstants.ID) Long anniversary,
			@Valid @RequestBody LoginDetails reqLoginDetails) {
		LoginDetails loginDetails = loginDetailsServices.updateLoginDetails(anniversary, reqLoginDetails);
		return ResponseEntity.ok(loginDetails);
	}

	@DeleteMapping(MappingConstants.URL_LOGIN_DETAILS_ID)
	public Map<String, Boolean> deleteDetails(@PathVariable(value = AppConstants.ID) Long id) {
		return loginDetailsServices.deleteLoginDetails(id);
	}

}