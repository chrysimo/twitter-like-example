package it.demo.twitterlike.server.web.api.domain;

import it.demo.twitterlike.server.domain.User;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class UserResource extends Resource<User>{
public static final String LINK_NAME_FOLLOWERS = "followers";
public static final String LINK_NAME_FOLLOWING = "following";
public static final String LINK_NAME_MESSAGES = "messages";


	public UserResource(User content, Iterable<Link> links) {
		super(content, links);
	}
	
	public UserResource(User content, Link... links) {
		super(content, links);
	}
	
	public String getUsername() {
		return getContent().getLogin();
	}

}
