package com.ak.qmyd.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.view.PickerView.onSelectListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author JGB
 * @date 2015-5-25 下午5:21:29
 */
public class ActionSheet{
	protected  final static int TRAIN_ALERT_HOUR = 0;
	protected  final static int TRAIN_ALERT_MINUTE = 1;
	private static  PickerView pv_msg_alert_hour;
	private static  PickerView pv_msg_alert_minute;
	private static  int currentHour;
	private static int currentMinute;
	private static  boolean typeA = false;
	private static boolean typeB = false;
	private static String time;
	private static int hour;
	private static int minute;
	private static  Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TRAIN_ALERT_HOUR:
				currentHour = (Integer.parseInt((String) msg.obj));
				typeA = true;
				break;
			case TRAIN_ALERT_MINUTE:
				currentMinute = (Integer.parseInt((String) msg.obj));
				typeB = true;
				break;

			default:
				break;
			}
		};
	};

	public interface OnActionSheetSelected {
		void onClick(int which,String time);
	}

	private ActionSheet() {
	}

	public static  Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_msg_alert_time, null);
		WindowManager m = ((Activity) context).getWindowManager();  
		Window win = dlg.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框
		hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
		minute = Integer.parseInt(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));
		p.height = (int) (d.getHeight() * 0.5);   //高度设置为屏幕的0.5
		p.width = (int) (d.getWidth());    //宽度设置为屏幕的宽度
		dlg.getWindow().setAttributes(p);     //设置生效  
//		layout.setMinimumWidth((int) (d.getWidth()));
		TextView tv_msg_alert_cancel = (TextView) layout
				.findViewById(R.id.tv_msg_alert_cancel);
		TextView tv_msg_alert_complete = (TextView) layout
				.findViewById(R.id.tv_msg_alert_complete);
		pv_msg_alert_hour = (PickerView) layout
				.findViewById(R.id.pv_msg_alert_hour);
		pv_msg_alert_minute = (PickerView) layout
				.findViewById(R.id.pv_msg_alert_minute);
		initPickerView();
		pv_msg_alert_hour.setSelected(hour);
		pv_msg_alert_minute.setSelected(minute);
		tv_msg_alert_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0,null);
				dlg.dismiss();
			}
		});

		tv_msg_alert_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!typeA && !typeB){
					time = formatTime(hour)+":"+formatTime(minute);
				}else if(typeA && !typeB){
					time = formatTime(currentHour)+":"+formatTime(minute);
				}else if(!typeA && typeB){
					time = formatTime(hour)+":"+formatTime(currentMinute);
				}else{
					time = formatTime(currentHour)+":"+formatTime(currentMinute);
				}
				actionSheetSelected.onClick(1,time);
				dlg.dismiss();
			}
		});
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	private static  void initPickerView() {

		List<String> data = new ArrayList<String>();
		List<String> seconds = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			data.add(i < 10 ? "0" + i : "" + i);
		}
		for (int i = 0; i < 60; i++) {
			seconds.add(i < 10 ? "0" + i : "" + i);
		}
		pv_msg_alert_hour.setData(data);

		pv_msg_alert_hour.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				sendMyMessage(text, TRAIN_ALERT_HOUR);
			}

		});
		pv_msg_alert_minute.setData(seconds);

		pv_msg_alert_minute.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				sendMyMessage(text, TRAIN_ALERT_MINUTE);
			}
		});

	}

	protected static  void sendMyMessage(String text,int what) {
			Message msg = Message.obtain();
	    	 msg.what = what;
	    	 msg.obj = text;
	    	 mHandler.sendMessage(msg);
	}
	
	private static String formatTime(int time) {
		return time < 10 ? "0" + time : "" + time;
	}
}
