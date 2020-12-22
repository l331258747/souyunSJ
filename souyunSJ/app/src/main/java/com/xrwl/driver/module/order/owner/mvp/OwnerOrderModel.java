package com.xrwl.driver.module.order.owner.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:17.
 */
public class OwnerOrderModel implements OwnerOrderContract.IModel {
    @Override
    public Observable<BaseEntity<List<Order>>> getOrderList(Map<String, String> params) {
        return RetrofitFactory.getInstance().getOwnerOrderList(params).compose(RxSchedulers.<BaseEntity<List<Order>>>compose());
    }
}
