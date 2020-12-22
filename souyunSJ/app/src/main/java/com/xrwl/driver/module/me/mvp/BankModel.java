package com.xrwl.driver.module.me.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.HistoryOrder;
import com.xrwl.driver.module.me.bean.Bank;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/21 上午9:27.
 */
public class BankModel implements BankContract.IModel {
    @Override
    public Observable<BaseEntity> addBank(Map<String, String> params) {
        return RetrofitFactory.getInstance().addBank(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity<List<Bank>>> getBankList(Map<String, String> params) {
        return RetrofitFactory.getInstance().getBankList(params).compose(RxSchedulers.<BaseEntity<List<Bank>>>compose());
    }

    @Override
    public Observable<BaseEntity<HistoryOrder>> getTotalPrice(Map<String, String> params) {
        return RetrofitFactory.getInstance().getHistoryList(params).compose(RxSchedulers.<BaseEntity<HistoryOrder>>compose());
    }

    @Override
    public Observable<BaseEntity> tixian(Map<String, String> params) {
        return RetrofitFactory.getInstance().tixian(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
