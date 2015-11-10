package com.ak.qmyd.dialog;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.VenuesAppointmentActivity;
import com.ak.qmyd.bean.AreaList;
import com.ak.qmyd.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecommendedDialog extends Dialog implements View.OnClickListener {

	private Context context;

	private int DialogHeight;

	private LinearLayout btn_linear_tuijian, btn_linear_zuijin,
			btn_linear_haoping;

	private ImageView imageView_tuijian, imageView_zuijin, imageView_haoping,
			iv_recommend_dialog_search;

	private RecommendedClick recommendedClick;

	private ImageButton ib_recommend_dialog_back;

	private TextView tv_recommend_dialog_circle,
			tv_recommend_dialog_recommended;

	public RecommendedDialog(Context context) {
		super(context);
		this.context = context;
	}

	public RecommendedDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommended_dialog);
		// 设置位置
		setLayout();
		findView();
		select();
		this.setCanceledOnTouchOutside(true);
	}

	private void select() {
		cleariamgeView();
		switch (Integer.parseInt(VenuesAppointmentActivity.seqArrangement)) {
		case 2:
			imageView_tuijian.setVisibility(View.VISIBLE);
			btn_linear_tuijian.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			break;
		case 1:
			imageView_zuijin.setVisibility(View.VISIBLE);
			btn_linear_zuijin.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			break;
		case 3:
			imageView_haoping.setVisibility(View.VISIBLE);
			btn_linear_haoping.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			break;
		}

	}

	/**
	 * 控件的初始化
	 * */
	@SuppressLint("ResourceAsColor")
	private void findView() {
		ib_recommend_dialog_back = (ImageButton) findViewById(R.id.ib_recommend_dialog_back);
		iv_recommend_dialog_search = (ImageView) findViewById(R.id.iv_recommend_dialog_search);
		tv_recommend_dialog_circle = (TextView) findViewById(R.id.tv_recommend_dialog_circle);
		tv_recommend_dialog_recommended = (TextView) findViewById(R.id.tv_recommend_dialog_recommended);
		imageView_tuijian = (ImageView) findViewById(R.id.imageView_tuijian);
		imageView_zuijin = (ImageView) findViewById(R.id.imageView_zuijin);
		imageView_haoping = (ImageView) findViewById(R.id.imageView_haoping);

		btn_linear_tuijian = (LinearLayout) findViewById(R.id.btn_linear_tuijian);
		btn_linear_zuijin = (LinearLayout) findViewById(R.id.btn_linear_zuijin);
		btn_linear_haoping = (LinearLayout) findViewById(R.id.btn_linear_haoping);
		btn_linear_tuijian.setOnClickListener(this);
		btn_linear_zuijin.setOnClickListener(this);
		btn_linear_haoping.setOnClickListener(this);
		ib_recommend_dialog_back.setOnClickListener(this);
		iv_recommend_dialog_search.setOnClickListener(this);
		tv_recommend_dialog_circle.setOnClickListener(this);
		tv_recommend_dialog_recommended.setOnClickListener(this);

	}

	/**
	 * dialog的宽、高和位置
	 * */
	private void setLayout() {
		WindowManager m = ((Activity) context).getWindowManager();
		Window window = this.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		window.getDecorView().setPadding(0, 0, 0, 0);
		window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
		LayoutParams layout = window.getAttributes();
		window.setGravity(Gravity.LEFT | Gravity.TOP);
		layout.width = Tools.getWidth(context);
		Measure();
		layout.height = DialogHeight;
		layout.x = 0;
		layout.y = dialogTopMargin();
		window.setAttributes(layout);

	}

	private int dialogTopMargin() {
		// 上边距由两部分组成头部是54DIP，按纽是50DIP
		final float scale = context.getResources().getDisplayMetrics().density;
		int headHeight = (int) ((54.0) * scale + 0.5f);
		int textHeight = (int) ((50.0) * scale + 0.5f);
		int dialogtopmargin = headHeight + textHeight;
		return 0;
	}

	/**
	 * 测量dialog的自身高度
	 * */
	private void Measure() {
		View DialogView = LayoutInflater.from(context).inflate(
				R.layout.recommended_dialog, null);
		DialogView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		DialogHeight = DialogView.getMeasuredHeight();

	}

	/**
	 * 点击事件
	 * */
	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_linear_tuijian:
			btn_linear_tuijian.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			VenuesAppointmentActivity.seqArrangement = "2";
			recommendedClick.RecommendClick();
			this.dismiss();
			break;
		case R.id.btn_linear_zuijin:
			btn_linear_zuijin.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			VenuesAppointmentActivity.seqArrangement = "1";
			recommendedClick.RecommendClick();
			this.dismiss();
			break;
		case R.id.btn_linear_haoping:
			btn_linear_haoping.setBackgroundColor(context.getResources()
					.getColor(R.color.recommend_bg));
			VenuesAppointmentActivity.seqArrangement = "3";
			recommendedClick.RecommendClick();
			this.dismiss();
			break;
		case R.id.ib_recommend_dialog_back:
			recommendedClick.RecommendClick(0);
			this.dismiss();
			break;
		case R.id.iv_recommend_dialog_search:
			recommendedClick.RecommendClick(1);
			this.dismiss();
			break;
		case R.id.tv_recommend_dialog_circle:
			recommendedClick.RecommendClick(2);
			this.dismiss();
			break;
		case R.id.tv_recommend_dialog_recommended:
			this.dismiss();
			break;
		}
	}

	private void cleariamgeView() {
		imageView_tuijian.setVisibility(View.GONE);
		imageView_zuijin.setVisibility(View.GONE);
		imageView_haoping.setVisibility(View.GONE);
	}

	public void setRecommendedClick(RecommendedClick recommendedClick) {
		this.recommendedClick = recommendedClick;
	}

	/**
	 * 创建接口用来回调
	 * */
	public interface RecommendedClick {
		public void RecommendClick();

		public void RecommendClick(int type);
	}
}
