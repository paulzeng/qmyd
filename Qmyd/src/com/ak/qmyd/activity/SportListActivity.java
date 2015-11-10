package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.MyGridView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SportListActivity extends BaseActivity {
	private MyGridView gridview;
	private ArrayList<SportType> data;
	private Map<String, ?> userInfoSp;
	private Dialog loadDialog;
	private RequestQueue mRequestQueue;
	private SportImageAdapter mAdapter;
	private String staffId;
	private ImageView iv_close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_list);
		initViews();
		initData();
	}

	void initViews() {
		gridview = (MyGridView) this.findViewById(R.id.grid_type_image);
		data = new ArrayList<SportType>();
		mAdapter = new SportImageAdapter(this, data);
		gridview.setAdapter(mAdapter);
		loadDialog = UIManager.getLoadingDialog(this);
		iv_close = (ImageView)this.findViewById(R.id.iv_close);
		iv_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	void initData() {
		staffId = getIntent().getStringExtra("staffId");
		userInfoSp = MyApplication.instance.userInfoSp.getAll(); 
		loadDialog.show();
		mRequestQueue = Volley.newRequestQueue(this);
		String httpurl = Config.BASE_URL
				+ "/api/rest/circle/querySportsList" + "/"
				+ userInfoSp.get("userId") + "/" + staffId + "/"
				+ userInfoSp.get("sessionId") + "/"
				+ MyApplication.instance.getHardId();

		DebugUtility.showLog("请求URL:" + httpurl);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest request = new StringRequest(Request.Method.GET,
					httpurl, new Listener<String>() {
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
							DebugUtility.showLog("获取运动类型失败："
									+ error.getMessage());
							UIManager.toggleDialog(loadDialog); 
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
			Toast.makeText(this, "网络不可用，请检查网络设置", 1 * 1000).show();
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
				String sportsList = JsonManager.getJsonItem(jsonObj,
						"sportsList").toString();
				data = new Gson().fromJson(sportsList,
						new TypeToken<List<SportType>>() {
						}.getType());
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "获取消息异常");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "未登陆或登陆超时，请重新登陆");
			}
			mAdapter = new SportImageAdapter(this, data);
			gridview.setAdapter(mAdapter); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class SportType {
		private String typeName, thumbnailPath;

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
		}

	}

	public class SportImageAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<SportType> data;

		public SportImageAdapter(Context c, ArrayList<SportType> data) {
			mContext = c;
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ImageView imageview;
			if (convertView == null) {
				imageview = new ImageView(mContext);
				imageview.setLayoutParams(new GridView.LayoutParams(
						150, 150));
				imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageview.setPadding(8, 8, 8, 8);
			} else {
				imageview = (ImageView) convertView;
			}
			// imageview.setImageResource(mThumbIds[position]);
			ImageLoader.getInstance().displayImage(
					Config.BASE_URL + data.get(position).getThumbnailPath(),
					imageview);
			return imageview;
		}

	}
}
