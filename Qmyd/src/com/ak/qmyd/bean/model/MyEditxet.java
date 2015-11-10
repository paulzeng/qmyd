package com.ak.qmyd.bean.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class MyEditxet extends EditText {

	
	private String hint="请输入城市名或首字母查询";
	
	public MyEditxet(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyEditxet(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyEditxet(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		this.setHint(hint);
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(hint);
                int drawablePadding = getCompoundDrawablePadding();
          
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
          
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2,0);             
            }
        }
		super.onDraw(canvas);
	}
}
