package it.demo.twitterlike.rest.connect;

import it.demo.twitterlike.rest.api.TwitterLike;

import org.springframework.hateoas.Resource;
import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

public class TwitterLikeAdapter implements ApiAdapter<TwitterLike> {

	public boolean test(TwitterLike api) {
		try {
			api.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	public void setConnectionValues(TwitterLike api, ConnectionValues values) {
		Resource<it.demo.twitterlike.rest.api.UserProfile> resourceProfile = api
				.userOperations().getUserProfile();
		values.setProviderUserId(resourceProfile.getContent().getId());
		values.setDisplayName(resourceProfile.getContent().getFirstName() + " "
				+ resourceProfile.getContent().getLastName());
		values.setProfileUrl(resourceProfile.getId().getHref());

	}

	public UserProfile fetchUserProfile(TwitterLike api) {
		it.demo.twitterlike.rest.api.UserProfile profile = api.userOperations()
				.getUserProfile().getContent();
		return new UserProfileBuilder().setFirstName(profile.getFirstName())
				.setLastName(profile.getLastName())
				.setEmail(profile.getEmail()).setUsername(profile.getId())
				.build();
	}

	public void updateStatus(TwitterLike api, String message) {
	}

}
