package com.booway.mvpdemo.DemoList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.booway.mvpdemo.BookList.BookListContract;
import com.booway.mvpdemo.DemoApplicatoin;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.data.DemoRespository;
import com.booway.mvpdemo.data.entities.Book;
import com.booway.mvpdemo.di.ActivityScoped;
import com.booway.mvpdemo.retrofit.Demo;
import com.booway.mvpdemo.retrofit.DemoListAPI;
import com.booway.mvpdemo.retrofit.DemoListPostAPI;
import com.booway.mvpdemo.retrofit.DemoListService;
import com.booway.mvpdemo.switchdemo.SwitchDemoActivity;
import com.booway.mvpdemo.utils.SerializableUtils;
import com.booway.mvpdemo.utils.StringUtils;
import com.booway.mvpdemo.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wandun on 2018/11/23.
 */

@ActivityScoped
public class DemoListFragment extends DaggerFragment implements DemoListContract.View {

    @Nullable
    public final static String ARGUMENT_EDIT_DEMO_ID = "DemoId";

    @BindView(R.id.id)
    EditText _idTxt;

    @BindView(R.id.name)
    EditText _nameTxt;

    @BindView(R.id.save)
    Button _saveBtn;

    @BindView(R.id.get)
    Button _getBtn;

    @BindView(R.id.listView)
    ListView _listView;

    private Unbinder mUnbinder;

    @Inject
    DemoListContract.Presenter mPresenter;

    @Inject
    BookListContract.Presenter mBookPresenter;

    private DemoListAdapter mDemoListAdapter;

    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/demo.dat";

    @Inject
    @Nullable
    public String mid;


    @Inject
    public DemoListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Toast.makeText(getActivity(), "hidden changed:" +
//                (hidden ? "0" : "1"), Toast.LENGTH_SHORT).show();
        ToastUtils.showToast("hidden changed:" + (hidden ? "0" : "1"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPause() {
        mPresenter.dropView();
        super.onPause();
    }

    DemoItemListener mItemListener = new DemoItemListener() {
        @Override
        public void onDemoClick(com.booway.mvpdemo.data.entities.Demo clickedDemo) {
            Toast toast = Toast.makeText(getActivity(), clickedDemo.toString(), Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        public void onCompleteDemoClick(com.booway.mvpdemo.data.entities.Demo completedDemo) {

        }

        @Override
        public void onActivateTaskClick(com.booway.mvpdemo.data.entities.Demo activatedDemo) {

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDemoListAdapter = new DemoListAdapter(new ArrayList<>(), mItemListener);
        TestPostMethod();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_demo_list_frag, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        _listView.setAdapter(mDemoListAdapter);
        return root;
    }

    @OnClick(R.id.get)
    public void Get() {
        mPresenter.getDemo(_idTxt.getText().toString());
    }

    @OnClick(R.id.save)
    public void Save() {
        com.booway.mvpdemo.data.entities.Demo demo = new com.booway.mvpdemo.data.entities.Demo(
                _idTxt.getText().toString(), _nameTxt.getText().toString(), 18
        );
        mPresenter.saveDemo(demo);
    }

    @OnClick(R.id.saveBook)
    public void saveBook() {
        Book book = new Book(_nameTxt.getText().toString(), _idTxt.getText().toString(),
                _idTxt.getText().toString());
        mBookPresenter.saveBook(book);
    }

    @OnClick(R.id.openActivity)
    public void open() {
        Intent intent = new Intent(getActivity(), SwitchDemoActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.getList)
    public void GetList() {
        mPresenter.getDemoList(true);
    }

    @OnClick(R.id.getUnionList)
    public void getUnionList() {
        mPresenter.getUnionList();
    }

    @OnClick(R.id.getRxjavaUnionList)
    public void getRxjavaUnionList() {
        mPresenter.getRxjavaUnionList();
    }

    private void TestSerializeWrite() {
        Demo demo = new Demo();
        demo.Id = 10002;
        demo.Name = "张三";


        try {
            SerializableUtils.serializeData(getActivity(), path, demo);
        } catch (Exception ex) {

        }
    }

    private void TestSerializeRead() {
        Demo demo;
        try {
            Object object = SerializableUtils.deserializeData(getActivity(), path);
            demo = (Demo) object;
        } catch (Exception ex) {

        }
    }

    private void TestGetHttpMethod() {
        DemoListAPI service = DemoListService.createDemoListService("demo");
        service.getDemoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void TestPostMethod() {
        Demo demo = new Demo();
        demo.Id = 10002;
        demo.Name = "张三";
//        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
//                new Gson().toJson(demo));
        DemoListPostAPI service = DemoListService.createDemoListPostService("demo");
        service.postDemos(demo)
                .doOnNext(result -> {
                    //do somethings you want
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void showDemoList(List<com.booway.mvpdemo.data.entities.Demo> demos) {
        mDemoListAdapter.replaceData(demos);
        _listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDemo(com.booway.mvpdemo.data.entities.Demo demo) {
        _idTxt.setText(demo.getId());
        _nameTxt.setText(demo.getName());
    }

    @Override
    public void showResult(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public interface DemoItemListener {

        void onDemoClick(com.booway.mvpdemo.data.entities.Demo clickedDemo);

        void onCompleteDemoClick(com.booway.mvpdemo.data.entities.Demo completedDemo);

        void onActivateTaskClick(com.booway.mvpdemo.data.entities.Demo activatedDemo);
    }

    private static class DemoListAdapter extends BaseAdapter {

        private List<com.booway.mvpdemo.data.entities.Demo> mDemoList;
        private DemoItemListener mDemoItemListener;

        public DemoListAdapter(List<com.booway.mvpdemo.data.entities.Demo> demos,
                               DemoItemListener itemListener) {
            setList(demos);
            this.mDemoItemListener = itemListener;
        }

        private void setList(List<com.booway.mvpdemo.data.entities.Demo> demoList) {
            mDemoList = checkNotNull(demoList);
        }

        public void replaceData(List<com.booway.mvpdemo.data.entities.Demo> demos) {
            setList(demos);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDemoList.size();
        }

        @Override
        public com.booway.mvpdemo.data.entities.Demo getItem(int position) {
            return mDemoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.demo_list_item, viewGroup, false);
            }

            final com.booway.mvpdemo.data.entities.Demo demo = getItem(i);

            TextView textViewId = rowView.findViewById(R.id.id);
            TextView textViewName = rowView.findViewById(R.id.name);
            textViewId.setText(demo.getId());
            textViewName.setText(demo.getName());

            rowView.setOnClickListener((v) -> {
                mDemoItemListener.onDemoClick(demo);
            });

            return rowView;
        }
    }
}
