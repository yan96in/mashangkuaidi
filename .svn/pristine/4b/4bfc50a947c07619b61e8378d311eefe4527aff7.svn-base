package com.striveen.express.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.striveen.express.MyApplication;
import com.striveen.express.R;
import com.striveen.express.net.GetData;
import com.striveen.express.util.ToastUtil;
import com.striveen.express.view.LoadingDataDialogManager;

/**
 * loadingToast
 * 
 * toast
 * 
 * @author Fei
 * 
 */
public class IndexAllActivity extends Activity {

	/**
	 * 提示toast的展示对象 可直接调用期show方法展示文字
	 */
	protected ToastUtil toast;

	/**
	 * 模拟toast展示加载数据的对话框，使界面更加友好
	 */
	protected LoadingDataDialogManager loadingToast;
	protected String error[];
	protected GetData getData;
	/**
	 * 加载数据显示文字
	 */
	protected String load_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		toast = ToastUtil.initToast(this);
		loadingToast = LoadingDataDialogManager.init(this, "玩命加载中。。。");
		error = getResources().getStringArray(R.array.getdata_msg);
		getData = new GetData(this);
	}

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

	/**
	 * 将展示的加载数据的对话框释放掉
	 * 
	 * @see Activity#onDestroy();
	 */
	@Override
	protected void onDestroy() {
		if (null != loadingToast) {
			loadingToast.destroy();
		}
		super.onDestroy();
	}

	/**
	 * 获得当前应用中的MyApplication
	 * 
	 * @return MyApplication
	 */
	protected final MyApplication getIndexApplication() {
		return (MyApplication) getApplicationContext();
	}
}
