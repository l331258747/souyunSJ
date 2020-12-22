package com.xrwl.driver.module.me.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.retrofit.BaseSimpleObserver;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/4/21 上午9:30.
 */
public class AddBankPresenter extends BankContract.AAddPresenter {

    private BankContract.IModel mModel;

    public AddBankPresenter(Context context) {
        super(context);

        mModel = new BankModel();
    }

    @Override
    public void addBank(Map<String, String> params) {
        params.put("userid", getAccount().getId());
        mModel.addBank(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
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
