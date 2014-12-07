package it.demo.twitterlike.server.service;

import it.demo.twitterlike.server.domain.Message;
import it.demo.twitterlike.server.domain.User;
import it.demo.twitterlike.server.repository.MessageRepository;
import it.demo.twitterlike.server.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {

	private final Logger log = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	public Message create(User author, String text) {
		return messageRepository.save(new Message(author, text));
		
	}
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')or #message.author.login == authentication.name")
	public Message update(Long id, User author, String text) {
		Message message = messageRepository.findOne(id);
		if (message == null) {
			throw new ResourceNotFoundException();
		}
		message.setAuthor(author);
		message.setText(text);
		return message;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or #message.author.login == authentication.name")
	public void delete(@P("message") Message message) {
		messageRepository.delete(message);
	}
	
}
