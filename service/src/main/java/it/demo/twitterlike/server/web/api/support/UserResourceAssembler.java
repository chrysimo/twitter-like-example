package it.demo.twitterlike.server.web.api.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.web.api.UserRestController;
import it.demo.twitterlike.server.web.api.domain.UserResource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * This Resource Assembler Map a Person to PersonResource.
 * @author Christian Simonelli
 *
 */
@Component
public class UserResourceAssembler implements
		ResourceAssembler<User, UserResource> {

	@Override
	public UserResource toResource(User person) {
		UserResource resource = new UserResource(person);
		
		resource.add(linkTo(
				methodOn(UserRestController.class).getUserById(person.getLogin()))
				.withSelfRel());
		
		resource.add(linkTo(
				methodOn(UserRestController.class).getUserFollowers(person.getLogin(), null, null))
				.withRel(UserResource.LINK_NAME_FOLLOWERS));
		
		resource.add(linkTo(
				methodOn(UserRestController.class).getUserFollowing(person.getLogin(), null ,null))
				.withRel(UserResource.LINK_NAME_FOLLOWING));
		
		
		resource.add(linkTo(
				methodOn(UserRestController.class).getUserMessages(person.getLogin(), null ,null))
				.withRel(UserResource.LINK_NAME_MESSAGES));
		

		return resource;
	}

}
