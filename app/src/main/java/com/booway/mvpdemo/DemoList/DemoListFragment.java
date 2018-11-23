package com.booway.mvpdemo.DemoList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booway.mvpdemo.di.ActivityScoped;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by wandun on 2018/11/23.
 */

@ActivityScoped
public class DemoListFragment extends DaggerFragment implements DemoListContract.View {

    @Inject
    public DemoListFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
