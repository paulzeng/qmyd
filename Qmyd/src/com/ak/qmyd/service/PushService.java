package com.ak.qmyd.service;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.MessageCenterActivity;
import com.ak.qmyd.bean.PushInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.PreferenceManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PushService extends Service {
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	/**
	 * 请求推送的时间间隔
	 */
	public static final int PUSHTIME = 1000 * 60 * 1;

	private Map<String, ?> userInfoSp;
	String tag;
	int page;
	int pageSize;
	// List<PushInfo> mDate = new ArrayList<PushInfo>();
	private RequestQueue mRequestQueue;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		DebugUtility.showLog("My Service Start");
		if (mTimer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel(); // 将原任务从队列中移除
			}
			mTimerTask = new MyTimerTask(); // 新建一个任务
			mTimer.schedule(mTimerTask, 1000, PUSHTIME);// 10S后启动任务
		}
	}

	@Override
	public void onCreate() {
		mTimer = new Timer(true);
	}

	/**
	 * 新建通知
	 * 
	 * @param title
	 * @param content
	 * @param id
	 */
	@SuppressWarnings("deprecation")
	void showNotifycation(String title, String content) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, null, when);// 第一个参数为图标,第二个参数为标题,第三个为通知时间
		// 点击notification之后，该notification自动消失
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent openintent = null;
		openintent = new Intent(this, MessageCenterActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				openintent, PendingIntent.FLAG_UPDATE_CURRENT);// 当点击消息时就会向系统发送openintent意图
		notification.setLatestEventInfo(this, title, content, contentIntent);
		mNotificationManager.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		DebugUtility.showLog("My Service Stoped");
		mTimer.cancel();
		mTimerTask.cancel();
		stopSelf();
	}

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 发送请求
			if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
				httpGetJson();
			}
		}
	}

	private void httpGetJson() {
		if (PreferenceManager.getString(this, "tag") == null
				|| PreferenceManager.getString(this, "tag").trim().equals("")) {
			tag = System.currentTimeMillis() + "";
			DebugUtility.showLog("时间戳为当前时间:" + tag);
		} else {
			tag = PreferenceManager.getString(this, "tag").toString();
			DebugUtility.showLog("时间戳为上次保存时间:" + tag);
		}
		page = 1;
		pageSize = 10;
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(this);
		String url = Config.MY_MESSAGE_URL + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId") + "/" + userInfoSp.get("userId")
				+ "/" + tag + "/" + page + "/" + pageSize;
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest request = new StringRequest(Request.Method.GET, url,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							parseRespense(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(PushService.this, "获取数据失败", 1 * 1000).show();
							DebugUtility.showLog("获取我的消息失败："
									+ error.getMessage());
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					// TODO Auto-generated method stub
					return super.getParams();
				}
			};
			mRequestQueue.add(request);
		} else {
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
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
				DebugUtility.showLog("获取消息：" + response);
				String tag = JsonManager.getJsonItem(jsonObj, "tag").toString();
				// 有新的数据时，更新tag
				PreferenceManager.setString(this, "tag", tag);
				String pushinfo = JsonManager.getJsonItem(jsonObj, "pushList")
						.toString();
				// 消息内容集合
				List<PushInfo> pushinfos = JsonManager.getListFromJSON(
						pushinfo, PushInfo.class);
				if (userInfoSp.get("userId").toString() != null
						&& !userInfoSp.get("userId").toString().equals("")) {
					// 将消息内容插入数据库
					DBManager.getInstance(this).inseartMessageList(
							userInfoSp.get("userId").toString(), pushinfos);
					// 将数据库和新获得数据组合在一起
					if (pushinfos.size() > 0) {
						showNotifycation("全民运动", "你好，有" + pushinfos.size()
								+ "条新消息.");
					}
				}

			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
				// Intent intent = new Intent();
				// intent.setAction("com.ak.qmyd.pushservice");
				// stopService(intent);
				stopSelf();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
