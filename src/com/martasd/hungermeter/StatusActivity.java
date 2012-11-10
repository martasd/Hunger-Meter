package com.martasd.hungermeter;

import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/** This defines the activity where the user enters food amount. */
public class StatusActivity extends Activity {
	public final static String EXTRA_AMOUNT = "com.martasd.hungermeter.AMOUNT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        setContentView(R.layout.activity_status);
        
        /** TODO: Launch Splash Screen. */
        
        /** Retrieve the energyDisplay preference. */
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        /** Check if this is the first app run. */
        if (firstRun(sharedPrefs)) {
        	
        	/** Launch dialog asking user to enter Preferences. */
        	
        	/** Indicate that first run has happened already. */
        	setFirstRun(sharedPrefs);
        }

        //String syncConnPref = sharedPrefs.getString(PrefsActivity.foodUnitsType, "");
        
//        /** Show the appropriate Enter Energy fragment. */
//        if (true)
//        {
//        	
//        }
//        else 
//        {
//        
//        }
//              
       
    }
    
    /** Determine if this is the first run of the app. */
    private boolean firstRun(SharedPreferences sharedPrefs) {
    	return sharedPrefs.getBoolean("firstRun", true);
    }
    
    /** Mark that the app has already been run in shared preferences. */
    private void setFirstRun(SharedPreferences sharedPrefs) {
    	
    	SharedPreferences.Editor edit = sharedPrefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
    }

    /** Called when the user clicks the Button */
    public void storeAmount(View view) {
    	
    	Intent intent = new Intent(this,DisplayStatsActivity.class);
    	EditText editText = (EditText) findViewById(R.id.foodIntake);
    	String amount = editText.getText().toString();
    	intent.putExtra(EXTRA_AMOUNT,  amount);
    	startActivity(intent);
    }
    
    /** Called the first time user launches the menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    /** Called when an options item is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.itemPrefs:
    		startActivity(new Intent(this, PrefsActivity.class));
    		return true;
    	case R.id.howItWorks:
    		startActivity(new Intent(this, HowItWorksActivity.class));
    		return true;
    	case R.id.credits:
    		startActivity(new Intent(this, CreditsActivity.class));
    		return true;
    	default:
            return super.onOptionsItemSelected(item);
    	}
    	
    }
    
}
