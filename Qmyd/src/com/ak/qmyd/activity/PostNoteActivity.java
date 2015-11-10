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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ImageGridviewAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.BitmapUtils;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.StringUtil;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.ContainsEmojiEditText;
import com.ak.qmyd.view.MyEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.android.volley.upload.MultiPartStack;
import com.android.volley.upload.MultiPartStringRequest;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoDelPreviewActivity;
import com.photoselector.ui.PhotoSelectorActivity;
import com.photoselector.util.CommonUtils;

/**
 * @author JGB
 * @date 2015-6-9 下午9:45:02
 */
public class PostNoteActivity extends BaseFragmentActivity implements
		OnClickListener, OnItemClickListener {

	private ImageButton ib_post_note_return;
	private TextView tv_post_note_complete;
	private ContainsEmojiEditText et_post_note_title, et_post_note_content;
	private GridView gv_post_note_gridview;
	private ArrayList<HashMap<String, Object>> imageItem;
	private ArrayList<HashMap<String, Object>> selectImageItem;
	private String pathImage; // 选择图片路径
	private ImageGridviewAdapter simpleAdapter; // 适配器
	private final int IMAGE_OPEN = 1; // 打开图片标记
	private String title;
	private String content;
	private Map<String, ?> userInfoSp;
	private ArrayList<PhotoModel> photos;
	private static RequestQueue mSingleQueue;
	private DrawerLayout mDrawerLayout;
	private ArrayList<PhotoModel> selectPhotos;
	private ArrayList<PhotoModel> delBackPhotos;// 删除后的数据源
	private Dialog selectHeadDialog;
	private static final int REQUEST_CAMERA = 2;
	private PhotoModel takePhotoModel;
	private Dialog loadDialog;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				InputMethodManager inputManager1 = (InputMethodManager) et_post_note_title
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager1.showSoftInput(et_post_note_title, 0);
				InputMethodManager inputManager2 = (InputMethodManager) et_post_note_content
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager2.showSoftInput(et_post_note_content, 0);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_note);
		loadDialog = UIManager.getLoadingDialog(this);
		BitmapUtils.initImageLoader(getApplicationContext());
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		findView();
		initView();
		initGridView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_post_note_dl);
		setEnableDrawerLayout(mDrawerLayout);
		mHandler.sendEmptyMessageDelayed(0, 500);
	}

	private void initGridView() {
		// 增加加号图片
		imageItem = new ArrayList<HashMap<String, Object>>();
		selectImageItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String imageUri = "drawable://" + R.drawable.add_img;
		map.put("itemImage", imageUri);
		imageItem.add(map);
		simpleAdapter = new ImageGridviewAdapter(this, imageItem);
		gv_post_note_gridview.setAdapter(simpleAdapter);
		gv_post_note_gridview.setOnItemClickListener(this);
		photos = new ArrayList<PhotoModel>();
		selectPhotos = new ArrayList<PhotoModel>();

	}

	private void initView() {
		ib_post_note_return.setOnClickListener(this);
		tv_post_note_complete.setOnClickListener(this);
	}

	private void findView() {
		ib_post_note_return = (ImageButton) findViewById(R.id.ib_post_note_return);
		tv_post_note_complete = (TextView) findViewById(R.id.tv_post_note_complete);
		et_post_note_title = (ContainsEmojiEditText) findViewById(R.id.et_post_note_title);
		et_post_note_content = (ContainsEmojiEditText) findViewById(R.id.et_post_note_content);
		gv_post_note_gridview = (GridView) findViewById(R.id.gv_post_note_gridview);
		et_post_note_title.setImeOptions(EditorInfo.TYPE_CLASS_TEXT);
		et_post_note_title.addTextChangedListener(new TextWatcher() {
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = et_post_note_title.getText().toString();
				String str = StringUtil.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_post_note_title.setText(str);
					// 设置新的光标所在位置
				}
				et_post_note_title.setSelection(et_post_note_title.length());
				cou = et_post_note_title.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionEnd = et_post_note_title.getSelectionEnd();
				if (cou > 18) {
					s.delete(18, selectionEnd);
					et_post_note_title.setText(s);
					et_post_note_title.setSelection(et_post_note_title.length());// 设置光标在最后
					showToast("帖子标题应控制在18字以内");
				}
			}
		});
		et_post_note_content.addTextChangedListener(new TextWatcher() {
			private int selectionEnd;
			private int cou = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				cou = before + count;
				String editable = et_post_note_content.getText().toString();
				String str = StringUtil.stringFilter(editable.toString());
				if (!editable.equals(str)) {
					et_post_note_content.setText(str);
					// 设置新的光标所在位置
				}
				et_post_note_content.setSelection(et_post_note_content.length());
				cou = et_post_note_content.length();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionEnd = et_post_note_content.getSelectionEnd();
				if (cou > 150) {
					s.delete(150, selectionEnd);
					et_post_note_content.setText(s);
					et_post_note_content.setSelection(et_post_note_content
							.length());// 设置光标在最后
					showToast("帖子内容应控制在150字以内");
				}
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_OPEN) {// selected // image
			if (data != null && data.getExtras() != null) {
				selectPhotos = (ArrayList<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				if (selectPhotos == null || selectPhotos.isEmpty()) {
					DebugUtility.showLog("没有选择任何照片");
				} else {
					photos.addAll(selectPhotos);
					refreshGirdview(photos);
				}
			}
		} else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String name = timeStamp + ".jpg";
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			FileOutputStream b = null;
			// 为什么不能直接保存在系统相册位置呢
			String savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File file = new File(savePath, "qmyd");
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = file.getAbsolutePath() + name;
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
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
			DebugUtility.showLog("####拍照后返回#####" + fileName);
			takePhotoModel = new PhotoModel(fileName);
			photos.add(takePhotoModel);
			refreshGirdview(photos);
		} else if (requestCode == 0) {
			// if (data != null && data.getExtras() != null) {
			// photos = (ArrayList<PhotoModel>) data.getExtras()
			// .getSerializable("photos");
			// DebugUtility.showLog("删除后照片集合：" + photos.toString());
			// if (photos == null || photos.isEmpty() || photos.size() == 0) {
			// imageItem.clear();
			// HashMap<String, Object> map = new HashMap<String, Object>();
			// String imageUri = "drawable://" + R.drawable.add_img;
			// map.put("itemImage", imageUri);
			// imageItem.add(map);
			// simpleAdapter = new ImageGridviewAdapter(this, imageItem);
			// gv_post_note_gridview.setAdapter(simpleAdapter);
			// selectImageItem.clear();
			// selectPhotos.clear();
			// } else {
			// selectPhotos.clear();
			// imageItem.clear();
			// selectImageItem.clear();
			// for (PhotoModel photo : photos) {
			// pathImage = "file://" + photo.getOriginalPath();
			// DebugUtility.showLog("选择的照片：" + pathImage);
			// HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("itemImage", pathImage);
			// imageItem.add(map);
			// }
			// selectPhotos = photos;
			// selectImageItem = imageItem;
			// HashMap<String, Object> map = new HashMap<String, Object>();
			// String imageUri = "drawable://" + R.drawable.add_img;
			// map.put("itemImage", imageUri);
			// imageItem.add(map);
			// simpleAdapter = new ImageGridviewAdapter(this, imageItem);
			// gv_post_note_gridview.setAdapter(simpleAdapter);
			// }
			// }
			if (data != null && data.getExtras() != null) {
				delBackPhotos = (ArrayList<PhotoModel>) data.getExtras()
						.getSerializable("photos");
				DebugUtility.showLog("删除后照片集合：" + delBackPhotos.toString());
				photos.clear();
				photos.addAll(delBackPhotos);
				refreshGirdview(photos);
			}
		}
	}

	private void refreshGirdview(List<PhotoModel> photos) {
		imageItem.clear();
		for (PhotoModel photo : photos) {
			pathImage = "file://" + photo.getOriginalPath();
			DebugUtility.showLog("选择的照片：" + pathImage);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", pathImage);
			imageItem.add(map);
		}
		// imageItem.addAll(selectImageItem);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String imageUri = "drawable://" + R.drawable.add_img;
		map.put("itemImage", imageUri);
		imageItem.add(map);
		// simpleAdapter = new ImageGridviewAdapter(this, imageItem);
		// gv_post_note_gridview.setAdapter(simpleAdapter);
		simpleAdapter.notifyDataSetChanged();
		// 刷新后释放防止手机休眠后自动添加
		pathImage = null;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_sheet_take_photo: {
			CommonUtils.launchActivityForResult(this, new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
			selectHeadDialog.dismiss();
		}
			break;
		case R.id.action_sheet_pick_photo: {
			// MediaManager.getPhotoFromAlbum(this);
			Intent intent = new Intent(PostNoteActivity.this,
					PhotoSelectorActivity.class);
			intent.putExtra(PhotoSelectorActivity.KEY_MAX, 8);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivityForResult(intent, 1);
			selectHeadDialog.dismiss();
		}
			break;
		case R.id.ib_post_note_return:
			finish();
			break;
		case R.id.tv_post_note_complete:
			postNote();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(0);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		mHandler.removeMessages(0);
		super.onBackPressed();
	}

	private void postNote() {
		title = et_post_note_title.getText().toString().replace(" ", "");
		content = et_post_note_content.getText().toString().replace(" ", "");
		if (title == null || title.length() == 0) {
			showToast("请输入帖子标题");
			et_post_note_title.requestFocus();
			return;
		} else {
			try {
				title = URLEncoder.encode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (content == null || content.length() == 0) {
			showToast("请输入帖子内容");
			et_post_note_content.requestFocus();
			return;
		} else {
			try {
				content = URLEncoder.encode(content, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (photos == null || photos.size() == 0) {
			showToast("请选择图片");
			return;
		}
		Map<String, File> files = new HashMap<String, File>();
		for (PhotoModel photo : photos) {
			File f = new File(photo.getOriginalPath());
			files.put(photo.getOriginalPath(), f);
		}
		postMyNote(files);
	}

	private void postMyNote(final Map<String, File> files) {
		loadDialog.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "image/jpeg");

		String uri = Config.POST_NOTE_URL + "/"
				+ userInfoSp.get("userId").toString() + "/"
				+ userInfoSp.get("sessionId").toString() + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ getIntent().getStringExtra("circleId") + "/" + title + "/"
				+ content + "/" + null + "/" + null + "/" + null;
		addPutUploadFileRequest(uri, files, params, mResonseListenerString,
				mErrorListener, null);
	}

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
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		if (imageItem.size() - 1 == 7) {
			showToast("一条动态最多选择7张图片");
		} else if (position == imageItem.size() - 1) { // 点击图片位置为+ 0对应0张图片
			View headselectview = View.inflate(this,
					R.layout.layout_action_sheet_select_userhead, null);
			selectHeadDialog = UIManager.getActionSheet(this, headselectview);
			selectHeadDialog.show();
			// Intent intent = new Intent(PostNoteActivity.this,
			// PhotoSelectorActivity.class);
			// intent.putExtra(PhotoSelectorActivity.KEY_MAX, 7);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			// PostNoteActivity.this.startActivityForResult(intent, IMAGE_OPEN);
			// 通过onResume()刷新数据
		} else {
			// view.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// Intent intent = new Intent(PostNoteActivity.this,
			// PhotoDelPreviewActivity.class);
			// ArrayList<ImagePath> data = new ArrayList<ImagePath>();
			// for (PhotoModel photo : selectPhotos) {
			// data.add(new ImagePath(photo.getOriginalPath()));
			// }
			// intent.putExtra("photos", selectPhotos);
			// intent.putExtra("position", position);
			// intent.putExtra("from", 1);
			// startActivityForResult(intent, 0);
			// }
			// });
			Intent intent = new Intent(this, PhotoDelPreviewActivity.class);
			intent.putExtra("photos", photos);
			intent.putExtra("position", position);
			intent.putExtra("from", 1);
			startActivityForResult(intent, 0);
		}

	}

	private void parseRespense(String response) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);
			String resultCode = JsonManager.getJsonItem(jsonObj, "resultCode")
					.toString();
			String resultInfo = JsonManager.getJsonItem(jsonObj, "resultInfo")
					.toString();
			UIManager.toggleDialog(loadDialog);
			if (resultCode.equals("1")) {
				showToast("发帖成功");
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
			e.printStackTrace();
		}
	}
}
