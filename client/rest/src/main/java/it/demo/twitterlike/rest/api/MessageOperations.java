package it.demo.twitterlike.rest.api;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

public interface MessageOperations {

	Resource<Message> getById(Long id);

	Resource<Message> getByLink(Link link);

	Resource<Message> create(String item);

	void delete(Long id);

	Resource<Message> update(Long id, String message);

	PagedResources<Resource<Message>> findDashboardMessages(Integer page,
			Integer size, Link link);


}
