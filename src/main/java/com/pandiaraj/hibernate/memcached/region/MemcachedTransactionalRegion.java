package com.pandiaraj.hibernate.memcached.region;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.MemcachedCache;

public class MemcachedTransactionalRegion extends MemcachedRegion implements TransactionalDataRegion {
	
	protected final Settings settings;
	protected final CacheDataDescription metadata;
	
	public MemcachedTransactionalRegion(MemcachedCache cache, Settings settings, CacheDataDescription metadata, Properties properties) {
		super(cache, properties);
		this.settings = settings;
		this.metadata = metadata;
	}

	public boolean isTransactionAware() {
		return false;
	}

	public CacheDataDescription getCacheDataDescription() {
		return metadata;
	}
	
	public Object get(Object key) throws CacheException {
		return cache.get(key);
	}
	
	public void put(Object key, Object value) throws CacheException {
		cache.put(key, cacheDurationInSecs, value);
	}
	
	public void remove(Object key) throws CacheException {
		cache.remove(key);
	}
	
	public void clear() throws CacheException {
		cache.clear();
	}
	
	public boolean writeLock(Object key) {
		try {
			return cache.lock(key, 1000L);
		}catch(InterruptedException e) {
			return false;
		}
	}
	
	public void releaseLock(Object key) {
		cache.unlock(key);
	}

}
