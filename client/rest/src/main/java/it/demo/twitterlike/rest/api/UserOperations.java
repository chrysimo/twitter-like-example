package it.demo.twitterlike.rest.api;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.social.ApiException;
import org.springframework.social.MissingAuthorizationException;

public interface UserOperations {

	PagedResources<Resource<UserProfile>> findAll(Integer page, Integer size,
			String sort);

	PagedResources<Resource<UserProfile>> findAll(Link link);

	PagedResources<Resource<UserProfile>> findFollowers(String userId,
			Integer page, Integer size, String sort);

	PagedResources<Resource<UserProfile>> findFollow(String userId,
			Integer page, Integer size, String sort);

	Resource<UserProfile> getByLink(Link link);

	/**
	 * Retrieves the profile for the authenticated user.
	 * 
	 * @return the user's profile information.
	 * @throws ApiException
	 *             if there is an error while communicating with Server.
	 * @throws MissingAuthorizationException
	 *             if Template was not created with an access token.
	 */
	Resource<UserProfile> getUserProfile();

	Resource<UserProfile> getUserProfile(String id);

	PagedResources<Resource<UserProfile>> findMyFollow(Integer page,
			Integer size, String sort);

	void follow(Resource<UserProfile>... users);

	void follow(Iterable<? extends UserProfile> users);

	void follow(UserProfile... users);

	void follow(String... users);

	void unfollow(String... users);

}
