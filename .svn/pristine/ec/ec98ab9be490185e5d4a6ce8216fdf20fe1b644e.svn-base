package com.striveen.express.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.striveen.express.Locate;
import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.sql.DBManager;
import com.striveen.express.util.Confing;
import com.striveen.express.util.StringUtil;
import com.striveen.express.util.SubstituteEncrypt;
import com.striveen.express.view.ViewInject;

/**
 * 设置
 * 
 * @author Fei
 * 
 */
public class UserSettingActivity extends MyActivity {

	@ViewInject(id = R.id.user_setting_bottom_rl, click = "bottom_rl")
	private RelativeLayout bottom_rl;
	@ViewInject(id = R.id.user_order_iv_top_left, click = "iv_top_left")
	private ImageView iv_top_left;
	@ViewInject(id = R.id.user_setting_rl_1, click = "setting_rl_1")
	private RelativeLayout setting_rl_1;
	@ViewInject(id = R.id.user_setting_rl_2, click = "setting_rl_2")
	private RelativeLayout setting_rl_2;
	@ViewInject(id = R.id.user_setting_rl_3, click = "setting_rl_3")
	private RelativeLayout setting_rl_3;
	@ViewInject(id = R.id.user_setting_rl_4, click = "setting_rl_4")
	private RelativeLayout setting_rl_4;
	@ViewInject(id = R.id.user_setting_rl_5, click = "setting_rl_5")
	private RelativeLayout setting_rl_5;
	@ViewInject(id = R.id.user_setting_rl_6, click = "setting_rl_6")
	private RelativeLayout setting_rl_6;
	DBManager daDbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		Locate.getInstance().addData_activity(this);
		loadUsedAddress();
	}

	/**
	 * 退出登陆
	 * 
	 * @param view
	 */
	public void bottom_rl(View view) {
		cancelOrder();
	}

	/**
	 * 退出登陆
	 */
	private void cancelOrder() {
		Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.main_toast_tishi));
		builder.setMessage("确认退出登录？");
		builder.setCancelable(false);
		builder.setPositiveButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});

		builder.setNegativeButton(getString(R.string.Ensure),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						if (null != daDbManager) {
							daDbManager.update_Customer(
									SubstituteEncrypt.getInstance().encrypt(
											"11"),
									SubstituteEncrypt.getInstance().encrypt(
											getMyApplication().getUserId()));
						}

						SharedPreferences sp = getSharedPreferences(
								Confing.SP_SaveUserInfo, Context.MODE_APPEND);
						sp.edit().putString(Confing.SP_SaveUserInfo_Id, "")
								.commit();
						// Intent intent = new Intent(UserSettingActivity.this,
						// UserLoginActivity.class);
						// intent.putExtra(ExtraKeys.Key_Msg1, "2");
						// intent.putExtra(ExtraKeys.Key_Msg2,
						// getMyApplication()
						// .getUserPhone());
						// startActivity(intent);
						// Locate.getInstance().exit();
						UserSettingActivity.this.finish();
					}
				});
		builder.show();
	}

	/**
	 * 顶部返回
	 * 
	 * @param view
	 */
	public void iv_top_left(View v) {
		UserSettingActivity.this.finish();
	}

	/**
	 * 常用地址
	 * 
	 * @param v
	 */
	public void setting_rl_1(View v) {
		if (TextUtils.isEmpty(getMyApplication().getUserId())) {
			toast.showToast(getString(R.string.all_toast_afresh_login));
			startActivity(new Intent(UserSettingActivity.this,
					UserLoginActivity.class));
			return;
		}
		startActivity(new Intent(UserSettingActivity.this,
				CommonAddressActivity.class));
	}

	/**
	 * 快递喜好
	 * 
	 * @param v
	 */
	public void rl_preferences(View v) {
		startActivity(new Intent(UserSettingActivity.this,
				ExpressPreferencesActivty.class));
	}

	/**
	 * 账号绑定
	 * 
	 * @param v
	 */
	public void setting_rl_2(View v) {
	}

	/**
	 * 通知开关
	 * 
	 * @param v
	 */

	public void setting_rl_3(View v) {
	}

	/**
	 * 快递指南
	 * 
	 * @param v
	 */
	public void setting_rl_4(View v) {
		startActivity(new Intent(UserSettingActivity.this,
				CourierGuideActivity.class));
	}

	/**
	 * 意见反馈
	 * 
	 * @param v
	 */
	public void setting_rl_5(View v) {
		if (TextUtils.isEmpty(getMyApplication().getUserId())) {
			toast.showToast(getString(R.string.all_toast_afresh_login));
			startActivity(new Intent(UserSettingActivity.this,
					UserLoginActivity.class));
			return;
		}
		startActivity(new Intent(UserSettingActivity.this,
				FeedBackActivity.class));
	}

	/**
	 * 关于我们
	 * 
	 * @param v
	 */
	public void setting_rl_6(View v) {
		startActivity(new Intent(UserSettingActivity.this,
				AboutUsActivity.class));
	}

	/**
	 * 加载本地地址信息
	 */
	private void loadUsedAddress() {
		if (StringUtil.judgeExpressDB() == -1) {
			if (300 <= StringUtil.getCompareSize()) {
				daDbManager = DBManager.getInstance(this);
			}
		} else {
			daDbManager = DBManager.getInstance(this);
		}
	}
}
