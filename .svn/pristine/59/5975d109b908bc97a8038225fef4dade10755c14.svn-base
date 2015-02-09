package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.striveen.express.Locate;
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.UserOrderAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.Confing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.view.MyLoadMoreListView;
import com.striveen.express.view.MyLoadMoreListView.LoadMoreDataListener;
import com.striveen.express.view.ViewInject;

/**
 * 订单列表
 * 
 * @author Fei
 * 
 */
public class UserOrderActivity extends MyActivity {

	private final String TAG = "UserOrderActivity";

	@ViewInject(id = R.id.sender_ddress_tv_top_center)
	private TextView tv_top_cente;
	@ViewInject(id = R.id.user_order_tv_day, click = "tv_day")
	private TextView tv_day;
	@ViewInject(id = R.id.user_order_tv_day1, click = "tv_day1")
	private TextView tv_day1;
	@ViewInject(id = R.id.user_order_ll_top, click = "ll_top")
	private LinearLayout ll_top;
	@ViewInject(id = R.id.user_order_ll_select)
	private LinearLayout ll_select;
	@ViewInject(id = R.id.user_order_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	@ViewInject(id = R.id.user_order_mlv_order, itemClick = "mlv_order")
	private MyLoadMoreListView mlv_order;

	/**
	 * 订单列表
	 */
	@ViewInject(id = R.id.user_order_lv_order, itemClick = "mlv_order")
	private ListView lv_order;

	/**
	 * 订单集合
	 */
	private List<JsonMap<String, Object>> data_order;
	/**
	 * 筛选订单布局
	 */
	// @ViewInject(id = R.id.user_order_rl_top2)
	// private RelativeLayout rl_top2;

	private UserOrderAdapter userOrderAdapter;
	private int check;
	/**
	 * check_order 为类型1.所有时间 2 当天订单 3 历史订单
	 */
	private String check_order;
	/**
	 * 请求路径
	 */
	private String url;
	/**
	 * 订单状态 1 已创建 2 已推送 3 已抢单 4 已收件(快递员) 5 已取消 6 已送达 7已收件（发件人）
	 */
	private int OrderStatus;
	private String OrderId;
	private boolean checkLoad;
	private View selectView;

	private int nextPage;
	private int lastItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_order);
		mlv_order.setAutoLoadMore(true);
		mlv_order.openAutoCorrectCurrentPage(3);
		mlv_order.setLoadMoreDataListener(loadMoreDataListener);
		check = 1;
		check_order = "1";
		Confing.PageSize = 10;
		Locate.getInstance().addData_activity(this);
		nextPage = 1;
		lv_order.setOnScrollListener(onScrollListener);
		getOrder(false);

	}

	/**
	 * 顶部弹出筛选框
	 * 
	 * @param view
	 */
	public void ll_top(View view) {
		if (check % 2 == 0) {
			check++;
			// rl_top2.setVisibility(View.GONE);
			ll_select.setVisibility(View.GONE);
			if (!"1".equals(check_order)) {
				check_order = "1";
				tv_top_cente.setText(getString(R.string.user_order_top_title));
				getOrder(true);

			}
		} else {
			check++;
			ll_select.setVisibility(View.VISIBLE);
			// rl_top2.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void iv_top_left(View view) {
		UserOrderActivity.this.finish();
	}

	/**
	 * 顶部筛选当天订单
	 * 
	 * @param view
	 */
	public void tv_day(View view) {
		ll_select.setVisibility(View.GONE);
		// if (!"2".equals(check_order)) {
		check_order = "2";
		check++;
		tv_top_cente.setText(getString(R.string.user_order_tv_day));
		getOrder(true);
		// }
	}

	/**
	 * 顶部筛选历史订单
	 * 
	 * @param view
	 */
	public void tv_day1(View view) {
		ll_select.setVisibility(View.GONE);
		// if (!"3".equals(check_order)) {
		check_order = "3";
		check++;
		tv_top_cente.setText(getString(R.string.user_order_tv_day1));
		getOrder(true);
		// }
	}

	public void mlv_order(AdapterView<?> panter, View v, int psotion, long id) {
		selectView = v;
		OrderStatus = data_order.get(psotion).getInt("OrderStatus");
		OrderId = data_order.get(psotion).getStringNoNull("OrderId");
		if (3 <= OrderStatus && 5 != OrderStatus) {
			Intent intent = new Intent(UserOrderActivity.this,
					UserOrderDatileActivity.class);
			intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
			intent.putExtra(ExtraKeys.Key_Msg2, data_order.get(psotion)
					.getStringNoNull("Avatar"));
			intent.putExtra(ExtraKeys.Key_Msg3, data_order.get(psotion)
					.getStringNoNull("ServerType"));
			startActivityForResult(intent, 0);
		}

	}

	/**
	 * 设置适配器
	 * 
	 * @param data
	 */
	private void setAdapter(List<JsonMap<String, Object>> data) {
		if (2 == nextPage) {
			data_order = data;
			// {\"ServerType\":1,\"StrServerType\":\"当日同城\",\"IsAppointment\":true,\"LogisticsName\":null,\
			// "OrderStatus\":1,\"StrOrderStatus\":\"已创建\",\"FromAddress\":\"上海市闵行区宜山路1718号-3幢\",\"
			// ""ToAddress\":\"江西省宜春市靖安县\",\"Time\":\"预约取件时间：14年07月02日 02:30\","
			// + "\"Evaluate\":\"未评价\",\"OrderId\":17}

			userOrderAdapter = new UserOrderAdapter(this, data_order,
					R.layout.item_user_order_list, new String[] {
							"LogisticsName", "CourierName", "StrOrderStatus",
							"FromAddress", "ToAddress", "Time", "Evaluate" },
					new int[] { R.id.user_order_tv_logistics,
							R.id.user_order_tv_name, R.id.user_order_tv_wait,
							R.id.user_order_tv_start_ade,
							R.id.user_order_tv_end_ade,
							R.id.user_order_tv_time,
							R.id.user_order_tv_evaluate }, 0);
			lv_order.setAdapter(userOrderAdapter);
		} else {
			data_order.addAll(data);
			userOrderAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			if (0 == requestCode) {
				if ("-1".equals(data.getStringExtra(ExtraKeys.Key_Msg1))) {
					TextView tv = (TextView) selectView
							.findViewById(R.id.user_order_tv_evaluate);
					tv.setText(getString(R.string.user_order_tv_pingjia));
				}
				if ("-1".equals(data.getStringExtra(ExtraKeys.Key_Msg2))) {
					TextView tv = (TextView) selectView
							.findViewById(R.id.user_order_tv_wait);
					tv.setText(getString(R.string.user_order_datile_tv_have_ecipient));
				}
				if ("2".equals(data.getStringExtra(ExtraKeys.Key_Msg2))) {

					TextView tv = (TextView) selectView
							.findViewById(R.id.user_order_tv_wait);
					tv.setText(getString(R.string.user_order_datile_tv_have_cancle));
				}
			}
		}
	}

	/**
	 * 获取列表
	 * 
	 * @param isChange
	 */
	private void getOrder(boolean isChange) {
		if (isChange) {
			nextPage = 1;
			mlv_order.setAdapter(null);
			data_order.clear();
			userOrderAdapter = null;
		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetOrdersByCustomerId);
	}

	/**
	 * 发件人订单列表
	 */
	private Runnable GetOrdersByCustomerId = new Runnable() {

		@Override
		public void run() {
			// {'CustomerId':10,PageIndex:1,Type:1}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("Type", check_order);
			param.put("PageIndex", nextPage);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"GetOrdersByCustomerId", 0);
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
					nextPage++;
					if (0 == what) {
						if (0 < data_.getList_JsonMap("Result").size()) {
							checkLoad = true;
							setAdapter(data_.getList_JsonMap("Result"));
						}
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}

		}
	};
	/**
	 * 加载更多的回调
	 */
	private LoadMoreDataListener loadMoreDataListener = new LoadMoreDataListener() {

		@Override
		public void loadMore() {
			if (checkLoad) {
				getOrder(false);
				checkLoad = false;
			}

		}
	};
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
}
