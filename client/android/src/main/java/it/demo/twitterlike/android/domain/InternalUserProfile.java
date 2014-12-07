package it.demo.twitterlike.android.domain;

import it.demo.twitterlike.android.utils.UserUtils;
import it.demo.twitterlike.rest.api.UserProfile;

import java.io.Serializable;

import org.springframework.hateoas.Link;

public class InternalUserProfile implements Serializable,
		Comparable<InternalUserProfile> {

	private static final long serialVersionUID = 1L;

	private final UserProfile user;

	private final Link self;

	private final Link messages;

	private boolean follow;

	public InternalUserProfile(UserProfile user, Link self, Link messages,
			boolean follow) {
		super();
		this.user = user;
		this.self = self;
		this.messages = messages;
		this.follow = follow;
	}

	@Override
	public int compareTo(InternalUserProfile another) {
		int result = UserUtils.getUserFullName(user).compareTo(
				UserUtils.getUserFullName(another.user));
		if (result == 0) {
			result = self.getHref().compareTo(another.getSelf().getHref());
		}
		return result;
	}

	public Link getSelf() {
		return self;
	}

	public boolean isFollow() {
		return follow;
	}

	public UserProfile getUser() {
		return user;
	}

	public Link getMessages() {
		return messages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((self == null) ? 0 : self.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InternalUserProfile other = (InternalUserProfile) obj;
		if (self == null) {
			if (other.self != null)
				return false;
		} else if (!self.equals(other.self))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InternalUserProfile [user="
				+ (user == null ? null : user.getId()) + "]";
	}
}
