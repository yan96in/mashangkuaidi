package com.striveen.express.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.striveen.express.R;

/**
 * 五棵星星的view
 * 
 * @author Fei
 * 
 */
public class LayoutProductCommentStartView extends LinearLayout {
	/**
	 * 使用上下文
	 */
	private Context context;
	/**
	 * 展示的星星的个数
	 */
	private int startNum = 0;
	/**
	 * 五个星星的点击事件
	 */
	private OnClickListener clickListener;
	/**
	 * 5个星星的展示视图
	 */
	private ImageView iv_1, iv_2, iv_3, iv_4, iv_5;
	/**
	 * 5个星星的展示视图
	 */
	private ImageView[] iv_s = new ImageView[5];
	private int pictureId1[] = { R.drawable.user_order_datile_star,
			R.drawable.user_order_datile_no_star };
	private int pictureId2[] = { R.drawable.user_order_datile_star_da,
			R.drawable.user_order_datile_no_star_da };
	private int pictureId[];
	private StarOnClickCallBack callBack;
	/**
	 * 是否可以改变
	 */
	private boolean isOpenChange;

	public LayoutProductCommentStartView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public LayoutProductCommentStartView(Context context,
			StarOnClickCallBack callBack) {
		super(context);
		this.context = context;
		this.callBack = callBack;
		init();
	}

	@SuppressLint("NewApi")
	public LayoutProductCommentStartView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public LayoutProductCommentStartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * 初始化这个view
	 */
	private void init() {
		pictureId = pictureId1;
		clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpenChange) {
					if (v.equals(iv_1)) {
						startNum = 1;
					} else if (v.equals(iv_2)) {
						startNum = 2;
					} else if (v.equals(iv_3)) {
						startNum = 3;
					} else if (v.equals(iv_4)) {
						startNum = 4;
					} else if (v.equals(iv_5)) {
						startNum = 5;
					}
					if (null != callBack) {
						callBack.getNum(startNum);
					}
					flustShowStart();
				}
			}
		};

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_product_comment_start, this);

		iv_1 = (ImageView) findViewById(R.id.l_p_c_s_iv_1);
		iv_2 = (ImageView) findViewById(R.id.l_p_c_s_iv_2);
		iv_3 = (ImageView) findViewById(R.id.l_p_c_s_iv_3);
		iv_4 = (ImageView) findViewById(R.id.l_p_c_s_iv_4);
		iv_5 = (ImageView) findViewById(R.id.l_p_c_s_iv_5);

		iv_s[0] = iv_1;
		iv_s[1] = iv_2;
		iv_s[2] = iv_3;
		iv_s[3] = iv_4;
		iv_s[4] = iv_5;

		iv_1.setOnClickListener(clickListener);
		iv_2.setOnClickListener(clickListener);
		iv_3.setOnClickListener(clickListener);
		iv_4.setOnClickListener(clickListener);
		iv_5.setOnClickListener(clickListener);
		flustShowStart();
	}

	/**
	 * 设置显示那个星星
	 */
	public void setStar() {
		pictureId = pictureId2;
	}

	/**
	 * 刷新展示的星星的个数
	 */
	private void flustShowStart() {
		for (int i = 0; i < 5; i++) {
			if (i < startNum) {
				iv_s[i].setImageResource(pictureId[0]);
			} else {
				iv_s[i].setImageResource(pictureId[1]);
			}
		}
	}

	/**
	 * 展示的星星的个数
	 */
	public int getStartNum() {
		return startNum;
	}

	/**
	 * 展示的星星的个数
	 */
	public void setStartNum(int startNum) {
		if (startNum < 1) {
			startNum = 1;
		} else if (startNum > 5) {
			startNum = 5;
		}
		this.startNum = startNum;
		flustShowStart();
	}

	/**
	 * 展示的星星的个数
	 */
	public void setStartNum(int startNum, StarOnClickCallBack callBack) {
		if (startNum < 1) {
			startNum = 1;
		} else if (startNum > 5) {
			startNum = 5;
		}
		this.startNum = startNum;
		this.callBack = callBack;
		flustShowStart();
	}

	/**
	 * 是否可以改变
	 */
	public void setOpenChange(boolean isOpenChange) {
		this.isOpenChange = isOpenChange;
	}

	/**
	 * 星星点击事件回调
	 * 
	 * @author Fei
	 * 
	 */
	public interface StarOnClickCallBack {
		public void getNum(int num);
	}
}
