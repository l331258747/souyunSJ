package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.retrofit.OtherRetrofitFactory;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 下午1:24.
 */
public class OrderConfirmModel implements OrderConfirmContract.IModel {
    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> onlinePay(Map<String, String> params) {
        return RetrofitFactory.getInstance().onlinePay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://driverpay.16souyun.com/").xrwlwxpay(params).compose(RxSchedulers
                .<BaseEntity<PayResult>>compose());
    }


}
