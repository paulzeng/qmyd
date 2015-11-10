package com.ak.qmyd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ControlPushBroadCastReceiver extends BroadcastReceiver {
	private static final String TAG = "ControlPushBroadCastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();		 
		if("com.ak.qmyd.pushservice".equals(action)){
			int status = intent.getIntExtra("status", 0);
			Log.d(TAG, " action status "+status);
			Intent newIntent = new Intent(context,PushService.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			if (status == 1) {
				context.startService(newIntent);
			}else{
				context.stopService(newIntent);			
			}
		}
	}

}
