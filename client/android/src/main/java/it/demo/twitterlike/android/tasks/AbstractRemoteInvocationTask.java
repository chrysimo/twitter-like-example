package it.demo.twitterlike.android.tasks;

import it.demo.twitterlike.android.application.MainApplication;
import it.demo.twitterlike.android.service.TwitterLikeService;
import it.demo.twitterlike.android.ui.utils.DeviceUtils;
import it.demo.twitterlike.rest.api.MessageOperations;
import it.demo.twitterlike.rest.api.UserOperations;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import android.util.Log;

import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

public abstract class AbstractRemoteInvocationTask extends GroundyTask {

	public static final String AUTENTICATION_ERROR = "authenticationError";

	public static final String CONNECTION_ERROR = "connectionError";

	public static final String ORDER_PARAMETER = "order";

	protected final String TAG = getClass().getSimpleName();

	private final boolean checkOnline;

	public AbstractRemoteInvocationTask() {
		this(true);
	}

	public AbstractRemoteInvocationTask(boolean checkOnline) {
		this.checkOnline = checkOnline;
	}

	@Override
	protected final TaskResult doInBackground() {
		if (!checkOnline || DeviceUtils.isOnline(getContext())) {
			try {
				return doInRemote();
			} catch (HttpClientErrorException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				if (HttpStatus.FORBIDDEN == e.getStatusCode()
						|| HttpStatus.UNAUTHORIZED == e.getStatusCode()) {
					return authenticationError();
				} else {
					return networkError();
				}

			} catch (RestClientException httph) {
				Log.e(TAG, httph != null ? httph.getLocalizedMessage()
						: "RestClient Exception", httph);
				return networkError();
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				return failed();
			}
		} else {
			return networkError();
		}
	}

	protected TaskResult doInRemote() {
		doRemoteExecution();
		return succeeded();
	}

	protected void doRemoteExecution() {

	}

	protected TaskResult authenticationError() {
		return failed().add(AUTENTICATION_ERROR, true);
	}

	protected TaskResult networkError() {
		return failed().add(CONNECTION_ERROR, true);
	}

	protected MessageOperations getMessageOperations() {
		return getApplication().getMessageOperations();
	}

	protected UserOperations getUserOperations() {
		return getApplication().getUserOperations();
	}

	protected TwitterLikeService getTwitterLikeService() {
		return getApplication().getTwitterLikeService();
	}

	protected MainApplication getApplication() {
		return (MainApplication) getContext().getApplicationContext();
	}
}
