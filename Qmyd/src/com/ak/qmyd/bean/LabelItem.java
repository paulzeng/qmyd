package com.ak.qmyd.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ak.qmyd.R;

public class LabelItem implements ListItem{

	  private String mLabel;
	  	  
	    public LabelItem(String label){
	        mLabel = label;
	    }
	
	@Override
	public int getLayout() {
		// TODO Auto-generated method stub
		return R.layout.project_labelitem_listview;
	}

	@Override
	public boolean isClickable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(Context context, View convertView,
			LayoutInflater inflater) {
		    convertView = inflater.inflate(getLayout(), null);
	        TextView title = (TextView) convertView.findViewById(R.id.listview_LabelItemtextView1);
	        title.setText(mLabel);
	        return convertView;
		
	}


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return mLabel;
	}

}
