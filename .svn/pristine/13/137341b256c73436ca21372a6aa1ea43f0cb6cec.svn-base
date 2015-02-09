package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.R;
import com.striveen.express.adapter.DialogAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.runmethodinthread.RunMITQueue;
import com.striveen.express.runmethodinthread.RunMITStaticQueue;
import com.striveen.express.runmethodinthread.RunMITUtil;
import com.striveen.express.runmethodinthread.RunMITUtil.IRunMITCallBack;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.AsyImgView;
import com.striveen.express.view.LayoutProductCommentStartView;
import com.striveen.express.view.ListViewNoScroll;

/**
 * 主界面，百度定位
 * 
 * @author Fei
 * 
 */
public class Index3Activity extends IndexAllActivity {

	private final String TAG = "Index3Activity";

	MenuDrawer myMenuDrawer;

	/**
	 * 百度地图BMapManager
	 */
	public BMapManager manager = null;
	/**
	 * 百度地图MapView
	 */
	private MapView myMapView = null;
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
	 * 定位地址
	 */
	private String locationAddr;
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
	/**
	 * 将当前位置移动到屏幕中央
	 */
	private ImageView iv_map_locate;
	/**
	 * 快递员所属公司
	 */
	private TextView tv_top_logistics;
	/**
	 * 快递员名称
	 */
	private TextView tv_top_name;
	/**
	 * 快递员以服务多少单
	 */
	private TextView tv_order_num;
	/**
	 * 快递员评价星星
	 */
	private LayoutProductCommentStartView lcsv_start;

	/**
	 * 订单类型
	 */
	private TextView tv_type;
	/**
	 * 发件地址
	 */
	private TextView tv_start_addr;
	/**
	 * 发件时间
	 */
	private TextView tv_time1;
	/**
	 * 收件地址
	 */
	private TextView tv_end_addr;
	/**
	 * 抢单时间
	 */
	private TextView tv_time2;
	/**
	 * 订单信息
	 */
	private RelativeLayout index3_rl_all;

	private LinearLayout ll_order;
	private LinearLayout ll_friend;
	private LinearLayout ll_setting;
	private Locate myLocate;
	/**
	 * 录入快递单号 确定
	 */
	Button bt_sumbit;
	/**
	 * 录入快递单号 快递单号
	 */
	EditText et_order_number;
	/**
	 * 录入快递单号 收件人手机号
	 */
	EditText et_phone_number;
	private LinearLayout ll_pressed;
	private ImageView iv_pressed;
	private ImageView iv_pull;
	private LinearLayout ll_normal;
	private ImageView iv_normal;
	/**
	 * 订单id
	 */
	private String OrderId;
	/**
	 * 快递员手机号
	 */
	private String CourierPhone;
	/**
	 * 显示取消订单原因
	 */
	private Dialog dialogLogin;
	/**
	 * 显示录入订单编号
	 */
	private Dialog dialogOrder;

	/**
	 * 订单取消原因id
	 */
	private String ReasonId;
	private List<JsonMap<String, Object>> data_dialog;
	/**
	 * 收件人手机
	 */
	private String ReceivePhone;
	/**
	 * 发件人运单号
	 */
	private String DeliveryNO;
	/**
	 * 判断录入快递单号是否显示
	 */
	private int checkdialog;
	private int CourierRefreshTime;
	Handler handler = new Handler();
	private AsyImgView aiv_avata;
	private DialogAdapter myDialogAdapter;
	private int getDialog;
	/**
	 * 是否录入运单号 1是2否
	 */
	private int checkPressed;
	private RunMITUtil runMITUtil;
	private int pullDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// latitude, longitude;
		pullDown = 0;
		// latitude = Double.parseDouble(getIntent().getStringExtra(
		// ExtraKeys.Key_Msg3));
		// longitude = Double.parseDouble(getIntent().getStringExtra(
		// ExtraKeys.Key_Msg2));
		// locationAddr = getIntent().getStringExtra(ExtraKeys.Key_Msg4);
		// Province = getIntent().getStringExtra(ExtraKeys.Key_Msg5);
		// City = getIntent().getStringExtra(ExtraKeys.Key_Msg6);
		// District = getIntent().getStringExtra(ExtraKeys.Key_Msg7);
		JsonMap<String, Object> map = getIndexApplication()
				.getData_sendAddress();
		Log.e(TAG, "map:" + map);
		// {AddressY=31.175554, AddressX=121.397084, Province=上海市, District=闵行区,
		// CustomerId=13, City=上海市, Address=上海市闵行区莲花路1733}

		if (null != map && !TextUtils.isEmpty(map.getString("Province"))) {
			Province = map.getStringNoNull("Province");
			City = map.getStringNoNull("City");
			District = map.getStringNoNull("District");
			longitude = Double.parseDouble(map.getStringNoNull("AddressX"));
			latitude = Double.parseDouble(map.getStringNoNull("AddressY"));
			locationAddr = map.getStringNoNull("Address");
		}

		myLocate = new Locate(callBack, getApplication());
		myLocate.SetBMapManager(manager);
		myMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		myMenuDrawer.setContentView(R.layout.activity_index3);
		myMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);

		myMenuDrawer.setMenuView(R.layout.activity_index_drawer_left);// 隐藏菜单

		runMITUtil = RunMITUtil.init();
		// 隐去标题栏（应用程序的名字）
		CourierRefreshTime = 60;
		getDialog = -1;
		initView();
		OrderId = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		Locate.getInstance().addData_activity(this);
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerTrackOrderDelivery);

		// handler.post(updateCourier);
		RunMITStaticQueue queue = new RunMITStaticQueue();
		queue.setCls(Thread.class);
		queue.setMethodName("sleep");
		queue.setParms(new Object[] { 500 });
		queue.setCallBack(new IRunMITCallBack() {

			@Override
			public void onRuned(RunMITQueue queue) {
				if (0 == getIndexApplication().getData_dialog().size()) {
					ThreadPoolManager.getInstance().execute(GetAllCancelReason);
				} else {
					data_dialog = getIndexApplication().getData_dialog();
				}
			}
		});
		runMITUtil.runQueue(queue);
		// RunMITStaticQueue queue1 = new RunMITStaticQueue();
		// queue.setCls(Thread.class);
		// queue.setMethodName("sleep");
		// queue.setParms(new Object[] { 500 });
		// queue.setCallBack(new IRunMITCallBack() {
		//
		// @Override
		// public void onRuned(RunMITQueue queue) {
		// }
		// });
		// RunMITUtil.init().runQueue(queue1);
	}

	private LocationClient lClient;

	private void initView() {

		lClient = new LocationClient(this);
		myMapView = (MapView) findViewById(R.id.index3_my_mapv);
		myLocate.SetMap(myMapView, lClient);

		iv_map_locate = (ImageView) findViewById(R.id.index3_iv_map_locate);
		iv_pull = (ImageView) findViewById(R.id.index3_iv_pull);

		tv_top_logistics = (TextView) findViewById(R.id.index3_tv_top_logistics);
		tv_top_name = (TextView) findViewById(R.id.index3_tv_top_name);
		tv_order_num = (TextView) findViewById(R.id.index3_tv_order_num);
		ImageView left_iv_top = (ImageView) findViewById(R.id.index_left_iv_top);
		// if (!TextUtils.isEmpty(getIndexApplication().getUserId())) {
		// left_iv_top.setImageResource(R.drawable.index_login_portrait);
		// } else {
		// left_iv_top.setImageResource(R.drawable.index_no_login_portrait);
		// }

		tv_type = (TextView) findViewById(R.id.index3_tv_type);
		tv_start_addr = (TextView) findViewById(R.id.index3_tv_start_addr);
		tv_time1 = (TextView) findViewById(R.id.index3_tv_time1);
		tv_end_addr = (TextView) findViewById(R.id.index3_tv_end_addr);
		tv_time2 = (TextView) findViewById(R.id.index3_tv_time2);

		aiv_avata = (AsyImgView) findViewById(R.id.index3_aiv_avata);

		ll_order = (LinearLayout) findViewById(R.id.index_left_ll_order);
		ll_friend = (LinearLayout) findViewById(R.id.index_left_ll_friend);
		ll_setting = (LinearLayout) findViewById(R.id.index_left_ll_setting);

		index3_rl_all = (RelativeLayout) findViewById(R.id.index3_rl_all);

		ll_order.setOnClickListener(onClickListener);
		ll_friend.setOnClickListener(onClickListener);
		ll_setting.setOnClickListener(onClickListener);

		lcsv_start = (LayoutProductCommentStartView) findViewById(R.id.index3_lcsv_start3);

	}

	/**
	 * 地图定位回调
	 */
	LocateCallBack callBack = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province1, String City1, String District1) {
			if (null != lClient) {
				lClient.stop();
				lClient = null;
			}
			// latitude = latitude1;
			// longitude = longitude1;
			// locationAddr = str;
			// Province = Province1;
			// City = City1;
			// District = District1;
			// iv_map_locate.setVisibility(0);
			handler.post(updateCourier);
		}
	};

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
			handler.removeCallbacks(updateCourier);
			Index3Activity.this.finish();
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
			myMapView.destroyDrawingCache();
		}
		// toast.showToast("onDestroy");
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
	public void createMark(double arg0, double arg1) {
		Log.d("", "latitude=" + latitude + ";longitude=" + longitude);
		Drawable mark = getResources().getDrawable(R.drawable.index_map_marka);
		OverlayTest itemOverlay = new OverlayTest(mark, myMapView);
		Drawable mark1 = getResources().getDrawable(R.drawable.index_package);

		GeoPoint p = new GeoPoint((int) (arg1 * 1E6), (int) (arg0 * 1E6));
		OverlayItem item = new OverlayItem(p, "item" + 1, "item" + 1);
		itemOverlay.addItem(item);
		OverlayTest itemOverlay1 = new OverlayTest(mark1, myMapView);

		GeoPoint p1 = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "item" + 1, "item" + 1);
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

		/**
		 * 用MapView构造ItemizedOverlay
		 * 
		 * @param mark
		 * @param mapView
		 */

		public OverlayTest(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}

		protected boolean onTap(final int index) {
			return true;
		}

	}

	/**
	 * 发件人跟踪订单投递详情
	 */
	private Runnable CustomerTrackOrderDelivery = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CustomerTrackOrderDelivery", 0);
		}
	};
	/**
	 * 发件人跟踪订单快递员轨迹
	 */
	private Runnable CustomerTrackOrderCourier = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CustomerTrackOrderCourier", 1);
		}
	};
	/**
	 * 发件人确认订单
	 */
	private Runnable CustomerConfirmOrder = new Runnable() {

		@Override
		public void run() {
			// {OrderId:””,ReceivePhone:””,DeliveryNO:””,LocationProvince:””,LocationCity
			// :””,LocationDistrict:””,LocationX:””,LocationY:””,LocationAddress:””}
			// "{'LocationCity':'上海市','DeliveryNO':'123456789','LocationProvince':'上海市',"
			// "'OrderId':'36','LocationAddress':'上海市闵行区宜山路1718号-3幢','ReceivePhone':"
			// "'13162972827','LocationX':121.396171,'LocationY':31.175009,'LocationDistrict':'闵行区'}"

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("ReceivePhone", ReceivePhone);
			param.put("DeliveryNO", DeliveryNO);
			param.put("LocationProvince", Province);
			param.put("LocationCity", City);
			param.put("LocationDistrict", District);
			param.put("LocationX", longitude);
			param.put("LocationY", latitude);
			param.put("LocationAddress", locationAddr);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CustomerConfirmOrder", 2);
		}
	};

	/**
	 * 发件人取消订单
	 */
	private Runnable CustomerCancelOrder = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':6,CustomerId:2,ReasonId:1}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("CustomerId", getIndexApplication().getUserId());
			param.put("ReasonId", ReasonId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CustomerCancelOrder", 3);
		}
	};
	/**
	 * 发件人更新运单号和收件人手机
	 */
	private Runnable UpdateOrderReceiveNo = new Runnable() {

		@Override
		public void run() {
			// {'ReceivePhone':'15317090401',DeliveryNO:'580092599111',OrderId:'1'}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("ReceivePhone", ReceivePhone);
			param.put("DeliveryNO", DeliveryNO);
			param.put("OrderId", OrderId);
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"UpdateOrderReceiveNo", 4);
		}
	};
	/**
	 * 获得取消原因
	 */
	private Runnable GetAllCancelReason = new Runnable() {

		@Override
		public void run() {
			getData.doPost(callBack1, GetDataConfing.ip, null,
					"GetAllCancelReason", 14);
		}
	};
	/**
	 * 定时跟踪订单快递员轨迹
	 */
	private Runnable updateCourier = new Runnable() {

		@Override
		public void run() {
			ThreadPoolManager.getInstance().execute(CustomerTrackOrderCourier);
			// 在此处添加执行的代码
			// handler.removeCallbacks(this);
			handler.postDelayed(updateCourier, CourierRefreshTime * 1000);
		}
	};
	/**
	 * 获取数据回调
	 */
	private ResponseCallBack callBack1 = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			if (1 != what || 14 != what) {
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
						JsonMap<String, Object> data_Result = data_
								.getJsonMap("Result");
						// \"Result\":{\"LogisticsName\":\"顺丰快递\",\"CourierName\":\"蒋师傅\","
						// + "
						// \"ServerCount\":1,\"ServerPoint\":5,\"CourierPhone\":\"13818489978\",
						// "OsList":[{"Status":"普通同城","Address":"上海市闵行区万源路2150弄-16号","DateTime":"2014-7-29 17:42:20"},
						// {"Status":"已接单","Address":"","DateTime":"2014-7-29 17:42:45"}
						// ,{"Status":"发件人确认收单","Address":"上海市闵行区万源路2150弄-16号","DateTime":"2014-7-29 17:43:11"}

						tv_top_logistics.setText(data_Result
								.getStringNoNull("LogisticsName"));
						tv_top_name.setText(data_Result
								.getStringNoNull("CourierName"));
						if (!TextUtils.isEmpty(data_Result
								.getStringNoNull("Avatar"))) {
							aiv_avata.setImgUrl(data_Result
									.getStringNoNull("Avatar"));
						}

						tv_order_num.setText(String.format(
								getString(R.string.user_order_datile_tv_num1),
								data_Result.getStringNoNull("ServerCount")));
						if (!TextUtils.isEmpty(data_Result
								.getStringNoNull("ServerPoint"))) {
							lcsv_start.setStartNum(Integer.parseInt(data_Result
									.getStringNoNull("ServerPoint")));
						}
						CourierPhone = data_Result
								.getStringNoNull("CourierPhone");

						List<JsonMap<String, Object>> data_lList = data_Result
								.getList_JsonMap("OsList");
						getIndexApplication().setData_orderDetaile(data_lList);
						if (null != data_lList && 0 < data_lList.size()) {
							tv_type.setText(data_lList.get(0).getStringNoNull(
									"Status"));
							tv_start_addr.setText(data_lList.get(0)
									.getStringNoNull("Address"));
							tv_time1.setText(data_lList.get(0).getStringNoNull(
									"DateTime"));
							tv_end_addr.setText(data_lList.get(1)
									.getStringNoNull("Address"));
							tv_time2.setText(data_lList.get(1).getStringNoNull(
									"DateTime"));
						}
					} else if (1 == what) {// 发件人跟踪订单快递员轨迹
						if (0 < data_.getList_JsonMap("Result").size()) {
							JsonMap<String, Object> map = data_
									.getList_JsonMap("Result").get(0);
							createMark(map.getDouble("LocationX"),
									map.getDouble("LocationY"));
						}

					} else if (2 == what) {// 发件人确认订单
						Intent intent = new Intent(Index3Activity.this,
								UserOrderDatileActivity.class);
						intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
						startActivity(intent);
						Index3Activity.this.finish();
					} else if (3 == what) {// 发件人取消订单
						handler.removeCallbacks(updateCourier);
						toast.showToast(data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message"));
						dialogLogin.cancel();
						startActivity(new Intent(Index3Activity.this,
								IndexActivity.class));
						Index3Activity.this.finish();
					} else if (4 == what) {// 发件人更新运单号和收件人手机
						dialogOrder.cancel();
						checkdialog = 1;
						loadingToast.show();
						ThreadPoolManager.getInstance().execute(
								CustomerConfirmOrder);
					} else if (14 == what) {// 获得取消原因
						data_dialog = data_.getList_JsonMap("Result");
						ReasonId = data_dialog.get(0).getStringNoNull(
								"ReasonId");
						getIndexApplication().setData_dialog(data_dialog);
						if (1 == getDialog) {
							showCancleDialog();
							getDialog = -1;
						}
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};

	/**
	 * 显示取消订单原因
	 */
	public void showCancleDialog() {

		dialogLogin = new Dialog(this, R.style.loginDialogTheme);
		dialogLogin.setCanceledOnTouchOutside(false);
		dialogLogin.setCancelable(true);
		LayoutInflater layout = LayoutInflater.from(this);
		View view = layout.inflate(R.layout.item_index3_cancle_dialog, null);
		ListViewNoScroll lv_context = (ListViewNoScroll) view
				.findViewById(R.id.index3_lv_context);
		ImageView top_phone = (ImageView) view
				.findViewById(R.id.index3_iv_top_phone);
		TextView tv_bottom = (TextView) view
				.findViewById(R.id.index3_tv_bottom);
		top_phone.setOnClickListener(onClickListener);
		tv_bottom.setOnClickListener(onClickListener);
		lv_context.setOnItemClickListener(itemListener);
		dialogLogin.setContentView(view);
		myDialogAdapter = new DialogAdapter(this, data_dialog);
		lv_context.setAdapter(myDialogAdapter);
		dialogLogin.show();
	}

	/**
	 * 显示录入订单编号
	 */
	public void showOrderDialog() {
		checkPressed = 1;
		dialogOrder = new Dialog(this, R.style.loginDialogTheme);
		dialogOrder.setCanceledOnTouchOutside(false);
		dialogOrder.setCancelable(true);
		LayoutInflater layout = LayoutInflater.from(this);
		View view = layout.inflate(R.layout.item_index_receipt_dialog, null);
		bt_sumbit = (Button) view
				.findViewById(R.id.item_receipt_dialog_bt_sumbit);
		et_order_number = (EditText) view
				.findViewById(R.id.item_receipt_dialog_et_order_number);
		et_phone_number = (EditText) view
				.findViewById(R.id.item_receipt_dialog_et_phone_number);
		ll_pressed = (LinearLayout) view
				.findViewById(R.id.item_receipt_dialog_ll_pressed);
		iv_pressed = (ImageView) view
				.findViewById(R.id.item_receipt_dialog_iv_pressed);
		ll_normal = (LinearLayout) view
				.findViewById(R.id.item_receipt_dialog_ll_normal);
		iv_normal = (ImageView) view
				.findViewById(R.id.item_receipt_dialog_iv_normal);

		bt_sumbit.setOnClickListener(onClickListener);
		ll_pressed.setOnClickListener(onClickListener);
		ll_normal.setOnClickListener(onClickListener);
		dialogOrder.setContentView(view);
		dialogOrder.show();
	}

	/**
	 * 顶部取消订单
	 * 
	 * @param v
	 */
	public void tv_top_right(View v) {
		if (null == data_dialog || 0 == data_dialog.size()) {
			getDialog = 1;
			ThreadPoolManager.getInstance().execute(GetAllCancelReason);
		} else {
			ReasonId = data_dialog.get(0).getStringNoNull("ReasonId");
			showCancleDialog();
		}
	}

	/**
	 * 给快递员打电话
	 * 
	 * @param v
	 */
	public void iv_top_phone(View v) {
		if (!TextUtils.isEmpty(CourierPhone)) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ CourierPhone));
			Index3Activity.this.startActivity(intent);
		}
	}

	/**
	 * TODO 底部确认发件
	 */
	public void tv_bottom_immediately_send(View v) {
		handler.removeCallbacks(updateCourier);
		// Intent intent = new Intent(Index3Activity.this,
		// UserOrderDatileActivity.class);
		// intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
		// startActivity(intent);
		// Index3Activity.this.finish();
		// 修改前的确认发出
		// if (0 == checkdialog) {
		// showOrderDialog();
		// } else if (1 == checkdialog) {
		// loadingToast.show();
		// ThreadPoolManager.getInstance().execute(CustomerConfirmOrder);
		// }
		// 修改之后的
		Intent intent = new Intent(Index3Activity.this,
				PayOrEvaluateActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
		intent.putExtra(ExtraKeys.Key_Msg2, "0");
		startActivity(intent);
		Index3Activity.this.finish();
	}

	/**
	 * @Title: left_ll_balance
	 * @Description: TODO(方法的作用：我的余额)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void left_ll_balance(View v) {
		startActivity(new Intent(Index3Activity.this, MyBalanceActivity.class));
	}

	/**
	 * 将当前位置移动到屏幕中央
	 */
	public void iv_map_locate(View v) {
		GeoPoint geoPoint = new GeoPoint((int) (latitude * 1e6),
				(int) (longitude * 1e6));
		myMapView.getController().animateTo(geoPoint);
	}

	/**
	 * 手柄点击
	 * 
	 */
	public void iv_top_handle(View v) {
		myMenuDrawer.openMenu();
	}

	public void pull_down(View v) {
		if (0 == pullDown % 2) {
			index3_rl_all.setVisibility(View.VISIBLE);
			iv_pull.setImageResource(R.drawable.index3_pull_up);
		} else {
			index3_rl_all.setVisibility(View.GONE);
			iv_pull.setImageResource(R.drawable.index3_pull_down);
		}
		pullDown++;

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 左边的
			case R.id.index_left_ll_order:
				handler.removeCallbacks(updateCourier);
				startActivity(new Intent(Index3Activity.this,
						UserOrderActivity.class));
				myMenuDrawer.closeMenu();
				Index3Activity.this.finish();
				break;
			case R.id.index_left_ll_friend:
				getIndexApplication().addShare(Index3Activity.this);
				// myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_setting:
				handler.removeCallbacks(updateCourier);
				startActivity(new Intent(Index3Activity.this,
						UserSettingActivity.class));
				myMenuDrawer.closeMenu();
				Index3Activity.this.finish();
				break;
			case R.id.index3_iv_top_phone:// 取消订单关闭dialog
				dialogLogin.cancel();
				break;
			case R.id.index3_tv_bottom:// 确定选择的原因
				cancelOrder();
				break;
			case R.id.item_receipt_dialog_bt_sumbit:// 确定提交运单号和收件人手机
				ReceivePhone = et_phone_number.getText().toString();
				DeliveryNO = et_order_number.getText().toString();
				if (1 == checkPressed) {
					if (TextUtils.isEmpty(DeliveryNO)) {
						toast.showToast(getString(R.string.user_order_datile_toast_deliverynO));
						return;
					}
					if (!StringUtil.isWaybillNumber(DeliveryNO)) {
						toast.showToast(getString(R.string.index3_toast_waybill_number));
						return;
					}
					if (TextUtils.isEmpty(ReceivePhone)) {
						toast.showToast(getString(R.string.user_order_datile_toast_phone));
						return;
					}
					loadingToast.show();
					ThreadPoolManager.getInstance().execute(
							UpdateOrderReceiveNo);
				} else {
					dialogOrder.cancel();
					ReceivePhone = "";
					DeliveryNO = "";
					loadingToast.show();
					ThreadPoolManager.getInstance().execute(
							CustomerConfirmOrder);
				}
				break;

			case R.id.item_receipt_dialog_ll_pressed:
				checkPressed = 1;
				iv_normal.setImageResource(R.drawable.radio_normal);
				iv_pressed.setImageResource(R.drawable.radio_pressed);
				break;

			case R.id.item_receipt_dialog_ll_normal:
				checkPressed = 2;
				iv_normal.setImageResource(R.drawable.radio_pressed);
				iv_pressed.setImageResource(R.drawable.radio_normal);
				break;

			}

		}
	};
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			ReasonId = data_dialog.get(position).getStringNoNull("ReasonId");
			myDialogAdapter.setSelectedPosition(position);
			myDialogAdapter.notifyDataSetChanged();
		}
	};
	/**
	 * @Title: ll_invite
	 * @Description: TODO(方法的作用：邀请朋友)
	 * 		@param v 
	 * void    返回类型
	 * @throws
	 */
	public void ll_invite(View v) {
		Intent intent = new Intent(Index3Activity.this,
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
								CustomerCancelOrder);
					}
				});
		builder.show();
	}

}
