package com.striveen.express.download;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.striveen.express.util.SdcardHelper;

public class DownloadAddQueueView extends Activity implements
		View.OnClickListener, ServiceConnection {
	private final String TAG = getClass().getSimpleName();
	private LinearLayout contentView;
	private String title;
	private DownLoadQueue queue;
	private int queue_id;
	private int queue_iconResId;
	private String queue_name;
	private String queue_downLoadUrl;
	private String queue_downLoadedPath;
	private boolean isToStop = false;

	private boolean isDownLoading = false;
	private String openFileType;
	private boolean downLoadedAutoOpenFile;
	private Button btn_stop;
	private Button btn_cancel;
	private DownloadService.DownLoadServiceBinder binder;
	private TextView tv;
	private Timer timer;
	private TimerTask timerTask = new TimerTask() {
		public void run() {
			DownloadAddQueueView.this.handler.sendEmptyMessage(1);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			DownloadAddQueueView.this.tv
					.setText(DownloadAddQueueView.this.queue.getName()
							+ "\n"
							+ " 已下载完成 " + DownloadAddQueueView.this.queue.getDownSize()+"/" + DownloadAddQueueView.this.queue.getOffSize());
							/*+ ProgressGet100Util.get100_00Progress(
									DownloadAddQueueView.this.queue
											.getDownSize(),
									DownloadAddQueueView.this.queue
											.getOffSize()));*/
//			LogUtil.Log(TAG,"getDownSize = "+ DownloadAddQueueView.this.queue.getDownSize());
//			LogUtil.Log(TAG, "getOffSize = " + DownloadAddQueueView.this.queue.getOffSize());
			
			if (DownloadAddQueueView.this.queue.isDownLoadOK()) {
				DownloadAddQueueView.this.finish();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		setTheme(16973936);
		super.onCreate(savedInstanceState);
		if (!new SdcardHelper().ExistSDCard())
			finish();
		try {
			Intent service = new Intent(this, DownloadService.class);
			getApplicationContext().startService(service);
			getApplicationContext().bindService(service, this, 1);
		} catch (Exception e) {
			Log.e(this.TAG, e.getMessage());
			finish();
			return;
		}
		getIntentData();
		if (this.queue == null) {
			Log.w(this.TAG, "下载出错");
			finish();
		}
		setTitle(this.title == null ? "更新版本" : this.title);
		setContentView(creatContentView());

		if (!this.isToStop) {
			this.timer = new Timer();
			this.timer.schedule(this.timerTask, 100, 300);
		}
	}

	protected void onDestroy() {
		if (this.timer != null)
			this.timer.cancel();
		try {
			getApplicationContext().unbindService(this);
		} catch (Exception e) {
			Log.e(this.TAG, e.getMessage());
		}
		super.onDestroy();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		this.title = intent.getStringExtra("title");
		this.isToStop = intent.getBooleanExtra("isToStop", false);
		try {
			this.queue = ((DownLoadQueue) intent.getExtras().get("queue"));
		} catch (Exception e) {
			this.queue = null;
		}

		if (this.queue == null) {
			this.queue_id = intent.getIntExtra("queue_id", 0);
			this.queue_iconResId = intent.getIntExtra("queue_iconResId", 0);
			this.queue_name = intent.getStringExtra("queue_name");
			this.queue_downLoadedPath = intent
					.getStringExtra("queue_downLoadedPath");
			this.queue_downLoadUrl = intent.getStringExtra("queue_downLoadUrl");
			this.downLoadedAutoOpenFile = intent.getBooleanExtra(
					"queue_downLoadedAutoOpenFile", false);
			this.openFileType = intent.getStringExtra("queue_openFileType");
			if ((this.queue_name != null)
					&& (this.queue_downLoadedPath != null)
					&& (this.queue_downLoadUrl != null)
					&& (!"".equals(this.queue_name))
					&& (!"".equals(this.queue_downLoadedPath))
					&& (!"".equals(this.queue_downLoadUrl))
					&& (this.queue_id > 0)) {
				this.queue = new DownLoadQueue();
				this.queue.setId(this.queue_id);
				this.queue.setIconResId(this.queue_iconResId);
				this.queue.setCancel(false);
				this.queue.setDownLoadedPath(this.queue_downLoadedPath);
				this.queue.setDownLoadUrl(this.queue_downLoadUrl);
				this.queue.setName(this.queue_name);
				this.queue.setOpenFileType(this.openFileType);
				this.queue.setDownLoadedAutoOpenFile(
						this.downLoadedAutoOpenFile, this.openFileType);
			}
		}
	}

	private View creatContentView() {
		this.contentView = new LinearLayout(this);
		this.contentView.setOrientation(1);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
		lp.setMargins(10, 10, 10, 10);
		this.tv = new TextView(this);

		LinearLayout ll = new LinearLayout(this);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(-2, -2);
		lp2.weight = 1.0F;
		this.btn_stop = new Button(this);
		this.btn_stop.setOnClickListener(this);
		this.btn_cancel = new Button(this);
		this.btn_cancel.setOnClickListener(this);
		ll.addView(this.btn_stop, lp2);
		ll.addView(this.btn_cancel, lp2);

		this.contentView.addView(this.tv, lp);
		this.contentView.addView(ll, lp);
		if (this.isToStop) {
			if (this.queue.isDownLoadOK()) {
				this.tv.setText(this.queue.getName() + "\n" + " 正在打开文件");

				this.btn_stop.setText("打开");
			} else {
				this.tv.setText(this.queue.getName() + "\n" + "暂定下载");
				this.btn_stop.setText("停止下载");
			}
			this.btn_cancel.setText("继续下载");
		} else {
			this.tv.setText(this.queue.getName() + "\n" + "下载完成"
					+ queue.getDownSize() + "/" + queue.getOffSize());
			/*
			 * + ProgressGet100Util.get100_00Progress( this.queue.getDownSize(),
			 * this.queue.getOffSize()));
			 */
			this.btn_stop.setText("后台下载");
			this.btn_cancel.setText("停止下载");
		}

		return this.contentView;
	}

	public void onClick(View v) {
		if (v.equals(this.btn_stop)) {
			if (this.isToStop) {
				if (this.queue.isDownLoadOK()) {
					this.queue.OpenFile(this);
					finish();
				} else {
					this.queue.setCancel(true);
					if (this.binder != null) {
						this.binder.getService().stopQueue(this.queue.getId());
					}
					finish();
				}
			} else
				finish();

		} else if (this.isToStop) {
			finish();
		} else {
			this.queue.setCancel(true);
			finish();
		}
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i(this.TAG, "DownloadService Connected");
		this.binder = ((DownloadService.DownLoadServiceBinder) service);
		if (!this.isToStop)
			this.queue = this.binder.getService().addQueue(this.queue);
	}

	public void onServiceDisconnected(ComponentName name) {
		Log.i(this.TAG, "DownloadService Disconnected");
	}
}