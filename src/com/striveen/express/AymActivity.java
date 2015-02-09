package com.striveen.express;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.striveen.express.view.ViewInject;

/**
 * 返回按钮 头部居中标题 右边有功能按钮 基础的AymActivity
 * 
 * @author Fei
 * 
 */
public class AymActivity extends MyActivity {
	/**
	 * 返回按钮和功能按钮
	 */
	protected ImageView btn_back, btn_fun;
	/**
	 * 头部展示的标题
	 */
	protected TextView tv_title;
	/**
	 * 当前activity的默认布局 只有头部信息 内容信息是没有的
	 * 
	 */
	private View view;
	@ViewInject(id = R.id.btn_fun)
	private ImageView iv_manage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		view = getLayoutInflater().inflate(R.layout.all_activity_view, null);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 初始化 得到标题中的控件 并设定后退按钮事件
	 * 
	 * @param title
	 *            标题展示文字
	 */
	protected void initActivityTitle(String title) {
		initActivityTitle(title, false, 0, null);
	}

	/**
	 * 初始化 得到标题中的控件 并设定后退按钮事件
	 * 
	 * @param titleResId
	 *            标题展示文字的对应索引
	 */
	protected void initActivityTitle(int titleResId) {
		initActivityTitle(getString(titleResId));
	}

	/**
	 * 初始化 得到标题中的控件 并设定后退按钮事件
	 * 
	 * @param titleResId
	 *            标题展示文字
	 * @param isShowBack
	 *            是否显示回退按钮
	 */
	protected void initActivityTitle(String title, boolean isShowBack) {
		initActivityTitle(title, isShowBack, 0, null);
	}

	/**
	 * 初始化 得到标题中的控件 并设定后退按钮事件
	 * 
	 * @param titleResId
	 *            标题展示文字的对应索引
	 * @param isShowBack
	 *            是否显示回退按钮
	 */
	protected void initActivityTitle(int titleResId, boolean isShowBack) {
		initActivityTitle(getString(titleResId), isShowBack);
	}

	/**
	 * 初始化 得到标题中的控件 并设定功能按钮事件
	 * 
	 * @param title
	 *            标题展示文字
	 * @param resid
	 *            功能按钮的展示图片的对应索引
	 * @param funOnClickListener
	 *            功能按钮事件
	 */
	protected void initActivityTitle(String title, int resid,
			OnClickListener funOnClickListener) {
		initActivityTitle(title, false, resid, funOnClickListener);
	}

	/**
	 * 初始化 得到标题中的控件 并设定功能按钮事件
	 * 
	 * @param titleResId
	 *            标题展示文字的对应索引
	 * @param resid
	 *            功能按钮的展示图片的对应索引
	 * @param funOnClickListener
	 *            功能按钮事件
	 */
	protected void initActivityTitle(int titleResId, int resid,
			OnClickListener funOnClickListener) {
		initActivityTitle(getString(titleResId), resid, funOnClickListener);
	}

	/**
	 * 初始化 得到标题中的控件 并设定功能按钮事件 和返回按钮
	 * 
	 * @param title
	 *            标题展示文字
	 * @param isShowBack
	 *            是否显示回退按钮
	 * @param resid
	 *            功能按钮的展示图片的对应索引
	 * @param funOnClickListener
	 *            功能按钮事件
	 */
	protected void initActivityTitle(String title, boolean isShowBack,
			int resid, OnClickListener funOnClickListener) {
		btn_back = (ImageView) view.findViewById(R.id.btn_back);
		btn_fun = (ImageView) view.findViewById(R.id.btn_fun);
		tv_title = (TextView) view.findViewById(R.id.tv_title);

		/*
		 * 回退的展示设定
		 */
		if (null != btn_back) {
			btn_back.setBackgroundColor(Color.TRANSPARENT);
			btn_back.setOnClickListener(clickListener);
			btn_back.setVisibility(isShowBack ? View.VISIBLE : View.INVISIBLE);
		}
		/*
		 * 标题的展示设定
		 */
		if (null != tv_title) {
			if (null != title) {
				tv_title.setText(title);
			}
		}
		/*
		 * 功能按钮的展示的图片和调用函数的设定
		 */
		if (null != btn_fun) {
			btn_fun.setBackgroundColor(Color.TRANSPARENT);
			if (resid > 0 && null != funOnClickListener) {
				btn_fun.setVisibility(View.VISIBLE);
				btn_fun.setImageResource(resid);
				btn_fun.setOnClickListener(funOnClickListener);
			} else {
				btn_fun.setVisibility(View.INVISIBLE);
				btn_fun.setOnClickListener(null);
			}
		}
	}

	/**
	 * 初始化 得到标题中的控件 并设定功能按钮事件 和返回按钮
	 * 
	 * @param titleResId
	 *            标题展示文字的对应索引
	 * @param isShowBack
	 *            是否显示回退按钮
	 * @param resid
	 *            功能按钮的展示图片的对应索引
	 * @param funOnClickListener
	 *            功能按钮事件
	 */
	protected void initActivityTitle(int titleResId, boolean isShowBack,
			int resid, OnClickListener funOnClickListener) {
		initActivityTitle(getString(titleResId), isShowBack, resid,
				funOnClickListener);
	}

	// 设定后退按钮事件
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AymActivity.this.finish();
		}
	};

	/**
	 * 重新父类的设置主view的方法
	 */
	public void setContentView(int layoutResID) {
		View view = getLayoutInflater().inflate(layoutResID, null);
		setActivityView(view, null);
	};

	/**
	 * 重新父类的设置主view的方法
	 */
	@Override
	public void setContentView(View view) {
		setActivityView(view, null);
	}

	/**
	 * 重新父类的设置主view的方法
	 */
	@Override
	public void setContentView(View view, LayoutParams params) {
		setActivityView(view, params);
	}

	/**
	 * 设置带有标题的activity
	 * 
	 * @param view
	 *            内容
	 */
	private void setActivityView(View v, LayoutParams params) {

		LinearLayout layout = (LinearLayout) view.findViewById(R.id.center);
		if (params == null) {
			layout.addView(v, new LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
		} else {
			layout.addView(v, params);
		}
		super.setContentView(view);
	}
}
