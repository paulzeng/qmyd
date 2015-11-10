package com.ak.qmyd.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
	private static Editor editor;
	private static SharedPreferences sp;

	private static SharedPreferences getPreferencesInstance(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp;
	}

	private static Editor getEditorInstance(Context context) {

		if (editor == null) {
			editor =getPreferencesInstance(context).edit();
		}
		return editor;
	}

	
	public static void setString(Context context, String key, String value) {
		getEditorInstance(context).putString(key, value).commit();
	}

	public static void setBoolean(Context context, String key, boolean value) {
		getEditorInstance(context).putBoolean(key, value).commit();
	}
    public static void setInt(Context context, String key, int value){
    	getEditorInstance(context).putInt(key, value).commit();
    }
    public static void setLong(Context context, String key, long value){
    	getEditorInstance(context).putLong(key, value).commit();
    }
	
	
	
	
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	};

	public static String getString(Context context, String key, String defValue) {
		return getPreferencesInstance(context).getString(key, defValue);
	};

	public static boolean getBoolean(Context context, String key) {
		return getPreferencesInstance(context).getBoolean(key, false);
	}
    public static int getInt(Context context, String key){
    	return getPreferencesInstance(context).getInt(key, -1);
    }
    public static long getLong(Context context, String key){
    	return getPreferencesInstance(context).getLong(key, -1);
    }
}
