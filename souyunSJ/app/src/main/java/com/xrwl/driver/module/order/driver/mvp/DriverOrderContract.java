package com.xrwl.driver.module.order.driver.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.Photo;
import com.xrwl.driver.mvp.MyLoadPresenter;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:14.
 */
public interface DriverOrderContract {
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

        void showLoading();

        void onWxPaySuccess(BaseEntity<PayResult> entity);
        void onWxPayError(Throwable e);

        void onNavSuccess(BaseEntity<OrderDetail> entity);
        void onNavError(Throwable e);

        void onCancelOrderSuccess(BaseEntity<OrderDetail> entity);
        void onCancelOrderError(Throwable e);

        void onCancelOrdertixingSuccess(BaseEntity<OrderDetail> entity);
        void onCancelOrdertixingError(Throwable e);

        void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity);
        void onCancelDriverkaishiyunshuError(Throwable e);

        void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmOrderError(Throwable e);

        void onConfirmqianOrderSuccess(BaseEntity<OrderDetail> entity);
        void onConfirmqianOrderError(Throwable e);

        void onLocationDriverSuccess(BaseEntity<OrderDetail> entity);
        void onLocationDriverError(Throwable e);

        void onTransSuccess(BaseEntity<OrderDetail> entity);
        void onTransError(Throwable e);

        void onHitSuccess(BaseEntity<OrderDetail> entity);
        void onHitError(Throwable e);

        void onGrapOrderSuccess(BaseEntity<OrderDetail> entity);
        void onGrapOrderError(Throwable e);

        void onGrappwdOrderSuccess(BaseEntity<OrderDetail> entity);
        void onGrappwdOrderError(Throwable e);

        void onUploadImagesSuccess(BaseEntity<OrderDetail> entity);
        void onUploadImagesError(BaseEntity entity);
        void onUploadImagesError(Throwable e);

        /**这个针对的是短信验证码货主和司机交互*/
        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);

        void oncalculateDistanceSuccess(BaseEntity<Distance> entity);
        void oncalculateDistanceError(Throwable e);

        void onOrderDetailReceivingSuccess(BaseEntity<OrderDetail> entity);
        void onOrderDetailReceivingError(Throwable e);
    }

    abstract class ADetailPresenter extends MyPresenter<IDetailView> {

        public ADetailPresenter(Context context) {
            super(context);
        }

        public abstract void getOrderDetail(String id);

        public abstract void getOrderDetailReceiving(String category);

        /** 导航 */
        public abstract void nav(String id);
        /** 取消订单 */
        public abstract void cancelOrder(String id);
        /** 司机线下现金支付确认 */
        public abstract void cancelOrdertixing(String id);
        /** 确认开始运输货主确认 */
        public abstract void cancelDriverkaishiyunshu(String id);
        /** 确认收货 */
        public abstract void confirmOrder(String id);
        /** 确认收货 前*/
        public abstract void confirmqianOrder(String id);
        /** 中转 */
        public abstract void trans(String id);
        /** 点击数 */
        public abstract void hit(String id);
        /** 抢单 */
        public abstract void grapOrder(String id);
        /** 私密抢单 */
        public abstract void grappwdOrder(String id);
        /** 上传图片 */
        public abstract void uploadImages(String id, List<Photo> images);

        public abstract void pay(Map<String, String> params);
        public abstract void getCodeButton(String mobile,String type,String start_city,String end_city,String order_sn,String surname,String logistics,String name_name);
        public abstract void getCodeButton(String type,String start_city,String end_city,String order_sn,String surname,String phone);
        public abstract void calculateDistanceWithLonLat(double startLon, double startLat, double endLon, double endLat);
    }

    interface IDetailModel {
        Observable<BaseEntity<OrderDetail>> getOrderDetail(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> getOrderDetailReceiving(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> nav(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelOrdertixing(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshu(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> confirmqianOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> trans(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> grapOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> grappwdOrder(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> uploadImages(Map<String, RequestBody> params);

        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
        Observable<BaseEntity<MsgCode>> getCodeButton(Map<String, String> params);

        Observable<BaseEntity<OrderDetail>> hit(Map<String, String> params);
        Observable<BaseEntity<Distance>> calculateDistance(Map<String, String> params);

    }
}
