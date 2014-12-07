package it.demo.twitterlike.android.tasks;

import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.rest.api.Message;
import it.demo.twitterlike.rest.api.UserProfile;
import it.demo.twitterlike.rest.api.util.LinkUtils;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import android.util.Log;

public abstract class AbstractMessageBasedRemoteInvocationTask extends
		AbstractUserBasedRemoteInvocationTask {

	public AbstractMessageBasedRemoteInvocationTask() {

	}

	public AbstractMessageBasedRemoteInvocationTask(boolean checkOnline) {
		super(checkOnline);
	}

	protected InternalMessage buildInternalMessage(Resource<Message> message) {
		Link authorLink = LinkUtils.getMessageAuthorLink(message);
		Resource<UserProfile> author = null;
		try {
			author = getUserOperations().getByLink(authorLink);

		} catch (Exception e) {
			Log.e(TAG, "Error getting Author of Message " + authorLink, e);
		}
		return new InternalMessage(buildInternalUserProfile(author),
				message.getContent());
	}

}
