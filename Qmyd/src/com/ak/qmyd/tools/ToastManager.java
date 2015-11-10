package com.ak.qmyd.tools;

import android.app.Activity;
import android.widget.Toast;

public class ToastManager {

	public static void show(Activity context, Object text) {
		show(context, text, 0);
	}

	public static void show(Activity context, Object text, int duration) {
		if (text instanceof String) {
			Toast.makeText(context, text.toString(), duration).show();
			return;
		}
		if (text instanceof Integer) {
			Toast.makeText(context, context.getString(Integer.parseInt(text.toString())), duration).show();
			return;
		}

	}
}
