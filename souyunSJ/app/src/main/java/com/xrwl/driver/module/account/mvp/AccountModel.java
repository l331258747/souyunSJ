package com.xrwl.driver.module.account.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.Register;
import com.xrwl.driver.retrofit.OtherRetrofitFactory;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:28.
 */
public class AccountModel implements AccountContract.IModel {

    @Override
    public Observable<BaseEntity<Account>> login(Map<String, String> params) {
        return RetrofitFactory.getInstance().login(params).compose(RxSchedulers.<BaseEntity<Account>>compose());
    }

    @Override
    public Observable<BaseEntity<Register>> register(Map<String, String> params) {
        return RetrofitFactory.getInstance().register(params).compose(RxSchedulers.<BaseEntity<Register>>compose());
    }


    @Override
    public Observable<BaseEntity<Register>> registergonghui(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("https://gh.jishanhengrui.com/").registergonghui(params).compose(RxSchedulers
                .<BaseEntity<Register>>compose());
    }


    @Override
    public Observable<BaseEntity> modify(Map<String, String> params) {
        return RetrofitFactory.getInstance().modify(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> retrieve(Map<String, String> params) {
        return RetrofitFactory.getInstance().modify(params).compose(RxSchedulers.<BaseEntity>compose());
    }





    @Override
    public Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").getCode(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }
}
