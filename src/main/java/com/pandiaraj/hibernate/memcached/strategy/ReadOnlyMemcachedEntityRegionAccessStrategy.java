package com.pandiaraj.hibernate.memcached.strategy;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedEntityRegion;

public class ReadOnlyMemcachedEntityRegionAccessStrategy extends AbstractMemcachedAccessStrategy<MemcachedEntityRegion> 
implements EntityRegionAccessStrategy {
	
	public ReadOnlyMemcachedEntityRegionAccessStrategy(MemcachedEntityRegion region, Settings settings) {
		super(region, settings);
	}
	
	public EntityRegion getRegion() {
		return region;
	}
	
	public Object get(Object key, long txnTimestamp) throws CacheException {
		return region.get(key);
	}

	@Override
	public boolean putFromLoad(Object key, Object value, long txnTimestamp,	Object version, boolean minimumPutOverride) throws CacheException {
		if(minimumPutOverride && region.contains(key)) {
			return false;
		} else {
			region.put(key, value);
			return true;
		}
	}
	
	public SoftLock lockItem(Object key, Object version) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Can't write to a readonly object");
	}
	
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}
	
	public boolean insert(Object key, Object value, Object version) throws CacheException {
		return false;
	}
	
	public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
		region.put(key, value);
		return true;
	}
	
	public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Can't write to a readonly object");
	}
	
	public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Can't write to a readonly object");
	}

}
