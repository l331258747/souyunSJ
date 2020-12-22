package com.xrwl.driver.module.loading.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Update;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;
import com.xrwl.driver.retrofit.file.FileRetrofitFactory;
import com.xrwl.driver.retrofit.file.JsDownloadListener;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by www.longdw.com on 2018/5/10 下午7:54.
 */
public class LoadingModel implements LoadingContract.IModel {
    @Override
    public Observable<BaseEntity<Update>> checkUpdate(Map<String, String> params) {
        return RetrofitFactory.getInstance().checkUpdate(params).compose(RxSchedulers.<BaseEntity<Update>>compose());
    }

    @Override
    public Observable<ResponseBody> downloadApk(String url, JsDownloadListener l) {
        return FileRetrofitFactory.getInstance(l).downloadApk(url).compose(RxSchedulers.<ResponseBody>compose());
    }
}
