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
 * @date 2015-6-2 下午6:35:53
 */
public class AlarmMiniActivity extends BaseFragmentActivity implements OnClickListener{
	
	private TextView tv_alarm_cancel, tv_alarm_title, tv_alarm_time,
	tv_alarm_day;
	private int time;
	private String mytime;
	private MediaPlayer mMediaPlayer;
	private boolean isAlarmRing;
	private boolean isStart = true;
	private int fiveMinutes = 5 * 60;// 5分钟 按秒算
	private int tenMinutes = 10 * 60;// 10分钟
	private int halfHour = 30 * 60;// 半小时
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
		
		DebugUtility.showLog("alarmMini刚开始的具体时间："
				+ getIntent().getStringExtra("currentTotalTime"));
		DebugUtility
				.showLog("alarmMini当前重复类型：" + getIntent().getStringExtra("timetype"));
		DebugUtility.showLog("alarmMini得到的当前时间的秒数：" + time);
	}
	
	
	private void startAlarm() {
		String way = getIntent().getStringExtra("way");
		if (way.equals("铃声")) {
			startAlarmRing();// 开启响铃
		} else if (way.equals("震动")) {
			startAlarmShake();// 开启震动
		} else if (way.equals("铃声震动") || way.equals("震动铃声")) {
			startAlarmRing();// 开启响铃
			startAlarmShake();// 开启震动
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
		if (count.equals("一次")) {
			isAlarmRing = false;
		} else if (count.equals("五分钟")) {
			isAlarmRing = false;
			if (isStart) {
				fiveMinuteHandler.sendEmptyMessage(0);
			}
		} else if (count.equals("十分钟")) {
			isAlarmRing = false;
			if (isStart) {
				tenMinuteHandler.sendEmptyMessage(0);
			}
		} else if (count.equals("半小时")) {
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
				startAlarm();// 开启闹钟
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
				startAlarm();// 开启闹钟
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
				startAlarm();// 开启闹钟
				fiveMinutes = 5 * 60;
				fiveMinuteHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private void stopKeyguardLock() {
		KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		if (manager.inKeyguardRestrictedInputMode()) {
			// 处于锁定界面,界面则通过KeyguardLock类方法来解锁
			keyguard = manager.newKeyguardLock(getLocalClassName());
			keyguard.disableKeyguard();
		}
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 获取电源管理器对象
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		wl.acquire();
		// 点亮屏幕

		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		// 得到键盘锁管理器对象
		KeyguardLock kl = km.newKeyguardLock("unLock");
		// 参数是LogCat里用的Tag
		kl.disableKeyguard();
		// 解锁
		kl.reenableKeyguard();
		// 重新启用自动加锁
		wl.release();
		// 释放
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
			showToast("本次指定闹钟已取消");
			finish();
			cancelAlarmRing();// 取消响铃
			cancelAlarmShake();// 取消震动
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
