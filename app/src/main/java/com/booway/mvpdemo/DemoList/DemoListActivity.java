package com.booway.mvpdemo.DemoList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.booway.mvpdemo.R;
import com.booway.mvpdemo.utils.ActivityUtils;

import javax.inject.Inject;

public class DemoListActivity extends AppCompatActivity {
    @Inject
    DemoListFragment mFragment;

    @Inject
    DemoListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);

        DemoListFragment fragment =
                (DemoListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null)
            fragment = mFragment;
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment, R.id.contentFrame);

    }
}
