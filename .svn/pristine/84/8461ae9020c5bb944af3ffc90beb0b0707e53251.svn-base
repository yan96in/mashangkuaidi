package com.striveen.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.view.ViewInject;

/**
 * 关于我们
 * 
 * @author Fei
 * 
 */
public class AboutUsActivity extends MyActivity {
	/**
	 * 版本号
	 */
	@ViewInject(id = R.id.about_us_tv_vision)
	private TextView tv_vision;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		tv_vision.setText("V " + getMyApplication().getVersionName());
	}

	/**
	 * 版本
	 * 
	 * @param v
	 */
	public void rl_vision(View v) {

	}

	/**
	 * 服务条款
	 * 
	 * @param v
	 */
	public void rl_service_item(View v) {
		Intent intent = new Intent(AboutUsActivity.this,
				ServiceTermsActivity.class);
		intent.putExtra(ExtraKeys.Key_Msg1, "2");
		startActivity(intent);
	}

	/**
	 * 联系我们
	 * 
	 * @param v
	 */
	public void rl_connect_us(View v) {
		startActivity(new Intent(AboutUsActivity.this, ConnectUsActivity.class));
	}

	public void iv_top_left(View v) {
		AboutUsActivity.this.finish();
	}
}
