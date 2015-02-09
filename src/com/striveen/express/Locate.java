package com.striveen.express;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 地图工具类
 * 
 * @author Fei
 * 
 */
public class Locate extends Application {

	/**
	 * 百度地图MapController 设置启用内置的缩放控件
	 */
	MapController mMapController;
	/**
	 * 埔便利publicvpi的百度地图LocationClient
	 */
	LocationClient locationClient;
	/**
	 * latitude纬度，longitude经度
	 */
	double latitude = -1, longitude = -1;
	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	private MyLocationListenner myLocationListenner;
	/**
	 * 定位地址
	 */
	private String locationAddress;
	/**
	 * 定位地址
	 */
	private String locationAddr;
	private LocateCallBack callBack;
	private Context context;
	private List<Activity> data_activity;
	private static Locate instance;
	private int noMap;

	/**
	 * 定位省
	 */
	private String Province;
	/**
	 * 定位市
	 */
	private String City;
	/**
	 * 定位区
	 */
	private String District;

	public Locate(LocateCallBack callBack, Context context) {
		this.context = context;
		this.callBack = callBack;
	}

	public Locate() {
	}

	public synchronized static Locate getInstance() {
		if (null == instance) {
			instance = new Locate();
		}
		return instance;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	/**
	 * 退出整个程序
	 */
	public void exit() {
		try {
			for (Activity activity : data_activity) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	/**
	 * 获取activity集合
	 * 
	 * @return
	 */
	public List<Activity> getData_activity() {
		if (null == this.data_activity) {
			data_activity = new ArrayList<Activity>();
		}
		return data_activity;
	}

	/**
	 * 添加activity
	 * 
	 * @param data_activity
	 */
	public void addData_activity(Activity context) {
		if (null == this.data_activity) {
			data_activity = new ArrayList<Activity>();
		}
		this.data_activity.add(context);
	}

	public void SetBMapManager(BMapManager manager) {
		manager = new BMapManager(context);

		manager.init(MyApplication.baiduMapAPI_KEY, new MKGeneralListener() {
			public void onGetNetworkState(int arg0) {
				if (arg0 == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(context, getString(R.string.neterror), 1000)
							.show();
				}
			}

			public void onGetPermissionState(int arg0) {
			}
		});
	}

	/**
	 * 配置地图信息
	 */
	public void SetMap(MapView myMapView) {
		LocationClient mClient = new LocationClient(context);
		SetMap(myMapView, mClient);
	}

	public void SetMap(MapView myMapView, LocationClient lClient) {
		SetMap(myMapView, lClient, 0);
	}

	/**
	 * 配置地图信息
	 */
	public void SetMap(MapView myMapView, LocationClient lClient, int m) {
		this.locationClient = lClient;
		noMap = m;
		myMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		mMapController = myMapView.getController();
		myMapView.setClickable(true);
		myLocationListenner = new MyLocationListenner();
		LocationClientOption locationClientOption = new LocationClientOption();

		locationClientOption.setOpenGps(true);
		// option.setCoorType(mCoorEdit.getText().toString()); //设置坐标类型
		locationClientOption.setServiceName("com.baidu.location.service_v2.9");
		// option.setPoiExtraInfo(mIsAddrInfoCheck.isChecked());
		locationClientOption.setAddrType("all");
		locationClientOption.setScanSpan(10000);

		// locationClientOption.setPriority(LocationClientOption.NetWorkFirst);
		// // 设置网络优先
		locationClientOption.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先

		locationClientOption.setPoiNumber(10);
		locationClientOption.disableCache(true);
		locationClientOption.setCoorType("bd09ll");// 返回国测局经纬度坐标系：gcj02
													// 返回百度墨卡托坐标系 ：bd09
													// 返回百度经纬度坐标系 ：bd09ll

		locationClientOption.setScanSpan(5000);
		locationClient.setLocOption(locationClientOption);
		locationClient.start();
		locationClient.requestLocation();
		locationClient.registerLocationListener(myLocationListenner);
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			// if (-1 != latitude && -1 != longitude) {
			// if (null != callBack) {
			// callBack.setText(latitude, longitude, locationAddr.trim(),
			// Province, City, District);
			// return;
			// }
			// }
			latitude = location.getLatitude();
			Province = location.getProvince();
			City = location.getCity();
			District = location.getDistrict();
			longitude = location.getLongitude();
			if (1 != noMap) {
				GeoPoint point = new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6));
				// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
				mMapController.setCenter(point);// 设置地图中心点
				mMapController.setZoom(17);
				locationClient.stop();
			}

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				/**
				 * 格式化显示地址信息
				 */
				// sb.append("\n省：");
				// sb.append(location.getProvince());
				// sb.append("\n市：");
				// sb.append(location.getCity());
				// sb.append("\n区/县：");
				// sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(locationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			logMsg(sb.toString());

		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			if (-1 != latitude && -1 != longitude) {
				if (null != callBack) {
					callBack.setText(latitude, longitude, locationAddr.trim(),
							Province, City, District);
					return;
				}
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			logMsg(sb.toString());
		}

	}

	public void loadAddre(LocateCallBack cBack, LocationClient lClient) {
		noMap = 1;
		callBack = cBack;
		this.locationClient = lClient;
		// 定位初始化
		// mLocClient = new LocationClient(context);
		myLocationListenner = new MyLocationListenner();
		locationClient.registerLocationListener(myLocationListenner);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09
										// 返回百度经纬度坐标系 ：bd09ll
		option.setScanSpan(1000);
		option.setAddrType("all");
		locationClient.setLocOption(option);
		locationClient.start();
	}

	/**
	 * 显示定位字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			locationAddr = str;
			Log.i("", "locationAddr:" + locationAddr);
			if (locationAddr.indexOf("addr") != -1) {
				// iv_map_locate.setVisibility(2);
				locationAddr = locationAddr.substring(
						locationAddr.indexOf("addr") + 7,
						locationAddr.indexOf("sdk"));
				if (null != callBack) {
					callBack.setText(latitude, longitude, locationAddr.trim(),
							Province, City, District);
				}
			} else {
				locationAddr = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface LocateCallBack {
		public void setText(Double latitude, Double longitude, String str,
				String Province, String City, String District);
	}

	/**
	 * 把view转换成Bitmap，并获得该图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;

	}
}
