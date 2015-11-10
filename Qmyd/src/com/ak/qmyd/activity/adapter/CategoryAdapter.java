package com.ak.qmyd.activity.adapter;

import java.util.ArrayList;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.Category;
import com.ak.qmyd.bean.TrainDetailExpandableListChildEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	private static final int TYPE_CATEGORY_ITEM = 0;
	private static final int TYPE_ITEM = 1;
	
	private ArrayList<Category> mListData;
	private LayoutInflater mInflater;
	
	
	public CategoryAdapter(Context context, ArrayList<Category> pData) {
		mListData = pData;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int count = 0;
		
		if (null != mListData) {
			//  ���з�����item���ܺ���ListVIew  Item���ܸ���
			for (Category category : mListData) {
				count += category.getItemCount();
			}
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		
		// �쳣�������
		if (null == mListData || position <  0|| position > getCount()) {
			return null; 
		}
		
		// ͬһ�����ڣ���һ��Ԫ�ص�����ֵ
		int categroyFirstIndex = 0;
		
		for (Category category : mListData) {
			int size = category.getItemCount();
			// �ڵ�ǰ�����е�����ֵ
			int categoryIndex = position - categroyFirstIndex;
			// item�ڵ�ǰ������
			if (categoryIndex < size) {
				return   category.getItem( categoryIndex );
			}
			
			// �����ƶ�����ǰ�����β������һ�������һ��Ԫ������
			categroyFirstIndex += size;
		}
		
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		// �쳣�������
		if (null == mListData || position <  0|| position > getCount()) {
			return TYPE_ITEM; 
		}
		
		
		int categroyFirstIndex = 0;
		
		for (Category category : mListData) {
			int size = category.getItemCount();
			// �ڵ�ǰ�����е�����ֵ
			int categoryIndex = position - categroyFirstIndex;
			if (categoryIndex == 0) {
				return TYPE_CATEGORY_ITEM;
			}
			
			categroyFirstIndex += size;
		}
		
		return TYPE_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int itemViewType = getItemViewType(position);
		switch (itemViewType) {
		case TYPE_CATEGORY_ITEM:
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.activity_my_train_history_title, null);
			}
			
			TextView tv_train_history_month = (TextView) convertView.findViewById(R.id.tv_train_history_month);
			String  itemValue = (String) getItem(position);
			tv_train_history_month.setText( itemValue );
			break;

		case TYPE_ITEM:
			ViewHolder viewHolder = null;
			if (null == convertView) {
				
				convertView = mInflater.inflate(R.layout.activity_my_train_history_listitem, null);
				
				viewHolder = new ViewHolder();
	            viewHolder.tv_train_history_item_title = (TextView) convertView.findViewById(R.id.tv_train_history_item_title);
	            viewHolder.tv_train_history_item_time = (TextView) convertView.findViewById(R.id.tv_train_history_item_time);
	            viewHolder.tv_train_history_item_score = (TextView) convertView.findViewById(R.id.tv_train_history_item_score);
	            viewHolder.iv_train_history_item_img = (ImageView) convertView.findViewById(R.id.iv_train_history_item_img);
	            convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
	         // ������
			viewHolder.iv_train_history_item_img.setImageResource(R.drawable.ceshi8);
            viewHolder.tv_train_history_item_time.setText( ((TrainDetailExpandableListChildEntity)getItem(position)).getExpandableList_Child_time() );
            viewHolder.tv_train_history_item_score.setText( ((TrainDetailExpandableListChildEntity)getItem(position)).getExpandableList_Child_score() );
            viewHolder.tv_train_history_item_title.setText( ((TrainDetailExpandableListChildEntity)getItem(position)).getExpandableList_Child_title() );
			break;
		}

		return convertView;
	}

	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != TYPE_CATEGORY_ITEM;
	}
	
	
	private class ViewHolder {
        TextView tv_train_history_item_title,tv_train_history_item_time,tv_train_history_item_score;
        ImageView iv_train_history_item_img;
    }
	
}
