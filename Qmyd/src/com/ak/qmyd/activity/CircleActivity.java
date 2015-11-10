package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.HotCircleListAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.CircleListResult;
import com.ak.qmyd.bean.result.CircleListResult.HotCircleList;
import com.ak.qmyd.bean.result.CircleListResult.MyCircleList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.MyGridView;
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
 * @date 2015-6-4 上午10:07:30
 */
public class CircleActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	private ImageButton ib_circle_return;
	private ImageView iv_circle_jia;
	private MyGridView gv_circle;
	private ListView lv_circle_listview;
	private HotCircleListAdapter adapter;
	private List<ImageView> imageviewList = new ArrayList<ImageView>();
	private ImageView imageView_item;
	private myHorizontialListViewAdapt myAdapt;
	private Map<String, ?> userInfoSp;
	private ArrayList<MyCircleList> myCircleList;
	private ArrayList<HotCircleList> hotCircleList;
	private TextView emptyView, lv_foot_textView;
	private View footview;
	private int currentNumber = 10;
	private int currentPage = 1;
	private DrawerLayout mDrawerLayout;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private float density;
	private View headView;
	private ArrayList<HotCircleList> totalHotCircleList;// 总集合的内容
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_list);
		loadDialog = UIManager.getLoadingDialog(this);
		headView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_circle, null);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		getData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_circle_dl);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无圈子");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) gv_circle.getParent()).addView(emptyView);
		gv_circle.setEmptyView(emptyView);
	}

	private void getData() {
		loadDialog.show();
		totalHotCircleList = new ArrayList<HotCircleList>();
		DebugUtility.showLog("当前页码" + currentPage);
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryMyCircleList/userId/sessionId/hardId/currPage/size/typeId
		String url = Config.CIRCLE_LIST_URL + "/" + userInfoSp.get("userId")
				+ "/" + userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "0");
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("圈子列表url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("圈子列表数据:" + response);
							Gson gson = new Gson();
							CircleListResult clr = gson.fromJson(response,
									CircleListResult.class);
							totalHotCircleList.clear();
							UIManager.toggleDialog(loadDialog);
							if (clr.getResultCode() == 1) {
								myCircleList = clr.getMyCircleList();
								hotCircleList = clr.getHotCircleList();
								if (hotCircleList != null) {
									totalHotCircleList.addAll(hotCircleList);
									flag = true;
									initData();
									adapter = new HotCircleListAdapter(
											CircleActivity.this,
											totalHotCircleList);
									lv_circle_listview.setAdapter(adapter);
									if (hotCircleList.size() < currentNumber) {
										lv_foot_textView.setText("没有更多相关数据");
									}
								} else {
									totalHotCircleList.clear();
									adapter.notifyDataSetChanged();
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (clr.getResultCode() == 0) {
								flag = false;
								initData();
								showToast("操作失败");
							} else if (clr.getResultCode() == 10000) {
								flag = false;
								changeLogin();
								showToast("未登陆或登陆超时，请重新登陆");
							}
							if (myCircleList == null) {
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

	private void initData() {
		initGridView();
		// adapter = new myListAdapter(this);
		// lv_circle_listview.setAdapter(adapter);
	}

	private void initGridView() {
		myAdapt = new myHorizontialListViewAdapt(this);
		gv_circle.setAdapter(myAdapt);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (dm != null) {
			density = dm.density;
		}
		if (myCircleList != null) {
			int allWidth = (int) (110 * myCircleList.size() * density);
			int itemWidth = (int) (87 * density);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
			gv_circle.setLayoutParams(params);
			gv_circle.setColumnWidth(itemWidth);
			gv_circle.setStretchMode(GridView.NO_STRETCH);
			gv_circle.setNumColumns(myCircleList.size());
		}
	}

	private void initView() {
		ib_circle_return.setOnClickListener(this);
		iv_circle_jia.setOnClickListener(this);
	}

	private void findView() {
		ib_circle_return = (ImageButton) findViewById(R.id.ib_circle_return);
		iv_circle_jia = (ImageView) findViewById(R.id.iv_circle_jia);
		gv_circle = (MyGridView) headView.findViewById(R.id.gv_circle);
		lv_circle_listview = (ListView) findViewById(R.id.lv_circle_listview);
		lv_circle_listview.addHeaderView(headView);
		lv_circle_listview.setOnScrollListener(this);
		footview = LayoutInflater.from(this).inflate(
				R.layout.listview_footview, null);
		lv_foot_textView = (TextView) footview.findViewById(R.id.tv_footview);
		lv_circle_listview.addFooterView(footview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_circle_return:
			finish();
			break;
		case R.id.iv_circle_jia:
			startActivity(CreateCirclesActivity.class, null);
			break;
		default:
			break;
		}
	}

	class myHorizontialListViewAdapt extends BaseAdapter {

		private Context context;

		public myHorizontialListViewAdapt(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return myCircleList == null ? 0 : myCircleList.size();
		}

		@Override
		public Object getItem(int position) {
			return myCircleList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			GridViewHolder gridHolder = null;
			if (convertView == null) {
				gridHolder = new GridViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.activity_circle_gridview_item, null);
				gridHolder.iv_gv_item_img = (CircleImageView) convertView
						.findViewById(R.id.iv_gv_item_img);
				convertView.setTag(gridHolder);
			} else {
				gridHolder = (GridViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(CircleActivity.this,
							CircleDetailActivity.class);
					newIntent.putExtra("circleId", myCircleList.get(position)
							.getCircleId());
					startActivity(newIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ myCircleList.get(position).getThunmnailPath(),
					gridHolder.iv_gv_item_img);
			return convertView;
		}
	}

	static class GridViewHolder {
		CircleImageView iv_gv_item_img;
	}

	static class ListViewHolder {
		CircleImageView iv_circle_item_img;
		TextView iv_circle_item_title, iv_circle_item_content;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getData();
	}

	private void loadMore() {
		loadDialog.show();
		currentPage++;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryMyCircleList/userId/sessionId/hardId/currPage/size/typeId
		String url = Config.CIRCLE_LIST_URL + "/" + userInfoSp.get("userId")
				+ "/" + userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "0");
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("热门圈子列表url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("热门圈子列表数据:" + response);
							Gson gson = new Gson();
							CircleListResult clr = gson.fromJson(response,
									CircleListResult.class);
							UIManager.toggleDialog(loadDialog);
							if (clr.getResultCode() == 1) {
								hotCircleList = clr.getHotCircleList();
								if (hotCircleList != null) {
									totalHotCircleList.addAll(hotCircleList);
									initData();
									flag = true;
									adapter.notifyDataSetChanged();
									if (hotCircleList.size() < currentNumber) {
										lv_foot_textView.setText("没有更多相关数据");
									}
								} else {
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (clr.getResultCode() == 0) {
								initData();
								showToast("操作失败");
							} else if (clr.getResultCode() == 10000) {
								changeLogin();
								showToast("未登陆或登陆超时，请重新登陆");
							}
//							if (lv_circle_listview.getEmptyView() == null) {
//								setEmptyView();
//							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							flag = true;
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
		}
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
			if ((firstVisibleItem + visibleItemCount) == (totalItemCount)) {
				flag = false;
				loadMore();
			}
		}
	}
}
