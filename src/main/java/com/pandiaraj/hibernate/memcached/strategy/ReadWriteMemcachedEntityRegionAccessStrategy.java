package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedEntityRegion;

public class ReadWriteMemcachedEntityRegionAccessStrategy extends AbstractReadWriteMemcachedAccessStrategy<MemcachedEntityRegion> 
implements EntityRegionAccessStrategy {
	
	public ReadWriteMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region, Settings settings) {
		super(region, settings);
	}

	public SoftLock lockItem(Object key, Object version) throws CacheException {
		return null;
	}

	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}

	public EntityRegion getRegion() {
		return region;
	}

}
