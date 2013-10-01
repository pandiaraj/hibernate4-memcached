package com.pandiaraj.hibernate.memcached.spymemcached;

import java.util.concurrent.ExecutionException;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.hibernate.cache.CacheException;

import com.pandiaraj.hibernate.memcached.MemcachedCache;

public class MemcachedCacheImpl implements MemcachedCache {
	
	private String regionName;
	private MemcachedClient client;
	private boolean locked = false;
	
	public MemcachedCacheImpl(String regionName, MemcachedClient client) {
		this.regionName = regionName;
		this.client = client;
	}

	public String getName() {
		return regionName;
	}

	public boolean exists(Object key) {
		Object value = get(key);
		return value != null;
	}

	public Object get(Object key) throws CacheException {
		return client.get(key.toString());
	}

	public void put(Object key, int expirationInSecs, Object value) throws CacheException {
		client.set(key.toString(), expirationInSecs, value);
	}

	public void remove(Object key) throws CacheException {
		client.delete(key.toString());
	}

	public void clear() throws CacheException {
		client.flush();
	}

	public long getSizeInMemory() {
		return -1;
	}

	public long getElementCountInMemory() {
		return -1;
	}

	public long getElementCountOnDisk() {
		return -1;
	}

	public int getTimeout() {
		return 0;
	}

	public boolean lock(Object key, long durationMSecs) throws InterruptedException {
		int timeout = getTimeout();
		String lockKey = generateLockKey(key);
		long expires = System.currentTimeMillis() + durationMSecs + 1;
		String expiresStr = String.valueOf(expires);
		
		while(timeout >= 0) {
			OperationFuture<Boolean> result = client.add(lockKey, (int)durationMSecs/1000, expiresStr);
			try {
				Boolean success = result.get();
				if(success) {
					locked = success;
					return success;
				}
			}catch(ExecutionException e) {
				return false;
			}
			
			String currentValueStr = (String) get(lockKey);
			if(currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
				String oldValueStr = (String) get(lockKey);
				put(lockKey, (int)durationMSecs/1000, expiresStr);
				if(oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					locked = true;
					return true;
				}
			}
			
			timeout -= 100;
			Thread.sleep(100);
		}
		
		return false;
	}

	public void unlock(Object key) {
		if(locked) {
			remove(generateLockKey(key));
			locked = false;
		}
	}

	public void destroy() {
	}
	
	private String generateLockKey(Object key) {
		if(key == null) {
			throw new IllegalArgumentException("Key must now be null");
		}
		
		return key.toString() + ".lock";
	}

}
