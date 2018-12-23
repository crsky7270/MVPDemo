package com.booway.mvpdemo.component.djisdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;


import com.booway.mvpdemo.utils.LogUtils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.nio.file.NotLinkException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.product.Model;
import dji.common.util.CommonCallbacks;
import dji.keysdk.AirLinkKey;
import dji.keysdk.CameraKey;
import dji.keysdk.KeyManager;
import dji.keysdk.ProductKey;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.media.DownloadListener;
import dji.sdk.media.FetchMediaTask;
import dji.sdk.media.FetchMediaTaskContent;
import dji.sdk.media.FetchMediaTaskScheduler;
import dji.sdk.media.MediaFile;
import dji.sdk.media.MediaManager;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;
import dji.sdk.sdkmanager.DJISDKManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建人：wandun
 * 创建时间：2018/12/17
 * 描述：大疆SDK组件
 */
@Singleton
public class DjiSdkComponent implements DjiSdkInterface {

    public final String MEDIA_DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getPath() + "/ZnxjTmp/Images/";

    private final String TAG = "DJI_SDK_COMPONENT";

    //可见光相机索引
    static final int VISUAL_CAMERA_INDEX = 0;

    //热成像相机索引
    static final int THERMAL_CAMERA_INDEX = 1;

    private BaseProduct mProduct;

    private List<MediaFile> mediaFileList;

    private FetchMediaTaskScheduler scheduler;

    private MediaManager mMediaManager;

    @Inject
    public DjiSdkComponent() {

    }

    public boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof Aircraft;
    }

    public boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof HandHeld;
    }

    public boolean isProductModuleAvailable() {
        return (null != getProductInstance());
    }

    public boolean isCameraModuleAvailable() {
        return isProductModuleAvailable() &&
                (null != getProductInstance().getCamera());
    }

    /**
     * @return 是否支持多图像流的产品
     */
    public boolean isMultiStreamPlatform() {
        Model model = DJISDKManager.getInstance().getProduct().getModel();
        return model != null && (model == Model.INSPIRE_2
                || model == Model.MATRICE_200
                || model == Model.MATRICE_210
                || model == Model.MATRICE_210_RTK
                || model == Model.MATRICE_600
                || model == Model.MATRICE_600_PRO
                || model == Model.A3
                || model == Model.N3);
    }

    /**
     * @return 是否M210的两个相机都已连接
     */
    public boolean isM210TwoCameraConnected() {

        Object model = null;
        if (KeyManager.getInstance() != null) {
            model = KeyManager.getInstance().getValue(ProductKey.create(ProductKey.MODEL_NAME));
        }
        if (model != null) {
            BaseProduct product = DJISDKManager.getInstance().getProduct();
            if (product != null && (product instanceof Aircraft)) {
                List<Camera> cameraList = ((Aircraft) product).getCameras();
                if ((model == Model.MATRICE_210 || model == Model.MATRICE_210_RTK)) {
                    return (cameraList != null
                            && cameraList.size() == 2
                            && cameraList.get(0).isConnected()
                            && cameraList.get(1).isConnected());
                }
            }
        }
        return false;
    }

    /**
     * 是否支持媒体文件下载至本地
     *
     * @return
     */
    public boolean isMediaManagerAvailable() {
        return isCameraModuleAvailable() && (null != getCameraInstance().getMediaManager());
    }

    /**
     * 大疆SDK注册
     * 代码示例：
     * Flowable.just(getApplicationContext())
     * .flatMap(context -> {
     * return mDjiSdkComponent.Register(context);
     * }).subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread())
     * .subscribe(s -> ToastUtils.showToast(s));
     *
     * @param context
     * @return
     */
    @Override
    public Flowable<String> Register(Context context) {
        return Flowable.create(e -> DJISDKManager.getInstance().registerApp(context,
                new DJISDKManager.SDKManagerCallback() {
                    @Override
                    public void onRegister(DJIError djiError) {
                        if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                            DJILog.e("App register", DJISDKError.REGISTRATION_SUCCESS.getDescription());
                            DJISDKManager.getInstance().startConnectionToProduct();
                            e.onNext("SDK注册成功");
                        } else {
                            e.onNext("SDK注册失败，请检查网络");
                        }
                    }

                    @Override
                    public void onProductDisconnect() {
                        e.onNext("与设备断开连接");
                    }

                    @Override
                    public void onProductConnect(BaseProduct baseProduct) {
                        e.onNext(String.format("设备已连接,连接到设备:%s", baseProduct));
                    }

                    @Override
                    public void onComponentChange(BaseProduct.ComponentKey componentKey,
                                                  BaseComponent oldComponent,
                                                  BaseComponent newComponent) {
                        if (newComponent != null) {
                            newComponent.setComponentListener(
                                    isConnected -> e.onNext("设备连接状态改变，当前状态：" +
                                            (isConnected == true ? "已连接" : "已断开")));
                        }
                        e.onNext(String.format("设备已切换 key:%s, 之前设备:%s, 当前设备:%s",
                                componentKey,
                                oldComponent,
                                newComponent));
                    }
                }), BackpressureStrategy.BUFFER);
    }

    /**
     * 返回BaseProduct实例
     *
     * @return 如果设备未连接，返回null
     */
    private synchronized BaseProduct getProductInstance() {
        if (null == mProduct) {
            mProduct = DJISDKManager.getInstance().getProduct();
        }
        return mProduct;
    }

    private synchronized Aircraft getAircraftInstance() {
        if (!isAircraftConnected()) return null;
        return (Aircraft) getProductInstance();
    }

    private synchronized Camera getCameraInstance() {

        if (getProductInstance() == null) return null;

        Camera camera = null;

        if (getProductInstance() instanceof Aircraft) {
            camera = ((Aircraft) getProductInstance()).getCamera();

        } else if (getProductInstance() instanceof HandHeld) {
            camera = ((HandHeld) getProductInstance()).getCamera();
        }

        return camera;
    }


    /**
     * 拍照事件，拍摄可见光图片
     * 代码示例：
     * mDjiSdkComponent.shootPhoto()
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread())
     * .subscribe(result -> {
     * if (result) {
     * ToastUtils.showToast("拍照完毕");
     * } else {
     * ToastUtils.showToast("拍照失败");
     * }
     * });
     */
    public Flowable<Boolean> shootPhoto() {
        return Flowable.create(e -> KeyManager.getInstance()
                .performAction(CameraKey.create(CameraKey.START_SHOOT_PHOTO, VISUAL_CAMERA_INDEX),
                        new ActionCallback() {
                            @Override
                            public void onSuccess() {
                                e.onNext(true);
                            }

                            @Override
                            public void onFailure(@NonNull DJIError error) {
                                e.onNext(false);
                            }
                        }), BackpressureStrategy.BUFFER);
    }

    /**
     * 仅显示左侧云台相机的图像，左侧云台为primaryVideoSource
     */
    public void showLeftCameraVideoSource() {
        AirLinkKey mainCameraBandwidthKey = AirLinkKey.createLightbridgeLinkKey(AirLinkKey.BANDWIDTH_ALLOCATION_FOR_LEFT_CAMERA);
        AirLinkKey lbBandwidthKey = AirLinkKey.createLightbridgeLinkKey(AirLinkKey.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT);
        KeyManager.getInstance().setValue(lbBandwidthKey, 0.8f, null);
        KeyManager.getInstance().setValue(mainCameraBandwidthKey, 1.0f, null);
    }

    /**
     * 仅显示右侧云台相机的图像，右侧云台为primaryVideoSource
     */
    public void showRightCameraVideoSource() {
        AirLinkKey mainCameraBandwidthKey = AirLinkKey.createLightbridgeLinkKey(AirLinkKey.BANDWIDTH_ALLOCATION_FOR_LEFT_CAMERA);
        AirLinkKey lbBandwidthKey = AirLinkKey.createLightbridgeLinkKey(AirLinkKey.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT);
        KeyManager.getInstance().setValue(lbBandwidthKey, 0.8f, null);
        KeyManager.getInstance().setValue(mainCameraBandwidthKey, 0.0f, null);
    }

    private Observable<Boolean> setMediaDownloadMode() {
        return Observable.create(e -> {
            if (null != getCameraInstance() && getCameraInstance().isMediaDownloadModeSupported()) {
                MediaManager mediaManager = getCameraInstance().getMediaManager();
                if (null != mediaManager) {

                    getCameraInstance().setMode(SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD,
                            cbError -> {
                                if (cbError == null) {
                                    if ((mediaManager.getSDCardFileListState() == MediaManager.FileListState.SYNCING) ||
                                            (mediaManager.getSDCardFileListState() == MediaManager.FileListState.DELETING)) {
                                        e.onNext(false);
                                        LogUtils.d(TAG, "Media Manager is busy.");
                                    } else {
                                        e.onNext(true);
                                        LogUtils.d(TAG, "Set MediaDownload Mode success.");
                                    }
                                }
                            });
                    scheduler = mediaManager.getScheduler();
                } else if (null != getCameraInstance() && !getCameraInstance().isMediaDownloadModeSupported()) {
                    e.onNext(false);
                }
                LogUtils.d(TAG, "Media Download Mode not Supported");
            } else {
                e.onNext(false);
            }
        });
    }

    /**
     * 初始化
     */
    public void initMediaManager() {
        if (null != getCameraInstance() && getCameraInstance().isMediaDownloadModeSupported()) {
            mMediaManager = getCameraInstance().getMediaManager();
            if (null != mMediaManager) {
                getCameraInstance().setMode(SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD,
                        (DJIError cbError) -> {
                            if (cbError == null) {
                                if ((mMediaManager.getSDCardFileListState() == MediaManager.FileListState.SYNCING) ||
                                        (mMediaManager.getSDCardFileListState() == MediaManager.FileListState.DELETING)) {
                                    LogUtils.d(TAG, "Media Manager is busy.");
                                } else {
                                    LogUtils.d(TAG, "Set MediaDownload Mode success.");
                                }
                            }
                        });
                scheduler = mMediaManager.getScheduler();
            } else if (null != getCameraInstance() && !getCameraInstance().isMediaDownloadModeSupported()) {
                LogUtils.d(TAG, "get media manager failed");
            }
            LogUtils.d(TAG, "Media Download Mode not Supported");
        } else {
            LogUtils.d(TAG, "Unknow error");
        }
    }

    /**
     * @return
     */
    public Maybe<List<MediaFile>> getMediaFileList() {
        initMediaManager();
        return Maybe.create(e -> {
            mMediaManager.refreshFileListOfStorageLocation(SettingsDefinitions.StorageLocation.SDCARD,
                    error -> {
                        if (error == null) {
                            mediaFileList = mMediaManager.getSDCardFileListSnapshot();
                            e.onSuccess(mediaFileList);
                        }
                    });
        });
    }

    private void getThumbnails() {
        if (mediaFileList.size() <= 0) {
            LogUtils.d(TAG, "No File info for downloading thumbnails");
            return;
        }
        for (int i = 0; i < mediaFileList.size(); i++) {
            getThumbnailByIndex(i);
        }
    }

    private void getPreviews() {
        if (mediaFileList.size() <= 0) {
            LogUtils.d(TAG, "No File info for downloading previews");
            return;
        }
        for (int i = 0; i < mediaFileList.size(); i++) {
            getPreviewByIndex(i);
        }
    }

    private void getThumbnailByIndex(final int index) {
        FetchMediaTask task = new FetchMediaTask(mediaFileList.get(index), FetchMediaTaskContent.THUMBNAIL, taskCallback);
        scheduler.moveTaskToEnd(task);
    }

    private void getPreviewByIndex(final int index) {
        FetchMediaTask task = new FetchMediaTask(mediaFileList.get(index), FetchMediaTaskContent.PREVIEW, taskCallback);
        scheduler.moveTaskToEnd(task);
    }

    public FetchMediaTask.Callback taskCallback = (file, option, error) -> {
        if (null == error) {
            if (option == FetchMediaTaskContent.PREVIEW) {

            }
            if (option == FetchMediaTaskContent.THUMBNAIL) {

            }
        } else {
            LogUtils.e(TAG, "Fetch Media Task Failed" + error.getDescription());
        }
    };


    public Flowable<Bitmap> downloadLastThumMediaFile() {
        return Flowable.create(e -> {
            MediaFile file = mediaFileList.get(mediaFileList.size() - 1);
            file.fetchThumbnail(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        Bitmap bitmap=file.getThumbnail();
                        e.onNext(bitmap);
                    }else{
//                        e.onNext("error");
                    }
                    e.onComplete();
                }
            });
        }, BackpressureStrategy.BUFFER);
    }


    public Flowable<String> downloadLastMediaFile(String tmpFileName) {
        return Flowable.create(e -> {
            getMediaFileList().subscribe(files -> {
                MediaFile file = files.get(files.size() - 1);
                file.fetchFileData(new File(MEDIA_DOWNLOAD_DIR), tmpFileName, new DownloadListener<String>() {
                    @Override
                    public void onStart() {
                        e.onNext("start");
                        LogUtils.d(TAG, "start");
                    }

                    @Override
                    public void onRateUpdate(long total, long current, long persize) {
                        e.onNext("p1:" + total + ",p2:" + current + "p3:" + persize);
                        LogUtils.d(TAG, "p1:" + total + ",p2:" + current + "p3:" + persize);
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        e.onNext("p1:" + total + ",p2:" + current);
                        LogUtils.d(TAG, "p1:" + total + ",p2:" + current);
                    }

                    @Override
                    public void onSuccess(String filePath) {
                        getProductInstance().getCamera()
                                .setMode(SettingsDefinitions.CameraMode.SHOOT_PHOTO, null);
                        e.onNext("success:" + filePath);
                        LogUtils.d(TAG, "success:" + filePath);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        e.onNext("error:" + djiError.getDescription());
                        LogUtils.d(TAG, "error:" + djiError.getDescription());
                    }
                });
            });
        }, BackpressureStrategy.BUFFER);
//        getMediaFileList().doOnSuccess(fileList->{
//            mediaFileList=fileList;
//        }).flatMap(new Function<List<MediaFile>, MaybeSource<String>>() {
//            @Override
//            public MaybeSource<String> apply(List<MediaFile> files) throws Exception {
//                return Flowable.create((FlowableEmitter<Object> e) ->{
//                    MediaFile file = files.get(mediaFileList.size() - 1);
//                    file.fetchFileData(new File(MEDIA_DOWNLOAD_DIR), tmpFileName, new DownloadListener<String>() {
//                        @Override
//                        public void onStart() {
//                            e.onNext("start");
//                            LogUtils.d(TAG, "start");
//                        }
//
//                        @Override
//                        public void onRateUpdate(long total, long current, long persize) {
//                            e.onNext("p1:" + total + ",p2:" + current + "p3:" + persize);
//                            LogUtils.d(TAG, "p1:" + total + ",p2:" + current + "p3:" + persize);
//                        }
//
//                        @Override
//                        public void onProgress(long total, long current) {
//                            e.onNext("p1:" + total + ",p2:" + current);
//                            LogUtils.d(TAG, "p1:" + total + ",p2:" + current);
//                        }
//
//                        @Override
//                        public void onSuccess(String filePath) {
//                            getProductInstance().getCamera()
//                                    .setMode(SettingsDefinitions.CameraMode.SHOOT_PHOTO, null);
//                            e.onNext("success:" + filePath);
//                            LogUtils.d(TAG, "success:" + filePath);
//                        }
//
//                        @Override
//                        public void onFailure(DJIError djiError) {
//                            e.onNext("error:" + djiError.getDescription());
//                            LogUtils.d(TAG, "error:" + djiError.getDescription());
//                        }
//                    });
//                },BackpressureStrategy.BUFFER);
//            }
//        });
//        return null;
    }


    /**
     * 下载最近一张相机SD卡媒体文件至本地
     *
     * @param tmpFileName 临时文件命名
     * @return
     */
//    public Flowable<String> downloadMediaFile(String tmpFileName) {
//        return Flowable.create(e -> {
//            if (null != getCameraInstance() && getCameraInstance().isMediaDownloadModeSupported()) {
//                MediaManager mediaManager = getCameraInstance().getMediaManager();
//                if (null != mediaManager) {
//                    getCameraInstance().setMode(SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD, cbError -> {
//                        if (cbError == null) {
//                            e.onNext("Set cameraMode success");
//                            LogUtils.d(TAG, "Set cameraMode success");
//                            if ((mediaManager.getSDCardFileListState() == MediaManager.FileListState.SYNCING) ||
//                                    (mediaManager.getSDCardFileListState() == MediaManager.FileListState.DELETING)) {
//                                e.onNext("Media Manager is busy.");
//                                LogUtils.d(TAG, "Media Manager is busy.");
//                            } else {
//                                mediaManager.refreshFileListOfStorageLocation(SettingsDefinitions.StorageLocation.SDCARD, new CommonCallbacks.CompletionCallback() {
//                                    @Override
//                                    public void onResult(DJIError djiError) {
//                                        if (null == djiError) {
//                                            List<MediaFile> djiMedias = mediaManager.getSDCardFileListSnapshot();
//
//                                            if (null != djiMedias) {
//                                                if (!djiMedias.isEmpty()) {
//                                                    media = djiMedias.get(djiMedias.size() - 1);
//                                                    if (isCameraModuleAvailable()
//                                                            && media != null
//                                                            && mediaManager != null) {
//                                                        media.fetchFileData(new File(MEDIA_DOWNLOAD_DIR), tmpFileName, new DownloadListener<String>() {
//                                                            @Override
//                                                            public void onStart() {
//                                                                e.onNext("start");
//                                                                LogUtils.d(TAG, "start");
//                                                            }
//
//                                                            @Override
//                                                            public void onRateUpdate(long l, long l1, long l2) {
//                                                                e.onNext("p1:" + l + ",p2:" + l1 + "p3:" + l2);
//                                                                LogUtils.d(TAG, "p1:" + l + ",p2:" + l1 + "p3:" + l2);
//                                                            }
//
//                                                            @Override
//                                                            public void onProgress(long l, long l1) {
//                                                                e.onNext("p1:" + l + ",p2:" + l1);
//                                                                LogUtils.d(TAG, "p1:" + l + ",p2:" + l1);
//                                                            }
//
//                                                            @Override
//                                                            public void onSuccess(String s) {
//
//                                                                getProductInstance()
//                                                                        .getCamera()
//                                                                        .setMode(SettingsDefinitions.CameraMode.SHOOT_PHOTO, null);
//                                                                e.onNext("success:" + s);
//                                                                LogUtils.d(TAG, "success:" + s);
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(DJIError djiError) {
//                                                                e.onNext("error:" + djiError.getDescription());
//                                                                LogUtils.d(TAG, "error:" + djiError.getDescription());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                        } else {
//                            e.onNext("Set cameraMode failed");
//                        }
//                    });
//                }
//
//            } else if (null != getCameraInstance()
//                    && !getCameraInstance().isMediaDownloadModeSupported()) {
//                e.onNext("Media Download Mode not Supported");
//            }
//        }, BackpressureStrategy.BUFFER);
//    }

    /**
     * 设置获取文件的具体方式及顺序
     *
     * @param mediaManager
     */

//    private void setMedia(MediaManager mediaManager) {
//        if (isMediaManagerAvailable() && mediaManager != null) {
//            mediaManager.refreshFileListOfStorageLocation(SettingsDefinitions.StorageLocation.SDCARD,
//                    djiError -> {
//                        if (null == djiError) {
//                            List<MediaFile> djiMedias = mediaManager.getSDCardFileListSnapshot();
//
//                            if (null != djiMedias) {
//                                if (!djiMedias.isEmpty()) {
//                                    media = djiMedias.get(djiMedias.size() - 1);
//                                }
//                            }
//                        }
//                    });
//        }
//    }

    /**
     * 获取飞机当前的经纬度信息
     * 示例代码：
     * mDjiSdkComponent.getAircraftLocation()
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread())
     * .subscribe(result -> {
     * ToastUtils.showToast("海拔:" + result.getLocationCoordinate3D().getAltitude() +
     * "，经度：" + result.getLocationCoordinate3D().getLongitude() +
     * "，纬度：" + result.getLocationCoordinate3D().getLatitude());
     * <p>
     * });
     */
    public Flowable<AirCraftLocationBean> getAircraftLocation() {
        return Flowable.create(e -> getAircraftInstance().getFlightController()
                .setStateCallback(flightControllerState -> {
                    LocationCoordinate3D location = flightControllerState.getAircraftLocation();
                    GPSSignalLevel signalLevel = flightControllerState.getGPSSignalLevel();
                    AirCraftLocationBean bean = new AirCraftLocationBean(location, signalLevel);
                    e.onNext(bean);
                }), BackpressureStrategy.BUFFER);
    }
}
