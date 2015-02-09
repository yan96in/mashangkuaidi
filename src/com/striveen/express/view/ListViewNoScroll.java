/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不滚动的listview
 * 
 * @author Fei
 * 
 */
public class ListViewNoScroll extends ListView {

	public ListViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListViewNoScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewNoScroll(Context context) {
		super(context);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
