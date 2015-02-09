package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.ToastErrorActivity;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.sql.DialogCityDB;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.ViewInject;
import com.striveen.express.wheel.widget.ArrayWheelAdapter;
import com.striveen.express.wheel.widget.OnWheelChangedListener;
import com.striveen.express.wheel.widget.WheelView;

/**
 * 预约
 * 
 * @author Fei
 * 
 */
public class ReservationActivity extends MyActivity {

	private final String TAG = "ReservationActivity";
	@ViewInject(id = R.id.reservation_ll_select_time, click = "ll_select_time")
	private LinearLayout ll_select_time;
	@ViewInject(id = R.id.reservation_ll_select_starting, click = "ll_select_starting")
	private LinearLayout ll_select_starting;
	@ViewInject(id = R.id.reservation_ll_select_end, click = "ll_select_end")
	private LinearLayout ll_select_end;
	/**
	 * 选择时间
	 */
	@ViewInject(id = R.id.reservation_tv_select_time)
	private TextView tv_select_time;

	/**
	 * 哪儿寄
	 */
	@ViewInject(id = R.id.reservation_tv_select_starting)
	private TextView tv_select_starting;
	/**
	 * 寄到哪儿
	 */
	@ViewInject(id = R.id.reservation_tv_select_end)
	private TextView tv_select_end;
	/**
	 * 地区选择布局
	 */
	@ViewInject(id = R.id.reservation_bottom_ll_wheelview)
	private LinearLayout ll_wheelview;
	/**
	 * 时间选择布局
	 */
	@ViewInject(id = R.id.reservation_bottom_ll_wheelview_time)
	private LinearLayout ll_wheelview_time;
	/**
	 * 发送订单布局
	 */
	@ViewInject(id = R.id.reservation_bottom_rl_send)
	private RelativeLayout rl_send;
	/**
	 * 发送订单
	 */
	@ViewInject(id = R.id.reservation_bt_send, click = "bt_send")
	private Button bt_send;
	@ViewInject(id = R.id.reservation_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	/**
	 * 区域确定
	 */
	@ViewInject(id = R.id.reservation_bt_wv_confirm, click = "bt_wv_confirm")
	private Button bt_wv_confirm;
	/**
	 * 时间确定
	 */
	@ViewInject(id = R.id.reservation_bt_wv_confirm_time, click = "bt_wv_confirm_time")
	private Button bt_wv_confirm_time;
	/**
	 * 时间取消
	 */
	@ViewInject(id = R.id.reservation_bt_cancel_time, click = "bt_cancel_time")
	private Button bt_cancel_time;
	@ViewInject(id = R.id.reservation_tv_wv_end)
	private TextView tv_wv_end;

	/**
	 * 从哪儿寄
	 */
	@ViewInject(id = R.id.reservation_et_select_starting)
	private EditText et_select_starting;
	/**
	 * 寄到哪儿
	 */
	@ViewInject(id = R.id.reservation_et_select_end)
	private EditText et_select_end;

	@ViewInject(id = R.id.reservation_wv_sheng)
	private WheelView wv_sheng;
	@ViewInject(id = R.id.reservation_wv_shi)
	private WheelView wv_shi;
	@ViewInject(id = R.id.reservation_wv_qu)
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

	@ViewInject(id = R.id.reservation_wv_data)
	private WheelView wv_data;
	@ViewInject(id = R.id.reservation_wv_time)
	private WheelView wv_time;
	@ViewInject(id = R.id.reservation_wv_minute)
	private WheelView wv_minute;

	private String[] data_dd = { "今天", "明天", "后天" };
	private String[] time;
	private String[] minute = { "00分", "15分", "30分", "45分" };

	private String st_data;
	private String st_time;
	private String st_minute;

	/**
	 * 日期选中的下标
	 */
	private int newValue_data;
	/**
	 * 小时选中的下标
	 */
	private int newValue_time;
	/**
	 * 分钟选中的下标
	 */
	private int newValue_minute;
	/**
	 * 时间长度
	 */
	private int index_time;
	/**
	 * 当前小时
	 */
	private int get_HH_time;

	/**
	 * 反查
	 */
	MKSearch mkSearch;
	/**
	 * 反查地址
	 */
	private String locationAddr_Receive;
	/**
	 * 反查latitude纬度
	 */
	private double latitude_Receive = 0.1;
	/**
	 * 反查longitude经度
	 */
	private double longitude_Receive = 0.1;
	/**
	 * 当前日期
	 */
	private String now_data;
	/**
	 * 更改发件地址
	 */
	private String Address;
	private String string_data;
	/**
	 * 修改发件地址
	 */
	private JsonMap<String, Object> select_data;
	private String CustomerProvince;
	private String CustomerCity;
	private String CustomerDistrict;
	private String CustomerLocationX;
	private String CustomerLocationY;
	private String CustomerLocationAddress;
	private int selsectProvince;
	/**
	 * 检验区域是否开通检验区域是否开通
	 */
	private String checkRegion;
	private String today;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		// initActivityTitle(getString(R.string.reservation_top_title), true);
		mkSearch = new MKSearch();
		this.data_sheng = getMyApplication().getData_sheng();
		CustomerLocationAddress = getMyApplication().getLocationAddr();
		tv_select_starting.setText(CustomerLocationAddress);
		CustomerProvince = getMyApplication().getProvince();
		CustomerCity = getMyApplication().getCity();
		CustomerDistrict = getMyApplication().getDistrict();
		CustomerLocationX = getMyApplication().getLongitude() + "";
		CustomerLocationY = getMyApplication().getLatitude() + "";
		now_data = StringUtil.getData_yyyy_MM_dd();
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
		// tv_select_end.setText(st_province + st_city + st_district);
		newValue_data = 0;
		updateTimes();
		tv_select_time.setText(getString(R.string.reservation_tv_time));
		initArea();
		initTime();
		// DialogCityDB.getInstance(this).insert_ServiceRegion(lisy, 0);
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
	 * 加载区域
	 */
	private void initArea() {
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
	 * 加载时间
	 */
	private void initTime() {
		wv_data.setVisibleItems(5);
		wv_data.setCyclic(true);//
		wv_data.setAdapter(new ArrayWheelAdapter<String>(data_dd));
		wv_time.setVisibleItems(5);

		wv_data.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				newValue_data = newValue;
				updateTimes();
				wv_time.setAdapter(new ArrayWheelAdapter<String>(time));
				wv_time.setCurrentItem(2);
			}
		});
		wv_time.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				newValue_time = newValue;
				updateTimes();
				wv_minute.setAdapter(new ArrayWheelAdapter<String>(minute));
				wv_minute.setCurrentItem(2);

			}
		});
		wv_minute.setVisibleItems(5);
		wv_minute.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				newValue_minute = newValue;
				updateTimes();
			}
		});
		wv_data.setCurrentItem(1);
	}

	/**
	 * 修改选中时间
	 * 
	 * @param newValue0
	 * @param newValue1
	 * @param newValue2
	 */
	private void updateTimes() {
		if (0 == newValue_data) {
			get_HH_time = StringUtil.getData_HH();
			index_time = 24 - get_HH_time;
			get_HH_time = get_HH_time + 1;
		} else {
			get_HH_time = 0;
			index_time = 25;
		}
		st_data = data_dd[newValue_data];
		time = new String[index_time];
		for (int i = 0; i < time.length; i++) {
			if (0 == get_HH_time) {
				time[i] = i < 10 ? "0" + i + "点" : i + "点";
			} else {
				get_HH_time = get_HH_time + i;
				time[i] = get_HH_time < 10 ? "0" + "点" + get_HH_time
						: get_HH_time + "点";
				get_HH_time = get_HH_time - i;
			}

		}
		if (0 < time.length) {
			if (time.length <= newValue_time) {
				newValue_time = 0;
			}
			st_time = time[newValue_time];
			if (0 < minute.length) {
				if (minute.length <= newValue_minute) {
					newValue_minute = 0;
				}
				st_minute = minute[newValue_minute];
			}
		}
		// tv_select_time.setText(st_data + " " + st_time + " " + st_minute);
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

	// {CustomerId:'10',CustomerProvince:'上海市',CustomerCity:'上海',CustomerDistrict:'闵行',
	// CustomerLocationX:'121.396270',CustomerLocationY:'31.175080',CustomerLocationAddress:'上海市闵行区宜山路1718号-3幢',
	// ReceiveProvince:'上海',ReceiveCity:'上海',ReceiveDistrict:'徐汇',ReceiveLocationX:'121.396250',
	// ReceiveLocationY:'31.185010',ReceiveLocationAddress:'上海市闵行区宜山路1718号-3幢',
	// AppointTime:'2014-06-30 16:54:31.000'}

	/**
	 * 发件人提交预约订单
	 */
	private Runnable CustomerSubmitPreOrder = new Runnable() {

		@Override
		public void run() {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("CustomerProvince", CustomerProvince);
			param.put("CustomerCity", CustomerCity);
			param.put("CustomerDistrict", CustomerDistrict);
			param.put("CustomerLocationX", CustomerLocationX);
			param.put("CustomerLocationY", CustomerLocationY);
			param.put("CustomerLocationAddress", CustomerLocationAddress);
			param.put("ReceiveProvince", st_province);
			param.put("ReceiveCity", st_city);
			param.put("ReceiveDistrict", st_district);
			param.put("ReceiveLocationX", longitude_Receive);
			param.put("ReceiveLocationY", latitude_Receive);
			param.put("ReceiveLocationAddress", locationAddr_Receive);
			param.put(
					"AppointTime",
					today + " " + st_time.substring(0, st_time.length() - 1)
							+ ":"
							+ st_minute.substring(0, st_minute.length() - 1));
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerSubmitPreOrder", 0);
		}
	};

	/**
	 * 获取数据回调
	 */
	ResponseCallBack callBack = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			loadingToast.dismiss();
			if (-1 != index) {
				toast.showToast(error[index]);
				return;
			}

			JsonMap<String, Object> data = JsonParseHelper.getJsonMap(msage);
			Log.d(TAG, String.format(getString(R.string.tojson), what) + data);
			if ("1".equals(data.getStringNoNull("ResultFlag"))) {
				JsonMap<String, Object> data_ = data
						.getJsonMap("MessageContent");
				if (getData.isOk1(data_)) {
					if (0 == what) {
						Intent intent = new Intent(ReservationActivity.this,
								Index2Activity.class);
						// {\"Result\":{\"OrderId\":202,\"NbList\":[{\"CourierId\":43,\"TrackingX\":121.396160,
						// \"TrackingY\":31.175050},{\"CourierId\":36,\"TrackingX\":121.396160,"
						// + "\"TrackingY\":31.175250}]
						intent.putExtra(ExtraKeys.Key_Msg1, "2");
						intent.putExtra(ExtraKeys.Key_Msg2, string_data);
						intent.putExtra(
								ExtraKeys.Key_Msg3,
								data_.getJsonMap("Result").getStringNoNull(
										"OrderId"));
						startActivity(intent);
						ReservationActivity.this.finish();
					} else if (11 == what) {
						// 检验区域是否开通
						checkRegion = data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message");
					}
				} else {
					Intent intent = new Intent(ReservationActivity.this,
							ToastErrorActivity.class);
					intent.putExtra(
							ExtraKeys.Key_Msg1,
							data_.getJsonMap("ResponseStatus").getStringNoNull(
									"Message"));
					startActivity(intent);
				}
			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};

	public void iv_top_left(View view) {
		ReservationActivity.this.finish();
	}

	/**
	 * 时间
	 * 
	 * @param view
	 */
	public void ll_select_time(View view) {
		rl_send.setVisibility(View.GONE);
		ll_wheelview_time.setVisibility(View.VISIBLE);
		ll_wheelview.setVisibility(View.GONE);
	}

	/**
	 * 时间确定
	 */
	public void bt_wv_confirm_time(View v) {
		tv_select_time.setText(st_data + " " + st_time + " " + st_minute);
		if ("今天".equals(st_data)) {
			today = StringUtil.getTodayData();
		} else if ("明天".equals(st_data)) {
			today = StringUtil.getTomoData();
		} else if ("后天".equals(st_data)) {
			today = StringUtil.getTheDayData();
		}
		ll_wheelview_time.setVisibility(View.GONE);
		ll_wheelview.setVisibility(View.GONE);
		rl_send.setVisibility(View.VISIBLE);
	}

	/**
	 * 时间取消
	 */
	public void bt_cancel_time(View v) {
		ll_wheelview.setVisibility(View.GONE);
		ll_wheelview_time.setVisibility(View.GONE);
		rl_send.setVisibility(View.VISIBLE);
	}

	/**
	 * 从哪儿寄
	 * 
	 * @param view
	 */
	public void ll_select_starting(View v) {
		Intent intent = new Intent(ReservationActivity.this,
				HistoryAddressActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, 3);
		startActivityForResult(intent, 11);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (11 == requestCode) {
				Address = data.getStringExtra(ExtraKeys.Key_Msg1);
				string_data = data.getStringExtra(ExtraKeys.Key_Msg2);
				select_data = JsonParseHelper.getJsonMap(string_data);
				CustomerProvince = select_data.getStringNoNull("Province");
				CustomerCity = select_data.getStringNoNull("City");
				CustomerDistrict = select_data.getStringNoNull("District");
				CustomerLocationX = select_data.getStringNoNull("AddressX");
				CustomerLocationY = select_data.getStringNoNull("AddressY");
				CustomerLocationAddress = select_data
						.getStringNoNull("Address");
				tv_select_starting.setText(CustomerLocationAddress);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 寄到哪儿
	 * 
	 * @param view
	 */
	public void ll_select_end(View v) {
		rl_send.setVisibility(View.GONE);
		ll_wheelview_time.setVisibility(View.GONE);
		ll_wheelview.setVisibility(View.VISIBLE);
	}

	/**
	 * 发送
	 * 
	 * @param view
	 */
	public void bt_send(View v) {
		if (TextUtils.isEmpty(tv_select_starting.getText().toString().trim())) {
			toast.showToast(getString(R.string.sender_address_toast_start));
			return;
		}
		// if (TextUtils.isEmpty(tv_select_end.getText().toString().trim())) {
		// toast.showToast(getString(R.string.sender_address_toast_end));
		// return;
		// }
		if (getString(R.string.reservation_tv_time).equals(
				tv_select_time.getText().toString())) {
			toast.showToast(getString(R.string.sender_address_toast_time));
			return;
		}
		if (getString(R.string.reservation_tv_end).equals(
				tv_select_end.getText().toString())) {
			toast.showToast(getString(R.string.sender_address_toast_end));
			return;
		}
		int houre = Integer
				.parseInt(st_time.substring(0, st_time.length() - 1));
		int men = Integer.parseInt(st_minute.substring(0,
				st_minute.length() - 1));
		int minutes = StringUtil.getData_mm() + StringUtil.getData_HH() * 60;

		int minutes1 = men + houre * 60;
		if ("今天".equals(st_data)) {
			if (20 < minutes1 - minutes) {
				loadingToast.show();
				ThreadPoolManager.getInstance().execute(CustomerSubmitPreOrder);
			} else {
				toast.showToast("重新选择预约时间");
			}
		} else {
			loadingToast.show();
			ThreadPoolManager.getInstance().execute(CustomerSubmitPreOrder);
		}
	}

	/**
	 * 确定选择地址
	 * 
	 * @param view
	 */
	public void bt_wv_confirm(View v) {
		locationAddr_Receive = st_province + st_city + st_district;
		// mkSearch.init(new BMapManager(this), new MySearchListener());
		// mkSearch.geocode(st_district, st_city);
		tv_select_end.setText(tv_wv_end.getText().toString());
		ll_wheelview.setVisibility(View.GONE);
		rl_send.setVisibility(View.VISIBLE);
	}

	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// 返回地址信息搜索结果。 参数： arg0 - 搜索结果 arg1 - 错误号，0表示结果正确，result中有
			// 相关结果信息；100表示结果正确，无相关地址信息
			if (arg0 == null) {
				toast.showToast(getString(com.striveen.express.R.string.reservation_mksearch_toast));
			} else {
				// 获得搜索地址的经纬度
				latitude_Receive = arg0.geoPt.getLatitudeE6() / 1E6;
				longitude_Receive = arg0.geoPt.getLongitudeE6() / 1E6;
				Log.d("", "latitude_Receive=" + latitude_Receive
						+ "\nlongitude_Receive=" + longitude_Receive);
			}
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}

	}

}
