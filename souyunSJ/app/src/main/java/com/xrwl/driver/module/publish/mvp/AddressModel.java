package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Address2;
import com.xrwl.driver.bean.New;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:24.
 */
public class AddressModel implements AddressContract.IModel {
    @Override
    public Observable<BaseEntity<List<Address2>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAddressList(params).compose(RxSchedulers.<BaseEntity<List<Address2>>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addAddress(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity<List<New>>> getDatanew(Map<String, String> params) {
        return RetrofitFactory.getInstance().getDatanew(params).compose(RxSchedulers.<BaseEntity<List<New>>>compose());
    }

    @Override
    public Observable<BaseEntity<List<New>>> getDatanewshow(Map<String, String> params) {
        return null;
    }
}
