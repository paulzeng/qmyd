package com.ak.qmyd.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CityLocationActivity;
import com.ak.qmyd.adapt.CityListAdapt;
import com.ak.qmyd.bean.AreaList;
import com.ak.qmyd.bean.CityName;
import com.ak.qmyd.bean.ConstUtil;
import com.ak.qmyd.bean.PinyinComparator;
import com.ak.qmyd.bean.SideBar;
import com.ak.qmyd.bean.SideBar.OnTouchingLetterChangedListener;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.GetXmlData;
import com.ak.qmyd.tools.MyListView;
import com.ak.qmyd.bean.CharacterParser;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CityLocationFragment extends Fragment implements OnClickListener {

	private MyListView listView;
	private View view;
	public static List<CityName> list;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private CityListAdapt adapter;
	private Context context;
	private SideBar sideBar;
	private ScrollView scrollView;
	private LinearLayout city_location_height;
	private int headHeight;
	private String cityName;
	private Button btn_shanghai, btn_guangzhou, btn_beijing, btn_shenzhen,
			btn_wuhan, btn_hangzhou, btn_nanjing, btn_changsha, btn_tianjin;
	private boolean firstTime;
	private double Latitude, Longitude;// 得到经度、纬度
	private LocationClient mlocaLocationClient;
	private String myAddr;// 得到当前所在地址
	private String cityCode;// 每个市的编码，用来查询区，默认为长沙.
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		iniData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.city_location_fragment1, null);
		findView();
		return view;
	}

	/**
	 * 初始化数据(测试数据)将得到的集合进行排序和初始化适配器
	 * */
	private void iniData() {
		Bundle bundle = getArguments();
		cityName = bundle.getString("cityName");
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		try {
			list = filledData(GetXmlData.parseXml(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(list, pinyinComparator);
		adapter = new CityListAdapt(list, context);
	}

	/**
	 * 获取当前的位置
	 * */
	private void getMyPosition() {      
        		firstTime=true;
        		mlocaLocationClient = new LocationClient(context);
        		LocationClientOption option = new LocationClientOption();
        		option.setCoorType("bd09ll");
        		option.setIsNeedAddress(true);
        		option.setOpenGps(true);
        		option.setScanSpan(10000);
        		mlocaLocationClient.setLocOption(option);
        		mlocaLocationClient.registerLocationListener(new BDLocationListener() {
        			@Override
        			public void onReceiveLocation(BDLocation arg0) {
        				myAddr = arg0.getAddrStr();
        				Latitude = arg0.getLatitude();
        				Longitude = arg0.getLongitude();	
        		
        				if((Latitude+"").equals("4.9E-324")||(Longitude+"").equals("4.9E-324")){
        					Latitude=0.0;
        					Longitude=0.0;  
        				
        				}
        				cityName = arg0.getCity();
        				DebugUtility.showLog("定位的当前城市: " + cityName);
        				if(firstTime){
        					//加个判断主要是getCityCode();方法耗时，防止多次执行
        					firstTime=false;
        				}			
        			}
        		});
        		mlocaLocationClient.start();
	}

	
	

	/**
	 * 给集合添加数据，元素为cityname对象
	 * */
	private List<CityName> filledData(List<CityName> mlist) {

		List<CityName> mSortList = new ArrayList<CityName>();

		for (int i = 0; i < mlist.size(); i++) {
			CityName cityName = new CityName();
			cityName.setName(mlist.get(i).getName());
			cityName.setCode(mlist.get(i).getCode());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(mlist.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				cityName.setSortLetters(sortString.toUpperCase());
			} else {
				cityName.setSortLetters("#");
			}
			mSortList.add(cityName);
		}
		return mSortList;

	}

	private void findView() {
		btn_shanghai = (Button) view.findViewById(R.id.btn_shanghai);
		btn_guangzhou = (Button) view.findViewById(R.id.btn_guangzhou);
		btn_beijing = (Button) view.findViewById(R.id.btn_beijing);
		btn_shenzhen = (Button) view.findViewById(R.id.btn_shenzhen);
		btn_wuhan = (Button) view.findViewById(R.id.btn_wuhan);
		btn_hangzhou = (Button) view.findViewById(R.id.btn_hangzhou);
		btn_nanjing = (Button) view.findViewById(R.id.btn_nanjing);
		btn_changsha = (Button) view.findViewById(R.id.btn_changsha);
		btn_tianjin = (Button) view.findViewById(R.id.btn_tianjin);

		btn_shanghai.setOnClickListener(this);
		btn_guangzhou.setOnClickListener(this);
		btn_beijing.setOnClickListener(this);
		btn_shenzhen.setOnClickListener(this);
		btn_wuhan.setOnClickListener(this);
		btn_hangzhou.setOnClickListener(this);
		btn_nanjing.setOnClickListener(this);
		btn_changsha.setOnClickListener(this);
		btn_tianjin.setOnClickListener(this);

		TextView textView_current_city = (TextView) view
				.findViewById(R.id.textView_current_city);
		DebugUtility.showLog("设置之前的当前城市: "+cityName);
		textView_current_city.setText(cityName);
		listView = (MyListView) view.findViewById(R.id.listView1);
		sideBar = (SideBar) view.findViewById(R.id.sideBar);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		city_location_height = (LinearLayout) view
				.findViewById(R.id.city_location_height);
		getLinearHeight();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 发送广播，将选择的城市名字传给场管预约界面
				Intent intent = new Intent();
				AreaList area = new AreaList();
				area.setAreaName(list.get(arg2).getName());
				area.setAreaCode(list.get(arg2).getCode());
				Bundle bundle = new Bundle();
				bundle.putSerializable("AreaList", area);
				intent.putExtras(bundle);
				intent.setAction(ConstUtil.VENUESAPPOINTMENT_ACTION);
				getActivity().sendBroadcast(intent);
				getActivity().finish();
			}
		});

		// 字母的点击事件
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int totalHeight = 0;

				int position = adapter.getPositionForSection1(s);

				if (position != -1) {
					for (int i = 0; i < position; i++) {
						View itemView = adapter.getView(i, null, listView);
						itemView.measure(0, 0);
						totalHeight += itemView.getMeasuredHeight()
								+ listView.getDividerHeight();
					}
					// 总高度为listview上面的高度+listview显现的高度
					int moveHeight = totalHeight + headHeight;

					scrollView.scrollTo(0, moveHeight);
				}
			}
		});
	}

	/**
	 * 得到listview上面的高度
	 * */
	private void getLinearHeight() {
		city_location_height.measure(0, 0);
		headHeight = city_location_height.getMeasuredHeight();
	}

	@Override
	public void onClick(View arg0) {
		String city;
		switch (arg0.getId()) {
		case R.id.btn_shanghai:
			city = btn_shanghai.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_guangzhou:
			city = btn_guangzhou.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_beijing:
			city = btn_beijing.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_shenzhen:
			city = btn_shenzhen.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_wuhan:
			city = btn_wuhan.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_hangzhou:
			city = btn_hangzhou.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_nanjing:
			city = btn_nanjing.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_changsha:
			city = btn_changsha.getText().toString();
			sendMessage(city);
			break;
		case R.id.btn_tianjin:
			city = btn_tianjin.getText().toString();
			sendMessage(city);
			break;
		}

	}

	private void sendMessage(String city) {
		String code = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(city)) {
				code = list.get(i).getCode();
			}
		}
		if (code != null) {
			Intent intent = new Intent();
			AreaList area = new AreaList();
			area.setAreaName(city);
			area.setAreaCode(code);
			Bundle bundle = new Bundle();
			bundle.putSerializable("AreaList", area);
			intent.putExtras(bundle);
			intent.setAction(ConstUtil.VENUESAPPOINTMENT_ACTION);
			getActivity().sendBroadcast(intent);
			getActivity().finish();
		} else {
			Toast.makeText(getActivity(), "当前城市编码错误", Toast.LENGTH_SHORT)
					.show();
		}

	}
}
