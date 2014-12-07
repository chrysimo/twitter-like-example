package it.demo.twitterlike.android.tasks;

import it.demo.twitterlike.android.domain.InternalUserProfile;
import it.demo.twitterlike.rest.api.UserProfile;

import java.util.TreeSet;

import org.springframework.hateoas.Resource;

public abstract class AbstractUserBasedRemoteInvocationTask extends
		AbstractRemoteInvocationTask {

	public AbstractUserBasedRemoteInvocationTask() {

	}

	public AbstractUserBasedRemoteInvocationTask(boolean checkOnline) {
		super(checkOnline);
	}

	protected TreeSet<InternalUserProfile> buildInternalUserProfile(
			Iterable<? extends Resource<UserProfile>> users, boolean follow) {
		TreeSet<InternalUserProfile> result = new TreeSet<InternalUserProfile>();
		for (Resource<UserProfile> current : users) {
			result.add(buildInternalUserProfile(current, follow));
		}
		return result;
	}

	protected InternalUserProfile buildInternalUserProfile(
			Resource<UserProfile> user) {
		return buildInternalUserProfile(user, false);
	}

	protected InternalUserProfile buildInternalUserProfile(
			Resource<UserProfile> user, boolean follow) {
		return user == null ? null : new InternalUserProfile(user.getContent(),
				user.getId(), user.getLink("messages"), follow);
	}
}
