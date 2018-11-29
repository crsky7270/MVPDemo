package com.booway.mvpdemo.DemoList;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booway.mvpdemo.R;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.retrofit.Demo;
import com.booway.mvpdemo.retrofit.DemoListAPI;
import com.booway.mvpdemo.retrofit.DemoListPostAPI;
import com.booway.mvpdemo.retrofit.DemoListService;
import com.booway.mvpdemo.utils.SerializeUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wandun on 2018/11/23.
 */

@ActivityScoped
public class DemoListFragment extends DaggerFragment implements DemoListContract.View {

    //    @Inject
//    DemoListContract.Presenter mPresenter;
    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/demo.dat";

    @Inject
    public DemoListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Demo demo = new Demo();
//        demo.Id = 10002;
//        demo.Name = "张三";
        Object object = SerializeUtils.ReadFromFile(path);
        demo = (Demo) object;
        //SerializeUtils.WriteToFile(demo, Environment.getExternalStorageDirectory().getAbsolutePath()+"/download/demo.dat");
//        DemoListPostAPI service = DemoListService.createDemoListPostService("demo");
//        service.postDemos()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


        //        DemoListAPI service = DemoListService.createDemoListService("demo");
//        service.getDemoList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new Observer<List<String>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<String> strings) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        View root = inflater.inflate(R.layout.activity_demo_list_frag, container, false);


        return root;
    }
}
