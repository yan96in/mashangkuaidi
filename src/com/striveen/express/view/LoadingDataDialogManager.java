package com.striveen.express.view;


import android.content.Context;
import android.util.Log;

import com.striveen.express.R;

public class LoadingDataDialogManager
{
  private final String TAG = LoadingDataDialogManager.class.getSimpleName();
  private int index;
  private LoadingDataDialog progressDialog;

  private LoadingDataDialogManager(Context context, String loadingMsg)
  {
    this.index = 0;

    this.progressDialog = new LoadingDataDialog(context);

    if (loadingMsg == null) {
      this.progressDialog.setMessage(context
        .getString(R.string.text_loadingdata));
    }

    this.progressDialog.setCancelable(false);
  }

  public static LoadingDataDialogManager init(Context context)
  {
    return new LoadingDataDialogManager(context, null);
  }

  public static LoadingDataDialogManager init(Context context, String loadingMsg)
  {
    return new LoadingDataDialogManager(context, loadingMsg);
  }

  public static LoadingDataDialogManager init(Context context, int resIdLoadingMsg)
  {
    return new LoadingDataDialogManager(context, context.getResources()
      .getString(resIdLoadingMsg));
  }

  public void destroy()
  {
    if (this.progressDialog != null) {
      this.progressDialog.dismiss();
      this.progressDialog = null;
    }
  }

  public void show()
  {
    if (this.index == 0) {
      if (this.progressDialog != null)
        this.progressDialog.show();
      else {
        Log.w(this.TAG, "progressDialog not init");
      }
    }
    this.index += 1;
  }

  public void update(String msg)
  {
    if (this.progressDialog != null)
      this.progressDialog.setMessage(msg);
    else
      Log.w(this.TAG, "progressDialog not init");
  }

  public void dismiss()
  {
    if (this.index <= 1) {
      if (this.progressDialog != null)
        this.progressDialog.hide();
      else {
        Log.w(this.TAG, "progressDialog not init");
      }
      this.index = 0;
    } else {
      this.index -= 1;
    }
  }
}