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
	 * �������͵�ʱ����
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
				mTimerTask.cancel(); // ��ԭ����Ӷ������Ƴ�
			}
			mTimerTask = new MyTimerTask(); // �½�һ������
			mTimer.schedule(mTimerTask, 1000, PUSHTIME);// 10S����������
		}
	}

	@Override
	public void onCreate() {
		mTimer = new Timer(true);
	}

	/**
	 * �½�֪ͨ
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
		Notification notification = new Notification(icon, null, when);// ��һ������Ϊͼ��,�ڶ�������Ϊ����,������Ϊ֪ͨʱ��
		// ���notification֮�󣬸�notification�Զ���ʧ
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent openintent = null;
		openintent = new Intent(this, MessageCenterActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				openintent, PendingIntent.FLAG_UPDATE_CURRENT);// �������Ϣʱ�ͻ���ϵͳ����openintent��ͼ
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
			// ��������
			if (!MyApplication.instance.userInfoSp.getAll().isEmpty()) {
				httpGetJson();
			}
		}
	}

	private void httpGetJson() {
		if (PreferenceManager.getString(this, "tag") == null
				|| PreferenceManager.getString(this, "tag").trim().equals("")) {
			tag = System.currentTimeMillis() + "";
			DebugUtility.showLog("ʱ���Ϊ��ǰʱ��:" + tag);
		} else {
			tag = PreferenceManager.getString(this, "tag").toString();
			DebugUtility.showLog("ʱ���Ϊ�ϴα���ʱ��:" + tag);
		}
		page = 1;
		pageSize = 10;
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(this);
		String url = Config.MY_MESSAGE_URL + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId") + "/" + userInfoSp.get("userId")
				+ "/" + tag + "/" + page + "/" + pageSize;
		DebugUtility.showLog("����URL:" + url);
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
							Toast.makeText(PushService.this, "��ȡ����ʧ��", 1 * 1000).show();
							DebugUtility.showLog("��ȡ�ҵ���Ϣʧ�ܣ�"
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
			Toast.makeText(this, "���粻���ã�������������", 1 * 1000).show();
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
				DebugUtility.showLog("��ȡ��Ϣ��" + response);
				String tag = JsonManager.getJsonItem(jsonObj, "tag").toString();
				// ���µ�����ʱ������tag
				PreferenceManager.setString(this, "tag", tag);
				String pushinfo = JsonManager.getJsonItem(jsonObj, "pushList")
						.toString();
				// ��Ϣ���ݼ���
				List<PushInfo> pushinfos = JsonManager.getListFromJSON(
						pushinfo, PushInfo.class);
				if (userInfoSp.get("userId").toString() != null
						&& !userInfoSp.get("userId").toString().equals("")) {
					// ����Ϣ���ݲ������ݿ�
					DBManager.getInstance(this).inseartMessageList(
							userInfoSp.get("userId").toString(), pushinfos);
					// �����ݿ���»�����������һ��
					if (pushinfos.size() > 0) {
						showNotifycation("ȫ���˶�", "��ã���" + pushinfos.size()
								+ "������Ϣ.");
					}
				}

			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "��ȡ��Ϣ�쳣");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "δ��½���½��ʱ�������µ�½");
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
