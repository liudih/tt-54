package com.tomtop.product.configuration;

import java.net.InetAddress;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONArray;

@Configuration
@EnableCaching
@EnableConfigurationProperties({ JdbcConnectionSettings.class,
		RedisConfigSettings.class, EsConfigSettings.class })
public class ApplicationConfigurations {

	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationConfigurations.class);

	@Autowired
	private RedisConfigSettings settings;

	@Autowired
	private EsConfigSettings esSetting;

	@Bean(name = "cacheManager")
	@Primary
	public CacheManager getCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getExpireTime());
		return cacheManager;
	}

	@Bean(name = "dayCacheManager")
	public CacheManager getDayCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getDailyExpireTime());
		return cacheManager;
	}

	@Bean(name = "productPageCacheManager")
	public CacheManager getProductPageCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getProductPageExpireTime());
		return cacheManager;
	}

	@Bean(name = "recentOrdersCacheManager")
	public CacheManager getRecentOrdersCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings
				.getRecentOrdersPageExpireTime());
		return cacheManager;
	}

	@SuppressWarnings("rawtypes")
	@Bean(name = "redisTemplate")
	public RedisTemplate getRedisTemplate(
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		return redisTemplate;
	}

	@Bean(name = "jedisConnectionFactory")
	public JedisConnectionFactory getJedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(settings.getIp());
		factory.setPassword(settings.getPassword());
		factory.setPort(settings.getPort());
		factory.setDatabase(settings.getDb());
		factory.setTimeout(settings.getTimeout());
		return factory;
	}

	@Bean(name = "productLiquibase")
	public SpringLiquibase liquibase(
			@Qualifier("mysqlProductDataSource") DataSource dataSource) {
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		// contexts specifies the runtime contexts to use.
		return lb;
	}

	@Bean(name = "indexClient")
	public TransportClient indexClient() throws Exception {

		Settings settings = Settings
				.settingsBuilder()
				.put("number_of_shards", esSetting.getNumberOfShards())
				.put("number_of_replicas", esSetting.getNumberOfReplicas())
				.put("client.transport.sniff",
						esSetting.getClientTransportSniff())
				.put("cluster.name", esSetting.getClusterName())
				.put("client.transport.ping_timeout",
						esSetting.getClientTransportPingTimeout()).build();

		TransportClient indexClient = TransportClient.builder()
				.settings(settings).build();
		JSONArray ips = esSetting.getClientNodesIp();
		JSONArray ports = esSetting.getClientNodesPort();

		if (ips == null || ports == null || ips.size() != ports.size()) {
			throw new RuntimeException(
					"ClientNodesIp && ClientNodesPort config error");
		}

		for (int i = 0; i < ips.size(); i++) {
			InetAddress host = InetAddress.getByName(ips.getString(i));
			int portNo = ports.getInteger(i);
			InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
					host, portNo);
			indexClient.addTransportAddress(transportAddress);
		}

		return indexClient;

	}

	@Bean(name = "transportClient")
	public TransportClient transportClient() throws Exception {

		Settings settings = Settings
				.settingsBuilder()
				.put("client.transport.sniff",
						esSetting.getClientTransportSniff())
				.put("cluster.name", esSetting.getClusterName())
				.put("client.transport.ping_timeout",
						esSetting.getClientTransportPingTimeout()).build();

		TransportClient indexClient = TransportClient.builder()
				.settings(settings).build();
		JSONArray ips = esSetting.getClientNodesIp();
		JSONArray ports = esSetting.getClientNodesPort();

		if (ips == null || ports == null || ips.size() != ports.size()) {
			throw new RuntimeException(
					"ClientNodesIp && ClientNodesPort config error");
		}

		for (int i = 0; i < ips.size(); i++) {
			InetAddress host = InetAddress.getByName(ips.getString(i));
			int portNo = ports.getInteger(i);
			InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
					host, portNo);
			indexClient.addTransportAddress(transportAddress);
		}

		return indexClient;

	}

}
