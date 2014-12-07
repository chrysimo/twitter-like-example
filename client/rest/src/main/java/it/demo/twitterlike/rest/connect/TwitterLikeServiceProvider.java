package it.demo.twitterlike.rest.connect;

import static it.demo.twitterlike.rest.api.util.UrlUtils.appendUrls;
import it.demo.twitterlike.rest.api.TwitterLike;
import it.demo.twitterlike.rest.api.impl.TwitterLikeTemplate;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class TwitterLikeServiceProvider extends
		AbstractOAuth2ServiceProvider<TwitterLike> {

	private final String baseUrl;

	/**
	 * Creates a FacebookServiceProvider for the given application ID, secret,
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
	public TwitterLikeServiceProvider(String baseUrl, String authServerBaseUrl,
			String appId, String appSecret) {
		super(getOAuth2Template(appId, appSecret, appendUrls(authServerBaseUrl
				+ "/oauth/authorize"), null, appendUrls(authServerBaseUrl + "/oauth/token"),
				false));
		this.baseUrl = appendUrls(baseUrl);
	}

	
	
	private static OAuth2Template getOAuth2Template(String appId,
			String appSecret, String authorizeUrl, String authenticateUrl,
			String accessTokenUrl, boolean useParameters) {
		OAuth2Template oAuth2Template = new OAuth2Template(appId, appSecret,
				authorizeUrl, authenticateUrl != null ? authenticateUrl
						: authorizeUrl, accessTokenUrl);
		oAuth2Template.setUseParametersForClientAuthentication(useParameters);
		return oAuth2Template;
	}

	@Override
	public TwitterLike getApi(String accessToken) {
		return new TwitterLikeTemplate(baseUrl, accessToken);
	}

}
