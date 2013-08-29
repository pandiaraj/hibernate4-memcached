package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedCollectionRegion;

public class ReadWriteMemcachedCollectionRegionAccessStrategy extends AbstractReadWriteMemcachedAccessStrategy<MemcachedCollectionRegion>
implements CollectionRegionAccessStrategy {

	public ReadWriteMemcachedCollectionRegionAccessStrategy(MemcachedCollectionRegion region, Settings settings) {
		super(region, settings);
	}

	public SoftLock lockItem(Object key, Object version) throws CacheException {
		return null;
	}

	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}

	public CollectionRegion getRegion() {
		return region;
	}

}
