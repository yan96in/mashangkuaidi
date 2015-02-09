package com.striveen.express.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.striveen.express.R;

public class LoadingDataDialog extends Dialog
{
  private Context context;
  private TextView tv_msg;

  public LoadingDataDialog(Context context)
  {
    super(context);
    this.context = context;
    init();
  }

  public LoadingDataDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener)
  {
    super(context, cancelable, cancelListener);
    this.context = context;
    init();
  }

  public LoadingDataDialog(Context context, int theme) {
    super(context, theme);
    this.context = context;
    init();
  }

  private void init()
  {
  }

  protected void onCreate(Bundle savedInstanceState)
  {
    requestWindowFeature(1);
//    getWindow().setBackgroundDrawableResource(
//      R.drawable.ic_launcher);
    super.onCreate(savedInstanceState);
    setContentView(creatLayout());
  }

  private LinearLayout creatLayout()
  {
    LinearLayout layout = new LinearLayout(this.context);
    layout.setPadding(10, 10, 10, 10);
    layout.setGravity(16);

    ProgressBar progressBar = new ProgressBar(this.context, null, 
      16842873);
    layout.addView(progressBar);
    this.tv_msg = new TextView(this.context);
    this.tv_msg.setText(R.string.text_loadingdata);
    this.tv_msg.setTextColor(Color.parseColor("#cccccc"));
    layout.addView(this.tv_msg);
    return layout;
  }

  public void setMessage(String msg)
  {
    if (this.tv_msg != null)
      this.tv_msg.setText(msg);
  }

  public void setMessage(int resId)
  {
    setMessage(this.context.getString(resId));
  }
}