/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express.net;

import com.striveen.express.MyApplication;


/**
 * 获取数据信息的配置信息
 * 
 * @author Fei
 * 
 */
public class GetDataConfing {
	/**
	 * 获取数据的服务器地址
	 */
	
	public static String ip1; 
	public static String ip;
	
	static{
		if (MyApplication.devMode == 2) {
//			线上地址
			ip = "http://app.mashangapp.com/CommandHandler.ashx";
			ip1 = "http://app.mashangapp.com";
		} else if (MyApplication.devMode == 1) {
//			test
			ip = "http://testapp.mashangapp.com/CommandHandler.ashx";
			ip1 = "http://testapp.mashangapp.com";
		}else if (MyApplication.devMode == 0) {
//			dev
			ip = "http://192.168.1.31/CommandHandler.ashx";
			ip1 = "http://192.168.1.31";
		}
	}
	
	/**
	 * 所有的action的父亲
	 */
	public final static String Action_Basic = ip + "/";
	/**
	 * helloworld
	 */
	public final static String Action_helloWorld = Action_Basic
			+ "ProudctWebService.asmx/HelloWorld";
	/**
	 * 获取手机验证码
	 */
	public final static String Action_GetVerificationCode = Action_Basic
			+ "GetVerificationCode?Mobile=";
	/**
	 * 发件人登录
	 */
	public final static String Action_CustomerLogin = Action_Basic
			+ "CustomerLogin";
	/**
	 * 发件人获取附近的快递员
	 */
	public final static String Action_NearByCouriers = Action_Basic
			+ "NearByCouriers?";
	/**
	 * 发件人获取附近的快递员 参数配置
	 */
	public final static String Parame_NearByCouriers = "Lng=%1$s&Lat=%2$s";
	/**
	 * 发件人常用地址
	 */
	public final static String Action_CustomerAddress = Action_Basic
			+ "CustomerAddress?Token=";

	/**
	 * 发件人历史订单
	 */
	public final static String Action_CustomerOrders = Action_Basic
			+ "CustomerOrders?";
	/**
	 * 发件人历史订单 参数配置
	 */
	public final static String Parame_CustomerOrders = "Token=%1$s&PageNum=%2$s&Range=%3$s";
	/**
	 * 意见反馈
	 */
	public final static String Action_PostAdvice = Action_Basic + "PostAdvice?";
	/**
	 * 意见反馈 参数配置
	 */
	public final static String Parame_PostAdvice = "Token=%1$s&Content=%2$s";
	/**
	 * 上传图片文件
	 */
	public final static String Action_PostImage = Action_Basic + "PostImage";
	/**
	 * 文件上传 Filetype照片传1 音频传2
	 */
	public final static String Action_PostFile = Action_Basic + "PostFile";
	/**
	 * 获取最新版本号
	 */
	public final static String Action_GetAppVersion = Action_Basic
			+ "GetAppVersion?";
}
