package com.caffeine.cache.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.caffeine.cache.CacheSerevice;
import com.caffeine.constant.CaffeineConstants;
import com.rits.cloning.Cloner;

@Service
public class CacheServiceImpl implements CacheSerevice {

	private static Cloner cloner = new Cloner();

	@Autowired
	private CacheManager cacheManager;

	@Override
	@Caching(evict = {
			@CacheEvict(value = { CaffeineConstants.CACHE1, CaffeineConstants.CACHE2, CaffeineConstants.CACHE3 }, allEntries = true) })
	public String clearAllCaches() {
		return CaffeineConstants.SUCCESS;
	}

	@Override
	public void clearSingleCache(String cacheName) {
		if (getCache(cacheName) != null) {
			getCache(cacheName).clear();
		}
	}

	@Override
	public <K> void remove(String cacheName, K key) {
		if (getCache(cacheName) != null) {
			getCache(cacheName).evict(key);
		}
	}

	@Override
	public Long size(String cacheName) {
		return getNativeCache(cacheName) != null ? getNativeCache(cacheName).estimatedSize() : null;
	}

	@Override
	public <K> Object get(String cacheName, K key) {
		ValueWrapper valueWrapper = null;
		if (getCache(cacheName) != null) {
			valueWrapper = getCache(cacheName).get(key);
		}
		return valueWrapper != null ? cloner.deepClone(valueWrapper.get()) : null;
	}

	@Override
	public <K, V> Map<K, V> getAll(String cacheName) {
		com.github.benmanes.caffeine.cache.Cache<K, V> cache = null;
		if (getNativeCache(cacheName) != null) {
			cache = getNativeCache(cacheName);
		}
		return cache != null ? cache.asMap() : null;
	}

	@Override
	public <K, V> void put(String cacheName, K key, V value) {
		if (getCache(cacheName) != null) {
			getCache(cacheName).put(key, value);
		}
	}

	@Override
	public <K, V> void putAll(String cacheName, Map<K, V> value) {
		if (getNativeCache(cacheName) != null) {
			getNativeCache(cacheName).putAll(value);
		}
	}

	private org.springframework.cache.Cache getCache(String cacheName) {
		return cacheManager.getCache(cacheName);
	}

	@SuppressWarnings("unchecked")
	private <K, V> com.github.benmanes.caffeine.cache.Cache<K, V> getNativeCache(String cacheName) {
		return getCache(cacheName) != null
				? (com.github.benmanes.caffeine.cache.Cache<K, V>) (getCache(cacheName).getNativeCache()) : null;
	}

}