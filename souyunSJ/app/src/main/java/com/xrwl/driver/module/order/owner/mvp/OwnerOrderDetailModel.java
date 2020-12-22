package com.xrwl.driver.module.order.owner.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:39.
 */
public class OwnerOrderDetailModel implements OwnerOrderContract.IDetailModel {
    @Override
    public Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params) {
        return RetrofitFactory.getInstance().getOwnerOrderDetail(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmOwnerOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> location(Map<String, String> params) {
        return RetrofitFactory.getInstance().locationDriver(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }
}
