package com.booway.mvpdemo.utils;

import android.app.Application;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.booway.mvpdemo.DemoApplicatoin;

import dagger.android.support.DaggerApplication;

/**
 * @author wandun
 * @date 2018/12/07
 * @description Toast封装类
 */
public class ToastUtils {
    private static Toast toast;

    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(DemoApplicatoin.context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }

    public static void showToast(int num) {
        if (toast == null) {
            toast = Toast.makeText(DemoApplicatoin.context, String.valueOf(num), Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(num));//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }

}
