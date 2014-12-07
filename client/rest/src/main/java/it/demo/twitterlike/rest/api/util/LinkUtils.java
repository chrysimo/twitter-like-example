package it.demo.twitterlike.rest.api.util;

import it.demo.twitterlike.rest.api.Message;
import it.demo.twitterlike.rest.api.UserProfile;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class LinkUtils {

	public static final String LINK_NAME_USER_FOLLOWERS = "followers";
	public static final String LINK_NAME_USER_FOLLOWING = "following";
	public static final String LINK_NAME_USER_MESSAGES = "messages";

	public static final String LINK_NAME_MESSAGE_AUTHOR = "author";

	public static Link getSelf(Resource<?> resource) {
		return resource.getId();
	}

	public static Link getMessageAuthorLink(Resource<Message> message) {
		return message.getLink(LINK_NAME_MESSAGE_AUTHOR);
	}

	public static Link getUserFollowersLink(Resource<UserProfile> message) {
		return message.getLink(LINK_NAME_USER_FOLLOWERS);
	}

	public static Link getUserFollowingLink(Resource<UserProfile> message) {
		return message.getLink(LINK_NAME_USER_FOLLOWING);
	}

	public static Link getUserMessagesLink(Resource<UserProfile> message) {
		return message.getLink(LINK_NAME_USER_MESSAGES);
	}

}
