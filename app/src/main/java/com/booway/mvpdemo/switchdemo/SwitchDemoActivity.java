package com.booway.mvpdemo.switchdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.booway.mvpdemo.DemoList.DemoListActivity;
import com.booway.mvpdemo.DemoList.DemoListFragment;
import com.booway.mvpdemo.R;
import com.booway.mvpdemo.component.djisdk.DjiSdkComponent;
import com.booway.mvpdemo.utils.ActivityUtils;
import com.booway.mvpdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import dji.sdk.sdkmanager.DJISDKManager;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SwitchDemoActivity extends DaggerAppCompatActivity {

    @Inject
    SwitchDemoFragment mFragment;

    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @Inject
    DjiSdkComponent mDjiSdkComponent;

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

//        ToastUtils.showToast("大疆SDK注册中...，请稍后");
//        Flowable.just(getApplicationContext())
//                .flatMap(context -> mDjiSdkComponent.Register(context)).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> {
//
//                    ToastUtils.showToast(s);
//                    SwitchDemoFragment fragment =
//                            (SwitchDemoFragment) getSupportFragmentManager()
//                                    .findFragmentById(R.id.contentFrame);
//                    if (fragment == null)
//                        fragment = mFragment;
//                    ActivityUtils.switchFragment(SwitchDemoActivity.class,
//                            getSupportFragmentManager(), fragment, R.id.contentFrame);
//                });
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
            startSDKRegistration();
        } else {
//            ToastUtils.setResultToToast("请检查应用权限是否都已开启");
        }
    }


    private void startSDKRegistration() {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            ToastUtils.showToast("大疆SDK注册中...，请稍后");
            Flowable.just(getApplicationContext())
                    .flatMap(context -> mDjiSdkComponent.Register(context))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        ToastUtils.showToast(s);
                    });
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
