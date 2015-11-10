package com.ak.qmyd.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseActivity;
import com.ak.qmyd.view.PickerView;
import com.ak.qmyd.view.PickerView.onSelectListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RecordTrainTimeActivity extends BaseActivity implements OnClickListener{

	protected static final int TRAIN_ALERT_HOUR_A = 0;
	protected static final int TRAIN_ALERT_MINUTE_A = 1;
	protected static final int TRAIN_ALERT_HOUR_B = 2;
	protected static final int TRAIN_ALERT_MINUTE_B = 3;
	private ImageButton ib_record_train_return;
	private PickerView pv_record_train_hour_a,pv_record_train_minute_a,pv_record_train_hour_b,pv_record_train_minute_b;
	private Button bt_record_train_complete;
	private boolean typeA = false;
	private boolean typeB = false;
	private boolean typeC = false;
	private boolean typeD = false;
	private int currentHourA,currentMinuteA;
	private int currentHourB,currentMinuteB;
	
private Handler mHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TRAIN_ALERT_HOUR_A:
				currentHourA = (Integer.parseInt((String) msg.obj));
				typeA = true;
				break;
			case TRAIN_ALERT_MINUTE_A:
				currentMinuteA = (Integer.parseInt((String) msg.obj));
				typeB = true;
				break;
			case TRAIN_ALERT_HOUR_B:
				currentHourB = (Integer.parseInt((String) msg.obj));
				typeC = true;
				break;
			case TRAIN_ALERT_MINUTE_B:
				currentMinuteB = (Integer.parseInt((String) msg.obj));
				typeD = true;
				break;

			default:
				break;
			}
		};
	};
	private int hour;
	private int minute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_train_time);
		
		RequestQueue queue = Volley.newRequestQueue(this);
		// 除了StringRequest JsonObjectRequest JsonArrayRequest ImageRequest 等
		// 也可以继承Request自定义 (Request是泛型)
		StringRequest strRequest = new StringRequest(Request.Method.GET,
				"http://xxxx.xxx", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						// 通讯成功的 返回数据
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// 通讯失败的 抛出异常
					}
				});
		queue.add(strRequest);// 加入到通讯队列中
		
		findView();
		initView();
		initPickerView(pv_record_train_hour_a,pv_record_train_minute_a,TRAIN_ALERT_HOUR_A,TRAIN_ALERT_MINUTE_A);
		initPickerView(pv_record_train_hour_b,pv_record_train_minute_b,TRAIN_ALERT_HOUR_B,TRAIN_ALERT_MINUTE_B);
	}

	private void initPickerView(PickerView pv_hour,PickerView pv_minute,final int whatA,final int whatB) {

		List<String> data = new ArrayList<String>();  
        List<String> seconds = new ArrayList<String>();  
        for (int i = 0; i < 24; i++)  
        {  
            data.add(i < 10 ? "0" + i : "" + i);  
        }  
        for (int i = 0; i < 60; i++)  
        {  
            seconds.add(i < 10 ? "0" + i : "" + i);  
        }  
        pv_hour.setData(data);  
        hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        pv_hour.setSelected(hour);
        pv_hour.setOnSelectListener(new onSelectListener()  
        {  
  
            @Override  
            public void onSelect(String text)  
            {  
            	 sendMyMessage(text,whatA);
            }

			
        });  
        pv_minute.setData(seconds);  
        minute = Integer.parseInt(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));
        pv_minute.setSelected(minute);
        pv_minute.setOnSelectListener(new onSelectListener()  
        {  
  
            @Override  
            public void onSelect(String text)  
            {  
                sendMyMessage(text,whatB);
            }  
        });  
		
	}

	
	private void sendMyMessage(String text,int what) {
		Message msg = Message.obtain();
    	 msg.what = what;
    	 msg.obj = text;
    	 mHandler.sendMessage(msg);
	} 
	private void initView() {
		ib_record_train_return.setOnClickListener(this);
		bt_record_train_complete.setOnClickListener(this);
	}

	private void findView() {
		ib_record_train_return = (ImageButton) findViewById(R.id.ib_record_train_return);
		pv_record_train_hour_a = (PickerView) findViewById(R.id.pv_record_train_hour_a);
		pv_record_train_minute_a = (PickerView) findViewById(R.id.pv_record_train_minute_a);
		pv_record_train_hour_b = (PickerView) findViewById(R.id.pv_record_train_hour_b);
		pv_record_train_minute_b = (PickerView) findViewById(R.id.pv_record_train_minute_b);
		bt_record_train_complete = (Button) findViewById(R.id.bt_record_train_complete);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_record_train_return:
			finish();
			break;
		case R.id.bt_record_train_complete:
			finish();
			if(!typeA && !typeB && !typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(minute)+"分到"+formatTime(hour)+"时"+formatTime(minute)+"分");
			}else if(!typeA && !typeB && typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(minute)+"分到"+formatTime(currentHourB)+"时"+formatTime(minute)+"分");
			}else if(!typeA && !typeB && !typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(minute)+"分到"+formatTime(hour)+"时"+formatTime(currentMinuteB)+"分");
			}else if(!typeA && !typeB && typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(minute)+"分到"+formatTime(currentHourB)+"时"+formatTime(currentMinuteB)+"分");
			}else if(typeA && !typeB && !typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(minute)+"分到"+formatTime(hour)+"时"+formatTime(minute)+"分");
			}else if(typeA && !typeB && typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(minute)+"分到"+formatTime(currentHourB)+"时"+formatTime(minute)+"分");
			}else if(typeA && !typeB && !typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(minute)+"分到"+formatTime(hour)+"时"+formatTime(currentMinuteB)+"分");
			}else if(typeA && !typeB && typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(minute)+"分到"+formatTime(currentHourB)+"时"+formatTime(currentMinuteB)+"分");
			}else if(!typeA && typeB && !typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(hour)+"时"+formatTime(minute)+"分");
			}else if(!typeA && typeB && typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(currentHourB)+"时"+formatTime(minute)+"分");
			}else if(!typeA && typeB && !typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(hour)+"时"+formatTime(currentMinuteB)+"分");
			}else if(!typeA && typeB && typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(hour)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(currentHourB)+"时"+formatTime(currentMinuteB)+"分");
			}else if(typeA && typeB && !typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(hour)+"时"+formatTime(minute)+"分");
			}else if(typeA && typeB && typeC && !typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(currentHourB)+"时"+formatTime(minute)+"分");
			}else if(typeA && typeB && !typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(hour)+"时"+formatTime(currentMinuteB)+"分");
			}else if(typeA && typeB && typeC && typeD){
				showToast("设置成功!设置时间为"+formatTime(currentHourA)+"时"+formatTime(currentMinuteA)+"分到"+formatTime(currentHourB)+"时"+formatTime(currentMinuteB)+"分");
			}
			Intent newIntent = new Intent(RecordTrainTimeActivity.this,TrainingListDatailActivity.class);
			startActivity(newIntent);
			break;
			
		default:
			break;
		}
	}
	
	private String formatTime(int time) {
		return time < 10 ? "0" + time : "" + time;
	}
}
