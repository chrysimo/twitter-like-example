package it.demo.twitterlike.rest.connect;

import it.demo.twitterlike.rest.api.TwitterLike;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class TwitterLikeConnectionFactory extends
		OAuth2ConnectionFactory<TwitterLike> {

	/**
	 * Creates a FacebookConnectionFactory for the given application ID and
	 * secret. Using this constructor, no application namespace is set (and
	 * therefore Facebook's Open Graph operations cannot be used).
	 * 
	 * @param appId
	 *            The application's App ID as assigned by Facebook
	 * @param appSecret
	 *            The application's App Secret as assigned by Facebook
	 */
	public TwitterLikeConnectionFactory(String baseUrl,
			String authServerBaseUrl, String appId, String appSecret) {
		this(baseUrl, authServerBaseUrl, appId, appSecret, null);
	}

	/**
	 * Creates a FacebookConnectionFactory for the given application ID, secret,
	 * and namespace.
	 * 
	 * @param appId
	 *            The application's App ID as assigned by Facebook
	 * @param appSecret
	 *            The application's App Secret as assigned by Facebook
	 * @param appNamespace
	 *            The application's App Namespace as configured with Facebook.
	 *            Enables use of Open Graph operations.
	 */
	public TwitterLikeConnectionFactory(String baseUrl,
			String authServerBaseUrl, String appId, String appSecret,
			String appNamespace) {
		super("twitterlike", new TwitterLikeServiceProvider(baseUrl,
				authServerBaseUrl, appId, appSecret), new TwitterLikeAdapter());
	}
}
