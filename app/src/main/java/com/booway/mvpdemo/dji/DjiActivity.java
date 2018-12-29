package com.booway.mvpdemo.dji;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.booway.mvpdemo.R;
import com.booway.mvpdemo.component.djisdk.DjiSdkComponent;
import com.booway.mvpdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;
import dji.common.camera.SettingsDefinitions;
import dji.common.product.Model;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.ux.widget.FPVOverlayWidget;
import dji.ux.widget.FPVWidget;
import io.reactivex.Flowable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class DjiActivity extends DaggerAppCompatActivity  {

    @Inject
    DjiSdkComponent mDjiSdkComponent;

    private Unbinder mUnbinder;

//    private VideoFeeder.PhysicalSourceListener sourceListener;

    @BindView(R.id.fpv_widget)
    FPVWidget mFPVWidget;

    @BindView(R.id.fpv_overlay_widget)
    FPVOverlayWidget mFPVOverlayWidget;

    @BindView(R.id.data_list)
    Button testButton1;

    @OnClick(R.id.data_list)
    public void zoomIn() {
//        mDjiSdkComponent.startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection.ZOOM_IN)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(msg -> {
//                    ToastUtils.showToast(msg);
//                });
        //是否支持数码变焦
//        mDjiSdkComponent.isDigitalZoomSupproted()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(e -> {
//                    if (e)
//                        ToastUtils.showToast("ok");
//                    else
//                        ToastUtils.showToast("err");
//                });
        mDjiSdkComponent.setOpticalZoomMultiplier(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    ToastUtils.showToast(msg);
                });
//        mDjiSdkComponent.getFocalLengStep()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(integer -> ToastUtils.showToast(integer))
//                .observeOn(Schedulers.io())
//                .flatMap(integer -> mDjiSdkComponent.setOpticalZoomFocalLength(integer * 2))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(e -> {
//                    ToastUtils.showToast(e);
//                });

        //43,1290,1


    }

    @OnClick(R.id.collection)
    public void zoomOut() {
//        mDjiSdkComponent.startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection.ZOOM_OUT)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(msg -> {
//                    ToastUtils.showToast(msg);
//                });
//        mDjiSdkComponent.getFocusMode()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(e->{
//                    ToastUtils.showToast(e.name().toString());
//                });
        mDjiSdkComponent.setFocusMode(SettingsDefinitions.FocusMode.MANUAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    ToastUtils.showToast(e.toString());
                });

    }

    @OnClick(R.id.stop)
    public void zoomStop() {
//        mDjiSdkComponent.setTapZoomEnabled(true)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(msg -> {
//                    ToastUtils.showToast(msg.toString());
//                });
        mDjiSdkComponent.shootPhoto().subscribe(e->{
           ToastUtils.showToast(e);
        });

//        mDjiSdkComponent.getFocusTarget()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(e -> {
//                    ToastUtils.showToast(e);
//                });

//        mDjiSdkComponent.getOpticalFocalSpec()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(result -> {
//                    ToastUtils.showToast(result);
//                });

//        mDjiSdkComponent.stopContinuousOpticalZoom()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(msg -> {
//                    ToastUtils.showToast(msg);
//                });
    }

//    private void setUpListeners() {
//        sourceListener = new VideoFeeder.PhysicalSourceListener() {
//            @Override
//            public void onChange(VideoFeeder.VideoFeed videoFeed, VideoFeeder.PhysicalSource newPhysicalSource) {
//                if (videoFeed == VideoFeeder.getInstance().getPrimaryVideoFeed()) {
//                    String newText = "Primary Source: " + newPhysicalSource.toString();
////                    ToastUtils.setResultToText(primaryVideoFeedTitle,newText);
//                }
//                if (videoFeed == VideoFeeder.getInstance().getSecondaryVideoFeed()) {
////                    ToastUtils.setResultToText(fpvVideoFeedTitle,"Secondary Source: " + newPhysicalSource.toString());
//                }
//            }
//        };
//
//        setVideoFeederListeners(true);
//    }

//    private boolean isMultiStreamPlatform() {
//        Model model = DJISDKManager.getInstance().getProduct().getModel();
//        return model != null && (model == Model.INSPIRE_2
//                || model == Model.MATRICE_200
//                || model == Model.MATRICE_210
//                || model == Model.MATRICE_210_RTK
//                || model == Model.MATRICE_600
//                || model == Model.MATRICE_600_PRO
//                || model == Model.A3
//                || model == Model.N3);
//    }

//    private void setVideoFeederListeners(boolean isOpen) {
//        if (VideoFeeder.getInstance() == null) return;
//
//        final BaseProduct product = DJISDKManager.getInstance().getProduct();
////        updateM210Buttons();
//        if (product != null) {
//
//            if (isOpen) {
//                mFPVWidget.registerLiveVideo(VideoFeeder.getInstance().getPrimaryVideoFeed(), true);
//                String newText = "Primary Source: " + VideoFeeder.getInstance().getPrimaryVideoFeed().getVideoSource().name();
//                ToastUtils.showToast(newText);
//                if (isMultiStreamPlatform()) {
////                    fpvVideoFeed.registerLiveVideo(VideoFeeder.getInstance().getSecondaryVideoFeed(), false);
////                    String newTextFpv = "Secondary Source: " + VideoFeeder.getInstance().getSecondaryVideoFeed().getVideoSource().name();
////                    ToastUtils.setResultToText(fpvVideoFeedTitle,newTextFpv);
//                }
//                VideoFeeder.getInstance().addPhysicalSourceListener(sourceListener);
////                ToastUtils.showToast("success");
//            } else {
//                VideoFeeder.getInstance().removePhysicalSourceListener(sourceListener);
//                VideoFeeder.getInstance().getPrimaryVideoFeed().setCallback(null);
//                if (isMultiStreamPlatform()) {
//                    VideoFeeder.getInstance().getSecondaryVideoFeed().setCallback(null);
//                }
////                ToastUtils.showToast("failed");
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dji);
        mUnbinder = ButterKnife.bind(this);


        mFPVOverlayWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PointF pointF = new PointF(event.getX(), event.getY());
//                mDjiSdkComponent.setFocusTarget(pointF);
//                ToastUtils.showToast("event:x:" + event.getX() + ",y:" + event.getY());
                ToastUtils.showToast(event.getX() + ",y:" + event.getY() + ":::" + event.getXPrecision() + "," + event.getYPrecision());
                return false;
            }
        });

//        setUpListeners();
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


}
