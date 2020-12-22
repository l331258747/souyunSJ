package com.xrwl.driver.module.order.driver.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.event.BankListRefreshEvent;
import com.xrwl.driver.event.DriverListRrefreshEvent;
import com.xrwl.driver.event.DriverOrderListRrefreshEvent;
import com.xrwl.driver.module.find.ui.FindFragment;
import com.xrwl.driver.module.me.mvp.AddBankPresenter;
import com.xrwl.driver.module.me.mvp.BankContract;
import com.xrwl.driver.module.order.driver.mvp.DriverOrderContract;
import com.xrwl.driver.module.order.driver.mvp.DriverOrderDetailPresenter;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.tab.activity.TabActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 面对面建群订单
 * Created by www.longdw.com on 2018/4/5 上午12:24.
 */
public abstract class SiLiaoOrderActivity extends BaseActivity<DriverOrderContract.IDetailView, DriverOrderDetailPresenter>
        implements DriverOrderContract.IDetailView,View.OnClickListener {

    @BindView(R.id.addBankNumEt)
    EditText mNumEt;//厂家密钥
    @BindView(R.id.drivertelEt)
    EditText mdrivertelEt;//厂家密钥
    private ProgressDialog mProgressDialog;

    @Override
    protected DriverOrderDetailPresenter initPresenter() {
        return new DriverOrderDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.addjianqun_activity;
    }

    @Override
    protected void initViews() { }

   @Override
    public void onRefreshSuccess(BaseEntity entity) {
//        mProgressDialog.dismiss();
//        EventBus.getDefault().post(new BankListRefreshEvent());
//        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
       // mProgressDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
       // mProgressDialog.dismiss();
        handleError(entity);
    }

    @OnClick(R.id.addBtn)
    public void confirm() {
            String num = mNumEt.getText().toString();
            if (TextUtils.isEmpty(num)) {
                showToast("请输入正确的密钥");
                return;
            }
            if (TextUtils.isEmpty(mdrivertelEt.getText().toString()) || mdrivertelEt.getText().toString().length() <11) {
                showToast("请输入正确的手机号");
                return;
            }
            mPresenter.grappwdOrder(num);
       }
    @Override
    public void showLoading() {

    }

    @Override
    public void onWxPaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onWxPayError(Throwable e) {

    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onNavError(Throwable e) {

    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelOrderError(Throwable e) {

    }

    @Override
    public void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {
        showToast("提示成功");
    }

    @Override
    public void onCancelDriverkaishiyunshuError(Throwable e) {

    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmOrderError(Throwable e) {

    }

    @Override
    public void onConfirmqianOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmqianOrderError(Throwable e) {

    }

    @Override
    public void onLocationDriverSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onLocationDriverError(Throwable e) {

    }

    @Override
    public void onTransSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onTransError(Throwable e) {

    }

    @Override
    public void onGrapOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onGrapOrderError(Throwable e) {

    }

    @Override
    public void onGrappwdOrderSuccess(BaseEntity<OrderDetail> entity) {

        showToast("确认接活成功");
        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("我的订单", 1);
        startActivity(intent);

        finish();
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        EventBus.getDefault().post(new DriverListRrefreshEvent());
    }

    @Override
    public void onGrappwdOrderError(Throwable e) {

        showToast("接活失败");
    }

    @Override
    public void onUploadImagesSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onUploadImagesError(Throwable e) {

    }
}
