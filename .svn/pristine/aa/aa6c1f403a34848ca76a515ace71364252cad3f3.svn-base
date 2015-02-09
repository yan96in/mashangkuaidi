package com.striveen.express.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.striveen.express.MyActivity;
import com.striveen.express.R;
import com.striveen.express.net.GetDataConfing;
import com.striveen.express.view.LayoutProductInfoInfoShowView;
import com.striveen.express.view.ViewInject;

/**
 * 联系我们
 * 
 * @author Fei
 * 
 */
public class ConnectUsActivity extends MyActivity {
	/**
	 * 联系我们详细信息
	 */
	@ViewInject(id = R.id.courier_guid_sv_info)
	private LayoutProductInfoInfoShowView sv_info;
	@ViewInject(id = R.id.courier_guid_tv_title)
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courier_guide);
		tv_title.setText(getString(R.string.set_connect_us));
		sv_info.loadUrl(GetDataConfing.ip1+"/help/contact.html");
	}
	public void iv_top_left(View v) {
		ConnectUsActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		if (null != sv_info) {
			sv_info.destroy();
		}
		super.onDestroy();
	}

	OnTouchListener wvTouchListener = new OnTouchListener() {
		private float OldX1, OldY1, OldX2, OldY2;
		private float NewX1, NewY1, NewX2, NewY2;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_POINTER_2_DOWN:
				if (event.getPointerCount() == 2) {
					OldX1 = event.getX(0);
					OldY1 = event.getY(0);
					OldX2 = event.getX(1);
					OldY2 = event.getY(1);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (event.getPointerCount() == 2) {
					if (OldX1 == -1 && OldX2 == -1)
						break;
					NewX1 = event.getX(0);
					NewY1 = event.getY(0);
					NewX2 = event.getX(1);
					NewY2 = event.getY(1);
					float disOld = (float) Math
							.sqrt((Math.pow(OldX2 - OldX1, 2) + Math.pow(OldY2
									- OldY1, 2)));
					float disNew = (float) Math
							.sqrt((Math.pow(NewX2 - NewX1, 2) + Math.pow(NewY2
									- NewY1, 2)));
					Log.i("onTouch", "disOld=" + disOld + "|disNew=" + disNew);
					if (disOld - disNew >= 25) {
						// 缩小
						// wv.zoomOut();
						// PDFShowView_1.this.loadUrl("javascript:mapScale=1;");
						sv_info.zoomOut();
						Log.i("onTouch", "zoomOut");
					} else if (disNew - disOld >= 25) {
						// 放大
						// wv.zoomIn();
						// PDFShowView_1.this.loadUrl("javascript:mapScale=-1;");
						sv_info.zoomIn();
						Log.i("onTouch", "zoomIn");
					}
					OldX1 = NewX1;
					OldX2 = NewX2;
					OldY1 = NewY1;
					OldY2 = NewY2;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (event.getPointerCount() < 2) {
					OldX1 = -1;
					OldY1 = -1;
					OldX2 = -1;
					OldY2 = -1;
				}
				break;
			}
			return false;
		}
	};
}
