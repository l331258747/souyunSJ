package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.module.publish.bean.PayResult;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 下午1:22.
 */
public interface OrderConfirmContract {
    interface IView extends BaseMVP.IBaseView<PayResult> {
        void onOnlinePaySuccess(BaseEntity<PayResult> entity);
        void wxonOnlinePaySuccess(BaseEntity<PayResult> entity);
    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void wxPay(Map<String, String> params);

        public abstract void onlinePay(Map<String, String> params);

        public abstract void xrwlwxpay(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> onlinePay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params);
    }
}
