package com.ak.qmyd.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.OrdercConfirmAdapt;
import com.ak.qmyd.bean.DeliverData;
import com.ak.qmyd.bean.OrderList;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.ImageLoad;
import com.ak.qmyd.tools.MyListView;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderConfirmActivity extends BaseFragmentActivity implements
		OnClickListener {

	private VenuesListInformation venuesListInformation;

	private Context context;

	private ImageLoader imageLoader;

	private ImageView imageView_venues;

	private TextView textView_venues_name;

	private TextView textView_venues_address;

	private TextView textView_money;

	private MyListView listView;

	List<OrderList> mList;

	private RadioButton radiobutton_xianxia;

	private Button button_pay;

	// private RelativeLayout rela_back_venuesdetail;

	private ImageButton imageButton_back;

	private String orderCode;// 定单号

	private String orderId, userId, hardId, sessionId;// 定单ID

	private RequestQueue mRequestQueue;

	private DeliverData data;

	private DrawerLayout drawerlayout;
	
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		ImageLoad.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();
		mRequestQueue = Volley.newRequestQueue(context);
		data = (DeliverData) getIntent().getExtras().getSerializable(
				"orderList");
		orderCode = getIntent().getExtras().getString("orderCode");
		orderId = getIntent().getExtras().getString("orderId");
		userId = getIntent().getExtras().getString("userId");
		hardId = MyApplication.instance.getHardId();
		sessionId = getIntent().getExtras().getString("sessionId");
		mList = data.getMlist();
		findView();
	}

	/**
	 * POST请求确认支付
	 * */
	private void httpPosr() {
		loadDialog.show();
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.VENUES_PAY_POST_URL);

		if (!NetManager.isNetworkConnected(context)) {
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}

		StringRequest request = new StringRequest(Request.Method.POST,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据
						UIManager.toggleDialog(loadDialog);
						Intent intent1 = new Intent(OrderConfirmActivity.this,
								FinishPayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("orderCode", orderCode);
						bundle.putString("venuesName",
								venuesListInformation.getVenuesName());
						bundle.putString("venuesAddress",
								venuesListInformation.getVenuesAddr());
						bundle.putSerializable("orderList", data);
						intent1.putExtras(bundle);
						startActivity(intent1);
						button_pay.setClickable(true);
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("hardId", hardId);
				map.put("sessionId", sessionId);
				map.put("orderId", orderId);
				map.put("orderType", "1");
				map.put("totalMoney", calculate() + "");
				map.put("venuesPayment", "1");
				map.put("userId", userId);
				return map;
			}
		};
		mRequestQueue.add(request);
	}

	private void findView() {
		textView_venues_address = (TextView) this
				.findViewById(R.id.textView_venues_address);
		textView_venues_name = (TextView) this
				.findViewById(R.id.textView_venues_name);
		imageView_venues = (ImageView) this.findViewById(R.id.imageView_venues);
		listView = (MyListView) this.findViewById(R.id.listView);
		textView_money = (TextView) this.findViewById(R.id.textView_money);
		// radiobutton_zhifubao=(RadioButton)this.findViewById(R.id.radiobutton_zhifubao);
		radiobutton_xianxia = (RadioButton) this
				.findViewById(R.id.radiobutton_xianxia);
		button_pay = (Button) this.findViewById(R.id.button_pay);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);

		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		// radiobutton_zhifubao.setOnClickListener(this);
		radiobutton_xianxia.setEnabled(false);
//		radiobutton_xianxia.setOnClickListener(this);
		button_pay.setOnClickListener(this);

		imageButton_back.setOnClickListener(this);
		iniData();
	}

	private void iniData() {
		listView.setAdapter(new OrdercConfirmAdapt(context, mList));
		venuesListInformation = VenuesAppointmentActivity.venuesListInformation;
		if (venuesListInformation == null) {
			venuesListInformation = new VenuesListInformation();
			venuesListInformation.setSupportId(VenuesDetailsActivity.venues
					.getSupportId());
			venuesListInformation.setVenuesAddr(VenuesDetailsActivity.venues
					.getVenuesAddr());
			venuesListInformation
					.setVenuesDistance(VenuesDetailsActivity.venues
							.getVenuesDistance());
			venuesListInformation.setVenuesId(VenuesDetailsActivity.venues
					.getVenuesId());
			venuesListInformation.setVenuesName(VenuesDetailsActivity.venues
					.getVenuesName());
			venuesListInformation
					.setVenuesThumbnail(VenuesDetailsActivity.venues
							.getVenuesThumbnail());
		}

		textView_venues_address.setText(venuesListInformation.getVenuesAddr());
		textView_venues_name.setText(venuesListInformation.getVenuesName());
		textView_money.setText(calculate() + "");
		DebugUtility.showLog("缩略图: " + getIntent().getExtras().getString("venuesThumbnail"));
		imageLoader.displayImage(Config.BASE_URL
				+ getIntent().getExtras().getString("venuesThumbnail"),
				imageView_venues);
	}

	/**
	 * 计算总金额
	 * 
	 * @return
	 * */
	private int calculate() {
		int money = 0;
		for (int i = 0; i < mList.size(); i++) {
			int m = Integer.parseInt(mList.get(i).getReserveMoney());
			int n = Integer.parseInt(mList.get(i).getBookticket());
			money += m * n;
		}
		return money;
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		// case R.id.radiobutton_zhifubao:
		// radiobutton_zhifubao.setChecked(true);
		// radiobutton_xianxia.setChecked(false);
		// break;
		case R.id.radiobutton_xianxia:
			// radiobutton_zhifubao.setChecked(false);
//			radiobutton_xianxia.setChecked(true);
			break;
		case R.id.button_pay:
			button_pay.setClickable(false);
			// if(radiobutton_zhifubao.isChecked()){
			// Toast.makeText(context, "你选择了支付宝交易", Toast.LENGTH_SHORT).show();
			// }else if(radiobutton_xianxia.isChecked()){
			// Toast.makeText(context, "你选择了线下交易", Toast.LENGTH_SHORT).show();
			// }
			httpPosr();

			break;
		case R.id.rela_back_venuesdetail:
			Intent intent = new Intent(OrderConfirmActivity.this,
					VenuesDetailsActivity.class);
			startActivity(intent);
			break;
		case R.id.imageButton_back:
			finish();
			break;
		}
	}
}
