package com.striveen.express.activity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.striveen.express.AymActivity;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.R;
import com.striveen.express.SmsReceiver;
import com.striveen.express.net.GetData;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.sql.DBManager;
import com.striveen.express.util.Confing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.util.StringUtil;
import com.striveen.express.util.SubstituteEncrypt;
import com.striveen.express.view.ViewInject;

/**
 * 登陆
 * 
 * @author Fei
 * 
 */
public class UserLoginActivity extends AymActivity {

	private final String TAG = "UserLoginActivity";
	/**
	 * 手机号
	 */
	@ViewInject(id = R.id.user_login_et_phone)
	private EditText et_phone;
	/**
	 * 验证码
	 */
	@ViewInject(id = R.id.user_login_et_verification)
	private EditText et_verification;
	/**
	 * 登录
	 */
	@ViewInject(id = R.id.user_login_bt_login, click = "bt_login")
	private Button bt_login;
	/**
	 * 一键清除手机号码
	 */
	@ViewInject(id = R.id.user_login_iv_top, click = "iv_top")
	private ImageView iv_top;
	/**
	 * 获取验证码
	 */
	@ViewInject(id = R.id.user_login_bt_verification, click = "bt_verification")
	private TextView bt_verification;

	/**
	 * 定时刷新的定时器
	 */
	private Timer timer;
	private FulshTime fulshTime;
	/**
	 * 验证码还可以使用的秒数
	 */
	private int yanzhenmakeyongmiao;
	/**
	 * 本机号码
	 */
	private String phone;
	GetData getData;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 手机验证码
	 */
	private String code;

	Locate getLocate;
	LocationClient lClient;
	/**
	 * 定位地址
	 */
	private String locationAddr = "";
	/**
	 * latitude纬度
	 */
	private double latitude = -1.0;
	/**
	 * longitude经度
	 */
	private double longitude = -1.0;
	/**
	 * 手机唯一标识
	 */
	private String IMEI = "";
	/**
	 * 连接网络类型
	 */
	private String Network = "";
	private String phoneIp = "";
	private String XinGeToken;
	private DBManager myDbManager;
	private String customerId;
	private JsonMap<String, Object> data_login;
	/**
	 * 账号保存天数
	 */
	private long data_time = -1;
	/**
	 * sd卡剩余容量
	 */
	private long SDSize;
	SharedPreferences sp_Token;
	
	MessageCodeReceiver receiver = new MessageCodeReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		initActivityTitle(getString(R.string.user_login_top_title));

		IMEI = StringUtil.getSubscriberId(UserLoginActivity.this);
		Network = StringUtil.detectionMesh(UserLoginActivity.this);
		if ("1".equals(Network)) {
			phoneIp = StringUtil.intToIp(this);
		} else {
			phoneIp = "0.0.0";
		}
		customerId = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		sp_Token = getSharedPreferences(Confing.SP_SaveUserInfo,
				Context.MODE_APPEND);
		Log.d("", "phoneIp:" + phoneIp);
		// Log.d("", "Network:" + Network);
		getLocate = Locate.getInstance();
		lClient = new LocationClient(this);
		getLocate.loadAddre(callBack, lClient);
		et_phone.addTextChangedListener(watcher);
		phone = StringUtil.getPhoneNumber(UserLoginActivity.this);
		// toast.showToast(phone);
		if (!TextUtils.isEmpty(phone)) {
			if (11 < phone.length()) {
				phone = phone.substring(phone.length() - 11);
			}
			et_phone.setText(phone);
		}
		if (!"-1".equals(customerId)) {
			if (!TextUtils.isEmpty(customerId)) {
				phone = SubstituteEncrypt.getInstance().decrypt(customerId);
			}
			et_phone.setText(phone);
		}
		if ("2".equals(customerId)) {
			phone = getIntent().getStringExtra(ExtraKeys.Key_Msg2);
			Log.e(TAG, "phone:" + phone);
			et_phone.setText(phone);
		}
		if (!TextUtils.isEmpty(et_phone.getText().toString().trim())) {
			iv_top.setVisibility(View.VISIBLE);
		}
		if (StringUtil.judgeExpressDB() == -1) {
			SDSize = StringUtil.getCompareSize();
			if (300 <= SDSize) {
				load_Customer();
			} else {
				data_time = -2;
			}
		} else {
			load_Customer();
		}
		registerBoradcastReceiver();
		
		timer = new Timer();
		getData = new GetData(this);
		

	}
	
	private void registerBoradcastReceiver(){
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(SmsReceiver.MESSAGE_CODE_ACTION_NAME);
		//注册广播      
		registerReceiver(receiver, myIntentFilter);
	}


	private LocateCallBack callBack = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province, String City, String District) {
			latitude = latitude1;
			longitude = longitude1;
			locationAddr = str;
			if (null != lClient) {
				lClient.stop();
				lClient = null;
			}

		}
	};
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (TextUtils.isEmpty(et_phone.getText().toString())) {
				iv_top.setVisibility(View.GONE);
			} else {
				iv_top.setVisibility(View.VISIBLE);
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

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		if (null != timer) {
			timer.cancel();
		}
		super.onDestroy();
	}

	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void bt_login(View view) {
		mobile = et_phone.getText().toString().trim();
		code = et_verification.getText().toString().trim();
		if (!StringUtil.isMobileNO(mobile)) {
			toast.showToast(getString(R.string.user_login_toast_phone));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			toast.showToast(getString(R.string.user_login_toast_verification));
			return;
		}
		if (null != myDbManager) {
			if ("2".equals(customerId)) {
				if (!TextUtils.isEmpty(phone)) {
					myDbManager.delete_Customer(SubstituteEncrypt.getInstance()
							.encrypt(phone));
				}
			} else {
				myDbManager.delete_Customer(data_login
						.getStringNoNull("mobile"));
			}

		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(CustomerLogin);
		// getMyApplication().setUserId("53");
		// startActivity(new Intent(UserLoginActivity.this,
		// IndexActivity.class));
		// UserLoginActivity.this.finish();
	}

	/**
	 * 清除输入框内容
	 * 
	 * @param view
	 */
	public void iv_top(View v) {
		et_phone.setText(null);
		iv_top.setVisibility(View.INVISIBLE);
	}

	/**
	 * 获取验证
	 * 
	 * @param view
	 */
	public void bt_verification(View v) {
		mobile = et_phone.getText().toString().trim();
		if (!StringUtil.isMobileNO(mobile)) {
			toast.showToast(getString(R.string.user_login_toast_phone));
			return;
		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(GetPhoneCodeByPhone);
	}

	/**
	 * 获取手机验证
	 */
	public Runnable GetPhoneCodeByPhone = new Runnable() {

		@Override
		public void run() {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("Phone", mobile);
			getData.doPost(callBack2, GetDataConfing.ip, param,
					"GetPhoneCodeByPhone", 10);
		}
	};
	/**
	 * 登陆
	 */
	public Runnable CustomerLogin = new Runnable() {

		@Override
		public void run() {

			// {'Phone':'15317090401',Code:'123',AppType:'1',Device:'设备',UserType:'0',
			// LoginIp:'192.168.1.1',LoginNetwork:'0',Address:'宜山路1718路',HistoryX:'121.396470',
			// HistoryY:'31.174560',IMEI:'faksfkafks',AppVersion:'1.0'}
			XinGeToken = sp_Token.getString(Confing.SP_SaveUserInfo_Token, "");
			if (TextUtils.isEmpty(XinGeToken) || "0".equals(XinGeToken)) {
				XinGeToken = "";
			}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("Phone", mobile);
			param.put("Code", code);
			param.put("AppType", "1");
			param.put("Device", "0");
			param.put("UserType", "0");// 0 发件人 1收件人
			param.put("LoginIp", phoneIp);// 登录IP
			param.put("LoginNetwork", Network);// 0 WIFI 1 3G 2 2G
			param.put("Address", locationAddr);
			param.put("HistoryX", longitude);
			param.put("HistoryY", latitude);
			param.put("IMEI", IMEI);// 手机唯一标识
			param.put("AppVersion", getMyApplication().getAppVersion());// 客户端版本号
			param.put("XinGeToken", XinGeToken);
			getData.doPost(callBack2, GetDataConfing.ip, param, "Login", 11);
		}
	};
	/**
	 * 获取数据回调
	 */
	ResponseCallBack callBack2 = new ResponseCallBack() {

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
						if (fulshTime != null) {
							fulshTime.cancel(); // 将原任务从队列中移除
						}
						bt_verification.setEnabled(false);
						fulshTime = new FulshTime();
						yanzhenmakeyongmiao = 59;
						timer.schedule(fulshTime, 1000, 1000);
						// et_verification
						// .setText(data_.getStringNoNull("Result"));

					} else if (11 == what) {
						if (null != lClient) {
							lClient.stop();
						}
						JsonMap<String, Object> map = new JsonMap<String, Object>();
						map.put("mobile", SubstituteEncrypt.getInstance()
								.encrypt(mobile));
						map.put("code", SubstituteEncrypt.getInstance()
								.encrypt(code));
						map.put("customerId", SubstituteEncrypt.getInstance()
								.encrypt(data_.getStringNoNull("Result")));
						map.put("data", StringUtil.getData_yyyy_MM_dd());
						Log.e("", "map:" + map);
						if (null != myDbManager && -2 != data_time) {
							myDbManager.delete_Customer(customerId);
						}
						if (null != myDbManager) {
							myDbManager.insert_Customer(map);
						}
						// toast.showToast("登陆成功");
						SharedPreferences sp = getSharedPreferences(
								Confing.SP_SaveUserInfo, Context.MODE_APPEND);
						sp.edit()
								.putString(Confing.SP_SaveUserInfo_Id,
										data_.getStringNoNull("Result"))
								.commit();
						Log.e("", "mobile:" + mobile);
						getMyApplication().setUserPhone(mobile);
						// if ("2".equals(customerId)) {
						// Intent intent = new Intent(UserLoginActivity.this,
						// IndexActivity.class);
						// startActivity(intent);
						// }

						Intent intent = getIntent();
						setResult(RESULT_OK, intent);
						UserLoginActivity.this.finish();
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}

		}
	};

	private final int what_flushTime = 3;

	/**
	 * 刷新30秒发送的展示的定时
	 * 
	 */
	private class FulshTime extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(what_flushTime);
		}

	}

	/**
	 * 刷新30秒发送的展示的执行
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			yanzhenmakeyongmiao--;
			if (yanzhenmakeyongmiao <= 0) {
				bt_verification.setEnabled(true);
				bt_verification
						.setText(getString(R.string.user_tv_verification));
				return;
			}
			bt_verification.setText(getString(R.string.user_login_show_time)
					+ yanzhenmakeyongmiao);
		};
	};

	/**
	 * 加载本地数据
	 */
	private void load_Customer() {
		myDbManager = DBManager.getInstance(this);
		myDbManager.createCustomerTable();
		data_login = myDbManager.get_Customer();

		if (null != data_login) {
			Log.e("", "data_login:" + data_login);
			if (TextUtils.isEmpty(data_login.getStringNoNull("mobile"))) {
				return;
			}
			if (TextUtils.isEmpty(data_login.getStringNoNull("data"))) {
				return;
			}
			if (TextUtils.isEmpty(data_login.getStringNoNull("customerId"))) {
				return;
			}
			data_time = StringUtil.compare_date(
					data_login.getStringNoNull("data"),
					StringUtil.getData_yyyy_MM_dd());
			phone = SubstituteEncrypt.getInstance().decrypt(
					data_login.getStringNoNull("mobile"));
			et_phone.setText(phone);
		}

	}

	/**
	 * 重新定义 退出事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			UserLoginActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class MessageCodeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equalsIgnoreCase(SmsReceiver.MESSAGE_CODE_ACTION_NAME)) {
					et_verification.setText(intent.getExtras().getString("message"));
				}
		}
		
	}
	
}
