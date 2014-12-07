package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.tasks.LoginTask;

import com.telly.groundy.annotations.OnSuccess;

public abstract class AbstractConnectAsyncActivity extends
AbstractAsyncActivity {

	@OnSuccess({ LoginTask.class })
	public void doConnect() {
		dismissProgressDialog();
		finish();
		MessageListActivity.show(this);
	}

}
