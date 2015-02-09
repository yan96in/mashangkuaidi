package com.striveen.express.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.striveen.express.R;
import com.striveen.express.util.JsonMap;

public class DialogAdapter extends BaseAdapter {

	List<JsonMap<String, Object>> data;
	Context context;
	private int selectedPosition = 0;// 选中的位置

	public DialogAdapter(Context context, List<JsonMap<String, Object>> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_dialog_item, null);
		RelativeLayout rl_top = (RelativeLayout) convertView
				.findViewById(R.id.item_dialog_item_rl_top);
		ImageView iv_left = (ImageView) convertView
				.findViewById(R.id.item_dialog_item_iv_left);
		TextView tv_title = (TextView) convertView
				.findViewById(R.id.item_dialog_item_tv_title);
		if (selectedPosition == position) {
			rl_top.setBackgroundColor(context.getResources().getColor(
					R.color.index3_dialog_select));
			iv_left.setVisibility(View.VISIBLE);
			tv_title.setTextColor(context.getResources().getColor(
					R.color.TextColorWhite));
		} else {
			rl_top.setBackgroundColor(context.getResources().getColor(
					R.color.index3_dialog_no_select));
			iv_left.setVisibility(View.INVISIBLE);
			tv_title.setTextColor(Color.BLACK);
		}
		// if (0 == position) {
		// rl_top.setBackgroundColor(context.getResources().getColor(
		// R.color.index3_dialog_select));
		// iv_left.setVisibility(View.VISIBLE);
		// } else {
		// rl_top.setBackgroundColor(context.getResources().getColor(
		// R.color.index3_dialog_no_select));
		// iv_left.setVisibility(View.GONE);
		// }
		tv_title.setText(data.get(position).getStringNoNull("Reason"));
		return convertView;
	}
}
