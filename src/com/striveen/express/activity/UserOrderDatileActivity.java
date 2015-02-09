package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.adapter.DialogAdapter;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.AsyImgView;
import com.striveen.express.view.LayoutProductCommentStartView;
import com.striveen.express.view.LayoutProductCommentStartView.StarOnClickCallBack;
import com.striveen.express.view.ListViewNoScroll;
import com.striveen.express.view.ViewInject;
import com.zijunlin.Zxing.Demo.CaptureActivity;

/**
 * 订单详情
 * 
 * @author Fei
 * 
 */
public class UserOrderDatileActivity extends MyActivity {

	private final String TAG = "UserOrderDatileActivity";

	@ViewInject(id = R.id.user_order_datile_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	/**
	 * 快递员
	 */
	@ViewInject(id = R.id.user_order_datile_lcsv_start)
	private LayoutProductCommentStartView lcsv_start;
	/**
	 * 评价订单
	 */
	@ViewInject(id = R.id.user_order_datile_lcsv_start1)
	private LayoutProductCommentStartView lcsv_start1;
	/**
	 * 快递员头像
	 */
	@ViewInject(id = R.id.user_order_datile_aiv_avata)
	private AsyImgView aiv_avata;
	/**
	 * 快递员所属公司
	 */
	@ViewInject(id = R.id.user_order_datile_tv_top_logistics)
	private TextView tv_top_logistics;
	/**
	 * 快递员名称
	 */
	@ViewInject(id = R.id.user_order_datile_tv_top_name)
	private TextView tv_top_name;
	/**
	 * 快递员以服务多少单
	 */
	@ViewInject(id = R.id.user_order_datile_tv_order_num)
	private TextView tv_order_num;
	/**
	 * 快递员手机号
	 */
	@ViewInject(id = R.id.user_order_datile_iv_top_phone, click = "iv_top_phone")
	private ImageView iv_top_phone;

	/**
	 * 不是当日同城且已收件时 隐藏
	 */
	@ViewInject(id = R.id.user_order_datile_ll8_0001)
	private LinearLayout ll8_0001;

	/**
	 * 快递类型
	 */
	@ViewInject(id = R.id.user_order_datile_tv_type)
	private TextView tv_type;
	/**
	 * 发件地址
	 */
	@ViewInject(id = R.id.user_order_datile_tv_start_addr)
	private TextView tv_start_addr;
	/**
	 * 发件时间
	 */
	@ViewInject(id = R.id.user_order_datile_tv_time1)
	private TextView tv_time1;
	/**
	 * 快递员是否已接单
	 */
	@ViewInject(id = R.id.user_order_datile_tv_orders)
	private TextView tv_orders;
	/**
	 * 收件人地址
	 */
	@ViewInject(id = R.id.user_order_datile_tv_end_addr)
	private TextView tv_end_addr;
	/**
	 * 提交评价
	 */
	@ViewInject(id = R.id.user_order_datile_tv_sumbit, click = "tv_sumbit")
	private TextView tv_sumbit;

	/**
	 * 快递员接单时间
	 */
	@ViewInject(id = R.id.user_order_datile_tv_time2)
	private TextView tv_time2;
	/**
	 * 收件人已收件显示该布局
	 */
	@ViewInject(id = R.id.user_order_datile_rl6)
	private LinearLayout ll_rl6;
	/**
	 * 收件人已收件显示该布局 收件时间
	 */
	@ViewInject(id = R.id.user_order_datile_ll7)
	private LinearLayout ll_ll7;
	/**
	 * 收件人已收件显示该布局 订单号布局
	 */
	@ViewInject(id = R.id.user_order_datile_ll8)
	private LinearLayout ll_ll8;

	/**
	 * 已收件并且是当日同城时显示该布局隐藏ll_ll8_02
	 */
	@ViewInject(id = R.id.user_order_datile_ll8_01)
	private LinearLayout ll_ll8_01;
	/**
	 * 已收件并且不是当日同城时显示该布局隐藏ll_ll8_01
	 */
	@ViewInject(id = R.id.user_order_datile_ll8_02)
	private LinearLayout ll_ll8_02;
	/**
	 * 未收件时该布局显示未收件
	 * 
	 * 已收件并且是当日同城时显示是否已送达，否则隐藏
	 */
	@ViewInject(id = R.id.user_order_datile_ll10)
	private LinearLayout ll_ll10;

	/**
	 * 收件人是否已收件
	 */
	@ViewInject(id = R.id.user_order_datile_tv_ecipient)
	private TextView tv_ecipient;
	/**
	 * 最后流程状态
	 */
	@ViewInject(id = R.id.user_order_datile_tv_last)
	private TextView tv_last;
	/**
	 * 最后流程时间
	 */
	@ViewInject(id = R.id.user_order_datile_tv_last_time)
	private TextView tv_last_time;
	/**
	 * 收件人收件时间
	 */
	@ViewInject(id = R.id.user_order_datile_tv_time3)
	private TextView tv_time3;
	/**
	 * 快递单号
	 */
	@ViewInject(id = R.id.user_order_datile_tv_single_number1)
	private TextView tv_single_number1;
	/**
	 * 收件人手机号
	 */
	@ViewInject(id = R.id.user_order_datile_tv_addressee_phone)
	private TextView tv_addressee_phone;
	/**
	 * 发件人确认发出
	 */
	@ViewInject(id = R.id.user_order_datile_tv_send)
	private TextView tv_send;

	/**
	 * 评价
	 */
	@ViewInject(id = R.id.user_order_tv_pingjia)
	private TextView tv_pingji;
	/**
	 * 订单详情布局
	 */
	@ViewInject(id = R.id.user_order_datile_rl_all)
	private RelativeLayout rl_all;

	/**
	 * 已录入快递单号布局
	 */
	@ViewInject(id = R.id.user_order_datile_ll_single_number)
	private LinearLayout ll_single_number;
	/**
	 * 未录入快递单号布局
	 */
	@ViewInject(id = R.id.user_order_datile_ll_no_single_number)
	private LinearLayout ll_no_single_number;
	/**
	 * 快递单号输入框
	 */
	@ViewInject(id = R.id.user_order_datile_et_single_number)
	private EditText et_single_number;
	/**
	 * 收件人手机号输入框
	 */
	@ViewInject(id = R.id.user_order_datile_et_phone_number)
	private EditText et_phone_number;
	/**
	 * 录入快递单号 提交
	 */
	@ViewInject(id = R.id.user_order_datile_bt_sumbit, click = "bt_sumbit")
	private Button bt_sumbit;

	/**
	 * 分享
	 */
	@ViewInject(id = R.id.user_order_bottom_ll, click = "bottom_ll")
	private LinearLayout bottom_ll;
	/**
	 * \ 订单取消
	 */
	@ViewInject(id = R.id.user_order_top_tv_right)
	private TextView top_tv_right;
	/**
	 * 订单id
	 */
	private String OrderId;
	/**
	 * 快递员手机号
	 */
	private String CourierPhone;
	/**
	 * 评价服务星星个数
	 */
	private int Score;
	/**
	 * 快递员头像
	 */
	private String Avatar;
	/**
	 * 订单类型 1当日同城 2 普通同城 3普通异地
	 */
	private String ServerType;
	/**
	 * 评价服务
	 */
	private int OrderPoint;
	/**
	 * 收件人手机
	 */
	private String ReceivePhone;
	/**
	 * 发件人运单号
	 */
	private String DeliveryNO;
	private String checkIntent;
	private String checkConfirmOrder;
	private LocationClient mLocClient;
	/**
	 * latitude 纬度，longitude经度
	 */
	private Double latitude = -1.0, longitude = -1.0;
	private String Province;
	private String City;
	private String District;
	private String address;
	/**
	 * 显示取消订单原因
	 */
	private Dialog dialogLogin;
	private DialogAdapter myDialogAdapter;
	/**
	 * 订单取消原因id
	 */
	private String ReasonId;
	private List<JsonMap<String, Object>> data_dialog;
	private int getDialog;
	/**
	 * 运单号
	 */
	private String WayBillNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_order_datile);
		lcsv_start.setStartNum(5);
		lcsv_start1.setStar();
		lcsv_start1.setStartNum(5, callBack2);
		rl_all.setVisibility(View.GONE);
		lcsv_start1.setOpenChange(true);
		checkIntent = "1";
		checkConfirmOrder = "1";
		getDialog = -1;
		Score = 5;
		OrderId = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		Avatar = getIntent().getStringExtra(ExtraKeys.Key_Msg2);
		ServerType = getIntent().getStringExtra(ExtraKeys.Key_Msg3);
		data_dialog = getMyApplication().getData_dialog();
		if (!TextUtils.isEmpty(Avatar)) {
			aiv_avata.setImgUrl(Avatar);
		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerTrackOrderDelivery);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void iv_top_left(View v) {
		UserOrderDatileActivity.this.finish();
	}

	/**
	 * 分享
	 * 
	 * @param view
	 */
	public void bottom_ll(View v) {
		getMyApplication().addShare(this);
	}

	/**
	 * 录入快递单号 提交
	 */
	public void bt_sumbit(View v) {
		ReceivePhone = et_phone_number.getText().toString();
		DeliveryNO = et_single_number.getText().toString();
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
		ThreadPoolManager.getInstance().execute(UpdateOrderReceiveNo);
	}

	/**
	 * 快递员电话
	 * 
	 * @param v
	 */
	public void iv_top_phone(View v) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ CourierPhone));
		UserOrderDatileActivity.this.startActivity(intent);
	}

	/**
	 * 刷新界面
	 * 
	 * @param data
	 */
	private void refresh(JsonMap<String, Object> data) {
		rl_all.setVisibility(View.VISIBLE);
		// "Result":{"LogisticsName":"中通快递","CourierName":"李雯雯","ServerCount":8,"ServerPoint":0,
		// "CourierPhone":"13818489951",

		// "OsList":[{"Status":"当日同城","Address":"上海市闵行区宜山路1718号-3幢",
		// "DateTime":"2014-7-19 20:21:21"},{"Status":"已接单","Address":"","DateTime":"2014-7-19 21:04:00"},
		// {"Status":"已送达","Address":"","DateTime":"2014-7-19 21:04:16"}]
		// ,"DeliveryNO":null,"ReceivePhone":null,"OrderPoint":0},

		OrderPoint = data.getInt("OrderPoint");
		if (0 != OrderPoint) {
			lcsv_start1.setStartNum(OrderPoint);
			lcsv_start1.setOpenChange(false);
			tv_sumbit.setVisibility(View.GONE);
			tv_pingji
					.setText(getString(R.string.user_order_datile_tv_pingjia111));
		} else {
			tv_sumbit.setVisibility(View.VISIBLE);
		}
		tv_top_logistics.setText(data.getStringNoNull("LogisticsName"));
		tv_top_name.setText(data.getStringNoNull("CourierName"));
		tv_order_num.setText(String.format(
				getString(R.string.user_order_datile_tv_num1),
				data.getStringNoNull("ServerCount")));
		CourierPhone = data.getStringNoNull("CourierPhone");
		lcsv_start.setStartNum(data.getInt("ServerPoint"));

		List<JsonMap<String, Object>> data_OsList = data
				.getList_JsonMap("OsList");
		getMyApplication().setData_orderDetaile(data_OsList);
		if (2 == data_OsList.size()) {
			mLocClient = new LocationClient(this);
			Locate.getInstance().loadAddre(callBack3, mLocClient);
			tv_send.setVisibility(View.VISIBLE);
			top_tv_right.setVisibility(View.VISIBLE);
		} else {
			tv_send.setVisibility(View.GONE);
			top_tv_right.setVisibility(View.GONE);
		}
		if (2 <= data_OsList.size()) {
			tv_type.setText(data_OsList.get(0).getStringNoNull("Status"));
			tv_start_addr
					.setText(data_OsList.get(0).getStringNoNull("Address"));
			tv_time1.setText(data_OsList.get(0).getStringNoNull("DateTime"));
			tv_orders.setText(data_OsList.get(1).getStringNoNull("Status"));
			tv_end_addr.setText(data_OsList.get(1).getStringNoNull("Address"));
			tv_time2.setText(data_OsList.get(1).getStringNoNull("DateTime"));
			ll_rl6.setVisibility(View.GONE);
			ll_ll7.setVisibility(View.GONE);
			ll_ll8.setVisibility(View.GONE);
			ll_ll10.setVisibility(View.VISIBLE);
			tv_last.setText(getString(R.string.user_order_datile_tv_wait));
		}
		if (3 <= data_OsList.size()) {
			ll_rl6.setVisibility(View.VISIBLE);
			ll_ll7.setVisibility(View.VISIBLE);
			ll_ll8.setVisibility(View.VISIBLE);
			ll_ll10.setVisibility(View.INVISIBLE);
			if ("1".equals(ServerType)) {
				ll_ll8_01.setVisibility(View.VISIBLE);
				ll_ll8_02.setVisibility(View.GONE);
				ll_ll10.setVisibility(View.VISIBLE);
				tv_last.setText(getString(R.string.user_order_datile_tv_undelivered));
				tv_last_time.setVisibility(View.GONE);
			} else {
				ll_ll8_01.setVisibility(View.GONE);
				ll_ll8_02.setVisibility(View.VISIBLE);
				ll8_0001.setVisibility(View.INVISIBLE);
			}

			tv_ecipient.setText(data_OsList.get(2).getStringNoNull("Status"));
			tv_time3.setText(data_OsList.get(2).getStringNoNull("DateTime"));
			if (TextUtils.isEmpty(data.getStringNoNull("DeliveryNO"))
					|| TextUtils.isEmpty(data.getStringNoNull("ReceivePhone"))) {
				ll_single_number.setVisibility(View.GONE);
				ll_no_single_number.setVisibility(View.VISIBLE);
				et_phone_number.setText(data.getStringNoNull("ReceivePhone"));
				et_single_number.setText(data.getStringNoNull("DeliveryNO"));
				setEdit(et_single_number);
			} else {
				ll_single_number.setVisibility(View.VISIBLE);
				ll_no_single_number.setVisibility(View.GONE);
				tv_single_number1.setText(data.getStringNoNull("DeliveryNO"));
				tv_addressee_phone
						.setText(data.getStringNoNull("ReceivePhone"));
			}
		}
		if (4 <= data_OsList.size()) {
			ll8_0001.setVisibility(View.VISIBLE);
			ll_ll10.setVisibility(View.VISIBLE);
			if ("1".equals(ServerType)) {
				ll_ll8_01.setVisibility(View.VISIBLE);
				ll_ll8_02.setVisibility(View.GONE);
				ll_ll10.setVisibility(View.VISIBLE);
				tv_last.setText(data_OsList.get(3).getStringNoNull("Status"));
				tv_last_time.setVisibility(View.VISIBLE);
				tv_last_time.setText(data_OsList.get(3).getStringNoNull(
						"DateTime"));
			} else {
				ll_ll8_01.setVisibility(View.GONE);
				ll_ll8_02.setVisibility(View.VISIBLE);
				ll_ll10.setVisibility(View.GONE);
			}
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
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerTrackOrderDelivery", 60);
		}
	};
	/**
	 * 发件人评论快递员服务 {'OrderId':'9','CustomerId':'10','Score':'5','Remark':'你好，是否'}
	 */
	private Runnable CustomerRateOrderCourier = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("Score", Score);
			param.put("Remark", "你好，是否");
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerRateOrderCourier", 61);
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
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("ReasonId", ReasonId);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerCancelOrder", 63);
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
			getData.doPost(callBack, GetDataConfing.ip, param,
					"UpdateOrderReceiveNo", 64);
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
//			 "{'LocationCity':'上海市','DeliveryNO':'123456789','LocationProvince':'上海市',"
//			 "'OrderId':'36','LocationAddress':'上海市闵行区宜山路1718号-3幢','ReceivePhone':"
//			 "'13162972827','LocationX':121.396171,'LocationY':31.175009,'LocationDistrict':'闵行区'}"

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("ReceivePhone", ReceivePhone);
			param.put("DeliveryNO", DeliveryNO);
			param.put("LocationProvince", Province);
			param.put("LocationCity", City);
			param.put("LocationDistrict", District);
			param.put("LocationX", longitude);
			param.put("LocationY", latitude);
			param.put("LocationAddress", address);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerConfirmOrder", 65);
		}
	};
	/**
	 * 获得取消原因
	 */
	private Runnable GetAllCancelReason = new Runnable() {

		@Override
		public void run() {
			getData.doPost(callBack, GetDataConfing.ip, null,
					"GetAllCancelReason", 66);
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
					if (60 == what) {
						refresh(data_.getJsonMap("Result"));
					} else if (61 == what) {
						checkIntent = "-1";
						loadingToast.show();
						ThreadPoolManager.getInstance().execute(
								CustomerTrackOrderDelivery);
					} else if (64 == what) {
						et_phone_number.setText(null);
						et_single_number.setText(null);
						ll_single_number.setVisibility(View.VISIBLE);
						ll_no_single_number.setVisibility(View.GONE);
						tv_single_number1.setText(DeliveryNO);
						tv_addressee_phone.setText(ReceivePhone);
					} else if (65 == what) {
						checkConfirmOrder = "-1";
						loadingToast.show();
						ThreadPoolManager.getInstance().execute(
								CustomerTrackOrderDelivery);
					} else if (63 == what) {// 发件人取消订单
						checkConfirmOrder = "2";
						toast.showToast(data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message"));
						dialogLogin.cancel();
						finish();

					} else if (66 == what) {// 获得取消原因
						data_dialog = data_.getList_JsonMap("Result");
						ReasonId = data_dialog.get(0).getStringNoNull(
								"ReasonId");
						getMyApplication().setData_dialog(data_dialog);
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
	 * 点击订单评价回调
	 */
	private StarOnClickCallBack callBack2 = new StarOnClickCallBack() {

		@Override
		public void getNum(int num) {
			Score = num;
		}
	};

	/**
	 * 评价服务
	 * 
	 * @param v
	 */
	public void tv_sumbit(View v) {

		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerRateOrderCourier);
	}

	public void finish() {
		Intent intent = getIntent();
		if ("-1" == checkIntent) {
			intent.putExtra(ExtraKeys.Key_Msg1, checkIntent);
			intent.putExtra(ExtraKeys.Key_Msg2, checkConfirmOrder);
		}
		if ("-1" == checkConfirmOrder) {
			intent.putExtra(ExtraKeys.Key_Msg1, checkIntent);
			intent.putExtra(ExtraKeys.Key_Msg2, checkConfirmOrder);
		}
		if ("2" == checkConfirmOrder) {
			intent.putExtra(ExtraKeys.Key_Msg1, checkIntent);
			intent.putExtra(ExtraKeys.Key_Msg2, checkConfirmOrder);
		}
		Log.e(TAG, "checkConfirmOrder=" + checkConfirmOrder);
		setResult(RESULT_OK, intent);
		super.finish();
	};

	/**
	 * 定位回调
	 */
	private LocateCallBack callBack3 = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province1, String City1, String District1) {
			Province = Province1;
			City = City1;
			District = District1;
			latitude = latitude1;
			longitude = longitude1;
			address = str;
			if (null != mLocClient) {
				mLocClient.stop();
				mLocClient = null;
			}
		}

	};

	/**
	 * 确认发出
	 * 
	 * @param v
	 */
	public void tv_send(View v) {
		Intent intent = new Intent(UserOrderDatileActivity.this,
				PayOrEvaluateActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
		intent.putExtra(ExtraKeys.Key_Msg2, OrderPoint);
		startActivityForResult(intent, 0);
		// loadingToast.show();
		// ThreadPoolManager.getInstance().execute(CustomerConfirmOrder);
	}

	/**
	 * @Title: iv_phone
	 * @Description: TODO(方法的作用：从通讯录中选择收件人手机号)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void iv_phone(View v) {
		startActivityForResult(new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI), 1);
	}

	/**
	 * @Title: iv_erweima
	 * @Description: TODO(方法的作用：二维码扫描)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void iv_erweima(View v) {
		Intent intent = new Intent(this, CaptureActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, OrderId);
		startActivity(intent);
	}

	// 声明姓名，电话
	private String username, usernumber;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "RESULT_OK:" + RESULT_OK);
		if (resultCode == RESULT_OK) {
			Log.e(TAG, "Key_Msg2:" + data.getStringExtra(ExtraKeys.Key_Msg2));
			if (0 == requestCode) {
				if ("-1".equals(data.getStringExtra(ExtraKeys.Key_Msg1))) {
					checkIntent = "-1";
				}
				if ("ok".equals(data.getStringExtra(ExtraKeys.Key_Msg2))) {
					loadingToast.show();
					ThreadPoolManager.getInstance().execute(
							CustomerTrackOrderDelivery);
					checkConfirmOrder = "-1";
				}
			} else if (1 == requestCode) {
				// ContentProvider展示数据类似一个单个数据库表
				// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
				ContentResolver reContentResolverol = getContentResolver();
				// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				cursor.moveToFirst();
				// 获得DATA表中的名字
				username = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					usernumber = phone
							.getString(
									phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
							.replaceAll(" ", "");
					et_phone_number.setText(usernumber);
					setEdit(et_phone_number);
				}
			}
		}

	}

	/**
	 * 取消订单
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

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.index3_iv_top_phone:// 取消订单关闭dialog
				dialogLogin.cancel();
				break;
			case R.id.index3_tv_bottom:// 确定选择的原因
				cancelOrder();
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

	DetaileBroadcastReceiver mReceiver;

	private void setEdit(EditText editText) {
		CharSequence text = editText.getText();
		// Debug.asserts(text instanceof Spannable);
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	private int onStartTime;

	@Override
	protected void onResume() {
		// if (0 == onStartTime) {
		// onStartTime++;
		// WayBillNo = null;
		// et_single_number.setText(WayBillNo);
		// Log.e(TAG, "WayBillNo:" + WayBillNo + ";onStartTime=" + onStartTime);
		// }
		// Log.e(TAG, "getSearchKey:" + getMyApplication().getSearchKey());
		// if (!isTextEmpty(getMyApplication().getSearchKey())) {
		// WayBillNo = getMyApplication().getSearchKey();
		//
		// getMyApplication().setSearchKey(null);
		// } else {
		// WayBillNo = null;
		// }
		// et_single_number.setText(WayBillNo);
		super.onResume();
	}

	protected void onStart() {
		// TODO 重写onStart方法 注册用于接收Service传送的广播
		Log.e(TAG, "mReceiver:" + mReceiver);
		if (null == mReceiver) {
			mReceiver = new DetaileBroadcastReceiver();
		}
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction(OrderId);
		registerReceiver(mReceiver, filter);// 注册Broadcast Receiver
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO onDestroy
		// 退出时销毁定位
		Log.e(TAG, "onStop= mReceiver");
		unregisterReceiver(mReceiver);
		mReceiver = null;
		super.onDestroy();
	}

	/**
	 * 自定义广播接受通知
	 * 
	 * @author Fei
	 * 
	 */
	public class DetaileBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自定义广播接受通知
			Bundle bundle = intent.getExtras();
			try {
				Log.e(TAG, "intent.getAction():" + intent.getAction());
				if (OrderId.equals(intent.getAction())) {
					WayBillNo = bundle.get("code").toString();
					et_single_number.setText(WayBillNo);
				} else {
					Log.e(TAG, "WayBillNo = null");
					WayBillNo = null;
					et_single_number.setText(WayBillNo);
					setEdit(et_single_number);
				}

			} catch (Exception e) {
				Log.v("test", "bundle:" + bundle);
				Log.v("test", "Exception:" + e.toString());
			}
		}
	}
}
