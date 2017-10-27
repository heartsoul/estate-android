package com.glodon.bim.basic.utils;



import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
/**
 * 描述：输入法管理
 * 作者：zhourf on 2017/10/23
 * 邮箱：zhourf@glodon.com
 */
public class InputMethodutil {

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    //强制显示或者关闭系统键盘
    public static void keyBoard(final EditText txtSearchKey) {

        InputMethodManager m = (InputMethodManager)
                txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!m.isActive()) {
            m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
        } else {
            m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
        }
    }

    //通过定时器强制隐藏虚拟键盘
    public static void TimerHideKeyboard(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        }, 10);
    }

    //输入法是否显示着
    public static boolean isActive(EditText edittext) {
        boolean bool = false;
        InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            bool = true;
        }
        return bool;

    }
}