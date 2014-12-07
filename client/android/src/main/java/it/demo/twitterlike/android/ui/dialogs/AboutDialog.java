package it.demo.twitterlike.android.ui.dialogs;

import it.demo.twitterlike.android.ui.R;
import it.demo.twitterlike.android.ui.utils.PreferencesManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;

public class AboutDialog extends DialogFragment {

	/**
	 * Constructor
	 * 
	 */
	public AboutDialog() {
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final CheckBox checkBox = new CheckBox(getActivity());

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			checkBox.setTextColor(Color.WHITE);
		}

		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.about)
				.setMessage(R.string.aboutMessage)
				.setView(checkBox)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								PreferencesManager.getInstance(getActivity())
										.setShowAbout(!checkBox.isChecked());
							}
						})

				.setNeutralButton(R.string.goToGitHub,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "https://github.com/chrysimo/twitter-like";
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(url));
								startActivity(i);
							}
						}).create();

	}
}
