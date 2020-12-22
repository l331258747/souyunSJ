package com.xrwl.driver.module.business.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午7:08.
 */
public class BusinessModel implements BusinessContract.IModel {
    @Override
    public Observable<BaseEntity<List<Business>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getBusinessList(params).compose(RxSchedulers.<BaseEntity<List<Business>>>compose());
    }
}
