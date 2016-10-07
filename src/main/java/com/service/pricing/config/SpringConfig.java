package com.service.pricing.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

@ComponentScan({"com.service.pricing"})
@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	@Bean
	public Cache cacheManager() {
		CacheManager c = new EhCacheCacheManager(ehCacheCacheManager().getObject());
		return c.getCache("productPriceCache");
	}
	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}
	/*@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		 PropertySourcesPlaceholderConfigurer p =new PropertySourcesPlaceholderConfigurer();
		return new PropertySourcesPlaceholderConfigurer();
	}
*/
	
}
