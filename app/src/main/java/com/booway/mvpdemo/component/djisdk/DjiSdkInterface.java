package com.booway.mvpdemo.component.djisdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.List;

import dji.common.camera.SettingsDefinitions;
import dji.common.flightcontroller.CompassCalibrationState;
import dji.sdk.media.MediaFile;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static dji.common.camera.SettingsDefinitions.*;

/**
 * 创建人：wandun
 * 创建时间：2018/12/17
 * 描述：接口
 */

public interface DjiSdkInterface {

    Observable<DjiSdkResponse> setCameraMode(CameraMode mode);

    Observable<DjiSdkResponse> initMediaManager(boolean isThermalCamera);

    public Flowable<DjiSdkResponse> Register(Context context);

    public boolean isAircraftConnected();

    boolean isHandHeldConnected();

    boolean isCameraModuleAvailable();

    boolean isMultiStreamPlatform();

    boolean isM210TwoCameraConnected();

    boolean isMediaManagerAvailable();

    Observable<DjiSdkResponse> shootPhoto(int cameraIdx);

    Observable<DjiSdkResponse> startShootPhoto();

    void showLeftCameraVideoSource();

    void showRightCameraVideoSource();

    Observable<DjiSdkResponse> getTheThumBitmap(MediaFile file);

    Observable<DjiSdkResponse> getMediaFileList(boolean isThermalCamera);

    Flowable<DjiSdkResponse> getAircraftLocation();

    Observable<Boolean> startCalibration();

    Observable<Boolean> stopCalibration();

    Observable<CompassCalibrationState> getCalibrationState();

    void setCalibrationCallBack(CompassCalibrationState.Callback callBack);

    boolean isCalibrating();

    boolean isTapZoomSupported();

    Observable<Boolean> getTapZoomEnabled();

    Observable<Boolean> setTapZoomEnabled(boolean flag);

    Observable<Boolean> isDigitalZoomSupproted();

    Observable<String> startContinuousOpticalZoom(ZoomDirection direction);

    Observable<String> stopContinuousOpticalZoom();

    Observable<String> setOpticalZoomMultiplier(int i);

    Observable<Integer> getFocalLengStep();

    Observable<String> getOpticalFocalSpec();

    Observable<String> getFocusTarget();

    Observable<String> setFocusTarget(PointF point);

//    Observable<DjiSdkResponse> downloadLastPreviewMediaFile();

    Observable<DjiSdkResponse> getThePreviewBitmap(MediaFile file);

    Observable<DjiSdkResponse> getAllPreview(boolean isThermalCamera);

}
