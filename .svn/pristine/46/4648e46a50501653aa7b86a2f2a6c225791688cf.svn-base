package com.striveen.express.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

public class StringUtil {
	/**
	 * 
	 * @param 手机号码的格式验证
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("[1][34578]\\d{9}");
		Matcher m = p.matcher(mobiles);
		return m.find();
	}

	/**
	 * 
	 * @param 密码格式验证
	 * @return
	 */
	public static boolean isPassWord(String mobiles) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]{6,16}$");
		Matcher m = p.matcher(mobiles);
		return m.find();
	}

	/**
	 * 
	 * @param 运单号验证
	 * @return
	 */
	public static boolean isWaybillNumber(String mobiles) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]{0,30}$");
		Matcher m = p.matcher(mobiles);
		return m.find();
	}

	/**
	 * 
	 * @param 邮箱的格式验证
	 * @return
	 */
	public static boolean getEmail(String line) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(line);
		return m.find();
	}

	/**
	 * 
	 * @param 账号的格式验证
	 * @return不能用中文的汉字和符号!
	 */
	public static boolean isHaveChineseChar(String str) {
		if (str.length() == str.getBytes().length) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本机手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * 获取本机手机IP
	 * 
	 * @param context
	 * @return
	 */
	public static String GetHostIp(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "."
				+ ((ipAddress >> 16) & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
	}

	/**
	 * 获取本机手机IP
	 * 
	 * @param context
	 * @return
	 */

	public static String intToIp(Context context) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("MyFeiGeActivity", "获取本地IP地址失败");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.e("MyFeiGeActivity", "获取本地IP地址失败");
		}
		return null;
	}

	/**
	 * 获取网络的状态信息
	 */
	public static String detectionMesh(Context context) {
		ConnectivityManager connectionManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取网络的状态信息，有下面三种方式 0手机网络1wifi

		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		if (null != networkInfo) {
			int aaaaString = networkInfo.getType();
			return aaaaString + "";
		} else {
			return "-1";
		}
	}

	/**
	 * 返回用户唯一标识，比如GSM网络的IMSI编号
	 * 
	 * @return
	 */
	public static String getSubscriberId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	/**
	 * 获取当前时间 yyyy年MM月dd日 HH:mm
	 * 
	 * @return
	 */
	public static String getData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 获取当前时间 yyyy年MM月dd日
	 * 
	 * @return
	 */
	public static String getData_yyyy_MM_dd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 获取当前时间 yyyy年MM月dd日 arg 0今天1明天2后天
	 * 
	 * @return
	 */
	public static String getThreeData_yyyy_MM_dd(int arg) {

		Date date = new Date(0);// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, arg);// 把日期往后增加一天.整数往后推,负数往前移动
		date = (Date) calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 获取当前时间 HH
	 * 
	 * @return
	 */
	public static int getData_HH() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		int str = Integer.parseInt(formatter.format(curDate).toString());
		return str;
	}

	/**
	 * 获取当前时间 mm
	 * 
	 * @return
	 */
	public static int getData_mm() {
		SimpleDateFormat formatter = new SimpleDateFormat("mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		int str = Integer.parseInt(formatter.format(curDate).toString());
		return str;
	}

	/**
	 * 比较时间
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static long compare_date(String DATE1, String DATE2) {
		long days = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date dt1 = df.parse(DATE1);
			java.util.Date dt2 = df.parse(DATE2);
			long diff = dt2.getTime() - dt1.getTime();
			days = diff / (1000 * 60 * 60 * 24);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return days;
	}

	/**
	 * 获取SD卡已用 容量 KIB 单位
	 * 
	 * @return
	 */
	public static long getAvailaleSize() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize) / 1024;

		// (availableBlocks * blockSize)/1024 KIB 单位

		// (availableBlocks * blockSize)/1024 /1024 MIB单位

	}

	/**
	 * 获取SD卡总容量KIB 单位
	 * 
	 * @return
	 */
	public static long getAllSize() {

		File path = Environment.getExternalStorageDirectory();

		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getBlockCount();
		return (availableBlocks * blockSize) / 1024;
	}

	/**
	 * 获取SD卡剩余容量
	 */
	public static long getCompareSize() {
		long size = getAllSize() - getAvailaleSize();
		return size;
	}

	/**
	 * 判断Express.db文件是否存在，存在0不存在-1
	 */
	public static int judgeExpressDB() {
		File downLoadDir = new File(Confing.dbPath);
		if (downLoadDir.exists()) {
			return 1;
		}
		return -1;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double GetDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static final double EARTH_RADIUS = 6378137;

	private static GregorianCalendar calendar = new GregorianCalendar();

	/**
	 * 提供“yyyy-mm-dd”形式的字符串到毫秒的转换
	 * 
	 * @param dateString
	 * @return
	 */

	public static long getMillis(String dateString) {

		String[] date = dateString.split("-");

		return getMillis(date[0], date[1], date[2]);

	}

	/**
	 * 根据输入的年、月、日，转换成毫秒表示的时间
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */

	public static long getMillis(int year, int month, int day) {

		GregorianCalendar calendar = new GregorianCalendar(year, month, day);

		return calendar.getTimeInMillis();

	}

	/**
	 * 根据输入的年、月、日，转换成毫秒表示的时间
	 * 
	 * @param yearString
	 * @param monthString
	 * @param dayString
	 * @return
	 */

	public static long getMillis(String yearString, String monthString,

	String dayString) {

		int year = Integer.parseInt(yearString);

		int month = Integer.parseInt(monthString);

		int day = Integer.parseInt(dayString);

		return getMillis(year, month, day);

	}

	/**
	 * 获得当前时间的毫秒表示
	 * 
	 * @return
	 */

	public static long getNow() {

		GregorianCalendar now = new GregorianCalendar();

		return now.getTimeInMillis();

	}

	/**
	 * 根据输入的毫秒数，获得日期字符串
	 * 
	 * @param millis
	 * @return
	 */

	public static String getDate(long millis) {

		calendar.setTimeInMillis(millis);

		return DateFormat.getDateInstance().format(calendar.getTime());

	}

	/**
	 * 根据输入的毫秒数，获得年份
	 * 
	 * @param millis
	 * @return
	 */

	public static int getYear(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.YEAR);

	}

	/**
	 * 根据输入的毫秒数，获得月份
	 * 
	 * @param millis
	 * @return
	 */

	public static int getMonth(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.MONTH);

	}

	/**
	 * 根据输入的毫秒数，获得日期
	 * 
	 * @param millis
	 * @return
	 */

	public static int getDay(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.DATE);

	}

	/**
	 * 根据输入的毫秒数，获得小时
	 * 
	 * @param millis
	 * @return
	 */

	public static int getHour(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.HOUR_OF_DAY);

	}

	/**
	 * 根据输入的毫秒数，获得分钟
	 * 
	 * @param millis
	 * @return
	 */

	public static int getMinute(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.MINUTE);

	}

	/**
	 * 根据输入的毫秒数，获得秒
	 * 
	 * @param millis
	 * @return
	 */

	public static int getSecond(long millis) {

		calendar.setTimeInMillis(millis);

		return calendar.get(Calendar.SECOND);

	}

	/**
	 * 获得今天日期
	 * 
	 * @return
	 */

	public static String getTodayData() {

		return getDate(getNow());

	}

	/**
	 * 获得明天日期
	 * 
	 * @return
	 */

	public static String getTomoData() {

		// 86400000为一天的毫秒数

		return getDate(getNow() + 86400000);

	}

	/**
	 * 获得后天日期
	 * 
	 * @return
	 */

	public static String getTheDayData() {

		return getDate(getNow() + 86400000 + 86400000);

	}
}
