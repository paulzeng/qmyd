package com.ak.qmyd.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.TrainHistoryResult;
import com.ak.qmyd.bean.result.TrainHistoryResult.HistoryList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CustomExpandableListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author JGB
 * @date 2015-6-5 下午4:08:41
 */
public class TrainHistoryMainActivity extends BaseFragmentActivity implements
		OnClickListener {

	private CustomExpandableListView elv_main_history;
	private ImageButton ib_main_history_return;
	private myExpandableListView adapter;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private String typeId;
	private TrainHistoryResult thr;
	private ArrayList<HistoryList> historyList;
	private LayoutInflater mGroupInflater; // 用于加载group的布局xml
	private LayoutInflater mChildInflater; // 用于加载listitem的布局xml
	private ViewGroupHolder viewGroupHolder = null;
	private ViewChildHolder viewChildHolder = null;
	private DrawerLayout mDrawerLayout;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_train_main_history);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		findView();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		if (NetManager.isNetworkConnected(this)) {
			setData();
			initView();
		} else {
			showToast("网络不可用，请检查网络设置");
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_history_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		elv_main_history.setFocusable(true);
		elv_main_history.setFocusableInTouchMode(true);
		elv_main_history.requestFocus();
		elv_main_history.setCacheColorHint(Color.rgb(0, 0, 0));
		adapter = new myExpandableListView(this);
		elv_main_history.setAdapter(adapter);
		Tools.setListViewHeightBasedOnChildren(elv_main_history);
	}

	private void setData() {
		loadDialog.show();
		// rest/train/History/{hardId}/{sessionId}/{userId}/{typeId}
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		if (getIntent().getStringExtra("source").equals("myTrain")) {
			typeId = getIntent().getStringExtra("typeId");
			DebugUtility.showLog("来自我的训练");
		} else if (getIntent().getStringExtra("source").equals("syncTrain")) {
			typeId = MyApplication.instance.getTypeId();
			DebugUtility.showLog("来自同步训练");
		}
		String userId = ((String) userInfoSp.get("userId") == null ? "0"
				: (String) userInfoSp.get("userId"));
		String url = Config.TRAIN_HISTORY_URL + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ userId + "/" + typeId;
		DebugUtility.showLog("返回历史链接:" + url);
		StringRequest strRequest = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// 通讯成功的 返回数据
						DebugUtility.showLog("返回历史数据" + response);
						Gson gson = new Gson();
						thr = gson.fromJson(response, TrainHistoryResult.class);
						UIManager.toggleDialog(loadDialog);
						if (thr.getResultCode() == 1) {
							historyList = thr.getHistoryList();
							adapter.notifyDataSetChanged();
						}
						setEmptyView();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// 通讯失败的 抛出异常
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		queue.add(strRequest);// 加入到通讯队列中

	}

	private void initView() {
		ib_main_history_return.setOnClickListener(this);
	}

	private void findView() {
		elv_main_history = (CustomExpandableListView) findViewById(R.id.elv_main_history);
		ib_main_history_return = (ImageButton) findViewById(R.id.ib_main_history_return);
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无训练历程");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) elv_main_history.getParent().getParent().getParent().getParent()).addView(emptyView);
		elv_main_history.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_main_history_return:
			finish();
			break;

		default:
			break;
		}
	}

	class myExpandableListView extends BaseExpandableListAdapter {

		private Context context;

		public myExpandableListView(Context context) {
			this.context = context;
			mChildInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mGroupInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getGroupCount() {

			return historyList == null ? 0 : historyList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {

			return historyList == null ? 0 : (historyList.get(groupPosition)
					.getSectionArray() == null ? 0 : historyList
					.get(groupPosition).getSectionArray().size());
		}

		@Override
		public Object getGroup(int groupPosition) {

			return historyList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return historyList.get(groupPosition).getSectionArray();
		}

		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return Long.parseLong(historyList.get(groupPosition)
					.getSectionArray().get(childPosition).getSectionId());
		}

		@Override
		public boolean hasStableIds() {

			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return false;
		}

		@SuppressLint("NewApi")
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			if (null == convertView) {
				convertView = mGroupInflater.inflate(
						R.layout.activity_main_history_elv_group, null);
				viewGroupHolder = new ViewGroupHolder();
				viewGroupHolder.tv_history_elv_group_year = (TextView) convertView
						.findViewById(R.id.tv_history_elv_group_year);
				viewGroupHolder.tv_history_elv_group_month = (TextView) convertView
						.findViewById(R.id.tv_history_elv_group_month);
				viewGroupHolder.tv_history_elv_group_total_time = (TextView) convertView
						.findViewById(R.id.tv_history_elv_group_total_time);
				viewGroupHolder.iv_history_elv_group_next = (ImageView) convertView
						.findViewById(R.id.iv_history_elv_group_next);
				convertView.setTag(viewGroupHolder);
			} else {
				viewGroupHolder = (ViewGroupHolder) convertView.getTag();
			}
			String year = historyList.get(groupPosition).getTrainMonth()
					.split("-")[0];
			String month = historyList.get(groupPosition).getTrainMonth()
					.split("-")[1];
			if (groupPosition - 1 >= 0) {
				if ((historyList.get(groupPosition - 1).getTrainMonth()
						.split("-")[0]).equals(year)) {
					 viewGroupHolder.tv_history_elv_group_year.setVisibility(View.GONE);
				}else{
					viewGroupHolder.tv_history_elv_group_year.setVisibility(View.VISIBLE);
				}
			}
			if ((new SimpleDateFormat("yyyy", Locale.getDefault())
					.format(new Date())).equals(year)) {
				viewGroupHolder.tv_history_elv_group_year
						.setVisibility(View.GONE);
			}
			viewGroupHolder.tv_history_elv_group_year.setClickable(false);
			viewGroupHolder.tv_history_elv_group_year.setEnabled(false);
			viewGroupHolder.tv_history_elv_group_year.setText(year + "年");
			viewGroupHolder.tv_history_elv_group_month
					.setText(FormatMonth(month));
			viewGroupHolder.tv_history_elv_group_total_time.setText(historyList
					.get(groupPosition).getTrainTotalTime() + "min");
			if (isExpanded) {
				viewGroupHolder.iv_history_elv_group_next
						.setBackgroundResource(R.drawable.delta_t);
				elv_main_history.setDivider(new ColorDrawable(Color
						.parseColor("#D7D7D7")));
//				 expandableListView.setCacheColorHint(Color.rgb(255, 255,
//				 255));
				elv_main_history.setDividerHeight(0);
			} else {
				viewGroupHolder.iv_history_elv_group_next
						.setBackgroundResource(R.drawable.delta_b);
				elv_main_history.setDivider(new ColorDrawable(Color
						.parseColor("#F0F0F0")));
				elv_main_history.setCacheColorHint(Color.rgb(255, 255, 255));
				elv_main_history.setDividerHeight(15);
			}
			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = mChildInflater.inflate(
						R.layout.activity_my_train_history_listitem, null);

				viewChildHolder = new ViewChildHolder();
				viewChildHolder.tv_train_history_item_title = (TextView) convertView
						.findViewById(R.id.tv_train_history_item_title);
				viewChildHolder.tv_train_history_item_time = (TextView) convertView
						.findViewById(R.id.tv_train_history_item_time);
				viewChildHolder.tv_train_history_item_score = (TextView) convertView
						.findViewById(R.id.tv_train_history_item_score);
				viewChildHolder.iv_train_history_item_img = (ImageView) convertView
						.findViewById(R.id.iv_train_history_item_img);
				convertView.setTag(viewChildHolder);
			} else {
				viewChildHolder = (ViewChildHolder) convertView.getTag();
			}
			if(isLastChild){
				elv_main_history.setDivider(new ColorDrawable(Color
						.parseColor("#F0F0F0")));
				elv_main_history.setCacheColorHint(Color.rgb(255, 255, 255));
				elv_main_history.setDividerHeight(15);
			}else{
				elv_main_history.setDivider(new ColorDrawable(Color
						.parseColor("#D7D7D7")));
				elv_main_history.setDividerHeight(0);
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(TrainHistoryMainActivity.this,
							TrainHistoryDetailActivity.class);
					newIntent.putExtra("sectionId", historyList
							.get(groupPosition).getSectionArray().get(childPosition).getSectionId());
					newIntent.putExtra("trainMonth", historyList
							.get(groupPosition).getSectionArray().get(childPosition).getTrainMonth());
					startActivity(newIntent);
				}
			});
			
			// 绑定数据
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ historyList.get(groupPosition).getSectionArray()
									.get(childPosition).getSectionImage(),
					viewChildHolder.iv_train_history_item_img);
			viewChildHolder.tv_train_history_item_time.setText(historyList
					.get(groupPosition).getSectionArray().get(childPosition)
					.getSectionTotalTime()
					+ "min");
			viewChildHolder.tv_train_history_item_score.setText(historyList
					.get(groupPosition).getSectionArray().get(childPosition)
					.getSectionTotalScore()
					+ "分");
			viewChildHolder.tv_train_history_item_title.setText(historyList
					.get(groupPosition).getSectionArray().get(childPosition)
					.getSectionName());
			return convertView;
		}
	}

	static class ViewChildHolder {
		TextView tv_train_history_item_title, tv_train_history_item_time,
				tv_train_history_item_score;
		ImageView iv_train_history_item_img;
	}

	static class ViewGroupHolder {
		TextView tv_history_elv_group_year, tv_history_elv_group_month,
				tv_history_elv_group_total_time;
		ImageView iv_history_elv_group_next;
	}

	private String FormatMonth(String month) {
		if (month.equals("01")) {
			return "一月";
		} else if (month.equals("02")) {
			return "二月";
		} else if (month.equals("03")) {
			return "三月";
		} else if (month.equals("04")) {
			return "四月";
		} else if (month.equals("05")) {
			return "五月";
		} else if (month.equals("06")) {
			return "六月";
		} else if (month.equals("07")) {
			return "七月";
		} else if (month.equals("08")) {
			return "八月";
		} else if (month.equals("09")) {
			return "九月";
		} else if (month.equals("10")) {
			return "十月";
		} else if (month.equals("11")) {
			return "十一月";
		} else if (month.equals("12")) {
			return "十二月";
		}
		return null;
	}
}
