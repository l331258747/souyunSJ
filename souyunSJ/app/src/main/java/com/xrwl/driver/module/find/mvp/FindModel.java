package com.xrwl.driver.module.find.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:13.
 */
public class FindModel implements FindContract.IModel {
    @Override
    public Observable<BaseEntity<List<Order>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getFindList(params).compose(RxSchedulers.<BaseEntity<List<Order>>>compose());
    }
    @Override
    public Observable<BaseEntity<List<Order>>> getDatamoren(Map<String, String> params) {
        return RetrofitFactory.getInstance().getFindListmoren(params).compose(RxSchedulers.<BaseEntity<List<Order>>>compose());
    }
    @Override
    public Observable<BaseEntity<List<Order>>> getDataaddress(Map<String, String> params) {
        return RetrofitFactory.getInstance().getFindListaddrss(params).compose(RxSchedulers.<BaseEntity<List<Order>>>compose());
    }
    @Override
    public Observable<BaseEntity<OrderDetail>> grappwdOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().grappwdOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }
}
