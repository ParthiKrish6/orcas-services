package com.orcas.utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder().maximumSize(100).expireAfterWrite(30, TimeUnit.MINUTES).recordStats();
	}

	@Bean
	public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
		Set<String> allCacheNames = CacheNameUtil.getAllCacheNames();
		String[] cacheNamesArray = allCacheNames.toArray(new String[0]);
		CaffeineCacheManager manager = new CaffeineCacheManager(cacheNamesArray);
		manager.setCaffeine(caffeine);
		return manager;
	}
}