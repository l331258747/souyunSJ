package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.retrofit.RetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/19 下午1:33.
 */
public class PublishConfirmModel implements PublishConfirmContract.IModel {
    @Override
    public Observable<BaseEntity<PostOrder>> postOrder(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postOrder(params).compose(RxSchedulers.<BaseEntity<PostOrder>>compose());
    }
}
