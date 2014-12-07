package it.demo.twitterlike.rest.api.impl;

import it.demo.twitterlike.rest.api.MessageOperations;
import it.demo.twitterlike.rest.api.TwitterLike;
import it.demo.twitterlike.rest.api.UserOperations;

import java.util.Arrays;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TwitterLikeTemplate extends AbstractOAuth2ApiBinding implements
		TwitterLike {

	private UserOperations userOpertations;

	private MessageOperations messageOperations;

	private final String baseUrl;

	public TwitterLikeTemplate(String baseUrl, String accessToken) {
		super(accessToken);
		this.baseUrl = baseUrl;
		initialize();
	}

	// private helpers
	private void initialize() {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so
		// that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector
				.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis();
	}

	@Override
	protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
		return getHalConverter();
	}

	private static final MappingJackson2HttpMessageConverter getHalConverter() {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jackson2HalModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

		converter.setObjectMapper(mapper);
		converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));

		return converter;
	}

	private void initSubApis() {
		userOpertations = new UserTemplate(baseUrl, getRestTemplate());
		messageOperations = new MessageTemplate(baseUrl, getRestTemplate());
	}

	@Override
	public UserOperations userOperations() {
		return userOpertations;
	}

	@Override
	public MessageOperations messageOperations() {
		return messageOperations;
	}
}
