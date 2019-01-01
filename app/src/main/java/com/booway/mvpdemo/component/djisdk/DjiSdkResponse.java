package com.booway.mvpdemo.component.djisdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import dji.common.error.DJIError;
import dji.sdk.media.MediaFile;

/**
 * 创建人：wandun
 * 创建时间：2018/12/29
 * 描述：大疆SDK接口调用返回基础类
 */

public class DjiSdkResponse {

    public enum DjiSdkResult {
        Failed,
        Success,
        Connection
    }

    public DjiSdkResponse() {
    }

    public DjiSdkResponse(@NonNull DjiSdkResult result) {
        this(result, null);
    }

    public DjiSdkResponse(@NonNull DjiSdkResult result, @Nullable DJIError error) {
        this.result = result;
        this.mDJIError = error;
    }

    private String connectionInfo;

    public String getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String info) {
        connectionInfo = info;
    }

    private DjiSdkResult result;

    public DjiSdkResult getResult() {
        return result;
    }

    public void setResult(DjiSdkResult result) {
        this.result = result;
    }

    private DJIError mDJIError;

    public DJIError getDJIError() {
        return mDJIError;
    }

    public void setDJIError(DJIError djiError) {
        this.mDJIError = djiError;
    }

    public static DjiSdkResponse create(@NonNull DjiSdkResult result) {
        return new DjiSdkResponse(result);
    }

    public static DjiSdkResponse create(@NonNull DjiSdkResult result, @Nullable DJIError error) {
        return new DjiSdkResponse(result, error);
    }

    private List<MediaFile> mMediaFiles;

    public List<MediaFile> getMediaFiles() {
        return mMediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        mMediaFiles = mediaFiles;
    }

    private MediaFile mTempMediaFile;

    public MediaFile getTempMediaFile() {
        return this.mTempMediaFile;
    }

    public void setTempMediaFile(MediaFile mediaFile) {
        this.mTempMediaFile = mediaFile;
    }

    private AirCraftLocationBean mLocationBean;

    public AirCraftLocationBean getLocationBean() {
        return mLocationBean;
    }

    public void setLocationBean(AirCraftLocationBean locationBean) {
        this.mLocationBean = locationBean;
    }


//    private MediaFile mLastMediaFile;
//
//    public MediaFile getLastMediaFile() {
//        return this.mMediaFiles.get(mMediaFiles.size() - 1);
//    }
//
//
//    private MediaFile mMediaFileWithThum;
//
//    public MediaFile getMediaFileWithThum() {
//        return mMediaFileWithThum;
//    }
//
//    public void setMediaFileWithThum(MediaFile file) {
//        mMediaFileWithThum = file;
//    }
//
//
//    private MediaFile mMediaFileWithPreview;
//
//    public MediaFile getMediaFileWithPreview() {
//        return mMediaFileWithPreview;
//    }
//
//    public void setMediaFileWithPreview(MediaFile file) {
//        mMediaFileWithPreview = file;
//    }

}
