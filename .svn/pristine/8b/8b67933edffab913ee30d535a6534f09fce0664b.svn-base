/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express;

import android.content.pm.ActivityInfo;

/**
 * 用来接收第三方入侵
 * 
 * @author aym
 * 
 */
public class IntrusionActivity extends AAAAcitivty {

	@Override
	protected void onResume() {
		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
