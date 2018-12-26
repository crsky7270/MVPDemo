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

/**
 * 创建人：wandun
 * 创建时间：2018/12/17
 * 描述：接口
 */

public interface DjiSdkInterface {

    void initMediaManager();

    public Flowable<String> Register(Context context);

    public boolean isAircraftConnected();

    boolean isHandHeldConnected();

    boolean isCameraModuleAvailable();

    boolean isMultiStreamPlatform();

    boolean isM210TwoCameraConnected();

    boolean isMediaManagerAvailable();

    Flowable<Boolean> shootPhoto();

    void showLeftCameraVideoSource();

    void showRightCameraVideoSource();

    Flowable<Bitmap> downloadLastThumMediaFile();

    Maybe<List<MediaFile>> getMediaFileList();

    Flowable<AirCraftLocationBean> getAircraftLocation();

    Observable<Boolean> startCalibration();

    Observable<Boolean> stopCalibration();

    Observable<CompassCalibrationState> getCalibrationState();

    void setCalibrationCallBack(CompassCalibrationState.Callback callBack);

    boolean isCalibrating();

    boolean isTapZoomSupported();

    Observable<Boolean> getTapZoomEnabled();

    Observable<Boolean> setTapZoomEnabled(boolean flag);

    Observable<Boolean> isDigitalZoomSupproted();

    Observable<String> startContinuousOpticalZoom(SettingsDefinitions.ZoomDirection direction);

    Observable<String> stopContinuousOpticalZoom();

    Observable<String> setOpticalZoomMultiplier(int i);

    Observable<Integer> getFocalLengStep();

    Observable<String> getOpticalFocalSpec();

    Observable<String> getFocusTarget();

    Observable<String> setFocusTarget(PointF point);

}
