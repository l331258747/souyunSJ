package com.xrwl.driver.module.account.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.Register;
import com.xrwl.driver.retrofit.BaseObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:33.
 */
public class RegisterPresenter extends AccountContract.ARegisterPresenter {

    private AccountModel mModel;

    public RegisterPresenter(Context context) {
        super(context);
        mModel = new AccountModel();
    }

    @Override
    public void register(Map<String, String> params) {
        mModel.register(params).subscribe(new BaseObserver<Register>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Register> entity) {
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
    public void registergonghui(String username,String device_type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("username", username);
        params.put("device_type", "android");
        mModel.registergonghui(params).subscribe(new BaseObserver<Register>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<Register> entity) {
                try {
                    mView.onGetregistergonghuiSuccess(entity);
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
}
