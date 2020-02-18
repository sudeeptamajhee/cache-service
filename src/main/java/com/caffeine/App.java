package com.caffeine;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.caffeine.cache.CacheService;
import com.caffeine.config.CaffeineConfig;
import com.caffeine.constant.CaffeineConstants;

public class App {

	public static void main(String[] args) {
		System.setProperty("cache.app.name", "SM");
		ApplicationContext context = new AnnotationConfigApplicationContext(CaffeineConfig.class);
		CacheService cacheSerevice = (CacheService)context.getBean(CacheService.class);

		App app = new App();
		app.execute(cacheSerevice);
		((AnnotationConfigApplicationContext)context).close();
	}

	private void execute(CacheService cacheSerevice) {
		cacheSerevice.put(CaffeineConstants.CACHE1, "caffeine-key-1", "caffeine-value-1");
		cacheSerevice.put(CaffeineConstants.CACHE1, "caffeine-key-2", "caffeine-value-2");
		
		Map<String, String> map = new HashMap<>();
		map.put("c-2-key-1", "c-2-value-1");
		map.put("c-2-key-2", "c-2-value-2");
		cacheSerevice.putAll(CaffeineConstants.CACHE2, map);
		
		System.out.println(cacheSerevice.getAll(CaffeineConstants.CACHE1));
		
		System.out.println(cacheSerevice.getAll(CaffeineConstants.CACHE2));
		
		System.out.println(cacheSerevice.getAll(CaffeineConstants.CACHE3));
	}
}
