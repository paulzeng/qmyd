package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.ListMessageAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.PushInfo;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.JsonManager;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.PreferenceManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * 
 * @author thomas
 * 
 */
public class MessageCenterActivity extends BaseFragmentActivity implements
		OnClickListener, OnItemClickListener {

	private ImageButton imgBtnBack;
	private ListView listView;
	private TextView emptyView;
	private RequestQueue mRequestQueue;
	private Context context;
	private ListMessageAdapter mAdapter;
	private DrawerLayout mDrawerLayout;
	private Map<String, ?> userInfoSp;
	String tag;
	int page;
	int pageSize;
	List<PushInfo> mDate = new ArrayList<PushInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		mRequestQueue = Volley.newRequestQueue(context);
		findView();
		initData();
		initView();
		httpGetJson();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_message_center_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
	}

	private void initView() {
		imgBtnBack.setOnClickListener(this);
	}

	private void findView() {
		imgBtnBack = (ImageButton) this.findViewById(R.id.imgBtnBack);
		listView = (ListView) findViewById(R.id.lstViewMessage);
		listView.setOnItemClickListener(this);
	}

	void initData() {
		// �����ݿ��в�ѯ����
		mDate = DBManager.getInstance(this).getMessageList(
				userInfoSp.get("userId").toString());
		DebugUtility.showLog("��ѯ����Ϣ�б�����" + mDate.toString());
	}

	private void httpGetJson() {
		if (PreferenceManager.getString(this, "tag") == null
				|| PreferenceManager.getString(this, "tag").trim().equals("")) {
			tag = System.currentTimeMillis() + "";
			DebugUtility.showLog("ʱ���Ϊ��ǰʱ��:" + tag);
		} else {
			tag = PreferenceManager.getString(this, "tag").toString();
			DebugUtility.showLog("ʱ���Ϊ�ϴα���ʱ��:" + tag);
		}
		page = 1;
		pageSize = 10;
		String url = Config.MY_MESSAGE_URL + "/"
				+ MyApplication.instance.getHardId() + "/"
				+ userInfoSp.get("sessionId") + "/" + userInfoSp.get("userId")
				+ "/" + tag + "/" + page + "/" + pageSize;
		DebugUtility.showLog("����URL:" + url);
		if (NetManager.isNetworkConnected(this)) {
			StringRequest request = new StringRequest(Request.Method.GET, url,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							parseRespense(response);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							showToast("��ȡ����ʧ��");
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
			Toast.makeText(this, "���粻���ã�������������", 1 * 1000).show();
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
				DebugUtility.showLog("��ȡ��Ϣ��" + response);
				String tag = JsonManager.getJsonItem(jsonObj, "tag").toString();
				// ���µ�����ʱ������tag
				PreferenceManager.setString(this, "tag", tag);
				String pushinfo = JsonManager.getJsonItem(jsonObj, "pushList")
						.toString();
				DebugUtility.showLog("�������Ϣ�б�����1" + pushinfo.toString());
				// ��Ϣ���ݼ���
				List<PushInfo> pushinfos = JsonManager.getListFromJSON(
						pushinfo, PushInfo.class);
				DebugUtility.showLog("�������Ϣ�б�����2" + pushinfos.toString());
				// ����Ϣ���ݺ�tag�������ݿ�
				DBManager.getInstance(this).inseartMessageList(
						userInfoSp.get("userId").toString(), pushinfos);
				// �����ݿ���»�����������һ��
				mDate = DBManager.getInstance(this).getMessageList(
						userInfoSp.get("userId").toString());
			} else if (resultCode.equals("0")) {
				DebugUtility.showLog("resultCode=" + resultCode + "��ȡ��Ϣ�쳣");
			} else if (resultCode.equals("3")) {
				DebugUtility.showLog("resultCode=" + resultCode + resultInfo);
			} else if (resultCode.equals("10000")) {
				DebugUtility.showLog("resultCode=" + resultCode
						+ "δ��½���½��ʱ�������µ�½");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			mAdapter = new ListMessageAdapter(this, mDate);
			listView.setAdapter(mAdapter);
			setEmptyView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setEmptyView() {
		emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setText("������Ϣ");
		emptyView.setTextSize(20);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgBtnBack:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		PushInfo info = mDate.get(position);
		DebugUtility.showLog("###��Ϣ����####" + info.toString());
		if (info.getType().equals("4")) {
			if (info.getTypeDetail().equals("1")) {// ���˹�ע��
				Intent intent = new Intent(this, GusterInfoActivity.class);
				intent.putExtra("userid", info.getUserId());
				startActivityForResult(intent,0);
			} else if (info.getTypeDetail().equals("2")) {// ��̬����
				Intent intent = new Intent(this, DongTaiDetailActivity.class);
				intent.putExtra("id", info.getInfoId());
				intent.putExtra("staffId", userInfoSp.get("userId").toString());
				startActivityForResult(intent,0);
			} else if (info.getTypeDetail().equals("3")) {
				Intent intent = new Intent(this, DongTaiDetailActivity.class);
				intent.putExtra("id", info.getInfoId());
				intent.putExtra("staffId", userInfoSp.get("userId").toString());
				startActivityForResult(intent,0);
			} else if (info.getTypeDetail().equals("4")) {
				Intent intent = new Intent(this, CircleContentNoteDetailActivity.class);
				intent.putExtra("infoId", info.getInfoId().split("-")[1]);
				intent.putExtra("circleId", info.getInfoId().split("-")[0]);
				startActivityForResult(intent,0);
			} else if (info.getTypeDetail().equals("5")) {
				Intent intent = new Intent(this, CircleContentNoteDetailActivity.class);
				intent.putExtra("infoId", info.getInfoId().split("-")[1]);
				intent.putExtra("circleId", info.getInfoId().split("-")[0]);
				startActivityForResult(intent,0);
			}
		} else {
			String url = Config.BASE_URL + info.getUrl();
			Intent intent = new Intent(this, PushMessageDescActivity.class);
			intent.putExtra("url", url);
			startActivityForResult(intent, 0);
		}
		// ����Ϣ״̬��Ϊ�Ѷ�
		mDate.get(position).setStatus("0");
		// �������ݿ�����Ϣ��״̬
		DBManager.getInstance(this).updateMessage(mDate.get(position).get_id(),
				mDate.get(position));
		DebugUtility.showLog("���µ���ϢID:" + mDate.get(position).get_id());
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		mDate = DBManager.getInstance(this).getMessageList(
				userInfoSp.get("userId").toString());
		DebugUtility.showLog("�������²�ѯ����Ϣ�б�����" + mDate.toString());
		mAdapter = new ListMessageAdapter(this, mDate);
		listView.setAdapter(mAdapter);
		setEmptyView();
		// mAdapter.notifyDataSetChanged();
	}

}
