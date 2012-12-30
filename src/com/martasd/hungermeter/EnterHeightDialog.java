package com.martasd.hungermeter;

import android.app.Dialog;
import android.os.Bundle;

public class EnterHeightDialog extends EnterDialog {

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {

    // call inherited method from EnterDialog
    return onCreateDialogHelper(
        R.layout.fragment_enter_height_dialog,
        R.string.height_dialog_title,
        R.id.heightUnits,
        R.id.enterHeight,
        R.string.buttonEnter,
        "height");
  }
}
