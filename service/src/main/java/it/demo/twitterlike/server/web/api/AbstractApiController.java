package it.demo.twitterlike.server.web.api;

import it.demo.twitterlike.server.domain.Message;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.web.api.domain.MessageResource;
import it.demo.twitterlike.server.web.api.domain.UserResource;
import it.demo.twitterlike.server.web.api.support.MessageResourceAssembler;
import it.demo.twitterlike.server.web.api.support.UserResourceAssembler;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractApiController {

	public static final String ME_PREFIX = "/me";
	
	protected final MessageResourceAssembler messageResourceAssembler;
	
	protected final UserResourceAssembler userResourceAssembler;
	
	protected AbstractApiController(MessageResourceAssembler messageResourceAssembler, UserResourceAssembler userResourceAssembler) {
		this.messageResourceAssembler = messageResourceAssembler;
		this.userResourceAssembler = userResourceAssembler;
	}
	
	protected HttpEntity<PagedResources<MessageResource>> doReturnMessagePagedResult(
			PagedResourcesAssembler<Message> assembler, Page<Message> pages) {
		return new ResponseEntity<>(assembler.toResource(pages,
				messageResourceAssembler), HttpStatus.OK);
	}
	
	protected HttpEntity<PagedResources<UserResource>> doReturnUserPagedResult(
			PagedResourcesAssembler<User> assembler, Page<User> pages) {
		return new ResponseEntity<>(assembler.toResource(pages,
				userResourceAssembler), HttpStatus.OK);
	}
	
	protected <T extends Serializable> T existEntity(CrudRepository<?, T> repository, T id) throws ResourceNotFoundException {
		if (!repository.exists(id)) {
			throw new ResourceNotFoundException();
		}
		return id;

	}

	
}
