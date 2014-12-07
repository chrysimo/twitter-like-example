package it.demo.twitterlike.rest.api;

public interface TwitterLike {

	/**
	 * API for performing operations on example user profiles.
	 */
	UserOperations userOperations();

	/**
	 * API for performing operations on example messages.
	 */
	MessageOperations messageOperations();
}
