/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 捕获全局异常
 * 
 * @author aym
 * 
 */
public class MyCrashHandler implements UncaughtExceptionHandler {

	// 需求是 整个应用程序 只有一个 MyCrash-Handler
	private static MyCrashHandler myCrashHandler;
	private Context context;

	// 1.私有化构造方法
	private MyCrashHandler() {
	}

	public static synchronized MyCrashHandler getInstance() {
		if (myCrashHandler == null) {
			myCrashHandler = new MyCrashHandler();
		}
		return myCrashHandler;
	}

	public void init(Context context) {
		this.context = context;
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {
		Log.e("MyCrashHandler", "程序挂了。。。。。\n" + arg1.getMessage());
		handleException(arg1);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());

		Intent intent2 = new Intent(context, InitActivity.class);
		intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent2);

	}

	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return true：如果处理了该异常信息；否则返回 false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		// 使用 Toast 来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "很抱歉，程序出现异常，正在尝试重新启动",
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		return true;
	}
}
