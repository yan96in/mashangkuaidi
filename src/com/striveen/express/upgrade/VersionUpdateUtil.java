package com.striveen.express.upgrade;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.striveen.express.download.DownLoadQueue;
import com.striveen.express.download.DownloadAddQueueView;
import com.striveen.express.net.GetServicesDataQueue;
import com.striveen.express.net.GetServicesDataUtil;

public class VersionUpdateUtil
{
  private final String TAG = getClass().getSimpleName();
  private Context context;
  private IVersionSelectedCallBack callBack;
  private GetServicesDataUtil dataUtil;
  private Timer timer;
  public static final int VersionUpdate = 2147483645;
  private GetServicesDataUtil.IGetServicesDataCallBack getDataCallBack = new GetServicesDataUtil.IGetServicesDataCallBack()
  {
    public void onLoaded(GetServicesDataQueue entity)
    {
      if ((entity.getWhat() == 2147483645) && (entity.isOk()))
        VersionUpdateUtil.this.callBack.isUpdate(entity.getInfo(), VersionUpdateUtil.this.getVersionName(), 
          VersionUpdateUtil.this.getVersionCode());
      else
        VersionUpdateUtil.this.callBack.netError(entity.getInfo());
    }
  };

  private final int what_delayDownload = -19;

  private Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      if (msg.what == -19) {
        DownLoadQueue queue = (DownLoadQueue)msg.obj;
        VersionUpdateUtil.this.addDownloadQueue(queue);
        if (VersionUpdateUtil.this.timer != null) {
          VersionUpdateUtil.this.timer.cancel();
          VersionUpdateUtil.this.timer = null;
        }
      }
    }
  };

  private VersionUpdateUtil(Context context)
  {
    this.context = context.getApplicationContext();
  }

  public static VersionUpdateUtil init(Context context)
  {
    return new VersionUpdateUtil(context);
  }

  public void doSelectVersion(String ip, String actionName, Map<String, String> params, boolean isGet, IVersionSelectedCallBack callBack)
  {
    if (callBack == null) {
      Log.w(this.TAG, "callBack is null");
      return;
    }
    this.callBack = callBack;
    this.dataUtil = GetServicesDataUtil.init();
    GetServicesDataQueue queue = new GetServicesDataQueue();
    queue.setWhat(2147483645);
    queue.setCallBack(this.getDataCallBack);
    queue.setActionName(actionName);
    queue.setGet(isGet);
    queue.setIp(ip);
    queue.setParams(params);
    this.dataUtil.getData(queue);
  }

  public void doUpdateVersion(String downFileUriPath, String downLoadedFilePath, String appName, int appIcon, boolean isDelay)
  {
    if ((downFileUriPath == null) || (downLoadedFilePath == null)) {
      Log.w(this.TAG, "downFileUriPath or downLoadedFilePath is null");
      return;
    }
    DownLoadQueue queue = new DownLoadQueue();
    queue.setId(2147483645);
    queue.setDownLoadedPath(downLoadedFilePath);
    queue.setDownLoadUrl(downFileUriPath);
    queue.setIconResId(appIcon);
    queue.setDownLoadedAutoOpenFile(true, 
      "application/vnd.android.package-archive");
    queue.setName(appName);
    if (isDelay) {
      if (this.timer == null) {
        this.timer = new Timer();
      }
      this.timer.schedule(new DelayDownloadTimerTask(queue), 1000L);
    } else {
      addDownloadQueue(queue);
    }
  }

  private void addDownloadQueue(DownLoadQueue queue)
  {
    try
    {
      Intent intent = new Intent(this.context, DownloadAddQueueView.class);
      intent.putExtra("queue", queue);
      intent.addFlags(268435456);
      this.context.startActivity(intent);
    } catch (Exception e) {
      Log.e(this.TAG, e.getClass().getName() + " : " + e.getMessage());
    }
  }

  private PackageInfo getPackageInfo()
  {
    PackageManager packageManager = this.context.getPackageManager();

    PackageInfo packInfo = null;
    try {
      packInfo = packageManager.getPackageInfo(this.context.getPackageName(), 
        0);
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(this.TAG, e.getClass().getName() + " : " + e.getMessage());
    }
    return packInfo;
  }

  public String getVersionName()
  {
    PackageInfo info = getPackageInfo();
    if (info == null) {
      return null;
    }
    return info.versionName;
  }

  public int getVersionCode()
  {
    PackageInfo info = getPackageInfo();
    if (info == null) {
      return 0;
    }
    return info.versionCode;
  }

  public void updateAPPVersion(String appFilePath)
  {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.setDataAndType(Uri.parse(appFilePath), 
      "application/vnd.android.package-archive");
    intent.addFlags(268435456);
    this.context.startActivity(intent);
  }

  private class DelayDownloadTimerTask extends TimerTask
  {
    private DownLoadQueue queue;

    public DelayDownloadTimerTask(DownLoadQueue queue)
    {
      this.queue = queue;
    }

    public void run()
    {
      Message message = VersionUpdateUtil.this.handler.obtainMessage(-19);
      message.obj = this.queue;
      VersionUpdateUtil.this.handler.sendMessage(message);
    }
  }
}