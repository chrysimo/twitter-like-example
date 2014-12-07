package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.application.MainApplication;
import it.demo.twitterlike.android.service.TwitterLikeService;
import it.demo.twitterlike.android.tasks.AbstractRemoteInvocationTask;
import it.demo.twitterlike.android.tasks.DelayedTask;
import it.demo.twitterlike.android.ui.fragments.CallbackDialogFragment.YesNoListener;
import it.demo.twitterlike.rest.api.MessageOperations;
import it.demo.twitterlike.rest.api.UserOperations;
import it.demo.twitterlike.rest.api.UserProfile;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;
import com.telly.groundy.GroundyManager;
import com.telly.groundy.annotations.OnFailure;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

public abstract class AbstractAsyncActivity extends Activity implements
		YesNoListener {

	private static final int GROUP_ID = 146;

	public static final int RESULT_SYNCRONIZE = 78;

	private static final String PROGRESSBAR_MESSAGE = "progressBarMessage";

	protected static final String TAG = AbstractAsyncActivity.class
			.getSimpleName();

	private Bundle savedInstanceState;

	private ProgressDialog progressDialog;

	protected CallbacksManager callbacksManager;

	private boolean destroyed = false;

	private CharSequence progressBarMessage = null;

	public CallbacksManager getCallbacksManager() {
		return callbacksManager;
	}

	protected Bundle getSavedInstanceState() {
		return savedInstanceState;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		callbacksManager = CallbacksManager.init(savedInstanceState, this);

		showProgressDialog(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		callbacksManager.onSaveInstanceState(savedInstanceState);
		if (progressBarMessage != null) {
			savedInstanceState.putCharSequence(PROGRESSBAR_MESSAGE,
					progressBarMessage);
		}
	}

	protected UserOperations getUserOperations() {
		return getApplicationContext().getUserOperations();
	}

	protected MessageOperations getMessageOperations() {
		return getApplicationContext().getMessageOperations();
	}

	protected void disconnect() {
		getApplicationContext().disconnect();
	}

	protected boolean isConnected() {
		return getApplicationContext().isAuthenticated();
	}

	protected TwitterLikeService getTwitterLikeService() {
		return getApplicationContext().getTwitterLikeService();
	}

	protected UserProfile getAuthenticatedUser() {
		return getApplicationContext().getAuthenticatedUser();
	}

	@OnFailure(AbstractRemoteInvocationTask.class)
	public void onError(
			@Param(AbstractRemoteInvocationTask.CONNECTION_ERROR) Boolean connectionError,
			@Param(AbstractRemoteInvocationTask.AUTENTICATION_ERROR) Boolean authenticationError) {
		dismissProgressDialog();

		if (!doOnError(connectionError, authenticationError)) {
			if (authenticationError != null && authenticationError) {
				makeToastText(R.string.login_message_authentication_failed_wrongusername);
				requestLogin();
			} else {
				if (connectionError != null && connectionError) {
					makeToastText(R.string.login_message_authentication_noconnection);
				} else {

					makeToastText(R.string.message_async_operation_failed);
				}
			}

		}
	}

	protected void requestLogin() {
		disconnect();
		if (!(this instanceof LoginActivity)) {
			finish();
			LoginActivity.show(this, true);
		}
	}

	protected boolean doOnError(Boolean connectionError,
			Boolean authenticationError) {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.savedInstanceState = null;
		callbacksManager.onDestroy();
		destroyed = true;
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

	@Override
	public MainApplication getApplicationContext() {
		return (MainApplication) super.getApplicationContext();
	}

	public void showLoadingProgressDialog() {
		showProgressDialog("Loading. Please wait...");
	}

	protected void makeToastText(String messageString) {
		Toast.makeText(this, messageString, Toast.LENGTH_LONG).show();
	}

	protected void makeToastText(int messageId) {
		Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
	}

	protected void showProgressDialog(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			showProgressDialog(savedInstanceState
					.getCharSequence(PROGRESSBAR_MESSAGE));
		}
	}

	@OnSuccess(DelayedTask.class)
	public void showProgressDialog(
			@Param(PROGRESSBAR_MESSAGE) CharSequence message) {
		showProgressDialog(message, 0);
	}

	public void showProgressDialog(CharSequence message, long delay) {
		if (message != null) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setIndeterminate(true);
			}

			progressDialog.setMessage(message);
			progressDialog.show();
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressBarMessage = message;
			if (delay > 0) {
				Groundy.create(DelayedTask.class).callback(this)
						.group(GROUP_ID)
						.arg(DelayedTask.DELAY_MILLIS_PARAMETER, delay)
						.arg(PROGRESSBAR_MESSAGE, message)
						.callbackManager(callbacksManager).executeUsing(this);

			}

		}
	}

	public void dismissProgressDialog() {
		progressBarMessage = null;
		GroundyManager.cancelTasksByGroup(this, GROUP_ID, null);
		if (progressDialog != null && !destroyed) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onYes(int code, Object... args) {

	}

	@Override
	public void onNo(int code, Object... args) {

	}

	@Override
	public void onNeutral(int code, Object... args) {

	}

}