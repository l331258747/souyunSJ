package com.xrwl.driver.module.publish.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.PublishBean;
import com.xrwl.driver.module.publish.mvp.OrderConfirmContract;
import com.xrwl.driver.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.driver.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单确认
 * Created by www.longdw.com on 2018/4/3 下午4:30.
 */

public class OrderConfirmActivity extends BaseActivity<OrderConfirmContract.IView, OrderConfirmPresenter> implements OrderConfirmContract.IView {

    @BindView(R.id.ocDateTv)
    TextView mDateTv;
    @BindView(R.id.ocTruckTv)
    TextView mTruckTv;
    @BindView(R.id.ocStartLocationTv)
    TextView mStartLocationTv;
    @BindView(R.id.ocEndLocationTv)
    TextView mEndLocationTv;
    @BindView(R.id.ocTruckLayout)
    View mTruckLayout;

    @BindView(R.id.ocOnlinePayCb)
    CheckBox mOnlinePayCb;
    @BindView(R.id.ocOfflinePayCb)
    CheckBox mOfflinePayCb;
    @BindView(R.id.ocPayLayout)
    View mPayLayout;
    @BindView(R.id.ocConfirmBtn)
    Button mConfirmBtn;

    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private PublishBean mPublishBean;
    private PostOrder mPostOrder;
    private ProgressDialog mPayDialog;
    private ProgressDialog mOnlinePayDialog;

    @Override
    protected OrderConfirmPresenter initPresenter() {
        return new OrderConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ordercomfirm_activity;
    }

    @Override
    protected void initViews() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("未付款订单，会大大降低接单率！");
        //设置确认按钮
        builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(mContext, OwnerOrderActivity.class));
            }
        });
        //设置取消按钮
        builder.setPositiveButton("取消",null);
        //显示弹框
        builder.show();
        mPublishBean = (PublishBean) getIntent().getSerializableExtra("publishBean");
        mPostOrder = (PostOrder) getIntent().getSerializableExtra("postOrder");
        mStartLocationTv.setText(mPublishBean.getStartPos());
        mEndLocationTv.setText(mPublishBean.getEndPos());

        if (mPublishBean.truck != null) {

            if(TextUtils.isEmpty(mPublishBean.truck.title))
            {
                mTruckTv.setText("无车型需求");
            }
            else
            {
                mTruckTv.setText(mPublishBean.truck.title);
            }

        } else {
            mTruckLayout.setVisibility(View.GONE);
        }
        mDateTv.setText(mPublishBean.date);

        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
    }

    @OnClick({
            R.id.ocWeixinPayLayout, R.id.ocAliPayLayout, R.id.ocOnlinePayCb,
            R.id.ocOfflinePayCb, R.id.ocConfirmBtn
    })
    public void onClick(View v) {
        if (v.getId() == R.id.ocWeixinPayLayout) {
            Map<String, String> params = new HashMap<>();
            params.put("appname", AppUtils.getAppName());
            params.put("product_name", mPublishBean.productName);
            params.put("describe", mPublishBean.defaultWeight + "吨" + mPublishBean.defaultArea + "方");
            params.put("orderid", mPostOrder.orderId);
            params.put("type", "1");
            params.put("price", mPublishBean.getPrice() + "");

            mPayDialog = LoadingProgress.showProgress(this, "正在发起支付...");
            mPresenter.wxPay(params);
        } else if (v.getId() == R.id.ocOnlinePayCb) {
            if (mOnlinePayCb.isChecked()) {
                mOfflinePayCb.setChecked(false);
                mConfirmBtn.setVisibility(View.GONE);
                mPayLayout.setVisibility(View.VISIBLE);
            } else {
                mPayLayout.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.ocOfflinePayCb) {
            if (mOfflinePayCb.isChecked()) {
                mOnlinePayCb.setChecked(false);
                mPayLayout.setVisibility(View.GONE);
                mConfirmBtn.setVisibility(View.VISIBLE);
            } else {
                mConfirmBtn.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.ocConfirmBtn) {
            //这里提交
            mOnlinePayDialog = LoadingProgress.showProgress(this, "正在提交...");
            Map<String, String> params = new HashMap<>();
            params.put("id", mPostOrder.orderId);
            mPresenter.onlinePay(params);
        }
    }
    @Override
    public void onRefreshSuccess(BaseEntity<PayResult> entity) {

        mPayDialog.dismiss();
        final PayResult pr = entity.getData();

        //发起微信或支付宝支付
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!mWXApi.isWXAppInstalled()) {
                    Toast.makeText(mContext, "没有安装微信哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mWXApi.isWXAppSupportAPI()) {
                    Toast.makeText(mContext, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
                    return;
                }
//                PayReq request = new PayReq();
//                request.appId = pr.appid;
//                request.partnerId = pr.partnerid;
//                request.prepayId = pr.prepayid;
//                request.packageValue = pr.packagestr;
//                request.nonceStr = pr.noncestr;
//                request.timeStamp = pr.timestamp;
//                request.sign = pr.sign;
//                mWXApi.registerApp(pr.appid);
//                mWXApi.sendReq(request);
//                showToast("启动微信中...");
            }
        });
    }

    @Override
    public void onRefreshError(Throwable e) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        handleError(entity);
    }

    @Override
    protected void onDestroy() {

        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }
       super.onDestroy();
    }

    @Override
    public void onOnlinePaySuccess(BaseEntity<PayResult> entity) {
        mOnlinePayDialog.dismiss();
        showToast("提交成功");

        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {

    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                showToast("付款成功");

                startActivity(new Intent(mContext, PublishSuccessActivity.class));

                finish();
            } else {
                showToast("付款失败");
            }
        }

    }
}
