package it.demo.twitterlike.server.web.rest.admin;

import it.demo.twitterlike.server.domain.Authority;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.AuthorityRepository;
import it.demo.twitterlike.server.repository.UserRepository;
import it.demo.twitterlike.server.repository.utils.RepositoryUtils;
import it.demo.twitterlike.server.web.rest.ApiEndpoints;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wordnik.swagger.annotations.Api;

@Controller
@RequestMapping(ApiEndpoints.API_ENDPOINT_ADMIN + "/users")
@Api(value = "User Admin Api")
@Transactional
public class UserAdminRestController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{login}/grantedAuthorities}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Collection<Authority> getUserRoles(
			@PathVariable("login") String login) {
		return getUser(login).getAuthorities();
	}

	/**
	 * <code>DELETE /api/admin/users/{id}/role/{rolenames}</code> - Deletes the
	 * entity backing the item resource.
	 * 
	 * @param resourceInformation
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws HttpRequestMethodNotSupportedException
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{login}/grantedAuthorities/{rolenames}", method = RequestMethod.DELETE)
	public void deleteUserRoles(@PathVariable("login") String login,
			@PathVariable("rolenames") String[] rolenames) {

		if (!ArrayUtils.isEmpty(rolenames)) {
			getUser(login).removeAuthorities(
					authorityRepository.findAll(Arrays.asList(rolenames)));
		}
	}

	/**
	 * <code>PUT /api/admin/users/role/{rolenames}</code> - Add the entity
	 * backing the item resource.
	 * 
	 * @param resourceInformation
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws HttpRequestMethodNotSupportedException
	 */
	@RequestMapping(value = "/{login}/grantedAuthorities/{rolenames}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void putUserRoles(@PathVariable("login") String login,
			@PathVariable("rolenames") String[] rolenames) {
		if (!ArrayUtils.isEmpty(rolenames)) {
			getUser(login).addAuthorities(
					authorityRepository.findAll(Arrays.asList(rolenames)));
		}

	}

	/**
	 * <code>PUT /api/admin/users/role/{rolenames}</code> - Add the entity
	 * backing the item resource.
	 * 
	 * @param resourceInformation
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws HttpRequestMethodNotSupportedException
	 */
	@RequestMapping(value = "/{login}/grantedAuthorities/{rolenames}", method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	public void setUserRoles(@PathVariable("login") String login,
			@PathVariable("rolenames") String[] rolenames) {
		if (!ArrayUtils.isEmpty(rolenames)) {
			getUser(login).clearAuthorities().addAuthorities(
					authorityRepository.findAll(Arrays.asList(rolenames)));
		}
	}

	protected User getUser(String login) throws ResourceNotFoundException {
		return RepositoryUtils.getEntity(userRepository, login);
	}

}
