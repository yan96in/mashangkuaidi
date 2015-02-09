/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 商品的非常的详细的信息的展示的webview
 * 
 * @author 亚明
 * 
 */
public class LayoutProductInfoInfoShowView extends WebView {
	private final String TAG = this.getClass().getSimpleName();

	private Context context;

	private LoadingDataDialogManager loadingToast;

	public LayoutProductInfoInfoShowView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();

	}

	public LayoutProductInfoInfoShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public LayoutProductInfoInfoShowView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressLint("NewApi")
	private void init() {
		loadingToast = LoadingDataDialogManager.init(context);
		this.getSettings().setJavaScriptEnabled(true);
		this.getSettings().setSupportZoom(true);
		this.getSettings().setDomStorageEnabled(true);
		this.getSettings().setAllowFileAccess(true);
//		this.getSettings().setPluginsEnabled(true);
		this.getSettings().setUseWideViewPort(true);
		// this.getSettings().setDefaultZoom(ZoomDensity.FAR);
		this.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。
		// webView.getSettings().setBuiltInZoomControls(true);
		this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		this.requestFocus(); // www.2cto.com
		this.getSettings().setLoadWithOverviewMode(true);
		this.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		// this.setScrollContainer(false);
		// this.setScrollBarStyle(0);

		this.setOnTouchListener(wvTouchListener);
		this.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

			}

		});

		this.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.i("URL", url);
				// loadingToast.show();
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// loadingToast.show();
				view.loadUrl(url);
				Log.i(TAG, "webview.loading" + url);
				return true;

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// loadingToast.dismiss();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				// loadingToast.dismiss();
			}

		});

		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (version > 14) {
			this.setOnGenericMotionListener(new OnGenericMotionListener() {

				@Override
				public boolean onGenericMotion(View v, MotionEvent event) {
					return false;
				}
			});
		}

	}

	OnTouchListener wvTouchListener = new OnTouchListener() {
		private float OldX1, OldY1, OldX2, OldY2;
		private float NewX1, NewY1, NewX2, NewY2;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
				if (event.getPointerCount() == 2) {
					OldX1 = event.getX(0);
					OldY1 = event.getY(0);
					OldX2 = event.getX(1);
					OldY2 = event.getY(1);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (event.getPointerCount() == 2) {
					if (OldX1 == -1 && OldX2 == -1)
						break;
					NewX1 = event.getX(0);
					NewY1 = event.getY(0);
					NewX2 = event.getX(1);
					NewY2 = event.getY(1);
					float disOld = (float) Math
							.sqrt((Math.pow(OldX2 - OldX1, 2) + Math.pow(OldY2
									- OldY1, 2)));
					float disNew = (float) Math
							.sqrt((Math.pow(NewX2 - NewX1, 2) + Math.pow(NewY2
									- NewY1, 2)));
					Log.i("onTouch", "disOld=" + disOld + "|disNew=" + disNew);
					if (disOld - disNew >= 25) {
						// 缩小
						// wv.zoomOut();
						// PDFShowView_1.this.loadUrl("javascript:mapScale=1;");
						LayoutProductInfoInfoShowView.this.zoomOut();
						Log.i("onTouch", "zoomOut");
					} else if (disNew - disOld >= 25) {
						// 放大
						// wv.zoomIn();
						// PDFShowView_1.this.loadUrl("javascript:mapScale=-1;");
						LayoutProductInfoInfoShowView.this.zoomIn();
						Log.i("onTouch", "zoomIn");
					}
					OldX1 = NewX1;
					OldX2 = NewX2;
					OldY1 = NewY1;
					OldY2 = NewY2;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (event.getPointerCount() < 2) {
					OldX1 = -1;
					OldY1 = -1;
					OldX2 = -1;
					OldY2 = -1;
				}
				break;
			}
			return false;
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

}
