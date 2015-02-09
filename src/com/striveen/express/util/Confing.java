package com.striveen.express.util;

import java.io.File;

import android.os.Environment;

/**
 * 项目中心配置信息
 * 
 * @author Fei
 * 
 */
public class Confing {
	/**
	 * 项目保存用户信息的sp的用户信息
	 */
	public final static String SP_SaveUserInfo = "sp_userinfo";
	/**
	 * 项目保存用户信息的sp的名字
	 */
	public final static String SP_SaveUserInfo_Name = "name";
	/**
	 * 项目保存用户信息的sp的Id
	 */
	public final static String SP_SaveUserInfo_Id = "Id";
	/**
	 * 保存用户token
	 */
	public final static String SP_SaveUserInfo_Token = "userToken";
	/**
	 * 保存用户token
	 */
	public final static String SP_SaveGuidance = "guidance";
	/**
	 * 在sdcard建立项目应用的路径
	 */
	public final static String productPath = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "Express" + File.separator;
	/**
	 * 在sdcard建立项目数据库的路径
	 */
	public final static String dbPath = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "Express" + File.separator + "Express.db";
	/**
	 * 在sdcard图片的物理缓存路径
	 */
	public final static String imgCache = productPath + "imgCache"
			+ File.separator;
	/**
	 * 在sdcard语音的物理缓存路径
	 */
	public final static String voiceCache = productPath + "Voice"
			+ File.separator;
	/**
	 * 使用支付宝是需要的回调地址
	 */
	public static String PayNotifyUrl;
	/**
	 * 使用支付宝是需要
	 */
	public static String PayPartnerID;
	/**
	 * 使用支付宝是需要
	 */
	public static String PaySellerNo;
	/**
	 * 使用支付宝是需要
	 */
	public static String PayPrivateKey;
	/**
	 * 支付宝（RSA）公钥 用签约支付宝账号
	 */
	public static String RSA_ALIPAY_PUBLIC = "";
	/**
	 * 验证码的可用时间
	 */
	public static int ValidationCodeEffectionTime;
	/**
	 * 使用分页加载时每页页数
	 */
	public static int PageSize;
}
