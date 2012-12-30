package com.martasd.hungermeter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class WelcomeDialog extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.fragment_welcome_dialog, null))
		.setIcon(R.drawable.ic_launcher)
		.setTitle(R.string.welcome_dialog_title)
		.setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//Launch the first dialog
				SherlockDialogFragment newFragment = new ChooseUnitsDialog();
				newFragment.show(getFragmentManager(), "unitsDialog");
			}
		})
		.setNegativeButton(R.string.buttonNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				WelcomeDialog.this.getDialog().cancel();
			}
		});

		return builder.create();
	}
}
