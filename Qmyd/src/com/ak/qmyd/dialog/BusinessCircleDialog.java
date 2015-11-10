package com.ak.qmyd.dialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CityLocationActivity;
import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.activity.VenuesDetailsActivity;
import com.ak.qmyd.adapt.BusinessCircleAdapt;
import com.ak.qmyd.adapt.VenuesAppointmentAdapt;
import com.ak.qmyd.bean.AreaList;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.JsonUtils;
import com.ak.qmyd.tools.Tools;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Color;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessCircleDialog extends Dialog implements View.OnClickListener{

	private  Context context;
	
	private int DialogHeight;
	
	private TextView textview_show_position,tv_business_dialog_circle,tv_business_dialog_recommended;
	
	private LinearLayout linear_city_location;
	private ImageButton ib_business_dialog_back;
	private ImageView iv_business_dialog_search;
	/**
	 * 新的视图
	 * */	
	private GridView gridView;

	List<AreaList> mlist;
	
	private String dialogCity;
	private String cityName;
	private String cityCode;
	RequestQueue mRequestQueue;
	
	private GridViewClick gridViewClick;
	
	private List<AreaList> alist;
	
	private String hardId;
	
	public BusinessCircleDialog(Context context, int theme,String dialogCity,String cityCode,String cityName) {
		super(context, theme);
		this.context=context;
		this.dialogCity=dialogCity;
		this.cityName=cityName;
		this.cityCode=cityCode;
		
		Log.i("ceshi", "城市名字:"+cityName);
		Log.i("ceshi", "城市code:"+cityCode);
		
		mRequestQueue=Volley.newRequestQueue(context);
		hardId=MyApplication.instance.getHardId();
		httpGet();
	}
	
	/**
	 * 网络GET请求得到JSON数据存到list集合里
	 * */
	private void httpGet() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.BUSINESS_CIRCLE_URL);
		String param = Tools.joinUrlByParam(hardId,cityCode);
		buffer.append(param);
		
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// 请求成功的返回
					@Override
					public void onResponse(String response) {
						// 解析JSON数据						
						try {
							parseJson(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
						
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// 请求失败的调用
					@Override
					public void onErrorResponse(VolleyError error) {
					
					}
				});
		mRequestQueue.add(request);
	}
	
	
	public  void parseJson(String response) throws JSONException {	
		Gson gson = new Gson();
		JSONObject object=new JSONObject(response);	
		String array;
		Type type = new TypeToken<List<AreaList>>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		if(resultCode==0){
			//返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}else if(resultCode==1){
			//返回成功		
			array=object.getString("areaList");	
			//得到集合
			mlist=gson.fromJson(array, type);		
			iniData();	
		}
	}
	
	
	private void iniData() {
		alist=new ArrayList<AreaList>();
		AreaList area=new AreaList();
		area.setAreaCode(cityCode);
		area.setAreaName("全城");
		alist.add(area);
		alist.addAll(mlist);
		
		BusinessCircleAdapt adapter=new BusinessCircleAdapt(context, alist);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {			       
				gridViewClick.buttonClick(alist.get(position));
				VenuesAppointmentActivity.position=position;
				close();			
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businesscircle1_dialog);
		//设置位置
		setLayout();
		findView();	
		this.setCanceledOnTouchOutside(true);
	}


	/**
	 * 初始化
	 * */
	private void findView() {	
		ib_business_dialog_back = (ImageButton) findViewById(R.id.ib_business_dialog_back);
		iv_business_dialog_search = (ImageView) findViewById(R.id.iv_business_dialog_search);
		tv_business_dialog_circle = (TextView) findViewById(R.id.tv_business_dialog_circle);
		tv_business_dialog_recommended = (TextView) findViewById(R.id.tv_business_dialog_recommended);
		gridView=(GridView)findViewById(R.id.gridView1);
		textview_show_position=(TextView)findViewById(R.id.textview_show_position);	
		linear_city_location=(LinearLayout)findViewById(R.id.linear_city_location);
		linear_city_location.setOnClickListener(this); 
		ib_business_dialog_back.setOnClickListener(this); 
		iv_business_dialog_search.setOnClickListener(this); 
		tv_business_dialog_circle.setOnClickListener(this); 
		tv_business_dialog_recommended.setOnClickListener(this); 
		textview_show_position.setText(dialogCity);		
	}

	/**
	 * 关掉dialog
	 * */
	private void close(){		
		this.dismiss();
	}
	
	
	/**
	 * dialog的宽、高和位置
	 * */
	private void setLayout() {
		WindowManager m = ((Activity) context).getWindowManager();  
		Window window = this.getWindow();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		window.getDecorView().setPadding(0, 0, 0, 0);
		window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
	    LayoutParams layout = window.getAttributes();
	    window.setGravity(Gravity.LEFT|Gravity.TOP);
	    layout.width=Tools.getWidth(context);
	    Measure();
	    layout.height=DialogHeight;
	    layout.x=0;
	    layout.y=dialogTopMargin();
	    window.setAttributes(layout);		
	}

	/**
	 * 计算视图的高度
	 * */
	
	private void Measure(){
		View DialogView = LayoutInflater.from(context).inflate(R.layout.businesscircle_dialog, null);
		DialogView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		DialogHeight = DialogView.getMeasuredHeight() + 405;
	}

	/**
	 * 计算dialog距离上边的距离
	 * @return 
	 * */
	
	private int dialogTopMargin(){
		//上边距由两部分组成头部是54DIP，按纽是50DIP,这里的高度是在xml设置的，如果xml高度变化这里需要改变
		  final float scale = context.getResources().getDisplayMetrics().density; 
          int headHeight = (int)((54.0) * scale + 0.5f); 
		  int textHeight = (int)((50.0) * scale + 0.5f);
		  int dialogtopmargin = headHeight+textHeight;
		  return 0;
	}

	
	/**
	 * 按纽点击事件
	 * */
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.linear_city_location:
			Intent intent=new Intent(context,CityLocationActivity.class);	
			intent.putExtra("dialogCity", dialogCity);
			intent.putExtra("cityName", cityName);
			context.startActivity(intent);	
			this.dismiss();			
			break;	
		case R.id.ib_business_dialog_back:
			gridViewClick.buttonClick(0);
			close();	
			break;
		case R.id.iv_business_dialog_search:
			gridViewClick.buttonClick(1);
			close();	
			break;
		case R.id.tv_business_dialog_circle:
			close();	
			break;
		case R.id.tv_business_dialog_recommended:
			gridViewClick.buttonClick(2);
			close();	
			break;
		}	
	}
	
	
	
	public void setGridViewClick(GridViewClick gridViewClick){
		this.gridViewClick=gridViewClick;
	}
	
	/**
	 * 创建接口用来回调
	 * */
	public interface GridViewClick{	
		public void buttonClick(AreaList areaList);
		public void buttonClick(int type);
	}

}
