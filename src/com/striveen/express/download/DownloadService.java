package com.striveen.express.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.striveen.express.R;
import com.striveen.express.net.HttpConnection;
import com.striveen.express.util.LogUtil;
import com.striveen.express.util.SdcardHelper;
import com.striveen.express.util.ToastUtil;

public class DownloadService extends Service {
	private final String TAG = getClass().getName();
	private final String fileLoadingHZ = ".aymdl";

	private Binder serviceBinder = new DownLoadServiceBinder();
	private NotificationManager manager;
	private Timer timer = new Timer();

	private Map<DownLoadQueue, Notification> downLoadQueues = new HashMap();
	private List<DownLoadQueue> downLoadedQueues = new Vector();
	private SdcardHelper sdHelper;
	private boolean isNoSdcard = false;

	private final int what_SDFreeSizeInadequate = -1;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				ToastUtil.initToast(DownloadService.this).showToast("sd卡空间不足");
				break;
			}
		}
	};

	public IBinder onBind(Intent intent) {
		Log.d("DownloadService", "onBind");
		return this.serviceBinder;
	}

	public void onCreate() {
		super.onCreate();
		Log.d("DownloadService", "onCreate");
		this.manager = ((NotificationManager) getSystemService("notification"));
		this.sdHelper = new SdcardHelper();
		this.isNoSdcard = (!this.sdHelper.ExistSDCard());
	}

	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("DownloadService", "onStart");
	}

	public void onDestroy() {
		this.timer.cancel();
		super.onDestroy();
	}

	private void startDownLoad(final DownLoadQueue queue) {
		new Thread() {
			public void run() {
				try {
					HttpClient client = HttpConnection.getHttpClient();
					HttpGet get = new HttpGet(queue.getDownLoadUrl());

					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					queue.setOffSize(length);
					if (DownloadService.this.sdHelper.getSDFreeSize() <= length / 1024L) {
						queue.setCancel(true);
						DownloadService.this.handler.sendEmptyMessage(-1);
						return;
					}
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					File file = null;
					String endName = null;
					if (is != null) {
						file = DownloadService.this.creatFile(queue
								.getDownLoadedPath());

						endName = file.getAbsolutePath().substring(
								0,
								file.getAbsolutePath().length()
										- ".aymdl".length());

						queue.setFileName(endName);

						fileOutputStream = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int charb = -1;
						int count = 0;

						while (((charb = is.read(b)) != -1)
								&& (!queue.isCancel())) {
							fileOutputStream.write(b, 0, charb);
							count += charb;
							queue.setDownSize(count);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					if (!queue.isCancel()) {
						queue.setDownSize(length);
						file.renameTo(new File(endName));
					}
				} catch (Exception e) {
					Log.e(DownloadService.this.TAG, e.getClass().getName()
							+ " : " + e.getMessage(), e);
				}
			}
		}

		.start();
	}

	public DownLoadQueue addQueue(DownLoadQueue queue) {
		if (this.isNoSdcard) {
			ToastUtil.initToast(this).showToast(R.string.nosdcard);

			return queue;
		}
		for (DownLoadQueue downLoadQueue : downLoadQueues.keySet()) {
			if (queue.getId() == downLoadQueue.getId()) {
				return downLoadQueue;
			}
		}
		synchronized (this.downLoadQueues) {
			this.downLoadQueues.put(queue, null);
			if (this.downLoadQueues.size() == 1) {
				LogUtil.Log(TAG, "开启定时更新下载进度");
				this.timer.schedule(new MyTimerTask(), 500, 500);
			}
		}
		startDownLoad(queue);
		return queue;
	}

	public List<DownLoadQueue> getQueueList() {
		List dlqs = new ArrayList();
		dlqs.addAll(this.downLoadQueues.keySet());

		return dlqs;
	}

	public void stopQueue(int queueId) {
		for (DownLoadQueue downLoadQueue : downLoadQueues.keySet())
			if (queueId == downLoadQueue.getId()) {
				synchronized (this.downLoadQueues) {
					downLoadQueue.setCancel(true);
				}
				return;
			}
	}

	private File creatFile(String filePath) throws Exception {
		File file = new File(filePath);
		boolean isNO = true;
		int i = 1;
		while (isNO) {
			if (file.exists()) {
				int index = filePath.lastIndexOf(".");
				if (index == -1) {
					if (i == 1)
						filePath = filePath + i;
					else {
						filePath = filePath.substring(0, filePath.length() - 1);
					}
				} else if (i == 1)
					filePath = new StringBuffer(filePath).insert(index,
							"(" + i + ")").toString();
				else {
					filePath = new StringBuffer(filePath).replace(
							filePath.indexOf("("), filePath.indexOf(")") + 1,
							"(" + i + ")").toString();
				}

				file = new File(filePath);
				i++;
			} else {
				isNO = false;
			}
		}
		filePath = filePath + ".aymdl";
		file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		return file;
	}

	public class DownLoadServiceBinder extends Binder {
		public DownLoadServiceBinder() {
		}

		public DownloadService getService() {
			return DownloadService.this;
		}
	}

	private class MyTimerTask extends TimerTask {
		private MyTimerTask() {
		}

		public void run() {
			updataDownLoadInfo();
		}

		private void updataDownLoadInfo() {
			Intent intent = null;
			try {
				intent = new Intent(DownloadService.this,
						DownloadAddQueueView.class);
			} catch (Exception localException) {
			}
			for (DownLoadQueue downLoadQueue : downLoadQueues.keySet())
				if (downLoadQueue.isCancel()) {
					DownloadService.this.downLoadQueues.remove(downLoadQueue);
					DownloadService.this.manager.cancel(downLoadQueue.getId());
				} else {
					Notification notification = (Notification) DownloadService.this.downLoadQueues
							.get(downLoadQueue);
					boolean downloading = downLoadQueue.getOffSize()
							- downLoadQueue.getDownSize() > 10L;
					if (notification == null) {
						notification = new Notification();
						notification.icon = downLoadQueue.getIconResId();
						updataNotify(intent, downLoadQueue, notification);
						DownloadService.this.downLoadQueues.put(downLoadQueue,
								notification);
					} else {
						updataNotify(intent, downLoadQueue, notification);
					}
				}
		}

		private void updataNotify(Intent intent, DownLoadQueue downLoadQueue,
				Notification notification) {
			PendingIntent intent2 = null;
			if ((intent != null)
					&& ((!downLoadQueue.isDownLoadOK()) || ((downLoadQueue
							.isDownLoadOK()) && (downLoadQueue
							.getOpenFileType() != null)))) {
				intent.putExtra("isToStop", true);
				intent.putExtra("queue", downLoadQueue);
				try {
					intent2 = PendingIntent.getActivity(DownloadService.this,
							1, intent, 134217728);
				} catch (Exception localException) {
				}
				if ((downLoadQueue.isDownLoadOK())
						&& (downLoadQueue.getOpenFileType() != null)
						&& (downLoadQueue.isDownLoadedAutoopenFile())) {
					downLoadQueue.OpenFile(DownloadService.this
							.getApplicationContext());
				}

			}

			notification
					.setLatestEventInfo(DownloadService.this,
							downLoadQueue.getName(), getProcess(downLoadQueue),
							intent2);
			DownloadService.this.manager.notify(downLoadQueue.getId(),
					notification);
		}

		private String getProcess(DownLoadQueue queue) {
//			LogUtil.Log(TAG,"getDownSize = "+ queue.getDownSize());
//			LogUtil.Log(TAG, "getOffSize = "+queue.getOffSize());
//			
//			String ratio = ProgressGet100Util.get100_00Progress(
//					queue.getDownSize(), queue.getOffSize());
//
			if (queue.getDownSize() == queue.getOffSize()) {
				synchronized (DownloadService.this.downLoadQueues) {
					DownloadService.this.downLoadedQueues.add(queue);
					DownloadService.this.downLoadQueues.remove(queue);
				}
				return "下载完成";
			}
//			LogUtil.Log(TAG, ratio);
//			return "已下载 " + ratio;
			return queue.getDownSize()+"/"+queue.getOffSize();
		}
	}
}