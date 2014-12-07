package it.demo.twitterlike.server.config.security.oauth2.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.resource.userInfo.mock")
public class OAuth2MokedClientProperties {

	private String username;

	private List<String> grantedAuthorities = new ArrayList<>();

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGrantedAuthorities(List<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public List<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public String getUsername() {
		return username;
	}
}
