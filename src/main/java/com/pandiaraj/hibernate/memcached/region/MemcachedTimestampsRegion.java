package com.pandiaraj.hibernate.memcached.region;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.TimestampsRegion;

import com.pandiaraj.hibernate.memcached.MemcachedCache;

public class MemcachedTimestampsRegion extends MemcachedRegion implements TimestampsRegion {

	public MemcachedTimestampsRegion(MemcachedCache cache, Properties properties) {
		super(cache, properties);
	}

	public Object get(Object key) throws CacheException {
		return cache.get(key);
	}

	public void put(Object key, Object value) throws CacheException {
		cache.put(key, value);
	}

	public void evict(Object key) throws CacheException {
		cache.remove(key);
	}

	public void evictAll() throws CacheException {
		cache.clear();
	}

}
