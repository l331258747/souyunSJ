package com.xrwl.driver.module.business.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.retrofit.BaseObserver;
import com.xrwl.driver.utils.AccountUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/8/16 下午7:24.
 */
public class BusinessPresenter extends BusinessContract.APresenter {

    private final Account mAccount;
    private BusinessContract.IModel mModel;

    public BusinessPresenter(Context context) {
        super(context);
        mModel = new BusinessModel();
        mAccount = AccountUtil.getAccount(context);
    }

    @Override
    public void requestData(String classify) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", mAccount.getId());
        params.put("classify", classify);
        mModel.getData(params).subscribe(new BaseObserver<List<Business>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Business>> entity) {
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
    public void requestMoreData() {

    }
}
