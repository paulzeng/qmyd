package com.ak.qmyd.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.DeliverData;
import com.ak.qmyd.bean.OrderList;

public class FinishPayActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView textView_order_number,textView_venues_name,textView_venues_address;
	
	private LinearLayout linear_add_time;
	
	private String orderCode,venuesName,venuesAddress;
	
	private List<OrderList> mList;
	
	private Context context;
	
	private ImageButton imageButton_back;
	
	private DrawerLayout drawerlayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_pay);
		context=getApplicationContext();
		orderCode=getIntent().getExtras().getString("orderCode");
		venuesName=getIntent().getExtras().getString("venuesName");
		venuesAddress=getIntent().getExtras().getString("venuesAddress");
		DeliverData data = (DeliverData) getIntent().getExtras().getSerializable("orderList");
		mList=data.getMlist();	
		findView();
		iniData();
	}

	private void iniData() {
		textView_order_number.setText(orderCode);
		textView_venues_name.setText(venuesName);
		textView_venues_address.setText(venuesAddress);
		//动态添加时间排列
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp1.setMargins(0, 0, 20, 0);
		
		for(int i=0;i<mList.size();i++){
			LinearLayout layout=new LinearLayout(context);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setLayoutParams(lp);
			
			TextView text1=new TextView(context);
			text1.setTextSize(18);
			text1.setText(mList.get(i).getVenuesDate());
			text1.setLayoutParams(lp1);
			text1.setTextColor(getResources().getColor(R.color.gray));
			layout.addView(text1);
			
			TextView text2=new TextView(context);
			text2.setTextSize(18);
			text2.setText(mList.get(i).getReserveTimeSlot());
			text2.setLayoutParams(lp1);
			text2.setTextColor(getResources().getColor(R.color.gray));
			layout.addView(text2);
					
			linear_add_time.addView(layout);
		}
	}

	private void findView() {
		textView_order_number=(TextView)this.findViewById(R.id.textView_order_number);
		textView_venues_name=(TextView)this.findViewById(R.id.textView_venues_name);
		textView_venues_address=(TextView)this.findViewById(R.id.textView_venues_address);
		linear_add_time=(LinearLayout)this.findViewById(R.id.linear_add_time);
		imageButton_back=(ImageButton)this.findViewById(R.id.imageButton_back);
		drawerlayout=(DrawerLayout)this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
		imageButton_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.imageButton_back:
			Intent intent=new Intent(FinishPayActivity.this,VenuesDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   
			startActivity(intent);			
			break;	
		}	
	}
	
	/**
	 * 监听返回键
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
	    	Intent intent=new Intent(FinishPayActivity.this,VenuesDetailsActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	    	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);   
			startActivity(intent);	
	    	return true;
	    }
		return super.onKeyDown(keyCode, event);
	}
}
