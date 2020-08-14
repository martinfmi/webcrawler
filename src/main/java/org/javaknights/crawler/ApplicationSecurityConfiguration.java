package org.javaknights.crawler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.mvcMatchers("/app/start").hasRole("APP_ADMIN")
		.and().formLogin().loginPage("/login")
		.and().logout().logoutUrl("/app/logout").permitAll();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		// ROLE_APP_ADMIN
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("test")
				.password("test")
				.roles("APP_ADMIN").build();
		// ROLE_GUEST
		UserDetails user2 = User.withDefaultPasswordEncoder()
				.username("test2")
				.password("test2")
				.roles("GUEST").build();
		
		return new InMemoryUserDetailsManager(user, user2);
	}
	
}
