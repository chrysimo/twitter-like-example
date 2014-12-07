package it.demo.twitterlike.android.tasks;

public class LoginTask extends AbstractRemoteInvocationTask {

	public static final String USERNAME_PARAMETER = "userName";

	public static final String PASSWORD_PARAMETER = "password";

	public static final int PROGRESS_UPDATE_SUCCESS_AUTHENTICATION = 50;

	protected void doRemoteExecution() {

		String username = getStringArg(USERNAME_PARAMETER);
		String password = getStringArg(PASSWORD_PARAMETER);

		getApplication().authenticate(username, password);
		updateProgress(PROGRESS_UPDATE_SUCCESS_AUTHENTICATION);
		getTwitterLikeService().syncronize();
		super.doRemoteExecution();
	}

}
