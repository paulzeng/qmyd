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
	private ListView lv_venues_appointment;// չʾ���б�
	private TextView textView_business_circle, textView_my_location,
			textView_recommended;
	private Context context;
	private BusinessCircleDialog dialog;// ��Ȧ�ĵ�����
	private RecommendedDialog dialog1;// �Ƽ��ĵ�����
	private RequestQueue mRequestQueue;
	private List<VenuesListInformation> totalVenuesList;// �ܼ��ϵ�����
	static VenuesListInformation venuesListInformation;// ���ڱ������ݣ��ڶ���ȷ��֧������
	public static int position;// ��¼BusinessCircleDialog���ĸ���Ŧ��
	private VenuesAppointmentReceiver venuesAppointmentReceiver;
	private String cityName;
	private LocationClient mlocaLocationClient;
	private String myAddr;// �õ���ǰ���ڵ�ַ
	private ImageButton imageButton_back;// �������ذ�Ŧ
	private int currenPage = 1;// �б�ĵ�ǰҳ��;
	private int currenNumber = 5;// ÿҳ���������;
	private String regionalName;// ��Ȧ��Ӧ���ı���,��Ĭ��Ϊ��ɳȫ����,�������б���ʱ��ֵ��cityCodeһ����
	private String cityCode;// ÿ���еı��룬������ѯ����Ĭ��Ϊ��ɳ.
	public static String seqArrangement = "1";// �Ƽ���Ӧ������,Ĭ��Ϊ�����Ƽ���
	private VenuesAppointmentAdapt adapter;
	private boolean flag = false;// ��֤scroll���β�ִ�У�ֱ�������������������ִ��
	private ImageView imageView_search, imageView_refresh;
	private TextView listView_foot_textView;
	private String venuesName = null;
	// private ProgressBar pb_venues_appointment;
	private boolean firstTime;
	private double Latitude, Longitude;// �õ����ȡ�γ��
	private DrawerLayout drawerlayout;

	private String typeId;// �ӿڵ�typeId
	private String hardId;// �ӿڵ�hardId
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
		registerBroadCast();// ע��㲥
		// �нӿں�listview��VenuesAppointmentAdapt������

	}

	private void getCityCode() {
		try {
			List<CityName> cityList = GetXmlData.parseXml(context);
			int m = 0;// �����ж��Ƿ����Լ����ڳ��ж�Ӧ�ı��룬û�о�Ĭ�ϳ�ɳ��
			for (int i = 0; i < cityList.size(); i++) {
				if (cityList.get(i).getName().equals(cityName)) {
					cityCode = cityList.get(i).getCode();
					m++;
				}
			}
			if (m == 0) {
				cityName = "��ɳ��";
				cityCode = "430100";
				regionalName = "430100";
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��ǰ��λ��
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
					textView_my_location.setText("û�л�ȡ����ǰλ��");
				} else {
					textView_my_location.setText(myAddr);
				}
				cityName = arg0.getCity();
				getCityCode();
				if (firstTime) {
					// �Ӹ��ж���Ҫ��getCityCode();������ʱ����ֹ���ִ��
					httpGet();
					firstTime = false;
				}
			}
		});
		mlocaLocationClient.start();
	}

	/**
	 * ע��㲥
	 * */
	private void registerBroadCast() {
		venuesAppointmentReceiver = new VenuesAppointmentReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstUtil.VENUESAPPOINTMENT_ACTION);
		registerReceiver(venuesAppointmentReceiver, filter);
	}

	/**
	 * ����GET����õ�JSON���ݴ浽list������
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
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����

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
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();

								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("û�и����������");
							}
							// listview�ĵ���¼�
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
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * �ؼ��ĳ�ʼ��
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
	 * ����¼�
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
		DebugUtility.showLog("������ĳ���: " + dialogCity);
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
	 * �Ӳ�ѯҳ�淵��
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 2) {
			String searchName = data.getExtras().getString("SearchContent");
			try {
				venuesName = URLEncoder.encode(searchName, "utf-8");
				DebugUtility.showLog("�����ĳ���: " + venuesName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SearchBack();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ��ѯ�����������
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
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����
						try {
							UIManager.toggleDialog(loadDialog);
							DebugUtility.showLog("�������ݷ���: " + response);
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
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("û�и����������");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * ����һ���㲥���ڴӳ��в�ѯ�󷵻ظ�������
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
	 * ���в�ѯ���غ�����б�
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
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����
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
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("û�и����������");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * �������ظ���
	 * */
	public void loadMore() {
		loadDialog.show();
		currenPage++;
		DebugUtility.showLog("��ǰҳ�룺" + currenPage + "��ǰ����" + currenNumber);
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_APPOINTMENT_URL);
		String param = Tools.joinUrlByParam(hardId, typeId, Longitude + "",
				Latitude + "", regionalName, venuesName, seqArrangement,
				currenPage + "", currenNumber + "");
		buffer.append(param);
		DebugUtility.showLog("������" + param);
		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}

		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����
						DebugUtility.showLog("�������ݣ�" + response);
						try {
							UIManager.toggleDialog(loadDialog);
							List<VenuesListInformation> venuesList = JsonUtils
									.parseJson(context, response);
							if (venuesList != null) {
								totalVenuesList.addAll(venuesList);
								flag = true;
								adapter.notifyDataSetChanged();
								if (venuesList.size() < currenNumber) {
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								listView_foot_textView.setText("û�и����������");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
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
	 * �ӿڻص����������Ȧdialog����ֵ�Ĵ��� �ٴ�����������������Ϊ���ж�Ӧ����
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
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����
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
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();

								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("û�и����������");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						UIManager.toggleDialog(loadDialog);
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * �ӿڻص���������Ƽ�dialog����ֵ�Ĵ��� �ٴ�����������������Ϊ�Ƽ�������
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
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����

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
									listView_foot_textView.setText("û�и����������");
								}
							} else {
								totalVenuesList.clear();
								adapter.notifyDataSetChanged();
								// adapter = new VenuesAppointmentAdapt(context,
								// totalVenuesList);
								// listView.setAdapter(adapter);
								listView_foot_textView.setText("û�и����������");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
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
