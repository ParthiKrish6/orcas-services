package com.orcas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orcas.entity.HealthCheck;
import com.orcas.repository.HealthCheckRepository;

@Transactional
@Service
public class HealthCheckService {
	@Autowired
	private HealthCheckRepository healthCheckRepository;

	@Transactional(rollbackFor = Exception.class)
	public HealthCheck healthCheck() {
		HealthCheck healthCheck = new HealthCheck();
		healthCheck = healthCheckRepository.save(healthCheck);
		healthCheckRepository.delete(healthCheck);
		return healthCheck;
	}
}
