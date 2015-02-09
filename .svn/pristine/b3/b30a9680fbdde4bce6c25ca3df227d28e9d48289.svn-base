package com.striveen.express.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.view.LayoutProductCommentStartView;
import com.striveen.express.view.LayoutProductCommentStartView.StarOnClickCallBack;
import com.striveen.express.view.ViewInject;
import com.zijunlin.Zxing.Demo.CaptureActivity;

/**
 * @ClassName: PayOrEvaluateActivity
 * @Description: TODO(该类的作用：支付和余额)
 * @author Fei
 * @date 2014年9月10日 下午3:21:24
 * 
 */
public class PayOrEvaluateActivity extends MyActivity {

	private final String TAG = "PayOrEvaluateActivity";
	/**
	 * 确认付款布局
	 */
	@ViewInject(id = R.id.pay_evaluace_ll_balance)
	private LinearLayout ll_balance;
	@ViewInject(id = R.id.pay_evaluace_ll_bottom)
	private LinearLayout ll_bottom;

	/**
	 * 选择使用余额
	 */
	@ViewInject(id = R.id.pay_evaluace_iv_credit_available)
	private ImageView iv_credit_available;
	@ViewInject(id = R.id.pay_evaluace_iv_clear, click = "iv_clear")
	private ImageView iv_clear;

	@ViewInject(id = R.id.pay_evaluace_et_balance)
	private EditText et_balance;
	/**
	 * 用余额
	 */
	@ViewInject(id = R.id.pay_evaluace_et_employ_balance)
	private EditText et_employ_balance;
	@ViewInject(id = R.id.pay_evaluace_et_phone)
	private EditText et_phone;
	/**
	 * 运单号
	 */
	@ViewInject(id = R.id.pay_evaluace_et_single_number)
	private EditText et_single_number;

	/**
	 * 可用余额
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_credit_available)
	private TextView tv_credit_available;
	/**
	 * 还需支付金额
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_also_pay)
	private TextView tv_also_pay;

	@ViewInject(id = R.id.pay_evaluace_sclv)
	private ScrollView _sclv;

	/**
	 * 快递类型
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_type)
	private TextView tv_type;
	/**
	 * 发件地址
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_start_addr)
	private TextView tv_start_addr;
	/**
	 * 发件时间
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_time1)
	private TextView tv_time1;
	/**
	 * 收件人地址
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_end_addr)
	private TextView tv_end_addr;
	/**
	 * 提交评价
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_sumbit, click = "tv_sumbit")
	private TextView tv_sumbit;

	/**
	 * 抽奖获得金额
	 */
	@ViewInject(id = R.id.pay_evaluate_tv_award_suceess_money)
	private TextView tv_award_suceess_money;
	/**
	 * 抽奖结果布局
	 */
	@ViewInject(id = R.id.pay_evaluace_rl_succsee)
	private RelativeLayout rl_succsee;

	/**
	 * 快递员接单时间
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_time2)
	private TextView tv_time2;

	/**
	 * 抽奖
	 */
	@ViewInject(id = R.id.pay_evaluace_tv_award, click = "tv_award")
	private TextView tv_award;

	@ViewInject(id = R.id.pay_evaluace_lcsv_start1)
	private LayoutProductCommentStartView lcsv_start1;
	/**
	 * 评价服务星星个数
	 */
	private int Score;

	/**
	 * 订单编号
	 */
	private String OrderId;
	/**
	 * 是否评价0未评价1已评价
	 */
	private int shear;
	// 声明姓名，电话
	private String username, usernumber;
	/**
	 * Total运费 Deduction余额抵扣Cash还需支付userDeduction可用余额
	 */
	private float Total, Deduction, Cash, userDeduction;
	/**
	 * 是否使用余额0，1
	 */
	private int userBalance;
	/**
	 * 是否付款成功
	 */
	private String Key_Msg2;
	private float drawBalance;
	/**
	 * 运单号
	 */
	private String WayBillNo;
	/**
	 * 运费编号
	 */
	private String CustomerBudgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_evaluate);
		lcsv_start1.setStar();
		lcsv_start1.setStartNum(5, callBack2);
		lcsv_start1.setOpenChange(true);
		Score = 5;
//		userBalance = 1;
		tv_also_pay.setText("0.0");
		Key_Msg2 = "no";
		OrderId = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		shear = getIntent().getIntExtra(ExtraKeys.Key_Msg2, 0);
		WayBillNo = null;
		et_single_number.setText(null);
		if (0 != shear) {
			lcsv_start1.setStartNum(shear);
			lcsv_start1.setOpenChange(false);
			tv_sumbit.setVisibility(View.GONE);
		} else {
			tv_sumbit.setVisibility(View.VISIBLE);
		}
		userDeduction = 0;
		tv_credit_available.setText(String.format(
				getString(R.string.pay_evaluate_tv_credit_available),
				userDeduction));
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetCustomerBalance);
		et_balance.addTextChangedListener(watcher);
		et_employ_balance.addTextChangedListener(watcher2);
		et_employ_balance.setOnFocusChangeListener(changeListener);
		et_balance.setOnFocusChangeListener(changeListener);
		et_employ_balance.addTextChangedListener(ll_credit_availableWatcher);
	}

	/**
	 * TODO 获得发件人余额
	 */
	private Runnable GetCustomerBalance = new Runnable() {

		@Override
		public void run() {
			// {'OrderId':8}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getMyApplication().getUserId());
			getData.doPost(callBack, GetDataConfing.ip, param,
					"GetCustomerBalance", 102);
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
					"CustomerRateOrderCourier", 0);
		}
	};
	/**
	 * TODO 提交运费
	 */
	private Runnable CustomerSubmitOrderFreight = new Runnable() {

		@Override
		public void run() {
			// {'CustomerId':'1','Total':'10','Deduction':'5','Cash':'5',
			// 'ReceivePhone':'15317090401','OrderId':'10','CurrentPhone':'15317090401'}

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("OrderId", OrderId);
			param.put("ReceivePhone", et_phone.getText().toString());
			param.put("CurrentPhone", getMyApplication().getUserPhone());
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("Total", Total);
			param.put("Deduction", Deduction);
			param.put("WayBillNo", WayBillNo);
			param.put("Cash", Cash);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"CustomerSubmitOrderFreight", 2);
		}
	};

	/**
	 * TODO 抽奖
	 */
	private Runnable GetDrawByCustomerId = new Runnable() {

		@Override
		public void run() {

			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("CustomerId", getMyApplication().getUserId());
			param.put("CustomerBudgetId", CustomerBudgetId);
			getData.doPost(callBack, GetDataConfing.ip, param,
					"GetDrawByCustomerId", 3);
		}
	};

	/**
	 * TODO 获取数据回调
	 */
	private ResponseCallBack callBack = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			sendTime = 0;
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
						lcsv_start1.setStartNum(Score);
						lcsv_start1.setOpenChange(false);
						shear = 1;
						tv_sumbit.setVisibility(View.GONE);
					} else if (2 == what) {
						Key_Msg2 = "ok";
						CustomerBudgetId = data_.getStringNoNull("Result");
						refresh();
					} else if (3 == what) {
						// {\"Result\":{\"Balance\":
						tv_award.setEnabled(true);
						drawBalance = data_.getJsonMap("Result").getFloat(
								"Balance");
						rl_succsee.setVisibility(View.VISIBLE);
						tv_award_suceess_money
								.setText(String
										.format(getString(R.string.pay_evaluate_tv_award_suceess_money),
												drawBalance + ""));
						tv_award.setVisibility(View.GONE);
					} else if (102 == what) {
						// "Result\":{\"Balance\":1006.00
						userDeduction = data_.getJsonMap("Result").getFloat(
								"Balance");
						// userDeduction = 0;
						Log.e(TAG, "userDeduction=" + userDeduction);
						if (0.0 < userDeduction) {
							Log.e(TAG, "userDeduction0.0 < userDeduction=");
							iv_credit_available
									.setImageResource(R.drawable.pay_evaluace_iv);
						} else {
							Log.e(TAG, "userDeduction=");
							userBalance = 0;
							iv_credit_available
									.setImageResource(R.drawable.pay_evaluace_iv_no);
						}
						tv_credit_available
								.setText(String
										.format(getString(R.string.pay_evaluate_tv_credit_available),
												userDeduction));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
			// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			username = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				usernumber = phone
						.getString(
								phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
						.replaceAll(" ", "");
				et_phone.setText(usernumber);
				setEdit(et_phone);
			}

		}
	}

	/**
	 * @Title: ll_credit_available
	 * @Description: TODO(方法的作用：选择使用余额)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void ll_credit_available(View v) {
		if (0.0 < userDeduction) {
			et_employ_balance.setEnabled(true);
		} else {
			toast.showToast(getString(R.string.my_balance_tv_toast_no));
			et_employ_balance.setEnabled(false);
			return;
		}
		userBalance++;
		if (0 == userBalance % 2) {

			if (!isTextEmpty(et_employ_balance.getText().toString().trim())) {
				Deduction = Float.valueOf(et_employ_balance.getText()
						.toString().trim());
				Log.e(TAG, "Total=" + Total + ";Deduction=" + Deduction);
				if (userDeduction >= Deduction) {
					if (Total >= Deduction) {
						Cash = Total - Deduction;
					} else {

						Cash = 0;
					}
				} else {
					if (Total >= Deduction) {
						Cash = Total - Deduction;
					} else {

						Cash = 0;
					}
				}
			} else {
				Deduction = 0;
				Cash = Total;
			}
			iv_credit_available.setImageResource(R.drawable.pay_evaluace_iv);
			int et_balance_value = Integer.valueOf(et_balance.getText().toString().trim());
			et_employ_balance.setText(((et_balance_value > userDeduction) ? userDeduction:et_balance_value)+"");
			et_employ_balance.setEnabled(true);
		} else {
			Deduction = 0;
			Cash = Total;
			iv_credit_available.setImageResource(R.drawable.pay_evaluace_iv_no);
			et_employ_balance.setText(null);
			et_employ_balance.setEnabled(false);
		}
		tv_also_pay.setText("" + Cash);
		
		
		
	}

	private int sendTime;

	/**
	 * @Title: tv_send
	 * @Description: TODO(方法的作用：确认付款)
	 * @param c
	 *            void 返回类型
	 * @throws
	 */
	public void tv_send(View c) {
		if (1 == sendTime) {
			return;
		}
		sendTime = 1;
		if (isTextEmpty(et_balance.getText().toString().trim())) {
			toast.showToast(getString(R.string.pay_evaluate_tosat_balance));
			return;
		}
		if (0 == userBalance % 2) {
			if (isTextEmpty(et_employ_balance.getText().toString().trim())) {
				toast.showToast(getString(R.string.pay_evaluate_tosat_deduction));
				return;
			} else {
				Deduction = Float.valueOf(et_employ_balance.getText()
						.toString().trim());
				if (0.0 < Deduction) {
					if (Deduction > userDeduction) {
						toast.showToast(getString(R.string.pay_evaluate_tosat_deduction_));
						return;
					}
					if (Total < Deduction) {
						toast.showToast(getString(R.string.pay_evaluate_tosat_deduction_1));
						return;
					}
				} else {
					toast.showToast(getString(R.string.pay_evaluate_tosat_deduction));
					return;
				}
			}
		}
		if (!isTextEmpty(et_phone.getText().toString().trim())) {
			if (!StringUtil.isMobileNO(et_phone.getText().toString().trim())) {
				toast.showToast(getString(R.string.user_login_toast_phone));
				return;
			}

		}
		WayBillNo = et_single_number.getText().toString().trim();
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerSubmitOrderFreight);
	}

	private void refresh() {
		ll_balance.setVisibility(View.GONE);
		_sclv.setVisibility(View.VISIBLE);
		List<JsonMap<String, Object>> data_OsList = getMyApplication()
				.getData_orderDetaile();
		getMyApplication().setData_orderDetaile(data_OsList);
		if (2 <= data_OsList.size()) {
			tv_type.setText(data_OsList.get(0).getStringNoNull("Status"));
			tv_start_addr
					.setText(data_OsList.get(0).getStringNoNull("Address"));
			tv_time1.setText(data_OsList.get(0).getStringNoNull("DateTime"));
			tv_end_addr.setText(data_OsList.get(1).getStringNoNull("Address"));
			tv_time2.setText(data_OsList.get(1).getStringNoNull("DateTime"));
		}
	}

	private void setEdit(EditText editText) {
		CharSequence text = editText.getText();
		// Debug.asserts(text instanceof Spannable);
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	public void iv_top_left(View v) {
		if ("ok".equals(Key_Msg2)) {
			finish1();
		} else {
			finish();
		}

	}

	/**
	 * @Title: tv_award
	 * @Description: TODO(方法的作用：抽奖)
	 * @param c
	 *            void 返回类型
	 * @throws
	 */
	public void tv_award(View c) {
		tv_award.setEnabled(false);
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetDrawByCustomerId);

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
				ContactsContract.Contacts.CONTENT_URI), 0);
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

	/**
	 * @Title: iv_clear
	 * @Description: TODO(方法的作用：清除运费)
	 * @param v
	 *            void 返回类型
	 * @throws
	 */
	public void iv_clear(View v) {
		et_balance.setText(null);
		Deduction = 0;
		Total = 0;
		Cash = 0;
		et_employ_balance.setText(null);
		tv_also_pay.setText(Cash + "");

	}

	public void ll_share(View v) {

	}

	public void tv_sumbit(View v) {
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerRateOrderCourier);
	}

	public void finish1() {
		if ("ok".equals(Key_Msg2)) {
			Intent intent = getIntent();
			if (1 == shear) {
				intent.putExtra(ExtraKeys.Key_Msg1, "-1");
			} else {
				intent.putExtra(ExtraKeys.Key_Msg1, "1");
			}
			intent.putExtra(ExtraKeys.Key_Msg2, Key_Msg2);
			setResult(RESULT_OK, intent);
		}
		Log.e(TAG, "Key_Msg2:" + Key_Msg2);
		super.finish();
	};

	/**
	 * 返回监听
	 */
	@Override
	public void onBackPressed() {
		finish1();
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!isTextEmpty(s.toString())) {
				iv_clear.setVisibility(View.VISIBLE);
			} else {
				iv_clear.setVisibility(View.GONE);
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
	
	private TextWatcher ll_credit_availableWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			if (0 == userBalance % 2) {
				if (!isTextEmpty(s.toString().trim())) {
					Deduction = Float.valueOf(s.toString().trim());
					Log.e(TAG, "Total=" + Total + ";Deduction=" + Deduction);
					if (userDeduction >= Deduction) {
						if (Total >= Deduction) {
							Cash = Total - Deduction;
						} else {
							Deduction = Deduction - Total;
							Cash = 0;
						}
					} else {
						if (Total >= userDeduction) {
							Cash = Total - userDeduction;
						} else {
							Deduction = userDeduction - Total;
							Cash = 0;
						}
					}
				} else {
					Cash = Total;
				}
				tv_also_pay.setText("" + Cash);
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 监察运费输入框
	 */
	private TextWatcher watcher2 = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (0 == userBalance % 2) {
				if (!isTextEmpty(s.toString().trim())) {
					Deduction = Float.valueOf(s.toString().trim());
					Log.e(TAG, "Total=" + Total + ";Deduction=" + Deduction);
					if (userDeduction >= Deduction) {
						if (Total >= Deduction) {
							Cash = Total - Deduction;
						} else {
							Deduction = Deduction - Total;
							Cash = 0;
						}
					} else {
						if (Total >= userDeduction) {
							Cash = Total - userDeduction;
						} else {
							Deduction = userDeduction - Total;
							Cash = 0;
						}
					}
				} else {
					Cash = Total;
				}
				tv_also_pay.setText("" + Cash);
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
	private OnFocusChangeListener changeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.pay_evaluace_et_balance:
				if (isTextEmpty(et_balance.getText().toString().trim())) {
					toast.showToast(getString(R.string.pay_evaluate_tosat_balance));
					break;
				}
				Total = Float.valueOf(et_balance.getText().toString().trim());
				if (0 < userDeduction) {
					int aa;
					Deduction = Total > userDeduction ? userDeduction : Total;
					aa = (int) Deduction;
					et_employ_balance.setText(aa + "");
					setEdit(et_employ_balance);
				} else {
					Deduction = 0;
				}

				Cash = Total - Deduction;
				tv_also_pay.setText("" + Cash);
				break;

			case R.id.pay_evaluace_et_employ_balance:
				// if (0 == userBalance % 2) {
				// changeEmployBalance();
				// }
				break;
			}

		}
	};

	/**
	 * @Title: changeEmployBalance
	 * @Description: TODO(方法的作用：检测抵用余额情况) void 返回类型
	 * @throws
	 */
	private void changeEmployBalance() {
		if (isTextEmpty(et_employ_balance.getText().toString().trim())) {
			toast.showToast(getString(R.string.pay_evaluate_tosat_deduction));
		} else {
			Deduction = Float.valueOf(et_employ_balance.getText().toString()
					.trim());
			if (0.0 < Deduction) {
				if (Deduction < userDeduction) {
					toast.showToast(getString(R.string.pay_evaluate_tosat_deduction_));
				}
				if (Total < Deduction) {
					toast.showToast(getString(R.string.pay_evaluate_tosat_deduction_1));
				}
			} else {
				toast.showToast(getString(R.string.pay_evaluate_tosat_deduction));
			}
		}
	}

	MyBroadcastReceiver mReceiver;

	private int onStartTime;

	protected void onStart() {
		// TODO 重写onStart方法 注册用于接收Service传送的广播
		Log.e(TAG, "mReceiver:" + mReceiver);
		if (null == mReceiver) {
			mReceiver = new MyBroadcastReceiver();
		}
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction(OrderId);
		registerReceiver(mReceiver, filter);// 注册Broadcast Receiver
		super.onStart();
	}

	@Override
	protected void onResume() {
		if (!isTextEmpty(getMyApplication().getSearchKey())) {
			WayBillNo = getMyApplication().getSearchKey();
			et_single_number.setText(WayBillNo);
			getMyApplication().setSearchKey(null);
		}
		if (0 == onStartTime) {
			onStartTime++;
			WayBillNo = null;
			et_single_number.setText(WayBillNo);
			Log.e(TAG, "mReceiveronResume:" + WayBillNo);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO onDestroy
		// 退出时销毁定位
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
	public class MyBroadcastReceiver extends BroadcastReceiver {

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
					WayBillNo = null;
					et_single_number.setText(WayBillNo);
					setEdit(et_employ_balance);
				}

			} catch (Exception e) {
				Log.v("test", "bundle:" + bundle);
				Log.v("test", "Exception:" + e.toString());
			}
		}
	}

}
