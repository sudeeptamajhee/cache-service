package com.caffeine.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ComponentScan(basePackages = { "com.caffeine" })
public class CaffeineConfig {
	
	private static final Log LOGGER = LogFactory.getLog(CaffeineConfig.class);

	public CaffeineConfig() {
		LOGGER.debug("********CacheConfig*******");
	}

	@Bean
	public CacheManager createCacheManager() {
		return CaffeineBuilder.getInstance();
	}

}
