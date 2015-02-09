package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.SimpleAsyImgAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.Confing;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.view.ViewInject;

/**
 * @ClassName: PayOrBalance
 * @Description: TODO(该类的作用：支付和余额)
 * @author Fei
 * @date 2014年9月10日 下午3:21:24
 * 
 */
public class MyBalanceActivity extends MyActivity {

	private final String TAG = "MyBalanceActivity";

	@ViewInject(id = R.id.my_balance_tv_surplus)
	private TextView tv_surplus;
	/**
	 * 订单列表
	 */
	@ViewInject(id = R.id.my_balance_lv_order)
	private ListView lv_order;
	private int nextPage;
	private int lastItem = 0;
	private SimpleAsyImgAdapter balanceAdapter;
	private List<JsonMap<String, Object>> data_order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_balance);
		Confing.PageSize = 10;
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerBudgetAndTotal);
		tv_surplus.setText("0.0");
		nextPage = 1;
		lv_order.setOnScrollListener(onScrollListener);
	}

	/**
	 * 获取列表
	 * 
	 * @param isChange
	 */
	private void getOrder(boolean isChange) {
		if (isChange) {
			nextPage = 1;
			lv_order.setAdapter(null);
			data_order.clear();
			balanceAdapter = null;
		}
		if (0 == nextPage) {
			nextPage = 1;
		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerBudgetAndTotal);
	}

	/**
	 * TODO 抽奖
	 */
	private Runnable CustomerBudgetAndTotal = new Runnable() {

		@Override
		public void run() {
			// {"CustomerId":13,"PageIndex":1}

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("PageIndex", nextPage);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerBudgetAndTotal", 0);
		}
	};

	/**
	 * TODO 获取数据回调
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
					if (0 == what) {
						nextPage++;
						// Result\":{\"Total\":\"1000.0000\",\"CustomerList\":
						data_.getJsonMap("").getFloat("Total");
						tv_surplus.setText(data_.getJsonMap("Result").getFloat(
								"Total")
								+ "");
						if (0 < data_.getJsonMap("Result")
								.getList_JsonMap("CustomerList").size()) {
							setAdapter(data_.getJsonMap("Result")
									.getList_JsonMap("CustomerList"));
						}

					}
				}
			}
		}
	};

	private void setAdapter(List<JsonMap<String, Object>> data) {
		if (2 == nextPage) {
			data_order = data;
			// {\"Money\":1000.00,\"Description\":\"1000.00\",\"DateTime\":\"2014/9/12
			// 17:50:03\"}
			balanceAdapter = new SimpleAsyImgAdapter(this, data_order,
					R.layout.item_my_balance, new String[] { "Description",
							"Money", "DateTime" }, new int[] {
							R.id.item_my_balance_tv_context,
							R.id.item_my_balance_tv_balance,
							R.id.item_my_balance_tv_time, }, 0);
			lv_order.setAdapter(balanceAdapter);
		} else {
			data_order.addAll(data);
			balanceAdapter.notifyDataSetChanged();
		}
	}

	OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.e("", "SCROLL_STATE_IDLE=" + SCROLL_STATE_IDLE
					+ "\nscrollState=" + scrollState + "\nlastItem=" + lastItem);
			if (SCROLL_STATE_IDLE == scrollState
					&& lastItem == Confing.PageSize * (nextPage - 1)) {
				getOrder(false);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount;
		}
	};

	public void iv_top_left(View v) {
		MyBalanceActivity.this.finish();
	}
}
