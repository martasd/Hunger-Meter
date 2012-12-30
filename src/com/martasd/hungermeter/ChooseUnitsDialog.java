package com.martasd.hungermeter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ChooseUnitsDialog extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());

		builder.setTitle(R.string.units_dialog_title)
		.setItems(R.array.units_dialog_entries, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// which is the index of the selected item
				String units = Integer.toString(which);
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());

				SharedPreferences.Editor edit = sharedPrefs.edit();
				edit.putString("units", units);
				edit.commit();

				//Launch the next dialog
				SherlockDialogFragment newFragment = new EnterHeightDialog();
				newFragment.show(getFragmentManager(), "heightDialog");
			}
		});

		return builder.create();
	}
}
