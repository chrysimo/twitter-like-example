package it.demo.twitterlike.server.config.security.oauth2;

import it.demo.twitterlike.server.config.utils.ProfileUtils;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * 
 * Enable embedded oauth server
 * @author Christian Simonelli
 *
 */
@Profile(ProfileUtils.PROFILE_SECURITY_OAUTH2)
@EnableAuthorizationServer
@ConditionalOnExpression("${oauth2.server.embedded.enabled:true}")
@Configuration
public class OAuth2AuthorizationServerConfigurerAdapter extends
		AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource datasource;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		clients.jdbc(datasource)
				.withClient("acme")
				.secret("acmesecret")
				.authorizedGrantTypes(
						"authorization_code"/* , "refresh_token" */, "password")
				.scopes("openid");

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {

		endpoints.authenticationManager(authenticationManager).tokenServices(
				createDefaultTokenServices());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {

		oauthServer.checkTokenAccess("permitAll()");
	}

	@Bean
	@Primary
	public DefaultTokenServices createDefaultTokenServices() throws Exception {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(new JdbcTokenStore(datasource));
		// Disable refresh token
		tokenServices.setSupportRefreshToken(false);
		// Enabled infinte token validity
		tokenServices.setAccessTokenValiditySeconds(0);
		return tokenServices;
	}

}
