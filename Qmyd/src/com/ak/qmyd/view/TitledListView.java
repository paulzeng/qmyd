package com.ak.qmyd.view;

import com.ak.qmyd.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class TitledListView extends ListView {

	private View mTitle;

	public TitledListView(Context context) {
		super(context);
	}

	public TitledListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TitledListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mTitle != null) {
			measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mTitle != null) {
			mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawChild(canvas, mTitle, getDrawingTime());
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		mTitle = inflater.inflate(R.layout.activity_my_train_history_title, this, false);
	}

	public void moveTitle() {
		View bottomChild = getChildAt(0);
		if (bottomChild != null) {
			int bottom = bottomChild.getBottom();
			int height = mTitle.getMeasuredHeight();
			int y = 0;
			if (bottom < height) {
				y = bottom - height;
			}
			mTitle.layout(0, y, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight() + y);
		}
	}

	public void updateTitle(String month,String total_time) {
		if (month != null) {
			TextView tv_train_history_month = (TextView) mTitle.findViewById(R.id.tv_train_history_month);
			TextView tv_train_history_total_time = (TextView) mTitle.findViewById(R.id.tv_train_history_total_time);
			tv_train_history_month.setText(month);
			tv_train_history_total_time.setText(total_time);
		}
		mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
	}
}

