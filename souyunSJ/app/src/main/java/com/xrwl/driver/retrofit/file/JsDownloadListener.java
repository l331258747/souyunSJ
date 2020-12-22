package com.xrwl.driver.retrofit.file;

public interface JsDownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}