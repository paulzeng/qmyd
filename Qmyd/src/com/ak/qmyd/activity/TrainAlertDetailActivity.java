package com.ak.qmyd.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.NoteBean;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.view.PickerView;
import com.ak.qmyd.view.PickerView.onSelectListener;

public class TrainAlertDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	protected static final int TRAIN_ALERT_HOUR = 0;
	protected static final int TRAIN_ALERT_MINUTE = 1;
	private ImageButton ib_movement_alert_return;
	private TextView tv_movement_alert_save, tv_movement_alert_day,
			tv_movement_alert_week, tv_movement_alert_timetype,
			tv_movement_alert_count, tv_movement_alert_way;
	private int currentHour, currentMinute;
	private boolean typeA = false;
	private boolean typeB = false;
	private PickerView pv_movement_alert_minute, pv_movement_alert_hour;
	private Button bt_movement_alert_btn1, bt_movement_alert_btn2,
			bt_movement_alert_btn3, bt_movement_alert_btn4,
			bt_movement_alert_btn5;
	private RelativeLayout rl_movement_alert_type, rl_movement_alert_count,
			rl_movement_alert_way;
	private DrawerLayout mDrawerLayout;
	private String year = null;
	private String month = null;
	private String day = null;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TRAIN_ALERT_HOUR:
				currentHour = (Integer.parseInt((String) msg.obj));
				typeA = true;
				break;
			case TRAIN_ALERT_MINUTE:
				currentMinute = (Integer.parseInt((String) msg.obj));
				typeB = true;
				break;

			default:
				break;
			}
		};
	};
	private int hour;
	private int minute;
	private String timetype;
	private String count;
	private String way;
	private static final int NOTE_EDIT = 0; // 编辑状态
	private static final int NOTE_INSERT = 1; // 插入状态
	private int state;

	private String time;
	private String index = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movement_alert);
		findView();
		initPickerView();
		initView();
		sp = getSharedPreferences("config", 0);
		editor = sp.edit();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_train_alert_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		Intent intent = getIntent();
		if (Intent.ACTION_INSERT == intent.getAction()) {
			hour = Integer.parseInt(new SimpleDateFormat("HH", Locale
					.getDefault()).format(new Date()));
			minute = (Integer.parseInt(new SimpleDateFormat("mm", Locale
					.getDefault()).format(new Date())));
			DebugUtility.showLog("当前分钟时间："
					+ minute
					+ "...."
					+ Integer.parseInt(new SimpleDateFormat("mm", Locale
							.getDefault()).format(new Date())));
			index = "1";
			if (getIntent().getStringExtra("type").equals("1")) {
				tv_movement_alert_day.setText(new SimpleDateFormat(
						"yyyy年MM月dd日", Locale.getDefault()).format(new Date()));
				Calendar calendar = Calendar.getInstance();
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			} else if (getIntent().getStringExtra("type").equals("2")) {
				tv_movement_alert_day
						.setText(getIntent().getStringExtra("day"));
				tv_movement_alert_week.setText(getIntent().getStringExtra(
						"week"));
			}
			state = NOTE_INSERT;
			initTime();
			String myTime = (getIntent().getStringExtra("mytime") == null ? (hour
					+ ":" + minute)
					: getIntent().getStringExtra("mytime"));
			DebugUtility.showLog("当前时间：" + myTime);
			pv_movement_alert_hour.setSelected(Integer.parseInt(myTime
					.split(":")[0]));
			pv_movement_alert_minute.setSelected(Integer.parseInt(myTime
					.split(":")[1]));
			timetype = getSharedPreferences("config", 0).getString("data",
					"不重复");
			// count = getSharedPreferences("config", 0).getString("count",
			// "一次");
			way = getSharedPreferences("config", 0).getString("way", "铃声");
			tv_movement_alert_way
					.setText(getIntent().getStringExtra("way") == null ? way
							: getIntent().getStringExtra("way"));
			tv_movement_alert_timetype.setText(getIntent().getStringExtra(
					"timetype") == null ? timetype : getIntent()
					.getStringExtra("timetype"));
			// tv_movement_alert_count.setText(getIntent().getStringExtra("count")
			// == null ? count : getIntent().getStringExtra("count"));
		} else if (Intent.ACTION_EDIT == intent.getAction()) {
			state = NOTE_EDIT;
			initTime();
			Calendar calendar = Calendar.getInstance();
			tv_movement_alert_week.setText(getWeek((calendar
					.get(Calendar.DAY_OF_WEEK)) % 7));
			if (getIntent() != null) {
				timetype = getIntent().getStringExtra("timetype");
//				count = getIntent().getStringExtra("count");
				way = getIntent().getStringExtra("way");
				tv_movement_alert_week.setText(getIntent().getStringExtra(
						"week"));
				tv_movement_alert_day
						.setText(getIntent().getStringExtra("day"));
				tv_movement_alert_way
						.setText(getIntent().getStringExtra("way"));
				tv_movement_alert_timetype.setText(getIntent().getStringExtra(
						"timetype"));
//				tv_movement_alert_count.setText(getIntent().getStringExtra(
//						"count"));
				String myTime = (getIntent().getStringExtra("mytime") == null ? getIntent()
						.getStringExtra("time") : getIntent().getStringExtra(
						"mytime"));
				pv_movement_alert_hour.setSelected(Integer.parseInt(myTime
						.substring(0, 2)));
				pv_movement_alert_minute.setSelected(Integer.parseInt(myTime
						.substring(3, 5)));
			}
		}
		setBtnBg();
		getIndex();
	}

	private void getIndex() {
		if (bt_movement_alert_btn1.isSelected()) {
			index = "1";
		}
		if (bt_movement_alert_btn2.isSelected()) {
			index = "2";
		}
		if (bt_movement_alert_btn3.isSelected()) {
			index = "3";
		}
		if (bt_movement_alert_btn4.isSelected()) {
			index = "4";
		}
		if (bt_movement_alert_btn5.isSelected()) {
			index = "5";
		}
	}

	private void setBtnBg() {
		if (getIntent().getStringExtra("index").equals("1")) {
			bt_movement_alert_btn1.setSelected(true);
			bt_movement_alert_btn1.setPressed(true);
			bt_movement_alert_btn1.setEnabled(false);
		}
		if (getIntent().getStringExtra("index").equals("2")) {
			bt_movement_alert_btn2.setSelected(true);
			bt_movement_alert_btn2.setPressed(true);
			bt_movement_alert_btn2.setEnabled(false);
		}

		if (getIntent().getStringExtra("index").equals("3")) {
			bt_movement_alert_btn3.setSelected(true);
			bt_movement_alert_btn3.setPressed(true);
			bt_movement_alert_btn3.setEnabled(false);
		}
		if (getIntent().getStringExtra("index").equals("4")) {
			bt_movement_alert_btn4.setSelected(true);
			bt_movement_alert_btn4.setPressed(true);
			bt_movement_alert_btn4.setEnabled(false);
		}
		if (getIntent().getStringExtra("index").equals("5")) {
			bt_movement_alert_btn5.setSelected(true);
			bt_movement_alert_btn5.setPressed(true);
			bt_movement_alert_btn5.setEnabled(false);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

	private void initTime() {
		bt_movement_alert_btn1.setText(new SimpleDateFormat("dd", Locale
				.getDefault()).format(new Date()));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		bt_movement_alert_btn2.setText(new SimpleDateFormat("dd")
				.format(calendar.getTime()));
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		bt_movement_alert_btn3.setText(new SimpleDateFormat("dd")
				.format(calendar.getTime()));
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		bt_movement_alert_btn4.setText(new SimpleDateFormat("dd")
				.format(calendar.getTime()));
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		bt_movement_alert_btn5.setText(new SimpleDateFormat("dd")
				.format(calendar.getTime()));
	}

	private void initData() {
		String newDay = (day == null ? (new SimpleDateFormat("dd",
				Locale.getDefault()).format(new Date())) : day)
				+ "日";
		String newMonth = (month == null ? (new SimpleDateFormat("MM",
				Locale.getDefault()).format(new Date())) : month)
				+ "月";
		String newYear = (year == null ? (new SimpleDateFormat("yyyy",
				Locale.getDefault()).format(new Date())) : year)
				+ "年";
		tv_movement_alert_day.setText(newYear + newMonth + newDay);
	}

	private String getWeek(int week) {
		if (week == 2) {
			return "星期一";
		} else if (week == 3) {
			return "星期二";
		} else if (week == 4) {
			return "星期三";
		} else if (week == 5) {
			return "星期四";
		} else if (week == 6) {
			return "星期五";
		} else if (week == 0) {
			return "星期六";
		} else if (week == 1) {
			return "星期日";
		}
		return null;
	}

	private void initPickerView() {
		List<String> data = new ArrayList<String>();
		List<String> seconds = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			data.add(i < 10 ? "0" + i : "" + i);
		}
		for (int i = 0; i < 60; i++) {
			seconds.add(i < 10 ? "0" + i : "" + i);
		}
		pv_movement_alert_hour.setData(data);

		pv_movement_alert_hour.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				sendMyMessage(text, TRAIN_ALERT_HOUR);
			}

		});
		pv_movement_alert_minute.setData(seconds);

		pv_movement_alert_minute.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				sendMyMessage(text, TRAIN_ALERT_MINUTE);
			}
		});

	}

	private void sendMyMessage(String text, int what) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = text;
		mHandler.sendMessage(msg);
	}

	private void initView() {
		ib_movement_alert_return.setOnClickListener(this);
		tv_movement_alert_save.setOnClickListener(this);
		rl_movement_alert_type.setOnClickListener(this);
//		rl_movement_alert_count.setOnClickListener(this);
		rl_movement_alert_way.setOnClickListener(this);
		bt_movement_alert_btn1.setOnClickListener(this);
		bt_movement_alert_btn2.setOnClickListener(this);
		bt_movement_alert_btn3.setOnClickListener(this);
		bt_movement_alert_btn4.setOnClickListener(this);
		bt_movement_alert_btn5.setOnClickListener(this);
	}

	private void findView() {
		ib_movement_alert_return = (ImageButton) findViewById(R.id.ib_movement_alert_return);
		tv_movement_alert_save = (TextView) findViewById(R.id.tv_movement_alert_save);
		pv_movement_alert_minute = (PickerView) findViewById(R.id.pv_movement_alert_minute);
		pv_movement_alert_hour = (PickerView) findViewById(R.id.pv_movement_alert_hour);

		bt_movement_alert_btn1 = (Button) findViewById(R.id.bt_movement_alert_btn1);
		bt_movement_alert_btn2 = (Button) findViewById(R.id.bt_movement_alert_btn2);
		bt_movement_alert_btn3 = (Button) findViewById(R.id.bt_movement_alert_btn3);
		bt_movement_alert_btn4 = (Button) findViewById(R.id.bt_movement_alert_btn4);
		bt_movement_alert_btn5 = (Button) findViewById(R.id.bt_movement_alert_btn5);
		rl_movement_alert_type = (RelativeLayout) findViewById(R.id.rl_movement_alert_type);
//		rl_movement_alert_count = (RelativeLayout) findViewById(R.id.rl_movement_alert_count);
		rl_movement_alert_way = (RelativeLayout) findViewById(R.id.rl_movement_alert_way);
		tv_movement_alert_day = (TextView) findViewById(R.id.tv_movement_alert_day);
		tv_movement_alert_week = (TextView) findViewById(R.id.tv_movement_alert_week);
		tv_movement_alert_timetype = (TextView) findViewById(R.id.tv_movement_alert_timetype);
//		tv_movement_alert_count = (TextView) findViewById(R.id.tv_movement_alert_count);
		tv_movement_alert_way = (TextView) findViewById(R.id.tv_movement_alert_way);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_movement_alert_return:
			editor.putString("data", "不重复");
//			editor.putString("count", "一次");
			editor.putString("way", "铃声");
			editor.commit();
			finish();
			break;
		case R.id.tv_movement_alert_save:
			editor.putString("data", "不重复");
//			editor.putString("count", "一次");
			editor.putString("way", "铃声");
			editor.commit();
			// if(tv_movement_alert_timetype.getText().equals("每天")){
			// newDay = (String) tv_movement_alert_timetype.getText();
			// }else if(!tv_movement_alert_timetype.getText().equals("每天")){
			// newDay = (String) tv_movement_alert_day.getText();
			// }
			if (NOTE_INSERT == state) {
				String myTimeHour = (getIntent().getStringExtra("mytime") == null ? (hour + "")
						: (getIntent().getStringExtra("mytime"))
								.substring(0, 2));
				String myTimeMinute = (getIntent().getStringExtra("mytime") == null ? (minute + "")
						: (getIntent().getStringExtra("mytime"))
								.substring(3, 5));
				if (!typeA && !typeB) {
					showToast("保存成功!保存时间为" + dataString()
							+ formatTime(Integer.parseInt(myTimeHour)) + "时"
							+ formatTime(Integer.parseInt(myTimeMinute)) + "分");
					time = formatTime(Integer.parseInt(myTimeHour)) + ":"
							+ formatTime(Integer.parseInt(myTimeMinute));
				} else if (typeA && !typeB) {
					showToast("保存成功!保存时间为" + dataString()
							+ formatTime(currentHour) + "时"
							+ formatTime(Integer.parseInt(myTimeMinute)) + "分");
					time = formatTime(currentHour) + ":"
							+ formatTime(Integer.parseInt(myTimeMinute));
				} else if (!typeA && typeB) {
					showToast("保存成功!保存时间为" + dataString()
							+ formatTime(Integer.parseInt(myTimeHour)) + "时"
							+ formatTime(currentMinute) + "分");
					time = formatTime(Integer.parseInt(myTimeHour)) + ":"
							+ formatTime(currentMinute);
				} else {
					showToast("保存成功!保存时间为" + dataString()
							+ formatTime(currentHour) + "时"
							+ formatTime(currentMinute) + "分");
					time = formatTime(currentHour) + ":"
							+ formatTime(currentMinute);
				}
				String indexStr = (index == null ? getIntent().getStringExtra(
						"index") : index);
				NoteBean note = new NoteBean(time, getIntent().getStringExtra(
						"sectionName"),
						(String) tv_movement_alert_day.getText(),
						(String) tv_movement_alert_way.getText() + "提醒",
						"一次",
						(String) tv_movement_alert_week.getText(),
						(String) tv_movement_alert_timetype.getText(),
						indexStr, "true");
				DBManager.getInstance(this).inseartNote(note);
			} else if (NOTE_EDIT == state) {
				String myTime = (getIntent().getStringExtra("mytime") == null ? getIntent()
						.getStringExtra("time") : getIntent().getStringExtra(
						"mytime"));
				if (!typeA && !typeB) {
					showToast("保存成功!保存时间为"
							+ dataString()
							+ formatTime(Integer.parseInt(myTime
									.substring(0, 2)))
							+ "时"
							+ formatTime(Integer.parseInt(myTime
									.substring(3, 5))) + "分");
					time = formatTime(Integer.parseInt(myTime.substring(0, 2)))
							+ ":"
							+ formatTime(Integer.parseInt(myTime
									.substring(3, 5)));
				} else if (typeA && !typeB) {
					showToast("保存成功!保存时间为"
							+ dataString()
							+ formatTime(currentHour)
							+ "时"
							+ formatTime(Integer.parseInt(myTime
									.substring(3, 5))) + "分");
					time = formatTime(currentHour)
							+ ":"
							+ formatTime(Integer.parseInt(myTime
									.substring(3, 5)));
				} else if (!typeA && typeB) {
					showToast("保存成功!保存时间为"
							+ dataString()
							+ formatTime(Integer.parseInt(myTime
									.substring(0, 2))) + "时"
							+ formatTime(currentMinute) + "分");
					time = formatTime(Integer.parseInt(myTime.substring(0, 2)))
							+ ":" + formatTime(currentMinute);
				} else {
					showToast("保存成功!保存时间为" + dataString()
							+ formatTime(currentHour) + "时"
							+ formatTime(currentMinute) + "分");
					time = formatTime(currentHour) + ":"
							+ formatTime(currentMinute);
				}
				String indexStr = (index == null ? getIntent().getStringExtra(
						"index") : index);
				NoteBean note = new NoteBean(time, getIntent().getStringExtra(
						"sectionName"),
						(String) tv_movement_alert_day.getText(),
						(String) tv_movement_alert_way.getText() + "提醒",
						"一次",
						(String) tv_movement_alert_week.getText(),
						(String) tv_movement_alert_timetype.getText(),
						indexStr, "true");
				DBManager.getInstance(this).upDateNote(
						Integer.parseInt(getIntent().getStringExtra("id")),
						note);
				DebugUtility.showLog("alertDetail里的....."
						+ getIntent().getStringExtra("isstart"));
			}
			finish();
			break;
		case R.id.rl_movement_alert_type:
			if (NOTE_INSERT == state) {
				getInsertTime();
				skipInsertActivity(MovementAlertTypeActivity.class);
			} else if (NOTE_EDIT == state) {
				getEditTime();
				skipEditActivity(MovementAlertTypeActivity.class);
			}
			finish();
			break;
//		case R.id.rl_movement_alert_count:
//			if (NOTE_INSERT == state) {
//				getInsertTime();
//				skipInsertActivity(MovementAlertCountActivity.class);
//			} else if (NOTE_EDIT == state) {
//				getEditTime();
//				skipEditActivity(MovementAlertCountActivity.class);
//			}
//			finish();
//			break;
		case R.id.rl_movement_alert_way:
			if (NOTE_INSERT == state) {
				getInsertTime();
				skipInsertActivity(MovementAlertWayActivity.class);
			} else if (NOTE_EDIT == state) {
				getEditTime();
				skipEditActivity(MovementAlertWayActivity.class);
			}
			finish();
			break;

		case R.id.bt_movement_alert_btn1:
			index = "1";
			bt_movement_alert_btn1.setSelected(true);
			bt_movement_alert_btn1.setPressed(true);
			changEnable(false, true, true, true, true);
			bt_movement_alert_btn2.setSelected(false);
			bt_movement_alert_btn2.setPressed(false);
			bt_movement_alert_btn3.setSelected(false);
			bt_movement_alert_btn3.setPressed(false);
			bt_movement_alert_btn4.setSelected(false);
			bt_movement_alert_btn4.setPressed(false);
			bt_movement_alert_btn5.setSelected(false);
			bt_movement_alert_btn5.setPressed(false);
			if (bt_movement_alert_btn1.isSelected()) {
				day = (String) bt_movement_alert_btn1.getText();
				Calendar calendar = Calendar.getInstance();
				month = new SimpleDateFormat("MM").format(calendar.getTime());
				year = new SimpleDateFormat("yyyy").format(calendar.getTime());
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			}
			initData();
			break;
		case R.id.bt_movement_alert_btn2:
			index = "2";
			bt_movement_alert_btn2.setSelected(true);
			bt_movement_alert_btn2.setPressed(true);
			changEnable(true, false, true, true, true);
			bt_movement_alert_btn1.setSelected(false);
			bt_movement_alert_btn1.setPressed(false);
			bt_movement_alert_btn3.setSelected(false);
			bt_movement_alert_btn3.setPressed(false);
			bt_movement_alert_btn4.setSelected(false);
			bt_movement_alert_btn4.setPressed(false);
			bt_movement_alert_btn5.setSelected(false);
			bt_movement_alert_btn5.setPressed(false);
			if (bt_movement_alert_btn2.isSelected()) {
				day = (String) bt_movement_alert_btn2.getText();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_WEEK, 1);
				month = new SimpleDateFormat("MM").format(calendar.getTime());
				year = new SimpleDateFormat("yyyy").format(calendar.getTime());
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			}
			initData();
			break;
		case R.id.bt_movement_alert_btn3:
			index = "3";
			bt_movement_alert_btn3.setSelected(true);
			bt_movement_alert_btn3.setPressed(true);
			changEnable(true, true, false, true, true);
			bt_movement_alert_btn1.setSelected(false);
			bt_movement_alert_btn1.setPressed(false);
			bt_movement_alert_btn2.setSelected(false);
			bt_movement_alert_btn2.setPressed(false);
			bt_movement_alert_btn4.setSelected(false);
			bt_movement_alert_btn4.setPressed(false);
			bt_movement_alert_btn5.setSelected(false);
			bt_movement_alert_btn5.setPressed(false);
			if (bt_movement_alert_btn3.isSelected()) {
				day = (String) bt_movement_alert_btn3.getText();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_WEEK, 2);
				month = new SimpleDateFormat("MM").format(calendar.getTime());
				year = new SimpleDateFormat("yyyy").format(calendar.getTime());
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			}
			initData();
			break;
		case R.id.bt_movement_alert_btn4:
			index = "4";
			bt_movement_alert_btn4.setSelected(true);
			bt_movement_alert_btn4.setPressed(true);
			changEnable(true, true, true, false, true);
			bt_movement_alert_btn1.setSelected(false);
			bt_movement_alert_btn1.setPressed(false);
			bt_movement_alert_btn2.setSelected(false);
			bt_movement_alert_btn2.setPressed(false);
			bt_movement_alert_btn3.setSelected(false);
			bt_movement_alert_btn3.setPressed(false);
			bt_movement_alert_btn5.setSelected(false);
			bt_movement_alert_btn5.setPressed(false);
			if (bt_movement_alert_btn4.isSelected()) {
				day = (String) bt_movement_alert_btn4.getText();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_WEEK, 3);
				month = new SimpleDateFormat("MM").format(calendar.getTime());
				year = new SimpleDateFormat("yyyy").format(calendar.getTime());
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			}
			initData();
			break;
		case R.id.bt_movement_alert_btn5:
			index = "5";
			bt_movement_alert_btn5.setSelected(true);
			bt_movement_alert_btn5.setPressed(true);
			changEnable(true, true, true, true, false);
			bt_movement_alert_btn1.setSelected(false);
			bt_movement_alert_btn1.setPressed(false);
			bt_movement_alert_btn2.setSelected(false);
			bt_movement_alert_btn2.setPressed(false);
			bt_movement_alert_btn3.setSelected(false);
			bt_movement_alert_btn3.setPressed(false);
			bt_movement_alert_btn4.setSelected(false);
			bt_movement_alert_btn4.setPressed(false);
			if (bt_movement_alert_btn5.isSelected()) {
				day = (String) bt_movement_alert_btn5.getText();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_WEEK, 4);
				month = new SimpleDateFormat("MM").format(calendar.getTime());
				year = new SimpleDateFormat("yyyy").format(calendar.getTime());
				tv_movement_alert_week.setText(getWeek((calendar
						.get(Calendar.DAY_OF_WEEK)) % 7));
			}
			initData();
			break;
		default:
			break;
		}

	}

	private void changEnable(boolean b1, boolean b2, boolean b3, boolean b4,
			boolean b5) {
		bt_movement_alert_btn1.setEnabled(b1);
		bt_movement_alert_btn2.setEnabled(b2);
		bt_movement_alert_btn3.setEnabled(b3);
		bt_movement_alert_btn4.setEnabled(b4);
		bt_movement_alert_btn5.setEnabled(b5);
	}

	private String dataString() {
		return tv_movement_alert_day.getText().toString()
				+ tv_movement_alert_week.getText();
	}

	private void skipInsertActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_INSERT);
		newIntent.setClass(this, clazz);
		newIntent.putExtra("timetype", timetype);
//		newIntent.putExtra("count", count);
		newIntent.putExtra("way", way);
		newIntent.putExtra("mytime", time);
		String indexStr = (index == null ? getIntent().getStringExtra("index")
				: index);
		newIntent.putExtra("index", indexStr);
		newIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		newIntent
				.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("week", tv_movement_alert_week.getText());
		newIntent.putExtra("day", tv_movement_alert_day.getText());
		startActivity(newIntent);
	}

	private void skipEditActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_EDIT);
		newIntent.setClass(this, clazz);
		newIntent.putExtra("timetype", timetype);
//		newIntent.putExtra("count", count);
		newIntent.putExtra("way", way);
		newIntent.putExtra("id", getIntent().getStringExtra("id"));
		String indexStr = (index == null ? getIntent().getStringExtra("index")
				: index);
		newIntent.putExtra("index", indexStr);
		newIntent.putExtra("time", getIntent().getStringExtra("time"));
		newIntent.putExtra("mytime", time);
		newIntent.putExtra("week", tv_movement_alert_week.getText());
		newIntent.putExtra("day", tv_movement_alert_day.getText());
		newIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		newIntent
				.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("isstart", getIntent().getStringExtra("isstart"));
		startActivity(newIntent);
	}

	private String formatTime(int time) {
		return time < 10 ? "0" + time : "" + time;
	}

	private void getEditTime() {
		String myTime = (getIntent().getStringExtra("mytime") == null ? getIntent()
				.getStringExtra("time") : getIntent().getStringExtra("mytime"));
		if (!typeA && !typeB) {
			time = formatTime(Integer.parseInt(myTime.substring(0, 2))) + ":"
					+ formatTime(Integer.parseInt(myTime.substring(3, 5)));
		} else if (typeA && !typeB) {
			time = formatTime(currentHour) + ":"
					+ formatTime(Integer.parseInt(myTime.substring(3, 5)));
		} else if (!typeA && typeB) {
			time = formatTime(Integer.parseInt(myTime.substring(0, 2))) + ":"
					+ formatTime(currentMinute);
		} else {
			time = formatTime(currentHour) + ":" + formatTime(currentMinute);
		}
	}

	private void getInsertTime() {
		String myTimeHour = (getIntent().getStringExtra("mytime") == null ? (hour + "")
				: (getIntent().getStringExtra("mytime")).substring(0, 2));
		String myTimeMinute = (getIntent().getStringExtra("mytime") == null ? (minute + "")
				: (getIntent().getStringExtra("mytime")).substring(3, 5));
		if (!typeA && !typeB) {
			time = formatTime(Integer.parseInt(myTimeHour)) + ":"
					+ formatTime(Integer.parseInt(myTimeMinute));
		} else if (typeA && !typeB) {
			time = formatTime(currentHour) + ":"
					+ formatTime(Integer.parseInt(myTimeMinute));
		} else if (!typeA && typeB) {
			time = formatTime(Integer.parseInt(myTimeHour)) + ":"
					+ formatTime(currentMinute);
		} else {
			time = formatTime(currentHour) + ":" + formatTime(currentMinute);
		}
	}
}
