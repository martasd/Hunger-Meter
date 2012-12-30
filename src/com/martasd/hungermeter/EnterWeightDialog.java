package com.martasd.hungermeter;

import android.app.Dialog;
import android.os.Bundle;

public class EnterWeightDialog extends EnterDialog {

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {

    // call inherited method from EnterDialog
    return onCreateDialogHelper(
        R.layout.fragment_enter_weight_dialog,
        R.string.weight_dialog_title,
        R.id.weightUnits,
        R.id.enterWeight,
        R.string.buttonEnter,
        "weight");
  }
}