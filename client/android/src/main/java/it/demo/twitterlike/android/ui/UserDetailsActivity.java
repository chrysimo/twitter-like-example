package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.android.domain.InternalUserProfile;
import it.demo.twitterlike.android.tasks.MessageListTask;
import it.demo.twitterlike.android.ui.adapters.MessageListAdapter;
import it.demo.twitterlike.android.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.hateoas.Link;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

public class UserDetailsActivity extends AbstractAsyncActivity {

	private static final String EXTRA_USERPROFILE_PARAMETER = "userprofile";

	private MessageListAdapter adapter;

	private View noMessagePresentsView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdetails);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();

		InternalUserProfile userProfile = (InternalUserProfile) intent
				.getSerializableExtra(EXTRA_USERPROFILE_PARAMETER);
		actionBar.setTitle(UserUtils.getUserFullName(userProfile.getUser()));

		if (adapter == null) {
			adapter = new MessageListAdapter(this,
					new ArrayList<InternalMessage>());

		}
		noMessagePresentsView = findViewById(R.id.noMessagesPresentTextView);
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);

		requestUpdate(userProfile.getMessages(), true);

	}

	public void requestUpdate(Link link, boolean showProgressBar) {
		if (showProgressBar) {
			showProgressDialog("Message Loading");
		}
		Groundy.create(MessageListTask.class).callback(this)
				.arg(MessageListTask.ARGS_LINK, link.getHref())
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

	}

	public static void show(Context context, InternalUserProfile userProfile) {
		final Intent intent = new Intent(context, UserDetailsActivity.class);
		intent.putExtra(EXTRA_USERPROFILE_PARAMETER, userProfile);
		context.startActivity(intent);

	}

}
