package com.martasd.hungermeter;

import android.os.Bundle;
import android.app.Activity;

public class PrefsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		/** Display the preference fragment. */
		getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new PrefsFragment()).commit();
	}	
	
}
