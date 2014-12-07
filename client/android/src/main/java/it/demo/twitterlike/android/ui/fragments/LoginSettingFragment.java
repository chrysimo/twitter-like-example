package it.demo.twitterlike.android.ui.fragments;

import it.demo.twitterlike.android.ui.R;
import android.content.Context;
import android.content.SharedPreferences;

public class LoginSettingFragment extends AbstractSettingFragment {

	private static final String[] mAutoSummaryFields = {
			"pref_network_protocol", "pref_network_hostname",
			"pref_network_port", "pref_network_context" }; // change here

	public LoginSettingFragment() {
		super(R.xml.preferences_network, mAutoSummaryFields);
	}

	public static boolean supports(String key) {
		for (int i = 0; i < mAutoSummaryFields.length; i++) {
			if (mAutoSummaryFields[i].equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}

	private static String getDefaultHostProtocol(Context context) {
		return context.getString(R.string.network_server_protocol);
	}

	private static String getDefaultHostPort(Context context) {
		return context.getString(R.string.network_server_port);
	}

	private static String getDefaultHostname(Context context) {
		return context.getString(R.string.network_server_hostname);
	}

	private static String getDefaultContext(Context context) {
		return context.getString(R.string.network_server_context);
	}

	public static String getConnectionAddress(Context context,
			SharedPreferences sharedPreferences) {

		return sharedPreferences.getString(mAutoSummaryFields[0],
				getDefaultHostProtocol(context))
				+ "://"
				+ sharedPreferences.getString(mAutoSummaryFields[1],
						getDefaultHostname(context))
				+ ":"
				+ sharedPreferences.getString(mAutoSummaryFields[2],
						getDefaultHostPort(context))
				+ "/"
				+ sharedPreferences.getString(mAutoSummaryFields[3],
						getDefaultContext(context));
	}

}
