package com.ak.qmyd.tools;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * @author HM
 * @date 2015-6-5 下午4:55:00
 */
public class LinearLayoutForListView extends LinearLayout {

	private BaseAdapter adapter;
	private OnClickListener onClickListener = null;

	/**
	 * 绑定布局
	 */
	public void bindLinearLayout() {
		int count = adapter.getCount();
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);

			v.setOnClickListener(this.onClickListener);
			addView(v, i);
		}
	}

	public LinearLayoutForListView(Context context) {
		super(context);
	}

}
