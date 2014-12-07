package it.demo.twitterlike.android.domain;

import it.demo.twitterlike.rest.api.Message;

import java.io.Serializable;

public class InternalMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Message message;

	private final InternalUserProfile author;

	public InternalMessage(InternalUserProfile author, Message message) {
		super();
		this.author = author;
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public InternalUserProfile getAuthor() {
		return author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InternalMessage other = (InternalMessage) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

}
