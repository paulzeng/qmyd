package com.ak.qmyd.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.ak.qmyd.R;

public class DongTaiBaseFragment extends Fragment {
	public FragmentActivity fa;
	public FragmentManager fm;
	public View emptyView;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		fa = (FragmentActivity) activity;
		fm = getFragmentManager();
		emptyView = View.inflate(fa, R.layout.empty_view, null);
		super.onAttach(activity);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		findViews();
		init();
		setListener();

	}
	protected void findViews() {
	};

	protected void init() {
	}

	protected void setListener() {
	}

}
