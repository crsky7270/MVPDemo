package com.booway.mvpdemo.switchdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.booway.mvpdemo.R;
import com.booway.mvpdemo.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.DaggerActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class SwitchDemoActivity extends DaggerAppCompatActivity {

    @Inject
    SwitchDemoFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_demo);

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
