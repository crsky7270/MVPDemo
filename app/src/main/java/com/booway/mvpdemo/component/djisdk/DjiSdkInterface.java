package com.booway.mvpdemo.component.djisdk;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import dji.sdk.media.MediaFile;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

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

}
