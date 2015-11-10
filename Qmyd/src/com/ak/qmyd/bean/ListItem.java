package com.ak.qmyd.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface ListItem {

	  public int getLayout();
	     
	  public boolean isClickable();
	     
	  public View getView(Context context, View convertView, LayoutInflater inflater);
	  
	  public String getTitle();
}
