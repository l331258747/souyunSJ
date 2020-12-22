package com.xrwl.driver.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.retrofit.OtherRetrofitFactory;
import com.xrwl.driver.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 上午9:10.
 */
public class PublishModel implements PublishContract.IModel {
    @Override
    public Observable<BaseEntity<Distance>> calculateDistance(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").calculateDistance(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }

    @Override
    public Observable<BaseEntity<Distance>> calculateLongDistance(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").calculateLongDistance(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }

    @Override
    public Observable<BaseEntity<Distance>> requestCityLonLat(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").requestCityLonLat(params).compose(RxSchedulers
                .<BaseEntity<Distance>>compose());
    }
}
