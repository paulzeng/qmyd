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
	// �����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	private int choose = -1;// ѡ��

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
	 * ���������Ԫ���������ұ���ĸ��洢��ĸ
	 * */
	private void getList() {

		list = CityLocationFragment.list;

		if (list == null) {
			return;
		}
		mlist = new ArrayList<String>();
		mlist.add("��λ");

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
	 * ��д�������
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// if(start){
		getList();
		// ��ȡ����ı䱳����ɫ.
		int height = getHeight();// ��ȡ��Ӧ�߶�
		int width = getWidth(); // ��ȡ��Ӧ���
		int singleHeight = height / mlist.size();// ��ȡÿһ����ĸ�ĸ߶�

		for (int i = 0; i < mlist.size(); i++) {
			paint.setColor(this.getResources().getColor(R.color.orange_color));
			// paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			
		
			
			paint.setTextSize(context.getResources().getDimension(R.dimen.text_size));
			// ѡ�е�״̬
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x��������м�-�ַ�����ȵ�һ��.
			float xPos = width / 2 - paint.measureText(mlist.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(mlist.get(i), xPos, yPos, paint);
			paint.reset();// ���û���
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// ���y����
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * mlist.size());// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.

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
	 * ���⹫���ķ���
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * �ӿ�
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}