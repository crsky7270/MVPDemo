package com.booway.mvpdemo.switchdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.DemoList.DemoListFragment;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class SwitchDemoActivity extends DaggerAppCompatActivity {

    @Inject
    SwitchDemoFragment mFragment;

    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_demo);
        ButterKnife.bind(this);

        SwitchDemoFragment fragment =
                (SwitchDemoFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (fragment == null)
            fragment = mFragment;
        ActivityUtils.switchFragment(SwitchDemoActivity.class,
                getSupportFragmentManager(), fragment, R.id.contentFrame);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
