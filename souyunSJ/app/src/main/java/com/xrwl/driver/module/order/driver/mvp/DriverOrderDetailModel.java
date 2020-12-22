package com.xrwl.driver.module.order.driver.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.retrofit.OtherRetrofitFactory;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:39.
 */
public class DriverOrderDetailModel implements DriverOrderContract.IDetailModel {
    @Override
    public Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params) {
        return RetrofitFactory.getInstance().getDriverOrderDetail(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> getOrderDetailReceiving(Map<String, String> params) {
        return RetrofitFactory.getInstance().getDriverOrderDetail(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }


    @Override
    public Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params) {
        return RetrofitFactory.getInstance().nav(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }


    @Override
    public Observable<BaseEntity<OrderDetail>> cancelOrdertixing(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelDriverOrdertixing(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }


    @Override
    public Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelDriverOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshu(Map<String, String> params) {
        return RetrofitFactory.getInstance().cancelDriverkaishiyunshuOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmDriverOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> confirmqianOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().confirmDriverqianOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> trans(Map<String, String> params) {
        return RetrofitFactory.getInstance().trans(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<OrderDetail>> grapOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().grapOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }


    @Override
    public Observable<BaseEntity<OrderDetail>> grappwdOrder(Map<String, String> params) {
        return RetrofitFactory.getInstance().grappwdOrder(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }


    @Override
    public Observable<BaseEntity<OrderDetail>> uploadImages(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().uploadDriverImages(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params);
    }

    @Override
    public Observable<BaseEntity<MsgCode>> getCodeButton(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("https://jiekou.16souyun.com/").getCodeButton(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }
    @Override
    public Observable<BaseEntity<OrderDetail>> hit(Map<String, String> params) {
        return RetrofitFactory.getInstance().hit(params).compose(RxSchedulers.<BaseEntity<OrderDetail>>compose());
    }

    @Override
    public Observable<BaseEntity<Distance>> calculateDistance(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").calculateDistance(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }
}
