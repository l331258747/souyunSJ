package com.xrwl.driver.module.find.mvp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.find.ui.FindFragment;
import com.xrwl.driver.module.me.ui.BankyuejinduActivity;
import com.xrwl.driver.module.me.ui.CouponActivity;
import com.xrwl.driver.retrofit.BaseObserver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:23.
 */
public class FindPresenter extends FindContract.APresenter {

    private FindContract.IModel mModel;
    private List<Order> mDatas;

    public FindPresenter(Context context) {
        super(context);

        mModel = new FindModel();
    }
    private Map<String, String> getParams(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        params.put("id", id);
        return params;
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
                    showToast("接货成功");
                    //startActivity(new Intent(mContext, FindFragment.class));
                } else {
                    showToast("接货失败,请查看个人订单");
                    return;

                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                showToast("接货失败，请查看个人订单");
                return;
            }
        });
    }
    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getData(Map<String, String> params) {
        mView.showLoading();
        mPageIndex = 0;

//        for (String key : params.keySet()) {
//            String value = params.get(key);
//            try {
//                params.put(key, URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }

        params.put("userid", getAccount().getId());
        params.put("transitpoint", getAccount().getTransitpoint());
        params.put("pages", mPageIndex + "");
        mModel.getData(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas = entity.getData();
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
    public void getDatamoren(Map<String, String> params) {
        mView.showLoading();
        mPageIndex = 0;
        params.put("userid", getAccount().getId());
        params.put("transitpoint", getAccount().getTransitpoint());
        params.put("pages", mPageIndex + "");

        mModel.getDatamoren(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas = entity.getData();
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
    public void getDataaddress(Map<String, String> params) {
        mView.showLoading();
        mPageIndex = 0;

        for (String key : params.keySet()) {
            String value = params.get(key);
            try {
                params.put(key, URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
               // Log.e(TAG, e.getMessage(), e);
            }
        }
        params.put("userid", getAccount().getId());
        params.put("transitpoint", getAccount().getTransitpoint());
        params.put("pages", mPageIndex + "");
        mModel.getDataaddress(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas = entity.getData();
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
    public void getMoreData(Map<String, String> params) {
        mPageIndex++;

//        for (String key : params.keySet()) {
//            String value = params.get(key);
//            try {
//                params.put(key, URLEncoder.encode(value, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
//        }

        mModel.getData(params).subscribe(new BaseObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Order>> entity) {
                if (entity.isSuccess()) {
                    mDatas.addAll(entity.getData());
                    entity.setData(mDatas);
                    mView.onRefreshSuccess(entity);
                } else {
                    mPageIndex--;
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mPageIndex--;
                mView.onRefreshError(e);
            }
        });
    }
}
