package com.striveen.express.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.buihha.audiorecorder.Mp3Recorder;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.MyApplication;
import com.striveen.express.R;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.Confing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.SdcardHelper;
import com.striveen.express.view.RoundProgressBar;

/**
 * 主界面，百度定位
 * 
 * @author Fei
 * 
 */
public class IndexActivity extends IndexAllActivity {
	/**
	 * 分享文字内容
	 */
	private final String TAG = "IndexActivity";
	/**
	 * 百度地图BMapManager
	 */
	public BMapManager manager = null;
	/**
	 * 百度地图MapView
	 */
	private MapView myMapView = null;
	/**
	 * 快递员标识集合
	 */
	List<JsonMap<String, Object>> data_mark;
	/**
	 * 百度地图MapController 设置启用内置的缩放控件
	 */
	MapController mMapController;
	/**
	 * 百度地图LocationClient
	 */
	LocationClient locationClient;

	/**
	 * latitude纬度，longitude经度
	 */
	double latitude, longitude;
	/**
	 * 地图弹出框view
	 */
	private View popview;
	/**
	 * 地图弹出框
	 */
	private PopupOverlay pop;
	MenuDrawer myMenuDrawer;
	/**
	 * 显示所在位置
	 */
	private TextView tv_mylocate;
	/**
	 * 将当前位置移动到屏幕中央
	 */
	private ImageView iv_map_locate;
	/**
	 * 录音
	 * 
	 */
	private CheckBox cb_bottom_left;
	/**
	 * 判断是语音还是文字
	 */
	private boolean isChecked;
	/**
	 * 底部文字输入
	 */
	private TextView tv_bottom_address;
	/**
	 * 底部录音中
	 */
	private TextView tv_bottom_recording;

	private RelativeLayout rl_recording;
	private EditText et_address;
	private RelativeLayout rl_bottom_address1;

	private LinearLayout ll_order;
	private LinearLayout ll_friend;
	private LinearLayout ll_setting;
	/**
	 * 判断是从哪个activity中跳转过来的
	 */
	private String checkIndex;
	private Locate myLocate;
	/**
	 * 更改发件地址
	 */
	private String Address;
	/**
	 * 更改发件地址 选中的地址
	 */
	private String select_data;
	/**
	 * 文字发快递备注
	 */
	private String Remark;

	private SdcardHelper sdHelper;
	/**
	 * 录音Mp3
	 */
	private Mp3Recorder recorder;
	/**
	 * 验证码还可以使用的秒数
	 */
	private int yanzhenmakeyongmiao = 0;
	private File myRecAudioDir;
	private Handler handler = new Handler();
	private RoundProgressBar index_rpb;
	/**
	 * 录音时间
	 */
	private int progress = 0;
	/**
	 * 录音剩余时间
	 */
	private String progress1;
	/**
	 * 录音时间
	 */
	private TextView tv_recording_time;
	private RelativeLayout rl_mashang;
	private ImageView iv_mashang;
	/**
	 * 推荐的快递员
	 */
	private ImageView iv_Customes;

	private int historyLocation;

	// private LoadingDialogManager loadingToast;

	// http://developer.baidu.com/map/sdkandev-4.htm#col23 百度地图周边检索
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		initGPS();
		locationClient = new LocationClient(this);
		myLocate = new Locate(callBack, getApplication());
		myLocate.SetBMapManager(manager);
		myMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		myMenuDrawer.setContentView(R.layout.activity_index);
		myMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);

		myMenuDrawer.setMenuView(R.layout.activity_index_drawer_left);// 隐藏菜单
		// 隐去标题栏（应用程序的名字）
		isChecked = true;
		initView();
		Locate.getInstance().addData_activity(this);
		recorder = new Mp3Recorder();
		sdHelper = new SdcardHelper();
		historyLocation = 0;
	}

	private void initView() {
		index_rpb = (RoundProgressBar) findViewById(R.id.index_rpb);

		et_address = (EditText) findViewById(R.id.index_et_address);

		cb_bottom_left = (CheckBox) findViewById(R.id.index_cb_bottom_left);
		iv_map_locate = (ImageView) findViewById(R.id.index_iv_map_locate);
		iv_mashang = (ImageView) findViewById(R.id.index_iv_mashang);
		iv_Customes = (ImageView) findViewById(R.id.index_iv_customes);

		ImageView left_iv_top = (ImageView) findViewById(R.id.index_left_iv_top);
		// if (!TextUtils.isEmpty(getIndexApplication().getUserId())) {
		// left_iv_top.setImageResource(R.drawable.index_login_portrait);
		// } else {
		// left_iv_top.setImageResource(R.drawable.index_no_login_portrait);
		// }

		tv_mylocate = (TextView) findViewById(R.id.index_tv_mylocate);
		tv_bottom_address = (TextView) findViewById(R.id.index_tv_bottom_address);
		tv_bottom_recording = (TextView) findViewById(R.id.index_tv_bottom_recording);
		tv_recording_time = (TextView) findViewById(R.id.index_tv_time);

		rl_recording = (RelativeLayout) findViewById(R.id.index_rl_recording);
		rl_bottom_address1 = (RelativeLayout) findViewById(R.id.index_rl_bottom_address1);
		rl_mashang = (RelativeLayout) findViewById(R.id.index_rl_mashang);

		ll_order = (LinearLayout) findViewById(R.id.index_left_ll_order);
		ll_friend = (LinearLayout) findViewById(R.id.index_left_ll_friend);
		ll_setting = (LinearLayout) findViewById(R.id.index_left_ll_setting);

		myMapView = (MapView) findViewById(R.id.index_my_mapv);
		mMapController = myMapView.getController();
		ll_order.setOnClickListener(onClickListener);
		ll_friend.setOnClickListener(onClickListener);
		ll_setting.setOnClickListener(onClickListener);
		TextView user_phone_number = (TextView) findViewById(R.id.user_phone_number);
		user_phone_number.setText(MyApplication.userphone);

		cb_bottom_left
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean Checked) {
						isChecked = Checked;
						check();
					}
				});
		myLocate.SetMap(myMapView, locationClient);
		check();
		// tv_mylocate.setText(String.format(
		// getString(R.string.index_tv_my_locate),
		// getString(R.string.index_tv_locate)));
		rl_bottom_address1.setOnTouchListener(onTouchListener);

		et_address.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
					// 先隐藏键盘
					// if (!TextUtils.isEmpty(et_address.getText().toString()))
					// {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(IndexActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 跳转到搜索结果界面
					Intent intent = new Intent(IndexActivity.this,
							Index2Activity.class);
					intent.putExtra(ExtraKeys.Key_Msg1, "1");
					if (!TextUtils.isEmpty(select_data)) {
						Address = select_data;
					}
					Remark = et_address.getText().toString();
					Log.e("", "Remark:" + Remark);
					intent.putExtra(ExtraKeys.Key_Msg2, Address);
					intent.putExtra(ExtraKeys.Key_Msg3, Remark);
					startActivity(intent);
					et_address.setText(null);
				}

				// }
				return false;
			}
		});
	}

	/**
	 * 地图定位回调
	 */

	private LocateCallBack callBack = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province, String City, String District) {
			if (0 == historyLocation && !TextUtils.isEmpty(Province)) {

				latitude = latitude1;
				longitude = longitude1;
				province = Province;
				city = City;
				district = District;
				iv_map_locate.setVisibility(0);
				tv_mylocate.setText(String.format(
						getString(R.string.index_tv_my_locate), str));
				Locate.getInstance().setLocationAddress(str);
				getIndexApplication().setLatitude(latitude);
				getIndexApplication().setLongitude(longitude);
				getIndexApplication().setProvince(Province);
				getIndexApplication().setCity(City);
				getIndexApplication().setDistrict(District);
				getIndexApplication().setLocationAddr(str);
				JsonMap<String, Object> jsonMap = new JsonMap<String, Object>();
				jsonMap.put("CustomerId", getIndexApplication().getUserId());
				jsonMap.put("Province", Province);
				jsonMap.put("City", City);
				jsonMap.put("District", District);
				jsonMap.put("Address", str);
				jsonMap.put("AddressX", longitude);
				jsonMap.put("AddressY", latitude);
				getIndexApplication().setData_sendAddress(jsonMap);
				createMark(4, 0);
			} else if (1 == historyLocation) {
				JsonMap<String, Object> data_map = getIndexApplication()
						.getData_sendAddress();
				// if (!TextUtils.isEmpty(select_data)) {
				if (null != data_map) {
					// 清除地图上已有的所有覆盖物
					myMapView.getOverlays().clear();
					// JsonMap<String, Object> data_map = JsonParseHelper
					// .getJsonMap(select_data);

					GeoPoint myGeoPoint = new GeoPoint(
							(int) (Double.parseDouble(data_map
									.getStringNoNull("AddressY")) * 1E6),
							(int) (Double.parseDouble(data_map
									.getStringNoNull("AddressX")) * 1E6));
					latitude = Double.parseDouble(data_map
							.getStringNoNull("AddressY"));
					longitude = Double.parseDouble(data_map
							.getStringNoNull("AddressX"));
					province = data_map.getStringNoNull("Province");
					city = data_map.getStringNoNull("City");
					district = data_map.getStringNoNull("District");
					mMapController.setCenter(myGeoPoint);
					mMapController.setZoom(17);
					createMark(4, 0);
					// locationClient.stop();

					Log.e(TAG, "2222将搜索到的兴趣点标注在地图上");
				}
			}
			// ThreadPoolManager.getInstance().execute(GetEffectiveServiceRegion);
			if (!TextUtils.isEmpty(Province)) {
				if (null != locationClient) {
					locationClient.stop();
					// locationClient = null;
				}
				loadingToast.show();
				ThreadPoolManager.getInstance().execute(NearByCouriers);
			}

		}

	};
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
	private String CustomerId;
	/**
	 * 获取附近的快递员
	 */
	private Runnable NearByCouriers = new Runnable() {

		@Override
		public void run() {
			// {'LocationX':'121.396470',LocationY:'31.174560'}'Province':'上海市','City':'上海市','District':'闵行区'
			HashMap<String, Object> param = new HashMap<String, Object>();
			CustomerId = getIndexApplication().getUserId();
			if (TextUtils.isEmpty(CustomerId)) {
				CustomerId = "0";
			}
			param.put("CustomerId", CustomerId);
			param.put("LocationX", longitude);
			param.put("LocationY", latitude);
			param.put("Province", province);
			param.put("City", city);
			param.put("District", district);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"NearByCouriersIsRecommed", 0);
		}
	};
	/**
	 * 获取公共配置参数
	 */
	private Runnable GetAppConfig = new Runnable() {

		@Override
		public void run() {
			// {'Type':1}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("Type", "0");
			getData.doPost(callBack1, GetDataConfing.ip, param, "Config",
					"GetAppConfig", 10);
		}
	};
	/**
	 * 更新录音时间
	 */
	private Runnable updateTime = new Runnable() {

		@Override
		public void run() {

			progress += 100 / 50;
			index_rpb.setProgress(progress);
			yanzhenmakeyongmiao++;
			progress1 = yanzhenmakeyongmiao >= 10 ? "" + yanzhenmakeyongmiao
					: "0" + yanzhenmakeyongmiao;
			tv_recording_time.setText("00:" + progress1);
			if (50 == yanzhenmakeyongmiao) {
				stopRecorder();
				handler.removeCallbacks(this);
				progress = 0;
				rl_recording.setVisibility(View.GONE);
				tv_bottom_recording.setVisibility(View.GONE);
				iv_mashang.setVisibility(View.VISIBLE);
				rl_mashang.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(select_data)) {
					Address = select_data;
				}
				Intent intent = new Intent(IndexActivity.this,
						Index2Activity.class);
				intent.putExtra(ExtraKeys.Key_Msg1, "0");
				intent.putExtra(ExtraKeys.Key_Msg2, Address);
				intent.putExtra(ExtraKeys.Key_Msg3, "a");
				startActivity(intent);
			} else {
				handler.postDelayed(updateTime, 1000);
			}

		}
	};
	/**
	 * TODO 获取数据回调
	 */
	private ResponseCallBack callBack1 = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			if (11 != what) {
				loadingToast.dismiss();
			}

			if (-1 != index) {
				toast.showToast(error[index]);
				return;
			}
			JsonMap<String, Object> data = JsonParseHelper.getJsonMap(msage);
			Log.d(TAG, String.format(getString(R.string.tojson), what) + data);
			if ("1".equals(data.getStringNoNull("ResultFlag"))) {
				JsonMap<String, Object> data_ = data
						.getJsonMap("MessageContent");

				if (getData.isOk(data_)) {
					if (0 == what) {
						JsonMap<String, Object> courier_map = null;
						data_mark = data_.getList_JsonMap("Result");
						// {"CourierId":79,"TrackingX":121.396240,"TrackingY":31.175030,"IsRecommed":0},
						for (JsonMap<String, Object> map : data_mark) {
							if ("1".equals(map.getString("IsRecommed"))) {
								courier_map = map;
							}
						}
						// if (null == courier_map) {
						// iv_Customes.setVisibility(View.GONE);
						// } else {
						// iv_Customes.setVisibility(View.VISIBLE);
						// }
						getIndexApplication().setData_courier(courier_map);
						courier_map = null;
						createMark(4, 0);
					} else if (10 == what) {
						List<JsonMap<String, Object>> data_list = data_
								.getList_JsonMap("Result");
						for (int i = 0; i < data_list.size(); i++) {
							if ("CourierRefreshTime".equals(data_list.get(i)
									.getStringNoNull("ConfigKey"))) {
								getIndexApplication().setCourierRefreshTime(
										data_list.get(i).getStringNoNull(
												"ConfigValue"));
							} else if ("ServiceTel".equals(data_list.get(i)
									.getStringNoNull("ConfigKey"))) {
								getIndexApplication().setServiceTel(
										data_list.get(i).getStringNoNull(
												"ConfigValue"));
							} else if ("CompanyQQ".equals(data_list.get(i)
									.getStringNoNull("ConfigKey"))) {
								getIndexApplication().setCompanyQQ(
										data_list.get(i).getStringNoNull(
												"ConfigValue"));
							} else if ("SinaWeiBo".equals(data_list.get(i)
									.getStringNoNull("ConfigKey"))) {
								getIndexApplication().setSinaWeiBo(
										data_list.get(i).getStringNoNull(
												"ConfigValue"));
							} else if ("TecentWeChat".equals(data_list.get(i)
									.getStringNoNull("ConfigKey"))) {
								getIndexApplication().setTecentWeChat(
										data_list.get(i).getStringNoNull(
												"ConfigValue"));
							}
						}
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};

	private void check() {
		if (isChecked) {
			rl_bottom_address1.setVisibility(View.VISIBLE);
			tv_bottom_address.setVisibility(View.GONE);
			et_address.setVisibility(View.GONE);
			// tv_bottom_address.setText(null);
			// tv_bottom_address.setBackgroundResource(R.drawable.index_bt_voice);
		} else {
			tv_bottom_address.setVisibility(View.VISIBLE);
			et_address.setVisibility(View.GONE);
			rl_bottom_address1.setVisibility(View.GONE);
			tv_bottom_address.setText(getString(R.string.index_tv_address11));
			tv_bottom_address
					.setBackgroundResource(R.drawable.index_tv_bottom_address);
		}

	}

	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 左边的
			case R.id.index_left_ll_order:
				// TODO 跳转发件历史
				if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
					goLogin();
					break;
				}
				startActivity(new Intent(IndexActivity.this,
						UserOrderActivity.class));
				myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_friend:
				getIndexApplication().addShare(IndexActivity.this);
				// myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_setting:
				if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
					goLogin();
					break;
				}
				startActivity(new Intent(IndexActivity.this,
						UserSettingActivity.class));
				myMenuDrawer.closeMenu();
				break;

			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(TAG, "requestCode=" + requestCode);
		if (resultCode == RESULT_OK) {
			if (0 == requestCode) {
				if (null == locationClient) {
					locationClient = new LocationClient(this);
				}

				historyLocation = 1;
				// myLocate.SetMap(myMapView, locationClient, 1);

				Address = data.getStringExtra(ExtraKeys.Key_Msg1);
				select_data = data.getStringExtra(ExtraKeys.Key_Msg2);
				tv_mylocate.setText(String.format(
						getString(R.string.index_tv_my_locate), Address));
				// locationClient.start();// 现在修改发件地址但不修改经纬度，所以不用重新定位
				// mMapController = myMapView.getController();

				// if (null != getIndexApplication().getMyMkPoiInfo()) {
				// PoiOverlay poioverlay = new PoiOverlay(IndexActivity.this,
				// myMapView);
				// // 清除地图上已有的所有覆盖物
				// myMapView.getOverlays().clear();
				// ArrayList<MKPoiInfo> dat_a = new ArrayList<MKPoiInfo>();
				// dat_a.add(getIndexApplication().getMyMkPoiInfo());
				// // 设置搜索到的POI数据
				// poioverlay.setData(dat_a);
				// // 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）
				// myMapView.getOverlays().add(poioverlay);
				// mMapController.setCenter(getIndexApplication()
				// .getMyMkPoiInfo().pt);
				// mMapController.setZoom(17);
				// latitude = getIndexApplication().getMyMkPoiInfo().pt
				// .getLatitudeE6() / 1E6;
				// longitude = getIndexApplication().getMyMkPoiInfo().pt
				// .getLongitudeE6() / 1E6;
				// createMark(4, 0);
				// Log.e(TAG, "1111将搜索到的兴趣点标注在地图上");
				// }
				// onResume();
			} else if (2 == requestCode) {
				loadingToast.show();
				ThreadPoolManager.getInstance().execute(NearByCouriers);
			}
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		checkIndex = intent.getStringExtra(ExtraKeys.Key_Msg1);
		if ("1".equals(checkIndex)) {
			createMark(3, 2);
			myMapView.onResume();
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
	        onBackPressed();
	        return true;
	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {
	        //监控/拦截菜单键
	    } else if(keyCode == KeyEvent.KEYCODE_HOME) {
	        //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/**
	 * 返回监听
	 */
	@Override
	public void onBackPressed() {
		// MyApplication.backPressed(Practice_Test.this);
		final int drawerState = myMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			myMenuDrawer.closeMenu();
			return;
		} else {
			Builder builder = new Builder(this);
			builder.setTitle(getString(R.string.main_toast_tishi));
			builder.setMessage(getString(R.string.main_toast_quit));
			builder.setPositiveButton(getString(R.string.Cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							IndexActivity.this.onResume();
						}
					});
			builder.setNegativeButton(getString(R.string.Ensure),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							SharedPreferences sp = getSharedPreferences(
									Confing.SP_SaveUserInfo,
									Context.MODE_APPEND);
							sp.edit().putString(Confing.SP_SaveUserInfo_Id, "")
									.commit();
							Locate.getInstance().exit();
							// IndexActivity.super.finish();
						}
					});
			builder.show();
		}
		// super.onBackPressed();
	}

	/**
	 * 创建标识位置
	 */
	@SuppressLint("NewApi")
	public void createMark(int arg0, int arg1) {
		Log.d("", "latitude=" + latitude + ";longitude=" + longitude);
		Drawable mark = getResources().getDrawable(R.drawable.index_map_marka);
		OverlayTest itemOverlay = new OverlayTest(mark, myMapView);

		if (null != data_mark && 0 < data_mark.size()) {
			for (int i = 0; i < data_mark.size(); i++) {

				if (!TextUtils.isEmpty(data_mark.get(i).getStringNoNull(
						"TrackingY"))) {

					double mLat1 = Double.parseDouble(data_mark.get(i)
							.getStringNoNull("TrackingY"));
					double mLon1 = Double.parseDouble(data_mark.get(i)
							.getStringNoNull("TrackingX"));
					GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6),
							(int) (mLon1 * 1E6));
					OverlayItem item = new OverlayItem(p1, "item" + i, "item"
							+ i);
					if ("1".equals(data_mark.get(i).getStringNoNull(
							"IsRecommed"))) {
						View view = LayoutInflater.from(IndexActivity.this)
								.inflate(R.layout.item_map_mark, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.item_map_mark_tv_title);
						TextView tv_companyname = (TextView) view
								.findViewById(R.id.item_map_mark_tv_companyname);
						String CourierName = data_mark.get(i).getStringNoNull(
								"CourierName");
						String CompanyName = data_mark.get(i).getStringNoNull(
								"CompanyName");
						Log.e(TAG, "CourierName:" + CourierName);
						tv_name.setText(CourierName);
						tv_companyname.setText(CompanyName);
						Bitmap bitmap = Locate.convertViewToBitmap(view);
						item.setMarker(new BitmapDrawable(bitmap));
					} else {
						item.setMarker(getResources().getDrawable(
								R.drawable.index_map_marka));
					}

					// aa
					itemOverlay.addItem(item);
				}

			}
		}

		int a = data_mark == null ? 1 : data_mark.size() + 1;
		GeoPoint p1 = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "item" + a, "item" + a);

		itemOverlay.addItem(item1);
		myMapView.getOverlays().clear();
		item1.setMarker(getResources().getDrawable(R.drawable.index_package));
		myMapView.getOverlays().add(itemOverlay);
		myMapView.refresh();
	}

	private int[] aa = { R.drawable.icon_marka, R.drawable.icon_markb,
			R.drawable.icon_markc, R.drawable.icon_markd,
			R.drawable.icon_marke, R.drawable.icon_markf };

	/**
	 * @ClassName: OverlayTest
	 * @Description: 
	 *               TODO(该类的作用：自定义图层：ItemizedOverlay)要处理overlay点击事件时需要继承ItemizedOverlay
	 *               不处理点击事件时可直接生成ItemizedOverlay.
	 * @author Fei
	 * @date 2014年9月10日 下午4:24:29
	 * 
	 */
	class OverlayTest extends ItemizedOverlay<OverlayItem> {

		public OverlayTest(Drawable mark, MapView mapView) {
			super(mark, mapView);

		}

		public OverlayTest(Drawable mark, MapView mapView, MapView mapView1) {
			super(mark, mapView);

		}

		/**
		 * (非 Javadoc)
		 * 
		 * @Title: onTap
		 * @Description: TODO(方法的作用：地图标记点点击事件)
		 * @param index
		 * @return
		 * @see com.baidu.mapapi.map.ItemizedOverlay#onTap(int)
		 */
		protected boolean onTap(final int index) {
			// 在此处理item点击事件
			if (0 == index) {
				// Intent intent = new Intent(IndexActivity.this,
				// HistoryAddressActivity.class);
				// startActivityForResult(intent, 0);
			}

			System.out.println("item onTap: " + index);
			pop = new PopupOverlay(myMapView, new PopupClickListener() {

				public void onClickedPopup(int age0) {
					pop.hidePop();
				}

			});

			pop.hidePop();
			if (index < data_mark.size()) {
				if ("1".equals(data_mark.get(index).getStringNoNull(
						"IsRecommed"))) {
					Intent intent = new Intent(IndexActivity.this,
							Index2Activity.class);
					intent.putExtra(ExtraKeys.Key_Msg1, "3");
					startActivity(intent);
				}
			}

			// popview = LayoutInflater.from(getBaseContext()).inflate(
			// R.layout.layout_index_popview, null);// 获取要转换的View资源
			// popview.setVisibility(View.GONE);
			// popview.destroyDrawingCache();
			// TextView address, tel;
			// address = (TextView) popview
			// .findViewById(R.id.layout_index_tv_address);
			// String add = data_mark.get(index).get("ShopAddress").toString();
			// if (add == null) {
			// address.setText("");
			// } else {
			// address.setText(add);
			// }
			// tel = (TextView) popview.findViewById(R.id.layout_index_tv_tel);
			// String tell = data_mark.get(index).get("Tel").toString();
			// if (tell == null) {
			// tel.setText("");
			// } else {
			// tel.setText(tell);
			// }
			// // Toast.makeText(getApplicationContext(), add, 1000).show();
			// Bitmap popbitmap = myLocate.convertViewToBitmap(popview);
			// double mLat1 = Double.parseDouble(data_mark.get(index)
			// .get("Latitude").toString());
			// double mLon1 = Double.parseDouble(data_mark.get(index)
			// .get("Longitude").toString());
			// GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 *
			// 1E6));
			// pop.showPopup(popbitmap, p1, 32);
			// popview.destroyDrawingCache();

			return true;
		}

	}

	/**
	 * @Title: iv_top_handle
	 * @Description: TODO(方法的作用：打开抽屉)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void iv_top_handle(View v) {
		myMenuDrawer.openMenu();
	}

	/**
	 * 回到定位位置
	 * 
	 * @param v
	 */
	public void iv_map_locate(View v) {
		GeoPoint geoPoint = new GeoPoint((int) (latitude * 1e6),
				(int) (longitude * 1e6));
		myMapView.getController().animateTo(geoPoint);
	}

	/**
	 * 定位地址点事件
	 * 
	 * @param v
	 */
	public void locate_rl_address(View v) {
		if (TextUtils.isEmpty(tv_mylocate.getText().toString().trim())) {
			toast.showToast(getString(R.string.index_toast_location_address));
			return;
		}
		Intent intent = new Intent(IndexActivity.this,
				HistoryAddressActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "01");
		startActivityForResult(intent, 0);
	}

	/**
	 * 文字发快递
	 * 
	 * @param v
	 */
	public void tv_bottom_address(View v) {
		// if (!getString(R.string.index_toast_region).equals(checkRegion)) {
		// if (0 == GetEffective) {
		// GetEffective++;
		// toast.showToast(getString(R.string.index_toast_region_11));
		// ThreadPoolManager.getInstance().execute(
		// GetEffectiveServiceRegion);
		// } else {
		// toast.showToast(getString(R.string.index_toast_region12));
		// }
		// return;
		// }
		// TODO 文字发快递
		if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
			goLogin();
			return;
		}
		if (TextUtils.isEmpty(tv_mylocate.getText().toString().trim())) {
			toast.showToast(getString(R.string.index_toast_location_address));
			return;
		}
		if (!isChecked) {
			Intent intent = new Intent(IndexActivity.this,
					HistoryAddressActivity.class);
			intent.putExtra(ExtraKeys.Key_Msg1, "02");
			startActivityForResult(intent, 0);
		}
	}

	/**
	 * 预约
	 * 
	 * @param v
	 */
	public void iv_bottom_right(View v) {
		// TODO 预约 发件
		// if (!getString(R.string.index_toast_region).equals(checkRegion)) {
		// if (0 == GetEffective) {
		// GetEffective++;
		// toast.showToast(getString(R.string.index_toast_region_11));
		// ThreadPoolManager.getInstance().execute(
		// GetEffectiveServiceRegion);
		// } else {
		// toast.showToast(getString(R.string.index_toast_region13));
		// }
		// return;
		// }
		// if (!getString(R.string.index_toast_region).equals(checkRegion)) {
		// toast.showToast(getString(R.string.index_toast_region13));
		// return;
		// }
		if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
			goLogin();
			return;
		}
		if (TextUtils.isEmpty(tv_mylocate.getText().toString().trim())) {
			toast.showToast(getString(R.string.index_toast_location_address));
			return;
		}
		startActivity(new Intent(IndexActivity.this, ReservationActivity.class));
	}

	@Override
	protected void onDestroy() {
		// clean.cleanApplicationData(getApplicationContext(), null);
		myMapView.destroy();

		if (manager != null) {
			manager.destroy();
			manager = null;

			if (data_mark != null) {
				data_mark.clear();
				myMapView.destroyDrawingCache();
			}
		}
		if (null != locationClient) {
			locationClient.stop();
			locationClient = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onPause() {
		myMapView.onPause();
		// 按个人定位显示起始地的代码

		if (manager != null) {
			manager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// locationClient.start();
		myMapView.onResume();
		// locationClient.stop();

		if (manager != null) {
			manager.start();
		}
		super.onResume();
	}

	/**
	 * @Title: iv_Customes
	 * @Description: TODO(方法的作用：针对快递员一对一发快递)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void iv_Customes(View v) {
		Intent intent = new Intent(IndexActivity.this, Index2Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "3");
		startActivity(intent);
	}

	/**
	 * 语音发快递
	 * 
	 * @param v
	 */
	public void bottom_address(View v) {
		// if (!getString(R.string.index_toast_region).equals(checkRegion)) {
		// if (0 == GetEffective) {
		// GetEffective++;
		// toast.showToast(getString(R.string.index_toast_region_11));
		// ThreadPoolManager.getInstance().execute(
		// GetEffectiveServiceRegion);
		// } else {
		// toast.showToast(getString(R.string.index_toast_region12));
		// }
		// return;
		// }
		// if (!getString(R.string.index_toast_region).equals(checkRegion)) {
		// toast.showToast(checkRegion);
		// return;
		// }
		// TODO 语音发快递
		if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
			goLogin();
			return;
		}
		if (TextUtils.isEmpty(tv_mylocate.getText().toString().trim())) {
			toast.showToast(getString(R.string.index_toast_location_address));
			return;
		}
		Intent intent = new Intent(IndexActivity.this, Index1Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "0");
		if (!TextUtils.isEmpty(select_data)) {
			Address = select_data;
		}
		intent.putExtra(ExtraKeys.Key_Msg2, Address);
		intent.putExtra(ExtraKeys.Key_Msg3, "a");
		startActivity(intent);
	}

	private static long lastClickTime;
	private static long laskTime;

	/**
	 * 为了防止用户或者测试MM疯狂的点击某个button
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1500) {
			lastClickTime = time;
			laskTime = time - lastClickTime;
			return true;
		}
		laskTime = time - lastClickTime;
		lastClickTime = time;
		return false;
	}

	/**
	 * 长按录音
	 */
	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// if (TextUtils.isEmpty(checkRegion)) {
			// toast.showToast(getString(R.string.index_toast_location_address11));
			// return false;
			// }
			// if (!getString(R.string.index_toast_region).equals(checkRegion))
			// {
			// toast.showToast(checkRegion);
			// return false;
			// }
			if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
				goLogin();
				return false;
			}
			if (TextUtils.isEmpty(tv_mylocate.getText().toString().trim())) {
				toast.showToast(getString(R.string.index_toast_location_address));
				return false;
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) { // 按下
				if (isFastDoubleClick()) {
					return false;
				}
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) { // 按下
				rl_recording.setVisibility(View.VISIBLE);
				tv_bottom_recording.setVisibility(View.VISIBLE);
				iv_mashang.setVisibility(View.GONE);
				rl_mashang.setVisibility(View.GONE);
				iv_map_locate.setVisibility(View.GONE);
				recorder();
				yanzhenmakeyongmiao = 0;
				handler.post(updateTime);

				return true;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {// 弹起
				rl_recording.setVisibility(View.GONE);
				tv_bottom_recording.setVisibility(View.GONE);
				iv_mashang.setVisibility(View.VISIBLE);
				rl_mashang.setVisibility(View.VISIBLE);
				iv_map_locate.setVisibility(View.VISIBLE);

				stopRecorder();
				progress = 0;
				handler.removeCallbacks(updateTime);
				if (3 > yanzhenmakeyongmiao) {
					yanzhenmakeyongmiao = 0;
					Toast.makeText(IndexActivity.this,
							getString(R.string.index1_toast_voice),
							Toast.LENGTH_SHORT).show();
				} else {
					yanzhenmakeyongmiao = 0;
					if (!TextUtils.isEmpty(select_data)) {
						Address = select_data;
					}
					Intent intent = new Intent(IndexActivity.this,
							Index2Activity.class);
					intent.putExtra(ExtraKeys.Key_Msg1, "0");
					intent.putExtra(ExtraKeys.Key_Msg2, Address);
					intent.putExtra(ExtraKeys.Key_Msg3, "a");
					startActivity(intent);
				}

				return true;
			}
			return false;
		}
	};

	/**
	 * @Title: left_ll_balance
	 * @Description: TODO(方法的作用：我的余额)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void left_ll_balance(View v) {
		if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
			goLogin();
			return;
		}
		startActivity(new Intent(IndexActivity.this, MyBalanceActivity.class));
	}

	/**
	 * @Title: ll_invite
	 * @Description: TODO(方法的作用：邀请朋友)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void ll_invite(View v) {
		if (TextUtils.isEmpty(getIndexApplication().getUserId())) {
			goLogin();
			return;
		}
		Intent intent = new Intent(IndexActivity.this,
				ServiceTermsActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		startActivity(intent);
	}

	/**
	 * 开始录音
	 */
	public void recorder() {

		try {
			if (!sdHelper.ExistSDCard()) {
				// Toast.makeText(IndexActivity.this, R.string.nosdcard,
				// Toast.LENGTH_SHORT).show();
				return;
			}
			myRecAudioDir = new File(Confing.voiceCache);
			if (!myRecAudioDir.exists()) {
				myRecAudioDir.mkdir();
			}
			recorder.startRecording();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Start error");
		}

	}

	/**
	 * 停止录音
	 */
	private void stopRecorder() {
		try {
			recorder.stopRecording();
		} catch (IOException e) {
			Log.d(TAG, "Stop error");
		}
	}

	/**
	 * 未登录时去登陆
	 */
	private void goLogin() {
		// TODO 未登录时去登陆
		startActivityForResult(new Intent(IndexActivity.this,
				UserLoginActivity.class), 2);
	}

	private void initGPS() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS模块是否开启，如果没有则开启
		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			// 转到手机设置界面，用户设置 ACTION_SECURITY_SETTINGS
			Builder builder = new Builder(this);
			builder.setTitle(getString(R.string.main_toast_tishi));
			builder.setMessage("请打开GPS开关以便师傅尽快联系您");
			builder.setPositiveButton(getString(R.string.Cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			builder.setNegativeButton(getString(R.string.Ensure),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
						}
					});
			builder.show();
		}
	}
}
