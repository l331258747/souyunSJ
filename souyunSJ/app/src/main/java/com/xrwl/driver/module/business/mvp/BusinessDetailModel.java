package com.xrwl.driver.module.business.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:33.
 */
public class BusinessDetailModel implements BusinessDetailContract.IModel {
    @Override
    public Observable<BaseEntity> requestData(Map<String, String> params) {
        return RetrofitFactory.getInstance().markBusinessRead(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
