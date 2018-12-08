package com.booway.mvpdemo.switchdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.di.FragmentScoped;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;


/**
 * Created by wandun on 2018/12/7.
 */
@ActivityScoped
public class SwitchDemoFragment extends DaggerFragment implements SwitchDemoContract.View {

    private Unbinder mUnbinder;

    @Inject
    public SwitchDemoFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_switch_demo, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        return root;
    }
}
