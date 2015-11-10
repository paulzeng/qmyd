package com.ak.qmyd.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ImageGridviewAdapter;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.ToastManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.upload.MultiPartStack;
import com.android.volley.upload.MultiPartStringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoDelPreviewActivity;
import com.photoselector.ui.PhotoSelectorActivity;
import com.photoselector.util.CommonUtils;

@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
public class SendDynamicActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ImageButton imgBtnBack;
	private TextView ib_submit;
	private ContainsEmojiEditText edt_send;
	private GridView mGridview;
	private ArrayList<HashMap<String, Object>> imageItem;// ����ͼƬ���� 
	private String pathImage; // ѡ��ͼƬ·��
	private ImageGridviewAdapter simpleAdapter; // ������
	private final int IMAGE_OPEN = 1; // ��ͼƬ���
	private TextView tv_location_, tv_location;
	private String pointX, pointY, publishPlace, publishContent;
	private ArrayList<PhotoModel> photos;// ԭʼ����Դ
	private ArrayList<PhotoModel> selectPhotos;// ɾ���������Դ
	private ArrayList<PhotoModel> delBackPhotos;// ɾ���������Դ
	private PhotoModel takePhotoModel;

	public LocationClient mLocationClient;// ��λSDK�ĺ�����
	public MyLocationListener mMyLocationListener;// ���������

	private Map<String, Object> userInfoSp;
	private Dialog loadDialog;
	private static RequestQueue mSingleQueue;
	private Dialog selectHeadDialog;
	private static final int REQUEST_CAMERA = 2;
	private int mMaxLenth = 200;// ��������������ַ�����

	private String typeId;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				InputMethodManager inputManager = (InputMethodManager) edt_send
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_send, 0);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_dynamic);
		typeId = getSharedPreferences("config", 0)
				.getString("typeId", "1");
		userInfoSp = MyApplication.instance.getUserInfo();
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		initView();
		initLocation();
		mHandler.sendEmptyMessageDelayed(0, 500);
	}

	@SuppressLint("WrongViewCast")
	void initView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		imgBtnBack.setOnClickListener(this);

		ib_submit = (TextView) this.findViewById(R.id.ib_submit);
		ib_submit.setOnClickListener(this);

		tv_location = (TextView) this.findViewById(R.id.tv_location);
		tv_location.setOnClickListener(this);
		tv_location_ = (TextView) this.findViewById(R.id.tv_location_);

		edt_send = (ContainsEmojiEditText) this.findViewById(R.id.edt_send);
		edt_send.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = edt_send.getText().toString();
				String str = StringUtil.stringFilter(editable); // ���������ַ�
				if (!editable.equals(str)) {
					edt_send.setText(str);
				}
				edt_send.setSelection(edt_send.length());
				cou = edt_send.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = edt_send.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
					ToastManager.show(SendDynamicActivity.this,
							"�������ݲ��ܳ���200���ַ�");
				}
			}
		});

		mGridview = (GridView) this.findViewById(R.id.grid_sendimgs);
		// ���ӼӺ�ͼƬ
		imageItem = new ArrayList<HashMap<String, Object>>(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		String imageUri = "drawable://" + R.drawable.add_img;
		map.put("itemImage", imageUri);
		imageItem.add(map);
		simpleAdapter = new ImageGridviewAdapter(this, imageItem);
		mGridview.setAdapter(simpleAdapter);
		mGridview.setOnItemClickListener(this);
		photos = new ArrayList<PhotoModel>();
		selectPhotos = new ArrayList<PhotoModel>();
		delBackPhotos = new ArrayList<PhotoModel>();
	}

	void initLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ø߾��ȶ�λ��λģʽ
		option.setCoorType("bd09ll");// ���ðٶȾ�γ������ϵ��ʽ
		option.setScanSpan(1000);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setIsNeedAddress(true);// �������þ���λ�ã�ֻ�����綨λ�ſ���
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	void refreshGirdview(List<PhotoModel> photos) {
		imageItem.clear();
		for (PhotoModel photo : photos) {
			pathImage = "file://" + photo.getOriginalPath();
			DebugUtility.showLog("ѡ�����Ƭ��" + pathImage);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", pathImage);
			imageItem.add(map);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		String imageUri = "drawable://" + R.drawable.add_img;
		map.put("itemImage", imageUri);
		imageItem.add(map);
		// simpleAdapter = new ImageGridviewAdapter(this, imageItem);
		// mGridview.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
		// ˢ�º��ͷŷ�ֹ�ֻ����ߺ��Զ����
		pathImage = null;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.action_sheet_take_photo: {
			CommonUtils.launchActivityForResult(this, new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
			selectHeadDialog.dismiss();
			break;
		}
		case R.id.tv_location: {
			if (publishPlace != null && !publishPlace.equals("")) {
				tv_location_.setText(publishPlace);
			} else {
				tv_location_.setText("�޷���ȡ��ǰ�û�����λ��");
			}
			break;
		}
		case R.id.action_sheet_pick_photo: {
			// MediaManager.getPhotoFromAlbum(this);
			Intent intent = new Intent(SendDynamicActivity.this,
					PhotoSelectorActivity.class);
			intent.putExtra(PhotoSelectorActivity.KEY_MAX, 8);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivityForResult(intent, 1);
			selectHeadDialog.dismiss();
			break;
		}
		case R.id.imgBtnBack: 
			finish();
			break;
		case R.id.ib_submit:
			if (photos == null || photos.size() == 0) {
				showToast("��ѡ��ͼƬ����̬");
			} else {
				publishContent = edt_send.getText().toString().replace(" ", "");
				try {
					if (publishContent != null && !publishContent.equals("")) {
						publishContent = URLEncoder.encode(publishContent,
								"utf-8");
					} else {
						publishContent = "null";
					}
					if (publishPlace != null) {
						publishPlace = URLEncoder.encode(publishPlace, "utf-8");
					} else {
						publishPlace = "null";
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<String, File> files = new HashMap<String, File>();
				for (PhotoModel photo : photos) {
					File f = new File(photo.getOriginalPath());
					files.put(photo.getOriginalPath(), f);
				}
				httpPostJson(files, publishContent, pointX, pointY,
						publishPlace);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		if (position == imageItem.size() - 1) { // ���ͼƬλ��Ϊ+ 0��Ӧ0��ͼƬ
			View headselectview = View.inflate(this,
					R.layout.layout_action_sheet_select_userhead, null);
			selectHeadDialog = UIManager.getActionSheet(this, headselectview);
			selectHeadDialog.show();

		} else {
			Intent intent = new Intent(this, PhotoDelPreviewActivity.class);
			intent.putExtra("photos", photos);
			intent.putExtra("position", position);
			intent.putExtra("from", 1);
			startActivityForResult(intent, 0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_OPEN) {// selected // image
			if (data != null && data.getExtras() != null) {
				selectPhotos = (ArrayList<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				if (selectPhotos == null || selectPhotos.isEmpty()) {
					DebugUtility.showLog("û��ѡ���κ���Ƭ");
				} else {
					photos.addAll(selectPhotos);
					refreshGirdview(photos);
				}
			}
		} else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String name = timeStamp + ".jpg";
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
			FileOutputStream b = null;
			// Ϊʲô����ֱ�ӱ�����ϵͳ���λ����
			String savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File file = new File(savePath, "qmyd");
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = file.getAbsolutePath() + name;
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			DebugUtility.showLog("####���պ󷵻�#####" + fileName);
			takePhotoModel = new PhotoModel(fileName);
			photos.add(takePhotoModel);
			refreshGirdview(photos);
		} else if (requestCode == 0) {
			if (data != null && data.getExtras() != null) {
				delBackPhotos = (ArrayList<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				DebugUtility.showLog("ɾ������Ƭ���ϣ�" + delBackPhotos.toString());
				photos.clear();
				photos.addAll(delBackPhotos);
				refreshGirdview(photos);
			}
		}
	}

	void httpPostJson(Map<String, File> files, String publishContent,
			String pointX, String pointY, String publishPlace) {
		loadDialog = UIManager.getLoadingDialog(this);
		loadDialog.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "image/jpeg");

		String uri = Config.BASE_URL + "/api/rest/circle/addDongTaiInfo/"
				+ userInfoSp.get("userId").toString() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ MyApplication.instance.getHardId() + "/" + publishContent
				+ "/" + pointX + "/" + pointY + "/" + publishPlace + "/"
				+ typeId;
		addPutUploadFileRequest(uri, files, params, mResonseListenerString,
				mErrorListener, null);
	}

	@Override
	public void onBackPressed() {
		mHandler.removeMessages(0);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(0);
		mLocationClient.stop();
		super.onDestroy();
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());// ��õ�ǰʱ��
			sb.append("\nerror code : ");
			sb.append(location.getLocType());// ���erro code��֪��λ��״
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());// ���γ��
			pointY = location.getLatitude() + "";
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());// ��þ���
			pointX = location.getLongitude() + "";
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// ͨ��GPS��λ
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// ����ٶ�
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());// ��õ�ǰ��ַ
				sb.append(location.getDirection());// ��÷�λ
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ͨ���������Ӷ�λ
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());// ��õ�ǰ��ַ
				publishPlace = location.getAddrStr();
				// ��Ӫ����Ϣ
				sb.append("\noperationers : ");
				sb.append(location.getOperators());// ��þ�Ӫ�̣�
			}
			Log.i("DEBUG", sb.toString());
		}
	}

	public OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
			} else {
				hint = textView.getTag().toString();
				textView.setHint(hint);
			}
		}
	};

	private void parseRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			if (resultCode.equals("1")) {
				showToast("����ɹ�");
				Intent intent = new Intent(this, DynamicMainActivity.class);
				this.startActivityForResult(intent, 0);
				finish();
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "��ȡ��Ϣ�쳣");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "δ��½���½��ʱ�������µ�½");
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("flag", "1");
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Listener<String> mResonseListenerString = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			DebugUtility.showLog(" on response String" + response.toString());
			parseRespense(response);
			UIManager.toggleDialog(loadDialog);
		}
	};

	ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (error != null) {
				if (error.networkResponse != null)
					DebugUtility.showLog(" error "
							+ new String(error.networkResponse.data));
			}
			UIManager.toggleDialog(loadDialog);
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

}
