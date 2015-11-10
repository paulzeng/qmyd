package com.ak.qmyd.tools;


import android.app.Activity;
import android.os.AsyncTask;

/** 
 * @author JGB
 * @date 2015-5-27 下午8:39:59
 */
public class ForgetPwdTask  extends AsyncTask<Void, Integer, String> {

	Activity mActivity; 
	ForgetPwdCallback mCallback;
	AsyncTasks.Result statusResult;
	String phone;

	public ForgetPwdTask(Activity activty, String phone,
			ForgetPwdCallback forgetPwdCallback) {
		mActivity = activty; 
		this.phone = phone;
		mCallback = forgetPwdCallback;
	}

	public interface ForgetPwdCallback {
		public void onForgetPwdSuccess(String authBean);
		public void onForgetPwdError();
	}

	@Override
	protected String doInBackground(Void... data) {
		try { 
			ForgetPwdResouce resource = new ForgetPwdResouce();
			String response = resource.forgetPwd(phone);
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
				mCallback.onForgetPwdSuccess(authBean);
		} else if (statusResult == AsyncTasks.Result.FAILED) {
			if (mCallback != null)
				mCallback.onForgetPwdError();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
