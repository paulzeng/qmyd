package com.ak.qmyd.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;

public class BuyTicketActivity extends BaseActivity {
	
	private TextView textView_data;
	private TextView textView_time;
	private TextView textView_address;
	private TextView textView_organizer;
	private TextView textView_undertake;
	private TextView textView_media;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_ticket);
		findView();
	}

	private void findView() {
	   textView_data=(TextView)findViewById(R.id.textView_data);
	   textView_time=(TextView)findViewById(R.id.textView_time);
	   textView_address=(TextView)findViewById(R.id.textView_address);
	   textView_organizer=(TextView)findViewById(R.id.textView_organizer);
	   textView_undertake=(TextView)findViewById(R.id.textView_undertake);
	   textView_media=(TextView)findViewById(R.id.textView_media);		
	}
}
