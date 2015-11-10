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
	 * �µ���ͼ
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
		
		Log.i("ceshi", "��������:"+cityName);
		Log.i("ceshi", "����code:"+cityCode);
		
		mRequestQueue=Volley.newRequestQueue(context);
		hardId=MyApplication.instance.getHardId();
		httpGet();
	}
	
	/**
	 * ����GET����õ�JSON���ݴ浽list������
	 * */
	private void httpGet() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Config.BUSINESS_CIRCLE_URL);
		String param = Tools.joinUrlByParam(hardId,cityCode);
		buffer.append(param);
		
		StringRequest request = new StringRequest(Request.Method.GET,
				buffer.toString(), new Listener<String>() {
					// ����ɹ��ķ���
					@Override
					public void onResponse(String response) {
						// ����JSON����						
						try {
							parseJson(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
						
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					// ����ʧ�ܵĵ���
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
		// ���ص�������Ϣ
		String info = object.getString("resultInfo");
		if(resultCode==0){
			//����ʧ��
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}else if(resultCode==1){
			//���سɹ�		
			array=object.getString("areaList");	
			//�õ�����
			mlist=gson.fromJson(array, type);		
			iniData();	
		}
	}
	
	
	private void iniData() {
		alist=new ArrayList<AreaList>();
		AreaList area=new AreaList();
		area.setAreaCode(cityCode);
		area.setAreaName("ȫ��");
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
		//����λ��
		setLayout();
		findView();	
		this.setCanceledOnTouchOutside(true);
	}


	/**
	 * ��ʼ��
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
	 * �ص�dialog
	 * */
	private void close(){		
		this.dismiss();
	}
	
	
	/**
	 * dialog�Ŀ��ߺ�λ��
	 * */
	private void setLayout() {
		WindowManager m = ((Activity) context).getWindowManager();  
		Window window = this.getWindow();
		Display d = m.getDefaultDisplay();  //Ϊ��ȡ��Ļ����  
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
	 * ������ͼ�ĸ߶�
	 * */
	
	private void Measure(){
		View DialogView = LayoutInflater.from(context).inflate(R.layout.businesscircle_dialog, null);
		DialogView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		DialogHeight = DialogView.getMeasuredHeight() + 405;
	}

	/**
	 * ����dialog�����ϱߵľ���
	 * @return 
	 * */
	
	private int dialogTopMargin(){
		//�ϱ߾������������ͷ����54DIP����Ŧ��50DIP,����ĸ߶�����xml���õģ����xml�߶ȱ仯������Ҫ�ı�
		  final float scale = context.getResources().getDisplayMetrics().density; 
          int headHeight = (int)((54.0) * scale + 0.5f); 
		  int textHeight = (int)((50.0) * scale + 0.5f);
		  int dialogtopmargin = headHeight+textHeight;
		  return 0;
	}

	
	/**
	 * ��Ŧ����¼�
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
	 * �����ӿ������ص�
	 * */
	public interface GridViewClick{	
		public void buttonClick(AreaList areaList);
		public void buttonClick(int type);
	}

}
