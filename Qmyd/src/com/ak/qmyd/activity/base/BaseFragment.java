package com.ak.qmyd.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ak.qmyd.R;

/**
 * @author HM
 * @date 2015-4-18 下午3:03:22
 */
public class BaseFragment extends Fragment implements IActivitySupport {

	private boolean isActive;// Activity是否在活动
	protected Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		isActive = true;
		context = getActivity();

	}

	@Override
	public void onStart() {
		isActive = true;
		super.onStart();
	}

	@Override
	public void onPause() {
		isActive = false;
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void showToast(String text, int longint) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, text, longint).show();

	}

	@Override
	public void showToast(String text) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showToast(int resId) {
		// TODO Auto-generated method stub
		if (isActive)
			Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	public void showFootView(Activity activity){
		 LayoutInflater LayoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View view=LayoutInflater.inflate(R.layout.view_bottom_bar, null);
		View footParentView=activity.findViewById(R.id.view_foot_parent);
		if(footParentView!=null&&footParentView instanceof LinearLayout){
			((LinearLayout)footParentView).addView(view);
		}
	}
	@Override
	public void startActivity(Class<?> clazz ,String typeId) {
		// TODO Auto-generated method stub
		Intent it=new Intent(context, clazz);
		it.putExtra("typeId", typeId);
		startActivity(it);
	}
}
