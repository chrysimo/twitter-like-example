package it.demo.twitterlike.server.service;

import it.demo.twitterlike.server.domain.Authority;
import it.demo.twitterlike.server.domain.PersistentToken;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.AuthorityRepository;
import it.demo.twitterlike.server.repository.PersistentTokenRepository;
import it.demo.twitterlike.server.repository.UserRepository;
import it.demo.twitterlike.server.security.SecurityUtils;
import it.demo.twitterlike.server.service.utils.RandomUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PersistentTokenRepository persistentTokenRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	public User activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return Optional.ofNullable(userRepository.getUserByActivationKey(key))
				.map(user -> {
					// activate given user for the registration key.
					user.setActivated(true);
					user.setActivationKey(null);
					userRepository.save(user);
					log.debug("Activated user: {}", user);
					return user;
				}).orElse(null);
	}

	public User createUserInformation(String login, String password,
			String firstName, String lastName, String email, String langKey) {
		User newUser = new User();
		Authority authority = authorityRepository.findOne("ROLE_USER");
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(login);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setLangKey(langKey);
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public void updateUserInformation(String firstName, String lastName,
			String email) {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		currentUser.setFirstName(firstName);
		currentUser.setLastName(lastName);
		currentUser.setEmail(email);
		userRepository.save(currentUser);
		log.debug("Changed Information for User: {}", currentUser);
	}

	public void changePassword(String password) {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		String encryptedPassword = passwordEncoder.encode(password);
		currentUser.setPassword(encryptedPassword);
		userRepository.save(currentUser);
		log.debug("Changed password for User: {}", currentUser);
	}

	/**
	 * Makes the source user follow the target user
	 *
	 * @param source
	 *            not null
	 * @param target
	 *            not null
	 */
	public void follow(String source, String... targetIds) {

		if (source != null && targetIds != null && targetIds.length > 0) {

			User sourcePerson = userRepository.findOne(source);
			Iterable<User> targets = userRepository.findAll(Arrays
					.asList(targetIds));
			sourcePerson.addFollowers(targets);
		}

	}

	/**
	 * Makes the source user unfollow the target user, if it was following it.
	 *
	 * @param source
	 *            not null
	 * @param target
	 *            not null
	 */
	public void unfollow(String source, String... targetIds) {

		if (source != null && targetIds != null && targetIds.length > 0) {

			User sourcePerson = userRepository.findOne(source);
			Iterable<User> targets = userRepository.findAll(Arrays
					.asList(targetIds));
			sourcePerson.removeFollowers(targets);
		}
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		User currentUser = userRepository.findOne(SecurityUtils
				.getCurrentLogin());
		currentUser.getAuthorities().size(); // eagerly load the association
		return currentUser;
	}

	/**
	 * Persistent Token are used for providing automatic authentication, they
	 * should be automatically deleted after 30 days.
	 * <p/>
	 * <p>
	 * This is scheduled to get fired everyday, at midnight.
	 * </p>
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void removeOldPersistentTokens() {
		LocalDate now = new LocalDate();
		List<PersistentToken> tokens = persistentTokenRepository
				.findByTokenDateBefore(now.minusMonths(1));
		for (PersistentToken token : tokens) {
			log.debug("Deleting token {}", token.getSeries());
			User user = token.getUser();
			user.getPersistentTokens().remove(token);
			persistentTokenRepository.delete(token);
		}
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p/>
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		DateTime now = new DateTime();
		List<User> users = userRepository
				.findNotActivatedUsersByCreationDateBefore(now.minusDays(3));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}
}
