package com.xrwl.driver.module.tab.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/4 上午10:47.
 */
public class TabModel implements TabContract.IModel {
    @Override
    public Observable<BaseEntity> postLonLat(Map<String, String> params) {
        return RetrofitFactory.getInstance().postLonLat(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity<List<Business>>> getBadgeCount(Map<String, String> params) {
        return RetrofitFactory.getInstance().getBusinessList(params).compose(RxSchedulers.<BaseEntity<List<Business>>>compose());
    }
}
