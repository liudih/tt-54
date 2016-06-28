package com.tomtop.advert.configuration;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

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

@Configuration
@EnableCaching
@EnableConfigurationProperties({ JdbcConnectionSettings.class,
		RedisConfigSettings.class })
public class ApplicationConfigurations {

	@Autowired
	private RedisConfigSettings settings;

	@Bean(name = "cacheManager")
	@Primary
	public CacheManager getCacheManager(
			@SuppressWarnings("rawtypes") @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(settings.getExpireTime());
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
		factory.setTimeout(settings.getTimeout());
		factory.setDatabase(settings.getDb());
		return factory;
	}

	@Bean
	public SpringLiquibase liquibase(
			@Qualifier("mysqlDataSource") DataSource dataSource) {
		SpringLiquibase lb = new SpringLiquibase();
		lb.setDataSource(dataSource);
		lb.setChangeLog("classpath:liquibase/mysql/*.xml");
		// contexts specifies the runtime contexts to use.
		return lb;
	}
}
