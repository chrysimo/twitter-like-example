package it.demo.twitterlike.server.web.rest;

import it.demo.twitterlike.server.domain.Message;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.MessageRepository;
import it.demo.twitterlike.server.repository.UserRepository;
import it.demo.twitterlike.server.repository.utils.RepositoryUtils;
import it.demo.twitterlike.server.service.UserService;
import it.demo.twitterlike.server.web.rest.domain.MessageResource;
import it.demo.twitterlike.server.web.rest.domain.UserResource;
import it.demo.twitterlike.server.web.rest.support.MessageResourceAssembler;
import it.demo.twitterlike.server.web.rest.support.UserResourceAssembler;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = ApiEndpoints.API_ENDPOINT_PEOPLE, produces = {
		"application/hal+json", "application/json" })
@Api(position = 1, value = "User Rest API", description = "Secured API for managing persons follow list")
public class UserRestController extends AbstractRestController {

	private static final String LOGIN_PREFIX = "/{login:.+}";

	private static final String TARGET_SUFFIX = "/{targets:.+}";

	public final static Log LOG = LogFactory.getLog(UserRestController.class);

	private final UserRepository userRepository;

	private final MessageRepository messageRepository;

	private final UserService userService;

	@Autowired
	public UserRestController(UserResourceAssembler userResourceAssembler,
			UserService userService, UserRepository userRepository,
			MessageRepository messageRepository,
			MessageResourceAssembler messageResourceAssembler) {
		super(messageResourceAssembler, userResourceAssembler);
		this.userRepository = userRepository;
		this.userService = userService;
		this.messageRepository = messageRepository;

	}

	@RequestMapping(value = ME_PREFIX, method = RequestMethod.GET)
	@ApiOperation(value = "me", notes = "Retrieves the authenticated user")
	@ApiResponses(value = { @ApiResponse(response = String.class, code = HttpServletResponse.SC_FORBIDDEN, message = "FORBIDDEN  the source user is not authenticated.") })
	public HttpEntity<UserResource> getMe(
			@AuthenticationPrincipal UserDetails userDetails)
			throws ResourceNotFoundException {
		return getUserById(userDetails.getUsername());
	}

	@RequestMapping(value = LOGIN_PREFIX, method = RequestMethod.GET)
	public HttpEntity<UserResource> getUserById(
			@PathVariable("login") String login)
			throws ResourceNotFoundException {
		return new ResponseEntity<>(
				userResourceAssembler.toResource(getUser(login)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "find-all", notes = "Retrieves all users")
	public HttpEntity<PagedResources<UserResource>> findAll(
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<User> assembler)
			throws ResourceNotFoundException {

		return doReturnUserPagedResult(assembler,
				userRepository.findAll(pageable));
	}
	
	

	@RequestMapping(value = LOGIN_PREFIX + "/followers", method = RequestMethod.GET)
	@ApiOperation(value = "get-followers", notes = "Retrieves the persons following the given person")
	public HttpEntity<PagedResources<UserResource>> getUserFollowers(
			@PathVariable String login,
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<User> assembler)
			throws ResourceNotFoundException {

		return doReturnUserPagedResult(assembler,
				userRepository.findFollowersByLogin(existUser(login), pageable));
	}

	@RequestMapping(value = ME_PREFIX + "/follows", method = RequestMethod.GET)
	@ApiOperation(value = "get-following", notes = "Retrieves the persons the authenticated user is following")
	public HttpEntity<PagedResources<UserResource>> getUserFollowing(
			@AuthenticationPrincipal UserDetails userDetails,
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<User> assembler)
			throws ResourceNotFoundException {

		return getUserFollowing(userDetails.getUsername(), pageable, assembler);

	}
	
	
	
	@RequestMapping(value = LOGIN_PREFIX + "/follows", method = RequestMethod.GET)
	@ApiOperation(value = "get-following", notes = "Retrieves the persons the given user is following")
	public HttpEntity<PagedResources<UserResource>> getUserFollowing(
			@PathVariable String login,
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<User> assembler)
			throws ResourceNotFoundException {

		return doReturnUserPagedResult(assembler,
				userRepository
						.findFollowingsByLogin(existUser(login), pageable));

	}

	@RequestMapping(value = LOGIN_PREFIX + "/messages", method = RequestMethod.GET)
	@ApiOperation(value = "get-messages", notes = "Retrieves the messages posted by given user")
	public HttpEntity<PagedResources<MessageResource>> getUserMessages(
			@PathVariable String login,
			@PageableDefault(size = 10, page = 0) Pageable pageable,
			PagedResourcesAssembler<Message> assembler)
			throws ResourceNotFoundException {

		return doReturnMessagePagedResult(assembler,
				messageRepository.findMessagesByAuthorLogin(existUser(login),
						pageable));
	}

	@RequestMapping(value = ME_PREFIX + "/follows/" + TARGET_SUFFIX, method = RequestMethod.POST)
	@ApiOperation(response = Void.class, value = "follow", notes = "Makes the source person follow the target person. Caller must be the source user for consistency.")
	@ApiResponses(value = {
			@ApiResponse(response = String.class, code = HttpServletResponse.SC_NOT_FOUND, message = "NOT FOUND  one or both the given users to link can't be found."),
			@ApiResponse(response = String.class, code = HttpServletResponse.SC_FORBIDDEN, message = "FORBIDDEN  the source user is not the one calling this API.") })
	@ResponseStatus(HttpStatus.CREATED)
	public void follow(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable String[] targets) {
		userService.follow(userDetails.getUsername(), targets);
	}

	@RequestMapping(value = ME_PREFIX + "/follows/" + TARGET_SUFFIX, method = RequestMethod.DELETE)
	@ApiOperation(response = Void.class, value = "unfollow", notes = "Makes the authenticated user unfollow the target user")
	@ApiResponses(value = {
			@ApiResponse(response = String.class, code = HttpServletResponse.SC_NOT_FOUND, message = "NOT FOUND  one or both the given users to link can't be found."),
			@ApiResponse(response = String.class, code = HttpServletResponse.SC_FORBIDDEN, message = "FORBIDDEN  the source user is not the one calling this API.") })
	public void unfollow(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable String[] targets) {
		userService.unfollow(userDetails.getUsername(), targets);
	}

	protected String existUser(String login) throws ResourceNotFoundException {
		return existEntity(userRepository, login);
	}

	protected User getUser(String login) throws ResourceNotFoundException {
		return RepositoryUtils.getEntity(userRepository, login);
	}
}
