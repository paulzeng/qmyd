package com.ak.qmyd.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.CityLocationActivity;
import com.ak.qmyd.bean.result.EndTrainPlanResult;
import com.ak.qmyd.tools.Tools;
import com.ak.qmyd.view.PickerView;
import com.ak.qmyd.view.ActionSheet.OnActionSheetSelected;
import com.ak.qmyd.view.PickerView.onSelectListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author JGB
 * @date 2015-5-7 下午9:25:23
 */
public class MenuDialog{

	public interface OnActionSheetSelected {
		void onClick(int which);
	}

	private MenuDialog() {
	}

	public static  Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_end_menu, null);
		WindowManager m = ((Activity) context).getWindowManager();  
		Window win = dlg.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框
		p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
		p.width = (int) (d.getWidth());    //宽度设置为屏幕的宽度 
		dlg.getWindow().setAttributes(p);     //设置生效  
//		layout.setMinimumWidth((int) (d.getWidth()));
		TextView tv_end_train = (TextView) layout
				.findViewById(R.id.tv_end_train);
		TextView tv_cancel = (TextView) layout
				.findViewById(R.id.tv_cancel);
		tv_end_train.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0);
				dlg.dismiss();
			}
		});

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(1);
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
