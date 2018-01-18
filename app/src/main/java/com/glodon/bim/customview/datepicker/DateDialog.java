package com.glodon.bim.customview.datepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.glodon.bim.customview.datepicker.adapter.NumericDateAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:日期view
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public class DateDialog {
    //日期选择框
    public static void showDateDialog(final Activity context, final CustomDatePickerUtils.OnDateSelectedListener listener) {
        Calendar c = Calendar.getInstance();
        final int curYear = c.get(Calendar.YEAR);
        final int curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DATE);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.HORIZONTAL);

        final DateView firstWheel = new DateView(context);
        final NumericDateAdapter firstNumericWheelAdapter = new NumericDateAdapter(context, 1, 10000);
        firstNumericWheelAdapter.setLabel("年");
        firstWheel.setViewAdapter(firstNumericWheelAdapter);
        firstWheel.setCyclic(false);

        final DateView secondWheel = new DateView(context);
        final NumericDateAdapter secondNumericWheelAdapter = new NumericDateAdapter(context, 1, 12, "%02d");
        secondNumericWheelAdapter.setLabel("月");
        secondWheel.setViewAdapter(secondNumericWheelAdapter);
        secondWheel.setCyclic(false);


        final DateView thirdWheel = new DateView(context);
        final NumericDateAdapter thirdNumericWheelAdapter = new NumericDateAdapter(context, 1, getDay(curYear, curMonth), "%02d");
        thirdNumericWheelAdapter.setLabel("日");
        thirdWheel.setViewAdapter(thirdNumericWheelAdapter);
        thirdWheel.setCyclic(false);

        firstWheel.setVisibleItems(7);
        secondWheel.setVisibleItems(7);
        thirdWheel.setVisibleItems(7);

        firstWheel.setCurrentItem(curYear - 1);
        secondWheel.setCurrentItem(curMonth - 1);
        thirdWheel.setCurrentItem(curDate - 1);
        LayoutParams firstParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        LayoutParams secondParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        LayoutParams thirdParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        firstParams.weight = 1;
        secondParams.weight = 1;
        thirdParams.weight = 1;

        secondWheel.addChangingListener(new OnDateChangedListener() {
            @Override
            public void onChanged(DateView wheel, int oldValue, int newValue) {

                int first = firstWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String firstStr = (String) firstNumericWheelAdapter.getItemText(first);
                String secondStr = (String) secondNumericWheelAdapter.getItemText(newValue);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                int year= Integer.parseInt(firstStr);
                int month= Integer.parseInt(secondStr);
                if(TextUtils.isEmpty(thirdStr)){
                    thirdStr = getDay(year, month)+"";
                }
                int date= Integer.parseInt(thirdStr);
                NumericDateAdapter thirdAdapter = new NumericDateAdapter(context, 1, getDay(year, month), "%02d");
                thirdAdapter.setLabel("日");
                thirdWheel.setViewAdapter(thirdAdapter);
                int current = date>getDay(year, month)?getDay(year, month):date;
                thirdWheel.setCurrentItem(current - 1);
            }
        });

        firstWheel.addChangingListener(new OnDateChangedListener() {
            @Override
            public void onChanged(DateView wheel, int oldValue, int newValue) {
                int second = secondWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String secondStr = (String) secondNumericWheelAdapter.getItemText(second);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                int month= Integer.parseInt(secondStr);
                if(TextUtils.isEmpty(thirdStr)){
                    thirdStr = getDay(newValue, month)+"";
                }
                int date= Integer.parseInt(thirdStr);
                NumericDateAdapter thirdAdapter = new NumericDateAdapter(context, 1, getDay(newValue, month), "%02d");
                thirdAdapter.setLabel("日");
                thirdWheel.setViewAdapter(thirdAdapter);
                int current = date>getDay(newValue, month)?getDay(newValue, month):date;
                thirdWheel.setCurrentItem(current - 1);
            }
        });



        view.addView(firstWheel, firstParams);
        view.addView(secondWheel, secondParams);
        view.addView(thirdWheel, thirdParams);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int first = firstWheel.getCurrentItem();
                int second = secondWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String firstStr = (String) firstNumericWheelAdapter.getItemText(first);
                String secondStr = (String) secondNumericWheelAdapter.getItemText(second);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                final Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("year", Integer.parseInt(firstStr));
                map.put("month", Integer.parseInt(secondStr));
                map.put("approachDate", Integer.parseInt(thirdStr));
                listener.onDateSelected(map);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    //时间选择框
    public static void showTimeDialog(Activity context, final CustomDatePickerUtils.OnDateSelectedListener listener) {
        Calendar c = Calendar.getInstance();
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMinute = c.get(Calendar.MINUTE);
        int curSecond = c.get(Calendar.SECOND);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.HORIZONTAL);

        final DateView firstWheel = new DateView(context);
        final NumericDateAdapter firstNumericWheelAdapter = new NumericDateAdapter(context, 0, 23, "%02d");
        firstNumericWheelAdapter.setLabel("时");
        firstWheel.setViewAdapter(firstNumericWheelAdapter);
        firstWheel.setCyclic(true);

        final DateView secondWheel = new DateView(context);
        final NumericDateAdapter secondNumericWheelAdapter = new NumericDateAdapter(context, 0, 59, "%02d");
        secondNumericWheelAdapter.setLabel("分");
        secondWheel.setViewAdapter(secondNumericWheelAdapter);
        secondWheel.setCyclic(true);


        final DateView thirdWheel = new DateView(context);
        final NumericDateAdapter thirdNumericWheelAdapter = new NumericDateAdapter(context, 0, 59, "%02d");
        thirdNumericWheelAdapter.setLabel("秒");
        thirdWheel.setViewAdapter(thirdNumericWheelAdapter);
        thirdWheel.setCyclic(true);

        firstWheel.setVisibleItems(7);
        secondWheel.setVisibleItems(7);
        thirdWheel.setVisibleItems(7);

        firstWheel.setCurrentItem(curHour);
        secondWheel.setCurrentItem(curMinute);
        thirdWheel.setCurrentItem(curSecond);
        LayoutParams firstParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        LayoutParams secondParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        LayoutParams thirdParams = new LayoutParams(0,
                LayoutParams.MATCH_PARENT);
        firstParams.weight = 1;
        secondParams.weight = 1;
        thirdParams.weight = 1;
        view.addView(firstWheel, firstParams);
        view.addView(secondWheel, secondParams);
        view.addView(thirdWheel, thirdParams);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int first = firstWheel.getCurrentItem();
                int second = secondWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String firstStr = (String) firstNumericWheelAdapter.getItemText(first);
                String secondStr = (String) secondNumericWheelAdapter.getItemText(second);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                final Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("hour", Integer.parseInt(firstStr));
                map.put("minute", Integer.parseInt(secondStr));
                map.put("second", Integer.parseInt(thirdStr));
                listener.onDateSelected(map);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private static int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
}
