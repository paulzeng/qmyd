package com.ak.qmyd.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

@SuppressLint("NewApi")
public class RegisterTask extends AsyncTask<Void, Integer, String> {
	Activity mActivity; 
	RegisterCallback mCallback;
	AsyncTasks.Result statusResult;
	String phone;

	public RegisterTask(Activity activty, String phone,
			RegisterCallback callback) {
		mActivity = activty; 
		this.phone = phone;
		mCallback = callback;
	}

	public interface RegisterCallback {
		public void onRegisterSuccess(String authBean);
		public void onRegisterError();
	}

	@Override
	protected String doInBackground(Void... data) {
		try { 
			RegisterResource resource = new RegisterResource();
			String response = resource.register(phone);
			if (response != null) {
				DebugUtility.showLog("获取验证码返回response=" + response);
			} else {
				DebugUtility.showLog("FAILED");
			}
			statusResult = AsyncTasks.Result.OK;
			return response;
		} catch (Exception e) {
			DebugUtility.showLog(e.getMessage().toString());
			statusResult = AsyncTasks.Result.FAILED; 
		}

		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String authBean) {
		super.onPostExecute(authBean);
		DebugUtility.showLog("RESULT " + statusResult);
		if (statusResult == AsyncTasks.Result.OK) {
			if (mCallback != null)
				mCallback.onRegisterSuccess(authBean);
		} else if (statusResult == AsyncTasks.Result.FAILED) {
			if (mCallback != null)
				mCallback.onRegisterError();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
}
