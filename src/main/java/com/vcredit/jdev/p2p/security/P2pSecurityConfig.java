package com.vcredit.jdev.p2p.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.vcredit.jdev.p2p.base.util.PropertiesUtils;

@ConditionalOnClass(AuthenticationManager.class)
@Configuration
public class P2pSecurityConfig {
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 10)
	public static class FormWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private PropertiesUtils propertiesUtils;
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/j_spring_security_logout|/app/acct/.*")
				.csrf().disable()
				.formLogin()
					.loginPage("/app/account/login.html")
					//.loginProcessingUrl("/app/account/login")
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
						.logoutUrl("/j_spring_security_logout")
						.logoutSuccessUrl(propertiesUtils.getAppUrl())
				.and()
					.sessionManagement()
	                .maximumSessions(1)
	                .expiredUrl("/app/account/login.html?expired")
	                .maxSessionsPreventsLogin(true)
	                .and()
	                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	               // .invalidSessionUrl(propertiesUtils.getAppUrl())
				.and()
					.authorizeRequests()
						.regexMatchers("/app/acct/.*").permitAll()
						.regexMatchers("/j_spring_security_logout").authenticated()
				.and()
					.rememberMe();
		}
	}
	
	/**
	 * For rest config
	 * @author xufucheng
	 *
	 */
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 9)
	public static class WSWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/data/ws/.*|/data/odata.svc.*")
				.csrf().disable()
				.httpBasic()
				.and()
					.authorizeRequests()
						.regexMatchers("/data/ws/.*", "/data/odata.svc.*").permitAll();
		}
	}
}
