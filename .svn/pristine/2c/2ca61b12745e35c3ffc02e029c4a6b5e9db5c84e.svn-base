package com.striveen.express.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 
 * @author xumy
 * 
 */
public class AndroidUtils {
	/**
	 * Returns version code
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns version name
	 * 
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieve device ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		String IMEI = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();

		return IMEI;
	}
}
