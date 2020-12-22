package com.xrwl.driver.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/19 下午1:32.
 */
public interface PublishConfirmContract {
    interface IView extends BaseMVP.IBaseView<PostOrder> {

    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void postOrder(List<String> imagePaths, Map<String, String> params);

        public abstract void postOrder1(List<String> imagePaths, Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<PostOrder>> postOrder(Map<String, RequestBody> params);
    }
}
