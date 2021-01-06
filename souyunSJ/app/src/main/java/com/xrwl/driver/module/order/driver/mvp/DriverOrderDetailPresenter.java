package com.xrwl.driver.module.order.driver.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.Photo;
import com.xrwl.driver.retrofit.BaseObserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:42.
 */
public class DriverOrderDetailPresenter extends DriverOrderContract.ADetailPresenter {

    private DriverOrderContract.IDetailModel mModel;

    public DriverOrderDetailPresenter(Context context) {
        super(context);

        mModel = new DriverOrderDetailModel();
    }

    @Override
    public void getOrderDetail(String id) {

        mView.showLoading();

        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        mModel.getOrderDetail(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void nav(String id) {
        mModel.nav(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onNavSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onNavError(e);
            }
        });
    }

    private Map<String, String> getParams(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        return params;
    }

    @Override
    public void cancelOrder(String id) {
        mModel.cancelOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onCancelOrderError(e);
            }
        });
    }


    @Override
    public void cancelOrdertixing(String id) {
        mModel.cancelOrdertixing(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelOrdertixingSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onCancelOrdertixingError(e);
            }
        });
    }

    @Override
    public void cancelDriverkaishiyunshu(String id) {
        mModel.cancelDriverkaishiyunshu(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onCancelDriverkaishiyunshuSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onCancelDriverkaishiyunshuError(e);
            }
        });
    }

    @Override
    public void confirmOrder(String id) {
        mModel.confirmOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onConfirmOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onConfirmOrderError(e);
            }
        });
    }

    @Override
    public void confirmqianOrder(String id) {
        mModel.confirmqianOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onConfirmqianOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onConfirmqianOrderError(e);
            }
        });
    }

    @Override
    public void trans(String id) {
        mModel.trans(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onTransSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onTransError(e);
            }
        });
    }

    @Override
    public void hit(String id) {
        mModel.hit(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onHitSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onHitError(e);
            }
        });
    }

    @Override
    public void grapOrder(String id) {
        mModel.grapOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {

                    mView.onGrapOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGrapOrderError(e);
            }
        });
    }


    @Override
    public void grappwdOrder(String id) {
        Map<String, String> strParams = new HashMap<>();

        strParams.put("userid", getAccount().getId());
        mModel.grappwdOrder(getParams(id)).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onGrappwdOrderSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGrappwdOrderError(e);
            }
        });
    }


    @Override
    public void uploadImages(String id, List<Photo> images) {
        Map<String, RequestBody> params = new HashMap<>();

        Map<String, String> strParams = new HashMap<>();
        strParams.put("id", id);
        strParams.put("userid", getAccount().getId());

        for (String key : strParams.keySet()) {
            try {
                String value = strParams.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    params.put(key, RequestBody.create(MediaType.parse("text/plain"), encodedParam));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        for (Photo photo : images) {
            if (photo.isCanDelete()) {
                File file = new File(photo.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                params.put("images\"; filename=\"" + file.getName(), requestBody);
            }
        }

        mModel.uploadImages(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onUploadImagesSuccess(entity);
                } else {
                    mView.onUploadImagesError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onUploadImagesError(e);
            }
        });
    }

    @Override
    public void pay(Map<String, String> params) {
        mModel.pay(params).subscribe(new BaseObserver<PayResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PayResult> entity) {
                if (entity.isSuccess()) {
                    mView.onWxPaySuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onWxPayError(e);
            }
        });
    }

    @Override
    public void getCodeButton(String mobile, String type, String start_city, String end_city, String order_sn, String surname, String logistics, String name_name) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobile);
        params.put("type", "3");
        params.put("start_city", start_city);
        params.put("end_city", end_city);
        params.put("order_sn", order_sn);
        params.put("surname", surname);
        params.put("logistics", logistics);
        params.put("name_name", name_name);
        mModel.getCodeButton(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                    mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onGetCodeError(e);
            }
        });
    }

    @Override
    public void getCodeButton(String type, String start_city, String end_city, String order_sn, String surname, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("type", "4");
        params.put("start_city", start_city);
        params.put("end_city", end_city);
        params.put("order_sn", order_sn);
        params.put("surname", surname);
        params.put("phone", phone);
        mModel.getCodeButton(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
             @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                    mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.onGetCodeError(e);
                }
            }
            @Override
            protected void onHandleError(Throwable e) {
                mView.onGetCodeError(e);
            }
        });
    }

    @Override
    public void calculateDistanceWithLonLat(double startLon, double startLat, double endLon, double endLat) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("type", "0");
        params.put("startx", startLon + "");
        params.put("starty", startLat + "");
        params.put("endx", endLon + "");
        params.put("endy", endLat + "");

        mModel.calculateDistance(params).subscribe(new BaseObserver<Distance>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Distance> entity) {
                if (entity.isSuccess()) {
                    mView.oncalculateDistanceSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }
            @Override
            protected void onHandleError(Throwable e) {
                mView.oncalculateDistanceError(e);
            }
        });
    }

    @Override
    public void getOrderDetailReceiving(String category) {

        mView.showLoading();

        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        mModel.getOrderDetailReceiving(params).subscribe(new BaseObserver<OrderDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<OrderDetail> entity) {
                if (entity.isSuccess()) {
                    mView.onOrderDetailReceivingSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onOrderDetailReceivingError(e);
            }
        });
    }

}
