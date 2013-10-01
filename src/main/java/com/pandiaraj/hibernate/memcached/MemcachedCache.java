package com.pandiaraj.hibernate.memcached;

import org.hibernate.cache.CacheException;

public interface MemcachedCache {
	
	String getName();
	boolean exists(Object key);
	Object get(Object key) throws CacheException;
	void put(Object key, int expirationInSecs, Object value) throws CacheException;
	void remove(Object key) throws CacheException;
	void clear() throws CacheException;
	long getSizeInMemory();
	long getElementCountInMemory();
	long getElementCountOnDisk();
	int getTimeout();
	boolean lock(Object key, long durationMSecs) throws InterruptedException;
	void unlock(Object key);
	void destroy();

}
