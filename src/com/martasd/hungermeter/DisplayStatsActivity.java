package com.martasd.hungermeter;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
import android.widget.TextView;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;

/** Defines the screen where remaining energy is displayed. */
public class DisplayStatsActivity extends Activity {
	public TextView remainingTimeContent;
	public TextView remainingTimeLabel;

	public final static double CALORIES_PER_MEAL = 500;
	public final static String ENERGY_DISPLAY = "Time";
	
	/** A person needs 2,000 calories per day on average. Thus, assuming that 
	 * a person eats 4 equally large meals a day,  she should consume 500 
	 * calories per meal to fulfill her nutritious need. We assume these 
	 * meals occur at 4 hour intervals during the day. These assumptions are
	 * relatively arbitrary, but suffice for the first version of the app.
	 */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        
        /** Determine the preferred format of displaying energy. */
//     SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//      String syncConnPref = sharedPrefs.getString(PrefsActivity.energyDisplay, "");
      
        // Get the amount in calories from the intent
        Intent intent = getIntent();
        String cals = intent.getStringExtra(StatusActivity.EXTRA_AMOUNT);
        int calories = Integer.parseInt(cals);
        
        /** TODO: Convert percentage to calories if necessary. */
        
        /** Show either remaining time or percentage. */
        if (ENERGY_DISPLAY.equals("Time"))
        {
        	/** Calculate time to be added. */
            long timeToAdd = caloriesToTime(calories);
            
            /** Create and show a timer. */
            remainingTimeContent = (TextView) findViewById(R.id.remainingTimeContent);
            FoodTimer foodTimer = new FoodTimer(timeToAdd,1000);
            foodTimer.start();
             
        }
        else
        {
        	/** Calculate percentage to be added. */
        	double percentageToAdd = caloriesToPercentage(calories);
        }

    }
    
    /** Notify the user that it's time to eat when FoodTimer reaches 0. */
    public void notifyToEat() {
    	
    	Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
    	
    	/** Instantiate a Builder object. */
        NotificationCompat.Builder builder = 
        		new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_notify) 
        .setLargeIcon(largeIcon)
        .setContentTitle("No more energy left!")
        .setContentText("Eat Eat Eat")
        .setDefaults(-1) // corresponds to DEFAULT_ALL 
        .setAutoCancel(true);

        /** Send intent on clicking the notification. */
        
        /** Creates an Intent for the resulting Activity. */
        Intent resultIntent =
        		new Intent(this, DisplayStatsActivity.class);
        
        /** Start the resulting Activity in a new, empty task. */
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        /** Creates the Pending intent for the resulting Activity. */
        PendingIntent resultPendingIntent = PendingIntent.getActivity
        		(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        /** Put the Pending Intent into the notification builder. */
        builder.setContentIntent(resultPendingIntent);
        
        /** Notifications are issued by sending them to the 
         * NotificationManager system service
         */
        NotificationManager notificationManager =
        		(NotificationManager) 
        		getSystemService(Context.NOTIFICATION_SERVICE);
        
        /** Build an anonymous Notification object 
         * and pass it to Notification Manager
         */
        notificationManager.notify(0, builder.build());
    }
    
	/** Convert from calories to remaining time. */
    private long caloriesToTime(int calories) {
    	
    	double meal_fraction = calories / CALORIES_PER_MEAL;
    	long millisInFourHours = 1000 * 3600 * 4;
    	double total_millis = (meal_fraction * millisInFourHours);
    	return Math.round(total_millis); 
    }
    
    /** Convert from calories to remaining percentage. */
    private double caloriesToPercentage(int calories) {
    	// STUB
    	return 0;
    }
    
    /** Countdown timer that shows time remaining before running out of energy */
    public class FoodTimer extends CountDownTimer {
    	
        public FoodTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
        	
        	remainingTimeContent.setTextColor(Color.parseColor("#EE0000"));
        	remainingTimeContent.setGravity(Gravity.CENTER);
            remainingTimeContent.setText("You have run out of energy. Eat urgently!");
            
            remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
            remainingTimeLabel.setVisibility(View.INVISIBLE);
            
            notifyToEat();
            
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
        	
        	/** Convert from milliseconds to hours, minutes, and seconds */
        	long seconds = millisUntilFinished / 1000;
        	int hours    = (int) Math.floor(seconds / 3600);
        	seconds      = seconds - hours * 3600;
        	int minutes  = (int) Math.floor(seconds / 60);
        	seconds      = seconds - minutes * 60;
         	
        	remainingTimeContent.setText(hours + ":" + minutes + ":" + seconds);
        }

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
