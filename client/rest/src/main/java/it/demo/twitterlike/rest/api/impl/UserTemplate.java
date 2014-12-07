package it.demo.twitterlike.rest.api.impl;

import it.demo.twitterlike.rest.api.UserOperations;
import it.demo.twitterlike.rest.api.UserProfile;
import it.demo.twitterlike.rest.api.util.ResourceUtils;
import it.demo.twitterlike.rest.api.util.UserProfileUtils;

import java.util.Arrays;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

public class UserTemplate extends AbstractTemplate implements UserOperations {

	private final ParameterizedTypeReference<Resource<UserProfile>> singleTypeParameter = new ParameterizedTypeReference<Resource<UserProfile>>() {
	};

	private final ParameterizedTypeReference<PagedResources<Resource<UserProfile>>> pagedTypeParameter = new ParameterizedTypeReference<PagedResources<Resource<UserProfile>>>() {
	};

	public UserTemplate(String baseUrl, RestTemplate restTemplate) {
		super(baseUrl, restTemplate);
	}

	public PagedResources<Resource<UserProfile>> findAll(Integer page, Integer size, String sort) {
		return doFind(pagedTypeParameter, page, size, sort, buildUrl(getBaseContext()));
	}
	
	public PagedResources<Resource<UserProfile>> findAll(Link link) {
		return doFind(pagedTypeParameter, null, null, null, link.getHref());
	}

	public PagedResources<Resource<UserProfile>> findFollowers(String userId,
			Integer page, Integer size, String sort) {
		return doFind(pagedTypeParameter, page, size, sort,
				new UriTemplate(buildUrl(getBaseContext()
						+ "/{userId}/followers")).expand(userId).toString());
	}

	public PagedResources<Resource<UserProfile>> findMyFollow(
			Integer page, Integer size, String sort) {
		return doFind(pagedTypeParameter, page, size, sort,buildUrl(getBaseContext() + "/me/follows"));
	}

	
	
	public PagedResources<Resource<UserProfile>> findFollow(String userId,
			Integer page, Integer size, String sort) {
		return doFind(pagedTypeParameter, page, size, sort, new UriTemplate(
				buildUrl(getBaseContext() + "/{userId}/follows"))
				.expand(userId).toString());
	}

	public Resource<UserProfile> getByLink(Link link) {
		return getByLink(singleTypeParameter, link);
	}

	@Override
	public Resource<UserProfile> getUserProfile() {
		return getSingleResult(buildUrl(getBaseContext() + "/me"),
				singleTypeParameter);
	}

	public Resource<UserProfile> getUserProfile(String id) {
		return getById(singleTypeParameter, id);
	}

	public void follow(Resource<UserProfile>... users) {
		follow(ResourceUtils.getContents(users));
	}

	public void follow(UserProfile... users) {
		follow(Arrays.asList(users));
	}

	public void follow(Iterable<? extends UserProfile> users) {
		follow(UserProfileUtils.getUserIds(users));
	}

	public void follow(String... users) {

		restTemplate.postForObject(new UriTemplate(buildUrl(getBaseContext()
				+ "/me/follows/{users}")).expand(users).toString(), null,
				Void.class);

	}

	public void unfollow(String... users) {

		restTemplate.delete(new UriTemplate(buildUrl(getBaseContext()
				+ "/me/follows/{users}")).expand(users).toString(), null,
				Void.class);

	}

	public void delete(Long id) {
		restTemplate.delete(buildUrl(getBaseContext() + "/{id}"), id);
	}

	@Override
	protected String getBaseContext() {
		return "users";
	}
}