package com.booway.mvpdemo.DemoList;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
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
        ActivityUtils.switchFragment(DemoListActivity.class,
                getSupportFragmentManager(), fragment, R.id.contentFrame);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @OnClick(R.id.fab_switch_frag)
    public void switchfFragment() {
        if (ActivityUtils.currentFragment.getClass().getName()
                .equals(mDemoDetailsFragment.getClass().getName())) {
            ActivityUtils.switchFragment(DemoListActivity.class, getSupportFragmentManager(),
                    mFragment, R.id.contentFrame);
        } else {
            ActivityUtils.switchFragment(DemoListActivity.class, getSupportFragmentManager(),
                    mDemoDetailsFragment, R.id.contentFrame);
        }
//        Toast.makeText(this, "switch!!!", Toast.LENGTH_SHORT).show();
    }
}
