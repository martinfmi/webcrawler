package org.javaknights.crawler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.javaknights.crawler.db.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
//@EnableJpaRepositories("org.javaknights.crawler")
public class ApplicationConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				System.out.println("Request comming: " + request.getPathInfo());
				return true;
			}
		});
	}
	
	@Autowired
	private ApplicationProperties properties;

	public void setProperties(ApplicationProperties properties) {
		this.properties = properties;
	}

	@Qualifier("dbpersistencemanager")
	@Bean
//	@DependsOn("filePersistenceManager")
//	@Scope("prototype")
//	@Lazy
	public PersistenceManager createPersistenceManager() {
		return new PersistenceManager();
	}

//	@Bean
//	public DataSource createDataSource() {
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
////	        dataSourceBuilder.driverClassName("org.h2.Driver");
//		dataSourceBuilder.url(properties.getUrl());
//		dataSourceBuilder.username(properties.getUser());
//		dataSourceBuilder.password(properties.getPass());
//		return dataSourceBuilder.build();
//	}

}
