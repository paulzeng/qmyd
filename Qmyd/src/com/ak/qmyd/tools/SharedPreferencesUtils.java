package com.ak.qmyd.tools;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * 优化后的SharedPreferences更加方便操作
 * @author yc
 *
 */
public class SharedPreferencesUtils {
	private String name = "config";
	private SharedPreferences sp = null;
	
	public SharedPreferencesUtils(Context context){
		sp = context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
	}
	
	public SharedPreferencesUtils(Context context,String name){
		
	    this.name = name;
		sp = context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
	}
	
	public void saveStringData(String key,String value){
		sp.edit().putString(key, value).commit();
	}	
	public void saveBooleanData(String key,Boolean value){
		sp.edit().putBoolean(key, value).commit();
	}
	
	public String getStringData(String key,String defValue){
		return sp.getString(key, defValue);
	}

	public boolean getBooleanData(String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}
	
	public boolean removeData(String key){
		return sp.edit().remove(key).commit();
	}
}
