package com.ak.qmyd.adapt;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.CityName;








import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapt extends BaseAdapter {
	
	
	private List<CityName> list;
	
	private Context context;
	

	public CityListAdapt(List<CityName> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	 * @param list
	 */
	public void updateListView(List<CityName> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		 CityName cityName = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.city_listview_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
		
		//�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(cityName.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		
		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}
	
	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}
	
	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	public int getPositionForSection1(String section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			String firstChar = sortStr.toUpperCase();
			
			if (firstChar.equals(section)) {
				return i;
			}
		}
		
		return -1;
	}
}
