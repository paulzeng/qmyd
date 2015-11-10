package com.ak.qmyd.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;

/** 
 * @author JGB
 * @date 2015-5-29 下午2:31:53
 */
public class AlarmDialog {


	public interface OnActionSheetSelected {
		void onClick(int which);
	}

	private AlarmDialog() {
	}

	public static  Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_alarm_dialog, null);
		WindowManager m = ((Activity) context).getWindowManager();  
		Window win = dlg.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框
		p.height = (int) (d.getHeight());   //高度设置为屏幕的高
		p.width = (int) (d.getWidth());    //宽度设置为屏幕的宽度 
		dlg.getWindow().setAttributes(p);     //设置生效  
//		layout.setMinimumWidth((int) (d.getWidth()));
		TextView tv_alarm_text = (TextView) layout
				.findViewById(R.id.tv_alarm_title);
		ImageView iv_alarm_nazhong = (ImageView) layout
				.findViewById(R.id.iv_alarm_nazhong);
		TextView tv_alarm_cancel = (TextView) layout
				.findViewById(R.id.tv_alarm_cancel);
		tv_alarm_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0);
				dlg.dismiss();
			}
		});

		iv_alarm_nazhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});
		tv_alarm_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(2);
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
	

}
