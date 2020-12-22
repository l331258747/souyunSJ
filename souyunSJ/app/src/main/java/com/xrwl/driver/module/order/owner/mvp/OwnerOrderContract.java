package com.xrwl.driver.module.order.owner.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.mvp.MyLoadPresenter;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:14.
 */
public interface OwnerOrderContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Order>> {
    }

    abstract class APresenter extends MyLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getOrderList(String type);

        public abstract void getMoreOrderList(String type);
    }

    interface IModel {
        Observable<BaseEntity<List<Order>>> getOrderList(Map<String, String> params);
    }

    interface IDetailView extends BaseMVP.IBaseView<OrderDetail> {
        void onCancelOrderSuccess(BaseEntity<OrderDetail> entity);
        void onCancelOrderError(Throwable e);

        void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmOrderError(Throwable e);

        void onLocationSuccess(BaseEntity<OrderDetail> entity);
        void onLocationError(Throwable e);

        void onPaySuccess(BaseEntity<PayResult> entity);
        void onPayError(Throwable e);
    }

    abstract class ADetailPresenter extends MyPresenter<IDetailView> {

        public ADetailPresenter(Context context) {
            super(context);
        }

        public abstract void getOrderDetail(String id);

        public abstract void cancelOrder(String id);
        public abstract void confirmOrder(String id);
        public abstract void location(String id, String driverId);

        public abstract void wxPay(Map<String, String> params);
    }

    interface IDetailModel {
        Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params);

        Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> location(Map<String, String> params);

        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
    }
}
