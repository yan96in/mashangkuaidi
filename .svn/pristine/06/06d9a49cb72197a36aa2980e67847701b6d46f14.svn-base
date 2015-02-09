package com.striveen.express.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.view.MyLoadMoreListView;
import com.striveen.express.view.ViewInject;

/**
 * 输入发送地址
 * 
 * @author Fei
 * 
 */
public class SenderAddressActivity extends MyActivity {

	private final String TAG = "SenderAddressActivity";

	@ViewInject(id = R.id.sender_ddress_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	@ViewInject(id = R.id.sender_ddress_bt_top_right, click = "bt_top_right")
	private Button bt_top_right;
	@ViewInject(id = R.id.sender_ddress_ll_1, click = "ll_1")
	private LinearLayout ll_1;
	@ViewInject(id = R.id.sender_ddress_ll_2, click = "ll_2")
	private LinearLayout ll_2;
	@ViewInject(id = R.id.sender_ddress_et_top_center)
	private EditText et_top_center;

	@ViewInject(id = R.id.common_address_mlv_addresss, itemClick = "mlv_addresss")
	private MyLoadMoreListView mlv_addresss;
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
	private StringBuilder sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender_address);
		// initActivityTitle(getString(R.string.action_settings), true);
		mkSearch = new MKSearch();
		mkSearch.init(new BMapManager(this), new MySearchListener());
		et_top_center.addTextChangedListener(watcher);
		sb = new StringBuilder();
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mkSearch.poiSearchInCity("上海市", et_top_center.getText().toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public void mlv_addresss(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
	}

	/**
	 * 
	 * @param view
	 */
	public void bt_top_right(View view) {
		if (TextUtils.isEmpty(et_top_center.getText().toString().trim())) {
			toast.showToast(getString(R.string.sender_address_toast));
			return;
		}
		Intent intent = new Intent(SenderAddressActivity.this,
				Index2Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		startActivity(intent);
	}

	public void ll_1(View view) {
		Intent intent = new Intent(SenderAddressActivity.this,
				Index2Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		startActivity(intent);
	}

	public void ll_2(View view) {
		Intent intent = new Intent(SenderAddressActivity.this,
				Index2Activity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "1");
		intent.putExtra(ExtraKeys.Key_Msg2, "");
		startActivity(intent);
	}

	public void iv_top_left(View v) {
		SenderAddressActivity.this.finish();
	}

	public void finish() {
		Intent intent = getIntent();
		getMyApplication().setCity("");
	};

	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// 返回地址信息搜索结果。 参数： arg0 - 搜索结果 arg1 - 错误号，0表示结果正确，result中有
			// 相关结果信息；100表示结果正确，无相关地址信息
			if (arg0 == null) {
				toast.showToast(getString(R.string.reservation_mksearch_toast));
			} else {
				// 获得搜索地址的经纬度
				latitude_Receive = arg0.geoPt.getLatitudeE6() / 1E6;
				longitude_Receive = arg0.geoPt.getLongitudeE6() / 1E6;

				for (int i = 0; i < arg0.poiList.size(); i++) {
					String aaString = arg0.poiList.get(i).address;
					Log.d("", i + "aaString:" + aaString);
				}
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
		public void onGetPoiResult(MKPoiResult result, int arg1, int arg2) {

			sb.append("共搜索到").append(result.getNumPois()).append("个POI/n");
			// 遍历当前页返回的POI（默认只返回10个）
			for (MKPoiInfo poiInfo : result.getAllPoi()) {
				sb.append("名称：").append(poiInfo.name).append("/n");
				sb.append("地址：").append(poiInfo.address).append("/n");
				sb.append("经度：")
						.append(poiInfo.pt.getLongitudeE6() / 1000000.0f)
						.append("/n");
				sb.append("纬度：")
						.append(poiInfo.pt.getLatitudeE6() / 1000000.0f)
						.append("/n");
			}
			Log.d("", "sb:" + sb);
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
	 * 发件人提交订单
	 */
	private Runnable CustomerSubmitOrder = new Runnable() {

		@Override
		public void run() {
			// //
			// {CustomerId:'10',CustomerProvince:'上海市',CustomerCity:'上海',CustomerDistrict:'闵行',
			// CustomerLocationX:'121.396270',CustomerLocationY:'31.175080',CustomerLocationAddress:'上海市闵行区宜山路1718号-3幢',
			// ReceiveProvince:'上海',ReceiveCity:'上海',ReceiveDistrict:'徐汇',ReceiveLocationX:'121.396250',
			// ReceiveLocationY:'31.185010',ReceiveLocationAddress:'上海市闵行区宜山路1718号-3幢',ServiceType:'1',
			// OrderVoice:'',Remark:'备注'}
			HashMap<String, Object> param = new HashMap<String, Object>();
			// param.put("LocationY", latitude);
			// param.put("Province", getIndexApplication().getProvince());
			// param.put("City", getIndexApplication().getCity());
			// param.put("District", getIndexApplication().getDistrict());
			getData.doPost(callBack1, GetDataConfing.ip, param,
					"CustomerSubmitOrder", 0);
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
					} else if (1 == what) {
					} else if (2 == what) {

					} else if (3 == what) {
						toast.showToast(data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message"));
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};
}
