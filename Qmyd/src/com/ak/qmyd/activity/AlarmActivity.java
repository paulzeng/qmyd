package com.ak.qmyd.activity;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.ak.qmyd.dialog.AlarmDialog;
import com.ak.qmyd.dialog.AlarmDialog.OnActionSheetSelected;
import com.ak.qmyd.receiver.AlarmReceiver;
import com.ak.qmyd.tools.DebugUtility;

/**
 * @author JGB
 * @date 2015-6-1 上午9:57:02
 */
public class AlarmActivity extends BaseFragmentActivity implements
		OnClickListener {
	private TextView tv_alarm_cancel, tv_alarm_title, tv_alarm_time,
			tv_alarm_day;
	private MediaPlayer mMediaPlayer;
	private Vibrator vibrator;
	private KeyguardLock keyguard;
	private int fiveMinutes = 5 * 60;// 5分钟 按秒算
	private int tenMinutes = 10 * 60;// 10分钟
	private int halfHour = 30 * 60;// 半小时
	private int Hour24 = 24 * 60 * 60;// 24小时
	private boolean isAlarmRing;
	private boolean isStart = true;
	private String mytime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_dialog);
		findView();
		initView();
		mytime = getIntent().getStringExtra("time");
		startMySelfAlarm();
		startAlarm();
		
		DebugUtility.showLog("alarm现在的时间" + mytime);
		DebugUtility.showLog("alarm刚开始的具体时间："
				+ getIntent().getStringExtra("currentTotalTime"));
		DebugUtility.showLog("alarm当前是：" + getCurrentWeek());
		DebugUtility.showLog("alarm当前重复类型："
				+ getIntent().getStringExtra("timetype"));
	}

	private void startMySelfAlarm() {
		if (getIntent().getStringExtra("timetype").equals("不重复")) {
			startMyAlarm();
		} else if (getIntent().getStringExtra("timetype").equals("每天")) {
			isAlarmRing = false;
			startMyAlarm();
			if (isStart) {
				Hour24Handler.sendEmptyMessage(0);
			}
		} else {
			if (getIntent().getStringExtra("timetype").contains("周一")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(2,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("周二")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(3,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
//					 registAlarmBrodcast(3,20,55, 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("周三")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(4,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}

			if (getIntent().getStringExtra("timetype").contains("周四")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(5,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("周五")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(6,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("周六")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(7,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("周日")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(1,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
		}
	}

	private void registAlarmBrodcast(int week, int hour, int minute, int sec,
			int millis) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.DAY_OF_WEEK, week);// 周几
		calendar.set(Calendar.HOUR_OF_DAY, hour);// 小时
		calendar.set(Calendar.MINUTE, minute);// 分钟
		calendar.set(Calendar.SECOND, sec);// 秒
		calendar.set(Calendar.MILLISECOND, millis);// 毫秒
		Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
		intent.setAction("miniAlarm");
		intent.putExtra("id", getIntent().getStringExtra("id"));
		intent.putExtra("title", getIntent().getStringExtra("title"));
		intent.putExtra("time", getIntent().getStringExtra("time"));
		intent.putExtra("timetype", getIntent().getStringExtra("timetype"));
		intent.putExtra("day", getIntent().getStringExtra("day"));
		intent.putExtra("way", getIntent().getStringExtra("way"));
		intent.putExtra("count", getIntent().getStringExtra("count"));
		intent.putExtra("currentTotalTime",
				getIntent().getStringExtra("currentTotalTime"));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);
	}

	private void startMyAlarm() {
		startAlarmCount();
		stopKeyguardLock();
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

	private void startAlarmShake() {
		try {
			vibrator = (Vibrator) getApplicationContext().getSystemService(
					Context.VIBRATOR_SERVICE);
			vibrator.vibrate(new long[] { 1000, 2000, 3000, 4000 }, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void initView() {
		tv_alarm_cancel.setOnClickListener(this);
		tv_alarm_title.setText(getIntent().getStringExtra("title"));
		tv_alarm_time.setText(getIntent().getStringExtra("time"));
		tv_alarm_day.setText(getIntent().getStringExtra("day"));
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
	private Handler Hour24Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Hour24--;
			if (Hour24 > 0) {
				Hour24Handler.sendEmptyMessageDelayed(0, 1000);
			} else {
				startMyAlarm();// 开启我的闹钟
				Hour24 = 24 * 60 * 60;
				Hour24Handler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};

	private void findView() {
		tv_alarm_cancel = (TextView) findViewById(R.id.tv_alarm_cancel);
		tv_alarm_title = (TextView) findViewById(R.id.tv_alarm_title);
		tv_alarm_time = (TextView) findViewById(R.id.tv_alarm_time);
		tv_alarm_day = (TextView) findViewById(R.id.tv_alarm_day);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_alarm_cancel:
			if (getIntent().getStringExtra("timetype").equals("不重复") && getIntent().getStringExtra("count").equals("一次")) {
				isStart = false;
				showToast("闹钟已关闭");
			} else {
				isStart = true;
				showToast("本次闹钟已关闭");
			}
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

	private String getCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mweek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mweek)) {
			return "周日";
		} else if ("2".equals(mweek)) {
			return "周一";
		} else if ("3".equals(mweek)) {
			return "周二";
		} else if ("4".equals(mweek)) {
			return "周三";
		} else if ("5".equals(mweek)) {
			return "周四";
		} else if ("6".equals(mweek)) {
			return "周五";
		} else if ("7".equals(mweek)) {
			return "周六";
		}
		return mweek;
	}
}
