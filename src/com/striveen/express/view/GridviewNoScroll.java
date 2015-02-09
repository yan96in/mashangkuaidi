package com.striveen.express.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class GridviewNoScroll extends GridView {

	public GridviewNoScroll(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GridviewNoScroll(Context context, AttributeSet set) {
		// TODO Auto-generated constructor stub
		super(context, set);
	}

	public GridviewNoScroll(Context context, AttributeSet set, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, set, defStyle);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	// 通过重新dispatchTouchEvent方法来禁止滑动
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	// return true;// 禁止Gridview进行滑动
	// }
	// return super.dispatchTouchEvent(ev);
	// }

}
