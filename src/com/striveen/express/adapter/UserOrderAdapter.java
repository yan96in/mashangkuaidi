package com.striveen.express.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.striveen.express.R;
import com.striveen.express.util.JsonMap;

/**
 * 订单列表
 * 
 * @author Fei
 * 
 */
public class UserOrderAdapter extends HasClickAdapter {
	private List<JsonMap<String, Object>> data;

	/**
	 * 订单类型 true 预约
	 */
	private boolean IsAppointment;
	/**
	 * 快递服务类型 1当日同城 2 普通同城 3普通异地
	 */
	private int ServerType;
	/**
	 * 终点
	 */
	private String ToAddress;
	/**
	 * 快递员名称
	 */
	private String CourierName;
	/**
	 * 物流名称
	 */
	private String LogisticsName;
	/**
	 * 订单状态 1 已创建 2 已推送 3 已抢单 4 已收件(快递员) 5 已取消 6 已送达 7已收件（发件人）
	 */
	private int OrderStatus;
	private Context context;
	/**
	 * 物流名称
	 */
	private TextView tv_logistics;
	/**
	 * 快递员名称
	 */
	private TextView tv_name;
	private TextView tv_time1;
	/**
	 * 预约
	 */
	private TextView tv_yuyue;
	private ImageView iv_type;
	/**
	 * 终点布局
	 */
	private RelativeLayout rl_end;

	public UserOrderAdapter(Context context,
			List<JsonMap<String, Object>> data, int resource, String[] from,
			int[] to, int defaultImgResource) {
		super(context, data, resource, from, to, defaultImgResource);
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		tv_time1 = (TextView) convertView
				.findViewById(R.id.user_order_tv_time1);
		tv_yuyue = (TextView) convertView
				.findViewById(R.id.user_order_iv_yuyue);
		tv_name = (TextView) convertView.findViewById(R.id.user_order_tv_name);
		tv_logistics = (TextView) convertView
				.findViewById(R.id.user_order_tv_logistics);

		iv_type = (ImageView) convertView.findViewById(R.id.user_order_iv_type);
		rl_end = (RelativeLayout) convertView
				.findViewById(R.id.user_order_rl_end);

		IsAppointment = data.get(position).getBoolean("IsAppointment");
		ServerType = data.get(position).getInt("ServerType");
		ToAddress = data.get(position).getStringNoNull("ToAddress");
		if (IsAppointment) {
			tv_yuyue.setVisibility(View.VISIBLE);
		} else {
			tv_yuyue.setVisibility(View.GONE);
		}

		OrderStatus = data.get(position).getInt("OrderStatus");
		if (3 <= OrderStatus && 5 != OrderStatus) {
			CourierName = data.get(position).getStringNoNull("CourierName");
			LogisticsName = data.get(position).getStringNoNull("LogisticsName");
			if (TextUtils.isEmpty(CourierName)) {
				CourierName = context.getResources().getString(
						R.string.user_order_tv_no);
			}
			if (TextUtils.isEmpty(LogisticsName)) {
				LogisticsName = context.getResources().getString(
						R.string.user_order_tv_no);
			}
		} else {
			CourierName = "";
			LogisticsName = "";
		}
		tv_name.setText(CourierName);
		tv_logistics.setText(LogisticsName);
		if (1 == ServerType) {
			iv_type.setImageResource(R.drawable.user_order_type1);
		} else if (2 == ServerType) {
			iv_type.setImageResource(R.drawable.user_order_type2);
		} else if (3 == ServerType) {
			iv_type.setImageResource(R.drawable.user_order_type3);
		}
		if (TextUtils.isEmpty(ToAddress)) {
			rl_end.setVisibility(View.GONE);
		} else {
			rl_end.setVisibility(View.VISIBLE);
		}

		return convertView;
	}
}
