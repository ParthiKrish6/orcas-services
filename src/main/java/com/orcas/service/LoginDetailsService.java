package com.orcas.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.constants.AppConstants;
import com.orcas.entity.LoginDetails;
import com.orcas.exception.ResourceNotFoundException;
import com.orcas.repository.LoginDetailsRepository;
import com.orcas.utils.CacheNames;

@Transactional
@Service
public class LoginDetailsService {
	
	@Autowired
	private LoginDetailsRepository loginDetailsRepository;
	
	@Autowired
	CacheAdminService cacheAdminService;

	private String NOT_FOUND = "LoginDetails not found for this id :: ";
	
	@Cacheable(value = CacheNames.ALL_LOGIN_DETAILS, unless = "#result == null || #result.isEmpty()")
	public List<LoginDetails> getLoginAllDetails() {
		List<LoginDetails> loginDetails = null;
		try {
			loginDetails = loginDetailsRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetails;
	}

	@Cacheable(value = CacheNames.LOGIN_DETAILS_BY_ID, key = "#loginId.toString()", unless = "#result == null")
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
	
	@Cacheable(value = CacheNames.LOGIN_DETAILS, key = "#user.toString() + '_' + #pwd.toString()", unless = "#result == null")
	public LoginDetails getUser(String user, String pwd) {
		LoginDetails loginDetails = null;
		try {
			loginDetails = loginDetailsRepository.getUser(user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetails;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public LoginDetails createLoginDetails(LoginDetails loginDetails) {
		loginDetails = loginDetailsRepository.save(loginDetails);
		cacheAdminService.clearCache(Arrays.asList(CacheNames.LOGIN_DETAILS, CacheNames.LOGIN_DETAILS_BY_ID,
				CacheNames.ALL_LOGIN_DETAILS));
		return loginDetails;
	}
	
	public LoginDetails login(LoginDetails loginDetails) {
		LoginDetails loginDetailsResp = null;
		try {
			loginDetailsResp = getUser(loginDetails.getUserId(), loginDetails.getPwd());
			/* if(loginDetailsResp == null) {
				if("orcasview".equals(loginDetails.getUserId())) {
					loginDetails.setType("V");
				} else if("orcasadm".equals(loginDetails.getUserId())) {
					loginDetails.setType("A");
				}
				loginDetailsRepository.save(loginDetails);
				cacheAdminService.clearCache(Arrays.asList(CacheNames.LOGIN_DETAILS, CacheNames.LOGIN_DETAILS_BY_ID,
				CacheNames.ALL_LOGIN_DETAILS));
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginDetailsResp;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public LoginDetails updateLoginDetails(Long id, LoginDetails reqLoginDetails) {
		LoginDetails loginDetails = null;
		LoginDetails updateLoginDetails = null;
		try {
			loginDetails = getLoginDetailsById(id);
			loginDetails.setUserId(reqLoginDetails.getUserId());
			loginDetails.setPwd(reqLoginDetails.getPwd());
			loginDetails.setType(reqLoginDetails.getType());
			updateLoginDetails = loginDetailsRepository.save(loginDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.LOGIN_DETAILS, CacheNames.LOGIN_DETAILS_BY_ID,
					CacheNames.ALL_LOGIN_DETAILS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateLoginDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	public Map<String, Boolean> deleteLoginDetails(Long id) {
		LoginDetails loginDetails;
		Map<String, Boolean> response = new HashMap<>();
		try {
			loginDetails = getLoginDetailsById(id);
			loginDetailsRepository.delete(loginDetails);
			cacheAdminService.clearCache(Arrays.asList(CacheNames.LOGIN_DETAILS, CacheNames.LOGIN_DETAILS_BY_ID,
					CacheNames.ALL_LOGIN_DETAILS));
			response.put(AppConstants.DELETED, Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
