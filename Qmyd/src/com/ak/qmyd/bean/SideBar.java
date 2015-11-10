package com.ak.qmyd.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.fragment.CityLocationFragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	private int choose = -1;// 选中

	private Paint paint = new Paint();

	private TextView mTextDialog;

	private List<CityName> list;

	private Context context;

	public static boolean start = false;

	private List<String> mlist;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		getList();
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getList();
	}

	public SideBar(Context context) {
		super(context);
		this.context = context;
		getList();
	}

	
	/**
	 * 给集合添加元素用来给右边字母表存储字母
	 * */
	private void getList() {

		list = CityLocationFragment.list;

		if (list == null) {
			return;
		}
		mlist = new ArrayList<String>();
		mlist.add("定位");

		for (int i = 0; i < list.size(); i++) {

			String str = list.get(i).getSortLetters();

			if (i >= 1) {
				if (!(list.get(i).getSortLetters().equals(list.get(i - 1)
						.getSortLetters()))) {
					mlist.add(str);
				}
			} else {
				mlist.add(str);
			}

		}

	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// if(start){
		getList();
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / mlist.size();// 获取每一个字母的高度

		for (int i = 0; i < mlist.size(); i++) {
			paint.setColor(this.getResources().getColor(R.color.orange_color));
			// paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			
		
			
			paint.setTextSize(context.getResources().getDimension(R.dimen.text_size));
			// 选中的状态
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(mlist.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(mlist.get(i), xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * mlist.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if (oldChoose != c) {
				if (c >= 0 && c < mlist.size()) {
					if (listener != null) {

						listener.onTouchingLetterChanged(mlist.get(c));
					}
					if (mTextDialog != null) {
						mTextDialog.setText(mlist.get(c));
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}