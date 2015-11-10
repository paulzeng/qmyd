package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** 
 * @author HM
 * @date 2015-4-18 ÏÂÎç1:18:30
 */
public class DynamicFramnetPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public DynamicFramnetPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragments=fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments==null?0:fragments.size();
	}

}
