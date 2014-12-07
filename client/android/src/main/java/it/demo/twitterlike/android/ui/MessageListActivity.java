package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.application.MainApplication;
import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.android.tasks.MessageListTask;
import it.demo.twitterlike.android.ui.adapters.MessageListAdapter;
import it.demo.twitterlike.android.ui.fragments.CallbackDialogFragment;
import it.demo.twitterlike.android.ui.utils.Constants;
import it.demo.twitterlike.rest.api.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

public class MessageListActivity extends AbstractAsyncActivity {

	private static final String ANIMATE_REFRESH = "animateRefresh";

	private static final String AUTOMATIC_CHECK_CALLED = "automaticCheckCalled";

	private static final String MESSAGES = "messages";

	private static final int ADDMESSAGE_REQUEST_CODE = 56;

	private static final int ADDUSER_REQUEST_CODE = 57;

	private MessageListAdapter adapter;

	private View noMessagePresentsView;

	private boolean automaticCheckCalled;

	/**
	 * Refresh menu item
	 */
	protected MenuItem refreshItem;

	static void show(Context context) {
		final Intent intent = new Intent(context, MessageListActivity.class);
		context.startActivity(intent);

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putBoolean(ANIMATE_REFRESH, refreshItem != null
				&& refreshItem.getActionView() != null);

		savedInstanceState.putBoolean(AUTOMATIC_CHECK_CALLED,
				automaticCheckCalled);

		savedInstanceState.putSerializable(MESSAGES,
				(Serializable) adapter.getAll());

	}

	public MainApplication getMainApplication() {
		return (MainApplication) super.getApplication();
	}

	@Override
	public void onBackPressed() {
		CallbackDialogFragment.createInstance(
				Constants.DIALOG_CODE_APPLICATION_QUIT,
				R.string.activity_message_list_quit_title, null,
				R.string.activity_message_list_quit_yes,
				R.string.activity_message_list_quit_no, null).show(
				getFragmentManager(), "exit");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			refreshIfNeeded(false);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_messagelist);

		ActionBar actionBar = getActionBar();

		actionBar.setTitle(R.string.activity_messagelist_title);
		updateConnectedUser(getAuthenticatedUser());

		noMessagePresentsView = findViewById(R.id.noMessagesPresentTextView);
		if (adapter == null) {
			adapter = new MessageListAdapter(this,
					new ArrayList<InternalMessage>());

		}
		if (savedInstanceState != null) {

			this.automaticCheckCalled = savedInstanceState
					.getBoolean(AUTOMATIC_CHECK_CALLED);
			updateResultList((List<InternalMessage>) savedInstanceState
					.get(MESSAGES));
		} else {

			requestUpdate(true);
		}

		ListView listView = (ListView) findViewById(android.R.id.list);

		listView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_activity_messages, menu);
		this.refreshItem = menu.findItem(R.id.refresh);
		Bundle bundle = getSavedInstanceState();
		if (bundle != null && bundle.getBoolean(ANIMATE_REFRESH, false)) {
			showRefreshAnimation();
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;

		case R.id.addMessage: {
			MessageCreateActivity.show(this, ADDMESSAGE_REQUEST_CODE);

			return true;
		}
		case R.id.addFollow: {
			FollowActivity.show(this, ADDUSER_REQUEST_CODE);

			return true;
		}

		case R.id.menu_logout: {

			CallbackDialogFragment.createInstance(
					Constants.DIALOG_CODE_APPLICATION_LOGOUT,
					R.string.activity_message_list_logout_title, null,
					R.string.activity_message_list_logout_yes,
					R.string.activity_message_list_logout_no, null).show(
					getFragmentManager(), "logout");

			return true;
		}

		case R.id.refresh: {
			forceRefresh();

			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void hideRefreshAnimation() {
		if (refreshItem != null) {
			refreshItem.setEnabled(true);
			View view = refreshItem.getActionView();
			if (view != null) {
				view.clearAnimation();
				refreshItem.setActionView(null);
			}
		}
	}

	protected boolean doOnError(Boolean connectionError,
			Boolean authenticationError) {
		hideRefreshAnimation();
		return false;
	}

	private void showRefreshAnimation() {
		if (refreshItem != null) {
			hideRefreshAnimation();
			refreshItem.setEnabled(false);
			ImageView refreshActionView = (ImageView) getLayoutInflater()
					.inflate(R.layout.action_view, null);
			refreshActionView.setImageResource(R.drawable.ic_action_refresh);
			refreshItem.setActionView(refreshActionView);
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.refresh);
			animation.setRepeatMode(Animation.RESTART);
			animation.setRepeatCount(Animation.INFINITE);
			refreshActionView.startAnimation(animation);
		}
	}

	protected void forceRefresh() {
		refreshIfNeeded(true);
	}

	protected void refreshIfNeeded(boolean force) {
		showRefreshAnimation();
		requestUpdate(false);
	}

	protected void updateConnectedUser(UserProfile user) {
		String fullName = null;
		if (user != null) {
			fullName = user.getFirstName() + " " + user.getLastName();
		}
		getActionBar().setSubtitle(fullName);
	}

	
	public void requestUpdate(boolean showProgressBar) {
		if (showProgressBar) {
			showProgressDialog(getString(R.string.progressbar_loading));
		}
		Groundy.create(MessageListTask.class).callback(this)
				.callbackManager(callbacksManager).queueUsing(this);

	}

	@OnSuccess({ MessageListTask.class })
	public void updateResultList(
			@Param(MessageListTask.MESSAGE_LIST) Collection<InternalMessage> result) {
		dismissProgressDialog();
		adapter.clear();
		if (result != null) {
			adapter.addAll(result);

		}

		adapter.notifyDataSetChanged();
		noMessagePresentsView.setVisibility(result == null
				|| result.size() <= 0 ? View.VISIBLE : View.GONE);
		hideRefreshAnimation();
		if (!automaticCheckCalled) {
			refreshIfNeeded(false);
			automaticCheckCalled = true;
		}

	}


	@Override
	public void onNeutral(int code, Object... args) {
		switch (code) {
		case Constants.DIALOG_CODE_APPLICATION_LOGOUT: {

			break;

		}

		}
	}

	@Override
	public void onYes(int code, Object... args) {

		switch (code) {
		case Constants.DIALOG_CODE_APPLICATION_QUIT: {
			finish();
			break;
		}

		case Constants.DIALOG_CODE_APPLICATION_LOGOUT: {
			requestLogin();
		}

		}

	}
}
