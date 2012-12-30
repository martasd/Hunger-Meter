package com.martasd.hungermeter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EnterDialog extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		return null;
  }

	protected Dialog onCreateDialogHelper (int dialogLayout, int dialogTitle, int unitsName, 
			final int pref, int buttonEnter, final String prefName) { 
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(dialogLayout, null);

		builder.setView(view)
		.setIcon(R.drawable.ic_launcher)
		.setTitle(dialogTitle);

		TextView prefUnits = (TextView) view.findViewById(unitsName);

		// Find out which units to use from preferences
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		String unitsValue = sharedPrefs.getString("units", "0");
		// TODO: Generalize

		//Prepare the next dialog
		final SherlockDialogFragment newFragment;
		final String nextDialog;
		if (prefName.equals("height")) {
			newFragment = new EnterWeightDialog();
			nextDialog = "weightDialog";
			if (unitsValue.equals("0")) {
				prefUnits.setText("cm");
			}
			else {
				prefUnits.setText("ft");
			}
		}
		else if (prefName.equals("weight")) {
			newFragment = new EnterAgeDialog();
			nextDialog = "ageDialog";
			if (unitsValue.equals("0")) {
				prefUnits.setText("kg");
			}
			else {
				prefUnits.setText("lbs");
			}
		}
		else if (prefName.equals("age")) {
			newFragment = new EnterSexDialog();
			nextDialog = "sexDialog";
			prefUnits.setText("yr");
		}
		else {
			newFragment = null;
			nextDialog = null;
		}

		final EditText prefField = (EditText) view.findViewById(pref);
		

		
		builder
		.setPositiveButton(buttonEnter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Save the value entered into preferences

				String prefValue = prefField.getText().toString();

				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());

				SharedPreferences.Editor edit = sharedPrefs.edit();
				edit.putString(prefName, prefValue);
				edit.commit();

				newFragment.show(getFragmentManager(), nextDialog);
			}
		});
		final Dialog dialog = builder.create();
		
		// Show keyboard automatically with the dialog
		prefField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		        }
		    }
		});
		
		return dialog;
	}
}
