package it.demo.twitterlike.server.web.api.domain;

import it.demo.twitterlike.server.domain.Message;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class MessageResource extends Resource<Message> {
	public static final String LINK_NAME_AUTHOR = "author";
	
	private final String author;

	public MessageResource(Message content, Link... links) {
		super(content, links);
		this.author = content.getAuthor().getLogin();
		
	}
	
	public String getAuthor() {
		return author;
	}


}