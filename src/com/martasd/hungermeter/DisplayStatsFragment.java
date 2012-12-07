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

import com.actionbarsherlock.app.SherlockFragment;
import com.martasd.hungermeter.StatusFragment.OnButtonClickListener;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.os.CountDownTimer;

/** Defines the screen where remaining energy is displayed. */
public class DisplayStatsFragment extends SherlockFragment {
	private TextView remainingTimeContent;
	private TextView remainingTimeLabel;
	public OnButtonClickListener buttonCallback;
	public NotifyToEatListener notifyCallback;
	private FoodTimer foodTimer = null; // indicate that FoodTimer has not started yet
	private static String enteredCalories = null;

	/** A person needs 2,000 calories per day on average. Thus, assuming that 
	 * a person eats 4 equally large meals a day,  she should consume 500 
	 * calories per meal to fulfill her nutritious need. We assume these 
	 * meals occur at 4 hour intervals during the day. These assumptions are
	 * relatively arbitrary, but suffice for the first version of the app.
	 */
	
	public final static double CALORIES_PER_MEAL = 500;
	public final static String ENERGY_DISPLAY = "Time";
	
	// Has to be implemented by the parent (container) activity
	public interface NotifyToEatListener {
	    public void notifyToEat(int remainingMinutes);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Determine the preferred format of displaying energy. */
       // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String syncConnPref = sharedPrefs.getString(PrefsActivityV4.energyDisplay, "");
 
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_display_stats, container, false);
	}
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	remainingTimeContent = (TextView) getView().findViewById(R.id.remainingTimeContent);
        remainingTimeLabel   = (TextView) getView().findViewById(R.id.remainingTimeLabel);
    }
    @Override
    public void onStart() {
    	super.onStart();

    	 /** Retrieve the energyDisplay preference. */
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
         
        String energyDisplay = sharedPrefs.getString("energyDisplay", "");
        
        if (enteredCalories == null) {
        	//setRemainingTimeContent("Tell me how much you have eaten first!");
        	//setRemainingTimeLabelVisibility(false);
        }
        else {
        	updateFoodTimer(enteredCalories);
        	enteredCalories = null;
        }
    }

    public void updateCalories(String calories) {
    	enteredCalories = calories;
    }
    
    /** Update the Food Timer based on data entered by the user. */
    public void updateFoodTimer(String enteredCalories) {
    	
    	long remainingTime; 
    	
    	// If the timer already exists, update it with the new data from the user
    	if (foodTimer != null) {
    		
    		// Retrieve the remaining time and cancel the timer, so that a new one can be created with updated time
    		remainingTime = foodTimer.getCurrentTime();
    		foodTimer.cancel();
    	}
    	else {
    		remainingTime = 0;
    	}
 
        int calories = Integer.parseInt(enteredCalories);
        
        /** TODO: Convert percentage to calories if necessary. */
        
        /** Show either remaining time or percentage. */
        if (ENERGY_DISPLAY.equals("Time"))
        {
        	/** Calculate time to be added. */
            long timeToAdd = caloriesToTime(calories);
            long totalTime = timeToAdd + remainingTime;
            
        	setRemainingTimeLabelVisibility(true);

            /** Create and show a timer. */
            foodTimer = new FoodTimer(totalTime,1000);
            foodTimer.start();         
        }
        else
        {
        	/** Calculate percentage to be added. */
        	double percentageToAdd = caloriesToPercentage(calories);
        }
    }
    
    /** Define a callback interface in order to communicate with parent activity. */
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interfaces. If not, it throws an exception
        try {
            buttonCallback = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickListener");
        }
        
        try {
            notifyCallback = (NotifyToEatListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NotifyToEatListener");
        }
    }
	
    /** Set what appears in the text field in the center of the screen. */
    private void setRemainingTimeContent(String content) {
    	
    	remainingTimeContent.setTextColor(Color.parseColor("#EE0000"));
    	remainingTimeContent.setGravity(Gravity.CENTER);
        remainingTimeContent.setText(content);
    }
	
    private void setRemainingTimeLabelVisibility(boolean isVisible) {
    	if (isVisible) {
    		remainingTimeLabel.setVisibility(View.VISIBLE);
    	}
    	else {
    		remainingTimeLabel.setVisibility(View.INVISIBLE);
    	}	
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
    	
    	private long currentTimeInMillis;
    	
        public FoodTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        	
        	setRemainingTimeContent("You have run out of energy. Eat urgently!");
        	setRemainingTimeLabelVisibility(false);
        	    
        	notifyCallback.notifyToEat(0); 
        }

        @Override
        public void onTick(long millisUntilFinished) {

        	setCurrentTime(millisUntilFinished);

        	// Convert from milliseconds to hours, minutes, and seconds
        	long seconds = millisUntilFinished / 1000;
        	int hours    = (int) Math.floor(seconds / 3600);
        	seconds      = seconds - hours * 3600;
        	int minutes  = (int) Math.floor(seconds / 60);
        	seconds      = seconds - minutes * 60;

        	String displayTime = hours + ":" + minutes + ":" + seconds;
        	setRemainingTimeContent(displayTime);

        	// Signal to Main Activity to send notification to the user  
        	if (hours == 0) {
        		if (minutes == 20 && seconds == 0) {
        			notifyCallback.notifyToEat(20);
        		}
        		if (minutes == 10 && seconds == 0) {
        			notifyCallback.notifyToEat(10);
        		}
        		if (minutes == 0 && seconds == 0) {
        			
        		}
        	}
        }
           
        public long getCurrentTime() {
        	return currentTimeInMillis;
        }
        
        private void setCurrentTime(long currentTime) {
        	currentTimeInMillis = currentTime;
        }
    }
}
