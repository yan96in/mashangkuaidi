package com.striveen.express.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 某个控件包含单击事件的数据适配器
 * 
 * @author Fei
 * 
 */
public class HasClickAdapter extends SimpleAsyImgAdapter {
	private final String TAG = HasClickAdapter.class.getSimpleName();
	private Context context;
	private List<? extends Map<String, ?>> data;
	private int resId;
	private ItemOneViewClickListener clickListener;

	/**
	 * 某个控件包含单击事件的数据适配器
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 * @param from
	 * @param to
	 */
	public HasClickAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to, int defaultImgResource) {
		super(context, data, resource, from, to, defaultImgResource);
	}

	/**
	 * 某个控件包含单击事件的数据适配器
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 * @param from
	 * @param to
	 * @param resId
	 * @param clickListener
	 */
	public HasClickAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to, int defaultImgResource, int resId,
			ItemOneViewClickListener clickListener) {
		this(context, data, resource, from, to, defaultImgResource);
		this.resId = resId;
		this.clickListener = clickListener;
	}

	/**
	 * 绑定对应id的view的点击事件
	 * 
	 * @param resId
	 *            对应id的view
	 * @param clickListener
	 *            点击事件
	 */
	public void bindViewOnClickListener(int resId,
			ItemOneViewClickListener clickListener) {
		this.resId = resId;
		this.clickListener = clickListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (resId != 0 && clickListener != null) {
			View v = view.findViewById(resId);
			if (v != null) {
				if (null != clickListener) {
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							clickListener.onClick(v, position);
						}
					});
				} else {
					v.setOnClickListener(null);
				}
			} else {
				Log.w(TAG, "not find view! ");
			}
		}
		return view;
	}

	/**
	 * adapter中的某个view的点击事件
	 * 
	 * @author Fei
	 * 
	 */
	public interface ItemOneViewClickListener {
		/**
		 * 事件处理
		 * 
		 * @param v
		 *            点击的view
		 * @param position
		 *            所在的adapter的position
		 */
		public void onClick(View v, int position);
	}

}
