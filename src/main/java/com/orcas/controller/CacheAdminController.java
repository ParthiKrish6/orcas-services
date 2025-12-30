package com.orcas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orcas.constants.MappingConstants;
import com.orcas.service.CacheAdminService;

@RestController
@RequestMapping(MappingConstants.URL_API_V1)
public class CacheAdminController {

    @Autowired
    private CacheAdminService cacheAdminService;

    @PostMapping(MappingConstants.CLEAR_CACHE)
    public ResponseEntity<String> clearBattingCache() {
        cacheAdminService.clearAllCache();
        return ResponseEntity.ok("Cache Cleared Successfully");
    }
}