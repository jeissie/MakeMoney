package com.exceptiondialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ShowExceptionDialog {

	private static boolean flag = true;

	public static void showNetDialog(Context context, String exception) {
		if (flag ) {
			flag = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(exception)
					.setCancelable(false)
					.setPositiveButton("È·¶¨",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									flag = true;
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}
	}

}
