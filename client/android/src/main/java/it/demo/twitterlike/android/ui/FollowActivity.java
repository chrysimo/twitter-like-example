package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.domain.InternalUserProfile;
import it.demo.twitterlike.android.tasks.AddFollowTask;
import it.demo.twitterlike.android.tasks.FollowedListTask;
import it.demo.twitterlike.android.tasks.RemoveFollowTask;
import it.demo.twitterlike.android.ui.adapters.UserListAdapter;

import java.util.ArrayList;
import java.util.Collection;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnStart;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

public class FollowActivity extends AbstractAsyncActivity {

	private UserListAdapter adapter;

	private View noUsersPresentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setTitle(R.string.activity_select_users_title);

		if (adapter == null) {
			adapter = new UserListAdapter(this,
					new ArrayList<InternalUserProfile>());

		}
		this.noUsersPresentView = findViewById(R.id.noMessagesPresentTextView);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		requestUpdate(true);

	}

	public void requestUpdate(boolean showProgressBar) {
		if (showProgressBar) {
			showProgressDialog("Caricamento Configurazione In Corso");
		}
		Groundy.create(FollowedListTask.class).callback(this)
				.callbackManager(callbacksManager).queueUsing(this);

	}

	@OnStart({ AddFollowTask.class, RemoveFollowTask.class,
			FollowedListTask.class })
	public void onProgress() {
		showProgressDialog(getString(R.string.progressbar_loading));
	}

	@OnSuccess({ AddFollowTask.class })
	public void followAdded() {
		makeToastText(R.string.toast_follow_created);
		doFinishAndReturn();
	}

	@OnSuccess({ RemoveFollowTask.class })
	public void followRemoved() {
		makeToastText(R.string.toast_follow_removed);
		doFinishAndReturn();
	}

	protected void doFinishAndReturn() {
		setResult(RESULT_OK);
		finish();
	}

	@OnSuccess({ FollowedListTask.class })
	public void updateResultList(
			@Param(FollowedListTask.ALLUSERS_RESULT) Collection<InternalUserProfile> result) {
		dismissProgressDialog();
		adapter.clear();
		if (result != null) {
			adapter.addAll(result);

		}
		noUsersPresentView
				.setVisibility(result == null || result.size() <= 0 ? View.VISIBLE
						: View.GONE);

		adapter.notifyDataSetChanged();

	}

	public static void show(Activity context, int requestCode) {
		final Intent intent = new Intent(context, FollowActivity.class);
		context.startActivityForResult(intent, requestCode);

	}

}
