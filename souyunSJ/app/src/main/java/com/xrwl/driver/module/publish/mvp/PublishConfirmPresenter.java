package com.xrwl.driver.module.publish.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.retrofit.BaseObserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/19 下午1:34.
 */
public class PublishConfirmPresenter extends PublishConfirmContract.APresenter {

    private PublishConfirmContract.IModel mModel;

    public PublishConfirmPresenter(Context context) {
        super(context);

        mModel = new PublishConfirmModel();
    }

    @Override
    public void postOrder(List<String> imagePaths, Map<String, String> params) {

        Map<String, RequestBody> images = new HashMap<>();
        if (imagePaths != null) {
            for (String path : imagePaths) {
                File file = new File(path);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                images.put("images\"; filename=\"" + file.getName(), requestBody);
            }
        }

        params.put("userid", getAccount().getId());

        for (String key : params.keySet()) {
            try {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    images.put(key, RequestBody.create(MediaType.parse("text/plain"), encodedParam));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        mModel.postOrder(images).subscribe(new BaseObserver<PostOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PostOrder> entity) {
                if (entity.isSuccess()) {
                    mView.onRefreshSuccess(entity);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void postOrder1(List<String> imagePaths, Map<String, String> params) {
        Map<String, RequestBody> images = new HashMap<>();
        if (imagePaths != null) {
            for (String path : imagePaths) {
                File file = new File(path);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                images.put("images\"; filename=\"" + file.getName(), requestBody);
            }
        }

        params.put("userid", getAccount().getId());

        for (String key : params.keySet()) {
            try {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    images.put(key, RequestBody.create(MediaType.parse("text/plain"), encodedParam));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        mModel.postOrder(images).subscribe(new BaseObserver<PostOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<PostOrder> entity) {

            }

            @Override
            protected void onHandleError(Throwable e) {
            }
        });
    }
}
