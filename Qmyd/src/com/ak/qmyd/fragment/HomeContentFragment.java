package com.ak.qmyd.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.DongTaiDetailActivity;
import com.ak.qmyd.activity.DynamicMainActivity;
import com.ak.qmyd.activity.HuodongActivity;
import com.ak.qmyd.activity.LoginActivity;
import com.ak.qmyd.activity.TrainingListActivity;
import com.ak.qmyd.activity.TrainingListDatailActivity;
import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.activity.adapter.ContentAdapter;
import com.ak.qmyd.activity.adapter.HuoDongContentAdapter;
import com.ak.qmyd.activity.base.BaseFragment;
import com.ak.qmyd.bean.model.Train;
import com.ak.qmyd.bean.model.Venues;
import com.ak.qmyd.bean.model.VenuesActivity;
import com.ak.qmyd.bean.result.HomeResult;
import com.ak.qmyd.bean.result.HomeResult.VenuesEventList;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.view.CirclePageIndicator;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author HM
 * @date 2015-4-21 下午3:04:21
 */
@SuppressLint("ValidFragment")
public class HomeContentFragment extends BaseFragment implements
		OnClickListener {
	// private View itemsView;
	private LayoutInflater inflater;
	private ViewPager vp_cg_viewPager, vp_huodong_viewPager;
	private HomeResult hr;
	private LinearLayout mLayout;
	private ProgressBar progressBar;
	private String typeId;
	private Context context;
	private ScrollView sl_home_content;

	private Handler cg_handler = new Handler();
	private Handler hd_handler = new Handler();
	
	private Map<String, ?> userInfoSp;
	private double latitude;
	private double longitude;
	private SharedPreferences sp;
	private SharedPreferences.Editor edit;
	private List<HomeResult.DongTai> dongTaiList;

	public HomeContentFragment(String id) {

		typeId = id;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.view_home_content_scroll, null);
		context = getActivity();
		BitmapUtils.initImageLoader(context);
		this.inflater = inflater;
		findViewByIds(view);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		sp = context.getSharedPreferences("config", 0);
		edit = sp.edit();
		get();
		return view;
	}

	private void findViewByIds(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.content_layout);
		// progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
	}

	private void get() {
		getXY();
		// home/type/table/{hardId}/{sessionId}/{typeId}/{pointX}/{pointY}
		RequestQueue queue = Volley.newRequestQueue(context);
		String sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
				: (String) userInfoSp.get("sessionId"));
		String url = Config.API_URL + "admin/home/type/table" + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ typeId + "/" + longitude + "/" + latitude;

		DebugUtility.showLog("首页内容url " + url);
		StringRequest tableRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							Gson gson = new Gson();
							hr = gson.fromJson(response, HomeResult.class);
							DebugUtility.showLog("首页内容信息：" + response);
							if (hr.getResultCode() == 1) {
								addDataNotifyDataSetChanged();
							}
						} catch (Exception e) {
							// showToast(e.getMessage());
						}
						// hideLoading();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// hideLoading();
						showToast("获取数据失败");
					}
				});

		queue.add(tableRequest);// 加入到通讯队列中
	}

	private void addDataNotifyDataSetChanged() {
		addTranView();// 首页-Fragment添加训练的View
		addVenuesView();// 首页-Fragment添加场馆的View
		addHuoDongView();
		addDongTaiView();
	}

	// 首页-Fragment添加训练的View
	private void addTranView() {
		final Train train = hr.getTrainObject();
		final String trainid = train.getTrainId();

		DebugUtility.showLog("训练:" + train.toString());
		View trainView = inflater.inflate(
				R.layout.view_home_content_scroll_items_xl, null);
		trainView.findViewById(R.id.layout_xl_top).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (trainid != null) {
							startActivity(TrainingListActivity.class, typeId);
						} else {
							showToast("训练内容正在制作，敬请期待");
						}
					}
				});
		ImageView item_img = (ImageView) trainView.findViewById(R.id.imageView);
		TextView tvTitle = (TextView) trainView
				.findViewById(R.id.tv_traninlist_item_title);
		TextView tvDirector = (TextView) trainView
				.findViewById(R.id.tv_traninlist_item_director);
		TextView tvType = (TextView) trainView
				.findViewById(R.id.tv_traninlist_item_type);
		if (trainid == null) {
			tvTitle.setText("训练内容正在制作，敬请期待");
		} else {
			tvTitle.setText(train.getTrainName());
		}

		if (trainid == null) {
			tvDirector.setText("");
		} else {
			tvDirector.setText("制定者:" + train.getTrainPerson());
		}
		if (trainid == null) {
			tvType.setText("");
		} else {
			tvType.setText("难度:" + train.getTrainDifficulty());
		}
		if (trainid != null) {
			ImageLoader.getInstance().displayImage(train.getTrainImage(),
					item_img);
		}

		mLayout.addView(trainView);
		edit.putString("trainid", trainid);
		edit.commit();
		item_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DebugUtility.showLog("trainid=" + trainid);
				if (trainid == null) {
					showToast("训练内容正在制作，敬请期待");
				} else {
					Intent newIntent = new Intent(getActivity(),
							TrainingListDatailActivity.class);
					newIntent.putExtra("typeId", typeId);
					newIntent.putExtra("trainID", train.getTrainId());
					startActivity(newIntent);
				}
			}
		});
	}

	// 首页-Fragment添加场馆的View
	private void addVenuesView() {
		List<Venues> venues = hr.getVenuesArray();
		View homeCgView = inflater.inflate(
				R.layout.view_home_content_scroll_items_cg, null);
		homeCgView.findViewById(R.id.layout_cg_top).setOnClickListener(this);
		vp_cg_viewPager = (ViewPager) homeCgView
				.findViewById(R.id.vp_cg_viewPager);
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < venues.size(); i++) {
			View v = inflater.inflate(
					R.layout.view_fragment_content_venues_item, null);
			views.add(v);
			ImageView venuesImg = (ImageView) v
					.findViewById(R.id.iv_venues_item_img);
			TextView venuesTitle = (TextView) v
					.findViewById(R.id.tv_venues_item_title);
			TextView venuesDirector = (TextView) v
					.findViewById(R.id.tv_venues_item_director);
			Venues venue = venues.get(i);
			venuesTitle.setText(venue.getVenuesName());
			venuesDirector.setText(venue.getVenuesAddr());
			ImageLoader.getInstance().displayImage(venue.getVenuesThumbnail(),
					venuesImg);
			List<VenuesActivity> venuesActivity = venue.getActivityList();
			LinearLayout yhLayout = (LinearLayout) v
					.findViewById(R.id.layout_yh);
			for (int j = 0; j < venuesActivity.size(); j++) {
				TextView tvYh = (TextView) inflater.inflate(
						R.layout.view_text_home_yh, null);
				yhLayout.addView(tvYh);
			}
		}
		mLayout.addView(homeCgView);
		vp_cg_viewPager.setAdapter(new ContentAdapter(views, context, venues,
				typeId));

		CirclePageIndicator circleIndicator = (CirclePageIndicator) homeCgView
				.findViewById(R.id.circle_indicator);
		circleIndicator.setViewPager(vp_cg_viewPager);

		final int length = venues.size() - 1;

		cg_handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (vp_cg_viewPager.getCurrentItem() == length) {
					vp_cg_viewPager.setCurrentItem(0);
				} else {
					vp_cg_viewPager.setCurrentItem(vp_cg_viewPager
							.getCurrentItem() + 1);
				}
				cg_handler.postDelayed(this, 3000);
			}
		}, 3000);

	}

	// 首页-Fragment添加活动的View
	private void addHuoDongView() {
		List<VenuesEventList> venuesEventList = hr.getVenuesEventList();
		View huodongView = inflater.inflate(
				R.layout.view_home_content_scroll_items_huodong, null);
		huodongView.findViewById(R.id.ll_huodong_top).setOnClickListener(this);
		vp_huodong_viewPager = (ViewPager) huodongView
				.findViewById(R.id.vp_huodong_viewPager);
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < venuesEventList.size(); i++) {
			View v = inflater.inflate(
					R.layout.view_fragment_content_huodong_item, null);
			views.add(v);
			ImageView iv_huodong_item_img = (ImageView) v
					.findViewById(R.id.iv_huodong_item_img);
			TextView tv_huodong_item_title = (TextView) v
					.findViewById(R.id.tv_huodong_item_title);
			TextView tv_huodong_item_time = (TextView) v
					.findViewById(R.id.tv_huodong_item_time);
			VenuesEventList venuesEventListItem = venuesEventList.get(i);
			tv_huodong_item_title.setText(venuesEventListItem.getEventName());
			tv_huodong_item_time.setText(venuesEventListItem
					.getEventBeginTime()
					+ "-"
					+ venuesEventListItem.getEventEndTime());
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + venuesEventListItem.getEventPic(),
					iv_huodong_item_img);
		}
		mLayout.addView(huodongView);
		
		vp_huodong_viewPager.setAdapter(new HuoDongContentAdapter(views, context, venuesEventList,
				typeId));

		CirclePageIndicator circleIndicator = (CirclePageIndicator) huodongView
				.findViewById(R.id.circle_huodong_indicator);
		circleIndicator.setViewPager(vp_huodong_viewPager);

		final int length = venuesEventList.size() - 1;

		hd_handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (vp_huodong_viewPager.getCurrentItem() == length) {
					vp_huodong_viewPager.setCurrentItem(0);
				} else {
					vp_huodong_viewPager.setCurrentItem(vp_huodong_viewPager
							.getCurrentItem() + 1);
				}
				hd_handler.postDelayed(this, 3000);
			}
		}, 3000);
	}

	// 首页-Fragment添加动态的View
	private void addDongTaiView() {
		View view = inflater.inflate(
				R.layout.view_home_content_scroll_items_bottom, null);
		ImageView imageView01 = (ImageView) view.findViewById(R.id.imageView01);
		imageView01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!MyApplication.instance.getUserInfo().isEmpty()) {
					Intent intent = new Intent(context,
							DongTaiDetailActivity.class);
					intent.putExtra("id", dongTaiList.get(0).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(0).getUserId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
					intent.putExtra("flag", 3);
					intent.putExtra("id", dongTaiList.get(0).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(0).getUserId());
					getActivity().finish();
				}
			}
		});
		ImageView imageView02 = (ImageView) view.findViewById(R.id.imageView02);
		imageView02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!MyApplication.instance.getUserInfo().isEmpty()) {
					Intent intent = new Intent(context,
							DongTaiDetailActivity.class);
					intent.putExtra("id", dongTaiList.get(1).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(1).getUserId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
					intent.putExtra("flag", 3);
					intent.putExtra("id", dongTaiList.get(1).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(1).getUserId());
					getActivity().finish();
				}
			}
		});
		ImageView imageView03 = (ImageView) view.findViewById(R.id.imageView03);
		imageView03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!MyApplication.instance.getUserInfo().isEmpty()) {
					Intent intent = new Intent(context,
							DongTaiDetailActivity.class);
					intent.putExtra("id", dongTaiList.get(2).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(2).getUserId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
					intent.putExtra("flag", 3);
					intent.putExtra("id", dongTaiList.get(2).getMyInfoId());
					intent.putExtra("staffId", dongTaiList.get(2).getUserId());
					getActivity().finish();
				}
			}
		});
		dongTaiList = hr.getDongTaiList();
		if (dongTaiList.size() == 3) {
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(0).getImagePath(),
					imageView01);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(1).getImagePath(),
					imageView02);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(2).getImagePath(),
					imageView03);
		} else if (dongTaiList.size() == 2) {
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(0).getImagePath(),
					imageView01);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(1).getImagePath(),
					imageView02);
		} else if (dongTaiList.size() == 1) {
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + dongTaiList.get(0).getImagePath(),
					imageView01);
		}

		mLayout.addView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!MyApplication.instance.getUserInfo().isEmpty()) {
					Intent intent = new Intent(context,
							DynamicMainActivity.class);
					intent.putExtra("typeId", typeId);
					startActivity(intent);
					getActivity().finish();
				} else {
					Intent intent = new Intent(context, LoginActivity.class);
					intent.putExtra("flag", 1);
					startActivity(intent);
					getActivity().finish();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_cg_top:
			startActivity(VenuesAppointmentActivity.class, typeId);
			break;
		case R.id.ll_huodong_top:
			startActivity(HuodongActivity.class, typeId);
			break;
		}
	}

	// private void hideLoading() {
	// progressBar.setVisibility(View.GONE);
	// }

	private void getXY() {
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				DebugUtility.showLog("latitude=" + latitude + "|"
						+ "longitude=" + longitude);
			} else {
				LocationListener locationListener = new LocationListener() {

					// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {

					}

					// Provider被enable时触发此函数，比如GPS被打开
					@Override
					public void onProviderEnabled(String provider) {

					}

					// Provider被disable时触发此函数，比如GPS被关闭
					@Override
					public void onProviderDisabled(String provider) {

					}

					// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
					@Override
					public void onLocationChanged(Location location) {
						if (location != null) {
							latitude = location.getLatitude(); // 经度
							longitude = location.getLongitude(); // 纬度
							DebugUtility.showLog("latitude=" + latitude + "|"
									+ "longitude=" + longitude);
						}
					}
				};
				locationManager
						.requestLocationUpdates(LocationManager.GPS_PROVIDER,
								1000, 0, locationListener);
				Location location1 = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location1 != null) {
					latitude = location1.getLatitude(); // 经度
					longitude = location1.getLongitude(); // 纬度
					DebugUtility.showLog("latitude=" + latitude + "|"
							+ "longitude=" + longitude);
				}
			}
		}
	}

	public String getTypeId() {
		return typeId;
	}

	// 取消fragment的预加载
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setUserVisibleHint(false);
		super.onActivityCreated(savedInstanceState);
	}

	private boolean mHasLoadedOnce = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			// we check that the fragment is becoming visible
			if (isVisibleToUser && !mHasLoadedOnce) {

				// async http request here
				mHasLoadedOnce = true;
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
}
