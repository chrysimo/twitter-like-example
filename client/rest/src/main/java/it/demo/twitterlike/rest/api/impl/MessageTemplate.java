package it.demo.twitterlike.rest.api.impl;

import it.demo.twitterlike.rest.api.Message;
import it.demo.twitterlike.rest.api.MessageOperations;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class MessageTemplate extends AbstractTemplate implements
		MessageOperations {

	private final ParameterizedTypeReference<Resource<Message>> singleTypeParameter = new ParameterizedTypeReference<Resource<Message>>() {
	};

	private final ParameterizedTypeReference<PagedResources<Resource<Message>>> pagedTypeParameter = new ParameterizedTypeReference<PagedResources<Resource<Message>>>() {
	};

	public MessageTemplate(String baseUrl, RestTemplate restTemplate) {
		super(baseUrl, restTemplate);
	}

	@Override
	protected String getBaseContext() {
		return "messages";
	}

	public PagedResources<Resource<Message>> findDashboardMessages(
			Integer page, Integer size, Link link) {
		return doFind(pagedTypeParameter, page, size, null,
				link == null ? buildUrl(getBaseContext())
						: link.getHref());
	}

	/*public PagedResources<Resource<Message>> findAll(Integer page,
			Integer size, String sort, Link link) {
		return doFind(pagedTypeParameter, page, size, sort,
				link == null ? buildUrl(getBaseContext()) : link.getHref());
	}*/

	public Resource<Message> getByLink(Link link) {
		return getByLink(singleTypeParameter, link);
	}

	public Resource<Message> getById(Long id) {
		return getById(singleTypeParameter, id);
	}

	public Resource<Message> create(String message) {

		Resource<Message> savedItem = restTemplate.exchange(
				buildUrl(getBaseContext()), HttpMethod.POST,
				new HttpEntity<String>(message), singleTypeParameter).getBody();

		return savedItem;
	}

	public Resource<Message> update(Long id, String message) {

		Resource<Message> result = restTemplate.exchange(
				buildUrl(getBaseContext() + "/{id}"), HttpMethod.PUT,
				new HttpEntity<String>(message), singleTypeParameter, id)
				.getBody();

		return result;
	}

	public void delete(Long id) {
		restTemplate.delete(buildUrl(getBaseContext() + "/{id}"), id);
	}
}