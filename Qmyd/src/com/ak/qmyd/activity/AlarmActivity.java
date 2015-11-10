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
 * @date 2015-6-1 ����9:57:02
 */
public class AlarmActivity extends BaseFragmentActivity implements
		OnClickListener {
	private TextView tv_alarm_cancel, tv_alarm_title, tv_alarm_time,
			tv_alarm_day;
	private MediaPlayer mMediaPlayer;
	private Vibrator vibrator;
	private KeyguardLock keyguard;
	private int fiveMinutes = 5 * 60;// 5���� ������
	private int tenMinutes = 10 * 60;// 10����
	private int halfHour = 30 * 60;// ��Сʱ
	private int Hour24 = 24 * 60 * 60;// 24Сʱ
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
		
		DebugUtility.showLog("alarm���ڵ�ʱ��" + mytime);
		DebugUtility.showLog("alarm�տ�ʼ�ľ���ʱ�䣺"
				+ getIntent().getStringExtra("currentTotalTime"));
		DebugUtility.showLog("alarm��ǰ�ǣ�" + getCurrentWeek());
		DebugUtility.showLog("alarm��ǰ�ظ����ͣ�"
				+ getIntent().getStringExtra("timetype"));
	}

	private void startMySelfAlarm() {
		if (getIntent().getStringExtra("timetype").equals("���ظ�")) {
			startMyAlarm();
		} else if (getIntent().getStringExtra("timetype").equals("ÿ��")) {
			isAlarmRing = false;
			startMyAlarm();
			if (isStart) {
				Hour24Handler.sendEmptyMessage(0);
			}
		} else {
			if (getIntent().getStringExtra("timetype").contains("��һ")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(2,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("�ܶ�")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(3,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
//					 registAlarmBrodcast(3,20,55, 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("����")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(4,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}

			if (getIntent().getStringExtra("timetype").contains("����")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(5,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("����")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(6,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("����")) {
				isAlarmRing = false;
				startMyAlarm();
				if (isStart) {
					registAlarmBrodcast(7,
							Integer.parseInt(mytime.substring(0, 2)),
							Integer.parseInt(mytime.substring(3, 5)), 0, 0);
				}
			}
			if (getIntent().getStringExtra("timetype").contains("����")) {
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
		calendar.set(Calendar.DAY_OF_WEEK, week);// �ܼ�
		calendar.set(Calendar.HOUR_OF_DAY, hour);// Сʱ
		calendar.set(Calendar.MINUTE, minute);// ����
		calendar.set(Calendar.SECOND, sec);// ��
		calendar.set(Calendar.MILLISECOND, millis);// ����
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
		if (way.equals("����")) {
			startAlarmRing();// ��������
		} else if (way.equals("��")) {
			startAlarmShake();// ������
		} else if (way.equals("������") || way.equals("������")) {
			startAlarmRing();// ��������
			startAlarmShake();// ������
		}
	}

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
	private Handler Hour24Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Hour24--;
			if (Hour24 > 0) {
				Hour24Handler.sendEmptyMessageDelayed(0, 1000);
			} else {
				startMyAlarm();// �����ҵ�����
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
			if (getIntent().getStringExtra("timetype").equals("���ظ�") && getIntent().getStringExtra("count").equals("һ��")) {
				isStart = false;
				showToast("�����ѹر�");
			} else {
				isStart = true;
				showToast("���������ѹر�");
			}
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
			return "����";
		} else if ("2".equals(mweek)) {
			return "��һ";
		} else if ("3".equals(mweek)) {
			return "�ܶ�";
		} else if ("4".equals(mweek)) {
			return "����";
		} else if ("5".equals(mweek)) {
			return "����";
		} else if ("6".equals(mweek)) {
			return "����";
		} else if ("7".equals(mweek)) {
			return "����";
		}
		return mweek;
	}
}
