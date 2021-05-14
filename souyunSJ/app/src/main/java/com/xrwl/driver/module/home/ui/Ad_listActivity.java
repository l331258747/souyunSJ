package com.xrwl.driver.module.home.ui;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.GongAnAuth;
import com.xrwl.driver.module.home.mvp.DriverAuthContract;
import com.xrwl.driver.module.home.mvp.DriverAuthPresenter;

/**
 * 广告图片显示
 * Created by www.longdw.com on 2018/4/4 下午10:29.
 */
public class Ad_listActivity extends BaseActivity<DriverAuthContract.IView, DriverAuthPresenter> implements DriverAuthContract
        .IView {
    @Override
    protected DriverAuthPresenter initPresenter() {
        return new DriverAuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ad_listactivity;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void getData() {
    }


    @Override
    public void onPostSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostError(BaseEntity entity) {

    }

    @Override
    public void onPostError(Throwable e) {

    }

    @Override
    public void onPostputongSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostputongError(BaseEntity entity) {

    }

    @Override
    public void onPostputongError(Throwable e) {

    }

    @Override
    public void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity) {

    }

    @Override
    public void shenfenzhengError(BaseEntity entity) {

    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
