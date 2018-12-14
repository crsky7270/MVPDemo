package com.booway.mvpdemo.retrofit;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wandun on 2018/12/14.
 */
public class DownloadResponseBody<T> extends ResponseBody {


    private ResponseBody mResponseBody;

    private DownloadListener mDownloadListener;

    private BufferedSource mBufferedSource;

    public DownloadResponseBody(ResponseBody body, DownloadListener listener) {
        this.mResponseBody = body;
        this.mDownloadListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null)
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (mDownloadListener != null) {
                    if (bytesRead != -1)
                        mDownloadListener.onProgress((int) (totalBytesRead * 100 / mResponseBody.contentLength()));
                }
                return bytesRead;
            }
        };
    }
}
