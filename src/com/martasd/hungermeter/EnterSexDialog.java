package com.martasd.hungermeter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EnterSexDialog extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());

		builder.setTitle(R.string.sex_dialog_title)
		.setItems(R.array.sex_dialog_entries, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// which is the index of the selected item
				// save the selected sex into preferences
				String sex = Integer.toString(which);
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());

				SharedPreferences.Editor edit = sharedPrefs.edit();
				edit.putString("sex", sex);
				edit.commit();

				//Launch the SetupDone dialog
				SherlockDialogFragment newFragment = new SetupDoneDialog();
				newFragment.show(getFragmentManager(), "doneDialog");
			}
		});

		return builder.create();
	}
}
