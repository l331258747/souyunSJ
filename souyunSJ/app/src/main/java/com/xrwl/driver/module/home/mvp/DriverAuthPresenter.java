package com.xrwl.driver.module.home.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.retrofit.BaseObserver;
import com.xrwl.driver.retrofit.BaseSimpleObserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午9:16.
 */
public class DriverAuthPresenter extends DriverAuthContract.APresenter {

    private DriverAuthContract.IModel mModel;

    public DriverAuthPresenter(Context context) {
        super(context);
        mModel = new DriverAuthModel();
    }

    @Override
    public void postData(Map<String, String> picMaps, Map<String, String> params) {
        params.put("userid", getAccount().getId());
        Map<String, RequestBody> finalParams = new HashMap<>();

        for (String key : picMaps.keySet()) {
            String value = picMaps.get(key);
            File file = new File(value);
            finalParams.put("images\"; filename=\"" + key + ".png", RequestBody.create(MediaType.parse("image/png"), file));
        }

        for (String key : params.keySet()) {
            try {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    finalParams.put(key, RequestBody.create(MediaType.parse("text/plain"), encodedParam));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        mModel.postData(finalParams).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onPostSuccess(entity);
                } else {
                    mView.onPostError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPostError(e);
            }
        });
    }

    @Override
    public void getCode(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobile);
        mModel.getCode(params).subscribe(new BaseObserver<MsgCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<MsgCode> entity) {
                try {
                  //  mView.onGetCodeSuccess(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                   // mView.onGetCodeError(e);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {

                //mView.onGetCodeError(e);
            }
        });
    }
    @Override
    public void postputongData(Map<String, String> params) {
        params.put("userid", getAccount().getId());
         mModel.postputongData(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onPostSuccess(entity);
                } else {
                    mView.onPostError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPostError(e);
            }
        });
    }

    @Override
    public void postputongchexingData(Map<String, String> params) {
        params.put("userid", getAccount().getId());
        mModel.postputongchexingData(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onPostSuccess(entity);
                } else {
                    mView.onPostError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onPostError(e);
            }
        });
    }


    @Override
    public void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());

        mModel.getData(params).subscribe(new BaseObserver<Auth>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Auth> entity) {
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
}
