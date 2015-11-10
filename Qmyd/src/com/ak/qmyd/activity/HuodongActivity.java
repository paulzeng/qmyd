package com.ak.qmyd.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.HuoDongResult;
import com.ak.qmyd.bean.result.HuoDongResult.VenuesHotEventList;
import com.ak.qmyd.bean.result.HuoDongResult.VenuesNewEventList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
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
 * @date 2015-7-3 上午11:24:30
 */
public class HuodongActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	private ImageButton ib_huodong_return;
	private ListView lv_huodong_hot, lv_huodong_new;
	private HotAdapter hotAdapter;
	private NewAdapter newAdapter;
	private DrawerLayout drawerlayout;
	private View headView, footview;
	private TextView lv_foot_textView, emptyView;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private Dialog loadDialog;
	private int currentNumber = 10;
	private int currentPage = 1;
	private ArrayList<VenuesHotEventList> venuesHotEventList;
	private ArrayList<VenuesNewEventList> venuesNewEventList;
	private ArrayList<VenuesNewEventList> totalVenuesNewEventList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huodong);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		headView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_huodong_head, null);
		findView();
		initView();
		getData();
		drawerlayout = (DrawerLayout) this
				.findViewById(R.id.dl_huodong_drawerlayout);
		setEnableDrawerLayout(drawerlayout);
		totalVenuesNewEventList = new ArrayList<VenuesNewEventList>();
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		emptyView.setLayoutParams(params);
		emptyView.setText("暂无活动");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_huodong_new.getParent()).addView(emptyView);
		lv_huodong_new.setEmptyView(emptyView);
	}

	private void getData() {
		loadDialog.show();
		currentPage = 1;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/venues/queryVenuesEventList/{hardId}/{typeId}/{currPage}/{pageSize}
		String url = Config.VENUE_ACTIVITY + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "1")
				+ "/" + currentPage + "/" + currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("活动url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							DebugUtility.showLog("活动返回数据:" + response);
							Gson gson = new Gson();
							HuoDongResult hdr = gson.fromJson(response,
									HuoDongResult.class);
							totalVenuesNewEventList.clear();
							UIManager.toggleDialog(loadDialog);
							if (hdr.getResultCode() == 1) {
								venuesHotEventList = hdr
										.getVenuesHotEventList();
								venuesNewEventList = hdr
										.getVenuesNewEventList();
								if (venuesNewEventList != null) {
									flag = true;
									hotAdapter = new HotAdapter(
											HuodongActivity.this,
											venuesHotEventList);
									lv_huodong_hot.setAdapter(hotAdapter);
									Tools.setListViewHeightBasedOnChildren(lv_huodong_hot);
									newAdapter = new NewAdapter(
											HuodongActivity.this,
											totalVenuesNewEventList);
									lv_huodong_new.setAdapter(newAdapter);
									if (venuesNewEventList != null) {
										totalVenuesNewEventList
												.addAll(venuesNewEventList);
										if (venuesNewEventList.size() < currentNumber) {
											lv_foot_textView
													.setVisibility(View.GONE);
										} else {
											lv_foot_textView.setText("正在加载...");
										}
									} else {
										totalVenuesNewEventList.clear();
										newAdapter.notifyDataSetChanged();
										lv_foot_textView.setText("没有更多相关数据");
									}
								}
							} else if (hdr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (hdr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_huodong_new.getEmptyView() == null) {
								setEmptyView();
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							flag = false;
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
			UIManager.toggleDialog(loadDialog);
		}

	}

	private void initView() {
		ib_huodong_return.setOnClickListener(this);

	}

	private void findView() {
		ib_huodong_return = (ImageButton) findViewById(R.id.ib_huodong_return);
		lv_huodong_hot = (ListView) headView
				.findViewById(R.id.lv_huodong_head_hot);
		lv_huodong_new = (ListView) findViewById(R.id.lv_huodong_new);
		lv_huodong_new.addHeaderView(headView);
		footview = LayoutInflater.from(this).inflate(
				R.layout.listview_footview, null);
		lv_foot_textView = (TextView) footview.findViewById(R.id.tv_footview);
		lv_huodong_new.addFooterView(footview);
		lv_huodong_new.setOnScrollListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_huodong_return:
			startActivity(HomeActivity.class, null);
			finish();
			break;

		default:
			break;
		}
	}

	class HotAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<VenuesHotEventList> venuesHotEventList;

		public HotAdapter(Context context,
				ArrayList<VenuesHotEventList> venuesHotEventList) {

			this.context = context;
			this.venuesHotEventList = venuesHotEventList;
		}

		@Override
		public int getCount() {

			return venuesHotEventList == null ? 0 : venuesHotEventList.size();
		}

		@Override
		public Object getItem(int position) {

			return venuesHotEventList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_huodong_listitem, null);
				holder.iv_huodong_item_img = (ImageView) convertView
						.findViewById(R.id.iv_huodong_item_img);
				holder.tv_huodong_item_title = (TextView) convertView
						.findViewById(R.id.tv_huodong_item_title);
				holder.tv_huodong_item_time = (TextView) convertView
						.findViewById(R.id.tv_huodong_item_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ venuesHotEventList.get(position).getEventPic(),
					holder.iv_huodong_item_img);
			holder.tv_huodong_item_title.setText(venuesHotEventList.get(
					position).getEventName());
			holder.tv_huodong_item_time.setText(venuesHotEventList
					.get(position).getEventBeginTime()
					+ "-"
					+ venuesHotEventList.get(position).getEventEndTime());
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView iv_huodong_item_img;
		TextView tv_huodong_item_title, tv_huodong_item_time;
	}

	class NewAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<VenuesNewEventList> totalVenuesNewEventList;

		public NewAdapter(Context context,
				ArrayList<VenuesNewEventList> totalVenuesNewEventList) {
			this.context = context;
			this.totalVenuesNewEventList = totalVenuesNewEventList;
		}

		@Override
		public int getCount() {

			return totalVenuesNewEventList == null ? 0
					: totalVenuesNewEventList.size();
		}

		@Override
		public Object getItem(int position) {

			return totalVenuesNewEventList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_huodong_listitem, null);
				holder.iv_huodong_item_img = (ImageView) convertView
						.findViewById(R.id.iv_huodong_item_img);
				holder.tv_huodong_item_title = (TextView) convertView
						.findViewById(R.id.tv_huodong_item_title);
				holder.tv_huodong_item_time = (TextView) convertView
						.findViewById(R.id.tv_huodong_item_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ totalVenuesNewEventList.get(position)
									.getEventPic(), holder.iv_huodong_item_img);
			holder.tv_huodong_item_title.setText(totalVenuesNewEventList.get(
					position).getEventName());
			holder.tv_huodong_item_time.setText(totalVenuesNewEventList.get(
					position).getEventBeginTime()
					+ "-"
					+ totalVenuesNewEventList.get(position).getEventEndTime());
			return convertView;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(HomeActivity.class, null);
			finish();
		}
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (flag) {
			DebugUtility.showLog(firstVisibleItem + "..." + visibleItemCount
					+ "..." + totalItemCount);
			if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
				flag = false;
				loadMore();
			}
		}
	}

	private void loadMore() {
		loadDialog.show();
		currentPage++;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryOneCircleInfo/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.VENUE_ACTIVITY + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "1")
				+ "/" + currentPage + "/" + currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("活动url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("活动返回数据:" + response);
							Gson gson = new Gson();
							HuoDongResult hdr = gson.fromJson(response,
									HuoDongResult.class);
							UIManager.toggleDialog(loadDialog);
							if (hdr.getResultCode() == 1) {
								venuesNewEventList = hdr.getVenuesNewEventList();
								if (venuesNewEventList.size() > 0) {
										flag = true;
										totalVenuesNewEventList
												.addAll(venuesNewEventList);
										newAdapter.notifyDataSetChanged();
										if (venuesNewEventList.size() < currentNumber) {
											lv_foot_textView
													.setText("没有更多相关数据");
										}
									} else {
										lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (hdr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (hdr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							flag = false;
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
			UIManager.toggleDialog(loadDialog);
		}
	}
}
