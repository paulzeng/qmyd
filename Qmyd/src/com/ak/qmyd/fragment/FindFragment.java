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
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DongTaiDetailActivity;
import com.ak.qmyd.activity.adapter.DongTaiListAdapter;
import com.ak.qmyd.activity.adapter.FindPageImageAdapter;
import com.ak.qmyd.activity.base.DongTaiBaseFragment;
import com.ak.qmyd.bean.DongTaiBean;
import com.ak.qmyd.bean.FindUser;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CirclePageIndicator;
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
public class FindFragment extends DongTaiBaseFragment implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener,
		OnRefreshListener<StaggeredGridView> {
	private static final String TAG = "FindFragment";
	private StaggeredGridView mFindGridView;
	private boolean mHasRequestedMore;
	private DongTaiListAdapter mFindAdapter;
	private ArrayList<DongTaiBean> mFindData;
	private ArrayList<FindUser> mUserData;
	private List<DongTaiBean> mNewData;
	private ViewPager bannerViewPager;
	private boolean isLoop = true;
	private View header;
	private Map<String, ?> userInfoSp;
	private TextView emptyView;
	private RequestQueue mRequestQueue;
	private FindPageImageAdapter mPageImageAdapter;
	private Dialog loadDialog;
	private String typeId;
	private static int rows = 1;
	private static int flag = 1;
	/** 下来刷新 **/
	private PullToRefreshStaggeredGridView mPullToRefreshStaggerdGridView;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mUserData != null) {
				if (bannerViewPager.getCurrentItem() == (mUserData.size() - 1)) {
					bannerViewPager.setCurrentItem(0);
				} else {
					bannerViewPager.setCurrentItem(bannerViewPager
							.getCurrentItem() + 1);
				}
			}
		}
	};

	public FindFragment(String typeId) {
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
		return inflater.inflate(R.layout.layout_find, container, false);
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
				.findViewById(R.id.find_pull_grid_view);
		mPullToRefreshStaggerdGridView.setMode(Mode.PULL_FROM_START);
		mPullToRefreshStaggerdGridView.setMode(Mode.BOTH);
		mPullToRefreshStaggerdGridView.setOnRefreshListener(this);
		mFindGridView = mPullToRefreshStaggerdGridView.getRefreshableView();
		mFindData = new ArrayList<DongTaiBean>();
		mFindAdapter = new DongTaiListAdapter(fa, mFindData);
		mFindGridView.setAdapter(mFindAdapter);
		mFindGridView.setOnScrollListener(this);
		mFindGridView.setOnItemClickListener(this);
	}

	void initViews() {
		bannerViewPager = (ViewPager) fa
				.findViewById(R.id.find_banner_viewpager);
		initCircleIndicator();

	}

	protected void initData() {
		mUserData = new ArrayList<FindUser>();
		httpGetJson("1", "10");
	}

	void httpGetJson(String currPage, String size) {
		loadDialog = UIManager.getLoadingDialog(fa);
		loadDialog.show();
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(fa);
		String url = Config.BASE_URL + "/api/rest/circle/queryFindInfo" + "/"
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
							DebugUtility.showLog("获取我的发现失败："
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
				String dongTaiInfoList = JsonManager.getJsonItem(jsonObj,
						"dongTaiInfoList").toString();
				mNewData = new Gson().fromJson(dongTaiInfoList,
						new TypeToken<List<DongTaiBean>>() {
						}.getType());
				if (flag == 1) {
					mFindData.clear();
					rows = 2;
				} else {
					rows++;
				}
				if(mNewData!=null){
					mFindData.addAll(mNewData);
				}
				
				mFindAdapter.notifyDataSetChanged();

				// 推荐的用户
				String findInfoList = JsonManager.getJsonItem(jsonObj,
						"findInfoList").toString();
				mUserData = new Gson().fromJson(findInfoList,
						new TypeToken<List<FindUser>>() {
						}.getType());

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
			if (mUserData != null) {
				mPageImageAdapter = new FindPageImageAdapter(mUserData, fa);
				bannerViewPager.setAdapter(mPageImageAdapter);
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
		((ViewGroup) mFindGridView.getParent()).addView(emptyView);
		mFindGridView.setEmptyView(emptyView);
	}

	// 初始化原点的Indicator
	private void initCircleIndicator() {
		// List<ImageView> images = addItems();
		mPageImageAdapter = new FindPageImageAdapter(mUserData, fa);
		bannerViewPager.setAdapter(mPageImageAdapter);
		CirclePageIndicator mIndicator = (CirclePageIndicator) fa
				.findViewById(R.id.find_banner_indicator);
		mIndicator.setViewPager(bannerViewPager);
		// 自动切换页面功能
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isLoop) {
					SystemClock.sleep(3000);
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
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
		// mData.addAll(sampleData);
		// mAdapter.notifyDataSetChanged();
		// mHasRequestedMore = false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		DebugUtility.showLog("mData = " + mFindData.toString());
		Intent intent = new Intent(fa, DongTaiDetailActivity.class);
		intent.putExtra("id", mFindData.get(position).getMyInfoId());
		intent.putExtra("staffId", mFindData.get(position).getUserId());
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isLoop = false;
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
		}
	}

}