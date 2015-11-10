package com.ak.qmyd.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.result.GoodNoteResult;
import com.ak.qmyd.bean.result.GoodNoteResult.CircleDetailList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
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
 * @date 2015-6-11 下午8:05:21
 */
public class GoodNoteActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	private ImageButton ib_good_note_return;
	private ListView lv_good_note_list;
	private Map<String, ?> userInfoSp;
	private TextView emptyView, lv_foot_textView;
	private ArrayList<CircleDetailList> circleDetailList;
	private myListAdapter adapter;
	private DrawerLayout mDrawerLayout;
	private int currentNumber = 10;
	private int currentPage = 1;
	private ArrayList<CircleDetailList> totalCircleDetailList;
	private View footview;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_note_list);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		getData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_good_note_dl);
		setEnableDrawerLayout(mDrawerLayout);
		totalCircleDetailList = new ArrayList<CircleDetailList>();
	}

	private void initView() {
		ib_good_note_return.setOnClickListener(this);
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无精华帖");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_good_note_list.getParent()).addView(emptyView);
		lv_good_note_list.setEmptyView(emptyView);
	}

	private void getData() {
		loadDialog.show();
		currentPage = 1;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryHotCircleDetailInfo/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_GOOD_NOTE_URL + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("精华帖url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("精华帖数据:" + response);
							Gson gson = new Gson();
							GoodNoteResult cnr = gson.fromJson(response,
									GoodNoteResult.class);
							totalCircleDetailList.clear();
							UIManager.toggleDialog(loadDialog);
							if (cnr.getResultCode() == 1) {
								circleDetailList = cnr.getCircleDetailList();
								if (circleDetailList != null) {
									totalCircleDetailList
											.addAll(circleDetailList);
									flag = true;
									initData();
									if (circleDetailList.size() < currentNumber) {
										lv_foot_textView
												.setVisibility(View.GONE);
									} else {
										lv_foot_textView.setText("正在加载...");
									}
								} else {
									totalCircleDetailList.clear();
									adapter.notifyDataSetChanged();
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (cnr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (cnr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_good_note_list.getEmptyView() == null) {
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

	@Override
	protected void onRestart() {
		super.onRestart();
		getData();
	}

	private void initData() {
		adapter = new myListAdapter(this);
		lv_good_note_list.setAdapter(adapter);
	}

	private void findView() {
		ib_good_note_return = (ImageButton) findViewById(R.id.ib_good_note_return);
		lv_good_note_list = (ListView) findViewById(R.id.lv_good_note_list);
		footview = LayoutInflater.from(this).inflate(
				R.layout.listview_footview, null);
		lv_foot_textView = (TextView) footview.findViewById(R.id.tv_footview);
		lv_good_note_list.addFooterView(footview);
		lv_good_note_list.setOnScrollListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_good_note_return:
			finish();
			break;

		default:
			break;
		}
	}

	class myListAdapter extends BaseAdapter {

		private Context context;

		public myListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return totalCircleDetailList == null ? 0 : totalCircleDetailList.size();
		}

		@Override
		public Object getItem(int position) {
			return totalCircleDetailList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.activity_circle_detail_listview_item, null);
				holder.tv_circle_list_item_title = (TextView) convertView
						.findViewById(R.id.tv_circle_list_item_title);
				holder.tv_circle_list_item_name = (TextView) convertView
						.findViewById(R.id.tv_circle_list_item_name);
				holder.tv_circle_list_item_comment = (TextView) convertView
						.findViewById(R.id.tv_circle_list_item_comment);
				holder.tv_circle_list_item_time = (TextView) convertView
						.findViewById(R.id.tv_circle_list_item_time);
				holder.iv_circle_list_item_img = (CircleImageView) convertView
						.findViewById(R.id.iv_circle_list_item_img);
				holder.iv_circle_list_item_is_good = (ImageView) convertView
						.findViewById(R.id.iv_circle_list_item_is_good);
				holder.rl_circle_list_item_user = (RelativeLayout) convertView
						.findViewById(R.id.rl_circle_list_item_user);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.rl_circle_list_item_user
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent newIntent = new Intent(
									GoodNoteActivity.this,
									GusterInfoActivity.class);
							newIntent.putExtra("userid",
									totalCircleDetailList.get(position).getUserId());
							startActivity(newIntent);
						}
					});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(GoodNoteActivity.this,
							CircleContentNoteDetailActivity.class);
					newIntent.putExtra("circleId",
							getIntent().getStringExtra("circleId"));
					newIntent.putExtra("infoId", totalCircleDetailList.get(position)
							.getCircleInfoId());
					startActivity(newIntent);
				}
			});
			holder.tv_circle_list_item_title.setText(totalCircleDetailList.get(
					position).getInfoTitle());
			holder.tv_circle_list_item_name.setText(totalCircleDetailList.get(
					position).getUserName());
			holder.tv_circle_list_item_comment.setText(totalCircleDetailList.get(
					position).getReplayNum()
					+ "评论");
			if ((new SimpleDateFormat("MM-dd", Locale.getDefault())
					.format(new Date())).equals(totalCircleDetailList.get(position)
					.getCreateTime().substring(5, 10))) {
				holder.tv_circle_list_item_time.setText("今天"
						+ totalCircleDetailList.get(position).getCreateTime()
								.substring(10));
			} else {
				holder.tv_circle_list_item_time.setText(totalCircleDetailList
						.get(position).getCreateTime().substring(0, 10));
			}
			ImageLoader.getInstance()
					.displayImage(
							Config.BASE_URL
									+ totalCircleDetailList.get(position)
											.getThumbnailPath(),
							holder.iv_circle_list_item_img);
			if (totalCircleDetailList.get(position).getInfoType().equals("1")) {
				holder.iv_circle_list_item_is_good.setVisibility(View.VISIBLE);
			} else {
				holder.iv_circle_list_item_is_good.setVisibility(View.GONE);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_circle_list_item_title, tv_circle_list_item_name,
				tv_circle_list_item_comment, tv_circle_list_item_time;
		ImageView iv_circle_list_item_is_good;
		CircleImageView iv_circle_list_item_img;
		RelativeLayout rl_circle_list_item_user;
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

	private void loadMore() {
		loadDialog.show();
		currentPage++;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryHotCircleDetailInfo/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_GOOD_NOTE_URL + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("精华帖url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("精华帖数据:" + response);
							Gson gson = new Gson();
							GoodNoteResult cnr = gson.fromJson(response,
									GoodNoteResult.class);
							UIManager.toggleDialog(loadDialog);
							if (cnr.getResultCode() == 1) {
								circleDetailList = cnr.getCircleDetailList();
								if (circleDetailList != null) {
									totalCircleDetailList
											.addAll(circleDetailList);
									flag = true;
									adapter.notifyDataSetChanged();
									if (circleDetailList.size() < currentNumber) {
										lv_foot_textView.setText("没有更多相关数据");
									}
								} else {
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (cnr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (cnr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_good_note_list.getEmptyView() == null) {
								setEmptyView();
							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
							flag = false;
						}
					});
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
		}
	}
}
