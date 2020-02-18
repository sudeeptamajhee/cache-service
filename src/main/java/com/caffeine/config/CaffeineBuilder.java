package com.caffeine.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CaffeineBuilder extends SimpleCacheManager implements Serializable, Cloneable {

	private static final Log LOGGER = LogFactory.getLog(CaffeineBuilder.class);
	private static final long serialVersionUID = 5236002029735771778L;
	private static volatile CaffeineBuilder caffeineBuilder = null;

	private CaffeineBuilder() {
		// Preventing attack by Reflection APIs
		if (caffeineBuilder != null) {
			LOGGER.error("Cant instantiate single object using constrictor");
			throw new RuntimeException("Cant instantiate single object using constrictor");
		}
	}

	public static CaffeineBuilder getInstance() {
		// Double-locking to prevent ambiguity in multi-thread environment.
		if (caffeineBuilder == null) {
			synchronized (CaffeineBuilder.class) {
				if (caffeineBuilder == null) {
					caffeineBuilder = new CaffeineBuilder();
					List<CaffeineCache> cacheList = new ArrayList<>();
					String appName = System.getProperty("cache.app.name");
					if ("SM".equalsIgnoreCase(appName)) {
						cacheList.add(buildExpiringCache("CACHE1", 60, 1000));
						cacheList.add(buildNoneExpiringCache("CACHE2"));
						cacheList.add(buildNoneExpiringCache("CACHE3"));
					}
					caffeineBuilder.setCaches(cacheList);
				}
			}
		}
		LOGGER.debug("CaffeineBuilder created.");
		return caffeineBuilder;
	}

	private static CaffeineCache buildExpiringCache(String cacheName, int minuteToExpire, int size) {
		return new CaffeineCache(cacheName, Caffeine.newBuilder().expireAfterWrite(minuteToExpire, TimeUnit.MINUTES)
				.maximumSize(size).removalListener(new CustomRemovalListener()).recordStats().build());
	}

	private static CaffeineCache buildNoneExpiringCache(String cacheName) {
		return new CaffeineCache(cacheName,
				Caffeine.newBuilder().removalListener(new CustomRemovalListener()).recordStats().build());
	}

	public Object readResolver() {
		return CaffeineBuilder.getInstance();
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}

class CustomRemovalListener implements RemovalListener<Object, Object> {
	private static final Log LOGGER = LogFactory.getLog(CustomRemovalListener.class);
	
	@Override
	public void onRemoval(Object key, Object value, RemovalCause cause) {
		LOGGER.info(key + " : " + cause);
	}
}
