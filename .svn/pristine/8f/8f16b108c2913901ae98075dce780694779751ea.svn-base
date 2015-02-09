package com.striveen.express.net;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetServicesDataRunnable
  implements Runnable
{
  private final String TAG = getClass().getSimpleName();

  private boolean isGet = true;
  private String ip;
  private String actionName;
  private Map<String, String> params;
  private Handler handler;
  public static final int what_default_ok = 0;
  public static final int what_default_error = -1;
  private int what_ok = 0;

  private int what_error = -1;
  private int msgTag;
  private int errorNum = 2;

  private String charset = "UTF-8";
  private DefaultHttpClient httpClient;

  public void setMsgTag(int magTag)
  {
    this.msgTag = magTag;
  }

  public void setErrorNum(int errorNum)
  {
    if (errorNum < 1)
      this.errorNum = 2;
    else
      this.errorNum = errorNum;
  }

  public void setCharset(String charset)
  {
    this.charset = charset;

    Log.d(this.TAG, "charset ：" + charset);
  }

  public GetServicesDataRunnable(String ip, String actionName, Map<String, String> params, Handler handler, boolean isGet)
  {
    this.ip = ip;
    this.actionName = actionName;
    if (params == null)
      params = new HashMap();
    else {
      this.params = params;
    }
    this.handler = handler;
    this.isGet = isGet;
    initHttpClient();
  }

  public GetServicesDataRunnable(String ip, String actionName, Map<String, String> params, Handler handler, int what_ok, int what_error, boolean isGet)
  {
    this(ip, actionName, params, handler, what_ok, isGet);
    this.what_error = what_error;
  }

  public GetServicesDataRunnable(String ip, String actionName, Map<String, String> params, Handler handler, int what_ok, boolean isGet)
  {
    this(ip, actionName, params, handler, isGet);
    this.what_ok = what_ok;
  }

  private void initHttpClient()
  {
    this.httpClient = new DefaultHttpClient();

    HttpParams httpParameters = new BasicHttpParams();
    int timeoutConnection = 3000;
    HttpConnectionParams.setConnectionTimeout(httpParameters, 
      timeoutConnection);
    int timeoutSocket = 4000;
    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

    Log.d(this.TAG, "connectionTimeout：" + timeoutConnection);
    Log.d(this.TAG, "socketTimeout：" + timeoutSocket);
    this.httpClient.setParams(httpParameters);
  }

  public void setHttpClientTimeout(int timeoutConnection, int timeoutSocket)
  {
    HttpParams httpParameters = new BasicHttpParams();
    if (timeoutConnection <= 0) {
      timeoutConnection = 3000;
    }
    HttpConnectionParams.setConnectionTimeout(httpParameters, 
      timeoutConnection);
    if (timeoutSocket <= 0) {
      timeoutSocket = 4000;
    }
    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

    Log.d(this.TAG, "connectionTimeout updata to ：" + timeoutConnection);
    Log.d(this.TAG, "socketTimeout updata to ：" + timeoutSocket);
    this.httpClient.setParams(httpParameters);
  }

  public void run() {
    String falg = null;
    Message message = null;
    if (this.handler != null) {
      message = this.handler.obtainMessage();
      message.arg2 = this.msgTag;
    }

    for (int eNum = 0; eNum < this.errorNum; eNum++)
      try
      {
        if (this.isGet)
          falg = getData(eNum);
        else {
          falg = postData(eNum);
        }

        falg = falg.trim();
        if ((this.handler == null) || (message == null)) break;
        message.obj = falg;
        message.what = this.what_ok;
        this.handler.sendMessage(message);
      }
      catch (Exception e)
      {
        Log.d(this.TAG, 
          eNum + 1 + 
          " times：" + (
          e.getMessage() == null ? e.getClass()
          .getName() : e.getMessage()));
        if (eNum >= this.errorNum - 1)
        {
          String exceptionStr = e.getClass().getName() + " : " + 
            e.getMessage();

          Log.e(this.TAG, exceptionStr);
          if ((this.handler != null) && (message != null)) {
            message.obj = exceptionStr;
            message.what = this.what_error;
            this.handler.sendMessage(message);
          }
        }
      }
  }

  private String getData(int num)
    throws Exception
  {
    StringBuffer uriAPI = new StringBuffer(this.ip + this.actionName + "?");
    StringBuffer uriAPICache = new StringBuffer(this.ip + this.actionName + "?");
    if (this.params != null)
    {
      for (Map.Entry entry : this.params.entrySet()) {
        uriAPI.append((String)entry.getKey() + "=" + 
          URLEncoder.encode((String)entry.getValue(), this.charset) + "&");
        uriAPICache.append((String)entry.getKey() + "=" + (String)entry.getValue() + 
          "&");
      }
    }

    uriAPI.deleteCharAt(uriAPI.length() - 1);
    uriAPICache.deleteCharAt(uriAPICache.length() - 1);

    if (num == 0) {
      Log.d(this.TAG, "uriAPI：\n" + uriAPICache.toString());
    }
    uriAPICache = null;

    Log.d(this.TAG, "Try " + (num + 1) + " times");

    HttpGet get = new HttpGet(uriAPI.toString());

    HttpResponse response = this.httpClient.execute(get);

    Log.d(this.TAG, "访问状态 : \n" + response.getStatusLine().toString());

    if (response.getStatusLine().getStatusCode() != 200) {
      throw new Exception(response.getStatusLine().toString());
    }

    String strResult = EntityUtils.toString(response.getEntity(), this.charset);
    Log.d(this.TAG, "返回数据：\n" + strResult);
    return strResult;
  }

  private String postData(int num)
    throws Exception
  {
    HttpPost httpPost = new HttpPost(this.ip + this.actionName);

    List postData = new ArrayList();
    StringBuffer uriAPICache = new StringBuffer(this.ip + this.actionName + "?");
    if (this.params != null)
    {
      for (Map.Entry entry : this.params.entrySet()) {
        postData.add(new BasicNameValuePair((String)entry.getKey(), 
          (String)entry
          .getValue()));
        uriAPICache.append((String)entry.getKey() + "=" + (String)entry.getValue() + 
          "&");
      }
    }
    uriAPICache.deleteCharAt(uriAPICache.length() - 1);

    if (num == 0) {
      Log.d(this.TAG, "uriAPI：\n" + uriAPICache.toString());
    }
    uriAPICache = null;

    Log.d(this.TAG, "Try " + (num + 1) + " times");

    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, this.charset);
    httpPost.setEntity(entity);
    HttpResponse response = this.httpClient.execute(httpPost);

    Log.d(this.TAG, "访问状态 : \n" + response.getStatusLine().toString());

    if (response.getStatusLine().getStatusCode() != 200) {
      throw new Exception(response.getStatusLine().toString());
    }

    String strResult = EntityUtils.toString(response.getEntity(), this.charset);
    Log.d(this.TAG, "返回数据：\n" + strResult);
    return strResult;
  }
}