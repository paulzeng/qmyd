package com.ak.qmyd.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.HorizontialListViewAdapt;
import com.ak.qmyd.adapt.HorizontialListViewAdapt.ViewHold;
import com.ak.qmyd.adapt.VenuesBookAdapt;
import com.ak.qmyd.bean.BookDtail;
import com.ak.qmyd.bean.DeliverData;
import com.ak.qmyd.bean.HorizontialListView;
import com.ak.qmyd.bean.OrderList;
import com.ak.qmyd.bean.VenuesBook;
import com.ak.qmyd.bean.VenuesInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.MyListView;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VenuesBookActivity extends BaseFragmentActivity implements OnClickListener {

	private ListView listView;

	private RequestQueue mRequestQueue;

	private List<VenuesBook> venuesReserveList;

	private Context context;

	private GridView gridView;
	
	private List<BookDtail> reserveDetialList;

	private VenuesBookAdapt adapter;

	private TextView textView_order;// 预定按纽

	private boolean flag;

	private int selectData;
	
	private ImageButton imageButton_back;
	
	private int supportId;
	
	private String venuesId,hardId;
	
	private String orderCode;//定单号
	
	private String orderId;//定单ID；
	
	private HorizontialListViewAdapt myAdapt;
	
	public static int position;
	
//	private ProgressBar progressBar;
	
	private int totalTicket=0;//总票数值
	
	private String userId,sessionId; 
	
	private DrawerLayout drawerlayout;
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venues_book);
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(context);
		supportId=getIntent().getExtras().getInt("supportId");
		venuesId=getIntent().getExtras().getString("venuesId");
		SharedPreferences share = getSharedPreferences("user_info", MODE_PRIVATE);
		userId= share.getString("userId", null);	
		sessionId=share.getString("sessionId", null);
		hardId=MyApplication.instance.getHardId();
		position=0;
		findView();
		httpGetJson();
	}

	private void findView() {
		gridView=(GridView)this.findViewById(R.id.gridView);

		listView = (ListView) this.findViewById(R.id.listView1);
		textView_order = (TextView) this.findViewById(R.id.textView_order);
		imageButton_back=(ImageButton)this.findViewById(R.id.imageButton_back);
//		progressBar=(ProgressBar)this.findViewById(R.id.progressBar1);
		drawerlayout=(DrawerLayout)this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
//		progressBar.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		textView_order.setOnClickListener(this);
		imageButton_back.setOnClickListener(this);
	}

	/**
	 * GET请求网络数据
	 * */
	private void httpGetJson() {
		loadDialog.show();
		StringBuffer buffer=new StringBuffer();
		buffer.append(Config.VENUES_BOOK_URL);
		String param = Tools.joinUrlByParam(hardId,supportId+"");
		buffer.append(param);
		
		if(!NetManager.isNetworkConnected(context)){
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;			
		}
		
		StringRequest request = new StringRequest(Method.GET, buffer.toString(),
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub	
						try {
							DebugUtility.showLog("场馆预定返回数据:"+response);
							UIManager.toggleDialog(loadDialog);
//							progressBar.setVisibility(View.GONE);
							listView.setVisibility(View.VISIBLE);
							parseJson(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				});
		//request.setRetryPolicy(new DefaultRetryPolicy(7*1000, 1, 1.0f));//设置请求时间
		mRequestQueue.add(request);
	}

	/**
	 * 解析JSON数据
	 * 
	 * @throws JSONException
	 * */
	private void parseJson(String response) throws JSONException {
		Gson gson = new Gson();
		JSONObject object = new JSONObject(response);
		String array;
		Type type = new TypeToken<List<VenuesBook>>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		if (resultCode == 0) {
			// 返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		} else if (resultCode == 1) {
			// 返回成功
			array = object.getString("venuesReserveList");
			// 得到集合
			venuesReserveList = gson.fromJson(array, type);
			initData();
		}else if(resultCode==3){
			Tools.setEmptyView(listView, context);
		}
	}

	private void initData() {
		myAdapt=new HorizontialListViewAdapt(context, venuesReserveList);	
		View view = LayoutInflater.from(context).inflate(R.layout.horizon_listview_item, null);
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		  LayoutParams params = new LayoutParams(venuesReserveList.size() * (view.getMeasuredWidth()+30), LayoutParams.WRAP_CONTENT);  
	       gridView.setLayoutParams(params);  
	       gridView.setColumnWidth(view.getMeasuredWidth());  
	       gridView.setHorizontalSpacing(30);  
	       gridView.setStretchMode(GridView.NO_STRETCH);  
	       gridView.setNumColumns(venuesReserveList.size()); 
		   gridView.setAdapter(myAdapt);
		//horizontialListView.setAdapter(myAdapt);	
		reserveDetialList = venuesReserveList.get(0).getReserveDetialList();
		// 判断是否已经预定过了
		flag = isBook(venuesReserveList);
	
	
			adapter = new VenuesBookAdapt(context, reserveDetialList, flag);
			listView.setAdapter(adapter);
			Tools.setEmptyView(listView, context);
	
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				reserveDetialList = venuesReserveList.get(arg2)
						.getReserveDetialList();
			
				position=arg2;
				changColor(arg2);
				adapter=new VenuesBookAdapt(context, reserveDetialList, flag);
				listView.setAdapter(adapter);
			}
		});
	}
	
	
	private void changColor(int index){
		List<View> mlist = myAdapt.list;	
		View horizonView;	
		ViewHold viewHold;
		
		//循环list使每个日期的圆圈按纽都变成不选中状态
		for(int i=0;i<mlist.size();i++){
			horizonView=mlist.get(i);	
			viewHold = (HorizontialListViewAdapt.ViewHold)horizonView.getTag();
			viewHold.imageView_book_data.setImageResource(R.drawable.circle_g);
			viewHold.textView_data.setTextColor(getResources().getColor(R.color.common_black_light));
			viewHold.textView_day.setTextColor(getResources().getColor(R.color.common_black_dark));	
		}
		//将点击的那个圆圈按纽变成选中状态
		horizonView = mlist.get(index);
		viewHold = (HorizontialListViewAdapt.ViewHold)horizonView.getTag();
		viewHold.imageView_book_data.setImageResource(R.drawable.circle_y);
		viewHold.textView_data.setTextColor(getResources().getColor(R.color.orange_color));
		viewHold.textView_day.setTextColor(getResources().getColor(R.color.orange_color));	
	}
	
	

	/**
	 * 
	 * 判断是否已经有过预定了
	 * */
	private boolean isBook(List<VenuesBook> venuesReserveList2) {

		for (int i = 0; i < venuesReserveList2.size(); i++) {
			List<BookDtail> aList = venuesReserveList2.get(i)
					.getReserveDetialList();
			for (int k = 0; k < aList.size(); k++) {
				if (aList.get(k).getBookticket() != 0) {
					selectData = i;
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 得到要确认预定的集合
	 * @return 
	 * */
	private List<OrderList> getOrderList(){
		
		List<OrderList> orderList=new ArrayList<OrderList>();
	
		for(int i=0;i<adapter.saveList.size();i++){
			if(adapter.saveList.get(i).get("ticketNumber")!=0){
				OrderList order=new OrderList();	
				order.setVenuesDate(venuesReserveList.get(position).getVenuesDate());
				order.setBookticket(adapter.saveList.get(i).get("ticketNumber")+"");
				order.setReserveTimeSlot(venuesReserveList.get(position).getReserveDetialList().get(i).getReserveTimeSlot());
				order.setReserveMoney(venuesReserveList.get(position).getReserveDetialList().get(i).getReserveMoney());
				 order.setTimeDefineId(venuesReserveList.get(position).getReserveDetialList().get(i).getTimeDefineId());
				 orderList.add(order);
			}
		}
		return orderList;
	}
	
	
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.textView_order:
			textView_order.setClickable(false);
			if(sessionId==null){
				Intent intent=new Intent(context,LoginActivity.class);
				startActivity(intent);
				finish();
			}else{
				if(venuesReserveList!=null&&venuesReserveList.size()!=0){				
					postData();
				}		
			}		
			break;
		case R.id.imageButton_back:		
			finish();
			break;	
		}
	}
	
	
	
	/**
	 * 解析出定单号
	 * */
	private void parseJsonArray(String response) throws JSONException {
	
		JSONObject object = new JSONObject(response);
			
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		
		if (resultCode == 0) {
			// 返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		} else if (resultCode == 1) {
			// 返回成功
			 JSONObject array = object.getJSONObject("venuesOrder");
			// 得到集合					 
			 orderCode=array.getString("orderCode");
			 orderId=array.getString("orderId");
			 Intent intent = new Intent(VenuesBookActivity.this,
			OrderConfirmActivity.class);
			Bundle bundle=new Bundle();
			DeliverData data=new DeliverData();
			data.setMlist(getOrderList());
			bundle.putSerializable("orderList", data);	
			bundle.putString("orderCode", orderCode);
			bundle.putString("orderId", orderId);
			bundle.putString("userId", userId);
			bundle.putString("sessionId", sessionId);
			bundle.putString("venuesThumbnail", getIntent().getExtras().getString("venuesThumbnail"));
			intent.putExtras(bundle);			
			startActivity(intent);
		}else if(resultCode==4||resultCode==5||resultCode==6||resultCode==7){
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}else if(resultCode==10000){
			Intent intent=new Intent(context,LoginActivity.class);
			startActivity(intent);
			finish();
		}	
	}
	
	/**
	 * POST提交数据给服务器
	 * */
	
	private void postData(){	
		
		//判断是否选了票
		List<BookDtail> mmList = venuesReserveList.get(position).getReserveDetialList();
		int ticket=0;
		int number=0;
		for(int i=0;i<adapter.saveList.size();i++){		
			ticket+=adapter.saveList.get(i).get("ticketNumber");
            if(adapter.saveList.get(i).get("ticketNumber")!=0){
            	number++;
			}
		}
		if(ticket==0){
			textView_order.setClickable(true);
			Toast.makeText(context, "请选择预定的票数", Toast.LENGTH_SHORT).show();
			return;	
		}
		
		//判断是否选的时间段连续
		String[] str=new String[number];
		for(int i=0,k=0;i<adapter.saveList.size();i++){
			if(adapter.saveList.get(i).get("ticketNumber")!=0){
				str[k]=mmList.get(i).getReserveTimeSlot();
				k++;
			}		
		}
		if(!Tools.isTimeLink(str)){
			Toast.makeText(context, "只能预定连续的时间段", Toast.LENGTH_SHORT).show();
			textView_order.setClickable(true);
			return;
		}
		
		//得到总票数，和预定的时间段
		  totalTicket=0;//总票数值
		String time = "";
		for(int i=0;i<adapter.saveList.size();i++){
			if(adapter.saveList.get(i).get("ticketNumber")!=0){
				totalTicket+=adapter.saveList.get(i).get("ticketNumber");
				time+=mmList.get(i).getTimeDefineId()+"="+adapter.saveList.get(i).get("ticketNumber");
				time+=",";
			}				
		}		
		final String timeDefine = time.substring(0, time.length()-1);//预定的时间段
		
		if(!NetManager.isNetworkConnected(context)){
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			textView_order.setClickable(true);
			return;			
		}
	    loadDialog.show();
		//POST请求
		StringBuffer buffer=new StringBuffer();
		buffer.append(Config.VENUES_BOOK_POST_URL);
		StringRequest request = new StringRequest(Method.POST, buffer.toString(),
				new Listener<String>() {
					@Override
			public void onResponse(String response) {							
					try {
						UIManager.toggleDialog(loadDialog);
						textView_order.setClickable(true);
						parseJsonArray(response);	
					} catch (Exception e) {
						
					}										
			}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						textView_order.setClickable(true);
						showToast("获取数据失败");
						UIManager.toggleDialog(loadDialog);
					}
				}){
			@Override
			protected Map<String, String> getParams()
					throws AuthFailureError {
				Map<String, String> map=new HashMap<String, String>();
				map.put("defineId", venuesReserveList.get(position).getDefineId());//从集合任意取一个，因为每个都有defineId;				
				map.put("totalNum", totalTicket+"");//上传总票数
				map.put("timeDefine", timeDefine);//上传预约时间段信息,时间段ID=票数
				map.put("venuesId", venuesId);
				map.put("supportId", supportId+"");
				map.put("appointmentDate", venuesReserveList.get(position).getVenuesDate());			
				map.put("hardId", hardId);
				map.put("sessionId", sessionId);
				map.put("userId", userId);	
				return map;
			}
		};	
		mRequestQueue.add(request);
	}
}
