package com.martasd.hungermeter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** Load preferences from XML */
		addPreferencesFromResource(R.xml.prefs);
	}
}
