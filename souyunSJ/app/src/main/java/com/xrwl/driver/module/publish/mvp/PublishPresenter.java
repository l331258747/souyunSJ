package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/23 上午9:12.
 */
public class PublishPresenter extends PublishContract.APresenter {

    private PublishContract.IModel mModel;

    public PublishPresenter(Context context) {
        super(context);

        mModel = new PublishModel();
    }

    @Override
    public void calculateDistanceWithLonLat(double startLon, double startLat, double endLon, double endLat) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("type", "0");
        params.put("startx", startLon + "");
        params.put("starty", startLat + "");
        params.put("endx", endLon + "");
        params.put("endy", endLat + "");
        mModel.calculateDistance(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void calculateDistanceWithCityName(String startCity, String endCity, String startPro, String endPro) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
//        params.put("type", "1");
        params.put("startadd", startCity);
        params.put("endadd", endCity);
        params.put("pstart", startPro);
        params.put("pend", endPro);
        mModel.calculateLongDistance(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void requestCityLonLat(String startCity, final String endCity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("startadd", startCity);
        params.put("endadd", endCity);
        mModel.requestCityLonLat(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.onRequestCityLatLonSuccess(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {

            }
        });
    }
}
