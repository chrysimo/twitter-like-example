package it.demo.twitterlike.android.tasks;

public class AddFollowTask extends AbstractRemoteInvocationTask {

	public static final String USER_PARAMETER = "user";

	protected void doRemoteExecution() {
		String username = getStringArg(USER_PARAMETER);
		getUserOperations().follow(username);
	}
}
