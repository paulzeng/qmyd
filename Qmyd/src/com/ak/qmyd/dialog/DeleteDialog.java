package com.ak.qmyd.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.dialog.MenuDialog.OnActionSheetSelected;

/** 
 * @author HM
 * @date 2015-6-12 上午10:40:05
 */
public class DeleteDialog {

	public interface OnActionSheetSelected {
		void onClick(int which);
	}

	private DeleteDialog() {
	}

	public static  Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener,String title) {
		final Dialog dlg = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_delete_dialog, null);
		WindowManager m = ((Activity) context).getWindowManager();  
		Window win = dlg.getWindow();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		win.getDecorView().setPadding(0, (int) (d.getHeight() * 0.25), 0, 0);
		win.getDecorView().setBackgroundColor(Color.TRANSPARENT);
		android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框
		p.height = (int) (d.getHeight() * 0.5);   //高度设置为屏幕的0.3
		p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的宽度 
		dlg.getWindow().setAttributes(p);     //设置生效  
//		layout.setMinimumWidth((int) (d.getWidth()));
		TextView tv_delete_dialog_title = (TextView) layout
				.findViewById(R.id.tv_delete_dialog_title);
		TextView tv_delete_dialog_cancel = (TextView) layout
				.findViewById(R.id.tv_delete_dialog_cancel);
		TextView tv_delete_dialog_ok = (TextView) layout
				.findViewById(R.id.tv_delete_dialog_ok);
		tv_delete_dialog_title.setText(title);
		tv_delete_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0);
				dlg.dismiss();
			}
		});

		tv_delete_dialog_ok.setOnClickListener(new OnClickListener() {

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
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}
	
}
