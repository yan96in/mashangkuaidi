/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LayoutPanicBuyingView extends LinearLayout {
	/**
	 * 使用上下文
	 */
	private Context context;
	/**
	 * 小时分钟秒的展示
	 */
	private TextView tv_shi, tv_fen, tv_miao;
	/**
	 * 还剩的时间
	 */
	private int qg_time;
	/**
	 * 倒计时定时
	 */
	private Timer timer;

	@SuppressLint("NewApi")
	public LayoutPanicBuyingView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public LayoutPanicBuyingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public LayoutPanicBuyingView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		timer = new Timer();
		timer.schedule(new FulstTimerTask(), 1000, 1000);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		inflater.inflate(R.layout.layout_panic_buying_product, this);
//		tv_shi = (TextView) findViewById(R.id.lpbp_tv_qinggou_shi);
//		tv_fen = (TextView) findViewById(R.id.lpbp_tv_qinggou_fen);
//		tv_miao = (TextView) findViewById(R.id.lpbp_tv_qinggou_miao);
	}

	/**
	 * 还剩的时间
	 */
	public void setTime(int time) {
		this.qg_time = time;
		if (qg_time <= 0) {
			tv_shi.setText("00");
			tv_fen.setText("00");
			tv_miao.setText("00");
		} else {
			time = qg_time;
			int shi = time / (60 * 60);
			time = time % (60 * 60);
			int fen = time / 60;
			time = time % 60;
			int miao = time;
			tv_shi.setText((shi < 10 ? ("0" + shi) : shi) + "");
			tv_fen.setText((fen < 10 ? ("0" + fen) : fen) + "");
			tv_miao.setText((miao < 10 ? ("0" + miao) : miao) + "");
		}
		qg_time--;

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (null != timer) {
			timer.cancel();
		}
	}

	/**
	 * 定时刷新倒计时
	 */
	private class FulstTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	/**
	 * 定时刷新倒计时执行者
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setTime(qg_time);
		};
	};

}
