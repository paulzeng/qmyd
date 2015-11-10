package com.ak.qmyd.activity.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.model.SportType;

public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** �Ƿ���ʾ�ײ���ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** ���Ƶ�postion */
	private int holdPosition;
	/** �Ƿ�ı� */
	private boolean isChanged = false;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** �����϶����б����û�ѡ���Ƶ���б� */
	public List<SportType> channelList;
	/** TextView Ƶ������ */
	private TextView item_text;
	/** Ҫɾ����position */
	public int remove_position = -1;

	public DragAdapter(Context context, List<SportType> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public SportType getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		SportType channel = getItem(position);
		item_text.setText(channel.getTypeName());
		if ((position == 0) || (position == 1)) {
			// item_text.setTextColor(context.getResources().getColor(R.color.black));
			// item_text.setEnabled(false);
		}
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if (remove_position == position) {
			item_text.setText("");
		}
		return view;
	}

	/** ���Ƶ���б� */
	public void addItem(SportType channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** �϶����Ƶ������ */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		SportType dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition="
				+ dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/** ��ȡƵ���б� */
	public List<SportType> getChannnelLst() {
		return channelList;
	}

	/** ����ɾ����position */
	public void setRemove(int position) { 
			remove_position = position;
			notifyDataSetChanged(); 
	}

	/** ����ɾ����position */
	public void setRemoveAdd(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** ɾ��Ƶ���б� */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** ɾ��Ƶ���б� */
	public void removeAdd() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();

	}

	/** ����Ƶ���б� */
	public void setListDate(List<SportType> list) {
		channelList = list;
	}

	/** ��ȡ�Ƿ�ɼ� */
	public boolean isVisible() {
		return isVisible;
	}

	/** �����Ƿ�ɼ� */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** ��ʾ���µ�ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}