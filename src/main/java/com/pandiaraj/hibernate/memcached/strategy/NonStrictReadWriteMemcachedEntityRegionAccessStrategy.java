package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedEntityRegion;

public class NonStrictReadWriteMemcachedEntityRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedEntityRegion> 
implements EntityRegionAccessStrategy {

	public NonStrictReadWriteMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region, Settings settings) {
		super(region, settings);
	}

	public Object get(Object key, long txTimestamp) throws CacheException {
		return region.get(key);
	}

	public SoftLock lockItem(Object key, Object version) throws CacheException {
		return null;
	}

	public void unlockItem(Object key, SoftLock lock) throws CacheException {
		region.remove(key);
	}

	public EntityRegion getRegion() {
		return region;
	}

	public boolean insert(Object key, Object value, Object version) throws CacheException {
		return false;
	}

	public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
		return false;
	}

	public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
		remove(key);
		return false;
	}

	public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws CacheException {
		return false;
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
