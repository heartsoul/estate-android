package com.glodon.bim.customview.datepicker;

import android.app.Activity;


import java.util.HashMap;
import java.util.Map;

/**
 * Description:日期选择工具类
 * Created by 李亚东 ON 2015/9/8
 * Job number:137124
 * Phone:13001292135
 * Email:liyadong@syswin.com
 * Person in charge:周瑞峰
 * Leader:周瑞峰
 */
public class TNBCustomDatePickerUtils {


	/**
	 * 日期和时间选择
	 * @param context
	 * @param listener
	 */
	public static void showDatePicker(final Activity context,
			final OnDateSelectedListener listener) {

		TNBDateDialog.showDateDialog(context, new OnDateSelectedListener() {
			@Override
			public void onDateSelected(Map<String, Integer> map) {
				final Map<String,Integer> params = new HashMap<String, Integer>();
				params.putAll(map);

				TNBDateDialog.showTimeDialog(context, new OnDateSelectedListener() {
					@Override
					public void onDateSelected(Map<String, Integer> map) {
						params.putAll(map);
						listener.onDateSelected(params);
					}
				});
			}
		});
	}
	/**
	 * 日期选择
	 * @param context
	 * @param listener
	 */
	public static void showDayPicker(final Activity context,
			final OnDateSelectedListener listener) {
		TNBDateDialog.showDateDialog(context,listener);
	}

	/**
	 * 时间选择器
	 * @param context
	 * @param listener
	 */
	public static void showTimePicker(final Activity context,
			final OnDateSelectedListener listener) {
		TNBDateDialog.showTimeDialog(context,listener);
	}



	/**
	 * 
	 * @Description: 日期时间选择的回调
	 * @author 周瑞峰
	 * @date 2015-9-8
	 */
	public interface OnDateSelectedListener {
		void onDateSelected(Map<String, Integer> map);
	}
}
