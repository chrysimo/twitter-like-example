package it.demo.twitterlike.server.config.security.oauth2.mock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

/**
 * Mock Token Service Configuration with mocked user profile
 * 
 * @author Christian Simonelli
 *
 */
@Component
@ConditionalOnExpression("'${oauth2.resource.userInfo.mock.username:${vcap.services.sso.userInfo.mock.username:}}'!=''")
@EnableConfigurationProperties(OAuth2MokedClientProperties.class)
public class MockResourceServerTokenServicesConfigurer implements
		ResourceServerTokenServices {

	public MockResourceServerTokenServicesConfigurer() {
		System.out.println();
	}
	@Autowired
	private OAuth2MokedClientProperties properties;

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		List<GrantedAuthority> grantedAuthorities = properties
				.getGrantedAuthorities() == null ? null : AuthorityUtils
				.createAuthorityList(properties.getGrantedAuthorities()
						.toArray(new String[] {}));
		User realuser = new User(properties.getUsername(), "N/A",
				grantedAuthorities);
		UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
				realuser, "N/A", grantedAuthorities);
		user.setDetails(realuser);
		OAuth2Request request = new OAuth2Request(null, "N/A", null, true,
				null, null, null, null, null);
		return new OAuth2Authentication(request, user);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException(
				"Not supported: read access token");
	}
}