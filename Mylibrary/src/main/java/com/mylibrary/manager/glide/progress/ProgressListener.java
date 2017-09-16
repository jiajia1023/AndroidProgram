package com.mylibrary.manager.glide.progress;

public interface ProgressListener {

    void progress(long bytesRead, long maxLength, boolean done);

}
