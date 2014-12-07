package it.demo.twitterlike.server.security;

import it.demo.twitterlike.server.domain.User;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class InternalUserDetails extends org.springframework.security.core.userdetails.User{

	private static final long serialVersionUID = 1L;
	private final User user;
	
	
	public InternalUserDetails(User user,
			Collection<? extends GrantedAuthority> authorities) {
		super(user.getLogin(), user.getPassword(), authorities);
		this.user = user;
		
	}
	
	
	public User getUser() {
		return user;
	}

	
	
}
