package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.ui.fragments.LoginSettingFragment;
import android.app.Activity;
import android.os.Bundle;

public class LoginSettingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new LoginSettingFragment())
				.commit();
	}

}
