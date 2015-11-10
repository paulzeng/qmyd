package com.ak.qmyd.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.tools.DebugUtility;

public class MovementAlertWayActivity extends BaseFragmentActivity implements OnClickListener{

	private ImageButton ib_alert_way_layout_return;
	private DrawerLayout mDrawerLayout;
	private ToggleButton tb_alert_way1,tb_alert_way2;
	private String way;
	private String way1 = null,way2 = null;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int NOTE_EDIT = 0;  //±à¼­×´Ì¬
	private static final int NOTE_INSERT = 1; // ²åÈë×´Ì¬
	private int state;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				way1 = (String) msg.obj;
				break;
			case 2:
				way1 = (String) msg.obj;
				break;
			case 3:
				way2 = (String) msg.obj;
				break;
			case 4:
				way2 = (String) msg.obj;
				break;
			default:
				break;
			}
		}
	};
	private String way3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alert_way_layout);
		findView();
		sp = getSharedPreferences("config", 0);
		editor = sp.edit();
		getWay();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl__alert_way_drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		initView();
		if (Intent.ACTION_INSERT == getIntent().getAction()) {
			state = NOTE_INSERT;
		}else if (Intent.ACTION_EDIT == getIntent().getAction()) {
			state = NOTE_EDIT;
		}
	}

	private void getWay() {
		way3 = getIntent().getStringExtra("way");
		if(way3.contains("ÁåÉù") && !way3.contains("Õð¶¯")){
			tb_alert_way1.setChecked(true);
			tb_alert_way2.setChecked(false);
		}else if(way3.contains("Õð¶¯") && !way3.contains("ÁåÉù")){
			tb_alert_way1.setChecked(false);
			tb_alert_way2.setChecked(true);
		}else if(way3.contains("Õð¶¯") && way3.contains("ÁåÉù")){
			tb_alert_way1.setChecked(true);
			tb_alert_way2.setChecked(true);
		}else{
			tb_alert_way1.setChecked(false);
			tb_alert_way2.setChecked(false);
		}
	}

	private void initView() {
		ib_alert_way_layout_return.setOnClickListener(this);
		tb_alert_way1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tb_alert_way1.setSelected(isChecked);
				if(isChecked){
					sendMyMessage("ÁåÉù", 1);
				}else{
					sendMyMessage("", 2);
					DebugUtility.showLog("way1" + way1);
					DebugUtility.showLog("way2" + way2);
//					if(!tb_alert_way2.isChecked()){
//						tb_alert_way2.setChecked(false);
//					}else if(tb_alert_way2.isChecked()){
//						tb_alert_way2.setChecked(true);
//					}
					if((way2 == null && way1 == "") && !tb_alert_way2.isChecked()){
						tb_alert_way2.setChecked(false);
					}else{
						tb_alert_way2.setChecked(true);
					}
				}
			}
		});
		tb_alert_way2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tb_alert_way2.setSelected(isChecked);
				if(isChecked){
					sendMyMessage("Õð¶¯", 3);
				}else{
					sendMyMessage("", 4);
					DebugUtility.showLog("way1" + way1);
					DebugUtility.showLog("way2" + way2);
//					if(!tb_alert_way1.isChecked()){
//						tb_alert_way1.setChecked(false);
//					}else if(tb_alert_way1.isChecked()){
//						tb_alert_way1.setChecked(true);
//					}
					if((way1 == null && way2 == "") && !tb_alert_way1.isChecked()){
						tb_alert_way1.setChecked(false);
					}else{
						tb_alert_way1.setChecked(true);
					}
				}
			}
		});
	}

	protected void sendMyMessage(Object obj, int what) {
		Message msg = Message.obtain();
		msg.obj = obj;
		msg.what = what;
		handler.sendMessage(msg);
	}

	private void findView() {
		ib_alert_way_layout_return = (ImageButton) findViewById(R.id.ib_alert_way_layout_return);
		tb_alert_way1 = (ToggleButton) findViewById(R.id.tb_alert_way1);
		tb_alert_way2 = (ToggleButton) findViewById(R.id.tb_alert_way2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_alert_way_layout_return:
			finish();
			if(NOTE_INSERT == state){
				skipInsertActivity(TrainAlertDetailActivity.class);
			}else if (NOTE_EDIT == state){
				skipEditActivity(TrainAlertDetailActivity.class);
			}
			break;

		default:
			break;
		}	
	}

	private void skipInsertActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_INSERT);
		DebugUtility.showLog( way1 + "....." + way2);
		newIntent.setClass(this, clazz);
		if(way1 != "" && way2 ==""){
			way = "ÁåÉù";
		}else if((way1 != null || way2 != null) && (way1 != "") && (way2 != "")){
			way = "ÁåÉùÕð¶¯";
		}else if(way1 == "" && way2 !=""){
			way = "Õð¶¯";
		}else if(way1 == null && way2 == null){
			way = getIntent().getStringExtra("way");
		}
	    DebugUtility.showLog(way + getIntent().getStringExtra("way") + "....." + way1 + "....." + way2);
		newIntent.putExtra("way", way);
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("timetype", getIntent().getStringExtra("timetype"));
		newIntent.putExtra("count", getIntent().getStringExtra("count"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("type", "2");
		editor.putString("way", way);
		editor.commit();
		startActivity(newIntent);
	}
	private void skipEditActivity(Class clazz) {
		Intent newIntent = new Intent(Intent.ACTION_EDIT);
		newIntent.setClass(this, clazz);
		if(way1 != "" && way2 ==""){
			way = "ÁåÉù";
		}else if((way1 != null || way2 != null) && (way1 != "") && (way2 != "")){
			way = "ÁåÉùÕð¶¯";
		}else if(way1 == "" && way2 !=""){
			way = "Õð¶¯";
		}else if(way1 == null && way2 == null){
			way = getIntent().getStringExtra("way");
		}
		newIntent.putExtra("id",getIntent().getStringExtra("id"));
		newIntent.putExtra("time",getIntent().getStringExtra("time"));
		newIntent.putExtra("mytime", getIntent().getStringExtra("mytime"));
		newIntent.putExtra("way", way);
		newIntent.putExtra("index",getIntent().getStringExtra("index"));
		newIntent.putExtra("timetype", getIntent().getStringExtra("timetype"));
		newIntent.putExtra("count", getIntent().getStringExtra("count"));
		newIntent.putExtra("week",getIntent().getStringExtra("week"));
		newIntent.putExtra("day",getIntent().getStringExtra("day"));
		newIntent.putExtra("sectionId", getIntent().getStringExtra("sectionId"));
		newIntent.putExtra("sectionName", getIntent().getStringExtra("sectionName"));
		newIntent.putExtra("isstart",getIntent().getStringExtra("isstart"));
		editor.putString("way", way);
		editor.commit();
		startActivity(newIntent);
	}
}
