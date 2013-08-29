package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedTransactionalRegion;

abstract class AbstractMemcachedAccessStrategy<T extends MemcachedTransactionalRegion> {
	
	protected final T region;
	protected final Settings settings;
	
	public AbstractMemcachedAccessStrategy(T region, Settings settings) {
		this.region = region;
		this.settings = settings;
	}
	
	public final boolean putFromLoad(Object key, Object value, long txnTimestamp, Object version) throws CacheException {
		return putFromLoad(key, value, txnTimestamp, version, settings.isMinimalPutsEnabled());
	}
	
	public abstract boolean putFromLoad(Object key, Object value, long txnTimestamp, Object version, boolean minimumPutOverride)
	throws CacheException;
	
	public final SoftLock lockRegion() {
		return null;
	}
	
	public final void unlockRegion(SoftLock lock) throws CacheException {
		region.clear();
	}
	
	public void remove(Object key) throws CacheException {
	}
	
	public final void removeAll() throws CacheException {
		region.clear();
	}
	
	public final void evict(Object key) throws CacheException {
		region.remove(key);
	}
	
	public final void evictAll() throws CacheException {
		region.clear();
	}

}
