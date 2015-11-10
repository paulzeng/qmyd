package com.ak.qmyd.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.VenuesDetailViewPager;
import com.ak.qmyd.bean.ServiceInformation;
import com.ak.qmyd.bean.VenuesDetailPicList;
import com.ak.qmyd.bean.VenuesInformation;
import com.ak.qmyd.bean.VenuesListInformation;
import com.ak.qmyd.bean.model.Venues;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.ImageLoad;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.tools.UIManager;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class VenuesDetailsActivity extends BaseFragmentActivity implements OnClickListener {

	private Context context;
	private TextView textView_venues_name,textView_venues_address,textView_venues_phone,
	                 textView_venues_favornumber,textView_venues_badnumber,textView_instructions;
	private LinearLayout ll_venues_detail_comments;
	private Button btn_venues_appointment;
	private ViewPager viewPager;
	private ImageView imageView_call,imageView_map;
	private LinearLayout linear_imageView;
	private RequestQueue mRequestQueue;
	private VenuesInformation venuesInformation;
	private ImageLoader imageLoader;
	private List<ImageView> imageviewList=new ArrayList<ImageView>();//用于头部viewpager的滑动
	private String venuesId;
	private ScrollView scrollView;
	private ImageButton imageButton_back;
	private String supportId;
	private String hardId;
	private DrawerLayout drawerlayout;
	public static Venues venues; 
	private Dialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venues_details);	    
		loadDialog = UIManager.getLoadingDialog(this);
		context = getApplicationContext();
		mRequestQueue=Volley.newRequestQueue(context);
		ImageLoad.initImageLoader(context);
		imageLoader=ImageLoader.getInstance();
		findView();
		//venuesId=getIntent().getExtras().getString("venuesId");
		venuesId=getIntent().getStringExtra("venuesId");
		//typeId=venuesId=getIntent().getExtras().getString("typeId");
		supportId=getIntent().getStringExtra("supportId");
		 venues = (Venues)getIntent().getSerializableExtra("venues");
		hardId=MyApplication.instance.getHardId();
		httpGetJson();
	}



	/**
	 * 为控件添加数据 
	 * */
	private void init(String response) {
		if(venuesInformation.getReserveType().equals("0")){
			btn_venues_appointment.setEnabled(false);
		}else{
			btn_venues_appointment.setEnabled(true);
		}
		textView_venues_name.setText(venuesInformation.getVenuesName());
		textView_venues_address.setText(venuesInformation.getVenuesAddr());
		textView_venues_phone.setText(venuesInformation.getVenuesPhone());
		textView_instructions.setText(venuesInformation.getVenuesExplain());
	
		textView_venues_favornumber.setText(venuesInformation.getVenuesFavorNumber()+"");
		textView_venues_badnumber.setText(venuesInformation.getVenuesBadNumber()+"");
		//服务信息的集合
		List<ServiceInformation> list = venuesInformation.getServiceList();
		
		
		//头部图片滑动的集合
		LinearLayout.LayoutParams imageView_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		List<VenuesDetailPicList> PicList = venuesInformation.getPicList();
		//头部bar给viewPager加集合
		for(int i=0;i<PicList.size();i++){
			ImageView imageView=new ImageView(context);
			imageView.setLayoutParams(imageView_lp);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageLoader.displayImage(Config.BASE_URL+PicList.get(i).getPicUrl(), imageView);		
			imageviewList.add(imageView);
		}
		viewPager.setAdapter(new VenuesDetailViewPager(imageviewList,this,response));		

		//服务信息里面textview使用
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				   LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 10);
		
		//服务信息里图片使用	
		int px = Tools.dip2px(context, 20);//将图片宽高设置成20dp，将20dp转换为像素单位
		LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(px, 
				   px);
		lp2.setMargins(px, px, px, px);		   
		
		//服务信息里装textview和图片容器使用
		LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				   LinearLayout.LayoutParams.WRAP_CONTENT);
		lp1.weight=1;
		
	//为服务信息动态添加图片
	 for(int i=0;i<list.size();i++){
		    LinearLayout layout=new LinearLayout(context);
		    layout.setOrientation(LinearLayout.VERTICAL);
		    layout.setLayoutParams(lp1);
		    layout.setGravity(Gravity.CENTER);
		    
			ImageView imageView=new ImageView(context);
			TextView textView=new TextView(context);
			imageLoader.displayImage(Config.BASE_URL+list.get(i).getPicUrl(), imageView);
			textView.setText(list.get(i).getServiceName());
			
			
			imageView.setLayoutParams(lp2);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			textView.setLayoutParams(lp);
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setSingleLine();
			
			layout.addView(imageView);
			layout.addView(textView);
			linear_imageView.addView(layout);
		}
		btn_venues_appointment.setFocusable(true);
	}

	@Override
	protected void onStart() {
		scrollView.fullScroll(View.FOCUS_UP);
		super.onStart();
	}


	/**
	 * 初始化控件 
	 * */
	private void findView() {
		textView_venues_name=(TextView)this.findViewById(R.id.textView_venues_name);
		textView_venues_address=(TextView)this.findViewById(R.id.textView_venues_address);
		textView_venues_phone=(TextView)this.findViewById(R.id.textView_venues_phone);
		textView_instructions=(TextView)this.findViewById(R.id.textView_instructions);
		drawerlayout=(DrawerLayout)this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);		
		
		textView_venues_favornumber=(TextView)this.findViewById(R.id.textView_venues_favornumber);
		textView_venues_badnumber=(TextView)this.findViewById(R.id.textView_venues_badnumber);
		viewPager=(ViewPager)this.findViewById(R.id.venues_detail_viewPager);
		imageView_map=(ImageView)this.findViewById(R.id.imageView_map);
		linear_imageView=(LinearLayout)this.findViewById(R.id.linear_imageView);
		btn_venues_appointment=(Button)this.findViewById(R.id.btn_venues_appointment);
		imageView_call=(ImageView)this.findViewById(R.id.imageView_call);
		ll_venues_detail_comments=(LinearLayout)this.findViewById(R.id.ll_venues_detail_comments);
		scrollView=(ScrollView)this.findViewById(R.id.scrollView);
		imageButton_back=(ImageButton)this.findViewById(R.id.imageButton_back);
		//button的点击事件
		btn_venues_appointment.setOnClickListener(this);
		btn_venues_appointment.setFocusable(false);
		imageView_call.setOnClickListener(this);
		imageView_map.setOnClickListener(this);
		ll_venues_detail_comments.setOnClickListener(this);
		imageButton_back.setOnClickListener(this);
	}
	
	
	/**
	 * GET请求网络数据
	 * */
	private void httpGetJson(){	
		loadDialog.show();
		StringBuffer buffer=new StringBuffer();
		buffer.append(Config.VENUES_DETAILS_URL);
		String param = Tools.joinUrlByParam(hardId,venuesId,supportId);
		buffer.append(param);		
		if(!NetManager.isNetworkConnected(context)){
			Toast.makeText(context, "网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
			return;			
		}

		StringRequest request=new StringRequest(Method.GET, buffer.toString(), 
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub							
						try {
							UIManager.toggleDialog(loadDialog);
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
		mRequestQueue.add(request);
	}

	/**
	 * 解析JSON数据
	 * @throws JSONException 
	 * */
	private void parseJson(String response) throws JSONException {
		DebugUtility.showLog("场馆详情返回数据:"+response);
		Gson gson = new Gson();
		JSONObject object=new JSONObject(response);	
		String array;
		Type type = new TypeToken<VenuesInformation>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		if(resultCode==0){
			//返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}else if(resultCode==1){
			//返回成功		
			array=object.getString("getVenuesDetail");
			//得到集合
			venuesInformation=gson.fromJson(array, type);
			init(response);
		}	
		
	}


	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_venues_appointment:	
			Intent intent=new Intent(context,VenuesBookActivity.class);
			Bundle bundle1=new Bundle();
			bundle1.putInt("supportId", venuesInformation.getSupportId());
			bundle1.putString("venuesThumbnail", venuesInformation.getVenuesThumbnail());
			bundle1.putString("venuesId", venuesId);
			intent.putExtras(bundle1);
			startActivity(intent);	
			break;
		case R.id.imageView_call:
			 Intent intent1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+textView_venues_phone.getText())); 
			 intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			 startActivity(intent1);			
			 break;
		case R.id.imageView_map:
			Intent intent3=new Intent(VenuesDetailsActivity.this,BaiDuMapActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("Latitude", venuesInformation.getPointY());
			bundle.putString("Longitude", venuesInformation.getPointX());
			intent3.putExtras(bundle);
			startActivity(intent3);				
			break;
		case R.id.ll_venues_detail_comments:
			Intent intent2=new Intent(VenuesDetailsActivity.this,EvaluationActivity.class);
		    Bundle bundle2=new Bundle();
		    bundle2.putInt("supportId", venuesInformation.getSupportId());
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		case R.id.imageButton_back:
			finish();
			break;	
		}		
	}
}
