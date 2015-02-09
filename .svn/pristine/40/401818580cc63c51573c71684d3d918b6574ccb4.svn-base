package com.striveen.express.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.net.GetData.ResponseCallBack;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.net.ThreadPoolManager;
import com.striveen.express.runmethodinthread.RunMITQueue;
import com.striveen.express.runmethodinthread.RunMITStaticQueue;
import com.striveen.express.runmethodinthread.RunMITUtil;
import com.striveen.express.runmethodinthread.RunMITUtil.IRunMITCallBack;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonParseHelper;
import com.striveen.express.view.ViewInject;

/**
 * 意见反馈
 * 
 * @author Fei
 * 
 */
public class FeedBackActivity extends MyActivity {
	private final String TAG = "FeedBackActivity";
	/**
	 * 输入框
	 */
	@ViewInject(id = R.id.feedback_et_feed)
	private EditText et_feed;
	/**
	 * 立即发送
	 */
	@ViewInject(id = R.id.feedback_bt_send, click = "bt_send")
	private Button bt_send;
	/**
	 * 企业QQ
	 */
	@ViewInject(id = R.id.feedback_tv_qq)
	private TextView tv_qq;
	/**
	 * 新浪微博
	 */
	@ViewInject(id = R.id.feedback_tv_sina_blog)
	private TextView tv_sina_blog;
	/**
	 * 微信公众号
	 */
	@ViewInject(id = R.id.feedback_tv_wechat)
	private TextView tv_wechat;
	private RunMITUtil runMITUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		runMITUtil = RunMITUtil.init();
	}

	public void bt_send(View c) {
		if (TextUtils.isEmpty(et_feed.getText().toString())) {
			toast.showToast(getString(R.string.feedback_toast_tv));
			return;
		}
		loadingToast.show();
		ThreadPoolManager.getInstance().execute(PostAdvice);
	}

	public void iv_top_left(View v) {
		FeedBackActivity.this.finish();
	}

	/**
	 * 意见反馈
	 */
	private Runnable PostAdvice = new Runnable() {

		@Override
		public void run() {
			// {'UserId':1,Context:'反馈意见',UserType:'1'}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("UserId", getMyApplication().getUserId());
			param.put("Context", et_feed.getText().toString());
			param.put("UserType", "0");// 0 发件人 1收件人
			getData.doPost(callBack, GetDataConfing.ip, param, "Config",
					"PostAdvice", 0);
		}
	};

	/**
	 * 获取数据回调
	 */
	private ResponseCallBack callBack = new ResponseCallBack() {

		@Override
		public void response(String msage, int what, int index) {
			loadingToast.dismiss();
			if (-1 != index) {
				toast.showToast(error[index]);
				return;
			}
			JsonMap<String, Object> data = JsonParseHelper.getJsonMap(msage);
			Log.d(TAG, String.format(getString(R.string.tojson), what) + data);
			if ("1".equals(data.getStringNoNull("ResultFlag"))) {
				JsonMap<String, Object> data_ = data
						.getJsonMap("MessageContent");
				if (getData.isOk(data_)) {
					if (0 == what) {
						toast.showToast(data_.getJsonMap("ResponseStatus")
								.getStringNoNull("Message"));
						/*
						 * 沉睡1秒
						 */
						RunMITStaticQueue queue = new RunMITStaticQueue();
						queue.setCls(Thread.class);
						queue.setMethodName("sleep");
						queue.setParms(new Object[] { 1000 });
						queue.setCallBack(new IRunMITCallBack() {

							@Override
							public void onRuned(RunMITQueue queue) {
								FeedBackActivity.this.finish();
							}
						});
						runMITUtil.runQueue(queue);
					}
				}

			} else {
				toast.showToast(data.getStringNoNull("ErrorMessage"));
			}

		}
	};
}
