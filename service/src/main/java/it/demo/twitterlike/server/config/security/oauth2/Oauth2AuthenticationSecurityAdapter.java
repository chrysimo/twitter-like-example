package it.demo.twitterlike.server.config.security.oauth2;

import it.demo.twitterlike.server.config.utils.ProfileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.cloud.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.cloud.security.oauth2.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.stereotype.Component;

/**
 * 
 * Configuration Oauth 2 for web and rest pages
 * 
 * @author Christian Simonelli
 *
 */
@Profile(ProfileUtils.PROFILE_SECURITY_OAUTH2)
@EnableOAuth2Resource
@Configuration
public class Oauth2AuthenticationSecurityAdapter {

	@Autowired
	private ResourceServerProperties resource;

	@Bean
	public ResourceServerConfigurer resourceServer() {
		return new ResourceSecurityConfigurer(resource);
	}

	/**
	 * 
	 * Api Invocation Resource Configuration
	 * 
	 * @author Christian Simonelli
	 *
	 */

	protected static class ResourceSecurityConfigurer extends
			ResourceServerConfigurerAdapter {

		private ResourceServerProperties resource;

		public ResourceSecurityConfigurer(ResourceServerProperties resource) {
			this.resource = resource;
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.resourceId(resource.getResourceId());
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http.requestMatchers()
					.antMatchers("/rest/api/users/**", "/rest/api/*/me/**",
							"/rest/api/messages/**").and().authorizeRequests()
					.anyRequest().authenticated();

		}

	}

	/**
	 * Protected Web pages configuration.
	 * 
	 * @author Christian Simonelli
	 *
	 */
	@Profile(ProfileUtils.PROFILE_SECURITY_OAUTH2)
	@Component
	@EnableOAuth2Sso
	public static class LoginConfigurer extends OAuth2SsoConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/dashboard/**").authorizeRequests().anyRequest()
					.authenticated();
		}
	}

}