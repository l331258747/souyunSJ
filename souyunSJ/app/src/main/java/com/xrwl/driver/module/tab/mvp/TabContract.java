package com.xrwl.driver.module.tab.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/4 上午10:45.
 */
public interface TabContract {

    abstract class APresenter extends MyPresenter<BaseMVP.IBaseView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void postLonLat(double lon, double lat);

        public abstract void getBadgeCount();
    }

    interface IModel {
        Observable<BaseEntity> postLonLat(Map<String, String> params);

        Observable<BaseEntity<List<Business>>> getBadgeCount(Map<String, String> params);
    }
}
