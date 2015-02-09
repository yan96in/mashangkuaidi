package com.striveen.express.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.striveen.express.util.JsonMap;

/**
 * 查询省份数据库的工具类
 */
public class DialogCityDB {

	private static DialogCityDBManager dbm;
	private static SQLiteDatabase db;
	private static DialogCityDB myDialogCityDB;

	public DialogCityDB(Context context) {
		dbm = new DialogCityDBManager(context);
	}

	public synchronized static DialogCityDB getInstance(Context context) {
		if (null == myDialogCityDB) {
			myDialogCityDB = new DialogCityDB(context);
		}
		return myDialogCityDB;
	}

	/**
	 * 业务服务定义表__查询数据
	 * 
	 * @param arg0
	 *            0查询所有 1查询省 2根据省查询市 3根据市查询区4查询市去掉相同的市
	 * @return
	 */
	public List<JsonMap<String, Object>> get_ServiceRegion(int arg0, String id) {
		dbm.openDatabase();
		db = dbm.getDatabase();
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
			cursor = db.rawQuery(sql, aa);

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
			// cursor.close();
			dbm.closeDatabase();
		}
		return data;
	}

	private int init_Region;
	private int length_Region;

	/**
	 * 业务服务定义表__添加数据
	 */
	public void insert_ServiceRegion(List<JsonMap<String, Object>> data, int arg) {
		String sql = "INSERT INTO ServiceRegion_column(RegionId,Country ,Province ,City ,District ,Town ,ZipCode ,DisplayOrder,IsOpen,Status) values(?,?,?,?,?,?,?,?,?,?)";
		StringBuilder sBuilder = new StringBuilder();
		String sql1 = "INSERT INTO ServiceRegion_column(RegionId,Country ,Province ,City ,District ,Town ,ZipCode ,DisplayOrder,IsOpen,Status) select ";
		sBuilder.append(sql1);
		dbm.openDatabase();
		db = dbm.getDatabase();
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
		}
		try {
			db.execSQL(sBuilder.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		dbm.closeDatabase();
	}
}