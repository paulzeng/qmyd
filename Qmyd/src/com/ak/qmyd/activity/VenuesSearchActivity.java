package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.adapt.VenuesSearchAdapter;
import com.ak.qmyd.tools.DebugUtility;

public class VenuesSearchActivity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText editText;
	private ListView listView;
	private List<String> mList;
	private int Number;
	private TextView textView;
	private VenuesSearchAdapter adapt;
	private ImageButton imageButton_back;
	private DrawerLayout drawerlayout;
	private SharedPreferences share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venues_search);
		findView();
		showHistory();
	}

	/**
	 * 显示历史记录
	 * */
	private void showHistory() {

		mList = new ArrayList<String>();

		share = getSharedPreferences("SearchHistory", MODE_PRIVATE);
		Number = share.getInt("Number", -1);
		mList.clear();
		if (Number != -1) {
			for (int i = Number; i >= 0; i--) {
				String SearchContent = share.getString("SearchContent" + i,
						null);
				if (SearchContent != null) {
					mList.add(SearchContent);
				}
			}
			DebugUtility.showLog("历史数据列表大小：" + mList.size());
			adapt = new VenuesSearchAdapter(mList, this);
			listView.setAdapter(adapt);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					saveContent(mList.get(position));
				}
			});
		}

	}

	/**
	 * 初始化
	 * */
	private void findView() {
		listView = (ListView) this.findViewById(R.id.lv_search_list);
		editText = (EditText) this.findViewById(R.id.et_search_edit);
		textView = (TextView) this.findViewById(R.id.textView_remove);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
		imageButton_back.setOnClickListener(this);
		textView.setOnClickListener(this);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				String content = editText.getText().toString();
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (content != null && (!(content.equals("")))) {
						saveContent(content);
						return true;
					}
				}
				return false;
			}
		});
	}

	/**
	 * 保存数据
	 * */
	private void saveContent(String content) {

		SharedPreferences share = this.getSharedPreferences("SearchHistory",
				Context.MODE_PRIVATE);
		Editor editor = share.edit();
		int number = share.getInt("Number", 0);
		for (int i = 0; i <= number; i++) {
			if (content != null
					&& content.equals(share
							.getString("SearchContent" + i, null))) {
				for (int k = i; k < number; k++) {
					editor.putString("SearchContent" + k,
							share.getString("SearchContent" + (k + 1), null));

				}
				Number--;
			}
		}
		Number++;
		editor.putString("SearchContent" + Number, content);
		editor.putInt("Number", Number);
		editor.commit();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("SearchContent", content);
		intent.putExtras(bundle);
		setResult(2, intent);
		finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.textView_remove:
			if (mList != null) {
				// clearHistory();
				share.edit().clear().commit();
				mList.clear();
				DebugUtility.showLog("清理后列表大小:" + mList.size());
				adapt.notifyDataSetChanged();
			}
			break;
		case R.id.imageButton_back:
			finish();
			break;

		}
	}

	/**
	 * 清除历史数据
	 * */
	private void clearHistory() {
		SharedPreferences share = this.getSharedPreferences("SearchHistory",
				Context.MODE_PRIVATE);
		Editor editor = share.edit();
		int number = share.getInt("Number", 0);
		for (int i = 0; i <= number; i++) {
			editor.remove("SearchContent" + i);
		}
		editor.remove("Number");
		editor.commit();
	}
	// adapt = new VenuesSearchAdapter(mList, context);
	// listView.setAdapter(adapt);
}
