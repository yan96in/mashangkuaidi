package com.striveen.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.striveen.express.R;
import com.striveen.express.util.JsonMap;

public class ExpressPreferencesAdapter extends HasClickAdapter {
	private List<JsonMap<String, Object>> data;

	public ExpressPreferencesAdapter(Context context,
			List<JsonMap<String, Object>> data, int resource, String[] from,
			int[] to, int defaultImgResource) {
		super(context, data, resource, from, to, defaultImgResource);
		this.data = data;
	}

	public void setSelectedPosition(int position, Boolean type) {
		data.get(position).put("IsDetest", type);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.item_express_preferences_iv_left);
		if (data.get(position).getBoolean("IsDetest")) {
			iv.setImageResource(R.drawable.item_express_preferences_iv_no);
		} else {
			iv.setImageResource(R.drawable.item_express_preferences_iv);
		}
		return convertView;
	}

}
