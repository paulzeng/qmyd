package com.ak.qmyd.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.qmyd.R;

public class UIManager {

	public static Dialog getLoadingDialog(Context context) {

		return getLoadingDialog(context, "请稍等...");
	}

	public static Dialog getLoadingDialog(Context context, Object loadingTextRes) {
		final Dialog dialog = new Dialog(context, R.style.netLoadingDialog);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.custom_progress_dialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = getScreenWidth(context) - dpToPx(context, 100);
		window.setGravity(Gravity.CENTER_VERTICAL);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialog_tv);
		if (loadingTextRes instanceof String) {
			titleTxtv.setText((String) loadingTextRes);
		}
		if (loadingTextRes instanceof Integer) {
			titleTxtv.setText((Integer) loadingTextRes);
		}

		return dialog;
	}

	public static void toggleDialog(Dialog loadDialog) {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}

	}

	/**
	 * action sheet dialog
	 * 
	 * @param context
	 * @param view
	 * @return
	 */

	public static Dialog getActionSheet(Context context, View view) {
		final Dialog dialog = new Dialog(context, R.style.action_sheet);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.action_sheet_cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = getScreenWidth(context);
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.action_sheet_animation);
		return dialog;
	}

	/**
	 * action sheet dialog
	 * 
	 * @param context
	 * @param view
	 * @return
	 */

	public static Dialog getCommonActionSheet(Context context, View view) {
		final Dialog dialog = new Dialog(context, R.style.action_sheet);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = getScreenWidth(context);
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.action_sheet_animation);
		return dialog;
	}

	public static Dialog getCommWarnDialog(Context context, String contentRes,
			final OnClickListener l) {
		final Dialog dialog = new Dialog(context, R.style.float_base);
		View view = View.inflate(context, R.layout.layout_common_dialog, null);
		TextView title = (TextView) view.findViewById(R.id.warn_title_tv);
		TextView content = (TextView) view.findViewById(R.id.warn_content_tv);
		TextView sure = (TextView) view.findViewById(R.id.warn_sure_bt);
		TextView cancle = (TextView) view.findViewById(R.id.warn_cancle_bt);
		title.setText("发现新的版本");
		content.setText(contentRes);
		sure.setText("立即下载");
		cancle.setText("以后再说");
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.warn_sure_bt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						l.onClick(v);
						dialog.dismiss();
					}
				});
		view.findViewById(R.id.warn_cancle_bt).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = getScreenWidth(context) - dpToPx(context, 50);
		window.setGravity(Gravity.CENTER_VERTICAL);

		return dialog;
	}

	public static int dpToPx(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(dpValue * scale);
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 计算listview item的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	 

}
