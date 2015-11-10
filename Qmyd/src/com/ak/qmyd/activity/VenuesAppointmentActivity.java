package com.ak.qmyd.activity;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.VenuesAppointmentAdapt;
import com.ak.qmyd.bean.AreaList;
import com.ak.qmyd.bean.CityName;
import com.ak.qmyd.bean.ConstUtil;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.dialog.BusinessCircleDialog;
import com.ak.qmyd.dialog.RecommendedDialog;
import com.ak.qmyd.dialog.BusinessCircleDialog.GridViewClick;
import com.ak.qmyd.dialog.RecommendedDialog.RecommendedClick;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.GetXmlData;
import com.ak.qmyd.tools.JsonUtils;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

public class VenuesAppointmentActivity extends BaseFragmentActivity implements
		OnClickListener, GridViewClick, RecommendedClick, OnScrollListener {
	private ListView lv_venues_appointment;// 展示的列表
	private TextView textView_business_circle, textView_my_location,
			textView_recommended;
	private Context context;
	private BusinessCircleDialog dialog;// 商圈的弹出框
	private RecommendedDialog dialog1;// 推荐的弹出框
	private RequestQueue mRequestQueue;
	private List<VenuesListInformation> totalVenuesList;// 总集合的内容
	static VenuesListInformation venuesListInformation;// 用于保存数据，在定单确认支付有用
	public static int position;// 记录BusinessCircleDialog是哪个按纽；
	private VenuesAppointmentReceiver venuesAppointmentReceiver;
	private String cityName;
	private LocationClient mlocaLocationClient;
	private String myAddr;// 得到当前所在地址
	private ImageButton imageButton_back;// 顶部返回按纽
	private int currenPage = 1;// 列表的当前页码;
	private int currenNumber = 5;// 每页请求的条数;
	private String regionalName;// 商圈对应区的编码,先默认为长沙全城市,当代表市编码时，值和cityCode一样；
	private String cityCode;// 每个市的编码，用来查询区，默认为长沙.
	public static String seqArrangement = "1";// 推荐对应的排序,默认为热门推荐；
	private VenuesAppointmentAdapt adapter;
	private boolean flag = false;// 保证scroll初次不执行，直到网络数据请求完成在执行
	private ImageView imageView_search, imageView_refresh;
	private TextView listView_foot_textView;
	private String venuesName = null;
	// private ProgressBar pb_venues_appointment;
	private boolean firstTime;
	private double Latitude, Longitude;// 得到经度、纬度
	private DrawerLayout drawerlayout;

	private String typeId;// 接口的typeId
	private String hardId;// 接口的hardId
	private String searchCityName;
	private Dialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_venues_appointment);
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(context);
		typeId = getIntent().getExtras().getString("typeId");
		hardId = MyApplication.instance.getHardId();

		findView();
		GetMyPosition();
		position = 0;
		registerBroadCast();// 注册广播
		// 有接口后listview用VenuesAppointmentAdapt适配器

	}

	private void getCityCode() {
		try {
			List<CityName> cityList = GetXmlData.parseXml(context);
			int m = 0;// 用来判断是否有自己所在城市对应的编码，没有就默认长沙；
			for (int i = 0; i < cityList.size(); i++) {
				if (cityList.get(i).getName().equals(cityName)) {
					cityCode = cityList.get(i).getCode();
					m++;
				}
			}
			if (m == 0) {
				cityName = "长沙市";
				cityCode = "430100";
				regionalName = "430100";
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取当前的位置
	 * */
	private void GetMyPosition() {
		firstTime = true;
		mlocaLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mlocaLocationClient.setLocOption(option);
		mlocaLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				myAddr = arg0.getAddrStr();
				Latitude = arg0.getLatitude();
				Longitude = arg0.getLongitude();

				if ((Latitude + "").equals("4.9E-324")
						|| (Longitude + "").equals("4.9E-324")) {
					Latitude = 0.0;
					Longitude = 0.0;

				}
				if (myAddr == null) {
					textView_my_location.setText("没有获取到当前位置");
				} else {
					textView_my_location.setText(myAddr);
				}
				cityName = arg0.getCity();
				getCityCode();
				if (firstTime) {
					// 加个判断主要是getCityCode();方法耗时，防止多次执行
					httpGet();
					firstTime = false;
				}
			}
		});
		mlocaLocationClient.start();
	}

	/**
	 * 注册广播
	 * */
	private void registerBroadCast() {
		venuesAppointmentReceiver = new VenuesAppointmentReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstUtil.VENUESAPPOINTMENT_ACTION);
		registerReceiver(venuesAppointmentReceiver, filter);
	}

	/**
	 * 网络GET请求得到JSON数据存到list集合里
	 * */
	private void httpGet() {
		// loadDialog.show();
		currenPage = 1;
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);

		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);

		totalVenuesList = new ArrayList<VenuesListInformation>();
		if (mlocaLocationClient != null) {
			mlocaLocationClient.stop();
		}
		;

		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据

						try {
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							imageView_refresh.setClickable(true);
							UIManager.toggleDialog(loadDialog);
							// pb_venues_appointment.setVisibility(View.GONE);
							lv_venues_appointment.setVisibility(View.VISIBLE);

							if (venuesList != null) {

								totalVenuesList.addAll(venuesList);
								flag = true;
								adapter = new VenuesAppointmentAdapt(context,
										totalVenuesList);
								lv_venues_appointment.setAdapter(adapter);
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();

								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("没有更多相关数据");
							}
							// listview的点击事件
							lv_venues_appointment
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int position, long arg3) {
											if (totalVenuesList != null
													&& totalVenuesList.size() != 0) {
												if (totalVenuesList.size() != position) {
													venuesListInformation = totalVenuesList
															.get(position);
													Intent intent = new Intent(
															VenuesAppointmentActivity.this,
															VenuesDetailsActivity.class);
													intent.putExtra(
															"venuesId",
															totalVenuesList
																	.get(position)
																	.getVenuesId());
													intent.putExtra("typeId",
															typeId);
													intent.putExtra(
															"venuesThumbnail",
															totalVenuesList
																	.get(position)
																	.getVenuesThumbnail());
													intent.putExtra(
															"supportId",
															totalVenuesList
																	.get(position)
																	.getSupportId());
													startActivity(intent);
												}
											}
										}
									});
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * 控件的初始化
	 * */
	private void findView() {
		textView_my_location = (TextView) this
				.findViewById(R.id.textView_my_location);
		textView_business_circle = (TextView) this
				.findViewById(R.id.textView_business_circle);
		textView_recommended = (TextView) this
				.findViewById(R.id.textView_recommended);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		textView_business_circle.setOnClickListener(this);
		textView_recommended.setOnClickListener(this);
		lv_venues_appointment = (ListView) findViewById(R.id.lv_venues_appointment);

		imageButton_back.setOnClickListener(this);
		lv_venues_appointment.setOnScrollListener(this);
		View view = LayoutInflater.from(context).inflate(
				R.layout.listview_footview, null);
		lv_venues_appointment.addFooterView(view);
		listView_foot_textView = (TextView) view.findViewById(R.id.tv_footview);
		imageView_search = (ImageView) this.findViewById(R.id.imageView_search);
		imageView_refresh = (ImageView) this
				.findViewById(R.id.imageView_refresh);
		// pb_venues_appointment=(ProgressBar)this.findViewById(R.id.pb_venues_appointment);
		// pb_venues_appointment.setVisibility(View.VISIBLE);
		loadDialog.show();
		lv_venues_appointment.setVisibility(View.GONE);
		imageView_search.setOnClickListener(this);
		imageView_refresh.setOnClickListener(this);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);

	}

	/**
	 * 
	 * 点击事件
	 * */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.textView_business_circle:
			businessCircleDialog();
			break;
		case R.id.textView_recommended:
			recommendedDialog();
			break;
		case R.id.imageButton_back:
			backSuperiorPage();
			break;
		case R.id.imageView_search:
			skipSearchActivity();
			break;
		case R.id.imageView_refresh:
			imageView_refresh.setClickable(false);
			// pb_venues_appointment.setVisibility(View.VISIBLE);
			loadDialog.show();
			lv_venues_appointment.setVisibility(View.GONE);
			GetMyPosition();
			break;
		}
	}

	private void businessCircleDialog() {
		String dialogCity = (searchCityName == null ? cityName : searchCityName);
		DebugUtility.showLog("弹出框的城市: " + dialogCity);
		dialog = new BusinessCircleDialog(VenuesAppointmentActivity.this,
				R.style.dialog, dialogCity, cityCode, cityName);
		dialog.setGridViewClick(this);
		dialog.show();
	}

	private void recommendedDialog() {
		dialog1 = new RecommendedDialog(VenuesAppointmentActivity.this,
				R.style.dialog);
		dialog1.setRecommendedClick(this);
		dialog1.show();
	}

	private void skipSearchActivity() {
		Intent intent = new Intent(VenuesAppointmentActivity.this,
				VenuesSearchActivity.class);
		startActivityForResult(intent, 1);
	}

	private void backSuperiorPage() {
		startActivity(HomeActivity.class, null);
		finish();
	}

	/**
	 * 从查询页面返回
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 2) {
			String searchName = data.getExtras().getString("SearchContent");
			try {
				venuesName = URLEncoder.encode(searchName, "utf-8");
				DebugUtility.showLog("搜索的场馆: " + venuesName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SearchBack();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 查询后的网络请求
	 * */

	private void SearchBack() {
		loadDialog.show();
		currenPage = 1;
		// pb_venues_appointment.setVisibility(View.VISIBLE);
		lv_venues_appointment.setVisibility(View.GONE);
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);

		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						try {
							UIManager.toggleDialog(loadDialog);
							DebugUtility.showLog("搜索数据返回: " + response);
							// pb_venues_appointment.setVisibility(View.GONE);
							lv_venues_appointment.setVisibility(View.VISIBLE);
							totalVenuesList.clear();
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("没有更多相关数据");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * 建立一个广播用于从城市查询后返回更新数据
	 * */
	public class VenuesAppointmentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			AreaList area = (AreaList) arg1.getSerializableExtra("AreaList");
			searchCityName = area.getAreaName();
			cityCode = area.getAreaCode();
			// pb_venues_appointment.setVisibility(View.VISIBLE);
			lv_venues_appointment.setVisibility(View.GONE);
			httpSearchCity(cityCode);
			position = 0;
		}
	}

	/**
	 * 城市查询返回后更新列表
	 * */
	private void httpSearchCity(String code) {
		loadDialog.show();
		currenPage = 1;
		regionalName = code;
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						try {
							UIManager.toggleDialog(loadDialog);
							totalVenuesList.clear();
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							// pb_venues_appointment.setVisibility(View.GONE);
							lv_venues_appointment.setVisibility(View.VISIBLE);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("没有更多相关数据");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * 上拉加载更多
	 * */
	public void loadMore() {
		loadDialog.show();
		currenPage++;
		DebugUtility.showLog("当前页码：" + currenPage + "当前条数" + currenNumber);
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);
		DebugUtility.showLog("参数：" + param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}

		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						DebugUtility.showLog("返回数据：" + response);
						try {
							UIManager.toggleDialog(loadDialog);
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								listView_foot_textView.setText("没有更多相关数据");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						flag = true;
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if (flag) {
			DebugUtility.showLog(arg1 + "..." + arg2 + "..." + arg3);
			if ((arg1 + arg2) == (arg3)) {
				flag = false;
				loadMore();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	/**
	 * 接口回调，处理从商圈dialog传回值的处理 再次网络请求，限制条件为城市对应的区
	 * */
	@Override
	public void buttonClick(AreaList areaList) {
		currenPage = 1;
		venuesName = null;
		// pb_venues_appointment.setVisibility(View.VISIBLE);
		loadDialog.show();
		lv_venues_appointment.setVisibility(View.GONE);
		regionalName = areaList.getAreaCode();
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						try {
							// pb_venues_appointment.setVisibility(View.GONE);
							UIManager.toggleDialog(loadDialog);
							lv_venues_appointment.setVisibility(View.VISIBLE);
							totalVenuesList.clear();
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();

								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("没有更多相关数据");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * 接口回调，处理从推荐dialog传回值的处理 再次网络请求，限制条件为推荐的排序
	 * */
	@Override
	public void RecommendClick() {
		currenPage = 1;
		venuesName = null;
		// pb_venues_appointment.setVisibility(View.VISIBLE);
		loadDialog.show();
		lv_venues_appointment.setVisibility(View.GONE);
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据

						try {
							UIManager.toggleDialog(loadDialog);
							// pb_venues_appointment.setVisibility(View.GONE);
							lv_venues_appointment.setVisibility(View.VISIBLE);
							totalVenuesList.clear();
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("没有更多相关数据");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("没有更多相关数据");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backSuperiorPage();
		}
		return false;
	}

	@Override
	public void buttonClick(int type) {
		switch (type) {
		case 0:
			backSuperiorPage();
			break;
		case 1:
			skipSearchActivity();
			break;
		case 2:
			recommendedDialog();
			break;
		default:
			break;
		}

	}

	@Override
	public void RecommendClick(int type) {
		switch (type) {
		case 0:
			backSuperiorPage();
			break;
		case 1:
			skipSearchActivity();
			break;
		case 2:
			businessCircleDialog();
			break;
		default:
			break;
		}
	}
}
