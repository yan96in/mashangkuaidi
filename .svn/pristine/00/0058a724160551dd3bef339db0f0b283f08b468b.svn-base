/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;

import com.striveen.express.R;
import com.striveen.express.util.Confing;
import com.striveen.express.view.LoadMoreListView.OnLoadMoreDataListener;

/**
 * 重写向下加载更多 加载更多的view 以设定 加载进度 视图刷新已做好
 * 
 * @author Fei
 * 
 */
public class MyLoadMoreListView extends LoadMoreListView implements
		OnLoadMoreDataListener {

	private LoadMoreDataListener loadMoreDataListener;
	private Context context;
	private boolean isInited;

	public MyLoadMoreListView(Context context) {
		super(context);
		this.context = context;
	}

	public MyLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public MyLoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * 初始化公共的信息
	 * 
	 * @param context
	 */
	private void init() {
		if (isInited) {
			return;
		}
		if (loadMoreDataListener != null) {
			this.setLoadMoreView(LayoutInflater.from(context).inflate(
					R.layout.layout_loadmore, null));
			this.setAutoLoadMore(true);
			this.setOnLoadMoreDataListener(this);
		}
		/*
		 * 这里是设定全局统一的样式的
		 */
		// this.setDivider(context.getResources().getDrawable(
		// R.drawable.all_listview_divider));
		// this.setDrawSelectorOnTop(false);
//		 this.setSelector(R.drawable.all_listview_select);
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		this.setBackgroundDrawable(null);
		isInited = true;
	}

	/**
	 * 重新父类方法 放入本项目个性化信息
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		init();
		super.setAdapter(adapter);
	}

	@Override
	public void loadMore(View arg0) {
		if (loadMoreDataListener != null) {
			loadMoreDataListener.loadMore();
		}
		// arg0.findViewById(R.id.pb).setVisibility(VISIBLE);
		// ((TextView) arg0.findViewById(R.id.tv_loadmore))
		// .setText(R.string.loadmoring);

	}

	@Override
	public void loadMoreFinish(View arg0) {
		// arg0.findViewById(R.id.pb).setVisibility(INVISIBLE);
		// ((TextView) arg0.findViewById(R.id.tv_loadmore))
		// .setText("");
	}

	/**
	 * 设定加载回调
	 * 
	 * @param loadMoreDataListener
	 */
	public void setLoadMoreDataListener(
			LoadMoreDataListener loadMoreDataListener) {
		this.loadMoreDataListener = loadMoreDataListener;
	}

	/**
	 * 加载数据的回调
	 * 
	 * @author Aym
	 * 
	 */
	public interface LoadMoreDataListener {
		void loadMore();
	}

	/**
	 * 调用此方法 无论pageSize传多少都会使用Confing.PageSize
	 */
	@Override
	public void setPageSize(int pageSize) {
		super.setPageSize(Confing.PageSize);
	}

	/**
	 * 调用此方法 无论pageSize传多少都会使用Confing.PageSize
	 */
	@Override
	public void openAutoCorrectCurrentPage(int pageSize) {
		super.openAutoCorrectCurrentPage(Confing.PageSize);
	}

}
