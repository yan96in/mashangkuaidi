package com.striveen.express.util;

import android.content.Context;
import android.widget.Toast;

import com.striveen.express.MyApplication;

 
/**
 * @author JasonHu<xiaoxiangtata#gmail.com>
 *
 */
public class LogUtil {
 
	public static boolean Debug;
	
	static{
		if (MyApplication.devMode == 2) {
			Debug = true;
		} else {
			Debug = false;
		}
	}
	
	/**
	 * 打印lo，同时在屏幕上输出msg通知
	 * @param context
	 * @param tag
	 * @param msg
	 */
	public static void Log(Context context,String tag,String msg) {
		if (Debug) {
			android.util.Log.d(tag, msg);		
			showToast(context,msg);
		}
	}
	
	/**
	 * 打印log到console
	 * @param tag
	 * @param msg
	 */
	public static void Log(String tag,String msg) {
		if (Debug) {
			android.util.Log.d(tag,  msg);		
		}
	}
	
	/**
	 * 输出提示到屏幕通知
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		if (Debug) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
		
	}

	public static void Log(String tag, String message, Exception exception) {
		if (Debug) {
		android.util.Log.e(tag, message, exception);
		}
	}

}
