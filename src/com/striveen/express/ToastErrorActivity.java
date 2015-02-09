package com.striveen.express;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.striveen.express.util.ExtraKeys;
import com.striveen.express.view.ViewInject;

public class ToastErrorActivity extends MyActivity {

	@ViewInject(id = R.id.toast_error_tv_context)
	private TextView tv_context;
	@ViewInject(id = R.id.exit_layout)
	private LinearLayout exit_layout;
	@ViewInject(id = R.id.toast_error_tv_know, click = "tv_know")
	private TextView tv_know;
	private String error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_error);
		error = getIntent().getStringExtra(ExtraKeys.Key_Msg1);
		tv_context.setText(error);
		exit_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void tv_know(View v) {
		this.finish();
	}
}
