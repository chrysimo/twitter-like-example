package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.ui.utils.DeviceUtils;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AbstractConnectAsyncActivity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		makeToastText(getString(R.string.toast_version_number,
				DeviceUtils.getApplicationVersion(this)));
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				if (isConnected()) {
					doConnect();
				} else {
					// This method will be executed once the timer is over
					// Start your app main activity
					LoginActivity.show(SplashScreenActivity.this);
					// close this activity
					finish();
				}

			}
		}, SPLASH_TIME_OUT);
	}

}