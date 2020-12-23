package com.xrwl.driver.module.home.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.GongAnAuth;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.retrofit.OtherRetrofitFactory;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午9:11.
 */
public class DriverAuthModel implements DriverAuthContract.IModel {
    @Override
    public Observable<BaseEntity<Auth>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAuthInfo(params).compose(RxSchedulers.<BaseEntity<Auth>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postDriverAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> postputongData(Map<String, String> params) {
        return RetrofitFactory.getInstance().postputongAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> postputongchexingData(Map<String, String> params) {
        return RetrofitFactory.getInstance().postputongAuthInfochexing(params).compose(RxSchedulers.<BaseEntity>compose());
    }


    @Override
    public Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").getCode(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }

    @Override
    //public/admin/idcar/cardids
    public Observable<BaseEntity<GongAnAuth>> shenfenzheng(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").shenfenzheng(params).compose(RxSchedulers
                .<BaseEntity<GongAnAuth>>compose());
    }
}
