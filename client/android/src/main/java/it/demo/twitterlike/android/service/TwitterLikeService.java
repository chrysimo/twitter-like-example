package it.demo.twitterlike.android.service;

import it.demo.twitterlike.rest.api.UserProfile;

public interface TwitterLikeService {

	void syncronize();

	UserProfile getAuthenticatedUser();

}
