package com.glodon.bim.customview.datepicker;

import android.app.Activity;


import java.util.HashMap;
import java.util.Map;

/**
 * Description:日期选择工具类
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public class CustomDatePickerUtils {


	/**
	 * 日期和时间选择
	 * @param context
	 * @param listener
	 */
	public static void showDatePicker(final Activity context,
			final OnDateSelectedListener listener) {

		DateDialog.showDateDialog(context, new OnDateSelectedListener() {
			@Override
			public void onDateSelected(Map<String, Integer> map) {
				final Map<String,Integer> params = new HashMap<String, Integer>();
				params.putAll(map);

				DateDialog.showTimeDialog(context, new OnDateSelectedListener() {
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
		DateDialog.showDateDialog(context,listener);
	}

	/**
	 * 时间选择器
	 * @param context
	 * @param listener
	 */
	public static void showTimePicker(final Activity context,
			final OnDateSelectedListener listener) {
		DateDialog.showTimeDialog(context,listener);
	}

	/**
	 * 日期选择
	 * @param context
	 * @param listener
	 */
	public static void showDayDialog(final Activity context,
									 final OnDateSelectedListener listener) {
		CustomDateDialog dialog = new CustomDateDialog(context);
		dialog.builder(listener);
		dialog.show();
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
