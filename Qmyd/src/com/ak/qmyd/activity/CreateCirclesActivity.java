package com.ak.qmyd.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.MediaManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CircleImageView;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.upload.MultiPartStack;
import com.android.volley.upload.MultiPartStringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.model.PhotoModel;

/**
 * @author JGB
 * @date 2015-6-9 上午10:41:02
 */
public class CreateCirclesActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageButton ib_create_circles_return;
	private TextView tv_create_circles_build;
	private ImageView iv_create_circle_jia;
	private CircleImageView iv_create_circle_img;
	private TextView tv_create_circle_set, tv_create_circle_location;
	private ContainsEmojiEditText et_create_circle_name, et_create_circle_summary;
	private String circleType;
	private Dialog selectHeadDialog;
	private Bitmap bm;
	private static RequestQueue mSingleQueue;
	private Map<String, ?> userInfoSp;
	private String name;
	private String summary;
	private String type;
	private Object obj;
	private DrawerLayout mDrawerLayout;
	public LocationClient mLocationClient;// 定位SDK的核心类
	public MyLocationListener mMyLocationListener;// 定义监听类
	private String pointX, pointY, publishPlace, publishContent;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_circles);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		findView();
		initView();
		initLocation();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_create_circle_dl);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置高精度定位定位模式
		option.setCoorType("bd09ll");// 设置百度经纬度坐标系格式
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);// 反编译获得具体位置，只有网络定位才可以
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	protected void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());// 获得当前时间
			sb.append("\nerror code : ");
			sb.append(location.getLocType());// 获得erro code得知定位现状
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());// 获得纬度
			pointY = location.getLatitude() + "";
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());// 获得经度
			pointX = location.getLongitude() + "";
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// 通过GPS定位
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 获得速度
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());// 获得当前地址
				sb.append(location.getDirection());// 获得方位
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 通过网络连接定位
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());// 获得当前地址
				tv_create_circle_location.setText(location.getAddrStr());
				publishPlace = location.getAddrStr();
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());// 获得经营商？
			}
		}
	}

	private void findView() {
		ib_create_circles_return = (ImageButton) findViewById(R.id.ib_create_circles_return);
		tv_create_circles_build = (TextView) findViewById(R.id.tv_create_circles_build);
		iv_create_circle_jia = (ImageView) findViewById(R.id.iv_create_circle_jia);
		iv_create_circle_img = (CircleImageView) findViewById(R.id.iv_create_circle_img);
		tv_create_circle_set = (TextView) findViewById(R.id.tv_create_circle_set);
		tv_create_circle_location = (TextView) findViewById(R.id.tv_create_circle_location);
		et_create_circle_name = (ContainsEmojiEditText) findViewById(R.id.et_create_circle_name);
		et_create_circle_summary = (ContainsEmojiEditText) findViewById(R.id.et_create_circle_summary);
		if (getIntent().getStringExtra("isComCircleDetail") != null
				&& getIntent().getStringExtra("isComCircleDetail").equals(
						"true")) {
			tv_create_circles_build.setText("修改");
		}

		et_create_circle_name.addTextChangedListener(new TextWatcher() {
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				cou = before + count;
				String editable = et_create_circle_name.getText().toString();
				String str = StringUtil.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_create_circle_name.setText(str);
					// 设置新的光标所在位置
				}
				et_create_circle_name.setSelection(et_create_circle_name.length());
				cou = et_create_circle_name.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionEnd = et_create_circle_name.getSelectionEnd();
				if (cou > 12) {
					s.delete(12, selectionEnd);
					et_create_circle_name.setText(s);
					et_create_circle_name.setSelection(et_create_circle_name
							.length());// 设置光标在最后
					showToast("圈子名称应控制在12字以内");
				}
			}
		});

		et_create_circle_summary.addTextChangedListener(new TextWatcher() {
			private int selectionEnd;
			private int cou = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = et_create_circle_summary.getText().toString();
				String str = StringUtil.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_create_circle_summary.setText(str);
					// 设置新的光标所在位置
				}
				et_create_circle_summary.setSelection(et_create_circle_summary.length());
				cou = et_create_circle_summary.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionEnd = et_create_circle_summary.getSelectionEnd();
				if (cou > 150) {
					s.delete(150, selectionEnd);
					et_create_circle_summary.setText(s);
					et_create_circle_summary
							.setSelection(et_create_circle_summary.length());// 设置光标在最后
					showToast("圈子简介应控制在150字以内");
				}

			}
		});
	}

	private void initView() {
		ib_create_circles_return.setOnClickListener(this);
		tv_create_circles_build.setOnClickListener(this);
		iv_create_circle_jia.setOnClickListener(this);
		// tv_create_circle_type.setOnClickListener(this);
		if (getIntent().getStringExtra("circleName") != null) {
			et_create_circle_name.setText(getIntent().getStringExtra(
					"circleName"));
			et_create_circle_summary.setText(getIntent().getStringExtra(
					"circleNotice"));
			iv_create_circle_img.setVisibility(View.VISIBLE);
			tv_create_circle_set.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL
							+ getIntent().getStringExtra("thumbnailPath"),
					iv_create_circle_img);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_create_circles_return:
			finish();
			break;
		case R.id.tv_create_circles_build:
			isCreateMyCircles();
			break;
		case R.id.iv_create_circle_jia:
			View headselectview = View.inflate(this,
					R.layout.layout_action_sheet_select_userhead, null);
			selectHeadDialog = UIManager.getActionSheet(this, headselectview);
			selectHeadDialog.show();
			break;

		case R.id.action_sheet_take_photo:
			MediaManager.getPhotoFromCamera(this);
			selectHeadDialog.dismiss();
			break;

		case R.id.action_sheet_pick_photo:
			MediaManager.getPhotoFromAlbum(this);
			selectHeadDialog.dismiss();
			break;

		case R.id.tv_create_circle_location:
			Intent i = new Intent(CreateCirclesActivity.this,
					SportTypeDialogActivity.class);
			// i.putExtra("circleType",
			// tv_create_circle_type.getText().toString());
			startActivityForResult(i, 0);
			break;
		case R.id.action_sheet_cancle:
			showToast("取消");
			break;
		default:
			break;
		}
	}

	private void isCreateMyCircles() {
		name = et_create_circle_name.getText().toString().replace(" ", "");;
		// type = tv_create_circle_type.getText().toString();
		summary = et_create_circle_summary.getText().toString().replace(" ", "");;
		if (iv_create_circle_img.getVisibility() == View.GONE) {
			showToast("请添加圈子图片");
			return;
		}
		if (name == null || name.length() == 0) {
			et_create_circle_name.requestFocus();
			showToast("请输入圈子名称");
			return;
		}
		// if (type == null || type.length() == 0 || type == "") {
		// showToast("请选择圈子类型");
		// }
		if (publishPlace != null) {
			try {
				publishPlace = URLEncoder.encode(publishPlace, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			publishPlace = "null";
		}

		if (summary == null || summary.length() == 0) {
			et_create_circle_summary.requestFocus();
			showToast("请输入圈子简介");
			return;
		}
		if (getIntent().getStringExtra("circleName") != null) {
			if (obj == null) {
				try {
					iv_create_circle_img.setDrawingCacheEnabled(true);
					File file = Tools.getFileFromBytes(Tools
							.Bitmap2Bytes(Bitmap
									.createBitmap(iv_create_circle_img
											.getDrawingCache())), Environment
							.getExternalStorageDirectory().getPath() + "/qmty");
					iv_create_circle_img.setDrawingCacheEnabled(false);
					changMyCircles(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				changMyCircles((File) obj);
			}
		} else {
			createMyCircles((File) obj);
		}
	}

	private void changMyCircles(File file) {
		// rest/circle/updateCircleObject/userId/sessionId/hardId/circleId/publishPlace/pointX/pointY/circleName/typeId/circleNotice/verify
		Map<String, File> files = new HashMap<String, File>();
		files.put("body", file);

		Map<String, String> params = new HashMap<String, String>();
		params.put("token", "DJrlPbpJQs21rv1lP41yiA==");
		String uri = Config.CHANGE_CIRCLE_URL + "/"
				+ userInfoSp.get("userId").toString() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ getIntent().getStringExtra("circleId") + "/" + publishPlace
				+ "/" + pointX + "/" + pointY + "/" + name + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "0")
				+ "/" + summary + "/" + "0";
		DebugUtility.showLog("修改圈子url" + uri);
		addPutUploadFileRequest(uri, files, params, mResonseListenerString,
				mErrorListener, null);
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		switch (resultCode) {
		case 100:
			MediaManager.onActivityTypeResult(this, handler, requestCode,
					resultCode, data);
			break;
		case RESULT_OK:
			MediaManager.onActivityResult(this, handler, requestCode,
					resultCode, data);
			break;
		default:
			break;
		}
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.SHOW_PHOTO: {
				FileInputStream fis;
				try {
					fis = new FileInputStream((File) msg.obj);
					bm = BitmapFactory.decodeStream(fis);
					obj = msg.obj;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				if (bm == null) {

				} else {
					iv_create_circle_img.setVisibility(View.VISIBLE);
					tv_create_circle_set.setVisibility(View.GONE);
					iv_create_circle_img.setImageBitmap(bm);
				}

				break;
			}
			case 100:
				Bundle bundle = ((Intent) msg.obj).getExtras();
				tv_create_circle_location.setText(bundle
						.getString("selectedType"));
				break;
			}
		}
	};

	private void createMyCircles(File file) {
		loadDialog.show();
		// rest/circle/addCircleObject/userId/sessionId/hardId/publishPlace/pointX/pointY/circleName/typeId/circleNotice/verify
		Map<String, File> files = new HashMap<String, File>();
		files.put("body", file);

		Map<String, String> params = new HashMap<String, String>();
		params.put("token", "DJrlPbpJQs21rv1lP41yiA==");
		String uri = Config.CREATE_CIRCLE_URL + "/"
				+ userInfoSp.get("userId").toString() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ MyApplication.instance.getHardId() + "/" + publishPlace + "/"
				+ pointX + "/" + pointY + "/" + name + "/"
				+ getSharedPreferences("config", 0).getString("typeId", "0")
				+ "/" + summary + "/" + "0";
		DebugUtility.showLog("创建圈子url" + uri);
		addPutUploadFileRequest(uri, files, params, mResonseListenerString,
				mErrorListener, null);
	}

	Listener<String> mResonseListenerString = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			DebugUtility.showLog(" on response String" + response.toString());
			parseRespense(response);
		}
	};

	ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (error != null) {
				if (error.networkResponse != null)
					DebugUtility.showLog(" error "
							+ new String(error.networkResponse.data));
				UIManager.toggleDialog(loadDialog);
			}
		}
	};

	public static void addPutUploadFileRequest(final String url,
			final Map<String, File> files, final Map<String, String> params,
			final Listener<String> responseListener,
			final ErrorListener errorListener, final Object tag) {
		if (null == url || null == responseListener) {
			return;
		}

		MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
				Request.Method.POST, url, responseListener, errorListener) {

			@Override
			public Map<String, File> getFileUploads() {
				return files;
			}

			@Override
			public Map<String, String> getStringUploads() {
				return params;
			}

		};
		DebugUtility.showLog(" volley put : uploadFile " + url);

		mSingleQueue.add(multiPartRequest);
	}

	protected void parseRespense(String response) {

		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			UIManager.toggleDialog(loadDialog);
			if (resultCode.equals("1")) {
				if (getIntent().getStringExtra("circleName") != null) {
					showToast("修改圈子成功");
				} else {
					showToast("创建圈子成功");
				}
				finish();
			} else if (resultCode.equals("0")) {
				showToast("操作失败");
			} else if (resultCode.equals("8")) {
				showToast("请上传图片");
			} else if (resultCode.equals("10000")) {
				showToast("未登陆或登陆超时，请重新登陆");
				changeLogin();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
