package com.xrwl.driver.module.find.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.mvp.MyLoadPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:10.
 */
public interface FindContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Order>> {
        void showLoading();
    }

    abstract class APresenter extends MyLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData(Map<String, String> params);
        public abstract void getDatamoren(Map<String, String> params);
        public abstract void getDataaddress(Map<String, String> params);
        public abstract void getMoreData(Map<String, String> params);

        public abstract void grappwdOrder(String num);
    }

    interface IModel {
        Observable<BaseEntity<List<Order>>> getDatamoren(Map<String, String> params);
        Observable<BaseEntity<List<Order>>> getData(Map<String, String> params);
        Observable<BaseEntity<List<Order>>> getDataaddress(Map<String, String> params);
        Observable<BaseEntity<OrderDetail>> grappwdOrder(Map<String, String> params);
    }
}
