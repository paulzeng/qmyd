package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.CircleMemberEntity;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.result.CircleDetailResult;
import com.ak.qmyd.bean.result.CircleMemberResult;
import com.ak.qmyd.bean.result.CircleMemberResult.CircleUserList;
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
 * @date 2015-6-10 下午1:49:21
 */
public class CircleMemberActivity extends BaseFragmentActivity implements
		OnClickListener, OnScrollListener {

	private ListView lv_circle_member_list;
	private ImageButton ib_circle_member_return;
	private myListAdapter adapter;
	private Map<String, ?> userInfoSp;
	private TextView emptyView, lv_foot_textView;
	private ArrayList<CircleUserList> circleUserList;
	private DrawerLayout mDrawerLayout;
	private ArrayList<CircleUserList> totalCircleUserList;
	private int currentNumber = 10;
	private int currentPage = 1;
	private View footview;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_member);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		findView();
		getData();
		initView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_circle_member_dl);
		setEnableDrawerLayout(mDrawerLayout);
		totalCircleUserList = new ArrayList<CircleUserList>();
	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无成员");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_circle_member_list.getParent()).addView(emptyView);
		lv_circle_member_list.setEmptyView(emptyView);
	}

	private void getData() {
		loadDialog.show();
		currentPage = 1;
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/queryCirclePersonList/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_MEMBER_LIST_URL + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("圈子成员url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("圈子成员数据:" + response);
							Gson gson = new Gson();
							CircleMemberResult cmr = gson.fromJson(response,
									CircleMemberResult.class);
							totalCircleUserList.clear();
							UIManager.toggleDialog(loadDialog);
							if (cmr.getResultCode() == 1) {
								circleUserList = cmr.getCircleUserList();
								if (circleUserList != null) {
									totalCircleUserList.addAll(circleUserList);
									initData();
									flag = true;
									if (circleUserList.size() < currentNumber) {
										lv_foot_textView
												.setVisibility(View.GONE);
									} else {
										lv_foot_textView.setText("正在加载...");
									}
								} else {
									totalCircleUserList.clear();
									adapter.notifyDataSetChanged();
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (cmr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (cmr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_circle_member_list.getEmptyView() == null) {
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
		adapter = new myListAdapter(this);
		lv_circle_member_list.setAdapter(adapter);
	}

	private void findView() {
		lv_circle_member_list = (ListView) findViewById(R.id.lv_circle_member_list);
		ib_circle_member_return = (ImageButton) findViewById(R.id.ib_circle_member_return);
		footview = LayoutInflater.from(this).inflate(
				R.layout.listview_footview, null);
		lv_foot_textView = (TextView) footview.findViewById(R.id.tv_footview);
		lv_circle_member_list.addFooterView(footview);
		lv_circle_member_list.setOnScrollListener(this);
	}

	private void initView() {
		ib_circle_member_return.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_circle_member_return:
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

			return totalCircleUserList == null ? 0 : totalCircleUserList.size();
		}

		@Override
		public Object getItem(int position) {

			return totalCircleUserList.get(position);
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
						R.layout.activity_circle_member_listitem, null);
				holder.iv_circle_member_item_img = (CircleImageView) convertView
						.findViewById(R.id.iv_circle_member_item_img);
				holder.tv_circle_member_item_name = (TextView) convertView
						.findViewById(R.id.tv_circle_member_item_name);
				holder.tv_circle_member_item_depict = (TextView) convertView
						.findViewById(R.id.tv_circle_member_item_depict);
				holder.tv_circle_member_item_focus = (TextView) convertView
						.findViewById(R.id.tv_circle_member_item_focus);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent newIntent = new Intent(CircleMemberActivity.this,
							GusterInfoActivity.class);
					newIntent.putExtra("userid",
							totalCircleUserList.get(position).getUserId());
					startActivity(newIntent);
				}
			});
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ totalCircleUserList.get(position)
									.getThumbnailPath(),
					holder.iv_circle_member_item_img);
			holder.tv_circle_member_item_name.setText(totalCircleUserList.get(
					position).getUserName());
			holder.tv_circle_member_item_depict.setText(totalCircleUserList
					.get(position).getDescirption());
			if (totalCircleUserList.get(position).getState().equals("0")) {
				holder.tv_circle_member_item_focus.setVisibility(View.GONE);
			} else {
				if (totalCircleUserList.get(position).getState().equals("2")) {
					holder.tv_circle_member_item_focus.setText("关注");
				} else {
					holder.tv_circle_member_item_focus.setText("取消关注");
				}
			}
			holder.tv_circle_member_item_focus
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							focusOrCancelFocus(totalCircleUserList
									.get(position).getUserId(),
									totalCircleUserList.get(position)
											.getState());
						}
					});
			return convertView;
		}
	}

	private void focusOrCancelFocus(final String herUserId, final String state) {
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		// rest/circle/addOrDelRelationPerson
		if (NetManager.isNetworkConnected(this)) {
			StringRequest strRequest = new StringRequest(Request.Method.POST,
					Config.FOCUS_OR_CANCEL_FOCUS_URL, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("关注或取消关注数据:" + response);
							Gson gson = new Gson();
							Result r = gson.fromJson(response, Result.class);
							if (r.getResultCode() == 1) {
								getData();
								if (state.equals("2")) {
									showToast("关注成功");
								} else {
									showToast("已取消关注");
								}
							} else if (r.getResultCode() == 0) {
								showToast("操作失败");
							} else if (r.getResultCode() == 2) {
								showToast("不能重复关注或取消");
							} else if (r.getResultCode() == 10000) {
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_circle_member_list.getEmptyView() == null) {
								setEmptyView();
							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("获取数据失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("userId", (String) userInfoSp.get("userId"));
					map.put("staffId", herUserId);
					map.put("sessionId", (String) userInfoSp.get("sessionId"));
					map.put("hardId", MyApplication.instance.getHardId());
					map.put("state", state);
					return map;
				}

			};
			queue.add(strRequest);// 加入到通讯队列中
		} else {
			showToast("网络不可用，请检查网络设置");
		}
	}

	static class ViewHolder {
		CircleImageView iv_circle_member_item_img;
		TextView tv_circle_member_item_name, tv_circle_member_item_depict,
				tv_circle_member_item_focus;
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
		// rest/circle/queryCirclePersonList/userId/circleId/sessionId/hardId/currPage/size
		String url = Config.CIRCLE_MEMBER_LIST_URL + "/"
				+ userInfoSp.get("userId") + "/"
				+ getIntent().getStringExtra("circleId") + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId() + "/" + currentPage + "/"
				+ currentNumber;
		if (NetManager.isNetworkConnected(this)) {
			DebugUtility.showLog("圈子成员url:" + url);
			StringRequest strRequest = new StringRequest(Request.Method.GET,
					url, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// 通讯成功的 返回数据
							DebugUtility.showLog("圈子成员数据:" + response);
							Gson gson = new Gson();
							CircleMemberResult cmr = gson.fromJson(response,
									CircleMemberResult.class);
							UIManager.toggleDialog(loadDialog);
							if (cmr.getResultCode() == 1) {
								circleUserList = cmr.getCircleUserList();
								if (circleUserList != null) {
									totalCircleUserList.addAll(circleUserList);
									flag = true;
									adapter.notifyDataSetChanged();
									if (circleUserList.size() < currentNumber) {
										lv_foot_textView.setText("没有更多相关数据");
									}
								} else {
									lv_foot_textView.setText("没有更多相关数据");
								}
							} else if (cmr.getResultCode() == 0) {
								flag = false;
								showToast("操作失败");
							} else if (cmr.getResultCode() == 10000) {
								flag = false;
								showToast("未登陆或登陆超时，请重新登陆");
								changeLogin();
							}
							if (lv_circle_member_list.getEmptyView() == null) {
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
