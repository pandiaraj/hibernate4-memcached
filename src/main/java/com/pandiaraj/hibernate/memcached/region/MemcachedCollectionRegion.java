package com.pandiaraj.hibernate.memcached.region;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.MemcachedCache;
import com.pandiaraj.hibernate.memcached.strategy.NonStrictReadWriteMemcachedCollectionRegionAccessStrategy;
import com.pandiaraj.hibernate.memcached.strategy.ReadOnlyMemcachedCollectionRegionAccessStrategy;
import com.pandiaraj.hibernate.memcached.strategy.ReadWriteMemcachedCollectionRegionAccessStrategy;

public class MemcachedCollectionRegion extends MemcachedTransactionalRegion implements CollectionRegion {

	public MemcachedCollectionRegion(MemcachedCache cache, Settings settings, CacheDataDescription metadata, Properties properties) {
		super(cache, settings, metadata, properties);
	}

	public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		if(AccessType.READ_ONLY.equals(accessType)) {
			return new ReadOnlyMemcachedCollectionRegionAccessStrategy(this, settings);
		} else if(AccessType.READ_WRITE.equals(accessType)) {
			return new ReadWriteMemcachedCollectionRegionAccessStrategy(this, settings);
		} else if(AccessType.NONSTRICT_READ_WRITE.equals(accessType)) {
			return new NonStrictReadWriteMemcachedCollectionRegionAccessStrategy(this, settings);
		} else if(AccessType.TRANSACTIONAL.equals(accessType)) {
			throw new CacheException("Transactional access is not supported by Memcached region factory");
		} else {
			throw new UnsupportedOperationException("Uknown access strategy type - " + accessType);
		}
	}

}
