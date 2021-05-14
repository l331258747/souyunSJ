package com.xrwl.driver.module.home.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.GongAnAuth;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.mvp.MyPresenter;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午8:43.
 */
public interface DriverAuthContract {
    interface IView extends BaseMVP.IBaseView<Auth> {
        void onPostSuccess(BaseEntity<GongAnAuth> entity);
        void onPostError(BaseEntity entity);
        void onPostError(Throwable e);

        void onPostputongSuccess(BaseEntity entity);
        void onPostputongError(BaseEntity entity);
        void onPostputongError(Throwable e);

        void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity);
        void shenfenzhengError(BaseEntity entity);
    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void postData(Map<String, String> picMaps, Map<String, String> params);
        public abstract void postputongData(Map<String, String> params);
        public abstract void postputongchexingData(Map<String, String> params);
        public abstract void shenfenzheng(String face_cardimg,String fileType, String type);
        public abstract void getData();
        public abstract void getCode(String phone);
    }

    interface IModel {
        Observable<BaseEntity<Auth>> getData(Map<String, String> params);
        Observable<BaseEntity<GongAnAuth>> postData(Map<String, RequestBody> params);
        Observable<BaseEntity> postputongData(Map<String, String> params);
        Observable<BaseEntity> postputongchexingData(Map<String, String> params);
        Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params);
        Observable<BaseEntity<GongAnAuth>> shenfenzheng(Map<String, String> params);
    }
}
