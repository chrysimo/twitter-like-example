package it.demo.twitterlike.server.web.api;

import it.demo.twitterlike.server.domain.Authority;
import it.demo.twitterlike.server.domain.PersistentToken;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.PersistentTokenRepository;
import it.demo.twitterlike.server.repository.UserRepository;
import it.demo.twitterlike.server.security.SecurityUtils;
import it.demo.twitterlike.server.service.MailService;
import it.demo.twitterlike.server.service.UserService;
import it.demo.twitterlike.server.web.api.dto.UserDTO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping(ApiEndpoints.API_BASE_ENDPOINT)
public class AccountRestController {

	private final Logger log = LoggerFactory
			.getLogger(AccountRestController.class);

	@Inject
	private ServletContext servletContext;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private SpringTemplateEngine templateEngine;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	@Inject
	private PersistentTokenRepository persistentTokenRepository;

	@Inject
	private MailService mailService;

	/**
	 * POST /rest/register -> register the user.
	 */
	@RequestMapping(value = "/guest/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerAccount(@RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {
		return Optional
				.ofNullable(userRepository.findOne(userDTO.getLogin()))
				.map(user -> new ResponseEntity<String>("login already in use",
						HttpStatus.BAD_REQUEST))
				.orElseGet(
						() -> {
							if (userRepository.findOneByEmail(userDTO
									.getEmail()) != null) {
								return new ResponseEntity<String>(
										"e-mail address already in use",
										HttpStatus.BAD_REQUEST);
							}
							User user = userService.createUserInformation(
									userDTO.getLogin(), userDTO.getPassword(),
									userDTO.getFirstName(), userDTO
											.getLastName(), userDTO.getEmail()
											.toLowerCase(), userDTO
											.getLangKey());

							final Locale locale = Locale.forLanguageTag(user
									.getLangKey());

							activateAccount(user.getLogin());
							// String content =
							// createHtmlContentFromTemplate(user, locale,
							// request, response);
							// mailService.sendActivationEmail(user.getEmail(),
							// content, locale);
							return new ResponseEntity<>(HttpStatus.CREATED);
						});
	}

	/**
	 * GET /rest/activate -> activate the registered user.
	 */
	@RequestMapping(value = "/users/activate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> activateAccount(
			@RequestParam(value = "key") String key) {
		return Optional
				.ofNullable(userService.activateRegistration(key))
				.map(user -> new ResponseEntity<String>(user.getLogin(),
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * GET /rest/authenticate -> check if the user is authenticated, and return
	 * its login.
	 */
	@RequestMapping(value = "/guest/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * GET /rest/account -> get the current user.
	 */
	@RequestMapping(value = "/users/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> getAccount() {
		return Optional
				.ofNullable(userService.getUserWithAuthorities())
				.map(user -> new ResponseEntity<>(new UserDTO(user.getLogin(),
						null, user.getFirstName(), user.getLastName(), user
								.getEmail(), user.getLangKey(), user
								.getAuthorities().stream()
								.map(Authority::getName)
								.collect(Collectors.toList())), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * POST /rest/account -> update the current user information.
	 */
	@RequestMapping(value = "/users/account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAccount(@RequestBody UserDTO userDTO) {
		User userHavingThisEmail = userRepository.findOneByEmail(userDTO
				.getEmail());
		if (userHavingThisEmail != null
				&& !userHavingThisEmail.getLogin().equals(
						SecurityUtils.getCurrentLogin())) {
			return new ResponseEntity<String>("e-mail address already in use",
					HttpStatus.BAD_REQUEST);
		}
		userService.updateUserInformation(userDTO.getFirstName(),
				userDTO.getLastName(), userDTO.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * POST /rest/change_password -> changes the current user's password
	 */
	
	@RequestMapping(value = "/users/account/change_password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(@RequestBody String password) {
		if (StringUtils.isEmpty(password)) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		userService.changePassword(password);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * GET /rest/account/sessions -> get the current open sessions.
	 */
	@RequestMapping(value = "/users/account/sessions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
		return Optional
				.ofNullable(
						userRepository.findOne(SecurityUtils.getCurrentLogin()))
				.map(user -> new ResponseEntity<>(persistentTokenRepository
						.findByUser(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * DELETE /rest/account/sessions?series={series} -> invalidate an existing
	 * session.
	 *
	 * - You can only delete your own sessions, not any other user's session -
	 * If you delete one of your existing sessions, and that you are currently
	 * logged in on that session, you will still be able to use that session,
	 * until you quit your browser: it does not work in real time (there is no
	 * API for that), it only removes the "remember me" cookie - This is also
	 * true if you invalidate your current session: you will still be able to
	 * use it until you close your browser or that the session times out. But
	 * automatic login (the "remember me" cookie) will not work anymore. There
	 * is an API to invalidate the current session, but there is no API to check
	 * which session uses which cookie.
	 */
	@RequestMapping(value = "/users/account/sessions/{series}", method = RequestMethod.DELETE)
	public void invalidateSession(@PathVariable String series)
			throws UnsupportedEncodingException {
		String decodedSeries = URLDecoder.decode(series, "UTF-8");
		User user = userRepository.findOne(SecurityUtils.getCurrentLogin());
		if (persistentTokenRepository
				.findByUser(user)
				.stream()
				.filter(persistentToken -> StringUtils.equals(
						persistentToken.getSeries(), decodedSeries)).count() > 0) {

			persistentTokenRepository.delete(decodedSeries);
		}
	}

	private String createHtmlContentFromTemplate(final User user,
			final Locale locale, final HttpServletRequest request,
			final HttpServletResponse response) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("user", user);
		variables.put("baseUrl", request.getScheme() + "://" + // "http" + "://
				request.getServerName() + // "myhost"
				":" + request.getServerPort());
		IWebContext context = new SpringWebContext(request, response,
				servletContext, locale, variables, applicationContext);
		return templateEngine.process("activationEmail", context);
	}
}
