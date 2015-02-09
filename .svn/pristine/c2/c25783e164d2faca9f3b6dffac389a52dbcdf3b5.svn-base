package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.ExpressPreferencesAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.view.GridviewNoScroll;
import com.striveen.express.view.ViewInject;

/**
 * 快递喜好
 * 
 * @author Fei
 * 
 */
public class ExpressPreferencesActivty extends MyActivity {
	private final String TAG = "ExpressPreferencesActivty";
	@ViewInject(id = R.id.express_preferences_gv_list, itemClick = "gv_list")
	private GridviewNoScroll gv_list;
	private ExpressPreferencesAdapter adapter;
	private List<JsonMap<String, Object>> data;
	int typeNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express_preferences);
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetCompanyDetest);
	}

	private void setAdapter(List<JsonMap<String, Object>> data) {
		this.data = data;
		adapter = new ExpressPreferencesAdapter(this, data,
				R.layout.item_express_preferences,
				new String[] { "CompanyName" },
				new int[] { R.id.item_express_preferencess_tv_title }, 0);
		gv_list.setAdapter(adapter);
	}

	public void gv_list(AdapterView<?> panter, View v, int position, long id) {
		boolean type = false;
		typeNum = 0;
		// {\"CompanyId\":4,\"CompanyName\":\"中通快递\",\"IsDetest\":false},IsDetest
		// true 是不喜欢的意思
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getBoolean("IsDetest")) {
				typeNum++;
			}
		}

		if (3 > typeNum) {
			if (data.get(position).getBoolean("IsDetest")) {
				typeNum--;
				type = false;
			} else {
				typeNum++;
				type = true;
			}
		} else {
			if (data.get(position).getBoolean("IsDetest")) {
				typeNum--;
			}
			type = false;
		}
		Log.d(TAG, "typeNum" + typeNum);
		Log.d(TAG, "getBoolean" + data.get(position).getBoolean("IsDetest"));
		Log.d(TAG, "type" + type);
		data.get(position).put("IsDetest", type);
		adapter.setSelectedPosition(position, type);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 获取快递公司喜好
	 */
	private Runnable GetCompanyDetest = new Runnable() {

		@Override
		public void run() {
			// {'CustomerId':14}
			HashMap<String, Object> par = new HashMap<String, Object>();
			par.put("CustomerId", getMyApplication().getUserId());
			getData.doPost(callBack, GetDataConfing.ip, par,
					"GetCompanyDetest", 22);
		}
	};
	/**
	 * 客户不喜欢的快递公司
	 */
	private Runnable UpdateCompanyDetest = new Runnable() {

		@Override
		public void run() {
			StringBuffer CompanyId = new StringBuffer();
			for (int i = 0; i < data.size(); i++) {

				if (data.get(i).getBoolean("IsDetest")) {
					CompanyId.append(","
							+ data.get(i).getStringNoNull("CompanyId"));
				}
			}
			Log.e(TAG, "CompanyId=" + CompanyId);
			if (1 < CompanyId.length()) {
				CompanyId.delete(0, 1);
			}

			// {'CustomerId':14,'CompanyId':'3,5'}
			HashMap<String, Object> par = new HashMap<String, Object>();
			par.put("CustomerId", getMyApplication().getUserId());
			par.put("CompanyId", CompanyId.toString());
			getData.doPost(callBack, GetDataConfing.ip, par,
					"UpdateCompanyDetest", 23);
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
				if (getData.isOk(data_)) {
					if (22 == what) {

						setAdapter(data_.getList_JsonMap("Result"));
					} else if (23 == what) {
						toast.showToast(data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message"));
					}
				}
			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}
		}
	};

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void iv_left(View v) {
		ExpressPreferencesActivty.this.finish();
	}

	/**
	 * 提交
	 * 
	 * @param v
	 */
	public void sumbit(View v) {
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(UpdateCompanyDetest);
	}
}
