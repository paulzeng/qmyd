package com.ak.qmyd.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.NoteBean;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.dialog.AlarmDialog;
import com.ak.qmyd.dialog.AlarmDialog.OnActionSheetSelected;
import com.ak.qmyd.receiver.AlarmReceiver;
import com.ak.qmyd.tools.DebugUtility;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;

/**
 * @author JGB
 * @date 2015-5-11 下午4:27:30
 */
public class TrainAlertListActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_alert_detail_return;
	private ImageView iv_alert_detail_jia;
	private SwipeMenuListView lv_alert_detail_listview;
	private MyAlertListAdapter myAlertListAdapter;
	private List<NoteBean> noteList;
	private NoteBean noteBean;
	private DrawerLayout mDrawerLayout;
	private View emptyView;
	private Calendar calendar;
    private TextView tv_alert_detail_title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alert_detail);

		findView();
		noteList = DBManager.getInstance(this).getNoteList();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_alert_detail_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		lv_alert_detail_listview = (SwipeMenuListView) findViewById(R.id.lv_alert_detail_listview);
		myAlertListAdapter = new MyAlertListAdapter();
		lv_alert_detail_listview.setAdapter(myAlertListAdapter);
		setEmptyView();
		listViewItemSlide();
		if (emptyView.getVisibility() == View.VISIBLE) {
			iv_alert_detail_jia.setVisibility(View.GONE);
		} else if (emptyView.getVisibility() == View.GONE) {
			iv_alert_detail_jia.setVisibility(View.VISIBLE);
		}
	}

	private void listViewItemSlide() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		lv_alert_detail_listview.setMenuCreator(creator);

		// // step 2. listener item click event
		lv_alert_detail_listview
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						switch (index) {
						case 0:
							showToast("已删除");
							DBManager.getInstance(getApplicationContext())
									.deleteNoteById(
											noteList.get(position).getId());
							noteList = DBManager.getInstance(
									getApplicationContext()).getNoteList();
							changTitle();
							myAlertListAdapter.notifyDataSetChanged();
							if (emptyView.getVisibility() == View.VISIBLE) {
								iv_alert_detail_jia.setVisibility(View.GONE);
							} else if (emptyView.getVisibility() == View.GONE) {
								iv_alert_detail_jia.setVisibility(View.VISIBLE);
							}
							break;
						}
						return false;
					}
				});
		lv_alert_detail_listview.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
		lv_alert_detail_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						noteBean = DBManager.getInstance(
								getApplicationContext()).getNoteByID(
								noteList.get(position).getId());
						skipActivity(position, noteBean);

					}
				});
	}

	private void setEmptyView() {
		emptyView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_alert_empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		Button bt_alert_empty = (Button) emptyView
				.findViewById(R.id.bt_alert_empty);
		LinearLayout ll_alert_empty = (LinearLayout) emptyView
				.findViewById(R.id.ll_alert_empty);
		ll_alert_empty.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		bt_alert_empty.setOnClickListener(this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_alert_detail_listview.getParent()).addView(emptyView);
		lv_alert_detail_listview.setEmptyView(emptyView);
	}

	class MyAlertListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return noteList == null ? 0 : noteList.size();
		}

		@Override
		public NoteBean getItem(int position) {
			return noteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.activity_alert_detail_item, null);
				new ViewHolder(convertView);
			}
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.tv_item_time.setText(noteList.get(position).getTime());
			holder.tv_item_title.setText(noteList.get(position).getTitle());
			holder.tv_item_week.setText(noteList.get(position).getDay());
			holder.tv_item_way.setText(noteList.get(position).getWay());
//			holder.tv_item_count.setText(noteList.get(position).getCount());
			DebugUtility.showLog("当前的状态:"+noteList.get(position).getIsstart());
			if (noteList.get(position).getIsstart().equals("true")) {
				holder.tb_alert_tb.setChecked(true);
			} else if (noteList.get(position).getIsstart().equals("false")) {
				holder.tb_alert_tb.setChecked(false);
			}
			holder.tb_alert_tb
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							holder.tb_alert_tb.setChecked(isChecked);
							if (isChecked) {
								DBManager.getInstance(getApplicationContext())
										.upDateNoteById(
												noteList.get(position).getId(),
												"true");
							} else {
								DBManager.getInstance(getApplicationContext())
										.upDateNoteById(
												noteList.get(position).getId(),
												"false");
							}
						}
					});
			if (noteList.get(position).getIsstart().equals("true")) {
				startAlarmClock(
						noteList.get(position).getId(),
						noteList.get(position).getTitle(),
						noteList.get(position).getTime(),
						noteList.get(position).getDay(),
						noteList.get(position).getTimeType(),
						noteList.get(position)
								.getWay()
								.substring(
										0,
										noteList.get(position).getWay()
												.length() - 2),
						noteList.get(position).getCount());// 开启闹钟功能
			}
			return convertView;

		}

		class ViewHolder {
			TextView tv_item_time, tv_item_title, tv_item_week, tv_item_way,
					tv_item_count;
			ToggleButton tb_alert_tb;

			public ViewHolder(View view) {
				tv_item_time = (TextView) view.findViewById(R.id.tv_item_time);
				tv_item_title = (TextView) view
						.findViewById(R.id.tv_item_title);
				tv_item_week = (TextView) view.findViewById(R.id.tv_item_week);
				tv_item_way = (TextView) view.findViewById(R.id.tv_item_way);
//				tv_item_count = (TextView) view
//						.findViewById(R.id.tv_item_count);
				tb_alert_tb = (ToggleButton) view
						.findViewById(R.id.tb_alert_tb);
				view.setTag(this);
			}
		}
	}

	public void skipActivity(final int position, NoteBean noteBean) {
		Intent itemIntent = new Intent(Intent.ACTION_EDIT);
		itemIntent.setClass(TrainAlertListActivity.this,
				TrainAlertDetailActivity.class);
		itemIntent.putExtra("id", noteList.get(position).getId() + "");
		itemIntent.putExtra("time", noteBean.getTime());
		itemIntent.putExtra("title", noteBean.getTitle());
		itemIntent.putExtra("day", noteBean.getDay());
		itemIntent.putExtra("way",
				noteBean.getWay().substring(0, noteBean.getWay().length() - 2));
		itemIntent.putExtra("count", noteBean.getCount());
		itemIntent.putExtra("week", noteBean.getWeek());
		itemIntent.putExtra("timetype", noteBean.getTimeType());
		itemIntent.putExtra("index", noteBean.getIndex());
		itemIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		itemIntent.putExtra("isstart", noteBean.getIsstart());
		DebugUtility.showLog("alertList里的....." + noteBean.getIsstart());
		startActivity(itemIntent);
	}

	@SuppressLint("NewApi")
	public void startAlarmClock(int id, String title, String time, String day,
			String timeType, String way, String count) {
		noteBean = DBManager.getInstance(getApplicationContext()).getNoteByID(
				id);
		int currentYear = Integer.parseInt(day.substring(0, 4));
		int currentMonth = Integer.parseInt(day.substring(5, 7));
		int currentDay = Integer.parseInt(day.substring(8, 10));
		String currentTotalTime = currentYear + "-" + currentMonth + "-"
				+ currentDay + " " + time + ":00";
		// 进行闹铃注册
		Intent intent = new Intent(TrainAlertListActivity.this,
				AlarmReceiver.class);
		intent.setAction("alertList");
		intent.putExtra("id", id + "");
		intent.putExtra("title", title);
		intent.putExtra("time", time);
		intent.putExtra("timetype", timeType);
		intent.putExtra("day", day);
		intent.putExtra("way", way);
		intent.putExtra("count", count);
		intent.putExtra("currentTotalTime", currentTotalTime);
		if (noteBean.getIsstart().equals("true")) {
			PendingIntent sender = PendingIntent.getBroadcast(
					TrainAlertListActivity.this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// 到了时间 执行这个闹铃
			int realTime = Integer.parseInt(time.substring(0, 2)) * 3600 * 1000
					+ Integer.parseInt(time.substring(3, 5)) * 60 * 1000;
			String currentTime = new SimpleDateFormat("HH:mm:ss",
					Locale.getDefault()).format(new Date());
			DebugUtility.showLog("currentTime:" + currentTime);
			int systemTime = Integer.parseInt(currentTime.substring(0, 2))
					* 3600 * 1000
					+ Integer.parseInt(currentTime.substring(3, 5)) * 60 * 1000;
			int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale
					.getDefault()).format(new Date()));
			int month = Integer.parseInt(new SimpleDateFormat("MM", Locale
					.getDefault()).format(new Date()));
			int days = Integer.parseInt(new SimpleDateFormat("dd", Locale
					.getDefault()).format(new Date()));
			
			String systemTotalTime = year + "-" + month + "-" + days + " "
					+ currentTime;
			DebugUtility.showLog(currentTotalTime + ".........."
					+ systemTotalTime);
			if (currentYear >= year) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					Date d1 = df.parse(systemTotalTime);
					Date d2 = df.parse(currentTotalTime);
					int alertTime = (int) ((d2.getTime() - d1.getTime()) / 1000);// 得到的是秒值
					DebugUtility.showLog("alertTime:" + alertTime + "秒");
					if (alertTime > 0) {
						calendar = Calendar.getInstance();
						calendar.setTimeInMillis(System.currentTimeMillis());
						calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
						calendar.add(Calendar.SECOND, alertTime);
						AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
						manager.setExact(AlarmManager.RTC_WAKEUP,
								calendar.getTimeInMillis(), sender);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			showToast("已关闭闹钟");
		}
	}

	private void initView() {
		ib_alert_detail_return.setOnClickListener(this);
		iv_alert_detail_jia.setOnClickListener(this);
		changTitle();
	}

	private void changTitle() {
		if(noteList.size() == 0){
			tv_alert_detail_title.setText("运动提醒");
		}else{
			tv_alert_detail_title.setText("提醒时间");
		}
	}

	private void findView() {
		ib_alert_detail_return = (ImageButton) findViewById(R.id.ib_alert_detail_return);
		iv_alert_detail_jia = (ImageView) findViewById(R.id.iv_alert_detail_jia);
		tv_alert_detail_title = (TextView) findViewById(R.id.tv_alert_detail_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_alert_empty:
			Intent i1 = new Intent(Intent.ACTION_INSERT);
			i1.setClass(this, TrainAlertDetailActivity.class);
			i1.putExtra("type", "1");
			i1.putExtra("sectionName", getIntent()
					.getStringExtra("sectionName"));
			i1.putExtra("index", "1");
			startActivity(i1);
			break;
		case R.id.iv_alert_detail_jia:
			Intent i = new Intent(Intent.ACTION_INSERT);
			i.setClass(this, TrainAlertDetailActivity.class);
			i.putExtra("type", "1");
			i.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
			i.putExtra("index", "1");
			startActivity(i);
			break;
		case R.id.ib_alert_detail_return:
			finish();
			// skipActivity(SynchronousTrainActivity2.class);
			break;
		default:
			break;
		}

	}

	private void skipActivity(Class clazz) {
		Intent newIntent = new Intent(TrainAlertListActivity.this, clazz);
		newIntent
				.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("sectionName",
				getIntent().getStringExtra("sectionName"));
		startActivity(newIntent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		DebugUtility.showLog("TrainAlertListActivity onRestart");
		noteList = DBManager.getInstance(this).getNoteList();
		changTitle();
		myAlertListAdapter = new MyAlertListAdapter();
		lv_alert_detail_listview.setAdapter(myAlertListAdapter);
		if (emptyView.getVisibility() == View.VISIBLE) {
			iv_alert_detail_jia.setVisibility(View.GONE);
		} else if (emptyView.getVisibility() == View.GONE) {
			iv_alert_detail_jia.setVisibility(View.VISIBLE);
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
