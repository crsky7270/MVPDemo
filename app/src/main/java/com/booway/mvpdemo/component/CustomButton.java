package com.booway.mvpdemo.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import com.booway.mvpdemo.R;

import java.util.jar.Attributes;

/**
 * Created by wandun on 2018/12/6.
 *
 * xmlns:myView="http://schemas.android.com/apk/res-auto"
 *
 * <com.booway.mvpdemo.component.CustomButton
 * android:layout_width="match_parent"
 * android:layout_height="36dp"
 * myView:isMin="true"/>
 */

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
//        init();
    }

    public CustomButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int style) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomButton, style, 0);
        boolean isMin = ta.getBoolean(R.styleable.CustomButton_isMin, false);
        //do som busyniess ......
    }

}
