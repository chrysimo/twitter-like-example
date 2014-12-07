package it.demo.twitterlike.rest.api;

import it.demo.twitterlike.rest.connect.TwitterLikeConnectionFactory;

import org.junit.After;
import org.junit.Before;
import org.springframework.social.connect.Connection;

public abstract class AbstractEndpointTest {

	protected final static String CLIENTID = "acme";
	protected final static String CLIENTSECRET = "acmesecret";
	protected final static String PARAM_USERNAME = "chrysimo";
	protected final static String PARAM_PASSWORD = "password";
	protected final static String BASE_URL = "http://localhost:8081";
	// private final static String PROXY_ADDRESS = "172.16.32.1";
	// private final static int PROXY_PORT = 3128;
//	@Rule
//	public ServerRunning serverRunning = ServerRunning.isRunning();
	protected TwitterLike service;

	protected Connection<TwitterLike> connection;

	@Before
	public void setup() {

		TwitterLikeConnectionFactory connectionFactory = new TwitterLikeConnectionFactory(
				BASE_URL + "/rest/api/", BASE_URL, CLIENTID, CLIENTSECRET);

		connection = connectionFactory.createConnection(connectionFactory
				.getOAuthOperations().exchangeCredentialsForAccess(
						PARAM_USERNAME, PARAM_PASSWORD, null));
		service = connection.getApi();
		
		doSetup();
	}

	@After
	public void teardown() {
		service = null;
		doTearDown();
	}

	protected void doSetup() {
	}

	protected void doTearDown() {
	}
}
