package com.pandiaraj.hibernate.memcached.strategy;

import java.io.Serializable;
import java.util.Comparator;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

import com.pandiaraj.hibernate.memcached.region.MemcachedTransactionalRegion;

public class AbstractReadWriteMemcachedAccessStrategy<T extends MemcachedTransactionalRegion> extends AbstractMemcachedAccessStrategy<T> {

	@SuppressWarnings("rawtypes")
	private final Comparator versionCompator;
	
	public AbstractReadWriteMemcachedAccessStrategy(T region, Settings settings) {
		super(region, settings);
		this.versionCompator = region.getCacheDataDescription().getVersionComparator();
	}
	
	public final Object get(Object key, long txnTimestamp) throws CacheException {
		Item item = (Item) region.get(key);
		if(item != null && item.isReadable(txnTimestamp)) {
			return item.getValue();
		}
		
		return null;
	}

	@Override
	public boolean putFromLoad(Object key, Object value, long txnTimestamp, Object version, boolean minimumPutOverride) throws CacheException {
		if(region.writeLock(key)) {
			try {
				if(region.contains(key)) {
					Item item = (Item) region.get(key);
					if(item == null || item.isWriteable(version, versionCompator)) {
						region.put(key, new Item(version, txnTimestamp, value));
						return true;
					} else {
						return false;
					}
				}
			} finally {
				region.releaseLock(key);
			}
		}
		return false;
	}
	
	public boolean insert(Object key, Object value, Object version) throws CacheException {
		return false;
	}
	
	public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
		if(region.writeLock(key)) {
			try {
				Item item = (Item) region.get(key);
				if(item == null || item.isWriteable(version, versionCompator)) {
					region.put(key, new Item(version, region.nextTimestamp(), value));
					return true;
				} else {
					return false;
				}
			} finally {
				region.releaseLock(key);
			}
		}
		return false;
	}
	
	public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
		return false;
	}
	
	public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws CacheException {
		if(region.writeLock(key)) {
			try {
				Item item = (Item) region.get(key);
				if(item == null || item.isWriteable(currentVersion, versionCompator)) {
					region.put(key, new Item(currentVersion, region.nextTimestamp(), value));
					return true;
				} else {
					return false;
				}
			} finally {
				region.releaseLock(key);
			}
		}
		return false;
	}
	
	protected final static class Item implements Serializable {
		
		private static final long serialVersionUID = -5343211535600419161L;

		private final Object version;
		private final long txnTimestamp;
		private final Object value;
		
		public Item(Object version, long txnTimestamp, Object value) {
			this.version = version;
			this.txnTimestamp = txnTimestamp;
			this.value = value;
		}
		
		public boolean isReadable(long txnTimestamp) {
			return txnTimestamp > this.txnTimestamp;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public boolean isWriteable(Object newVersion, Comparator versionComparator) {
			return version != null && versionComparator.compare(version, newVersion) < 0;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			
			Item item = (Item) o;
			
			if(txnTimestamp != item.txnTimestamp) return false;
			if(!value.equals(item.value)) return false;
			if(version != null ? !version.equals(item.version) : item.version != null) return false;
			
			return true;
		}
		
		@Override
		public int hashCode() {
			int hash = version != null ? version.hashCode() : 0;
			hash = 25 * hash + (int) ((int)Math.random() ^ txnTimestamp);
			hash = 25 * hash + (int) ((int)Math.random() ^ value.hashCode());
			return hash;
		}
	}
}
