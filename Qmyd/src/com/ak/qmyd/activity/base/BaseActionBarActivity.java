package com.ak.qmyd.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/** 
 * @author HM
 * @date 2015-4-18 上午10:36:49
 */
public class BaseActionBarActivity extends ActionBarActivity implements
		IActivitySupport {

	private boolean isActive;//Activity是否在活动
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		isActive=true;
		context=this;
		
	}

	@Override
	protected void onStart() {
		isActive=true;
		super.onStart();
	}

	@Override
	protected void onRestart() {
		
		super.onRestart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	
	}

	@Override
	protected void onPause() {
		isActive=false;
		super.onPause();
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void showToast(String text, int longint) {
		// TODO Auto-generated method stub
		if(isActive)
			Toast.makeText(context, text, longint).show();
		
	}

	@Override
	public void showToast(String text) {
		// TODO Auto-generated method stub
		if(isActive)
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showToast(int resId) {
		// TODO Auto-generated method stub
		if(isActive)
			Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void startActivity(Class<?> clazz,String str) {
		// TODO Auto-generated method stub
		Intent it=new Intent(context, clazz);
		startActivity(it);
	}
}