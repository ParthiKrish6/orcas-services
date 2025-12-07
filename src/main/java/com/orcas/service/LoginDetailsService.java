package com.orcas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.LoginDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.LoginDetailsRepository;

@Transactional
@Service
public class LoginDetailsService {
	@Autowired
	private LoginDetailsRepository loginDetailsRepository;

	private String NOT_FOUND = "LoginDetails not found for this id :: ";
	
	public List<LoginDetails> getLoginAllDetails() {
		List<LoginDetails> loginDetails = null;
		try {
			loginDetails = loginDetailsRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetails;
	}

	public LoginDetails getLoginDetailsById(Long loginId) {
		LoginDetails loginDetails = null;
		try {
			loginDetails = loginDetailsRepository.findById(loginId)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + loginId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public LoginDetails createLoginDetails(LoginDetails loginDetails) {
		loginDetails = loginDetailsRepository.save(loginDetails);
		return loginDetails;
	}
	
	public LoginDetails login(LoginDetails loginDetails) {
		LoginDetails loginDetailsResp = null;
		try {
			loginDetailsResp = loginDetailsRepository.getUser(loginDetails.getUserId(), loginDetails.getPwd());
			System.out.println("loginDetailsRepository.getUser :: User :: "+loginDetails.getUserId());
			System.out.println("loginDetailsRepository.getUser :: Pwd :: "+loginDetails.getPwd());
			System.out.println("loginDetailsRepository.getUser :: Resp :: "+loginDetailsResp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetailsResp;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public LoginDetails updateLoginDetails(Long anniversary, LoginDetails reqLoginDetails) {
		LoginDetails loginDetails = null;
		LoginDetails updateLoginDetails = null;
		try {
			loginDetails = loginDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			loginDetails.setUserId(reqLoginDetails.getUserId());
			loginDetails.setPwd(reqLoginDetails.getPwd());
			loginDetails.setType(reqLoginDetails.getType());
			updateLoginDetails = loginDetailsRepository.save(loginDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateLoginDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteLoginDetails(Long anniversary) {
		LoginDetails loginDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			loginDetails = loginDetailsRepository.findById(anniversary)
					.orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + anniversary));
			loginDetailsRepository.delete(loginDetails);
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
