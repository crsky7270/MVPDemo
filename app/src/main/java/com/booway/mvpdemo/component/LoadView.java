package com.booway.mvpdemo.component;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.booway.mvpdemo.DemoApplicatoin;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.TmpApplication;
import com.booway.mvpdemo.data.entities.Demo;
import com.booway.mvpdemo.utils.DisplayUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 创建人：万吨
 * 创建时间：2018/12/17
 * 描述：全局加载框
 */

public class LoadView {

    private static LoadView sLoadView;

    private static AlertDialog.Builder sBuilder;

    public static AlertDialog sDialog;

    public static LVCircular sLVCircular;

    public static LoadView getInstance(Context context) {
        if (sLoadView == null) {
            synchronized (LoadView.class) {
                if (sLoadView == null)
                    sLoadView = new LoadView();
                sBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
                Observable.just(sBuilder)
                        .doOnNext(builder -> {
                            sLVCircular = LVCircular.getInstance();
                            sLVCircular.setViewColor(Color.rgb(255, 99, 99));
                            sLVCircular.setRoundColor(Color.rgb(255, 0, 0));
                            builder.setView(sLVCircular);
                            builder.setCancelable(false);
                        })
                        .subscribe(builder -> {
                            sDialog = builder.create();
                        });
            }
        }
        return sLoadView;
    }

    private void init() {

    }


    public void show() {
        sLVCircular.startAnim();
        sDialog.show();
        android.view.WindowManager.LayoutParams p = sDialog.getWindow().getAttributes();
        p.height = DisplayUtils.dip2px(DemoApplicatoin.context, 100);
        p.width = DisplayUtils.dip2px(DemoApplicatoin.context, 100);
        sDialog.getWindow().setAttributes(p);
    }

    public void close() {
        sLVCircular.stopAnim();
        sDialog.dismiss();
        ((ViewGroup)sLVCircular.getParent()).removeAllViews();
        sLVCircular = null;
        sDialog = null;
        sBuilder = null;
        sLoadView = null;

    }


    //    public void showLoadingView() {
//        builder.setView(CircularLoadingView.getInstance());
//        builder.create().getWindow().setLayout(DisplayUtils.dip2px(DemoApplicatoin.context, 50),
//                DisplayUtils.dip2px(DemoApplicatoin.context, 50));
//        builder.show();
//    }


}
