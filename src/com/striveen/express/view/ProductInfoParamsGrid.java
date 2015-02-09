/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 产品的信息中规格的设置
 * 
 * @author 亚明
 * 
 */
public class ProductInfoParamsGrid extends GridView implements
		android.widget.AdapterView.OnItemClickListener {
	/**
	 * 商品属性选中改变回调
	 */
	private IProductInfoParamsChange change;

	/**
	 * 商品属性选中改变回调
	 */
	public void setChange(IProductInfoParamsChange change) {
		this.change = change;
	}

	/**
	 * 以选中的那个
	 */
	private TextView tv_current;

	public ProductInfoParamsGrid(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
		this.setColumnWidth(LayoutParams.WRAP_CONTENT);
		this.setNumColumns(GridView.AUTO_FIT);
		// this.setSelector(getResources().getDrawable(
		// R.drawable.layout_product_info_params_selector));
		this.setOnItemClickListener(this);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		super.setVerticalScrollBarEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// TextView tv = (TextView) view.findViewById(R.id.i_l_p_i_p_tv_name);
		//
		// tv.setBackgroundResource(R.drawable.layout_product_info_param_select);
		// if (tv_current != null) {
		// tv_current
		// .setBackgroundResource(R.drawable.layout_product_info_param_noselect);
		// tv_current = tv;
		// }
	}

	public interface IProductInfoParamsChange {
		void onChange();
	}

}
