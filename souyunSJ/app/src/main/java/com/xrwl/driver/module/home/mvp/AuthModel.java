package com.xrwl.driver.module.home.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午9:11.
 */
public class AuthModel implements AuthContract.IModel {
    @Override
    public Observable<BaseEntity<Auth>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAuthInfo(params).compose(RxSchedulers.<BaseEntity<Auth>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
