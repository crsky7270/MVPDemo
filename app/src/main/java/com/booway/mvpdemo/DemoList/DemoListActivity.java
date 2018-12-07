package com.booway.mvpdemo.DemoList;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.booway.mvpdemo.DemoDetails.DemoDetailsFragment;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class DemoListActivity extends DaggerAppCompatActivity {
    @Inject
    DemoListFragment mFragment;

    @Inject
    DemoDetailsFragment mDemoDetailsFragment;

//    @Inject
//    DemoListPresenter mPresenter;
//    @BindView(R.id.contentFrame)
//    FrameLayout mFrameLayout;

    @BindView(R.id.fab_switch_frag)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);
        ButterKnife.bind(this);

        DemoListFragment fragment =
                (DemoListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null)
            fragment = mFragment;
//        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
//                fragment, R.id.contentFrame);
        ActivityUtils.switchFragment(getSupportFragmentManager(), fragment, R.id.contentFrame);
    }

    @OnClick(R.id.fab_switch_frag)
    public void switchfFragment() {
        if (ActivityUtils.currentFragment.getClass().getName()
                .equals(mDemoDetailsFragment.getClass().getName())) {
            ActivityUtils.switchFragment(getSupportFragmentManager(),
                    mFragment, R.id.contentFrame);
        } else {
            ActivityUtils.switchFragment(getSupportFragmentManager(),
                    mDemoDetailsFragment, R.id.contentFrame);
        }
//        Toast.makeText(this, "switch!!!", Toast.LENGTH_SHORT).show();
    }
}
