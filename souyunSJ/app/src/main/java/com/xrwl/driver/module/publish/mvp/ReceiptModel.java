package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Receipt;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:24.
 */
public class ReceiptModel implements ReceiptContract.IModel {

    @Override
    public Observable<BaseEntity<List<Receipt>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getReceiptList(params).compose(RxSchedulers.<BaseEntity<List<Receipt>>>compose());
    }
    @Override
    public Observable<BaseEntity> postData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addReceipt(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
