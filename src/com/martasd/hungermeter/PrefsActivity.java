/**
 *   Hunger Meter
 *
 *   Copyright © 2012 Martin Dluhos
 *
 *   This file is part of Hunger Meter.
 *
 *   Hunger Meter is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Hunger Meter is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Hunger Meter.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.martasd.hungermeter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefsActivity extends PreferenceActivity 
implements OnSharedPreferenceChangeListener {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Set summaries for all preferences
		String prefs[] = {"height", "weight", "age", "units", "sex"}; 
		for (String key : prefs) 
			setSummary(sharedPrefs, key);
	}

	/* If preference is changed, update its summary. */ 
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		setSummary(sharedPreferences, key);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
		.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
		.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void setSummary(SharedPreferences sharedPreferences, 
			String key) {

		Preference pref= findPreference(key);
		String val = sharedPreferences.getString(key, "");

	   String unitsValue = sharedPreferences.getString("units", "0");

		if ( key.equals("height") || key.equals("weight") || key.equals("age")) {
			pref.setSummary(val);
		}
		else if (key.equals("units")) {

			if (val.equals("0")) {
				pref.setSummary("Metric");
			}
			else {
				pref.setSummary("Imperial");
			}
		}
		else if (key.equals("sex")) {

			if (val.equals("0")) {
				pref.setSummary("Male");
			}
			else {
				pref.setSummary("Female"); 
			}
		}
	}
}
