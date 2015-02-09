/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 去掉了快速滑动
 * 
 * @author 亚明
 * 
 */
public class MyGallery extends Gallery {

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// return super.onFling(e1, e2, velocityX, velocityY);
		return true;
	}
}
