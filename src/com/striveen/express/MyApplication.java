/**
 */
package com.striveen.express;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKPoiInfo;
import com.striveen.express.util.Confing;
import com.striveen.express.util.JsonMap;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.tencent.android.tpush.service.channel.protocol.TpnsRedirectReq;
import com.tencent.stat.StatService;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.sso.UMQQSsoHandler;

/**
 * 全局
 * 
 * @author Fei
 * 
 */
public class MyApplication extends Application {

	// sdk controller
	private UMSocialService mController = null;
	/**
	 * 分享文字内容
	 */
	private String shareContext = "马上快递很好用哦，最近一直在用@马上快递，不用一直在苦苦等待快递师傅哦%>_<%，每次发快递很快就来咯O(∩_∩)O，要发快递就马上快递，你也来体验一下哦。";
	private String shareContext1 = "最近一直在用@%1$s，闪电取件，寄快递就马上快递，你也来体验一下！下载地址http://www.mabukeji.com/down/c.html";
	/**
	 * 登录用户的id
	 */
	private String userId;
	/**
	 * 登录用户的name
	 */
	private String userName;
	public static  String userphone;
	private String token;
	private String appVersion;

	private String data_sheng[];
	private String data_shi[][];
	private String data_qu[][];
	/**
	 * 定位地址
	 */
	private String locationAddr;
	/**
	 * latitude纬度
	 */
	private double latitude;
	/**
	 * longitude经度
	 */
	private double longitude;
	/**
	 * 定位省
	 */
	private String province;
	/**
	 * 定位市
	 */
	private String city;
	/**
	 * 定位区
	 */
	private String district;

	private MKPoiInfo myMkPoiInfo;
	private String courierRefreshTime;
	private String MyToken;
	/**
	 * 客服电话
	 */
	private String serviceTel;
	/**
	 * QQ
	 */
	private String CompanyQQ;
	/**
	 * 微博
	 */
	private String SinaWeiBo;
	/**
	 * 微信公众号
	 */
	private String TecentWeChat;
	private List<JsonMap<String, Object>> data_dialog;
	/**
	 * 修改发件地址
	 */
	private JsonMap<String, Object> data_sendAddress;
	private JsonMap<String, Object> data_courier;

	private List<JsonMap<String, Object>> data_orderDetaile;

	private String searchKey;
	
	public static int devMode = -1;
	
	public int getDevMode(){
		return devMode;
	}

	/**
	 * 二维码扫描信息
	 */
	public String getSearchKey() {
		return searchKey;
	}

	/**
	 * 二维码扫描信息
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<JsonMap<String, Object>> getData_orderDetaile() {
		return data_orderDetaile;
	}

	public void setData_orderDetaile(
			List<JsonMap<String, Object>> data_orderDetaile) {
		this.data_orderDetaile = data_orderDetaile;
	}

	/**
	 * 推荐该用户的快递员
	 */
	public JsonMap<String, Object> getData_courier() {
		return data_courier;
	}

	/**
	 * 推荐该用户的快递员
	 */
	public void setData_courier(JsonMap<String, Object> data_courier) {
		this.data_courier = data_courier;
	}

	/**
	 * @Title: getData_sendAddress
	 * @Description: TODO(方法的作用：获取发件地址)
	 * @return JsonMap<String,Object> 返回类型
	 * @throws
	 */
	public JsonMap<String, Object> getData_sendAddress() {
		return data_sendAddress;
	}

	/**
	 * @Title: setData_sendAddress
	 * @Description: TODO(方法的作用：缓存发件地址)
	 * @param data_sendAddress
	 *            void 返回类型
	 * @throws
	 */
	public void setData_sendAddress(JsonMap<String, Object> data_sendAddress) {
		this.data_sendAddress = data_sendAddress;
	}

	/**
	 * 取消订单原因
	 * 
	 * @return
	 */
	public List<JsonMap<String, Object>> getData_dialog() {
		if (null == data_dialog) {
			data_dialog = new ArrayList<JsonMap<String, Object>>();
		}
		return data_dialog;
	}

	/**
	 * 取消订单原因
	 * 
	 * @return
	 */
	public void setData_dialog(List<JsonMap<String, Object>> data_dialog) {
		if (null == data_dialog) {
			data_dialog = new ArrayList<JsonMap<String, Object>>();
		}
		this.data_dialog = data_dialog;
	}

	/**
	 * QQ
	 */
	public String getCompanyQQ() {
		return CompanyQQ;
	}

	/**
	 * QQ
	 */
	public void setCompanyQQ(String companyQQ) {
		CompanyQQ = companyQQ;
	}

	/**
	 * 微博
	 */
	public String getSinaWeiBo() {
		return SinaWeiBo;
	}

	/**
	 * 微博
	 */
	public void setSinaWeiBo(String sinaWeiBo) {
		SinaWeiBo = sinaWeiBo;
	}

	/**
	 * 微信公众号
	 */
	public String getTecentWeChat() {
		return TecentWeChat;
	}

	/**
	 * 微信公众号
	 */
	public void setTecentWeChat(String tecentWeChat) {
		TecentWeChat = tecentWeChat;
	}

	/**
	 * 客服电话
	 */
	public String getServiceTel() {
		return serviceTel;
	}

	/**
	 * 客服电话
	 */
	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	/**
	 * : 快递员端刷新时间（秒
	 * 
	 * @return
	 */
	public String getCourierRefreshTime() {
		return courierRefreshTime;
	}

	/**
	 * : 快递员端刷新时间（秒
	 */
	public void setCourierRefreshTime(String courierRefreshTime) {
		this.courierRefreshTime = courierRefreshTime;
	}

	// public MKPoiInfo getMyMkPoiInfo() {
	// return myMkPoiInfo;
	// }
	//
	// public void setMyMkPoiInfo(MKPoiInfo myMkPoiInfo) {
	// this.myMkPoiInfo = myMkPoiInfo;
	// }

	/**
	 * 定位地址
	 */
	public String getLocationAddr() {
		return locationAddr;
	}

	/**
	 * 定位地址
	 */
	public void setLocationAddr(String locationAddr) {
		this.locationAddr = locationAddr;
	}

	/**
	 * latitude纬度
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * latitude纬度
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * longitude经度
	 */

	public double getLongitude() {
		return longitude;
	}

	/**
	 * longitude经度
	 */

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 定位省
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 定位省
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 定位市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 定位市
	 */

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 定位区
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * 定位区
	 */

	public void setDistrict(String district) {
		this.district = district;
	}

	private int screenWidth;

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 设置屏幕宽度
	 * 
	 * @return
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * 客户端版本号
	 * 
	 * @return
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * 客户端版本号
	 * 
	 * @return
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * 获取登录Token
	 * 
	 * @return
	 */
	public String getUserToken() {
		return token;

	}

	public void setUserToken(String token) {
		this.token = token;
	}

	public String[] getData_sheng() {
		return data_sheng;
	}

	public void setData_sheng(String[] data_sheng) {
		this.data_sheng = data_sheng;
	}

	public String[][] getData_shi() {
		return data_shi;
	}

	public void setData_shi(String[][] data_shi) {
		this.data_shi = data_shi;
	}

	public String[][] getData_qu() {
		return data_qu;
	}

	public void setData_qu(String[][] data_qu) {
		this.data_qu = data_qu;
	}

	/**
	 * 获取当前登录用户的id 如果没有登录 则返回""
	 * 
	 * @return
	 */
	public String getUserId() {
		SharedPreferences sp = getSharedPreferences(Confing.SP_SaveUserInfo,
				Context.MODE_APPEND);
		return sp.getString(Confing.SP_SaveUserInfo_Id, "");
	}

	/**
	 * 设置当前登录用户的id 只能在登录后及注销时使用 其他位置请勿修改
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		if (TextUtils.isEmpty(userId)) {
		}
		this.userId = userId;
	}

	/**
	 * 获取当前登录用户的Name 如果没有登录 则返回""
	 * 
	 * @return getMyApplication().setUserId(
	 *         SubstituteEncrypt.getInstance().decrypt(
	 *         data_login.getStringNoNull("customerId")));
	 */
	public String getUserName() {
		return null == userName ? null : userName;
	}

	/**
	 * 设置当前登录用户的Name 只能在登录后及注销时使用 其他位置请勿修改
	 * 
	 * @param userId
	 */
	public void setUserName(String userName) {
		if (TextUtils.isEmpty(userName)) {
		}
		this.userName = userName;
	}

	/**
	 * 获取当前登录用户的手机号的Phone 如果没有登录 则返回""
	 * 
	 * @return
	 */
	public String getUserPhone() {
		return null == userphone ? "" : userphone;
	}

	/**
	 * 设置当前登录用户的手机号的Phone 只能在登录后及注销时使用 其他位置请勿修改
	 * 
	 * @param userId
	 */
	public void setUserPhone(String userphone) {
		if (TextUtils.isEmpty(userphone)) {
		}
		this.userphone = userphone;
	}


	/**
	 * 信鸽Token
	 * 
	 * @return
	 */
	public String getMyToken() {
		return MyToken;
	}

	/**
	 * 信鸽Token
	 * 
	 * @return
	 */
	public void setMyToken(String myToken) {
		MyToken = myToken;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	public boolean fileIsExists(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	public static final String readF1(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		String line = br.readLine();
		// for (String line = br.readLine(); line != null; line = br.readLine())
		// {
		// System.out.println(line);
		// }
		br.close();
		return line;
	}
	
	public static String baiduMapAPI_KEY;

	@Override
	public void onCreate() {
		super.onCreate();
		
	    ApplicationInfo appInfo = null;
		try {
			appInfo = getPackageManager()
			        .getApplicationInfo(getPackageName(),
			PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		devMode = appInfo.metaData.getInt("DEV_TEST_PUB");
		baiduMapAPI_KEY = appInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
	    Log.d("MyApplication", "Application devMode == " + devMode );
	    
		// 将程序运行中出现的错误碎片进行捕获
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(this);
		// 设定展示日志信息 正式发布的时候 一定不能忘记 设定不展示日志信息
		// 设定不展示日志信息
		// Log.isShowLog(false);
		if (TextUtils.isEmpty(getMyToken())) {
			XGPushConfig.enableDebug(this, true);
			// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
			// XGIOperateCallback)带callback版本
			// 如果需要绑定账号，请使用registerPush(getApplicationContext(),"account")版本
			// 具体可参考详细的开发指南
			// 传递的参数为ApplicationContext
			TpnsRedirectReq s;
			XGPushManager.registerPush(getApplicationContext(),
					new XGIOperateCallback() {

						@Override
						public void onSuccess(Object arg0, int arg1) {
							XGPushManager.registerPush(getApplicationContext(),
									"Customer" + arg0.toString());
							setMyToken(arg0.toString());
							// XGPushManager.setTag(this, "UserAccount")
						}

						@Override
						public void onFail(Object arg0, int arg1, String arg2) {
						}
					});
			XGPushConfig.getToken(this);
			enableComponentIfNeeded(getApplicationContext(),
					XGPushService.class.getName());
			StatService.trackCustomEvent(this, "onCreate", "");
		}
		SharedPreferences sp = getSharedPreferences(Confing.SP_SaveUserInfo,
				Context.MODE_APPEND);
		Token_ = sp.getString(Confing.SP_SaveUserInfo_Token, "");
		if (TextUtils.isEmpty(Token_) || "0".equals(Token_)) {
			sp.edit()
					.putString(Confing.SP_SaveUserInfo_Token,
							XGPushConfig.getToken(this)).commit();
		}
		if (devMode == 2) {
			/*
			 * 捕获全局异常模块
			 */
			MyCrashHandler handler = MyCrashHandler.getInstance();
			handler.init(getApplicationContext());
			Thread.setDefaultUncaughtExceptionHandler(handler);
		}
	}

	private String Token_;

	/**
	 * 分享给QQ好友
	 */
	public void shareQQ() {
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，
		// 参数3为开发者在QQ互联申请的APP kEY.
		// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
		// (Activity) getApplicationContext(), "801524601",
		// "e909e6d431ce33a6b894718c47a16091");
		// qqSsoHandler.addToSocialSDK();
		// QQShareContent qqShareContent = new QQShareContent();
		// qqShareContent.setShareContent(shareContext);
		// qqShareContent.setTitle("hello, title");
		// qqShareContent.setShareImage(new UMImage(
		// (Activity) getApplicationContext(), R.drawable.ic_launcher));
		// qqShareContent.setTargetUrl("你的URL链接");
		// mController.setShareMedia(qqShareContent);
	}

	/**
	 * 打开分享面板
	 * 
	 * @param context
	 */
	public void addShare(final Activity context) {
		if (null == mController) {
			mController = UMServiceFactory
					.getUMSocialService("com.striveen.express");
		}

		// TODO 设置新浪SSO handle
		SinaShareContent sinaShare = new SinaShareContent();
		sinaShare.setShareContent(String.format(shareContext1, "马布科技_马上快递"));
		UMImage localImage = new UMImage(context, R.drawable.share_icon);
		sinaShare.setShareImage(localImage);
		mController.setShareMedia(sinaShare);

		// TODO 设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent(String.format(shareContext1, "mashangapp"));
		tencent.setShareImage(localImage);
		// TODO 设置tencent分享内容
		mController.setShareMedia(tencent);

		// TODO wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx65158dcd3ecbf393";
		UMWXHandler wxHandler = new UMWXHandler(context, appId);
		wxHandler.addToSocialSDK();
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(shareContext);
		weixinContent.setTitle("马上快递");
		weixinContent.setTargetUrl("http://www.mabukeji.com/down/c.html");
		weixinContent.setShareImage(localImage);
		mController.setShareMedia(weixinContent);
		// TODO 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appId);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContext);
		circleMedia.setTitle("马上快递——发快递神器");
		circleMedia.setShareImage(localImage);
		circleMedia.setTargetUrl("http://www.mabukeji.com/down/c.html");
		mController.setShareMedia(circleMedia);

		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.TENCENT);
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		mController.getConfig().closeToast();
		// mController.openShare(IndexActivity.this, false);
		mController.openShare(context, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				// 分享结束，eCode==200代表分享成功，非200代表分享失败
				if (eCode == 200) {
					Toast.makeText(context, "分享成功", 1).show();
				}
			}
		});
	}

	// 启用被禁用组件方法

	private static void enableComponentIfNeeded(Context context,
			String componentName) {
		PackageManager pmManager = context.getPackageManager();
		if (pmManager != null) {
			ComponentName cnComponentName = new ComponentName(
					context.getPackageName(), componentName);
			int status = pmManager.getComponentEnabledSetting(cnComponentName);
			if (status != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
				pmManager.setComponentEnabledSetting(cnComponentName,
						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);
			}
		}
	}

	public void SetBMapManager(BMapManager manager, Context context) {
		manager = new BMapManager(context);
		manager.init("L2gzBFtGLiLuz5ehG9WRzey9", new MKGeneralListener() {
			public void onGetNetworkState(int arg0) {
				if (arg0 == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.neterror), 1000).show();
				}
			}

			public void onGetPermissionState(int arg0) {
			}
		});
	}

	/**
	 * 获取当前应用的版本号：
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
