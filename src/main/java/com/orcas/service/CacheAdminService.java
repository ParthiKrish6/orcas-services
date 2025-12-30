package com.orcas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.orcas.utils.CacheNameUtil;

@Service
public class CacheAdminService {

	@Autowired
	private CacheManager cacheManager;

	public void clearAllCache() {
		CacheNameUtil.getAllCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
	}
	
	public void clearCache(List<String> cacheNames) {
		cacheNames.forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
	}
}