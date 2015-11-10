package com.ak.qmyd.fragment;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.AreaList;
import com.ak.qmyd.bean.CityName;
import com.ak.qmyd.bean.ConstUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CitySearchFragment extends Fragment {

	View view;

	List<CityName> filterDateList;

	ListView listView;

	TextView textView;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
		filterDateList = (List<CityName>) bundle.getSerializable("cityList");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.city_search_fragment, null);
		findView();
		showData();
		return view;
	}

	private void showData() {
		if (filterDateList == null||filterDateList.size()==0) {
			textView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			textView.setText("没有相关数据");
		} else {
			listView.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
			CitySearchAdapt adapter = new CitySearchAdapt();
			listView.setAdapter(adapter);
		}

	}

	private void findView() {
		listView = (ListView) view.findViewById(R.id.listView1);
		textView = (TextView) view.findViewById(R.id.textView_no_data);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//发送广播，将选择的城市名字传给场管预约界面
				Intent intent=new Intent();
				
				
				AreaList area=new AreaList();
				area.setAreaName(filterDateList.get(arg2).getName());
				area.setAreaCode(filterDateList.get(arg2).getCode());	
				
				Bundle bundle=new Bundle();
				bundle.putSerializable("AreaList", area);
				intent.putExtras(bundle);
				intent.setAction(ConstUtil.VENUESAPPOINTMENT_ACTION);
				getActivity().sendBroadcast(intent);
				getActivity().finish();				
			}
		});
	}

	class CitySearchAdapt extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return filterDateList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return filterDateList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.citysearch_listview_item, null);
			}
			TextView text = (TextView) arg1
					.findViewById(R.id.textView_search_cityname);
			text.setText(filterDateList.get(arg0).getName());
			Log.i("ceshi",filterDateList.get(arg0).getName());
			return arg1;
		}
	}
}
