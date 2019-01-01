package com.booway.mvpdemo.component.djisdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.nfc.Tag;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;


import com.booway.mvpdemo.component.djisdk.DjiSdkResponse.DjiSdkResult;
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
import dji.common.flightcontroller.CompassCalibrationState;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.GPSSignalLevel;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.product.Model;
import dji.common.util.CommonCallbacks;
import dji.common.util.CommonCallbacks.CompletionCallbackWith;
import dji.keysdk.AirLinkKey;
import dji.keysdk.CameraKey;
import dji.keysdk.KeyManager;
import dji.keysdk.ProductKey;
import dji.keysdk.callback.ActionCallback;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.flightcontroller.Compass;
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
    public static final int VISUAL_CAMERA_INDEX = 0;

    //热成像相机索引
    public static final int THERMAL_CAMERA_INDEX = 1;

    private BaseProduct mProduct;

    private List<MediaFile> mediaFileList;

    private FetchMediaTaskScheduler scheduler;

    private MediaManager mMediaManager;

    private DJISDKManager mDJISDKManager = DJISDKManager.getInstance();

    private MediaManager.FileListState currentFileListState = MediaManager.FileListState.UNKNOWN;

    private boolean isRegistered;

//    private int scheduleState = 0;

    private MediaManager.FileListStateListener updateFileListStateListener = new MediaManager.FileListStateListener() {
        @Override
        public void onFileListStateChange(MediaManager.FileListState state) {
            currentFileListState = state;
        }
    };

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
        Model model = mProduct.getModel();
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
            if (mProduct != null && (mProduct instanceof Aircraft)) {
                List<Camera> cameraList = ((Aircraft) mProduct).getCameras();
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

    private synchronized Camera getThermalCameraInstance() {
        if (getProductInstance() == null) return null;
        Camera thermalCamera = null;
        List<Camera> cameras = ((Aircraft) getProductInstance()).getCameras();
        for (Camera camera : cameras) {
            if (camera.isThermalCamera())
                thermalCamera = camera;
        }
        return thermalCamera;
    }

    /**
     * 设置拍摄模式
     *
     * @param mode
     * @return
     */
    public Observable<DjiSdkResponse> setCameraMode(SettingsDefinitions.CameraMode mode) {
        return Observable.create(e -> {
            getCameraInstance().setMode(mode, djiError -> {
                if (djiError == null) {
                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Success));
                } else {
                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, djiError));
                }
                e.onComplete();
            });
        });
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
    public Flowable<DjiSdkResponse> Register(Context context) {
        return Flowable.create((FlowableEmitter<DjiSdkResponse> e) -> mDJISDKManager.registerApp(context,
                new DJISDKManager.SDKManagerCallback() {
                    @Override
                    public void onRegister(DJIError djiError) {
                        if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                            isRegistered = true;
                            mDJISDKManager.startConnectionToProduct();
                            DJILog.e(TAG, DJISDKError.REGISTRATION_SUCCESS.getDescription());
                            DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
                            result.setConnectionInfo("SDK注册成功!");
                            e.onNext(result);
                        } else {
                            e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, djiError));
                        }
                    }

                    @Override
                    public void onProductDisconnect() {
                        DjiSdkResponse response = DjiSdkResponse.create(DjiSdkResult.Connection);
                        response.setConnectionInfo("与设备断开连接");
                        e.onNext(response);
                    }

                    @Override
                    public void onProductConnect(BaseProduct baseProduct) {
                        DjiSdkResponse response = DjiSdkResponse.create(DjiSdkResult.Connection);
                        response.setConnectionInfo(String.format("设备已连接,连接到设备:%s", baseProduct));
                        e.onNext(response);
                    }

                    @Override
                    public void onComponentChange(BaseProduct.ComponentKey componentKey,
                                                  BaseComponent oldComponent,
                                                  BaseComponent newComponent) {
                        DjiSdkResponse response = DjiSdkResponse.create(DjiSdkResult.Connection);
                        if (newComponent != null) {
                            newComponent.setComponentListener(
                                    isConnected -> {
                                        response.setConnectionInfo("设备连接状态改变，当前状态：" +
                                                (isConnected == true ? "已连接" : "已断开"));
                                        e.onNext(response);
                                    });
                        }
                        response.setConnectionInfo(String.format("设备已切换 key:%s, 之前设备:%s, 当前设备:%s",
                                componentKey,
                                oldComponent,
                                newComponent));
                        e.onNext(response);
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
            mProduct = mDJISDKManager.getProduct();
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
     * 拍照事件，拍摄可见光图片-0/红外镜头拍照-1
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
    public Observable<DjiSdkResponse> shootPhoto(int cameraIdx) {
        return Observable.create(e -> KeyManager.getInstance()
                .performAction(CameraKey.create(CameraKey.START_SHOOT_PHOTO, cameraIdx),
                        new ActionCallback() {
                            @Override
                            public void onSuccess() {
                                e.onNext(DjiSdkResponse.create(DjiSdkResult.Success));
                                e.onComplete();
                            }

                            @Override
                            public void onFailure(@NonNull DJIError error) {
                                e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, error));
                            }
                        }));
    }

    /**
     * 另一种拍照写法
     *
     * @return
     */
    public Observable<DjiSdkResponse> startShootPhoto() {
        return Observable.create(e -> {

            getCameraInstance().startShootPhoto(djiError -> {
                if (djiError == null) {
                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Success));
                } else {
                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, djiError));
                }
                e.onComplete();
            });
        });
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
                                        DJILog.d(TAG, "Media Manager is busy.");
                                    } else {
                                        e.onNext(true);
                                        DJILog.d(TAG, "Set MediaDownload Mode success.");
                                    }
                                }
                            });
                    scheduler = mediaManager.getScheduler();
                } else if (null != getCameraInstance() && !getCameraInstance().isMediaDownloadModeSupported()) {
                    e.onNext(false);
                }
                DJILog.d(TAG, "Media Download Mode not Supported");
            } else {
                e.onNext(false);
            }
        });
    }

    /**
     * 初始化MediaManager
     * 主要对象为可见光管理以及热成像镜头
     */
    public Observable<DjiSdkResponse> initMediaManager(boolean isThermalCamera) {
        return Observable.create(e -> {
            Camera camera = isThermalCamera ? getThermalCameraInstance() : getCameraInstance();
            if (null != camera && camera.isMediaDownloadModeSupported()) {
                mMediaManager = camera.getMediaManager();
                if (null != mMediaManager) {
                    //设置文件列表状态监听
                    mMediaManager.addUpdateFileListStateListener(this.updateFileListStateListener);
                    camera.setMode(SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD,
                            (DJIError cbError) -> {
                                if (cbError == null) {
                                    if ((mMediaManager.getSDCardFileListState() == MediaManager.FileListState.SYNCING) ||
                                            (mMediaManager.getSDCardFileListState() == MediaManager.FileListState.DELETING)) {
                                        cbError.setDescription("Media Manager is busy.");
                                        e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, cbError));
                                    } else {
                                        scheduler = mMediaManager.getScheduler();
                                        DJILog.d("get scheduler completed");
                                        e.onNext(DjiSdkResponse.create(DjiSdkResult.Success));
                                        DJILog.d(TAG, "Set MediaDownload Mode success.");
                                    }
                                } else {
                                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, cbError));
                                }
                                e.onComplete();
                            });
                } else if (null != camera && !camera.isMediaDownloadModeSupported()) {
                    DJILog.d(TAG, "get media manager failed");
                }
                DJILog.d(TAG, "Media Download Mode not Supported");
            } else {
                DJILog.d(TAG, "Unknow error");
            }
        });
    }

    /**
     * 获取硬件中媒体文件列表
     *
     * @return
     */
    public Observable<DjiSdkResponse> getMediaFileList(boolean isThermalCamera) {
        return Observable.create(e -> {
            initMediaManager(isThermalCamera).subscribe(response -> {
                if (response.getResult() == DjiSdkResult.Success) {
                    mMediaManager.refreshFileListOfStorageLocation(SettingsDefinitions.StorageLocation.SDCARD,
                            error -> {
                                if (error == null) {
                                    DJILog.d("refresh file success!");
                                    if (currentFileListState != MediaManager.FileListState.INCOMPLETE) {
                                        if (mediaFileList != null)
                                            mediaFileList.clear();
                                    }
                                    mediaFileList = mMediaManager.getSDCardFileListSnapshot();
                                    Collections.sort(mediaFileList, (lhs, rhs) -> {
                                        if (lhs.getTimeCreated() < rhs.getTimeCreated()) {
                                            return -1;
                                        } else if (lhs.getTimeCreated() > rhs.getTimeCreated()) {
                                            return 1;
                                        }
                                        return 0;
                                    });
                                    DJILog.d("collections sort completed");
                                    DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
                                    result.setMediaFiles(mediaFileList);
                                    e.onNext(result);
                                    e.onComplete();
                                } else {
                                    DJILog.d("refresh file failed!");
                                    e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, error));
                                }
                                e.onComplete();
                            });
                } else {
                    DJILog.d(TAG, response.getDJIError().getDescription());
                }
            });
        });
    }

    /**
     * 获取所有预览图
     *
     * @param isThermalCamera
     * @return
     */
    public Observable<DjiSdkResponse> getAllPreview(boolean isThermalCamera) {
        return Observable.create(e -> getMediaFileList(isThermalCamera).subscribe(response -> {
            DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
            if (response.getResult() == DjiSdkResult.Success) {
                scheduler.resume(djiError -> {
                    if (djiError == null) {
                        for (int i = 0; i < mediaFileList.size(); i++) {
                            FetchMediaTask task = new FetchMediaTask(mediaFileList.get(i),
                                    FetchMediaTaskContent.PREVIEW, (file, content, error) -> {
                                if (error == null) {
                                    result.setTempMediaFile(file);
                                    e.onNext(result);
                                } else {
                                    result.setResult(DjiSdkResult.Failed);
                                    result.setDJIError(error);
                                    e.onNext(result);
                                }
                            });
                            scheduler.moveTaskToEnd(task);
                        }
                    } else {
                        result.setResult(DjiSdkResult.Failed);
                        result.setDJIError(djiError);
                    }
                    e.onComplete();
                });
            } else {
                DJILog.d("get media file list error");
            }
        }));
    }

    private void getThumbnails() {
        if (mediaFileList.size() <= 0) {
            DJILog.d(TAG, "No File info for downloading thumbnails");
            return;
        }
        for (int i = 0; i < mediaFileList.size(); i++) {
            getThumbnailByIndex(i);
        }
    }

    private void getPreviews() {
        if (mediaFileList.size() <= 0) {
            DJILog.d(TAG, "No File info for downloading previews");
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
//                scheduleState = 99;
            }
            if (option == FetchMediaTaskContent.THUMBNAIL) {
//                scheduleState = 88;
            }
        } else {
            DJILog.e(TAG, "Fetch Media Task Failed" + error.getDescription());
        }
    };

//    public Observable<DjiSdkResponse> downloadLastPreviewMediaFile() {
//        return Observable.create(e -> {
//            if (mediaFileList != null) {
//                MediaFile file = mediaFileList.get(mediaFileList.size() - 1);
//                if (file.getPreview() == null) {
//                    file.fetchPreview(djiError -> {
//                        if (djiError == null) {
//                            Bitmap bitmap = file.getThumbnail();
//                            e.onNext(file.getFileName() + "," + ",fetch success," + bitmap.getByteCount());
//
//
//                        } else {
//                            e.onNext(djiError.getDescription());
//                        }
//                    });
//                } else {
//                    e.onNext(file.getFileName() + "," + " no fectch return");
//                }
//            } else {
//                e.onNext("media file list is null");
//            }
//            e.onComplete();
//        });
//    }

    /**
     * 获取指定MediaFile的缩略图
     *
     * @param file
     * @return
     */
    public Observable<DjiSdkResponse> getTheThumBitmap(MediaFile file) {
        return Observable.create(e -> {
            if (file.getThumbnail() == null) {
                file.fetchThumbnail(djiError -> {
                    if (djiError == null) {
                        DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
                        result.setTempMediaFile(file);
                        e.onNext(result);
                    } else {
                        e.onNext(DjiSdkResponse.create(DjiSdkResult.Failed, djiError));
                    }
                    e.onComplete();
                });
            } else {
                DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
                result.setTempMediaFile(file);
                e.onNext(result);
                e.onComplete();
            }
        });
    }

    /**
     * 获取指定MediaFile的预览图
     *
     * @param file
     * @return
     */
    public Observable<DjiSdkResponse> getThePreviewBitmap(MediaFile file) {
        return Observable.create(e -> {
            DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
            if (file.getPreview() == null) {
                file.fetchPreview(djiError -> {
                    if (djiError == null) {
                        result.setTempMediaFile(file);
                        e.onNext(result);
                    } else {
                        result.setResult(DjiSdkResult.Failed);
                        result.setDJIError(djiError);
                        e.onNext(result);
                    }
                    e.onComplete();
                });
            } else {
                result.setTempMediaFile(file);
                e.onNext(result);
                e.onComplete();
            }
        });
    }

    /**
     * 获取媒体文件的原始文件
     *
     * @param tmpFileName
     * @param isThermalCamera
     * @return
     */
    public Observable<DjiSdkResponse> downloadTheOriginMediaFileSource(MediaFile mediaFile,
                                                                       String tmpFileName,
                                                                       boolean isThermalCamera) {
        return Observable.create(e -> {
            DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
            if (mediaFile != null) {
                mediaFile.fetchFileData(new File(MEDIA_DOWNLOAD_DIR), tmpFileName, new DownloadListener<String>() {
                    @Override
                    public void onStart() {
                        DJILog.d(TAG, "start");
                    }

                    @Override
                    public void onRateUpdate(long total, long current, long persize) {
                        DJILog.d(TAG, "p1:" + total + ",p2:" + current + "p3:" + persize);
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        DJILog.d(TAG, "p1:" + total + ",p2:" + current);
                    }

                    @Override
                    public void onSuccess(String filePath) {
                        e.onNext(result);
                        e.onComplete();
                        DJILog.d(TAG, "success:" + filePath);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        result.setDJIError(djiError);
                        result.setResult(DjiSdkResult.Failed);
                        e.onNext(result);
                        DJILog.d(TAG, "error:" + djiError.getDescription());
                    }
                });
            } else {
                result.setResult(DjiSdkResult.Failed);
                e.onNext(result);
                e.onComplete();
            }
        });
    }


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
    public Flowable<DjiSdkResponse> getAircraftLocation() {
        return Flowable.create(e -> {
            DjiSdkResponse result = DjiSdkResponse.create(DjiSdkResult.Success);
            if (getAircraftInstance().getFlightController() == null) {
                result.setResult(DjiSdkResult.Failed);
                e.onNext(result);
                e.onComplete();
            } else {
                getAircraftInstance().getFlightController()
                        .setStateCallback(flightControllerState -> {
                            LocationCoordinate3D coordinate3D = flightControllerState.getAircraftLocation();
                            GPSSignalLevel signalLevel = flightControllerState.getGPSSignalLevel();
                            AirCraftLocationBean location = new AirCraftLocationBean(coordinate3D, signalLevel);
                            result.setLocationBean(location);
                            e.onNext(result);
                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 开始指南针校对
     *
     * @return
     */
    public Observable<Boolean> startCalibration() {
        return Observable.create(e -> {
            getAircraftInstance().getFlightController().getCompass().startCalibration(djiError -> {
                if (djiError == null) {
                    e.onNext(true);
                } else {
                    e.onNext(false);
                }
                e.onComplete();
            });
        });
    }

    /**
     * 停止指南针校对
     *
     * @return
     */
    public Observable<Boolean> stopCalibration() {
        return Observable.create(e -> {
            getAircraftInstance().getFlightController().getCompass().stopCalibration(djiError -> {
                if (djiError == null) {
                    e.onNext(true);
                } else {
                    e.onNext(false);
                }
                e.onComplete();
            });
        });
    }

    /**
     * 获取指南针校准状态
     *
     * @return
     */
    public Observable<CompassCalibrationState> getCalibrationState() {
        return Observable.create(e -> {
            e.onNext(getAircraftInstance().getFlightController().getCompass().getCalibrationState());
            e.onComplete();
        });
    }

    /**
     * 设置指南针状态回调
     *
     * @param callBack
     */
    public void setCalibrationCallBack(CompassCalibrationState.Callback callBack) {
        getAircraftInstance().getFlightController().getCompass().setCalibrationStateCallback(callBack);
    }

    /**
     * 指南针是否正在校准中
     *
     * @return
     */
    public boolean isCalibrating() {
        return getAircraftInstance().getFlightController().getCompass().isCalibrating();
    }

    /**
     * 检测摄像头是否支持点击对焦放大
     *
     * @return
     */
    public boolean isTapZoomSupported() {
        return getCameraInstance().isTapZoomSupported();
    }

    /**
     * 获取点击对焦状态是否开启
     *
     * @return
     */
    public Observable<Boolean> getTapZoomEnabled() {
        return Observable.create(e -> {
            getCameraInstance().getTapZoomEnabled(new CompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    e.onNext(true);
                    e.onComplete();
                }

                @Override
                public void onFailure(DJIError djiError) {
                    e.onNext(false);
                    DJILog.d(TAG, djiError.getDescription());
                    e.onComplete();
                }
            });
        });
    }

    /**
     * 设置摄像头点击放大功能是否可用
     *
     * @param flag
     * @return 设置是否成功
     */
    public Observable<Boolean> setTapZoomEnabled(boolean flag) {
        return Observable.create(e -> {
            getCameraInstance().setTapZoomEnabled(flag, djiError -> {
                if (djiError == null) {
                    e.onNext(true);
                } else {
                    e.onNext(false);
                }
                e.onComplete();
            });
        });
    }

    /**
     * 设置镜头变焦模式
     *
     * @param mode
     * @return
     */
    public Observable<Boolean> setFocusMode(SettingsDefinitions.FocusMode mode) {
        return Observable.create(e -> getCameraInstance().setFocusMode(mode, djiError -> {
            if (djiError == null) {
                e.onNext(true);
            } else {
                e.onNext(false);
            }
            e.onComplete();
        }));
    }

    /**
     * 获取镜头对焦模式
     *
     * @return
     */
    public Observable<SettingsDefinitions.FocusMode> getFocusMode() {
        return Observable.create(e -> getCameraInstance().getFocusMode(
                new CompletionCallbackWith<SettingsDefinitions.FocusMode>() {
                    @Override
                    public void onSuccess(SettingsDefinitions.FocusMode focusMode) {
                        e.onNext(focusMode);
                        e.onComplete();
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        if (djiError == null) {
                            e.onNext(SettingsDefinitions.FocusMode.UNKNOWN);
                            e.onComplete();
                        }
                    }
                }));
    }

    /**
     * 是否支持数码变焦
     *
     * @return
     */
    public Observable<Boolean> isDigitalZoomSupproted() {
        return Observable.create(e -> {
            e.onNext(getCameraInstance().isDigitalZoomSupported());
            e.onComplete();
        });
    }

    /**
     * 放大或缩小镜头
     *
     * @return
     */
    public Observable<String> startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection direction) {
        return Observable.create(e -> {
            getCameraInstance().startContinuousOpticalZoom(direction
                    , SettingsDefinitions.ZoomSpeed.MODERATELY_SLOW, djiError -> {
                        if (djiError == null) {
                            e.onNext("zoom in");
                        } else {
                            e.onNext("zoom out");
                        }
                        e.onComplete();
                    });
        });
    }

    /**
     * 停止缩放
     *
     * @return
     */
    public Observable<String> stopContinuousOpticalZoom() {
        return Observable.create(e -> {
            getCameraInstance().stopContinuousOpticalZoom(djiError -> {
                if (djiError == null) {
                    e.onNext("zoom stop");
                } else {
                    e.onNext(djiError.getDescription());
                }

            });
        });
    }

    /**
     * @return
     */
    public Observable<String> setOpticalZoomMultiplier(int i) {
        return Observable.create(e -> {
            getCameraInstance().setTapZoomMultiplier(i, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        e.onNext("zoom multiplier stop");
                    } else {
                        e.onNext(djiError.getDescription());
                    }

                }
            });
        });
    }

    /**
     * @return
     */
    public Observable<Integer> getFocalLengStep() {
        return Observable.create(e -> {
            getCameraInstance().getOpticalZoomFocalLength(new CompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    e.onNext(integer);
                    e.onComplete();
                }

                @Override
                public void onFailure(DJIError djiError) {
                    e.onNext(-1);
                    e.onComplete();
                }
            });
        });
    }

    /**
     * @return
     */
    public Observable<String> getOpticalFocalSpec() {
        return Observable.create(e -> {
            getCameraInstance().getOpticalZoomSpec(new CompletionCallbackWith<SettingsDefinitions.OpticalZoomSpec>() {
                @Override
                public void onSuccess(SettingsDefinitions.OpticalZoomSpec opticalZoomSpec) {
                    e.onNext("min:" + opticalZoomSpec.getMinFocalLength() +
                            "," + opticalZoomSpec.getMaxFocalLength() +
                            "," + opticalZoomSpec.getFocalLengthStep());
                }

                @Override
                public void onFailure(DJIError djiError) {
                    e.onNext(djiError.getDescription());
                }
            });
        });

    }

    /**
     * @param step
     * @return
     */
    public Observable<String> setOpticalZoomFocalLength(int step) {
        return Observable.create(e -> {
                    getCameraInstance().setOpticalZoomFocalLength(step, djiError -> {
                        if (djiError == null) {
                            e.onNext("zoom multiplier stop");
                        } else {
                            e.onNext(djiError.getDescription());
                        }
                        e.onComplete();
                    });
                }
        );
    }

    /**
     * @return
     */
    public Observable<String> getFocusTarget() {
        return Observable.create(e -> {
            getCameraInstance().getFocusTarget(new CompletionCallbackWith<PointF>() {
                @Override
                public void onSuccess(PointF pointF) {
                    e.onNext("pointf,x:" + pointF.x + ",y:" + pointF.y);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    e.onNext(djiError.getDescription());
                }

            });
        });
    }

    public Observable<String> setFocusTarget(PointF point) {
        return Observable.create(e -> {
            getCameraInstance().tapZoomAtTarget(point, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null)
                        e.onNext("success!!! pointf,x:" + point.x + ",y:" + point.y);
                }
            });
        });
    }

    /**
     * 退出飞控界面时销毁
     */
    public void destory() {
        if (mMediaManager != null) {
            mMediaManager.stop(null);
            mMediaManager.removeFileListStateCallback(updateFileListStateListener);
            mMediaManager.exitMediaDownloading();
            if (scheduler != null) {
                scheduler.removeAllTasks();
            }
        }
    }

}
