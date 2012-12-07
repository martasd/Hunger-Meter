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

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;

/** This defines the activity where the user enters food amount. */
public class StatusFragment extends SherlockFragment {
	public final static String EXTRA_AMOUNT = "com.martasd.hungermeter.AMOUNT";
	OnButtonClickListener buttonCallback;
	
	// Has to be implemented by the parent (container) activity
	public interface OnButtonClickListener {
        public void onButtonClick(String amount);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_status, container, false);
	    }
	
    
	/** Define a callback interface in order to communicate with parent activity. */
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            buttonCallback = (OnButtonClickListener) activity;
        } 
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

	@Override
	public void onStart() {
		super.onStart();

		// Pass the entered amount to the container activity when button is clicked
		Button enterButton = (Button) getView().findViewById(R.id.buttonEnter);
		enterButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Retrieve the entered amount
				EditText editText = (EditText) getView().findViewById(R.id.foodIntake);
				String amount = editText.getText().toString();

				// pass the amount to the container function
				buttonCallback.onButtonClick(amount);
			}
		});
	}
}
