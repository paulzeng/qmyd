package com.ak.qmyd.activity.adapter;

import java.util.List;

import com.ak.qmyd.bean.model.SportType;
import com.ak.qmyd.fragment.HomeContentFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/** 
 * @author HM
 * @date 2015-4-28 ÏÂÎç3:57:20
 */
public class FragmentTabContentAdapter extends FragmentStatePagerAdapter {

	private List<SportType>  listTypes;
	private String typeId;
	public FragmentTabContentAdapter(FragmentManager fm,List<SportType>  listTypes) {
		super(fm);
		this.listTypes=listTypes;
	}

	
	public void setListTypes(List<SportType> listTypes) {
		this.listTypes = listTypes;
		notifyDataSetChanged();
	}


	@Override
	public Fragment getItem(int position) {
		Fragment f=new HomeContentFragment(listTypes.get(position).getTypeId());
		typeId = listTypes.get(position).getTypeId();
		return f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listTypes == null ? 0 : listTypes.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return listTypes.get(position).getTypeName();
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		 
		return  POSITION_NONE;
	}
	
	public String getTypeId(){
		return typeId;
	}
}
