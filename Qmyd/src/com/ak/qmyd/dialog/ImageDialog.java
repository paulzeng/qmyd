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
import com.ak.qmyd.dialog.MenuDialog.OnActionSheetSelected;

/** 
 * @author HM
 * @date 2015-6-8 下午9:22:26
 */
public class ImageDialog {
	public interface OnActionSheetSelected {
		void onClick(int which);
	}
	private ImageDialog() {
	}
	
	public static  Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener,String img) {
		final Dialog dlg = new Dialog(context, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_gridview_image_item, null);
		WindowManager m = ((Activity) context).getWindowManager();  
		Window win = dlg.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框
		p.height = (int) (d.getHeight());   //高度设置为屏幕的高度
		p.width = (int) (d.getWidth());    //宽度设置为屏幕的宽度 
		dlg.getWindow().setAttributes(p);     //设置生效  
		layout.setMinimumWidth((int) (d.getWidth()));
		layout.setMinimumHeight((int) (d.getHeight()));
		ImageView iv_image_item = (ImageView) layout
				.findViewById(R.id.iv_image_item);
		iv_image_item.setBackgroundResource(Integer.parseInt(img));
		iv_image_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0);
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
