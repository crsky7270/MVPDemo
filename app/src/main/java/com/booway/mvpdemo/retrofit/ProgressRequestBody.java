package com.booway.mvpdemo.retrofit;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by wandun on 2018/12/14.
 */

public class ProgressRequestBody extends RequestBody {

    public interface UploadProgressListener {
        void onProgress(long currentBytsCount, long totalBytesCount);
    }

    private final RequestBody mRequestBody;

    private final UploadProgressListener mUploadProgressListener;

    private BufferedSink mBufferedSink;

    public ProgressRequestBody(RequestBody requestBody, UploadProgressListener progressListener) {
        this.mRequestBody = requestBody;
        this.mUploadProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == mBufferedSink) {
            mBufferedSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long writtenBytesCount = 0L;
            long totalBytesCount = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writtenBytesCount += byteCount;
                if (totalBytesCount == 0)
                    totalBytesCount = contentLength();
                Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            mUploadProgressListener.onProgress(writtenBytesCount, totalBytesCount);
                        });
            }
        };
    }
}
