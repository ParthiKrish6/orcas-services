package com.orcas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orcas.constants.MappingConstants;
import com.orcas.entity.HealthCheck;
import com.orcas.service.HealthCheckService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class HealthCheckController {
	@Autowired
	private HealthCheckService healthCheckServices;

	@GetMapping(MappingConstants.HEALTH_CHECK)
	public HealthCheck getAllHealthCheck() {
		return healthCheckServices.healthCheck();
	}

}