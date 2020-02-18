package com.caffeine.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ComponentScan(basePackages = { "com.caffeine" })
public class CaffeineConfig {

	public CaffeineConfig() {
		System.out.println("********CacheConfig*******");
	}

	@Bean
	public CacheManager createCacheManager() {
		return CaffeineBuilder.getInstance();
	}

}
