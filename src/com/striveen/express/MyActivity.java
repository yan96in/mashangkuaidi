/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.striveen.express.net.GetData;
import com.striveen.express.net.GetServicesDataUtil;

/**
 * 定义项目中的公用属性
 * 
 * @author Fei
 * 
 */
public class MyActivity extends IntrusionActivity {
	/**
	 * 调用服务器接口获取服务器数据的工具
	 */
	protected GetServicesDataUtil getDataUtil;
	MyApplication myApplication;
	protected String error[];
	protected GetData getData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getDataUtil = GetServicesDataUtil.init();
		try {
			IntentFilter myIntentFilter = new IntentFilter();
			myIntentFilter
					.addAction(GoShoppingCartBroadcastReceiver_ActionName);
			registerReceiver(goShoppingCartBroadcastReceiver, myIntentFilter);
		} catch (Exception e) {
		}
		error = getResources().getStringArray(R.array.getdata_msg);
		getData = new GetData(this);
	}

	@Override
	protected void onDestroy() {
		try {
			this.unregisterReceiver(goShoppingCartBroadcastReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	/**
	 * 获得当前应用中的MyApplication
	 * 
	 * @return MyApplication
	 */
	protected final MyApplication getMyApplication() {
		return (MyApplication) getApplicationContext();
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param con
	 * @return
	 */
	protected boolean isTextEmpty(String con) {
		if (null != con && !TextUtils.isEmpty(con)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 当发错去购物车的广播时本activity自动关闭 的ActionName
	 */
	public final String GoShoppingCartBroadcastReceiver_ActionName = "GoShoppingCartBroadcastReceiver";

	/**
	 * 当发错去购物车的广播时本activity自动关闭
	 */
	public BroadcastReceiver goShoppingCartBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (GoShoppingCartBroadcastReceiver_ActionName.equals(intent
					.getAction())) {
				MyActivity.this.finish();
			}
		}
	};
}
