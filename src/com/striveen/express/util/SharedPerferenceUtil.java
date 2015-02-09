package com.striveen.express.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPerferenceUtil {
	
	public static String  PREFERENCENAME = "system_config";
	
	public static void setSystemConfig(Context context, String key, String value){
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME,  Context.MODE_APPEND);
		sp.edit().putString(key, String.valueOf(value)).commit();
	}
	public static String getSystemConfig(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME,  Context.MODE_APPEND);
		return sp.getString(key, "");
	}

}
