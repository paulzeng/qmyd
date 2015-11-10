package com.ak.qmyd.activity;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.DebugUtility;

/** 
 * @author JGB
 * @date 2015-6-2 ����6:35:53
 */
public class AlarmMiniActivity extends BaseFragmentActivity implements OnClickListener{
	
	private TextView tv_alarm_cancel, tv_alarm_title, tv_alarm_time,
	tv_alarm_day;
	private int time;
	private String mytime;
	private MediaPlayer mMediaPlayer;
	private boolean isAlarmRing;
	private boolean isStart = true;
	private int fiveMinutes = 5 * 60;// 5���� ������
	private int tenMinutes = 10 * 60;// 10����
	private int halfHour = 30 * 60;// ��Сʱ
	private Vibrator vibrator;
	private KeyguardLock keyguard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_dialog);
		findView();
		initView();
		startMySelfAlarm();
		startAlarm();
		mytime = getIntent().getStringExtra("time");
		
		DebugUtility.showLog("alarmMini�տ�ʼ�ľ���ʱ�䣺"
				+ getIntent().getStringExtra("currentTotalTime"));
		DebugUtility
				.showLog("alarmMini��ǰ�ظ����ͣ�" + getIntent().getStringExtra("timetype"));
		DebugUtility.showLog("alarmMini�õ��ĵ�ǰʱ���������" + time);
	}
	
	
	private void startAlarm() {
		String way = getIntent().getStringExtra("way");
		if (way.equals("����")) {
			startAlarmRing();// ��������
		} else if (way.equals("��")) {
			startAlarmShake();// ������
		} else if (way.equals("������") || way.equals("������")) {
			startAlarmRing();// ��������
			startAlarmShake();// ������
		}
	}


	private void startAlarmShake() {
		try {
			vibrator = (Vibrator) getApplicationContext().getSystemService(
					Context.VIBRATOR_SERVICE);
			vibrator.vibrate(new long[] { 1000, 2000, 3000, 4000 }, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startMySelfAlarm() {
		isAlarmRing = false;
		startMyAlarm();
	}


	private void startMyAlarm() {
		startAlarmCount();
		stopKeyguardLock();
	}
	
	
	private void startAlarmCount() {
		String count = getIntent().getStringExtra("count");
		if (count.equals("һ��")) {
			isAlarmRing = false;
		} else if (count.equals("�����")) {
			isAlarmRing = false;
			if (isStart) {
				fiveMinuteHandler.sendEmptyMessage(0);
			}
		} else if (count.equals("ʮ����")) {
			isAlarmRing = false;
			if (isStart) {
				tenMinuteHandler.sendEmptyMessage(0);
			}
		} else if (count.equals("��Сʱ")) {
			isAlarmRing = false;
			if (isStart) {
				halfHourHandler.sendEmptyMessage(0);
			}
		}
	}
	
	private Handler halfHourHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			halfHour--;
			if (halfHour > 0) {
				halfHourHandler.sendEmptyMessageDelayed(0, 1000);
			} else {
				stopKeyguardLock();
				startAlarm();// ��������
				halfHour = 30 * 60;
				halfHourHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private Handler tenMinuteHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tenMinutes--;
			if (tenMinutes > 0) {
				tenMinuteHandler.sendEmptyMessageDelayed(0, 1000);
			} else {
				stopKeyguardLock();
				startAlarm();// ��������
				tenMinutes = 10 * 60;
				tenMinuteHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private Handler fiveMinuteHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			fiveMinutes--;
			if (fiveMinutes > 0) {
				fiveMinuteHandler.sendEmptyMessageDelayed(0, 1000);
			} else {
				stopKeyguardLock();
				startAlarm();// ��������
				fiveMinutes = 5 * 60;
				fiveMinuteHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private void stopKeyguardLock() {
		KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		if (manager.inKeyguardRestrictedInputMode()) {
			// ������������,������ͨ��KeyguardLock�෽��������
			keyguard = manager.newKeyguardLock(getLocalClassName());
			keyguard.disableKeyguard();
		}
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// ��ȡ��Դ����������
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
		// ��ȡPowerManager.WakeLock����,����Ĳ���|��ʾͬʱ��������ֵ,������LogCat���õ�Tag
		wl.acquire();
		// ������Ļ

		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		// �õ�����������������
		KeyguardLock kl = km.newKeyguardLock("unLock");
		// ������LogCat���õ�Tag
		kl.disableKeyguard();
		// ����
		kl.reenableKeyguard();
		// ���������Զ�����
		wl.release();
		// �ͷ�
	}
	
	private void initView() {
		tv_alarm_cancel.setOnClickListener(this);
		tv_alarm_title.setText(getIntent().getStringExtra("title"));
		tv_alarm_time.setText(getIntent().getStringExtra("time"));
		tv_alarm_day.setText(getIntent().getStringExtra("day"));
	}


	private void findView() {
		tv_alarm_cancel = (TextView) findViewById(R.id.tv_alarm_cancel);
		tv_alarm_title = (TextView) findViewById(R.id.tv_alarm_title);
		tv_alarm_time = (TextView) findViewById(R.id.tv_alarm_time);
		tv_alarm_day = (TextView) findViewById(R.id.tv_alarm_day);
	}

	private void startAlarmRing() {
		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(getApplicationContext(), alert);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			mMediaPlayer.setLooping(isAlarmRing);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_alarm_cancel:
			showToast("����ָ��������ȡ��");
			finish();
			cancelAlarmRing();// ȡ������
			cancelAlarmShake();// ȡ����
			DBManager.getInstance(getApplicationContext()).upDateNoteById(
					Integer.parseInt(getIntent().getStringExtra("id")),
					isStart + "");
			break;

		default:
			break;
		}
	}
	
	private void cancelAlarmRing() {
		try {
			if (this.mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					this.mMediaPlayer.stop();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cancelAlarmShake() {
		try {
			if (null != vibrator) {
				vibrator.cancel();
				vibrator = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
