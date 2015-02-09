package com.zijunlin.Zxing.Demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.MyApplication;

public class MainActivity extends MyActivity {

	Context context;
	MainBroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(this);
		TextView tv = new TextView(this);
		tv.setText(getIntent().getStringExtra("code"));
		layout.addView(tv);
		setContentView(layout);
		Log.e("MainActivity", "code:" + getIntent().getStringExtra("code"));
		((MyApplication) getApplicationContext()).setSearchKey(getIntent()
				.getStringExtra("code"));
		// startActivity(new Intent(this, PayOrEvaluateActivity.class));
	}

	protected void onStart() {
		// TODO 重写onStart方法 注册用于接收Service传送的广播
		if (null == mReceiver) {
			mReceiver = new MainBroadcastReceiver();
		}
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("MainActivity");
		registerReceiver(mReceiver, filter);// 注册Broadcast Receiver
		try {
			Log.e("aaaaaa", "action:" + getIntent().getStringExtra("action"));
			Thread.sleep(500);
			Intent intent = new Intent();// 创建Intent对象
			intent.setAction(getIntent().getStringExtra("action"));
			intent.putExtra("code", getIntent().getStringExtra("code"));
			sendBroadcast(intent);
			this.finish();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO onDestroy
		// handler.removeCallbacks(updateOnLineTime);
		// 退出时销毁定位
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	/**
	 * 自定义广播接受通知
	 * 
	 * @author Fei
	 * 
	 */
	public class MainBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		}
	}

}
