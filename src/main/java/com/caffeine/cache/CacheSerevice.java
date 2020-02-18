package com.caffeine.cache;

import java.util.Map;

public interface CacheSerevice {

	public String clearAllCaches();

	public void clearSingleCache(String cacheName);

	public <K> void remove(String cacheName, K key);

	public Long size(String cacheName);

	public <K> Object get(String cacheName, K key);

	public <K, V> Map<K, V> getAll(String cacheName);

	public <K, V> void put(String cacheName, K key, V value);

	public <K, V> void putAll(String cacheName, Map<K, V> value);

}