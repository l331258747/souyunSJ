package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.module.publish.bean.Truck;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/16 下午1:16.
 */
public class TruckModel implements TruckContract.IModel {
    @Override
    public Observable<BaseEntity<List<Truck>>> getList(Map<String, String> params) {
        return RetrofitFactory.getInstance().getTruckList(params).compose(RxSchedulers.<BaseEntity<List<Truck>>>compose());
    }
}
