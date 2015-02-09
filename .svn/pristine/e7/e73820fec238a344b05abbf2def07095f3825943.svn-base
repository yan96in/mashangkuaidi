package com.striveen.express.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.striveen.express.Locate;
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.SimpleAsyImgAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.sql.DBManager;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonMapOrListJsonMap2JsonUtil;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.MyLoadMoreListView;
import com.striveen.express.view.ViewInject;

/**
 * 历史地址
 * 
 * @author Fei
 * 
 */
public class HistoryAddressActivity extends MyActivity {

	private final String TAG = "HistoryAddressActivity";
	@ViewInject(id = R.id.history_ddress_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	@ViewInject(id = R.id.history_ddress_bt_top_right, click = "bt_top_right")
	private Button bt_top_right;
	@ViewInject(id = R.id.history_ddress_ll_1)
	private LinearLayout ll_1;
	@ViewInject(id = R.id.history_ddress_et_top_center)
	private EditText et_top_center;
	@ViewInject(id = R.id.history_address_tv_now_place)
	private TextView tv_now_place;

	@ViewInject(id = R.id.history_address_tv_place1)
	private TextView tv_place1;

	@ViewInject(id = R.id.history_address_mlv_data, itemClick = "mlv_data")
	private MyLoadMoreListView mlv_data;
	private SimpleAsyImgAdapter adapter;
	List<JsonMap<String, Object>> place_data;
	List<MKPoiInfo> data_mkPoiInfo;
	MKPoiInfo myMkPoiInfo;
	/**
	 * 判断是从哪个界面跳转 的
	 */
	private String checkIntent;
	private DBManager dbManager;
	/**
	 * 反查
	 */
	MKSearch mkSearch;
	private StringBuilder sb;
	private String Address;
	/**
	 * 选中的地址
	 */
	private JsonMap<String, Object> select_data;
	private boolean isUsedAddress;
	/**
	 * 检验区域是否开通检验区域是否开通
	 */
	private String checkRegion;
	/**
	 * 缓存每次搜索最后闪现的第一条数据
	 */
	private MKPoiInfo poiInfo;
	/**
	 * 0不改变地图，1修改地图
	 */
	private int checkUpdateMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_address);
		checkIntent = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		place_data = new ArrayList<JsonMap<String, Object>>();

		SharedPreferences sp = getSharedPreferences("UsedAddress",
				Context.MODE_APPEND);
		isUsedAddress = sp.getBoolean("isUsedAddress", true);
		loadUsedAddress();
		setAdapter(place_data);
		tv_now_place.setText(Locate.getInstance().getLocationAddress());
		et_top_center.addTextChangedListener(watcher);

		mkSearch = new MKSearch();
		mkSearch.init(new BMapManager(this), new MySearchListener());
		sb = new StringBuilder();
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!TextUtils.isEmpty(et_top_center.getText().toString().trim())) {
				if (null != dbManager) {
					place_data = dbManager.get_CommonlyUsedAddress(
							et_top_center.getText().toString().trim(), "1");
				} else {
					place_data = null;
				}
				Log.e(TAG, "place_data:" + place_data);
				if (null == place_data || 0 == place_data.size()) {
					if (null != place_data) {
						place_data.clear();
					}
					Log.e(TAG, "开始查询符合的地址" + getMyApplication().getCity());
					mkSearch.poiSearchInCity(getMyApplication().getCity(),
							et_top_center.getText().toString().trim());
				} else {
					tv_place1.setVisibility(View.VISIBLE);
					setAdapter(place_data);
				}

			} else {
				if (null != dbManager) {
					place_data = dbManager.get_CommonlyUsedAddress("-1");
				} else {
					place_data = null;
				}
				tv_place1.setVisibility(View.VISIBLE);
				setAdapter(place_data);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	/**
	 * 确定地址
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public void bt_top_right(View view) {

		// TODO 顶部确认按钮
		if (et_top_center.getText().toString().trim().isEmpty()) {
			toast.showToast(getString(R.string.index_tv_address11));
			return;
		}
		Address = et_top_center.getText().toString().trim();
		if (null != select_data) {
			double aa = 0.0;
			double AddressX = 0.0;
			double AddressY = 0.0;
			if (!select_data.getStringNoNull("AddressX").isEmpty()) {
				AddressX = Double.parseDouble(select_data
						.getStringNoNull("AddressX"));
			}
			if (!select_data.getStringNoNull("AddressY").isEmpty()) {
				AddressY = Double.parseDouble(select_data
						.getStringNoNull("AddressY"));
			}
			aa = StringUtil.GetDistance(getMyApplication().getLongitude(),
					getMyApplication().getLatitude(), AddressX, AddressY);
			if (200 < aa) {
				checkUpdateMap = 1;
			} else {
				checkUpdateMap = 0;
			}
			select_data.put("Address", Address);
			addAddress(select_data, Address);
			getMyApplication().setData_sendAddress(select_data);
			if ("02".equals(checkIntent)) {
				intentActivity();
			} else {
				itemFinish();
			}
			Log.e(TAG, "itemFinishselect_data:" + select_data);
			select_data = null;
		} else if (null != myMkPoiInfo) {
			GeoPoint geoPoint = new GeoPoint(myMkPoiInfo.pt.getLatitudeE6(),
					myMkPoiInfo.pt.getLongitudeE6());
			// mkSearch.reverseGeocode(geoPoint);//TODO 根据经纬度查询详细地址，暂时没用
			saveSelect();
		} else if (null == myMkPoiInfo && null != poiInfo) {
			myMkPoiInfo = poiInfo;
			GeoPoint geoPoint = new GeoPoint(myMkPoiInfo.pt.getLatitudeE6(),
					myMkPoiInfo.pt.getLongitudeE6());
			saveSelect();
			// mkSearch.reverseGeocode(geoPoint);//TODO 根据经纬度查询详细地址，暂时没用
		} else {
			toast.showToast(getString(R.string.reservation_mksearch_toast));
		}

	}

	/**
	 * 
	 * @Title: saveSelect
	 * @Description: TODO(方法的作用：保存搜索地址) void 返回类型
	 * @throws
	 */
	private void saveSelect() {
		JsonMap<String, Object> jsonMap = new JsonMap<String, Object>();
		jsonMap.put("CustomerId", getMyApplication().getUserId());
		jsonMap.put("Province", getMyApplication().getProvince());
		jsonMap.put("City", getMyApplication().getCity());
		jsonMap.put("District", getMyApplication().getDistrict());
		jsonMap.put("Address", Address);
		jsonMap.put("AddressX", getMyApplication().getLongitude());
		jsonMap.put("AddressY", getMyApplication().getLatitude());
		jsonMap.put("AddressType", "发件");
		jsonMap.put("AddressLabe", "0");
		select_data = jsonMap;
		getMyApplication().setData_sendAddress(select_data);
		if ("02".equals(checkIntent)) {
			intentActivity();
		} else {
			itemFinish();
		}
		Log.e(TAG, "itemFinishselect_data:" + select_data);
		select_data = null;
	}

	/**
	 * 点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void mlv_data(AdapterView<?> parent, View view, int position, long id) {

		if (null != data_mkPoiInfo && 0 < data_mkPoiInfo.size()) {
			select_data = null;
			myMkPoiInfo = data_mkPoiInfo.get(position);
			GeoPoint geoPoint = new GeoPoint(myMkPoiInfo.pt.getLatitudeE6(),
					myMkPoiInfo.pt.getLongitudeE6());
			// mkSearch.reverseGeocode(geoPoint);//TODO 根据经纬度查询详细地址，暂时没用
			et_top_center.setText(myMkPoiInfo.address);
			setEdit();

		}
		if (place_data.size() > position && place_data.size() > 0) {
			select_data = place_data.get(position);
			et_top_center.setText(select_data.getStringNoNull("Address"));
			setEdit();
		}

	}

	/**
	 * @Title: setEdit
	 * @Description: TODO(方法的作用：设置光标位置)
	 */
	private void setEdit() {
		et_top_center.setText(et_top_center.getText() + "    ");
		CharSequence text = et_top_center.getText();
		// Debug.asserts(text instanceof Spannable);
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length() - 4);
		}
	}

	private void setAdapter(List<JsonMap<String, Object>> data) {
		adapter = new SimpleAsyImgAdapter(this, data,
				R.layout.item_history_address_list, new String[] { "Address" },
				new int[] { R.id.item_history_address_tv }, 0);
		mlv_data.setAdapter(adapter);
	}

	class MySearchListener implements MKSearchListener {

		@Override
		public void onGetPoiResult(MKPoiResult result, int arg1, int arg2) {
			// TODO mkSearch.poiSearchInCity("地区","地址");回调
			Log.e(TAG, "查询结束:result\n" + result);
			if (result == null) {
				return;
			}
			sb.append("共搜索到").append(result.getNumPois()).append("个POI/n");
			List<JsonMap<String, Object>> data_ = new ArrayList<JsonMap<String, Object>>();
			// 遍历当前页返回的POI（默认只返回10个）
			if (null != data_mkPoiInfo) {
				data_mkPoiInfo.clear();
			}
			data_mkPoiInfo = result.getAllPoi();

			if (null != data_mkPoiInfo) {
				poiInfo = data_mkPoiInfo.get(0);
				for (MKPoiInfo poiInfo : data_mkPoiInfo) {
					JsonMap<String, Object> dataJsonMap = new JsonMap<String, Object>();
					dataJsonMap.put("Address", poiInfo.address);
					data_.add(dataJsonMap);
				}
			}
			tv_place1.setVisibility(View.GONE);
			if (TextUtils.isEmpty(et_top_center.getText().toString().trim())) {
				tv_place1.setVisibility(View.VISIBLE);
				return;
			}
			setAdapter(data_);
		}

		@Override
		public void onGetAddrResult(MKAddrInfo result, int arg1) {
			// TODO mkSearch.reverseGeocode(geoPoint);回调
			Log.e("HistoryAddressActivity", ""
					+ result.addressComponents.district);
			Log.e("HistoryAddressActivity", "" + Address + "\ngetLongitudeE6="
					+ myMkPoiInfo.pt.getLongitudeE6() / 1E6);
			JsonMap<String, Object> jsonMap = new JsonMap<String, Object>();
			jsonMap.put("CustomerId", getMyApplication().getUserId());
			jsonMap.put("Province", getMyApplication().getProvince());
			jsonMap.put("City", myMkPoiInfo.city);
			jsonMap.put("District", result.addressComponents.district);
			jsonMap.put("Address", Address);
			jsonMap.put("AddressX", myMkPoiInfo.pt.getLongitudeE6() / 1E6);
			jsonMap.put("AddressY", myMkPoiInfo.pt.getLatitudeE6() / 1E6);
			jsonMap.put("AddressType", "发件");
			jsonMap.put("AddressLabe", "0");
			select_data = jsonMap;
			addAddress(jsonMap, Address);
			myMkPoiInfo = null;
			getMyApplication().setData_sendAddress(select_data);
			if ("02".equals(checkIntent)) {
				intentActivity();
			} else {
				itemFinish();
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

	/**
	 * 用定位位置发件
	 * 
	 * @param v
	 */
	public void location_address(View v) {
		et_top_center.setText(Locate.getInstance().getLocationAddress());
		setEdit();
		Address = Locate.getInstance().getLocationAddress();
		JsonMap<String, Object> jsonMap = new JsonMap<String, Object>();
		jsonMap.put("CustomerId", getMyApplication().getUserId());
		jsonMap.put("Province", getMyApplication().getProvince());
		jsonMap.put("City", getMyApplication().getCity());
		jsonMap.put("District", getMyApplication().getDistrict());
		jsonMap.put("Address", getMyApplication().getLocationAddr());
		jsonMap.put("AddressX", getMyApplication().getLongitude());
		jsonMap.put("AddressY", getMyApplication().getLatitude());
		jsonMap.put("AddressType", "发件");
		jsonMap.put("AddressLabe", "0");
		select_data = jsonMap;
		addAddress(jsonMap, Address);
	}

	/**
	 * 向数据库中添加地址
	 * 
	 * @param jsonMap
	 */
	private void addAddress(JsonMap<String, Object> jsonMap, String address) {
		int inist = 0;
		if (null != dbManager) {
			place_data = dbManager.get_CommonlyUsedAddress("-1");
			if (place_data.size() > 0) {
				for (int i = 0; i < place_data.size(); i++) {
					if (address.equals(place_data.get(i).getStringNoNull(
							"Address"))) {
						inist = 1;
					}
				}
			}
			if (0 == inist) {
				if (300 <= StringUtil.getCompareSize()) {
					dbManager.insert_CommonlyUsedAddress(null, jsonMap, 0, "0");
				}

			}
		}
	}

	/**
	 * 文字发件跳转
	 */
	private void intentActivity() {
		JsonMapOrListJsonMap2JsonUtil<String, Object> util = new JsonMapOrListJsonMap2JsonUtil<String, Object>();
		Intent intent = new Intent(HistoryAddressActivity.this,
				Index2Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		intent.putExtra(ExtraKeys.Key_Msg2, util.map2Json(select_data));
		intent.putExtra(ExtraKeys.Key_Msg3, "");
		startActivity(intent);
		HistoryAddressActivity.this.finish();
	}

	public void itemFinish() {

		Intent intent = getIntent();
		JsonMapOrListJsonMap2JsonUtil<String, Object> util = new JsonMapOrListJsonMap2JsonUtil<String, Object>();
		intent.putExtra(ExtraKeys.Key_Msg1, Address);
		intent.putExtra(ExtraKeys.Key_Msg2, util.map2Json(select_data));
		setResult(RESULT_OK, intent);
		super.finish();
	}

	public void iv_top_left(View v) {
		HistoryAddressActivity.this.finish();
	}

	/**
	 * 加载本地地址信息
	 */
	private void loadUsedAddress() {
		if (StringUtil.judgeExpressDB() == -1) {
			if (300 <= StringUtil.getCompareSize()) {
				dbManager = DBManager.getInstance(HistoryAddressActivity.this);
				dbManager.createCommonlyUsedAddressTable();
				place_data = dbManager.get_CommonlyUsedAddress("-1");
			}
		} else {
			dbManager = DBManager.getInstance(HistoryAddressActivity.this);
			dbManager.createCommonlyUsedAddressTable();
			place_data = dbManager.get_CommonlyUsedAddress("-1");
		}
	}

	/**
	 * 检验区域是否开通
	 */
	// private Runnable GetEffectiveServiceRegion = new Runnable() {
	//
	// @Override
	// public void run() {
	// // {Province:'上海市',City:'上海市',District:'闵行区'}
	// HashMap<String, Object> param = new HashMap<String, Object>();
	// param.put("Province", CustomerProvince);
	// param.put("City", CustomerCity);
	// param.put("District", CustomerDistrict);
	// getData.doPost(callBack, GetDataConfing.ip, param, "Config",
	// "GetEffectiveServiceRegion", 260);
	// }
	// };
	/**
	 * 获取数据回调
	 */
	ResponseCallBack callBack = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
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
					if (260 == what) {
						if (isUsedAddress) {
							// 检验区域是否开通
							checkRegion = data_.getJsonMap("ResponseStatus")
									.getStringNoNull("Message");
							if (getString(R.string.index_toast_region).equals(
									checkRegion)) {
								intentActivity();
							} else {
								toast.showToast(getString(R.string.index_toast_region13));
							}
						}

					}
				}
			}
		}
	};
}
