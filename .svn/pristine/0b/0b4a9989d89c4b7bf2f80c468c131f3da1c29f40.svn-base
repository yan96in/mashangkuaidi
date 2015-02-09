package com.striveen.express.activity;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.striveen.express.R;

/**
 * 主界面，百度定位
 * 
 * @author Fei
 * 
 * 没用
 * 
 */
public class Index4Activity extends Activity {

	MenuDrawer myMenuDrawer;
	/**
	 * 手柄点击
	 * 
	 */
	private ImageView iv_top_handle;

	private TextView tv_mylocate;
	/**
	 * 录音
	 * 
	 */
	private CheckBox cb_bottom_left;
	/**
	 * 预约
	 * 
	 */
	private ImageView iv_bottom_right;
	/**
	 * 判断是语音还是文字
	 */
	private boolean isChecked;
	private TextView tv_bottom_address;
	private RelativeLayout rl_recording;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		myMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		myMenuDrawer.setContentView(R.layout.activity_index);
		myMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);

		myMenuDrawer.setMenuView(R.layout.activity_index_drawer_left);// 隐藏菜单
		// 隐去标题栏（应用程序的名字）
		isChecked = true;
		init();
	}

	private void init() {
		cb_bottom_left = (CheckBox) findViewById(R.id.index_cb_bottom_left);
		iv_top_handle = (ImageView) findViewById(R.id.index_iv_top_handle);
		iv_bottom_right = (ImageView) findViewById(R.id.index_iv_bottom_right);
		tv_mylocate = (TextView) findViewById(R.id.index_tv_mylocate);
		tv_bottom_address = (TextView) findViewById(R.id.index_tv_bottom_address);

		rl_recording = (RelativeLayout) findViewById(R.id.index_rl_recording);

		iv_top_handle.setOnClickListener(onClickListener);
		iv_bottom_right.setOnClickListener(onClickListener);
		cb_bottom_left
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean Checked) {
						isChecked = Checked;
						check();
					}
				});
		String sFinal1 = String.format(getString(R.string.index_tv_my_locate),
				"花木路大唐盛世花园");
		check();
		tv_mylocate.setText(sFinal1);
	}

	private void check() {
		if (isChecked) {
			tv_bottom_address.setText(null);
			tv_bottom_address.setBackgroundResource(R.drawable.index_bt_voice);
			// tv_bottom_address.setOnTouchListener(onTouchListener);
		} else {
			tv_bottom_address.setText(getString(R.string.index_tv_address));
			tv_bottom_address
					.setBackgroundResource(R.drawable.index_tv_bottom_address);
		}
		tv_bottom_address.setOnClickListener(onClickListener);
	}

	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				rl_recording.setVisibility(View.GONE);
				check();
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_recording.setVisibility(View.VISIBLE);
				tv_bottom_address
						.setText(getString(R.string.index_tv_recording));
				tv_bottom_address
						.setBackgroundResource(R.drawable.index_iv_recording1);
			}
			return false;
		}
	};
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.index_iv_top_handle:
				myMenuDrawer.openMenu();
				break;
			case R.id.index_iv_bottom_right:
				startActivity(new Intent(Index4Activity.this,
						ReservationActivity.class));
				break;
			case R.id.index_tv_bottom_address:
				if (isChecked) {
					startActivity(new Intent(Index4Activity.this,
							Index1Activity.class));
				} else {
					startActivity(new Intent(Index4Activity.this,
							SenderAddressActivity.class));
				}

				break;
			}

		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		// MyApplication.backPressed(Practice_Test.this);
		final int drawerState = myMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			myMenuDrawer.closeMenu();
			return;
		}
		// super.onBackPressed();
	}
}
