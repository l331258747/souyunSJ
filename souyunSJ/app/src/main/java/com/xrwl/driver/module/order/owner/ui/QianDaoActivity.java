package com.xrwl.driver.module.order.owner.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.tab.activity.TabActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class QianDaoActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>
        implements OwnerOrderContract.IDetailView {
    @BindView(R.id.qiandao)
    ImageView mqiandao;
   @BindView(R.id.fanhuiqd)
   ImageView mfanhuiqd;
   @Override
    protected OwnerOrderDetailPresenter initPresenter() {
        return new OwnerOrderDetailPresenter(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.qiandao;
    }
    @Override
    protected void initViews() {

     }

    @OnClick({ R.id.qiandao,R.id.fanhuiqd})
    public void onClick(View v) {
        if (v.getId() == R.id.qiandao) {
           showToast("正在开发中....");
         }
        else if(v.getId() == R.id.fanhuiqd)
        {
         startActivity(new Intent(mContext, TabActivity.class));
        }
    }


    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelOrderError(Throwable e) {

    }



    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmOrderError(Throwable e) {

    }


    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onLocationError(Throwable e) {

    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onPayError(Throwable e) {

    }


    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
