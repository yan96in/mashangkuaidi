package com.striveen.express.util;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author xumy
 * 
 */
public class FileUtil {
	/**
	 * SD卡路径
	 */
	public static String SDPATH = Environment.getExternalStorageDirectory()
			.toString() + "/";

	/**
	 * 获取SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean getExernalState() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 创建目录
	 * 
	 * @param dirName
	 *            目录相对路径,省去SDPATH
	 * @return
	 */
	public static boolean mkdir(String dirName) {
		File file = new File(SDPATH + dirName);
		return file.mkdirs();
	}

	/**
	 * 包括扩展名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		String name = new Long(System.currentTimeMillis()).toString() + ".X";
		int index = url.lastIndexOf("/");
		if (index > 0) {
			name = url.substring(index + 1);
			if (name.trim().length() > 0) {
				return name;
			}
		}
		return name;
	}

	/**
	 * 获取文件目录
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileUrl(String url) {
		String name = new Long(System.currentTimeMillis()).toString() + ".X";
		int index = url.lastIndexOf("/");
		if (index > 0) {
			name = url.substring(0, index + 1);
			if (name.trim().length() > 0) {
				return name;
			}
		}
		return name;
	}

	/**
	 * 获取文件名数组 1文件名 2扩展名
	 */
	public static String[] getFileNameArray(String fileName) {
		String fname = getFileNameFromUrl(fileName);
		int index = fname.lastIndexOf(".");
		String names[] = { null, null };
		if (index > 0) {
			names[0] = fname.substring(0, index);
			names[1] = fname.substring(index + 1);
			System.out.println(names[0] + "====" + names[1]);
		}
		return names;
	}
}
