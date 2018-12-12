package com.booway.mvpdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import java.util.List;

import io.reactivex.Observable;


/**
 * 创建人：万吨
 * 创建时间：20181211
 * 描述：提示框辅助类，包括常用对话框、单选对话框、多选对话框
 */
public class AlertDialogUtils {

    public static class AlertDialogParam {

        public String key;

        public String val;

        public boolean isChecked;
    }

    private static AlertDialogUtils mAlertDialogUtil;
    public static AlertDialog.Builder builder;

    /**
     * 保障对象的单例
     *
     * @param mContent
     * @return
     */
    public static AlertDialogUtils getInstance(Context mContent) {
        if (mAlertDialogUtil == null) {
            synchronized (AlertDialogUtils.class) {
                if (mAlertDialogUtil == null)
                    mAlertDialogUtil = new AlertDialogUtils();
                builder = new AlertDialog.Builder(mContent);
            }
        }
        return mAlertDialogUtil;
    }

    /**
     * 单个按钮对话框
     *
     * @param title
     * @param msg
     * @param iconResId
     * @param positiveBtnText
     * @param posListener
     */
    public void showAlertDialog(@Nullable String title, @Nullable String msg,
                                @Nullable int iconResId, String positiveBtnText,
                                DialogInterface.OnClickListener posListener) {
        builder.setTitle(title).setIcon(iconResId).setMessage(msg)
                .setPositiveButton(positiveBtnText, posListener)
                .show();
    }

    /**
     * 有两个按钮对话框
     *
     * @param title
     * @param msg
     * @param iconResId
     * @param negativeBtnText
     * @param positiveBtnText
     * @param negListener
     * @param posListener
     */
    public void showAlertDialog(@Nullable String title, @Nullable String msg, @Nullable int iconResId,
                                String negativeBtnText, String positiveBtnText,
                                DialogInterface.OnClickListener negListener,
                                DialogInterface.OnClickListener posListener) {
        builder.setTitle(title).setIcon(iconResId).setMessage(msg)
                .setNegativeButton(negativeBtnText, negListener)
                .setPositiveButton(positiveBtnText, posListener)
                .show();
    }

    /**
     * 三个按钮对话框
     *
     * @param title
     * @param msg
     * @param iconResId
     * @param negativeBtnText
     * @param positiveBtnText
     * @param neutralBtnText
     * @param negListener
     * @param posListener
     * @param neuListener
     */
    public void showAlertDialog(@Nullable String title, @Nullable String msg, @Nullable int iconResId,
                                String negativeBtnText, String positiveBtnText, String neutralBtnText,
                                DialogInterface.OnClickListener negListener,
                                DialogInterface.OnClickListener posListener,
                                DialogInterface.OnClickListener neuListener) {
        builder.setTitle(title).setIcon(iconResId).setMessage(msg)
                .setNegativeButton(negativeBtnText, negListener)
                .setPositiveButton(positiveBtnText, posListener)
                .setNeutralButton(neutralBtnText, neuListener)
                .show();
    }

    /**
     * 单选对话框
     *
     * @param title
     * @param iconResId
     * @param params
     * @param listener
     */
    public void showSingleSelectDialog(@Nullable String title, @Nullable int iconResId,
                                       List<AlertDialogParam> params,
                                       DialogInterface.OnClickListener listener) {
        Observable.fromIterable(params)
                .map(alertDialogParam -> alertDialogParam.val).toList()
                .subscribe(result -> {
                            String[] showTexts = result.toArray(new String[result.size()]);
                            builder.setTitle(title)
                                    .setIcon(iconResId)
                                    .setItems(showTexts, listener)
                                    .show();
                        }
                );
    }

    /**
     * 多选对话框
     *
     * @param title
     * @param iconResId
     * @param params
     * @param negativeBtnText
     * @param positiveBtnText
     * @param negListener
     * @param posListener
     * @param multiChoiceListener
     */
    public void showMutliSelectedDialog(@Nullable String title, @Nullable int iconResId,
                                        List<AlertDialogParam> params,
                                        String negativeBtnText, String positiveBtnText,
                                        DialogInterface.OnClickListener negListener,
                                        DialogInterface.OnClickListener posListener,
                                        DialogInterface.OnMultiChoiceClickListener multiChoiceListener) {
        String[] showTexts = new String[params.size()];
        boolean[] isCheckeds = new boolean[params.size()];
        //由于Map以及Boolean对象转换问题，所以不用Rxjava表示更简洁
        for (int i = 0; i < showTexts.length; i++) {
            showTexts[i] = params.get(i).val;
            isCheckeds[i] = params.get(i).isChecked;
        }
        builder.setTitle(title).setIcon(iconResId)
                .setNegativeButton(negativeBtnText, negListener)
                .setPositiveButton(positiveBtnText, posListener)
                .setMultiChoiceItems(showTexts, isCheckeds, multiChoiceListener)
                .show();
    }
}
