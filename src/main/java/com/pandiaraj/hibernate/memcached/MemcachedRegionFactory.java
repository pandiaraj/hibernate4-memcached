package com.pandiaraj.hibernate.memcached;

import java.io.IOException;
import java.util.Properties;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;

public class MemcachedRegionFactory extends AbstractMemcachedRegionFactory {

	private static final long serialVersionUID = 3052887042898413129L;

	public void start(Settings settings, Properties properties) throws CacheException {
		this.settings = settings;
		this.properties = properties;
		try {
			String host = properties.getProperty("hibernate.memcached.host", "localhost");
			String port = properties.getProperty("hibernate.memcached.port", "11211");
			client = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(host + ":" + port));
		} catch (IOException e) {
			throw new CacheException(e);
		}
	}

	public void stop() {
		client.shutdown();
	}

}
