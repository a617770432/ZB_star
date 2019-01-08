package com.xingqiuzhibo.phonelive.interfaces;

public interface ProgressListener {

    void onProgress(long currentBytes, long contentLength, boolean done);

}
