package it.demo.twitterlike.android.ui.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AbstractSettingFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private final String[] mAutoSummaryFields;
	private final int mEntryCount;
	private Preference[] mPreferenceEntries;
	private final int preferenceResouceId;

	private boolean valueChanged;

	public AbstractSettingFragment(int preferenceResouceId,
			String... mAutoSummaryFields) {
		this.mAutoSummaryFields = mAutoSummaryFields;
		this.mEntryCount = mAutoSummaryFields.length;
		this.preferenceResouceId = preferenceResouceId;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		// Load the preferences from an XML resource
		addPreferencesFromResource(preferenceResouceId);
		mPreferenceEntries = new Preference[mEntryCount];
		for (int i = 0; i < mEntryCount; i++) {
			mPreferenceEntries[i] = getPreferenceScreen().findPreference(
					mAutoSummaryFields[i]);
		}
	}

	public boolean isValueChanged() {
		return this.valueChanged;
	}

	@Override
	public void onResume() {
		super.onResume();
		for (int i = 0; i < mEntryCount; i++) {
			updateSummary(mAutoSummaryFields[i]); // initialization
		}
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this); // register
																	// change
																	// listener
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this); // unregister
																	// change
																	// listener
	}

	private void updateSummary(String key) {
		for (int i = 0; i < mEntryCount; i++) {
			if (key.equals(mAutoSummaryFields[i])) {
				if (mPreferenceEntries[i] instanceof EditTextPreference) {
					final EditTextPreference currentPreference = (EditTextPreference) mPreferenceEntries[i];
					mPreferenceEntries[i].setSummary(currentPreference
							.getText());
				} else if (mPreferenceEntries[i] instanceof ListPreference) {
					final ListPreference currentPreference = (ListPreference) mPreferenceEntries[i];
					mPreferenceEntries[i].setSummary(currentPreference
							.getEntry());
				}
				break;
			}
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updateSummary(key);
		this.valueChanged = true;
	}
}
