package com.ak.qmyd.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

public class ProjectDetailActivity extends BaseActivity implements OnClickListener{
	
	private Button btn_sign_up;
	private Button btn_activity_prizes;
	private Button btn_activity_rules;
	private TextView textView_activity_time;
	private TextView textView_activity_instructions;
	private TextView textView_participating_qualification;
    private	ImageView  imageView_point;
    private int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		getScreenWidth();
		findView();
	}
	private void findView() {
		// TODO Auto-generated method stub
		btn_sign_up=(Button)findViewById(R.id.btn_sign_up);
		btn_activity_prizes=(Button)findViewById(R.id.btn_activity_prizes);
		btn_activity_rules=(Button)findViewById(R.id.btn_activity_rules);
		imageView_point=(ImageView)findViewById(R.id.imageView_point);
		btn_sign_up.setOnClickListener(this);
		btn_activity_prizes.setOnClickListener(this);
		btn_activity_rules.setOnClickListener(this);		
		
	}
	@Override
	public void onClick(View arg0) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
	     switch(arg0.getId()){
	     case R.id.btn_sign_up:
	    	 Intent intent=new Intent(this,BuyTicketActivity.class);
	    	 startActivity(intent);
	    	 break;
	     case R.id.btn_activity_prizes:	       
	       lp.leftMargin=10+((width-20)/4);	   
	       imageView_point.setLayoutParams(lp);
	    	 break;
	     case R.id.btn_activity_rules:		       
		       lp.leftMargin=3*((width-20)/4);	
		       imageView_point.setLayoutParams(lp);
		      break;		 
	     }		
	}
	
	
	/**
	 * 得到屏幕的宽度
	 * */
	private void getScreenWidth(){		
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
	}	
}
