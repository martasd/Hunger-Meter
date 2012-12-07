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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import com.martasd.hungermeter.DisplayStatsFragment.NotifyToEatListener;
import com.martasd.hungermeter.StatusFragment.OnButtonClickListener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.inputmethod.InputMethodManager;
import android.app.NotificationManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This is a container activity for all application fragments.
 *
 * Adapted from:
 * android-sdk/samples/android-16/ApiDemos/src/com/example/android/apis/app/FragmentTabs.java
 */
public class MainActivity extends SherlockFragmentActivity implements OnButtonClickListener, NotifyToEatListener {
	public ActionBar actionBar;
	public ActionBar.Tab statusTab;
	public ActionBar.Tab displayTab;
	private DisplayStatsTabListener displayStatsTabListener;
	private TabListener<StatusFragment> statusTabListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
		
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        /** Check if this is the first app run. */
        if (firstRun(sharedPrefs)) {
        	
        	/** TODO: Launch dialog asking user to enter Preferences. */
        	
        	/** Indicate that first run has happened already. */
        	setFirstRun(sharedPrefs);
        }
       	
		// Setup the action bar for tabs.
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowTitleEnabled(false);
				
		statusTab = actionBar.newTab();
		statusTabListener = new TabListener<StatusFragment>(this, "status", StatusFragment.class);
		actionBar.addTab(statusTab
				.setText(R.string.tab_name_status)
				.setTabListener(statusTabListener));	
		
		displayTab = actionBar.newTab();
		displayStatsTabListener = new DisplayStatsTabListener(this, "display");
		actionBar.addTab(displayTab
				.setText(R.string.tab_name_display_stats)
				.setTabListener(displayStatsTabListener));
	
		// If a previous state was saved, restore 
//		if (savedInstanceState != null) {
//			actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
//		}
	}

  /** Save the activity's stated is saved before it is destroyed. */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
  }
	
  @Override
  public void onButtonClick(String amount) {
  
      // Hide the keyboard when the user presses enter button
	  InputMethodManager inputManager = 
			  (InputMethodManager) 
			  getSystemService(Context.INPUT_METHOD_SERVICE); 
	  inputManager.hideSoftInputFromWindow(
			  this.getCurrentFocus().getWindowToken(),
			  InputMethodManager.HIDE_NOT_ALWAYS); 

	  // Switch to the DisplayStats tab to show the remaining time
	  FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
	  //statusTabListener.onTabUnselected(statusTab, ft);
	  //displayStatsTabListener.onTabSelected(displayTab, ft);
	  actionBar.selectTab(displayTab);
	  ft.commit();
	  DisplayStatsFragment displayStatsFragment = (DisplayStatsFragment) displayStatsTabListener.getFragment();
	  if (displayStatsFragment != null) {
		  displayStatsFragment.updateCalories(amount);
	  }
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
  
  /** Notify the user that it's time to eat when FoodTimer reaches 0. */
  @Override
  public void notifyToEat(int remainingMinutes) {

    Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
    String notificationText;
    
    if (remainingMinutes != 0) {
    	notificationText = "You have energy only for " + remainingMinutes + " more minutes.";
    }
    else {
    	notificationText = "You don't have any energy left.";
    }
    
    /** Instantiate a Builder object. */
    NotificationCompat.Builder builder = 
      new NotificationCompat.Builder(this)
      .setSmallIcon(R.drawable.ic_stat_notify) 
      .setLargeIcon(largeIcon)
      .setContentTitle("Eat Eat Eat")
      .setContentText(notificationText)
      .setDefaults(-1) // corresponds to DEFAULT_ALL 
      .setAutoCancel(true);

    /** Send intent on clicking the notification. */

    // Creates an Intent for the resulting Activity.
    Intent resultIntent =
      new Intent(this, MainActivity.class);

    // Start the resulting Activity in a new, empty task.
    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    // Creates the Pending intent for the resulting Activity.
    PendingIntent resultPendingIntent = PendingIntent.getActivity
      (this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Put the Pending Intent into the notification builder.
    builder.setContentIntent(resultPendingIntent);

    // Notifications are issued by sending them to the NotificationManager
    NotificationManager notificationManager =
      (NotificationManager) 
      getSystemService(Context.NOTIFICATION_SERVICE);

    /** Build an anonymous Notification object 
     * and pass it to Notification Manager
     */
    notificationManager.notify(0, builder.build());
  }

  /** Called the first time user launches the menu */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getSupportMenuInflater().inflate(R.menu.menu, menu);
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

  /** Need to implement the Tab Listener interface to specify what to do on
   * tab actions.
   */
  public static class TabListener<T extends SherlockFragment> implements ActionBar.TabListener {
    private final SherlockFragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private final Bundle mArgs;
    private SherlockFragment mFragment;

    public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz) {
      this(activity, tag, clz, null);
    }

    public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz, Bundle args) {
      mActivity = activity;
      mTag = tag;
      mClass = clz;
      mArgs = args;

      // IS THIS WHAT I WANT???
      // If a fragment already exists, deactivate it since tab isn't shown in
      // our initial state
//      if (!mFragment.isDetached()) {
//	        ft = mActivity.getSupportFragmentManager().beginTransaction();
//	        ft.detach(mFragment);
//	        ft.commit();
//	      }
    }
    

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
      mFragment = (SherlockFragment) mActivity.getSupportFragmentManager().findFragmentByTag(mTag);

      if (mFragment == null) {
        mFragment = (SherlockFragment) SherlockFragment.instantiate(mActivity, mClass.getName(), mArgs);
        ft.add(android.R.id.content, mFragment, mTag); 
      }
      else {
        ft.attach(mFragment);
      }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
      if (mFragment != null) {
        ft.detach(mFragment);
      }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
      // Nothing needs to be done besides showing the reselected fragment
    }
  }
  
  public static class DisplayStatsTabListener implements ActionBar.TabListener {
	    private final SherlockFragmentActivity mActivity;
	    private final String mTag;
	    private DisplayStatsFragment mFragment;

	    public DisplayStatsTabListener(SherlockFragmentActivity activity, String tag) {
	      mActivity = activity;
	      mTag = tag;

	      // IS THIS WHAT I WANT???
	      // If a fragment already exists, deactivate it since tab isn't shown in
	      // our initial state
//	      if (!mFragment.isDetached()) {
//		        ft = mActivity.getSupportFragmentManager().beginTransaction();
//		        ft.detach(mFragment);
//		        ft.commit();
//		      }
	    }

	    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	      mFragment = (DisplayStatsFragment) mActivity.getSupportFragmentManager().findFragmentByTag(mTag);

	      if (mFragment == null) {
	        mFragment = new DisplayStatsFragment();
	        ft.add(android.R.id.content, mFragment, mTag); 
	      }
	      else {
	        ft.attach(mFragment);
	      }
	    }

	    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	      if (mFragment != null) {
	        ft.detach(mFragment);
	      }
	    }

	    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	      // Nothing needs to be done besides showing the reselected fragment
	    }
	    
	    public SherlockFragment getFragment() {
	    	return mFragment;
	    }
	  }
}
