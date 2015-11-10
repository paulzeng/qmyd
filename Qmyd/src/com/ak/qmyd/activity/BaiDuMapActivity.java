package com.ak.qmyd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class BaiDuMapActivity extends BaseActivity {

	private MapView mMapView = null;

	private BaiduMap mBaiduMap;

	private LocationClient mlocaLocationClient;

	private Context context;

	boolean isfirst = true;

	double Latitude;

	double Longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_baidu_map);
		context = getApplicationContext();
		Intent bundle = getIntent();
		String weidu = bundle.getStringExtra("Latitude");
		Latitude = Double.parseDouble(weidu);
		String jingdu = bundle.getStringExtra("Longitude");
		Longitude = Double.parseDouble(jingdu);
		findView();
	}

	private void findView() {
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// MapStatusUpdate status = MapStatusUpdateFactory.zoomTo(15.0f);
		// mBaiduMap.setMapStatus(status);

		mlocaLocationClient = new LocationClient(context);
		mlocaLocationClient.registerLocationListener(new MyLocationListener());

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);

		mlocaLocationClient.setLocOption(option);

		LatLng lat = new LatLng(Latitude, Longitude);

		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.map_location);

		MarkerOptions op = new MarkerOptions().position(lat).icon(bitmap)
				.zIndex(5);

		mBaiduMap.addOverlay(op);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onStart() {

		super.onStart();

		mBaiduMap.setMyLocationEnabled(true);

		if (!mlocaLocationClient.isStarted()) {
			mlocaLocationClient.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mlocaLocationClient.stop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			MyLocationData data = new MyLocationData.Builder()
					.accuracy(arg0.getRadius()).latitude(arg0.getLatitude())
					.longitude(arg0.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);

			if (isfirst) {

				LatLng latLng = new LatLng(arg0.getLatitude(),
						arg0.getLongitude());

				MapStatusUpdate status = MapStatusUpdateFactory
						.newLatLng(latLng);

				mBaiduMap.animateMapStatus(status);

				isfirst = false;
			}

		}

	}
}
