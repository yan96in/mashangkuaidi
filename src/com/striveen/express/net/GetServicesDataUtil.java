package com.striveen.express.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public final class GetServicesDataUtil
{
  private static GetServicesDataUtil util;
  private Map<Integer, GetServicesDataQueue> queues = new HashMap();

  private int index = 0;

  private int errorNum = 2;

  private String charset = "UTF-8";
  private Context context;
  private Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      int index = msg.arg2;
      GetServicesDataQueue q = (GetServicesDataQueue)GetServicesDataUtil.this.queues.get(Integer.valueOf(index));
      q.setOk(msg.what == 0);

      q.setInfo((String)msg.obj);
      q.getCallBack().onLoaded(q);
      synchronized (GetServicesDataUtil.this.queues) {
        GetServicesDataUtil.this.queues.remove(Integer.valueOf(index));
      }
    }
  };

  private GetServicesDataUtil()
  {
    Random random = new Random();
    this.index = random.nextInt(1000);
  }

  public static GetServicesDataUtil init()
  {
    if (util == null) {
      util = new GetServicesDataUtil();
    }
    return util;
  }

  public void setErrorNum(int errorNum)
  {
    if (errorNum < 1)
      this.errorNum = 2;
    else
      this.errorNum = errorNum;
  }

  public void setContext(Context context)
  {
    this.context = context.getApplicationContext();
  }

  public void setCharset(String charset)
  {
    for (String s : CharsetUtil.CHARSETS) {
      if (s.equals(charset)) {
        this.charset = charset;
        return;
      }
    }
    this.charset = "UTF-8";
  }

  public void getData(GetServicesDataQueue queue)
  {
    this.index += 1;
    queue.setGsdqId(this.index);
    if ((this.context != null) && 
      (!NetWorkHelper.isWifiConnected(this.context))) {
      queue.setOk(false);
      queue.setInfo("java.net.UnknownHostException:No network");
      queue.getCallBack().onLoaded(queue);
      return;
    }

    GetServicesDataRunnable runnable = new GetServicesDataRunnable(
      queue.getIp(), queue.getActionName(), queue.getParams(), 
      this.handler, queue.isGet());
    runnable.setMsgTag(queue.getGsdqId());
    runnable.setErrorNum(this.errorNum);
    runnable.setCharset(this.charset);
    new Thread(runnable).start();
    synchronized (this.queues) {
      this.queues.put(Integer.valueOf(this.index), queue);
    }
  }

  public static abstract interface IGetServicesDataCallBack
  {
    public abstract void onLoaded(GetServicesDataQueue paramGetServicesDataQueue);
  }
}