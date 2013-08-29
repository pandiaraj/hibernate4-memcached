package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedCollectionRegion;

public class ReadOnlyMemcachedCollectionRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedCollectionRegion> 
implements CollectionRegionAccessStrategy {

	public ReadOnlyMemcachedCollectionRegionAccessStrategy(	MemcachedCollectionRegion region, Settings settings) {
		super(region, settings);
	}

	public Object get(Object key, long txTimestamp) throws CacheException {
		return region.get(key);
	}

	public SoftLock lockItem(Object key, Object version) throws CacheException {
		throw new UnsupportedOperationException("Can't write to a readonly object");
	}

	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}

	public CollectionRegion getRegion() {
		return region;
	}

	@Override
	public boolean putFromLoad(Object key, Object value, long txnTimestamp, Object version, boolean minimumPutOverride) throws CacheException {
		if(minimumPutOverride && region.contains(key)) {
			return false;
		} else {
			region.put(key, value);
			return true;
		}
	}

}
