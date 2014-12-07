package it.demo.twitterlike.rest.api.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractTemplate {

	protected final RestTemplate restTemplate;
	private final String baseUrl;

	public AbstractTemplate(String baseUrl, RestTemplate restTemplate) {
		this.baseUrl = baseUrl;
		this.restTemplate = restTemplate;

	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String buildUrl(String path) {
		if (path.startsWith("http")) {
			return path;
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return getBaseUrl() + path;
	}

	protected <T> T getByLink(ParameterizedTypeReference<T> type, Link link) {
		T item = restTemplate.exchange(link.getHref(), HttpMethod.GET, null,
				type).getBody();
		return item;
	}

	protected <T> T getById(ParameterizedTypeReference<T> type, Serializable id) {

		return getSingleResult(buildUrl(getBaseContext() + "/{id}"), type, id);
	}

	protected <T> T getSingleResult(String url,
			ParameterizedTypeReference<T> type, Object... parameters) {
		T item = restTemplate.exchange(url, HttpMethod.GET, null, type,
				parameters).getBody();
		return item;
	}

	protected <T> T doFind(ParameterizedTypeReference<T> type, Link url) {
		return doFind(type, null, null, null, url.getHref());
	}

	protected <T> T doFind(ParameterizedTypeReference<T> type, Integer page,
			Integer size, String sort, String url) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		if (page != null) {
			parameters.put("page", page);
		}

		if (size != null) {
			parameters.put("size", size);
		}

		if (sort != null) {
			parameters.put("sort", sort);
		}

		T result = restTemplate.exchange(url, HttpMethod.GET, null, type,
				parameters).getBody();

		return result;
	}

	protected abstract String getBaseContext();

}
