package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.SimpleAsyImgAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.JsonKeys;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.sql.DBManager;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.MyLoadMoreListView;
import com.striveen.express.view.ViewInject;

/**
 * 常用地址
 * 
 * @author Fei
 * 
 */
public class CommonAddressActivity extends MyActivity {

	private final String TAG = "CommonAddressActivity";
	@ViewInject(id = R.id.common_ddress_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	@ViewInject(id = R.id.common_address_mlv_data, itemLongClick = "mlv_data")
	private MyLoadMoreListView mlv_data;
	private SimpleAsyImgAdapter adapter;
	List<JsonMap<String, Object>> place_data;
	List<JsonMap<String, Object>> load_data;

	private DBManager dbManager;
	private String customerId;
	private String AddressId;
	private String Address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_address);

		customerId = getMyApplication().getUserId();
		// customerId = "4";
		// if (null != place_data && place_data.size() > 0) {
		// // Log.d("", "place_data:" + place_data);
		// setAdapter(place_data);
		// } else {
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetCustomerAddress);
		// }
		// loadingToast.show();
		// ThreadPoolManager.getInstance().execute(GetCustomerAddress);
		mlv_data.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				AddressId = load_data.get(arg2).getStringNoNull("AddressId");
				Address = load_data.get(arg2).getStringNoNull("Address");
				// {\"AddressId\":41,\"CustomerId\":53,\"Province\":\"上海市\",\"City\":\"上海市\""
				// ",\"District\":\"闵行区\",\"AddressType\":null,\"Address\":\"上海市闵行区莲花路1733号-d栋-107室\""
				// + ",\"AddressX\":121.396040,\"AddressY\":31.175180,"
				cancelOrder();
				return false;
			}
		});
	}

	public void iv_top_left(View v) {
		CommonAddressActivity.this.finish();
	}

	private void setAdapter(List<JsonMap<String, Object>> data) {
		if (null == data) {
			return;
		}
		// place_data.clear();
		// place_data = data;
		adapter = new SimpleAsyImgAdapter(this, load_data,
				R.layout.item_history_address_list, new String[] { "Address" },
				new int[] { R.id.item_history_address_tv }, 0);
		mlv_data.setAdapter(adapter);
	}

	/**
	 * 发件人常用地址
	 */
	private Runnable GetCustomerAddress = new Runnable() {

		@Override
		public void run() {
			// {'CustomerId':4}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", customerId);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"GetCustomerAddress", 10);
		}
	};
	/**
	 * 发件人删除常用地址
	 */
	private Runnable CustomerDeleteAddress = new Runnable() {

		@Override
		public void run() {
			// {AddressId:’’}

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("AddressId", AddressId);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerDeleteAddress", 11);
		}
	};
	/**
	 * 获取数据回调
	 */
	private ResponseCallBack callBack = new ResponseCallBack() {

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
					if (10 == what) {
						load_data = data_.getList_JsonMap("Result");
						// for (int i = 0; i < place_data.size(); i++) {
						// dbManager.insert_CommonlyUsedAddress(
						// getMyApplication().getUserPhone(),
						// place_data.get(i), 1, "1");
						// }
						// place_data = dbManager.get_CommonlyUsedAddress("1");
						setAdapter(load_data);
					} else if (11 == what) {
						loadingToast.show();
						ThreadPoolManager.getInstance().execute(
								GetCustomerAddress);
						if (StringUtil.judgeExpressDB() == -1) {
							if (300 <= StringUtil.getCompareSize()) {
								dbManager = new DBManager(
										CommonAddressActivity.this);
								dbManager.createCommonlyUsedAddressTable();
								place_data = dbManager
										.get_CommonlyUsedAddress("-1");
								delectAddress();
							}
						} else {
							if (null == dbManager) {
								dbManager = DBManager
										.getInstance(CommonAddressActivity.this);
								dbManager.createCommonlyUsedAddressTable();
							}
							place_data = dbManager
									.get_CommonlyUsedAddress("-1");
							delectAddress();
						}

					}
				}
			} else {
				toast.showToast(data.getStringNoNull(JsonKeys.Key_Msg));
			}
		}
	};

	/**
	 * 删除本地地址
	 */
	private void delectAddress() {
		int inist = 0;
		if (place_data.size() > 0) {
			for (int i = 0; i < place_data.size(); i++) {
				if (Address
						.equals(place_data.get(i).getStringNoNull("Address"))) {
					inist = 1;
				}
			}
		}
		if (1 == inist) {
			dbManager.delete_CommonlyUsedAddress_Address(Address);
		}
	}

	/**
	 * 删除该地址
	 */
	private void cancelOrder() {
		Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.main_toast_tishi));
		builder.setMessage("确认删除该地址？");
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
								CustomerDeleteAddress);
					}
				});
		builder.show();
	}
}
