package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.module.publish.bean.Truck;
import com.xrwl.driver.retrofit.BaseObserver;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/16 下午1:15.
 */
public class TruckPresenter extends TruckContract.APresenter {

    private TruckContract.IModel mModel;

    public TruckPresenter(Context context) {
        super(context);

        mModel = new TruckModel();
    }

    @Override
    public void getList(Map<String, String> params) {
        mModel.getList(params).subscribe(new BaseObserver<List<Truck>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Truck>> entity) {
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
}
