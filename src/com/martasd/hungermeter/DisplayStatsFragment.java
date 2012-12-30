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

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.os.CountDownTimer;

import com.actionbarsherlock.app.ActionBar;

/** Defines the screen where remaining energy is displayed. */
public class DisplayStatsFragment extends SherlockFragment {
	OnButtonResetClickListener buttonResetCallback;
	private Button buttonReset;
	private TextView remainingTimeContent;
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
	
	// Colors for FoodTimer
	private final static String GREEN = "#008B00";
	private final static String YELLOW = "#FFAA00";
	private final static String RED = "#EE0000";
	
	// Has to be implemented by the parent (container) activity
	public interface NotifyToEatListener {
	    public void notifyToEat(int remainingMinutes);
	}
	
	// Has to be implemented by the parent (container) activity
	public interface OnButtonResetClickListener {
        public void onButtonResetClick();
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
        buttonReset          = (Button)   getView().findViewById(R.id.buttonReset);
    }
    @Override
    public void onStart() {
    	super.onStart();
		remainingTimeContent.setVisibility(View.VISIBLE);


    	buttonReset.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			if (foodTimer != null) {
    				foodTimer.cancel();
    				foodTimer = null;
    				remainingTimeContent.setVisibility(View.INVISIBLE);
    			}
    			
    			// Switch to StatusFragment
    			buttonResetCallback.onButtonResetClick();
    		}
    	});
    	
    	/** Retrieve the energyDisplay preference. */
    	//SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());   
    	//String energyDisplay = sharedPrefs.getString("energyDisplay", "");

    	if (enteredCalories != null) {
    		// The user entered calories
    		updateFoodTimer(enteredCalories);
    		selectFoodTimerColor();
    		buttonReset.setVisibility(View.VISIBLE);
    		enteredCalories = null;
    	}
    	else if (foodTimer == null) {
    		// Food Timer has not been started yet
    		setRemainingTimeContent("Tell me how much you have eaten first!");
    		setRemainingTimeContentColor(GREEN);
    		buttonReset.setVisibility(View.INVISIBLE);
    	}
    	else {
    		// Select appropriate color on start
    		selectFoodTimerColor();
    		buttonReset.setVisibility(View.VISIBLE);
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
        
        /** TODO: Convert calories to percentage if necessary. */
        
        /** Show either remaining time or percentage. */
        if (ENERGY_DISPLAY.equals("Time"))
        {
        	/** Calculate time to be added. */
            long timeToAdd = caloriesToTime(calories);
            long totalTime = timeToAdd + remainingTime;

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
            notifyCallback = (NotifyToEatListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NotifyToEatListener");
        }      
        try {
            buttonResetCallback = (OnButtonResetClickListener) activity;
        } 
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonResetClickListener");
        }
    }
	
    /** Set what appears in the text field in the center of the screen. */
    private void setRemainingTimeContent(String content) {
    	
    	remainingTimeContent.setGravity(Gravity.CENTER);
        remainingTimeContent.setText(content);
    }
	
    /** Set the color of the FoodTimer. */
    private void setRemainingTimeContentColor(String color) {
    	remainingTimeContent.setTextColor(Color.parseColor(color));
    }
    
	/** Convert from calories to remaining time in milliseconds. */
    private long caloriesToTime(int calories) {
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	double height = Double.parseDouble(prefs.getString("height", ""));
    	double weight = Double.parseDouble(prefs.getString("weight", ""));
    	double age    = Double.parseDouble(prefs.getString("age", ""));
    	
    	String sex = prefs.getString("sex", "");

    	// Calculate Basal Metabolic rate using revised Harris-Benedict equation
    	double bmr;
    	if (sex.equals("Male")) {
    		bmr = 88.362 + (13.397 * weight) + (5.799 * height) - (5.677 * age);
    	}
    	else {
    		bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);  		
    	}
    	
    	double exerciseFactor = 1.2;
    	double caloriesPerDay = bmr * exerciseFactor;
    	
    	// Fraction of calories needed daily
    	double caloriesFraction = calories / caloriesPerDay;
    	
    	long millisInDay = 3600 * 24 * 1000;
    	double totalMillis = caloriesFraction * millisInDay;
    	return Math.round(totalMillis); 
    }
    
    /** Convert from calories to remaining percentage. */
    private double caloriesToPercentage(int calories) {
    	// STUB
    	return 0;
    }
    
    private void selectFoodTimerColor() {
    	long currentTime = foodTimer.getCurrentTime();
        if (currentTime > 1200000) {
        	setRemainingTimeContentColor(GREEN);
        }
        else if (currentTime < 1200000 && currentTime > 600000) {
        	setRemainingTimeContentColor(YELLOW);
        }
        else {
        	setRemainingTimeContentColor(RED);
        }
    }
    
    /** Countdown timer that shows time remaining before running out of energy */
    public class FoodTimer extends CountDownTimer {
    	
    	private long currentTimeInMillis;
    	
        public FoodTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            
            // Needs to be set so that colors can be chosen upon timer creation
        	setCurrentTime(millisInFuture);
        }

        @Override
        public void onFinish() {
        	
        	setRemainingTimeContent("You have run out of energy. Eat urgently!");       	    
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
        	     	setRemainingTimeContent(displayTime);
        	     	setRemainingTimeContentColor(YELLOW);
        			notifyCallback.notifyToEat(20);
        		}
        		if (minutes == 10 && seconds == 0) {
        			notifyCallback.notifyToEat(10);
        			setRemainingTimeContent(displayTime);
        			setRemainingTimeContentColor(RED);
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
