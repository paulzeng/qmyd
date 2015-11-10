package com.ak.qmyd.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ak.qmyd.activity.FocusActivity;
import com.ak.qmyd.bean.Focus;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CustomCrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "Activity";
	private Context mContext;
	private static final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().toString();
	private static CustomCrashHandler mInstance = new CustomCrashHandler();

	private CustomCrashHandler() {
	}

	public static CustomCrashHandler getInstance() {
		return mInstance;
	}

	@SuppressWarnings("static-access")
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		savaInfoToSD(mContext, ex);
		sendInfoToHost(mContext, ex);
		showToast(mContext, "很抱歉，程序遇到异常，即将退出");
		try {
			thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		((MyApplication) mContext.getApplicationContext()).exit();
	}

	public void setCustomCrashHanler(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private void showToast(final Context context, final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}).start();
	}

	@SuppressLint("NewApi")
	private HashMap<String, String> obtainSimpleInfo(Context context) {
		HashMap<String, String> map = new HashMap<String, String>();
		PackageManager mPackageManager = context.getPackageManager();
		PackageInfo mPackageInfo = null;
		try {
			mPackageInfo = mPackageManager.getPackageInfo(
					context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		map.put("errorTime", paserTime(System.currentTimeMillis()));
		map.put("versionName", mPackageInfo.versionName);
		map.put("versionCode", "" + mPackageInfo.versionCode);

		return map;
	}

	private String obtainExceptionInfo(Throwable throwable) {
		StringWriter mStringWriter = new StringWriter();
		PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
		throwable.printStackTrace(mPrintWriter);
		mPrintWriter.close();

		Log.e(TAG, mStringWriter.toString());
		return mStringWriter.toString();
	}

	private void sendInfoToHost(Context context, Throwable ex) {
		final String errorContent = obtainExceptionInfo(ex);
		final String errorTime = paserTime(System.currentTimeMillis());
		final String versionName = obtainSimpleInfo(context).get("versionName");
		final String versionCode = obtainSimpleInfo(context).get("versionCode");
		DebugUtility.showLog("Start####错误日志####");
		DebugUtility.showLog("####errorContent####" + errorContent);
		DebugUtility.showLog("####errorTime####" + errorTime);
		DebugUtility.showLog("####phoneModel####" + android.os.Build.MODEL);
		DebugUtility.showLog("####osType####" + android.os.Build.VERSION.SDK);
		DebugUtility.showLog("####versionName####" + versionName);
		DebugUtility.showLog("####versionCode####" + versionCode);
		DebugUtility.showLog("####错误日志####End");
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		String httpurl = Config.BASE_URL + "/api/rest/admin/postErrorLog";
		DebugUtility.showLog("请求URL:" + httpurl);
		if (NetManager.isNetworkConnected(context)) {
			StringRequest request = new StringRequest(Request.Method.POST,
					httpurl, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							parseRespense(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							DebugUtility.showLog("发送错误日志失败："
									+ error.getMessage());
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					// TODO Auto-generated method stub
					Map<String, String> params = new HashMap<String, String>();
					params.put("appType", "1");
					params.put("hardId", MyApplication.instance.getHardId());
					params.put("errorContent", errorContent);
					params.put("errorTime", errorTime);
					params.put("phoneModel", android.os.Build.MODEL);
					params.put("osType", android.os.Build.VERSION.SDK);
					params.put("versionName", versionName);
					params.put("versionCode", versionCode);
					return params;
				}
			};
			requestQueue.add(request);
		} else {

		}
	}

	void parseRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				DebugUtility.showLog("发送错误日志成功");
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String savaInfoToSD(Context context, Throwable ex) {
		String fileName = null;
		StringBuffer sb = new StringBuffer();

		for (Map.Entry<String, String> entry : obtainSimpleInfo(context)
				.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append(" = ").append(value).append("\n");
		}

		sb.append(obtainExceptionInfo(ex));

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(SDCARD_ROOT + File.separator + "crash"
					+ File.separator);
			if (!dir.exists()) {
				dir.mkdir();
			}

			try {
				fileName = dir.toString() + File.separator
						+ paserTime(System.currentTimeMillis()) + ".log";
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return fileName;

	}

	private String paserTime(long milliseconds) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String times = format.format(new Date(milliseconds));

		return times;
	}
}
