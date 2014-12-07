package it.demo.twitterlike.android.tasks;

import it.demo.twitterlike.android.domain.InternalUserProfile;

import java.util.TreeSet;

import com.telly.groundy.TaskResult;

public class FollowedListTask extends AbstractUserBasedRemoteInvocationTask {

	public static final String ALLUSERS_RESULT = "allUsers";

	public static final String MY_FOLLOW_USERS_RESULT = "myFollowUser";

	public FollowedListTask() {
		super(true);
	}

	protected TaskResult doInRemote() {

		TreeSet<InternalUserProfile> followUsers = buildInternalUserProfile(
				getUserOperations().findMyFollow(null, Integer.MAX_VALUE, null)
						.getContent(), true);

		TreeSet<InternalUserProfile> allUsers = buildInternalUserProfile(
				getUserOperations().findAll(null, Integer.MAX_VALUE, null)
						.getContent(), false);
		followUsers.addAll(allUsers);

		// Remove me
		followUsers.remove(buildInternalUserProfile(getUserOperations()
				.getUserProfile()));

		return succeeded().add(ALLUSERS_RESULT, followUsers).add(
				MY_FOLLOW_USERS_RESULT, followUsers);
	}
}
