package com.booway.mvpdemo.switchdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.DemoList.DemoListFragment;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.component.djisdk.DjiSdkComponent;
import com.booway.mvpdemo.utils.ActivityUtils;
import com.booway.mvpdemo.utils.LogUtils;
import com.booway.mvpdemo.utils.ToastUtils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SwitchDemoActivity extends DaggerAppCompatActivity {
    private static final String TAG = SwitchDemoActivity.class.getName();

    @Inject
    SwitchDemoFragment mFragment;

    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

//    @Inject
//    DjiSdkComponent mDjiSdkComponent;

    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private List<String> missingPermission = new ArrayList<>();
    private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
    private static final int REQUEST_PERMISSION_CODE = 12345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_demo);
        ButterKnife.bind(this);

//        DJISDKManager manager = DJISDKManager.getInstance();

        checkAndRequestPermissions();

        SwitchDemoFragment fragment =
                (SwitchDemoFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (fragment == null)
            fragment = mFragment;
        ActivityUtils.switchFragment(SwitchDemoActivity.class,
                getSupportFragmentManager(), fragment, R.id.contentFrame);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(2);
        Observable.fromIterable(list).sorted().subscribe(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.d(integer + "");
                    }
                }
        );


//        Observable.create((ObservableOnSubscribe<Integer>) e -> {
//            for (int i = 1; i <= 5; i++) {
//                e.onNext(i);
//            }
//        }).concatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                final List<String> list = new ArrayList<>();
//                for (int i = 0; i < 3; i++) {
//                    list.add("I am integer " + integer + "");
//                }
//                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
//            }
//        }).subscribe(e -> {
//            LogUtils.d(e);
//        });

//        Flowable.interval(200,TimeUnit.MILLISECONDS).

//        Flowable.just(1).interval(200,TimeUnit.MILLISECONDS);
//
//        Flowable.interval(200,TimeUnit.MILLISECONDS);
//
//        Flowable flowable=Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
//                e.onNext(1);
//            }
//        }, BackpressureStrategy.BUFFER);

//        Flowable.interval(200,TimeUnit.MILLISECONDS).

//        Flowable.interval(3,TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        LogUtils.d(aLong+++"");
//                    }
//                });

//        Observable.create(e->{
//
//        }).filter().subscribeOn().observeOn().subscribe();

//        Observable.create((ObservableOnSubscribe<Integer>) e -> {
//            e.onNext(1);
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(e -> {
//
//                }).flatMap(new Function<Integer, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(Integer integer) throws Exception {
//                return Observable.create(e -> {
//                    e.onNext(1);
//                });
//            }
//        }).observeOn(Schedulers.io())
//                .subscribe();
//
//
//        Observable<Integer> ob1 = Observable.just(1);
//        Observable<String> ob2 = Observable.just("a");
//        Observable.zip(ob1, ob2, new BiFunction<Integer, String, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer, String s) throws Exception {
//                return null;
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    private void checkAndRequestPermissions() {
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }

        if (missingPermission.isEmpty()) {
            startSDKRegistration();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Request for missing permissions
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * Result of runtime permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check for granted permission and remove from missing list
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    missingPermission.remove(permissions[i]);
                }
            }
        }
        // If there is enough permission, we will start the registration
        if (missingPermission.isEmpty()) {
//            startSDKRegistration();
        } else {
//            ToastUtils.setResultToToast("请检查应用权限是否都已开启");
        }
    }


//    private void startSDKRegistration() {
//        if (isRegistrationInProgress.compareAndSet(false, true)) {
//            ToastUtils.showToast("大疆SDK注册中...，请稍后");
//            Flowable.just(getApplicationContext())
//                    .flatMap(context -> mDjiSdkComponent.Register(context))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(s -> {
//                        ToastUtils.showToast(s);
//                    });
//        }
//    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SwitchDemoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSDKRegistration() {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    showToast( "registering, pls wait...");
                    DJISDKManager.getInstance().registerApp(getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
                        @Override
                        public void onRegister(DJIError djiError) {
                            if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                                DJILog.e("App registration", DJISDKError.REGISTRATION_SUCCESS.getDescription());
                                DJISDKManager.getInstance().startConnectionToProduct();
                                showToast("Register Success");
                            } else {
                                showToast( "Register sdk fails, check network is available");
                            }
                            Log.v(TAG, djiError.getDescription());
                        }

                        @Override
                        public void onProductDisconnect() {
                            Log.d(TAG, "onProductDisconnect");
                            showToast("Product Disconnected");

                        }
                        @Override
                        public void onProductConnect(BaseProduct baseProduct) {
                            Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));
                            showToast("Product Connected");

                        }
                        @Override
                        public void onComponentChange(BaseProduct.ComponentKey componentKey, BaseComponent oldComponent,
                                                      BaseComponent newComponent) {

                            if (newComponent != null) {
                                newComponent.setComponentListener(new BaseComponent.ComponentListener() {

                                    @Override
                                    public void onConnectivityChange(boolean isConnected) {
                                        Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
                                    }
                                });
                            }
                            Log.d(TAG,
                                    String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                                            componentKey,
                                            oldComponent,
                                            newComponent));

                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
            Intent attachedIntent = new Intent();
            attachedIntent.setAction(DJISDKManager.USB_ACCESSORY_ATTACHED);
            sendBroadcast(attachedIntent);
        }
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
