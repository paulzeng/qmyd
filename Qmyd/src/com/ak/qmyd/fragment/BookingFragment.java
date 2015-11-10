package com.ak.qmyd.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.LoginActivity;
import com.ak.qmyd.activity.base.BaseFragment;
import com.ak.qmyd.adapt.BookingFragmentAdapt;
import com.ak.qmyd.bean.ConstUtil;
import com.ak.qmyd.bean.MyOrderList;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BookingFragment extends BaseFragment implements OnScrollListener {

	ListView listView;

	RequestQueue mRequestQueue;

	Context context;

	private TextView textView_no_data;

	private ProgressBar progressBar;

	private View view;

	private int currentPage;

	private boolean flag = false;

	private TextView footTextView;

	private List<MyOrderList> totalList;

	private int pagerNumber = 4;

	private BookingFragmentAdapt myAdapt;

	private String hardId, sessionId, userId, orderType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		mRequestQueue = Volley.newRequestQueue(context);
		hardId = MyApplication.instance.getHardId();
		sessionId = context.getSharedPreferences("user_info",
				Context.MODE_PRIVATE).getString("sessionId", null);
		userId = context
				.getSharedPreferences("user_info", Context.MODE_PRIVATE)
				.getString("userId", null);
		orderType = "1";
	}

	/**
	 * ע��㲥
	 * */
	private void registerBroadCast() {
		BookFragmentReceiver receiver = new BookFragmentReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstUtil.BOOKFRAGMENT_ACTION);
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_booking, null);
		findView();
		httpGet();
		registerBroadCast();
		return view;
	}

	private void findView() {
		listView = (ListView) view.findViewById(R.id.listView1);
		textView_no_data = (TextView) view.findViewById(R.id.textView_no_data);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		View footView = LayoutInflater.from(context).inflate(
				R.layout.listview_footview, null);
		footTextView = (TextView) footView.findViewById(R.id.tv_footview);
		listView.addFooterView(footView);
		listView.setOnScrollListener(this);
	}

	/**
	 * Get����
	 * */
	private void httpGet() {
		currentPage = 1;
		totalList = new ArrayList<MyOrderList>();
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.MY_ORDER_BOOKING);
		String param = Tools.joinUrlByParam(hardId, sessionId, userId,
				orderType, currentPage + "", pagerNumber + "");
		buffer.append(param);

		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "���粻���ã�������������", Toast.LENGTH_SHORT).show();
			return;
		}
		StringRequest request = new StringRequest(Method.GET,
				buffer.toString(), new Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							progressBar.setVisibility(View.GONE);
							List<MyOrderList> list = parseJson(response);
							if (list == null || list.size() == 0) {
								textView_no_data.setVisibility(View.VISIBLE);
								textView_no_data.setText("û��Ԥ������");
							} else {
								flag = true;
								totalList.addAll(list);
								listView.setVisibility(View.VISIBLE);
								myAdapt = new BookingFragmentAdapt(totalList,
										context, userId);
								listView.setAdapter(myAdapt);
								if (list.size() < pagerNumber) {
									footTextView.setText("û�и����������");
								}
								// if (list.size() < pagerNumber) {
								// ((View)
								// footTextView.getParent()).setVisibility(View.GONE);
								// }
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
					}
				});
		mRequestQueue.add(request);

	}

	/**
	 * ����JSON����
	 * */
	private List<MyOrderList> parseJson(String response) throws JSONException {
		List<MyOrderList> venuesOrderList;
		Gson gson = new Gson();
		JSONObject object = new JSONObject(response);
		String array;
		Type type = new TypeToken<List<MyOrderList>>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// ���ص�������Ϣ
		String info = object.getString("resultInfo");
		if (resultCode == 0) {
			// ����ʧ��
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		} else if (resultCode == 1) {
			// ���سɹ�
			array = object.getString("venuesOrderList");
			// �õ�����
			venuesOrderList = gson.fromJson(array, type);
			return venuesOrderList;
		} else if (resultCode == 10000) {
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
		return null;
	}

	/**
	 * ���ظ�������
	 * */
	private void loadMore() {
		currentPage++;
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.MY_ORDER_BOOKING);
		String param = Tools.joinUrlByParam(hardId, sessionId, userId,
				orderType, currentPage + "", pagerNumber + "");
		buffer.append(param);
		// String url = Config.BASE_URL +
		// "/api/rest/venues/ListOrder/1/1/1/1/+"+currentPage+"/"+pagerNumber;
		StringRequest request = new StringRequest(Method.GET,
				buffer.toString(), new Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {

							List<MyOrderList> list = parseJson(response);
							if (list == null) {
								footTextView.setText("û�и����������");
							} else {
								totalList.addAll(list);
								flag = true;
								myAdapt.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						flag = true;
					}
				});
		mRequestQueue.add(request);
	}

	/**
	 * �㲥����ȡ������������ˢ��listview
	 * */
	public class BookFragmentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			progressBar.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			textView_no_data.setVisibility(View.GONE);
			httpGet();
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

		if (flag) {
			if (arg1 + arg2 == arg3) {
				flag = false;
				loadMore();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
