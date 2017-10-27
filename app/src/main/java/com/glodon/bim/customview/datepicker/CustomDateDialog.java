package com.glodon.bim.customview.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.customview.datepicker.adapter.TNBNumericDateAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：自定义时间选择器
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */
public class CustomDateDialog {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    private TextView mSureView,mCancelView;
    private LinearLayout mContentView;

    public CustomDateDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public CustomDateDialog builder(final TNBCustomDatePickerUtils.OnDateSelectedListener listener) {
        View root = LayoutInflater.from(context).inflate(R.layout.view_custom_date_dialog,null);
        mSureView = root.findViewById(R.id.custom_date_dialog_sure);
        mContentView = root.findViewById(R.id.custom_date_dialog_content);
        mCancelView = root.findViewById(R.id.custom_date_dialog_cancel);

        Calendar c = Calendar.getInstance();
        final int curYear = c.get(Calendar.YEAR);
        final int curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DATE);

        final TNBDateView firstWheel = new TNBDateView(context);
        final TNBNumericDateAdapter firstNumericWheelAdapter = new TNBNumericDateAdapter(context, 1, 10000);
        firstNumericWheelAdapter.setLabel("年");
        firstWheel.setViewAdapter(firstNumericWheelAdapter);
        firstWheel.setCyclic(false);

        final TNBDateView secondWheel = new TNBDateView(context);
        final TNBNumericDateAdapter secondNumericWheelAdapter = new TNBNumericDateAdapter(context, 1, 12, "%02d");
        secondNumericWheelAdapter.setLabel("月");
        secondWheel.setViewAdapter(secondNumericWheelAdapter);
        secondWheel.setCyclic(false);


        final TNBDateView thirdWheel = new TNBDateView(context);
        final TNBNumericDateAdapter thirdNumericWheelAdapter = new TNBNumericDateAdapter(context, 1, getDay(curYear, curMonth), "%02d");
        thirdNumericWheelAdapter.setLabel("日");
        thirdWheel.setViewAdapter(thirdNumericWheelAdapter);
        thirdWheel.setCyclic(false);

        firstWheel.setVisibleItems(7);
        secondWheel.setVisibleItems(7);
        thirdWheel.setVisibleItems(7);

        firstWheel.setCurrentItem(curYear - 1);
        secondWheel.setCurrentItem(curMonth - 1);
        thirdWheel.setCurrentItem(curDate - 1);
        LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams secondParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams thirdParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        firstParams.weight = 1;
        secondParams.weight = 1;
        thirdParams.weight = 1;

        secondWheel.addChangingListener(new TNBOnDateChangedListener() {
            @Override
            public void onChanged(TNBDateView wheel, int oldValue, int newValue) {

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
                TNBNumericDateAdapter thirdAdapter = new TNBNumericDateAdapter(context, 1, getDay(year, month), "%02d");
                thirdAdapter.setLabel("日");
                thirdWheel.setViewAdapter(thirdAdapter);
                int current = date>getDay(year, month)?getDay(year, month):date;
                thirdWheel.setCurrentItem(current - 1);
            }
        });

        firstWheel.addChangingListener(new TNBOnDateChangedListener() {
            @Override
            public void onChanged(TNBDateView wheel, int oldValue, int newValue) {
                int second = secondWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String secondStr = (String) secondNumericWheelAdapter.getItemText(second);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                int month= Integer.parseInt(secondStr);
                if(TextUtils.isEmpty(thirdStr)){
                    thirdStr = getDay(newValue, month)+"";
                }
                int date= Integer.parseInt(thirdStr);
                TNBNumericDateAdapter thirdAdapter = new TNBNumericDateAdapter(context, 1, getDay(newValue, month), "%02d");
                thirdAdapter.setLabel("日");
                thirdWheel.setViewAdapter(thirdAdapter);
                int current = date>getDay(newValue, month)?getDay(newValue, month):date;
                thirdWheel.setCurrentItem(current - 1);
            }
        });



        mContentView.addView(firstWheel, firstParams);
        mContentView.addView(secondWheel, secondParams);
        mContentView.addView(thirdWheel, thirdParams);

        //设置点击事件
        mSureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int first = firstWheel.getCurrentItem();
                int second = secondWheel.getCurrentItem();
                int third = thirdWheel.getCurrentItem();
                String firstStr = (String) firstNumericWheelAdapter.getItemText(first);
                String secondStr = (String) secondNumericWheelAdapter.getItemText(second);
                String thirdStr = (String) thirdNumericWheelAdapter.getItemText(third);
                final Map<String, Integer> map = new HashMap<>();
                map.put("year", Integer.parseInt(firstStr));
                map.put("month", Integer.parseInt(secondStr));
                map.put("date", Integer.parseInt(thirdStr));
                listener.onDateSelected(map);

                dismiss();
            }
        });

        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        // 设置Dialog最小宽度为屏幕宽度
        root.setMinimumWidth(display.getWidth());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.transparentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(root);

        return this;
    }



    public CustomDateDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CustomDateDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        if(dialog!=null) {
            dialog.show();
        }
    }

    public void dismiss(){
        if(dialog!=null) {
            dialog.dismiss();
        }
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
