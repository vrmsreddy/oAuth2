package org.ms.oauth2.wso2.config;

import org.ms.oauth2.wso2.service.TokenServiceWSO2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

/**
 * @author MS
 *
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends ResourceServerConfigurerAdapter{
	@Bean
	public TokenServiceWSO2 getTokenService(){
		return new TokenServiceWSO2();
	}
	@Bean
	public OAuth2AuthenticationEntryPoint getAuthEntryPoint(){
		return new OAuth2AuthenticationEntryPoint();
	}
	
	 
	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
			throws Exception {
		/*resources
				.authenticationEntryPoint(getAuthEntryPoint());*/
		resources.tokenServices(getTokenService());
		super.configure(resources);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/protected/**").and()
				.authorizeRequests().anyRequest().authenticated();
	}
}
