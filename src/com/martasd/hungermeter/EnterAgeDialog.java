package com.martasd.hungermeter;

import android.app.Dialog;
import android.os.Bundle;

public class EnterAgeDialog extends EnterDialog {

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {

    // call inherited method from EnterDialog
    return onCreateDialogHelper(
        R.layout.fragment_enter_age_dialog,
        R.string.age_dialog_title,
        R.id.ageUnits,
        R.id.enterAge,
        R.string.buttonEnter,
        "age");
  }
}