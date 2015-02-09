package com.striveen.express.sql;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.striveen.express.util.Confing;
import com.striveen.express.util.JsonMap;

public class DBManager {

	private static DBManager dbManager;
	private SQLiteDatabase sqlitedb;
	private File f; // 数据库文件
	public DBHelper dbHelper;
	private insertServiceRegionCallBack callBack;

	private int init_Region;
	private int length_Region;

	public DBManager(Context context) {
		dbHelper = new DBHelper(context);
		f = new File(Confing.dbPath);
		// ----如要在SD卡中创建数据库文件，先做如下的判断和创建相对应的目录和文件----
		if (!f.exists()) { // 判断文件是否存在
			try {
				f.createNewFile(); // 创建文件
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
	}

	public synchronized static DBManager getInstance(Context context) {
		if (null == dbManager) {
			dbManager = new DBManager(context);
		}
		return dbManager;
	}

	/**
	 * 创建__发件人用户表
	 */
	public void createCustomerTable() {

		String sql = dbHelper.Customer_column;
		sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
		sqlitedb.execSQL(sql);
		sqlitedb.close();
	}

	/**
	 * 创建__常用地址表
	 */
	public void createCommonlyUsedAddressTable() {
		String sql = dbHelper.CommonlyUsedAddress_column;
		sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
		sqlitedb.execSQL(sql);
		sqlitedb.close();
	}

	/**
	 * 创建__系统内容定义表
	 */
	public void createCMSTable() {
		String sql = dbHelper.CMS_column;
		sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
		sqlitedb.execSQL(sql);
		sqlitedb.close();
	}

	/**
	 * 创建__业务服务定义表
	 */
	public void createServiceRegionTable() {
		String sql = dbHelper.ServiceRegion_column;
		sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
		sqlitedb.execSQL(sql);
		sqlitedb.close();
	}

	// =============================发件人用户表=========================================
	/**
	 * 发件人用户表__添加数据
	 */
	public void insert_Customer(JsonMap<String, Object> data) {
		String sql = "INSERT INTO Customer_column(" + SQLKeys.customer_Mobile
				+ "," + SQLKeys.customer_Code
				+ ",customerId,data) values(?,?,?,?)";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(
					sql,
					new String[] { data.getStringNoNull("mobile"),
							data.getStringNoNull("code"),
							data.getStringNoNull("customerId"),
							data.getStringNoNull("data") });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 发件人用户表__修改数据
	 */
	public void update_Customer(String customer_Code, String CustomerId) {
		String sql = "UPDATE Customer_column set " + SQLKeys.customer_Code
				+ "=? " + "where customerId=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { customer_Code, CustomerId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 发件人用户表__删除数据
	 */
	public void delete_Customer(String CustomerId) {
		String sql = "DELETE FROM Customer_column where Mobile=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { CustomerId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 发件人用户表__查询数据
	 */
	public JsonMap<String, Object> get_Customer() {
		String sql = "select * from Customer_column order by Id";
		Cursor cursor = null;
		JsonMap<String, Object> map = new JsonMap<String, Object>();
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			cursor = sqlitedb.rawQuery(sql, null);
			while (cursor.moveToNext()) {

				map.put("mobile", cursor.getString(1));
				map.put("code", cursor.getString(2));
				map.put("customerId", cursor.getString(3));
				map.put("data", cursor.getString(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			sqlitedb.close();
		}
		return map;
	}

	// =============================常用地址表=========================================
	/**
	 * 常用地址表__添加数据 AddressLabel 0，历史地址；1，常用地址，2收件地址
	 */
	public void insert_CommonlyUsedAddress(String moble,
			JsonMap<String, Object> data, int arg, String AddressLabel) {
		String sql = null;
		String AddressX = null;
		String AddressY = null;
		String[] add = null;
		if (-1 != arg) {
			AddressX = data.getStringNoNull("AddressX");
			AddressY = data.getStringNoNull("AddressY");
			sql = "INSERT INTO CommonlyUsedAddress_column(CustomerId,AddressId,Province,City,Distrct,Address,AddressX,AddressY,AddressType,AddressLabel) values(?,?,?,?,?,?,?,?,?,?)";
			add = new String[] { data.getStringNoNull("CustomerId"),
					data.getStringNoNull("AddressId"),
					data.getStringNoNull("Province"),
					data.getStringNoNull("City"),
					data.getStringNoNull("District"),
					data.getStringNoNull("Address"), AddressX, AddressY,
					data.getStringNoNull("AddressType"), AddressLabel };
		} else if (0 == arg) {
			sql = "INSERT INTO CommonlyUsedAddress_column(CustomerId,Province,City,Distrct,Address,AddressX,AddressY,AddressType,AddressLabel) values(?,?,?,?,?,?,?,?,?)";
			AddressX = data.getStringNoNull("AddressX");
			AddressY = data.getStringNoNull("AddressY");
			add = new String[] { data.getStringNoNull("CustomerId"),
					data.getStringNoNull("Province"),
					data.getStringNoNull("City"),
					data.getStringNoNull("District"),
					data.getStringNoNull("Address"), AddressX, AddressY,
					data.getStringNoNull("AddressType"), AddressLabel };
		} else {
			sql = "INSERT INTO CommonlyUsedAddress_column(CustomerId,AddressId,Province,City,Distrct,Address,AddressX,AddressY,AddressType,AddressLabel) values(?,?,?,?,?,?,?,?,?,?)";
			add = new String[] { data.getStringNoNull("CustomerId"),
					data.getStringNoNull("AddressId"),
					data.getStringNoNull("Province"),
					data.getStringNoNull("City"),
					data.getStringNoNull("District"),
					data.getStringNoNull("Address"), AddressX, AddressY,
					data.getStringNoNull("AddressType"), AddressLabel };
		}
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, add);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 常用地址表__删除数据
	 */
	public void delete_CommonlyUsedAddress(String CommonlyAddressId) {
		String sql = "DELETE FROM CommonlyUsedAddress_column where CommonlyAddressId=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { CommonlyAddressId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 常用地址表__根据地址删除数据
	 */
	public void delete_CommonlyUsedAddress_Address(String Address) {
		String sql = "DELETE FROM CommonlyUsedAddress_column where Address=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { Address });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 安徽省合肥市安山区
	 * 
	 * UPDATE CommonlyUsedAddress_column set Address='湖南省长沙市安山区' where
	 * CommonlyAddressId='9'
	 * 
	 * 常用地址表__修改数据
	 */
	public void update_CommonlyUsedAddress(String Address,
			String CommonlyAddressId) {
		String sql = "UPDATE CommonlyUsedAddress_column set Address=? where CommonlyAddressId=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { Address, CommonlyAddressId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 常用地址表__查询数据 AddressLabel -1全部,0，历史地址；1，常用地址，2收件地址
	 */
	public List<JsonMap<String, Object>> get_CommonlyUsedAddress(
			String AddressLabel) {
		String sql = null;
		String[] aa = null;
		if ("-1".endsWith(AddressLabel)) {
			sql = "select * from CommonlyUsedAddress_column where CommonlyAddressId";
		} else {
			aa = new String[] { AddressLabel };
			sql = "select * from CommonlyUsedAddress_column order by AddressLabel=?";
		}
		Cursor cursor = null;
		List<JsonMap<String, Object>> data = new ArrayList<JsonMap<String, Object>>();
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			cursor = sqlitedb.rawQuery(sql, aa);
			while (cursor.moveToNext()) {
				JsonMap<String, Object> map = new JsonMap<String, Object>();
				map.put("CommonlyAddressId", cursor.getString(0));
				map.put("AddressId", cursor.getString(2));
				map.put("Province", cursor.getString(3));
				map.put("City", cursor.getString(4));
				map.put("District", cursor.getString(5));
				map.put("Address", cursor.getString(6));
				map.put("AddressX", cursor.getString(7));
				map.put("AddressY", cursor.getString(8));
				map.put("AddressType", cursor.getString(9));
				map.put("AddressLabel", cursor.getString(10));
				data.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			sqlitedb.close();
		}
		return data;
	}

	/**
	 * SELECT * FROM CommonlyUsedAddress_column where Address like '%湖%' order
	 * by AddressLabel='1' 常用地址表__查询 匹配数据
	 */
	public List<JsonMap<String, Object>> get_CommonlyUsedAddress(
			String Address, String AddressLabel) {
		String sql = null;
		String[] aa = null;
		if ("-1".endsWith(AddressLabel)) {
			aa = new String[] { "%" + Address + "%" };
			sql = "SELECT * FROM CommonlyUsedAddress_column where Address like ?";
		} else {
			aa = new String[] { "%" + Address + "%", AddressLabel };
			sql = "SELECT * FROM CommonlyUsedAddress_column where Address like ? and AddressLabel=?";
		}
		Cursor cursor = null;

		List<JsonMap<String, Object>> data = new ArrayList<JsonMap<String, Object>>();
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			Log.d("", "aa:" + aa);
			cursor = sqlitedb.rawQuery(sql, aa);
			while (cursor.moveToNext()) {
				JsonMap<String, Object> map = new JsonMap<String, Object>();
				map.put("CommonlyAddressId", cursor.getString(0));
				map.put("AddressId", cursor.getString(2));
				map.put("province", cursor.getString(3));
				map.put("city", cursor.getString(4));
				map.put("Distrct", cursor.getString(5));
				map.put("Address", cursor.getString(6));
				map.put("AddressX", cursor.getString(7));
				map.put("AddressY", cursor.getString(8));
				map.put("AddressType", cursor.getString(9));
				map.put("AddressLabel", cursor.getString(10));
				data.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			sqlitedb.close();
		}
		return data;
	}

	// =============================业务服务定义表=========================================

	public interface insertServiceRegionCallBack {
		public void response();
	}

	/**
	 * 业务服务定义表__添加数据
	 */
	public void insert_ServiceRegion(List<JsonMap<String, Object>> data) {
		insert_ServiceRegion(data, null, 0);
	}

	/**
	 * 业务服务定义表__添加数据
	 */
	public void insert_ServiceRegion(List<JsonMap<String, Object>> data,
			insertServiceRegionCallBack callBack1, int arg) {
		callBack = callBack1;
		String sql = "INSERT INTO ServiceRegion_column(RegionId,Country ,Province ,City ,District ,Town ,ZipCode ,DisplayOrder,IsOpen,Status) values(?,?,?,?,?,?,?,?,?,?)";
		StringBuilder sBuilder = new StringBuilder();
		String sql1 = "INSERT INTO ServiceRegion_column(RegionId,Country ,Province ,City ,District ,Town ,ZipCode ,DisplayOrder,IsOpen,Status) select ";
		sBuilder.append(sql1);
		Log.d("", "data.size():" + data.size());
		init_Region = 500 * arg;
		if (500 > data.size() - init_Region) {
			length_Region = data.size();
		} else {
			length_Region = init_Region + 500;
		}
		for (int i = init_Region; i < length_Region; i++) {

			sBuilder.append("'" + data.get(i).getStringNoNull("RegionId") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("Country") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("Province") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("City") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("District") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("Town") + "'"
					+ ",");
			// data.get(i).getStringNoNull("Town")
			sBuilder.append("'" + data.get(i).getStringNoNull("ZipCode") + "'"
					+ ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("DisplayOrder")
					+ "'" + ",");
			sBuilder.append("'" + data.get(i).getStringNoNull("IsOpen") + "'"
					+ ",");

			if (length_Region - 1 == i) {
				Log.w("", "DBManager   sql1:" + "sBuilder.toString()");
				sBuilder.append("'" + data.get(i).getStringNoNull("Status")
						+ "'");
			} else {
				sBuilder.append("'" + data.get(i).getStringNoNull("Status")
						+ "'" + " union all select ");
			}

			// if (data.size() - 1 == i) {
			// Log.w("", "DBManager   sql1:" + "sBuilder.toString()");
			// sBuilder.append("'" + data.get(i).getStringNoNull("Status")
			// + "'");
			// } else {
			// sBuilder.append("'" + data.get(i).getStringNoNull("Status")
			// + "'" + " union all select ");
			// }

		}
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sBuilder.toString());
			if (null != callBack) {
				callBack.response();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
		// try {
		// sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
		// for (int i = 0; i < data.size(); i++) {
		// sqlitedb.execSQL(sql, new String[] {
		// data.get(i).getStringNoNull("RegionId"),
		// data.get(i).getStringNoNull("Country"),
		// data.get(i).getStringNoNull("Province"),
		// data.get(i).getStringNoNull("City"),
		// data.get(i).getStringNoNull("District"),
		// data.get(i).getStringNoNull("Town"),
		// data.get(i).getStringNoNull("ZipCode"),
		// data.get(i).getStringNoNull("DisplayOrder"),
		// data.get(i).getStringNoNull("IsOpen"),
		// data.get(i).getStringNoNull("Status") });
		// }
		// if (null != callBack) {
		// callBack.response();
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// sqlitedb.close();
		// }
	}

	/**
	 * 业务服务定义表__修改数据
	 */
	public void update_ServiceRegion(String customer_Mobile,
			String customer_Code, String CustomerId) {
		String sql = "UPDATE ServiceRegion_column set "
				+ SQLKeys.customer_Mobile + "=?," + SQLKeys.customer_Code
				+ "=? " + "where CustomerId=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { customer_Mobile,
					customer_Code, CustomerId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 业务服务定义表__删除数据
	 */
	public void delete_ServiceRegion(String CustomerId) {
		String sql = "DELETE FROM ServiceRegion_column where RegionId=?";
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			sqlitedb.execSQL(sql, new String[] { CustomerId });

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 业务服务定义表__查询数据
	 * 
	 * @param arg0
	 *            0查询所有 1查询省 2根据省查询市 3根据市查询区4查询市去掉相同的市
	 * @return
	 */
	public List<JsonMap<String, Object>> get_ServiceRegion(int arg0, String id) {
		String sql = null;
		String[] aa = null;
		if (0 == arg0) {
			sql = "select * from ServiceRegion_column order by RegionId";
		} else if (1 == arg0) {
			sql = "select distinct Province from ServiceRegion_column";
		} else if (2 == arg0) {
			sql = "select distinct City from ServiceRegion_column where Province=?";
			aa = new String[] { id };
		} else if (3 == arg0) {
			aa = new String[] { id };
			sql = "select * from ServiceRegion_column where City=?";
		} else if (4 == arg0) {
			sql = "select distinct City from ServiceRegion_column";
		}
		Cursor cursor = null;
		List<JsonMap<String, Object>> data = new ArrayList<JsonMap<String, Object>>();
		try {
			sqlitedb = SQLiteDatabase.openOrCreateDatabase(f, null);
			cursor = sqlitedb.rawQuery(sql, aa);

			while (cursor.moveToNext()) {
				JsonMap<String, Object> map = new JsonMap<String, Object>();
				if (1 == arg0) {
					map.put("Province", cursor.getString(0));
				} else if (2 == arg0) {
					map.put("City", cursor.getString(0));
				} else if (4 == arg0) {
					map.put("City", cursor.getString(0));
				} else {
					map.put("RegionId", cursor.getString(0));
					map.put("Country", cursor.getString(1));
					map.put("Province", cursor.getString(2));
					map.put("City", cursor.getString(3));
					map.put("District", cursor.getString(4));
					map.put("Town", cursor.getString(5));
					map.put("ZipCode", cursor.getString(6));
					map.put("DisplayOrder", cursor.getString(7));
					map.put("IsOpen", cursor.getString(8));
					map.put("Status", cursor.getString(9));
				}
				data.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			sqlitedb.close();
		}
		return data;
	}
}
