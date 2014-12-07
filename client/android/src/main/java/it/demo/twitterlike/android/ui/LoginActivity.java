package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.tasks.LoginTask;
import it.demo.twitterlike.android.ui.utils.Constants;
import it.demo.twitterlike.android.ui.utils.DeviceUtils;
import it.demo.twitterlike.android.ui.utils.ViewUtils;

import org.springframework.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnProgress;
import com.telly.groundy.annotations.Param;

public class LoginActivity extends AbstractConnectAsyncActivity {

	public static final String DISCONNECT_PARAMETER = "disconnect";

	private static final String USER_CONNECTED_CHECKED_PARAMETER = "userConnectedChecked";

	private static boolean connected;

	public static boolean setPreferences(SharedPreferences preferences,
			String username, String password, int id) {
		Editor editor = preferences.edit();
		editor.putString(Constants.USERNAME_ATTRIBUTE_NAME, username);
		editor.putString(Constants.PASSWORD_ATTRIBUTE_NAME, password);
		editor.putInt(Constants.USERID_ATTRIBUTE_NAME, id);
		boolean commit = editor.commit();
		return commit;
	}

	public static void resetPasswordPreferences(SharedPreferences preferences) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();

	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(USER_CONNECTED_CHECKED_PARAMETER,
				connected);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();

		setContentView(R.layout.activity_login);

		if (extras != null) {
			if (extras.getBoolean(DISCONNECT_PARAMETER, false)) {
				disconnect();
			}
		}

		boolean connected = (savedInstanceState != null && savedInstanceState
				.containsKey(USER_CONNECTED_CHECKED_PARAMETER)) ? savedInstanceState
				.getBoolean(USER_CONNECTED_CHECKED_PARAMETER) : isConnected();
		if (connected) {
			doConnect();
		} else {
			if (!DeviceUtils.isOnline(this)) {
				Toast.makeText(LoginActivity.this,
						getString(R.string.login_help_nointernetconnection),
						Toast.LENGTH_LONG).show();
			}

			showConnectOption();
		}

	}

	private void showConnectOption() {
		final EditText userEditText = (EditText) findViewById(R.id.username);
		String defaultUserName = getString(R.string.account_default_username);
		if (StringUtils.hasText(defaultUserName)) {
			userEditText.setText(defaultUserName);
		}
		final EditText passEditText = (EditText) findViewById(R.id.password);

		String defaultPassword = getString(R.string.account_default_password);
		if (StringUtils.hasText(defaultPassword)) {
			passEditText.setText(defaultPassword);
		}

		final Button buttonSend = (Button) findViewById(R.id.sign_in_button);

		// Setto il listener sul bottone
		buttonSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				ViewUtils.closeKeyboard(LoginActivity.this, userEditText);

				if (!DeviceUtils.isOnline(LoginActivity.this)) {

					makeToastText(R.string.login_help_nointernetconnection);

				} else {

					String userName = userEditText.getText().toString();
					String password = passEditText.getText().toString();
					if (userName != null && password != null
							&& userName.trim().length() > 0
							&& password.trim().length() > 0) {

						showProgressDialog(getString(R.string.login_message_user_password_verify));
						Groundy.create(LoginTask.class)
								.callback(LoginActivity.this)
								.callbackManager(callbacksManager)
								// required if you want to
								// get notified of your task
								// lifecycle
								.arg(LoginTask.USERNAME_PARAMETER, userName)
								.arg(LoginTask.PASSWORD_PARAMETER, password)
								.queueUsing(LoginActivity.this);

					} else {
						makeToastText(R.string.login_message_authentication_username_password_required);

					}
				}
			}
		});
	}

	@OnProgress(LoginTask.class)
	public void onProgress(@Param(Groundy.PROGRESS) int progress) {
		switch (progress) {
		case LoginTask.PROGRESS_UPDATE_SUCCESS_AUTHENTICATION: {
			showProgressDialog(getString(R.string.login_message_user_download_content));
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			// NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.menu_settings: {
			Intent intent = new Intent(this, LoginSettingActivity.class);
			startActivityForResult(intent, 1);
			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	static void show(Context context) {
		show(context, false);
	}

	static void show(Context context, boolean disconnect) {
		final Intent intent = new Intent(context, LoginActivity.class);
		intent.putExtra(DISCONNECT_PARAMETER, disconnect);
		context.startActivity(intent);
	}

}
