package com.striveen.express.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.MyApplication;
import com.striveen.express.R;
import com.striveen.express.ToastErrorActivity;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.sql.DialogCityDB;
import com.striveen.express.util.Confing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.SdcardHelper;
import com.striveen.express.wheel.widget.ArrayWheelAdapter;
import com.striveen.express.wheel.widget.OnWheelChangedListener;
import com.striveen.express.wheel.widget.WheelView;

/**
 * 主界面，百度定位
 * 
 * @author Fei
 * 
 */
public class Index2Activity extends IndexAllActivity {

	private final String TAG = "Index2Activity";
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
	private double latitude, longitude;
	/**
	 * 地图弹出框view
	 */
	private View popview;
	/**
	 * 地图弹出框
	 */
	private PopupOverlay pop;
	/**
	 * 地图上展示图片的数组
	 */
	private int[] mark_picture = { R.drawable.index_package,
			R.drawable.index_type_select, R.drawable.index_map_voice,
			R.drawable.index_map_marka, R.drawable.index_waiting_orders_icom };
	MenuDrawer myMenuDrawer;
	/**
	 * 手柄点击
	 * 
	 */
	private ImageView iv_top_handle;
	/**
	 * 显示所在位置
	 */
	private TextView tv_mylocate;
	/**
	 * 将当前位置移动到屏幕中央
	 */
	private ImageView iv_map_locate;

	/**
	 * 加载地图布局
	 */
	private LinearLayout index2_ll_map;
	/**
	 * 底部立即发送或取消发送按键
	 */
	private TextView tv_bottom_immediately_send;
	/**
	 * 已通知快递员人数，等待时间
	 */
	private TextView tv_communicated;
	/**
	 * 超时重新发送订单
	 */
	private TextView tv_bottom_again_send;

	private RelativeLayout rl_type;

	private ImageView ImageView01;
	private ImageView ImageView02;
	private ImageView ImageView03;

	private RelativeLayout bottom_rl_bt_1;
	private RelativeLayout bottom_rl_bt_2;
	private RelativeLayout bottom_rl_bt_3;
	private RelativeLayout rl_tv_wv;
	private LinearLayout ll_wheelview;
	private TextView tv_wv_end;
	private Button bt_wv_confirm;

	private LinearLayout ll_order;
	private LinearLayout ll_friend;
	private LinearLayout ll_setting;
	private Locate myLocate;
	/**
	 * 语音播放
	 */
	private MediaPlayer mMediaPlayer;
	/**
	 * 录音文件路径
	 */
	private File myPlayFile;
	/**
	 * 录音文件集合
	 */
	private ArrayList<String> recordFiles;
	private SdcardHelper sdHelper;
	private File myRecAudioDir;

	/**
	 * 判断是不是语音 0语音 1文本 2预约3一对一发送订单
	 */
	private String checkVoice;
	private WheelView wv_sheng;
	private WheelView wv_shi;
	private WheelView wv_qu;

	/**
	 * 省选中的下标
	 */
	private int newValue0;
	/**
	 * 市选中的下标
	 */
	private int newValue1;
	/**
	 * 区选中的下标
	 */
	private int newValue2;

	/**
	 * 省资源
	 */
	private List<JsonMap<String, Object>> data_Province;
	/**
	 * 市资源
	 */
	private List<JsonMap<String, Object>> data_City;
	/**
	 * 区资源
	 */
	private List<JsonMap<String, Object>> data_District;

	private String data_sheng[];
	private String[] city;
	private String[] district;

	private String st_province;
	private String st_city;
	private String st_district;
	private float touchDownX; // 手指按下的X坐标
	private float touchUpX; // 手指松开的X坐标
	private int selectid;
	/**
	 * 订单编号
	 */
	private String OrderId;
	/**
	 * 取消订单原因
	 */
	private String ReasonId = "1";
	/**
	 * 发件地址
	 */
	// private String Address;
	/**
	 * 修改发件地址
	 */
	private JsonMap<String, Object> select_data;
	/**
	 * 文字发快递备注
	 */
	private String Remark;
	private String CustomerProvince;
	private String CustomerCity;
	private String CustomerDistrict;
	private String CustomerLocationX;
	private String CustomerLocationY;

	private String CustomerLocationAddress;
	/**
	 * 异地收件地址
	 */
	private String ReceiveLocationAddress;

	/**
	 * ,ServiceType为服务类型1为次日同城 2 为异地快件 3为当日必达 ServiceType为服务类型1为当日同城 2 为普通同城
	 * 3为普通异地 接口传参
	 */
	private String ServiceType;
	/**
	 * 语音base64编码
	 */
	private String avatar;
	/**
	 * 抢单快递员id
	 */
	private String CourierId = "0";

	private Handler handler = new Handler();
	/**
	 * 定时刷新时间
	 */
	private int update;
	/**
	 * 订单已推送人数
	 */
	private String PushOrdersCount = "0";
	/**
	 * 等待时间00:00
	 */
	private String st_waitTime = "00:00";
	/**
	 * 等待接单时间60秒非当日同城发送信息，90秒全部超时，重新发送或取消订单
	 */
	private int waitTime;
	LocationClient myLClient;
	private int selsectProvince;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		sdHelper = new SdcardHelper();
		checkVoice = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		// Address = getIntent().getStringExtra(ExtraKeys.Key_Msg2);
		Log.e("", "checkVoice:" + checkVoice);
		// if (!TextUtils.isEmpty(Address)) {
		// select_data = JsonParseHelper.getJsonMap(Address);
		// CustomerProvince = select_data.getStringNoNull("Province");
		// CustomerCity = select_data.getStringNoNull("City");
		// CustomerDistrict = select_data.getStringNoNull("District");
		// CustomerLocationX = select_data.getStringNoNull("AddressX");
		// CustomerLocationY = select_data.getStringNoNull("AddressY");
		// CustomerLocationAddress = select_data.getStringNoNull("Address");
		// ReceiveLocationAddress = getIndexApplication().getProvince()
		// + getIndexApplication().getCity();
		// } else {
		// CustomerProvince = getIndexApplication().getProvince();
		// CustomerCity = getIndexApplication().getCity();
		// CustomerDistrict = getIndexApplication().getDistrict();
		// CustomerLocationX = getIndexApplication().getLongitude() + "";
		// CustomerLocationY = getIndexApplication().getLatitude() + "";
		// CustomerLocationAddress = getIndexApplication().getLocationAddr();
		// ReceiveLocationAddress = getIndexApplication().getProvince()
		// + getIndexApplication().getCity();
		// }

		JsonMap<String, Object> map = getIndexApplication()
				.getData_sendAddress();
		// {AddressY=31.174965, AddressX=121.396232, AddressLabe=0,
		// District=闵行区, CustomerId=13, Address=上海市闵行区宜山路1718号-3幢,
		// AddressType=发件, Province=上海市, City=上海市}

		if (null != map && !TextUtils.isEmpty(map.getString("Province"))) {
			CustomerProvince = map.getStringNoNull("Province");
			CustomerCity = map.getStringNoNull("City");
			CustomerDistrict = map.getStringNoNull("District");
			CustomerLocationX = map.getStringNoNull("AddressX");
			CustomerLocationY = map.getStringNoNull("AddressY");
			CustomerLocationAddress = map.getStringNoNull("Address");
			ReceiveLocationAddress = getIndexApplication().getProvince()
					+ getIndexApplication().getCity();
		}
		Log.e(TAG, "map:" + map);
		if ("1".equals(checkVoice) || "0".equals(checkVoice)) {
			Remark = getIntent().getStringExtra(ExtraKeys.Key_Msg3);
			getRecordFiles();
			// 语音播放
			if (0 < recordFiles.size()) {
				myPlayFile = new File(myRecAudioDir.getAbsolutePath()
						+ File.separator + recordFiles.get(0));
			}
		}
		myLocate = new Locate(callBack, getApplication());
		myLocate.SetBMapManager(manager);
		myMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		myMenuDrawer.setContentView(R.layout.activity_index2);
		myMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);

		myMenuDrawer.setMenuView(R.layout.activity_index_drawer_left);// 隐藏菜单
		myLClient = new LocationClient(this);

		ServiceType = "2";
		update = 5;
		waitTime = 0;
		// 隐去标题栏（应用程序的名字）
		initView();

		Locate.getInstance().addData_activity(this);
		Log.e("", "checkVoice:" + checkVoice);
		if ("2".equals(checkVoice)) {
			OrderId = getIntent().getStringExtra(ExtraKeys.Key_Msg3);
			tv_bottom_immediately_send
					.setText(getString(R.string.index_tv_cancel_send));
			rl_type.setVisibility(View.GONE);
			handler.post(updateQueryOrderStatus);
			tv_bottom_again_send.setVisibility(View.GONE);
			tv_communicated.setVisibility(View.VISIBLE);
			tv_communicated.setText(String.format(
					getString(R.string.index_tv_communicated), PushOrdersCount,
					st_waitTime));
			rl_type.setVisibility(View.GONE);
			handler.post(updateQueryOrderStatus);
			handler.post(updateWaitTime);
		}
		selectid = 2;
		loadProvince();
		if (null != data_sheng && 0 < data_sheng.length) {
			selsectProvince = data_sheng.length / 2;
			for (int i = 0; i < data_sheng.length; i++) {
				if (getString(R.string.index_province).equals(data_sheng[i])) {
					selsectProvince = i;
				}
			}
			loadCity(data_sheng[selsectProvince]);
			st_province = data_sheng[selsectProvince];
		}
		if (null != city && 0 < city.length) {
			st_city = city[city.length / 2];
			loadDistrict(city[city.length / 2]);
		}
		if (null != district && 0 < district.length) {
			st_district = district[district.length / 2];
		}
		LoadAddr();
	}

	/** 开启播放录音文件的程序 */
	private void openFile(File f) {
		if (null == mMediaPlayer) {
			mMediaPlayer = new MediaPlayer();
		}
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(Uri.fromFile(f).toString());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					// 播放完成一首之后进行下一首
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取录音文件
	 */
	private void getRecordFiles() {

		recordFiles = new ArrayList<String>();
		if (!sdHelper.ExistSDCard()) {
			// Toast.makeText(Index2Activity.this, R.string.nosdcard,
			// Toast.LENGTH_SHORT).show();
			return;
		}
		myRecAudioDir = new File(Confing.voiceCache);
		if (myRecAudioDir.exists()) {
			File files[] = myRecAudioDir.listFiles();
			if (files != null) {

				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().indexOf(".") >= 0) {
						/* 读取.amr文件 */
						String fileS = files[i].getName().substring(
								files[i].getName().indexOf("."));
						if (fileS.toLowerCase().equals(".mp3"))
							recordFiles.add(files[i].getName());

					}
				}
			}

		}
	}

	/**
	 * TODO 地图定位回调
	 */
	LocateCallBack callBack = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province, String City, String District) {
			if (!TextUtils.isEmpty(CustomerLocationX)
					&& !TextUtils.isEmpty(CustomerLocationY)) {
				latitude = Double.parseDouble(CustomerLocationY);
				longitude = Double.parseDouble(CustomerLocationX);
				mMapController = myMapView.getController();
				GeoPoint point = new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6));
				// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
				mMapController.setCenter(point);// 设置地图中心点
				mMapController.setZoom(17);

				if ("0".equals(checkVoice)) {
					createMark(3, 2, 0);
				} else {
					createMark(3, 0, 0);
				}

				// iv_map_locate.setVisibility(0);
				tv_mylocate.setText(String.format(
						getString(R.string.index_tv_my_locate),
						CustomerLocationAddress));
				if (null != myLClient) {
					myLClient.stop();
					myLClient = null;
				}
			}
			if ("3".equals(checkVoice)) {
				if (null == data_mark) {
					data_mark = new ArrayList<JsonMap<String, Object>>();
				}
				data_mark.add(getIndexApplication().getData_courier());
				Log.e(TAG, "getData_courier:"
						+ getIndexApplication().getData_courier());
				CourierId = getIndexApplication().getData_courier().getString(
						"CourierId");
				if (TextUtils.isEmpty(CourierId)) {
					CourierId = "0";
				}
				createMark(3, 0, 0);
			} else {
				loadingToast.show();
				ThreadPoolManager.getInstance().execute(NearByCouriers);
			}

		}
	};
	/**
	 * 获取附近的快递员
	 */
	private Runnable NearByCouriers = new Runnable() {

		@Override
		public void run() {
			// {'LocationX':'121.396470',LocationY:'31.174560'}'Province':'上海市','City':'上海市','District':'闵行区'
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("LocationX", longitude);
			param.put("LocationY", latitude);
			param.put("Province", getIndexApplication().getProvince());
			param.put("City", getIndexApplication().getCity());
			param.put("District", getIndexApplication().getDistrict());
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"NearByCouriers", 0);
		}
	};

	/**
	 * 上传音频文件，成功后删除本地存储的音频文件
	 */
	private Runnable PostFile = new Runnable() {

		@Override
		public void run() {
			if (myPlayFile != null) {
				/* 因将Adapter移除文件名 */
				/* 删除文件 */
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("Token",
							((MyApplication) getApplicationContext())
									.getUserToken());
					jsonObject.put("Filelength", myPlayFile.length());
					jsonObject.put("Filetype", "2");
					jsonObject.put("Mobile",
							((MyApplication) getApplicationContext())
									.getUserPhone());

					getData.doUploadFile(callBack1,
							GetDataConfing.Action_PostFile, myPlayFile,
							jsonObject, 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * 发件人提交订单
	 */
	private Runnable CustomerSubmitOrder = new Runnable() {

		@Override
		public void run() {
			// {CustomerId:'10',CustomerProvince:'上海市',CustomerCity:'上海',CustomerDistrict:'闵行',
			// CustomerLocationX:'121.396270',CustomerLocationY:'31.175080',CustomerLocationAddress:'上海市闵行区宜山路1718号-3幢',
			// ReceiveProvince:'上海',ReceiveCity:'上海',ReceiveDistrict:'徐汇',ReceiveLocationX:'121.396250',
			// ReceiveLocationY:'31.185010',ReceiveLocationAddress:'上海市闵行区宜山路1718号-3幢',
			// ServiceType:'1',OrderVoice:'',Remark:'备注'}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getIndexApplication().getUserId());
			param.put("CustomerProvince", CustomerProvince);
			param.put("CustomerCity", CustomerCity);
			param.put("CustomerDistrict", CustomerDistrict);
			param.put("CustomerLocationX", CustomerLocationX);
			param.put("CustomerLocationY", CustomerLocationY);
			param.put("CustomerLocationAddress", CustomerLocationAddress);
			param.put("ReceiveLocationX", "0.0");
			param.put("ReceiveLocationY", "0.0");
			param.put("ReceiveProvince", CustomerProvince);
			param.put("ReceiveCity", CustomerCity);
			param.put("ReceiveDistrict", CustomerDistrict);
			param.put("ReceiveLocationAddress", ReceiveLocationAddress);
			param.put("ServiceType", ServiceType);
			if (null != myPlayFile) {
				String path = myPlayFile.getPath();
				try {
					FileInputStream fis = new FileInputStream(path);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = fis.read(buf)) != -1) {
						baos.write(buf, 0, len);
					}
					byte[] data1 = baos.toByteArray();
					avatar = new String(Base64.encode(data1, Base64.DEFAULT));
					// avatar = Base64.encode(data);
					fis.close();
					baos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if ("0".equals(checkVoice)) {
				param.put("OrderVoice", avatar);// 语音发件
				param.put("Remark", null);// 文字发件
			} else if ("1".equals(checkVoice)) {
				param.put("OrderVoice", null);// 语音发件
				param.put("Remark", Remark);// 文字发件
			}

			if ("3".equals(checkVoice)) {
				param.put("CourierId", CourierId);
				getData.doPost(callBack1, GetDataConfing.ip, param,
						"CustomerSubmitOrderForOneCourier", 1);
			} else {
				getData.doPost(callBack1, GetDataConfing.ip, param,
						"CustomerSubmitOrder", 1);
			}

		}
	};
	/**
	 * 抢单前的订单取消
	 */
	private Runnable CancelOrderBeforeRob = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':6,CustomerId:2, ReasonId:1}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("CustomerId", getIndexApplication().getUserId());
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CancelOrderBeforeRob", 2);

		}
	};

	/**
	 * 发件人发件后获得该订单数目和状态
	 */
	private Runnable CustomerQueryOrderStatus = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"GetCustomerHasPushAndRob", 113);

		}
	};

	/**
	 * 发件端发送后60S后发给客服
	 */
	private Runnable GetStayPushCouierServiceCourierNeedPushOrder = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"PushCourierService", 114);

		}
	};

	/**
	 * 发件端90S后重新发件
	 */
	private Runnable RetryCourierNeedPushOrder = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"RetryCourierNeedPushOrder", 115);

		}
	};
	/**
	 * 定时刷新订单状态
	 */
	private Runnable updateQueryOrderStatus = new Runnable() {

		@Override
		public void run() {
			ThreadPoolManager.getInstance().execute(CustomerQueryOrderStatus);
			// waitTime += update;
			// 在此处添加执行的代码
			// handler.removeCallbacks(this);
			handler.postDelayed(updateQueryOrderStatus, update * 1000);
		}
	};
	/**
	 * 定时刷新等待时间
	 */
	private Runnable updateWaitTime = new Runnable() {

		@Override
		public void run() {
			// 在此处添加执行的代码
			// handler.removeCallbacks(this);

			//
			if (0 == waitTime) {
				tv_communicated.setVisibility(View.VISIBLE);
			}
			waitTime = waitTime + 1;
			if (60 > waitTime) {
				if (10 <= waitTime) {
					st_waitTime = String.format(
							getString(R.string.index2_wait_time), 0, waitTime);
				} else {
					st_waitTime = String.format(
							getString(R.string.index2_wait_time1), 0, waitTime);
				}

			} else if (90 > waitTime && waitTime >= 60) {
				waitTime = waitTime - 60;
				if (10 <= waitTime) {

					st_waitTime = String.format(
							getString(R.string.index2_wait_time), 1, waitTime);
				} else {
					st_waitTime = String.format(
							getString(R.string.index2_wait_time1), 1, waitTime);
				}
				waitTime = waitTime + 60;
			}
			if (30 == waitTime) {
				if (!"1".equals(ServiceType)) {
					ThreadPoolManager.getInstance().execute(
							GetStayPushCouierServiceCourierNeedPushOrder);
				}

			}
			tv_communicated.setText(String.format(
					getString(R.string.index_tv_communicated), PushOrdersCount,
					st_waitTime));
			if (90 == waitTime) {
				tv_bottom_again_send.setVisibility(View.VISIBLE);
				handler.removeCallbacks(updateQueryOrderStatus);
				handler.removeCallbacks(this);
			} else {
				handler.postDelayed(updateWaitTime, 1000);
			}
		}
	};

	/**
	 * 获取数据回调
	 */
	private ResponseCallBack callBack1 = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			loadingToast.dismiss();
			if (-1 != index) {
				if (113 != what && 114 != what) {
					toast.showToast(error[index]);
				}
				return;
			}

			JsonMap<String, Object> data = JsonParseHelper.getJsonMap(msage);
			Log.d(TAG, String.format(getString(R.string.tojson), what) + data);

			if ("1".equals(data.getStringNoNull("ResultFlag"))) {
				JsonMap<String, Object> data_ = data
						.getJsonMap("MessageContent");
				if (1 == what) {
					if (getData.isOk1(data_)) {
						// TODO 提交订单返回信息
						if ("0".equals(checkVoice)) {
							if (myPlayFile != null) {
								myPlayFile.delete();
							}
						}
						// dbManager.insert_CommonlyUsedAddress(null,
						// select_data,
						// 0, "0");
						// tv_bottom_again_send.setVisibility(View.VISIBLE);
						tv_bottom_immediately_send.setVisibility(View.VISIBLE);
						OrderId = data_.getJsonMap("Result").getStringNoNull(
								"OrderId");
						tv_bottom_immediately_send
								.setText(getString(R.string.index_tv_cancel_send));
						// tv_communicated.setVisibility(View.VISIBLE);
						// tv_communicated.setText(String.format(
						// getString(R.string.index_tv_communicated),
						// PushOrdersCount, st_waitTime));
						rl_type.setVisibility(View.GONE);
						handler.post(updateQueryOrderStatus);
						handler.post(updateWaitTime);
						createMark(3, 0, 1);
					} else {
						Intent intent = new Intent(Index2Activity.this,
								ToastErrorActivity.class);
						intent.putExtra(ExtraKeys.Key_Msg1,
								data_.getJsonMap("ResponseStatus")
										.getStringNoNull("Message"));
						startActivity(intent);
					}
					return;
				}
				if (getData.isOk(data_)) {
					// 附近快递员返回信息
					if (0 == what) {
						data_mark = data_.getList_JsonMap("Result");
						if ("0".equals(checkVoice)) {
							createMark(3, 2, 0);
						} else {
							createMark(3, 0, 0);
						}
					} else if (1 == what) {// 提交订单返回信息

					} else if (2 == what) {// 取消订单返回信息
						tv_communicated.setVisibility(View.GONE);
						handler.removeCallbacks(updateQueryOrderStatus);
						handler.removeCallbacks(updateWaitTime);
						toast.showToast(getString(R.string.index2_toast));
						startActivity(new Intent(Index2Activity.this,
								IndexActivity.class));
						Index2Activity.this.finish();
					} else if (113 == what) {// 发件人查询订单状态
						// \"CourierId\":10}

						PushOrdersCount = data_.getJsonMap("Result")
								.getStringNoNull("PushOrdersCount");
						tv_communicated.setVisibility(View.VISIBLE);
						tv_communicated.setText(String.format(
								getString(R.string.index_tv_communicated),
								PushOrdersCount, st_waitTime));
						if ("1".equals(data_.getJsonMap("Result")
								.getStringNoNull("IsRobSuccess"))) {
							waitTime = 0;
							tv_communicated.setVisibility(View.GONE);
							handler.removeCallbacks(updateQueryOrderStatus);
							handler.removeCallbacks(updateWaitTime);
							Intent intent = new Intent(Index2Activity.this,
									Index3Activity.class);
							intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
							intent.putExtra(ExtraKeys.Key_Msg2,
									CustomerLocationX);
							intent.putExtra(ExtraKeys.Key_Msg3,
									CustomerLocationY);
							intent.putExtra(ExtraKeys.Key_Msg4,
									CustomerLocationAddress);
							intent.putExtra(ExtraKeys.Key_Msg5,
									CustomerProvince);
							intent.putExtra(ExtraKeys.Key_Msg6, CustomerCity);
							intent.putExtra(ExtraKeys.Key_Msg7,
									CustomerDistrict);
							startActivity(intent);
							Index2Activity.this.finish();
						}

					} else if (114 == what) {// 发件端发送后60S后发给客服

					} else if (115 == what) {// 发件端90S后重新发件
						tv_communicated.setVisibility(View.GONE);
						tv_bottom_again_send.setVisibility(View.GONE);
						tv_communicated.setText(String.format(
								getString(R.string.index_tv_communicated),
								PushOrdersCount, st_waitTime));
						waitTime = 0;
						handler.post(updateWaitTime);
						handler.post(updateQueryOrderStatus);
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};

	private OnTouchListener touchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 取得左右滑动时手指按下的X坐标
				touchDownX = event.getX();
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				// 取得左右滑动时手指松开的X坐标
				touchUpX = event.getX();

				// 从左往右，看前一个View
				if (touchUpX - touchDownX > 50) {

					switch (selectid) {
					case 1:
						selectid = selectid + 1;
						updatePicture(1, 0, 0);
						showHideAddr(View.GONE, View.VISIBLE);
						ServiceType = "2";
						break;
					case 2:
						selectid = selectid + 1;
						updatePicture(0, 1, 0);
						ServiceType = "3";
						showHideAddr(View.GONE, View.VISIBLE);
						break;
					case 3:
						updatePicture(0, 0, 1);
						ServiceType = "1";
						showHideAddr(View.VISIBLE, View.GONE);
						break;

					default:
						break;
					}
					// 从右往左，看后一个View
				} else if (touchDownX - touchUpX > 50) {

					switch (selectid) {
					case 1:
						updatePicture(1, 0, 0);
						showHideAddr(View.GONE, View.VISIBLE);
						break;
					case 2:
						selectid = selectid - 1;
						updatePicture(0, 1, 0);
						showHideAddr(View.GONE, View.VISIBLE);
						break;
					case 3:
						selectid = selectid - 1;
						updatePicture(0, 0, 1);
						showHideAddr(View.VISIBLE, View.GONE);
						break;

					default:
						break;
					}
				}

				return true;
			}
			return false;
		}
	};
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int tag;
			switch (v.getId()) {
			case R.id.ImageView01:
				tag = (Integer) v.getTag();
				selectid = tag;
				break;
			case R.id.ImageView02:
				tag = (Integer) v.getTag();
				selectid = tag;
				break;
			case R.id.ImageView03:
				tag = (Integer) v.getTag();
				selectid = tag;

				break;
			case R.id.index_iv_top_handle:
				myMenuDrawer.openMenu();
				break;
			case R.id.index_bottom_rl_bt_1:
				ReceiveLocationAddress = getIndexApplication().getProvince()
						+ getIndexApplication().getCity();
				tag = (Integer) v.getTag();
				selectid = tag;
				updatePicture(1, 0, 0);
				ServiceType = "2";
				showHideAddr(View.GONE, View.VISIBLE);
				break;
			case R.id.index_bottom_rl_bt_2:

				updatePicture(0, 1, 0);
				ServiceType = "3";
				tag = (Integer) v.getTag();
				selectid = tag;
				showHideAddr(View.VISIBLE, View.GONE);
				break;
			case R.id.index_bottom_rl_bt_3:
				tag = (Integer) v.getTag();
				selectid = tag;
				ServiceType = "1";
				ReceiveLocationAddress = getIndexApplication().getProvince()
						+ getIndexApplication().getCity();
				updatePicture(0, 0, 1);
				showHideAddr(View.GONE, View.VISIBLE);
				break;
			case R.id.index2_bt_wv_confirm:// 确定选择地址
				ReceiveLocationAddress = st_province + st_city + st_district;
				showHideAddr(View.GONE, View.VISIBLE);
				break;
			case R.id.index_tv_bottom_immediately_send:
				if (getString(R.string.index_tv_immediately_send).equals(
						tv_bottom_immediately_send.getText().toString())) {
					loadingToast.show();
					ThreadPoolManager.getInstance()
							.execute(CustomerSubmitOrder);
				} else {
					cancelOrder();
				}
				break;

			case R.id.index2_iv_map_locate:
				GeoPoint geoPoint = new GeoPoint((int) (latitude * 1e6),
						(int) (longitude * 1e6));
				myMapView.getController().animateTo(geoPoint);
				break;
			// 左边的
			case R.id.index_left_ll_order:
				startActivity(new Intent(Index2Activity.this,
						UserOrderActivity.class));
				Index2Activity.this.finish();
				waitTime = 0;
				handler.removeCallbacks(updateWaitTime);
				handler.removeCallbacks(updateQueryOrderStatus);
				tv_communicated.setVisibility(View.GONE);
				myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_friend:
				getIndexApplication().addShare(Index2Activity.this);
				// myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_setting:
				startActivity(new Intent(Index2Activity.this,
						UserSettingActivity.class));
				myMenuDrawer.closeMenu();
				waitTime = 0;
				handler.removeCallbacks(updateQueryOrderStatus);
				handler.removeCallbacks(updateWaitTime);
				tv_communicated.setVisibility(View.GONE);
				tv_communicated.setVisibility(View.GONE);
				Index2Activity.this.finish();
				break;
			}

		}
	};

	/**
	 * 加载省市区
	 */
	private void LoadAddr() {
		wv_sheng.setVisibleItems(5);
		wv_sheng.setCyclic(true);//
		wv_sheng.setAdapter(new ArrayWheelAdapter<String>(data_sheng));
		wv_shi.setVisibleItems(5);

		wv_sheng.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Log.d("", "wv_sheng oldValue=" + oldValue + ";newValue="
						+ newValue);
				newValue0 = newValue;
				updateAddr();
				wv_shi.setAdapter(new ArrayWheelAdapter<String>(city));
				wv_shi.setCurrentItem(city.length / 2);
				if (3 >= city.length) {
					wv_qu.setAdapter(new ArrayWheelAdapter<String>(district));
					wv_qu.setCurrentItem(district.length / 2);
				}
			}
		});
		wv_shi.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Log.d("", "wv_shi oldValue=" + oldValue + ";newValue="
						+ newValue);
				newValue1 = newValue;
				updateAddr();
				wv_qu.setAdapter(new ArrayWheelAdapter<String>(district));
				wv_qu.setCurrentItem(district.length / 2);

			}
		});
		wv_qu.setVisibleItems(5);
		wv_qu.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				Log.d("", "wv_qu oldValue=" + oldValue + ";newValue="
						+ newValue);
				newValue2 = newValue;
				updateAddr();
			}
		});
		wv_sheng.setCurrentItem(selsectProvince);
		if (0 == selsectProvince) {
			newValue0 = selsectProvince;
			updateAddr();
			wv_shi.setAdapter(new ArrayWheelAdapter<String>(city));
			wv_shi.setCurrentItem(city.length / 2);
			if (3 >= city.length) {
				wv_qu.setAdapter(new ArrayWheelAdapter<String>(district));
				wv_qu.setCurrentItem(district.length / 2);
			}
		}
	}

	/**
	 * 显示或隐藏地址选择
	 * 
	 * @param arg0
	 * @param arg1
	 */
	private void showHideAddr(int arg0, int arg1) {
		rl_tv_wv.setVisibility(arg0);
		ll_wheelview.setVisibility(arg0);
		tv_bottom_immediately_send.setVisibility(arg1);
	}

	/**
	 * @Title: updatePicture
	 * @Description: TODO(方法的作用：修改展示的图片)
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return void 返回类型
	 * @throws
	 */
	private void updatePicture(int arg0, int arg1, int arg2) {
		ImageView01
				.setImageResource((arg0 == 0) ? R.drawable.index_type_no_select
						: R.drawable.index_type_select);
		ImageView02
				.setImageResource((arg1 == 0) ? R.drawable.index_type_no_select
						: R.drawable.index_type_select);

		ImageView03
				.setImageResource((arg2 == 0) ? R.drawable.index_type_no_select
						: R.drawable.index_type_select);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		// MyApplication.backPressed(Practice_Test.this);
		final int drawerState = myMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			myMenuDrawer.closeMenu();
			return;
		} else {
			waitTime = 0;
			handler.removeCallbacks(updateQueryOrderStatus);
			handler.removeCallbacks(updateWaitTime);
			tv_communicated.setVisibility(View.GONE);
			Index2Activity.this.finish();
		}
		// super.onBackPressed();
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
	 * 创建标识位置
	 */
	public void createMark(int arg0, int arg1, int arg2) {
		Log.d("", "latitude=" + latitude + ";longitude=" + longitude);
		// view.getOverlays().clear();"Result":[{"CourierId":21,"TrackingX":121.396470,"TrackingY":31.174560}]

		Drawable mark = getResources().getDrawable(mark_picture[arg0]);
		OverlayTest itemOverlay = new OverlayTest(mark, myMapView, arg2);
		Drawable mark1 = getResources().getDrawable(mark_picture[arg1]);
		OverlayTest itemOverlay1 = new OverlayTest(mark1, myMapView, arg2);
		if (null != data_mark && 0 < data_mark.size()) {
			for (int i = 0; i < data_mark.size(); i++) {
				double mLat1 = Double.parseDouble(data_mark.get(i)
						.getStringNoNull("TrackingY"));
				double mLon1 = Double.parseDouble(data_mark.get(i)
						.getStringNoNull("TrackingX"));
				GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6),
						(int) (mLon1 * 1E6));
				OverlayItem item = new OverlayItem(p1, "item" + i, "item" + i);

				if ("3".equals(checkVoice)) {
					View view = LayoutInflater.from(Index2Activity.this)
							.inflate(R.layout.item_map_mark, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.item_map_mark_tv_title);
					TextView tv_companyname = (TextView) view
							.findViewById(R.id.item_map_mark_tv_companyname);
					String CourierName = data_mark.get(i).getStringNoNull(
							"CourierName");
					String CompanyName = data_mark.get(i).getStringNoNull(
							"CompanyName");
					tv_name.setVisibility(View.VISIBLE);
					Log.e(TAG, "CourierName:" + CourierName);
					tv_name.setText(CourierName);
					tv_companyname.setText(CompanyName);
					Bitmap bitmap = Locate.convertViewToBitmap(view);
					item.setMarker(new BitmapDrawable(bitmap));
				}
				itemOverlay.addItem(item);
			}
		}
		int a = data_mark == null ? 0 : data_mark.size();
		GeoPoint p1 = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "item" + a, "item" + a);
		itemOverlay1.addItem(item1);
		myMapView.getOverlays().clear();
		myMapView.getOverlays().add(itemOverlay);
		myMapView.getOverlays().add(itemOverlay1);
		myMapView.refresh();
	}

	/**
	 * 自定义图层：ItemizedOverlay
	 * 
	 * 要处理overlay点击事件时需要继承ItemizedOverlay 不处理点击事件时可直接生成ItemizedOverlay.
	 */
	class OverlayTest extends ItemizedOverlay<OverlayItem> {

		private int checkPicture;

		/**
		 * 用MapView构造ItemizedOverlay
		 * 
		 * @param mark
		 * @param mapView
		 */

		public OverlayTest(Drawable mark, MapView mapView, int index) {
			super(mark, mapView);
			this.checkPicture = index;
			// if (1 == checkPicture) {
			// onTap(0);
			// }
		}

		protected boolean onTap(final int index) {
			// 在此处理item点击事件
			if (1 == checkPicture && 0 == index) {
			} else if (0 == checkPicture && 0 == index
					&& "0".equals(checkVoice)) {

				if (myPlayFile != null && myPlayFile.exists()) {
					/* 开启播放的程序 */
					openFile(myPlayFile);
				}

			}

			System.out.println("item onTap: " + index);
			pop = new PopupOverlay(myMapView, new PopupClickListener() {

				public void onClickedPopup(int age0) {
					if (1 == checkPicture) {
					}

					pop.hidePop();
				}

			});
			pop.hidePop();
			return true;
		}
	}

	/**
	 * 加载省资源
	 */
	private void loadProvince() {
		// data_Province = getDbManager.get_ServiceRegion(1, "");
		data_Province = DialogCityDB.getInstance(this).get_ServiceRegion(1, "");
		data_sheng = new String[data_Province.size()];
		for (int i = 0; i < data_sheng.length; i++) {
			data_sheng[i] = data_Province.get(i).getStringNoNull("Province");
		}
	}

	/**
	 * 加载市资源
	 */
	private void loadCity(String province) {
		// data_City = getDbManager.get_ServiceRegion(2, province);
		data_City = DialogCityDB.getInstance(this).get_ServiceRegion(2,
				province);

		city = new String[data_City.size()];
		for (int i = 0; i < city.length; i++) {
			city[i] = data_City.get(i).getStringNoNull("City");
		}
	}

	/**
	 * 加载区资源
	 */
	private void loadDistrict(String city) {
		// data_District = getDbManager.get_ServiceRegion(3, city);
		data_District = DialogCityDB.getInstance(this).get_ServiceRegion(3,
				city);
		district = new String[data_District.size()];
		for (int i = 0; i < district.length; i++) {
			district[i] = data_District.get(i).getStringNoNull("District");
		}
	}

	/**
	 * 修改选中地址
	 * 
	 * @param newValue0
	 * @param newValue1
	 * @param newValue2
	 */
	private void updateAddr() {
		st_province = data_sheng[newValue0];
		loadCity(st_province);
		if (0 < city.length) {
			if (city.length <= newValue1) {
				newValue1 = 0;
			}
			st_city = city[newValue1];
			loadDistrict(st_city);
			if (0 < district.length) {
				if (district.length <= newValue2) {
					newValue2 = 0;
				}
				st_district = district[newValue2];
			}
		}
		tv_wv_end.setText(st_province + " " + st_city + " " + st_district);
		// tv_select_end.setText(st_province + " " + st_city + " " +
		// st_district);
	}

	/**
	 * TODO 等待90秒后重新发送订单
	 * 
	 * @param v
	 */
	public void tv_bottom_again_send(View v) {
		CourierId = "0";
		ThreadPoolManager.getInstance().execute(RetryCourierNeedPushOrder);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		myMapView = (MapView) findViewById(R.id.index2_my_mapv);
		myLocate.SetMap(myMapView, myLClient, 2);
		iv_top_handle = (ImageView) findViewById(R.id.index_iv_top_handle);
		iv_map_locate = (ImageView) findViewById(R.id.index2_iv_map_locate);
		ImageView01 = (ImageView) findViewById(R.id.ImageView01);
		ImageView02 = (ImageView) findViewById(R.id.ImageView02);
		ImageView03 = (ImageView) findViewById(R.id.ImageView03);
		ImageView left_iv_top = (ImageView) findViewById(R.id.index_left_iv_top);
		// if (!TextUtils.isEmpty(getIndexApplication().getUserId())) {
		// left_iv_top.setImageResource(R.drawable.index_login_portrait);
		// } else {
		// left_iv_top.setImageResource(R.drawable.index_no_login_portrait);
		// }
		ImageView01.setTag(11);
		ImageView02.setTag(21);
		ImageView03.setTag(31);

		// ImageView01.setOnTouchListener(touchListener);
		// ImageView02.setOnTouchListener(touchListener);
		// ImageView03.setOnTouchListener(touchListener);

		tv_mylocate = (TextView) findViewById(R.id.index2_tv_mylocate);
		tv_wv_end = (TextView) findViewById(R.id.index2_tv_wv_end);
		tv_communicated = (TextView) findViewById(R.id.index2_tv_communicated);
		tv_bottom_again_send = (TextView) findViewById(R.id.index_tv_bottom_again_send);

		bt_wv_confirm = (Button) findViewById(R.id.index2_bt_wv_confirm);

		tv_bottom_immediately_send = (TextView) findViewById(R.id.index_tv_bottom_immediately_send);

		index2_ll_map = (LinearLayout) findViewById(R.id.index2_ll_map);

		rl_type = (RelativeLayout) findViewById(R.id.index_bottom_rl_type);
		bottom_rl_bt_1 = (RelativeLayout) findViewById(R.id.index_bottom_rl_bt_1);
		bottom_rl_bt_2 = (RelativeLayout) findViewById(R.id.index_bottom_rl_bt_2);
		bottom_rl_bt_3 = (RelativeLayout) findViewById(R.id.index_bottom_rl_bt_3);

		bottom_rl_bt_1.setTag(1);
		bottom_rl_bt_2.setTag(2);
		bottom_rl_bt_3.setTag(3);

		// bottom_rl_bt_1.setOnTouchListener(touchListener);
		// bottom_rl_bt_2.setOnTouchListener(touchListener);
		// bottom_rl_bt_3.setOnTouchListener(touchListener);

		rl_tv_wv = (RelativeLayout) findViewById(R.id.index2_rl_tv_wv);

		ll_wheelview = (LinearLayout) findViewById(R.id.index2_bottom_ll_wheelview);
		ll_order = (LinearLayout) findViewById(R.id.index_left_ll_order);
		ll_friend = (LinearLayout) findViewById(R.id.index_left_ll_friend);
		ll_setting = (LinearLayout) findViewById(R.id.index_left_ll_setting);

		wv_sheng = (WheelView) findViewById(R.id.index2_wv_sheng);
		wv_shi = (WheelView) findViewById(R.id.index2_wv_shi);
		wv_qu = (WheelView) findViewById(R.id.index2_wv_qu);

		bt_wv_confirm.setOnClickListener(onClickListener);

		ll_order.setOnClickListener(onClickListener);
		ll_friend.setOnClickListener(onClickListener);
		ll_setting.setOnClickListener(onClickListener);

		bottom_rl_bt_1.setOnClickListener(onClickListener);
		bottom_rl_bt_2.setOnClickListener(onClickListener);
		bottom_rl_bt_3.setOnClickListener(onClickListener);
		iv_map_locate.setOnClickListener(onClickListener);
		iv_top_handle.setOnClickListener(onClickListener);

		tv_bottom_immediately_send.setOnClickListener(onClickListener);

		// tv_mylocate.setText(String.format(
		// getString(R.string.index_tv_my_locate),
		// getString(R.string.index_tv_locate)));
	}

	/**
	 * @Title: left_ll_balance
	 * @Description: TODO(方法的作用：我的余额)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void left_ll_balance(View v) {
		startActivity(new Intent(Index2Activity.this, MyBalanceActivity.class));
	}

	/**
	 * 上传音频文件
	 */
	private Runnable UpLoadVoice = new Runnable() {

		@Override
		public void run() {
			// {Stream:’’}
			String path = myPlayFile.getPath();
			byte[] bytes = null;
			InputStream is;
			File myfile = new File(path);
			try {
				is = new FileInputStream(path);
				bytes = new byte[(int) myfile.length()];
				int len = 0;
				int curLen = 0;
				while ((len = is.read(bytes)) != -1) {
					curLen += len;
					is.read(bytes);
				}
				is.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// byte[] updata = GpsImagePackage.getPacket(jsonObject.toString(),
			// bytes); // 参数与文件封装成单个数据包

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("Stream", bytes.toString());
			getData.doPost(callBack1, GetDataConfing.ip, param, "UpLoadVoice",
					1);
		}
	};

	/**
	 * @Title: ll_invite
	 * @Description: TODO(方法的作用：邀请朋友)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void ll_invite(View v) {
		Intent intent = new Intent(Index2Activity.this,
				ServiceTermsActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		startActivity(intent);
	}

	/**
	 * 确认取消订单
	 */
	private void cancelOrder() {
		Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.main_toast_tishi));
		builder.setMessage("确认取消订单？");
		builder.setCancelable(false);
		builder.setPositiveButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		builder.setNegativeButton(getString(R.string.Ensure),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						loadingToast.show();
						ThreadPoolManager.getInstance().execute(
								CancelOrderBeforeRob);
					}
				});
		builder.show();
	}
}
