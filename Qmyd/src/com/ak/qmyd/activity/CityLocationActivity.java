package com.ak.qmyd.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.CharacterParser;
import com.ak.qmyd.bean.CityName;
import com.ak.qmyd.bean.PinyinComparator;
import com.ak.qmyd.fragment.CityLocationFragment;
import com.ak.qmyd.fragment.CitySearchFragment;

public class CityLocationActivity extends BaseFragmentActivity implements
		OnFocusChangeListener, OnTouchListener, OnClickListener {

	FragmentManager fm;

	FragmentTransaction ft;

	private EditText editText;

	private Drawable mClearDrawable;

	private String cityName;

	private String dialogCity;

	private CharacterParser characterParser;

	private PinyinComparator pinyinComparator;

	private List<CityName> list;

	private Context context;

	private ImageButton imageButton_back;

	private TextView textView_currentcity;

	private DrawerLayout drawerlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_location);
		context = getApplicationContext();
		cityName = getIntent().getExtras().getString("cityName");
		dialogCity = getIntent().getExtras().getString("dialogCity");
		context = getApplicationContext();
		CityLocationFragment fragment = new CityLocationFragment();
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("cityName", cityName);
		fragment.setArguments(bundle);
		ft.add(R.id.framlayout_fragment, fragment);
		ft.commit();
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		findView();
	}

	private void findView() {
		editText = (EditText) findViewById(R.id.editText_city_search);
		imageButton_back = (ImageButton) this
				.findViewById(R.id.imageButton_back);
		textView_currentcity = (TextView) this
				.findViewById(R.id.textView_currentcity);
		drawerlayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
		setEnableDrawerLayout(drawerlayout);
		imageButton_back.setOnClickListener(this);
		iniEditext();
		textView_currentcity.setText("当前城市-" + dialogCity);
	}

	private void iniEditext() {
		mClearDrawable = editText.getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.ceshi33);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		setClearIconVisible(false);
		editText.setOnFocusChangeListener(this);
		editText.setOnTouchListener(this);

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				setClearIconVisible(arg0.length() > 0);
				if (list == null) {
					list = CityLocationFragment.list;
					filterData(arg0.toString());
				} else {
					filterData(arg0.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		editText.setCompoundDrawables(editText.getCompoundDrawables()[0],
				editText.getCompoundDrawables()[1], right,
				editText.getCompoundDrawables()[3]);
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if (arg1) {
			setClearIconVisible(editText.getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}

	}

	private void filterData(String string) {
		List<CityName> filterDateList = new ArrayList<CityName>();

		if (TextUtils.isEmpty(string)) {
			CityLocationFragment fragment = new CityLocationFragment();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putString("cityName", cityName);
			bundle.putSerializable("cityList", (Serializable) list);
			fragment.setArguments(bundle);
			ft.replace(R.id.framlayout_fragment, fragment);
			ft.commit();
		} else {
			filterDateList.clear();

			for (CityName cName : list) {
				String name = cName.getName();
				if (name.indexOf(string.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								string.toString())
						|| characterParser.getSelling(name).toUpperCase()
								.startsWith(string.toString())) {
					filterDateList.add(cName);
				}
			}

			CitySearchFragment fragment = new CitySearchFragment();
			ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putSerializable("cityList", (Serializable) filterDateList);
			fragment.setArguments(bundle);
			ft.replace(R.id.framlayout_fragment, fragment);
			ft.commit();
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		// adapter.updateListView(filterDateList);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		if (editText.getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (editText.getWidth()
						- editText.getPaddingRight() - mClearDrawable
							.getIntrinsicWidth())
						&& (event.getX() < ((editText.getWidth() - editText
								.getPaddingRight())));
				if (touchable) {
					editText.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imageButton_back:
			finish();
			break;
		}

	}

}
