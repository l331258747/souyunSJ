package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.module.publish.bean.Truck;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/16 下午1:12.
 */
public interface TruckContract {
    interface IView extends BaseMVP.IBaseView<List<Truck>> {

    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getList(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<Truck>>> getList(Map<String, String> params);
    }
}
