/**
 * Copyright (c) 2013 An Yaming,  All Rights Reserved
 */
package com.striveen.express;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.striveen.express.activity.IndexActivity;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.runmethodinthread.RunMITQueue;
import com.striveen.express.runmethodinthread.RunMITStaticQueue;
import com.striveen.express.runmethodinthread.RunMITUtil;
import com.striveen.express.runmethodinthread.RunMITUtil.IRunMITCallBack;
import com.striveen.express.sql.DBManager;
import com.striveen.express.sql.DBManager.insertServiceRegionCallBack;
import com.striveen.express.upgrade.VersionUpdateUtil;
import com.striveen.express.util.Confing;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.SdcardHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.util.SubstituteEncrypt;
import com.striveen.express.view.AsyImgConfig;
import com.striveen.express.view.ViewInject;

//import com.alipay.android.appDemo4.UsedMobileSecurePayHelper;

/**
 * 初始化
 * 
 * @author LQ
 * 
 */
public class InitActivity extends MyActivity {
	private final String TAG = InitActivity.class.getSimpleName();

	// 全局
	private MyApplication application;
	// 版本升级的工具
	private VersionUpdateUtil util;
	private RunMITUtil runMITUtil;
	// 内存卡管理的帮助类, click = "cb_pdwd"
	private SdcardHelper sdHelper;

	DBManager daDbManager;
	private boolean isLogin;
	private StringBuffer vision;
	/**
	 * 账号保存天数
	 */
	private long data_time = -1;
	private JsonMap<String, Object> data_login;
	private long SDSize;
	/**
	 * 第一次安装启动引导页
	 */
	private boolean checkGuidance;
	@ViewInject(id = R.id.init_vPager)
	private ViewPager init_vPager;
	private List<View> listViews;
	private int guidance_picture[] = { R.drawable.init_guidance1,
			R.drawable.init_guidance2, R.drawable.init_guidance3,
			R.drawable.init_guidance4 };
	SharedPreferences sp_Guidance;

	@ViewInject(id = R.id.init_ll_weizhi)
	private LinearLayout init_ll_weizhi;

	@ViewInject(id = R.id.rl_init)
	private RelativeLayout rl_init;

	private String userId;
	private String cacheWebView = "/data/data/com.courierimmediately/app_webview/Cache";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//
		// 强制为横屏

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		// 竖屏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		try {
			this.unregisterReceiver(goShoppingCartBroadcastReceiver);
		} catch (Exception e) {
		}
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		rl_init.startAnimation(aa);

		application = (MyApplication) getApplication();
		vision = new StringBuffer();
		vision.append(application.getVersionName());
		Confing.PageSize = 3;
		// 异步图片的加载初始化 有物理缓存
		// AsyImgConfig.init(true, 6, Confing.imgCache);
		// 没有屋里缓存
		AsyImgConfig.init(true, 6);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
		getMyApplication().setScreenWidth(screenWidth);
		util = VersionUpdateUtil.init(this);
		runMITUtil = RunMITUtil.init();
		clearCacheFolder(this.getCacheDir(), System.currentTimeMillis());
		sdHelper = new SdcardHelper();
		if (!sdHelper.ExistSDCard()) {
			// Toast.makeText(InitActivity.this, R.string.nosdcard,
			// Toast.LENGTH_SHORT).show();
		} else {
			File downLoadDir = new File(Confing.productPath);
			if (!downLoadDir.exists()) {
				downLoadDir.mkdir();
			}
		}
		clearCacheFolder(new File(cacheWebView), System.currentTimeMillis());
		sp_Guidance = getSharedPreferences(Confing.SP_SaveUserInfo,
				Context.MODE_APPEND);
		checkGuidance = sp_Guidance.getBoolean(Confing.SP_SaveGuidance, false);
		if (!checkGuidance) {
			RunMITStaticQueue queue = new RunMITStaticQueue();
			queue.setCls(Thread.class);
			queue.setMethodName("sleep");
			queue.setParms(new Object[] { 500 });
			queue.setCallBack(new IRunMITCallBack() {

				@Override
				public void onRuned(RunMITQueue queue) {
					if (StringUtil.judgeExpressDB() == -1) {
						SDSize = StringUtil.getCompareSize();
						if (300 <= SDSize) {
							load_Customer();
						} else {
							data_time = -2;
						}
					} else {
						load_Customer();
					}
					showGuidance();
				}
			});
			runMITUtil.runQueue(queue);
		} else {
			if (StringUtil.judgeExpressDB() == -1) {
				SDSize = StringUtil.getCompareSize();
				if (300 <= SDSize) {
					load_Customer();
				} else {
					data_time = -2;
				}
			} else {
				load_Customer();
			}
			// loadingToast.show();
			ThreadPoolManager.getInstance().execute(GetAppVersion);
		}
	}

	/**
	 * @Title: clearCacheFolder
	 * @Description: TODO(方法的作用：清除webView缓存)
	 * @param dir
	 * @param numDays
	 * @return int 返回类型
	 * @throws
	 */
	private int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "删除异常:" + e);
			}
		}
		return deletedFiles;
	}

	/**
	 * 显示引导页
	 */
	private void showGuidance() {
		listViews = new ArrayList<View>();
		// get_Data();
		LayoutInflater mInflater = getLayoutInflater();
		for (int i = 0; i < 4; i++) {
			listViews.add(mInflater.inflate(R.layout.item_guidance, null));
		}
		fulshGuangGaoZhiShiQi(0);
		init_vPager.setAdapter(new MyPagerAdapter(listViews));
		init_vPager.setCurrentItem(0);
		init_vPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ViewPager的Adapter
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * ViewPager设置滑动的监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			fulshGuangGaoZhiShiQi(position);
			((RelativeLayout) listViews.get(position).findViewById(
					R.id.item_guidance_rl))
					.setBackgroundResource(guidance_picture[position]);
			if (listViews.size() - 1 == position) {
				init_ll_weizhi.setVisibility(View.GONE);
				ImageView init_iv = (ImageView) listViews.get(position)
						.findViewById(R.id.item_guidance_iv_init);
				init_iv.setVisibility(View.VISIBLE);
				init_iv.setOnClickListener(clickListener);
			} else {
				init_ll_weizhi.setVisibility(View.VISIBLE);
				((ImageView) listViews.get(position).findViewById(
						R.id.item_guidance_iv_init)).setVisibility(View.GONE);
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// 重新onkeydown拦截返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			InitActivity.this.finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * 进入本界面停留2秒后做的事情
	 */
	private void next() {

		// loadingToast.show();
		// ThreadPoolManager.getInstance().execute(GetAppConfig);

		// data_Province1 = getDbManager.get_ServiceRegion(0, "");
		// if (null == data_Province1 || 0 == data_Province1.size()) {
		// loadingToast.show();
		// ThreadPoolManager.getInstance().execute(GetServiceRegion);
		// }

	}

	/**
	 * 获取服务区域
	 */
	private Runnable GetServiceRegion = new Runnable() {

		@Override
		public void run() {
			getData.doPost(callBack, GetDataConfing.ip, null, "Config",
					"GetServiceRegion", 1);
		}
	};
	/**
	 * 获取客户端版本信息
	 */
	private Runnable GetAppVersion = new Runnable() {

		@Override
		public void run() {
			HashMap<String, Object> par = new HashMap<String, Object>();
			par.put("AppType", "1");
			getData.doPost(callBack, GetDataConfing.ip, par, "Config",
					"GetAppVersion", 2);
		}
	};
	/**
	 * 获取数据回调
	 */
	ResponseCallBack callBack = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			// loadingToast.dismiss();
			if (-1 != index) {
				if (2 == what) {
					InitActivity.this.finish();
				}
				toast.showToast(error[index]);
				return;
			}

			JsonMap<String, Object> data = JsonParseHelper.getJsonMap(msage);
			Log.d(TAG, String.format(getString(R.string.tojson), what) + data);
			if ("1".equals(data.getStringNoNull("ResultFlag"))) {
				JsonMap<String, Object> data_ = data
						.getJsonMap("MessageContent");
				if (getData.isOk(data_)) {
					if (1 == what) {
						// loadingToast.show();

						List<JsonMap<String, Object>> dataList = data_
								.getList_JsonMap("Result");
						Log.e("", "获取服务范围集合大小 dataList:" + dataList.size());
						if (0 == dataList.size() % 500) {
							index = dataList.size() / 500;
						} else {
							index = dataList.size() / 500 + 1;
						}
						for (int i = 0; i < index; i++) {
							daDbManager.insert_ServiceRegion(dataList,
									callBack2, i);
						}

					} else if (2 == what) {
						List<JsonMap<String, Object>> data_list = data_
								.getList_JsonMap("Result");
						getMyApplication().setAppVersion(
								data_list.get(0).getStringNoNull("Version"));

						StringBuffer aa = new StringBuffer();
						aa.append(data_list.get(0).getStringNoNull("Version"));
						if (null != aa) {
							if (Float.parseFloat(vision.deleteCharAt(
									vision.indexOf(".")).toString()) == Float
									.parseFloat(aa
											.deleteCharAt(aa.indexOf("."))
											.toString())) {
								goMainActivity();
							} else {
								showNewVersion(data_list.get(0));
							}
						} else {
							goMainActivity();
						}
					}
				}
			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};
	private insertServiceRegionCallBack callBack2 = new insertServiceRegionCallBack() {

		@Override
		public void response() {
			// loadingToast.dismiss();
			goMainActivity();
		}
	};

	/**
	 * 提示用户有新版本
	 */
	private void showNewVersion(final JsonMap<String, Object> info) {
		// 给用户提示框
		Builder builder = new Builder(InitActivity.this);
		builder.setTitle(String.format(getString(R.string.app_version_select),
				info.getStringNoNull("Version")));
		builder.setCancelable(false);
		builder.setMessage(info.getStringNoNull("Description"));
		builder.setPositiveButton(R.string.app_version_update,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String appName = getString(R.string.app_name);
						util.doUpdateVersion(info.getStringNoNull("AppFile"),
								Confing.productPath + appName + ".apk",
								appName, R.drawable.ic_launcher, true);
						goMainActivity();
					}
				});
		if (!info.getBoolean("IsUpgrade")) {
			builder.setNegativeButton(R.string.app_version_iknow,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							goMainActivity();
						}
					});
		}

		builder.show();
	}

	/**
	 * 加载本地数据
	 */
	private void load_Customer() {
		daDbManager = DBManager.getInstance(InitActivity.this);
		daDbManager.createCustomerTable();
		data_login = daDbManager.get_Customer();

		if (null != data_login) {
			Log.e("", "data_login:" + data_login);
			if (TextUtils.isEmpty(data_login.getStringNoNull("mobile"))) {
				return;
			}
			if (TextUtils.isEmpty(data_login.getStringNoNull("data"))) {
				return;
			}
			if (TextUtils.isEmpty(data_login.getStringNoNull("customerId"))) {
				return;
			}
			data_time = StringUtil.compare_date(
					data_login.getStringNoNull("data"),
					StringUtil.getData_yyyy_MM_dd());
		}
	}

	/**
	 * 离开本界面到主界面去
	 */
	private void goMainActivity() {

		String mobile;
		Intent intent = new Intent();
		// if (-2 != data_time) {
		// if (-1 == data_time
		// || 7 < data_time
		// || "11".equals(SubstituteEncrypt.getInstance().decrypt(
		// data_login.getStringNoNull("code")))) {
		// if (-1 != data_time) {
		// // daDbManager.delete_Customer(SubstituteEncrypt.getInstance()
		// // .decrypt(data_login.getStringNoNull("customerId")));
		// daDbManager.update_Customer(SubstituteEncrypt.getInstance()
		// .encrypt("11"), SubstituteEncrypt.getInstance()
		// .encrypt(data_login.getStringNoNull("customerId")));
		// }
		// intent.setClass(this, UserLoginActivity.class);
		// if (TextUtils.isEmpty(data_login.getStringNoNull("mobile"))) {
		// intent.putExtra(ExtraKeys.Key_Msg1, "-1");
		// } else {
		// intent.putExtra(ExtraKeys.Key_Msg1,
		// data_login.getStringNoNull("mobile"));
		// }
		//
		// } else {
		// // intent.setClass(this, IndexMapActivity.class);
		// intent.setClass(this, IndexActivity.class);
		// SharedPreferences sp = getSharedPreferences(
		// Confing.SP_SaveUserInfo, Context.MODE_APPEND);
		// sp.edit()
		// .putString(
		// Confing.SP_SaveUserInfo_Id,
		// SubstituteEncrypt.getInstance().decrypt(
		// data_login
		// .getStringNoNull("customerId")))
		// .commit();
		// getMyApplication().setUserId(
		// sp.getString(Confing.SP_SaveUserInfo_Id, ""));
		// }
		// } else {
		// intent.setClass(this, UserLoginActivity.class);
		// }

		if (-1 < data_time
				&& 8 > data_time
				&& !"11".equals(SubstituteEncrypt.getInstance().decrypt(
						data_login.getStringNoNull("code")))) {
			userId = SubstituteEncrypt.getInstance().decrypt(
					data_login.getStringNoNull("customerId"));
			mobile = SubstituteEncrypt.getInstance().decrypt(
					data_login.getStringNoNull("mobile"));
		} else {
			if (7 < data_time) {
				mobile = SubstituteEncrypt.getInstance().decrypt(
						data_login.getStringNoNull("mobile"));
			} else {
				mobile = "";
			}
			userId = "";
		}
		intent.setClass(this, IndexActivity.class);
		SharedPreferences sp = getSharedPreferences(Confing.SP_SaveUserInfo,
				Context.MODE_APPEND);
		sp.edit().putString(Confing.SP_SaveUserInfo_Id, userId).commit();
		getMyApplication().setUserPhone(mobile);
		startActivity(intent);
		this.finish();
		// LoadData();
	}

	/**
	 * 刷新展示广告的指示器的信息
	 */
	private void fulshGuangGaoZhiShiQi(int selectPposition) {
		init_ll_weizhi.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(4, 0, 4, 0);
		lp.weight = 1;
		for (int i = 0; i < listViews.size(); i++) {
			ImageView iv = new ImageView(this, null, R.style.defaultImgBG);
			if (i == selectPposition) {
				iv.setImageResource(R.drawable.init_guanggao_select);
			} else {
				iv.setImageResource(R.drawable.init_guanggao_noselect);
			}
			iv.setScaleType(ScaleType.FIT_XY);
			init_ll_weizhi.addView(iv, lp);
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sp_Guidance.edit().putBoolean(Confing.SP_SaveGuidance, true)
					.commit();
			goMainActivity();
		}
	};
}
