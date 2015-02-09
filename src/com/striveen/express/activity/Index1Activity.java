package com.striveen.express.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.simonvt.menudrawer.MenuDrawer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.buihha.audiorecorder.Mp3Recorder;
import com.striveen.express.Locate;
import com.striveen.express.Locate.LocateCallBack;
import com.striveen.express.R;
import com.striveen.express.util.Confing;
import com.striveen.express.util.ExtraKeys;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.SdcardHelper;
import com.striveen.express.view.RoundProgressBar;

/**
 * 主界面，百度定位
 * 
 * @author Fei
 * 
 *         没用
 * 
 */
public class Index1Activity extends IndexAllActivity {

	private final String TAG = "Index1Activity";
	/**
	 * 百度地图BMapManager
	 */
	public BMapManager manager = null;
	/**
	 * 百度地图MapView
	 */
	private MapView myMapView = null;
	/**
	 * 快递员标识集合
	 */
	List<JsonMap<String, Object>> data_mark;
	/**
	 * 百度地图MapController 设置启用内置的缩放控件
	 */
	MapController mMapController;
	/**
	 * 百度地图LocationClient
	 */
	LocationClient locationClient;
	/**
	 * latitude纬度，longitude经度
	 */
	double latitude, longitude;
	MenuDrawer myMenuDrawer;
	/**
	 * 手柄点击
	 * 
	 */
	private ImageView iv_top_handle;
	/**
	 * 显示所在位置
	 */
	private TextView tv_mylocate;
	/**
	 * 将当前位置移动到屏幕中央
	 */
	private ImageView iv_map_locate;
	/**
	 * 地图上展示图片的数组
	 */
	private int[] mark_picture = { R.drawable.index_package,
			R.drawable.index_type_select, R.drawable.index_map_voice,
			R.drawable.index_map_marka, R.drawable.index_waiting_orders_icom };
	/**
	 * 底部录音
	 */
	private TextView tv_recording;
	/**
	 * 录音时间
	 */
	private TextView tv_recording_time;

	/**
	 * 底部 语音，预约布局
	 */
	private RelativeLayout index_bottom_rl;

	private LinearLayout ll_order;
	private LinearLayout ll_friend;
	private LinearLayout ll_setting;
	/**
	 * 判断是从哪个activity中跳转过来的
	 */
	private String checkIndex;
	private Locate myLocate;
	private RoundProgressBar index1_rpb;
	/**
	 * 录音时间
	 */
	private int progress = 0;
	/**
	 * 录音剩余时间
	 */
	private String progress1;
	/**
	 * 定时刷新的定时器
	 */
	private Timer timer;
	/**
	 * 验证码还可以使用的秒数
	 */
	private int yanzhenmakeyongmiao = 50;
	// private File myRecAudioFile;
	private File myRecAudioDir;
	// private MediaRecorder mMediaRecorder01;
	/**
	 * 是否停止录音
	 */
	// private boolean isStopRecord;
	/**
	 * 录音文件名
	 */
	// private String strTempFile;
	private SdcardHelper sdHelper;
	/**
	 * 录音Mp3
	 */
	private Mp3Recorder recorder;

	// http://developer.baidu.com/map/sdkandev-4.htm#col23 百度地图周边检索
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		myLocate = new Locate(callBack, getApplication());
		myLocate.SetBMapManager(manager);
		myMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		myMenuDrawer.setContentView(R.layout.activity_index1);
		myMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);

		myMenuDrawer.setMenuView(R.layout.activity_index_drawer_left);// 隐藏菜单
		// 隐去标题栏（应用程序的名字）
		initView();

		recorder = new Mp3Recorder();
		// strTempFile = getString(R.string.index1_voice_name);
		// strTempFile = "express_voice";
		timer = new Timer();
		Locate.getInstance().addData_activity(this);
		sdHelper = new SdcardHelper();

	}

	private void initView() {
		index1_rpb = (RoundProgressBar) findViewById(R.id.index1_rpb);
		iv_top_handle = (ImageView) findViewById(R.id.index1_iv_top_handle);
		iv_map_locate = (ImageView) findViewById(R.id.index1_iv_map_locate);

		tv_mylocate = (TextView) findViewById(R.id.index1_tv_mylocate);
		tv_recording = (TextView) findViewById(R.id.index1_tv_recording);
		tv_recording_time = (TextView) findViewById(R.id.index1_tv_time);

		ll_order = (LinearLayout) findViewById(R.id.index_left_ll_order);
		ll_friend = (LinearLayout) findViewById(R.id.index_left_ll_friend);
		ll_setting = (LinearLayout) findViewById(R.id.index_left_ll_setting);

		myMapView = (MapView) findViewById(R.id.index1_my_mapv);

		ll_order.setOnClickListener(onClickListener);
		ll_friend.setOnClickListener(onClickListener);
		ll_setting.setOnClickListener(onClickListener);

		myLocate.SetMap(myMapView);
		tv_mylocate.setText(String.format(
				getString(R.string.index_tv_my_locate),
				getString(R.string.index_tv_locate)));
		tv_recording.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					// 按下
					tv_recording
							.setText(getString(R.string.index_tv_recording));
					yanzhenmakeyongmiao = 50;
					timer.schedule(new FulshTime(), 1000, 1000);
					tv_recording_time.setText("00:" + yanzhenmakeyongmiao);
					recorder();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					tv_recording.setText(getString(R.string.index1_tv_voice));
					// 弹起
					stopRecorder();
					if (46 < yanzhenmakeyongmiao) {
						yanzhenmakeyongmiao = -2;
						tv_recording
								.setText(getString(R.string.index1_tv_voice));
						Toast.makeText(Index1Activity.this,
								getString(R.string.index1_toast_voice),
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(Index1Activity.this,
								Index2Activity.class);
						intent.putExtra(ExtraKeys.Key_Msg1, "0");
						intent.putExtra(ExtraKeys.Key_Msg2, getIntent()
								.getStringExtra(ExtraKeys.Key_Msg2));
						intent.putExtra(ExtraKeys.Key_Msg3, "a");
						startActivity(intent);
						Index1Activity.this.finish();
					}

				}
				return false;
			}
		});
	}

	/**
	 * 开始录音
	 */
	public void recorder() {

		try {
			if (!sdHelper.ExistSDCard()) {
				Toast.makeText(Index1Activity.this, R.string.nosdcard,
						Toast.LENGTH_SHORT).show();
				return;
			}
			myRecAudioDir = new File(Confing.voiceCache);
			if (!myRecAudioDir.exists()) {
				myRecAudioDir.mkdir();
			}
			recorder.startRecording();
			/* 建立录音档 */
			// myRecAudioFile = File.createTempFile(strTempFile, ".amr",
			// myRecAudioDir);
			// mMediaRecorder01 = new MediaRecorder();
			// /* 设定录音来源为麦克风 */
			// mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
			// mMediaRecorder01
			// .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			// mMediaRecorder01
			// .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			//
			// mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
			// mMediaRecorder01.prepare();
			// mMediaRecorder01.start();

			// isStopRecord = false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Start error");
		}

	}

	/**
	 * 停止录音
	 */
	private void stopRecorder() {
		try {
			recorder.stopRecording();
		} catch (IOException e) {
			Log.d(TAG, "Stop error");
		}
		// if (myRecAudioFile != null) {
		// /* 停止录音 */
		// if (mMediaRecorder01 != null) {
		// mMediaRecorder01.stop();
		// /* 将录音文件名给Adapter */
		// mMediaRecorder01.release();
		// mMediaRecorder01 = null;
		// isStopRecord = true;
		// }
		//
		// }
	}

	/**
	 * 地图定位回调
	 */

	private LocateCallBack callBack = new LocateCallBack() {

		@Override
		public void setText(Double latitude1, Double longitude1, String str,
				String Province, String City, String District) {
			latitude = latitude1;
			longitude = longitude1;
			iv_map_locate.setVisibility(0);
			tv_mylocate.setText(String.format(
					getString(R.string.index_tv_my_locate), str));
		}
	};
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 左边的
			case R.id.index_left_ll_order:
				startActivity(new Intent(Index1Activity.this,
						UserOrderActivity.class));
				myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_friend:
				myMenuDrawer.closeMenu();
				break;
			case R.id.index_left_ll_setting:
				startActivity(new Intent(Index1Activity.this,
						UserSettingActivity.class));
				myMenuDrawer.closeMenu();
				break;
			}

		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 返回监听
	 */
	@Override
	public void onBackPressed() {
		// MyApplication.backPressed(Practice_Test.this);
		final int drawerState = myMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			myMenuDrawer.closeMenu();
			return;
		} else {
			stopRecorder();
			Index1Activity.this.finish();
		}
		// super.onBackPressed();
	}

	/**
	 * 创建标识位置
	 */
	public void createMark(int arg0, int arg1) {
		Log.d("", "latitude=" + latitude + ";longitude=" + longitude);
		Drawable mark = getResources().getDrawable(mark_picture[arg0]);
		OverlayTest itemOverlay = new OverlayTest(mark, myMapView);
		Drawable mark1 = getResources().getDrawable(mark_picture[arg1]);
		OverlayTest itemOverlay1 = new OverlayTest(mark1, myMapView);
		// view.getOverlays().clear();
		if (null != data_mark && 0 < data_mark.size()) {
			for (int i = 0; i < data_mark.size(); i++) {
				double mLat1 = Double.parseDouble(data_mark.get(i)
						.getStringNoNull("Lat"));
				double mLon1 = Double.parseDouble(data_mark.get(i)
						.getStringNoNull("Lng"));
				GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6),
						(int) (mLon1 * 1E6));
				OverlayItem item = new OverlayItem(p1, "item" + i, "item" + i);
				itemOverlay.addItem(item);
			}
		}

		GeoPoint p1 = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "item" + data_mark.size(),
				"item" + data_mark.size());
		itemOverlay1.addItem(item1);
		myMapView.getOverlays().clear();
		myMapView.getOverlays().add(itemOverlay);
		myMapView.getOverlays().add(itemOverlay1);
		myMapView.refresh();
	}

	/**
	 * 自定义图层：ItemizedOverlay
	 * 
	 * 要处理overlay点击事件时需要继承ItemizedOverlay 不处理点击事件时可直接生成ItemizedOverlay.
	 */
	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		/**
		 * 用MapView构造ItemizedOverlay
		 * 
		 * @param mark
		 * @param mapView
		 */
		public OverlayTest(Drawable mark, MapView mapView) {
			super(mark, mapView);

		}

		public OverlayTest(Drawable mark, MapView mapView, MapView mapView1) {
			super(mark, mapView);

		}

		protected boolean onTap(final int index) {
			return true;
		}

	}

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
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			yanzhenmakeyongmiao--;
			if (yanzhenmakeyongmiao <= -2) {
				progress = 0;
				index1_rpb.setProgress(progress);
				tv_recording_time.setText("00:50");
				return;
			}
			if (yanzhenmakeyongmiao <= 0) {
				stopRecorder();
				Intent intent = new Intent(Index1Activity.this,
						Index2Activity.class);
				intent.putExtra(ExtraKeys.Key_Msg1, "0");
				startActivity(intent);
				Index1Activity.this.finish();
				return;
			}
			progress += 100 / 50;
			index1_rpb.setProgress(progress);
			progress1 = yanzhenmakeyongmiao >= 10 ? "" + yanzhenmakeyongmiao
					: "0" + yanzhenmakeyongmiao;
			tv_recording_time.setText("00:" + progress1);
		};
	};

	/**
	 * 快速回到定位地址
	 * 
	 * @param v
	 */
	public void map_locate(View v) {
		GeoPoint geoPoint = new GeoPoint((int) (latitude * 1e6),
				(int) (longitude * 1e6));
		myMapView.getController().animateTo(geoPoint);
	}

	/**
	 * 打开。关闭抽屉
	 * 
	 * @param v
	 */
	public void iv_top_handle(View v) {
		myMenuDrawer.openMenu();
	}

	/**
	 * 底部点击事件
	 * 
	 * @param v
	 */
	public void bottom_ll(View v) {
		// stopRecorder();
		// if (46 < yanzhenmakeyongmiao) {
		// yanzhenmakeyongmiao = -2;
		// tv_recording.setText(getString(R.string.index1_tv_voice));
		// Toast.makeText(Index1Activity.this,
		// getString(R.string.index1_toast_voice), Toast.LENGTH_SHORT)
		// .show();
		// return;
		// }
		// if (getString(R.string.index1_tv_voice).equals(
		// tv_recording.getText().toString().trim())) {
		// yanzhenmakeyongmiao = 50;
		// recorder();
		// tv_recording.setText(getString(R.string.index_tv_recording));
		// return;
		// }
		// Intent intent = new Intent(Index1Activity.this,
		// Index2Activity.class);
		// intent.putExtra(ExtraKeys.Key_Msg1, "0");
		// intent.putExtra(ExtraKeys.Key_Msg2,
		// getIntent().getStringExtra(ExtraKeys.Key_Msg2));
		// intent.putExtra(ExtraKeys.Key_Msg3, "a");
		// startActivity(intent);
		// Index1Activity.this.finish();
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onDestroy() {
		// clean.cleanApplicationData(getApplicationContext(), null);
		myMapView.destroy();
		if (null != timer) {
			timer.cancel();
		}
		if (manager != null) {
			manager.destroy();
			manager = null;

			if (data_mark != null) {
				data_mark.clear();
				myMapView.destroyDrawingCache();
			}
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		try {
			recorder.stopRecording();
		} catch (IOException e) {
			Log.d(TAG, "Stop error");
		}
		// if (mMediaRecorder01 != null && !isStopRecord) {
		// /* 停止录音 */
		// mMediaRecorder01.stop();
		// mMediaRecorder01.release();
		// mMediaRecorder01 = null;
		// }
		super.onStop();
	}

	@Override
	protected void onPause() {
		myMapView.onPause();
		// 按个人定位显示起始地的代码

		if (manager != null) {
			manager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// locationClient.start();
		myMapView.onResume();
		// locationClient.stop();

		if (manager != null) {
			manager.start();
		}
		super.onResume();
	}
}
