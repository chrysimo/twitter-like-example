package it.demo.twitterlike.server.web.api.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import it.demo.twitterlike.server.domain.Message;
import it.demo.twitterlike.server.web.api.MessageRestController;
import it.demo.twitterlike.server.web.api.UserRestController;
import it.demo.twitterlike.server.web.api.domain.MessageResource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class MessageResourceAssembler implements
		ResourceAssembler<Message, MessageResource> {

	@Override
	public MessageResource toResource(Message entity) {
		MessageResource resource = new MessageResource(entity);
		
		resource.add(linkTo(
				methodOn(MessageRestController.class).getMessageById(
						entity.getId())).withSelfRel());
		
		resource.add(linkTo(
				methodOn(UserRestController.class).getUserById(
						entity.getAuthor().getLogin())).withRel(
								MessageResource.LINK_NAME_AUTHOR));

		
		
		return resource;
	}

}
