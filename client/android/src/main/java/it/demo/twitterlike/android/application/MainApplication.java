package it.demo.twitterlike.android.application;

import it.demo.twitterlike.android.service.TwitterLikeService;
import it.demo.twitterlike.android.service.inmemory.InMemoryTwitterLikeService;
import it.demo.twitterlike.android.ui.R;
import it.demo.twitterlike.android.ui.fragments.LoginSettingFragment;
import it.demo.twitterlike.android.ui.utils.Constants;
import it.demo.twitterlike.rest.api.MessageOperations;
import it.demo.twitterlike.rest.api.TwitterLike;
import it.demo.twitterlike.rest.api.UserOperations;
import it.demo.twitterlike.rest.api.UserProfile;
import it.demo.twitterlike.rest.api.util.UrlUtils;
import it.demo.twitterlike.rest.connect.TwitterLikeConnectionFactory;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

//@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=7af2cb25", formKey="")
public class MainApplication extends Application implements
		OnSharedPreferenceChangeListener {

	private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

	private ConnectionFactoryRegistry connectionFactoryRegistry;
	private SQLiteOpenHelper repositoryHelper;
	private ConnectionRepository connectionRepository;

	private TwitterLike remoteService;

	private TwitterLikeService twitterLikeService;

	private SharedPreferences preferences;

	public static final String TAG = MainApplication.class.getSimpleName();

	// ***************************************
	// Application Methods
	// ***************************************
	@Override
	public void onCreate() {
		// ACRA.init(this);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);

		initConnectionConfiguration();

	}

	protected void clearConnectioConfiguration() {
		this.connectionFactoryRegistry = null;
		this.connectionRepository = null;
		this.remoteService = null;
		this.twitterLikeService = null;
	}

	protected Proxy findProxy(String url) {
		Proxy result = null;
		try {
			ProxySelector defaultProxySelector = ProxySelector.getDefault();
			List<Proxy> proxyList = defaultProxySelector.select(new URI(url));
			if (!CollectionUtils.isEmpty(proxyList)) {
				result = proxyList.iterator().next();
			}
		} catch (Exception e) {
			Log.e(Constants.LOGTAG_USER_LOGIN,
					"Error getting proxy list for url " + url, e);
		}
		return result;
	}

	protected void initConnectionConfiguration() {

		this.connectionFactoryRegistry = new ConnectionFactoryRegistry();
		this.connectionFactoryRegistry
				.addConnectionFactory(new TwitterLikeConnectionFactory(UrlUtils
						.appendUrls(getAppUrl() + "rest/api/"), getAppUrl(),
						getAppId(), getAppSecret()));

		// set up the database and encryption
		this.repositoryHelper = new SQLiteConnectionRepositoryHelper(this);
		this.connectionRepository = new SQLiteConnectionRepository(
				this.repositoryHelper, this.connectionFactoryRegistry,
				AndroidEncryptors.text("password", "5c0744940b5c369b"));

	}

	// ***************************************
	// Private methods
	// ***************************************
	private String getAppId() {
		return getString(R.string.ordermanagement_app_id);
	}

	private String getAppSecret() {
		return getString(R.string.ordermanagement_app_secret);
	}

	private int getConnectionTimeout() {
		String result = getString(R.string.network_connection_timeout);
		try {
			if (result != null) {
				return Integer.parseInt(result);
			}
		} catch (Exception e) {
		}

		return DEFAULT_CONNECTION_TIMEOUT;

	}

	private String getAppUrl() {
		String result = LoginSettingFragment.getConnectionAddress(this,
				preferences);
		return result;
	}

	// ***************************************
	// Public methods
	// ***************************************
	private ConnectionRepository getConnectionRepository() {
		if (connectionRepository == null) {
			initConnectionConfiguration();
		}
		return this.connectionRepository;
	}

	private ConnectionFactoryRegistry getConnectionFactoryRegistry() {
		if (connectionFactoryRegistry == null) {
			initConnectionConfiguration();
		}
		return this.connectionFactoryRegistry;
	}

	public void disconnect() {
		getConnectionRepository().removeConnections(
				getConnectionFactory().getProviderId());
		this.remoteService = null;
		this.twitterLikeService = null;

	}

	public UserProfile getAuthenticatedUser() {
		UserProfile result = null;
		Connection<?> connection = getConnectionRepository()
				.findPrimaryConnection(TwitterLike.class);
		if (connection != null && !connection.hasExpired()) {
			result = getTwitterLikeService().getAuthenticatedUser();
		}
		return result;
	}

	public boolean isAuthenticated() {
		return getAuthenticatedUser() != null;
	}

	public void authenticate(String username, String password) {
		ConnectionRepository connectionRepository = getConnectionRepository();
		TwitterLikeConnectionFactory connectionFactory = getConnectionFactory();

		String prettyPrintUser = "[" + username + "] ";
		Log.v(Constants.LOGTAG_USER_LOGIN, prettyPrintUser
				+ "Try Authenticating User with password " + password);

		disconnect();

		MultiValueMap<String, String> formData2 = new LinkedMultiValueMap<String, String>();
		formData2.add("scope", getString(R.string.ordermanagement_scope));

		MultiValueMap<String, String> additionalParameters = new LinkedMultiValueMap<String, String>();

		AccessGrant grant = connectionFactory.getOAuthOperations()
				.exchangeCredentialsForAccess(username, password,
						additionalParameters);

		if (grant != null) {
			Connection<TwitterLike> connection = connectionFactory
					.createConnection(grant);
			try {
				// persist connection to the repository
				connectionRepository.addConnection(connection);

				initializeServices();
			} catch (DuplicateConnectionException e) {
				// connection already exists in repository!
				Log.d(TAG, e.getLocalizedMessage(), e);
			}
		}
	}

	public TwitterLikeService getTwitterLikeService() {
		if (twitterLikeService == null) {
			TwitterLike twitterLike = getConnectionRepository()
					.findPrimaryConnection(TwitterLike.class).getApi();
			this.twitterLikeService = InMemoryTwitterLikeService.getInstance(
					this, twitterLike);
		}
		return twitterLikeService;
	}

	public MessageOperations getMessageOperations() {
		if (remoteService == null) {
			initializeServices();
		}

		return this.remoteService == null ? null : this.remoteService
				.messageOperations();
	}

	public UserOperations getUserOperations() {

		if (remoteService == null) {
			initializeServices();
		}
		return this.remoteService == null ? null : this.remoteService
				.userOperations();
	}

	protected TwitterLike initializeServices() {
		this.remoteService = getConnectionRepository().findPrimaryConnection(
				TwitterLike.class).getApi();
		return remoteService;
	}

	public TwitterLikeConnectionFactory getConnectionFactory() {
		return (TwitterLikeConnectionFactory) getConnectionFactoryRegistry()
				.getConnectionFactory(TwitterLike.class);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (LoginSettingFragment.supports(key)) {
			clearConnectioConfiguration();
		}
	}

}
