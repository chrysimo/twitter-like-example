package it.demo.twitterlike.server.web.api;

import it.demo.twitterlike.server.domain.Message;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.MessageRepository;
import it.demo.twitterlike.server.security.InternalUserDetails;
import it.demo.twitterlike.server.service.MessageService;
import it.demo.twitterlike.server.web.api.domain.MessageResource;
import it.demo.twitterlike.server.web.api.support.MessageResourceAssembler;
import it.demo.twitterlike.server.web.api.support.UserResourceAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;

@RestController
@RequestMapping(value = ApiEndpoints.API_ENDPOINT_MESSAGES, produces = {
		"application/hal+json", "application/json", "application/xml" })
@Api(position = 2, value = "Messages Rest API", description = "Secured API for managing messages follow list")
public class MessageRestController extends AbstractApiController {

	private final MessageRepository messageRepository;

	private final MessageService messageService;

	@Autowired
	public MessageRestController(MessageService messageService,
			MessageResourceAssembler messageResourceAssembler,
			UserResourceAssembler resourceAssembler,
			MessageRepository messageRepository) {
		super(messageResourceAssembler, resourceAssembler);
		this.messageService = messageService;
		this.messageRepository = messageRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public HttpEntity<PagedResources<MessageResource>> findFollowedMessages(
			@AuthenticationPrincipal UserDetails userDetails,
			@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
			PagedResourcesAssembler<Message> assembler)
			throws ResourceNotFoundException {
		return doReturnMessagePagedResult(
				assembler,
				messageRepository.findFollowedMessages(
						userDetails.getUsername(), pageable));
	}

	/*@RequestMapping(method = RequestMethod.GET)
	public HttpEntity<PagedResources<MessageResource>> getMessages(
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<Message> assembler)
			throws ResourceNotFoundException {

		return doReturnMessagePagedResult(assembler,
				messageRepository.findAll(pageable));
	}*/

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<MessageResource> getMessageById(@PathVariable Long id)
			throws ResourceNotFoundException {
		return doReturnMessageResult(getMessage(id), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<MessageResource> postMessage(
			@AuthenticationPrincipal InternalUserDetails userDetails,
			@RequestBody String text) throws ResourceNotFoundException {
		User author = userDetails.getUser();
		return doReturnMessageResult(messageService.create(author, text),
				HttpStatus.CREATED);

	}

	/**
	 * Updates message content by current user.
	 * 
	 * @param commentId
	 * @param updateMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	public HttpEntity<MessageResource> updateMessage(@AuthenticationPrincipal InternalUserDetails userDetails,
			@PathVariable("id") Long id, String text) {
		User author = userDetails.getUser();
		return doReturnMessageResult(messageService.update(id, author, text),
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteMessage(@PathVariable("id") Long id) {
		messageService.delete(getMessage(id));
	}

	protected HttpEntity<MessageResource> doReturnMessageResult(
			Message message, HttpStatus status) {
		return new ResponseEntity<>(
				messageResourceAssembler.toResource(message), status);
	}

	protected Message getMessage(Long id) throws ResourceNotFoundException {

		Message result = messageRepository.findOne(id);
		if (result == null) {
			throw new ResourceNotFoundException();
		}
		return result;
	}
}
