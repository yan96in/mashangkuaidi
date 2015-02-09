package com.striveen.express.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 此类继承了SQLiteOpenHelper抽象类，是一个辅助器类，需要 一个构造函数和重写两个方法。
 * 
 * @author Fei
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Express.db"; // 数据库名
	public static final int VERSION = 1; // 版本号
	public static final String TABLE_NAME = "andy"; // 表名
	/**
	 * 发件人用户表
	 */
	public static final String TABLE_Customer = SQLKeys.customer;
	/**
	 * 发件人用户表
	 */
	String Customer_column = "CREATE TABLE IF NOT EXISTS Customer_column(Id integer PRIMARY KEY autoincrement,"
			+ SQLKeys.customer_Mobile
			+ " varchar(20),"
			+ SQLKeys.customer_Code
			+ " varchar(20),customerId varchar(20),data varchar(30));";
	/**
	 * 常用地址
	 */
	public static final String TABLE_CommonlyUsedAddress = "CommonlyUsedAddress";
	/**
	 * 常用地址
	 * 
	 * CustomerId 用户id,AddressId 地址id, province 省份, city 城市, Distrct 区域,Address
	 * 地址,AddressX 地址经度,AddressY 地址纬度 ,AddressType 地址类型,AddressLabel
	 * 0，历史地址；1，常用地址，2收件地址
	 */
	String CommonlyUsedAddress_column = "CREATE TABLE IF NOT EXISTS CommonlyUsedAddress_column(CommonlyAddressId integer PRIMARY KEY autoincrement,"
			+ "CustomerId varchar(10),"
			+ "AddressId varchar(10),"
			+ "Province varchar(50),"
			+ "City varchar(50),"
			+ "Distrct varchar(50),"
			+ "Address varchar(200),"
			+ "AddressX varchar(20),"
			+ "AddressY varchar(20),"
			+ "AddressType varchar(20)," + "AddressLabel varchar(20));";
	/**
	 * 系统内容定义表
	 */
	public static final String TABLE_CMS = "CMS";
	/**
	 * 系统内容定义表
	 */
	String CMS_column = "CREATE TABLE IF NOT EXISTS CMS_column(CMSId integer PRIMARY KEY autoincrement,"
			+ "CMSCode varchar(20)," + "CMSContent varchar(20));";
	/**
	 * 业务服务定义表
	 */
	public static final String TABLE_ServiceRegion = "ServiceRegion";
	/**
	 * 业务服务定义表 RegionId int(4) √ 自增长，业务服务区域表唯一标识 Country narchar(50) √ 国家名称，默认中国
	 * Province narchar(50) √ 省 City narchar(50) √ 城市 District narchar(50) √ 区域
	 * Town narchar(50) √ 城镇 ZipCode narchar(50) √ 邮编 DisplayOrder int(4) 显示顺序
	 * IsOpen Bit 是否开通服务 0 未开通1 开通 Status Bit 状态标识0 无效1 有效
	 */
	String ServiceRegion_column = "CREATE TABLE IF NOT EXISTS ServiceRegion_column(RegionId integer PRIMARY KEY autoincrement,"
			+ "Country varchar(50),"
			+ "Province varchar(50),"
			+ "City varchar(50),"
			+ "District varchar(50),"
			+ "Town varchar(50),"
			+ "ZipCode varchar(50),"
			+ "DisplayOrder int varchar(4),"
			+ "IsOpen boolean varchar(4),"
			+ "Status boolean varchar(4));";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("DBOpenHelper",
				"DBOpenHelperDBOpenHelperDBOpenHelperDBOpenHelper");
		db.execSQL(Customer_column);
		db.execSQL(CommonlyUsedAddress_column);
		db.execSQL(CMS_column);
		db.execSQL(ServiceRegion_column);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("DBOpenHelper", "onUpgradeonUpgradeonUpgradeonUpgrade");
		db.execSQL("DROP TABLE IF EXISTS Customer_column");
		db.execSQL("DROP TABLE IF EXISTS CommonlyUsedAddress_column");
		db.execSQL("DROP TABLE IF EXISTS CMS_column");
		db.execSQL("DROP TABLE IF EXISTS ServiceRegion_column");
		onCreate(db);
	}

}
