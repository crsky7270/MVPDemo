package com.booway.mvpdemo.component.djisdk;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：${DESC}
 */

public class DownloadMediaFileStatus {

    public enum DownloadMediaFileStatusEnum {
        Start,
        RateUpdate,
        Progress,
        Success,
        Failure
    }

    private DownloadMediaFileStatusEnum mDownloadMediaFileStatusEnum;

    public DownloadMediaFileStatusEnum getDownloadMediaFileStatusEnum() {
        return mDownloadMediaFileStatusEnum;
    }

    public void setDownloadMediaFileStatusEnum(DownloadMediaFileStatusEnum downloadMediaFileStatusEnum) {
        mDownloadMediaFileStatusEnum = downloadMediaFileStatusEnum;
    }



}
