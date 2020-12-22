package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Receipt;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:07.
 */
public interface ReceiptContract {
    interface IView extends BaseMVP.IBaseView<List<Receipt>> {
        void onPostSuccess(BaseEntity entity);
        void onPostError(BaseEntity entity);
        void onPostError(Throwable e);
    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData();
        public abstract void postData(HashMap<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<Receipt>>> getData(Map<String, String> params);
        Observable<BaseEntity> postData(Map<String, String> params);
    }
}
