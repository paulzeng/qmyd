package com.ak.qmyd.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CircleActivity;
import com.ak.qmyd.activity.CircleContentNoteDetailActivity;
import com.ak.qmyd.activity.adapter.QuanziListAdapter;
import com.ak.qmyd.activity.base.DongTaiBaseFragment;
import com.ak.qmyd.bean.QuanZiBean;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.PullToRefreshStaggeredGridView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

@SuppressLint("NewApi")
public class QuanZiFragment extends DongTaiBaseFragment implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener,
		OnRefreshListener<StaggeredGridView> {
	private static final String TAG = "QuanZiFragment";
	private StaggeredGridView mGridView;
	private boolean mHasRequestedMore;
	private QuanziListAdapter mAdapter;
	private ArrayList<QuanZiBean> mData;
	private List<QuanZiBean> mNewData;
	private TextView tv_circle_title;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private RequestQueue mRequestQueue;
	private Dialog loadDialog;
	private String typeId;
	private static int rows = 1;
	private static int flag = 1;
	/** 下来刷新 **/
	private PullToRefreshStaggeredGridView mPullToRefreshStaggerdGridView;

	public QuanZiFragment(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_quanzi, container, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPullToRefresh();
		initData();
		initViews();

	}

	void initPullToRefresh() {
		mPullToRefreshStaggerdGridView = (PullToRefreshStaggeredGridView) fa
				.findViewById(R.id.qz_pull_grid_view);
		mPullToRefreshStaggerdGridView.setMode(Mode.PULL_FROM_START);
		mPullToRefreshStaggerdGridView.setMode(Mode.BOTH);
		mPullToRefreshStaggerdGridView.setOnRefreshListener(this);
		mGridView = mPullToRefreshStaggerdGridView.getRefreshableView();
		mData = new ArrayList<QuanZiBean>();
		mAdapter = new QuanziListAdapter(fa, mData);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);
	}

	protected void initData() {
		mData = new ArrayList<QuanZiBean>();
		httpGetJson("1", "10");
	}

	void initViews() {
		tv_circle_title = (TextView) getView().findViewById(
				R.id.tv_circle_title);
		tv_circle_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(fa, CircleActivity.class);
				startActivity(newIntent);
			}
		});
	}

	private void httpGetJson(String currPage, String size) {
		loadDialog = UIManager.getLoadingDialog(fa);
		loadDialog.show();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(fa);
		String url = Config.BASE_URL + "/api/rest/circle/queryCircleInfo" + "/"
				+ userInfoSp.get("userId") + "/" + userInfoSp.get("sessionId")
				+ "/" + MyApplication.instance.getHardId() + "/" + currPage
				+ "/" + size + "/" + typeId;
		DebugUtility.showLog("请求URL:" + url);
		if (NetManager.isNetworkConnected(fa)) {
			StringRequest request = new StringRequest(Request.Method.GET, url,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							parseRespense(response);
							UIManager.toggleDialog(loadDialog);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							DebugUtility.showLog("获取我的圈子失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog);
							Toast.makeText(fa, "获取数据失败", 1 * 1000).show();
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					// TODO Auto-generated method stub
					return super.getParams();
				}
			};
			mRequestQueue.add(request);
		} else {
			Toast.makeText(fa, "网络不可用，请检查网络设置", 1 * 1000).show();
		}
	}

	void parseRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				String circleInfoList = JsonManager.getJsonItem(jsonObj,
						"circleInfoList").toString();
				mNewData = new Gson().fromJson(circleInfoList,
						new TypeToken<List<QuanZiBean>>() {
						}.getType());
				if (flag == 1) {
					mData.clear();
					rows = 2;
				} else {
					rows++;
				}
				mData.addAll(mNewData);
//				mAdapter.notifyDataSetChanged();
				mAdapter = new QuanziListAdapter(fa, mData);
				mGridView.setAdapter(mAdapter);
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				if (flag == 1) {
					// ToastManager.show(fa, "暂无数据");
				} else {
					ToastManager.show(fa, "已经加载全部动态");
				}
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
			}
			if (mPullToRefreshStaggerdGridView.isRefreshing()) {
				mPullToRefreshStaggerdGridView.onRefreshComplete();
			}
			setEmptyView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setEmptyView() {
		emptyView = new TextView(fa);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("暂无消息");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) mGridView.getParent()).addView(emptyView);
		mGridView.setEmptyView(emptyView);
	}

	@Override
	public void onScrollStateChanged(final AbsListView view,
			final int scrollState) {
		Log.d(TAG, "onScrollStateChanged:" + scrollState);
	}

	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem,
			final int visibleItemCount, final int totalItemCount) {
		Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem
				+ " visibleItemCount:" + visibleItemCount + " totalItemCount:"
				+ totalItemCount);
		// our handling
		if (!mHasRequestedMore) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				onLoadMoreItems();
			}
		}
	}

	private void onLoadMoreItems() {
		// 加载更多

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent newIntent = new Intent(fa, CircleContentNoteDetailActivity.class);
		newIntent.putExtra("circleId", mData.get(position).getCircleId());
		newIntent.putExtra("infoId", mData.get(position).getCircleInfoId());
		startActivity(newIntent);
	}

	@Override
	public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
		// TODO Auto-generated method stub
		if (refreshView.isHeaderShown()) {
			httpGetJson("1", "10");
			flag = 1;
		} else {
			flag = 2;
			DebugUtility.showLog("上啦加载更多");
			httpGetJson(rows + "", "10");
			// if (mNewData.size() >= 10) {
			// httpGetJson(rows + "", "10");
			// }
		}
	}
}
