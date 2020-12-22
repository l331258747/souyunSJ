package com.xrwl.driver.module.publish.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.event.PublishClearCacheEvent;
import com.xrwl.driver.module.friend.bean.Friend;
import com.xrwl.driver.module.friend.ui.FriendActivity;
import com.xrwl.driver.module.publish.bean.Coupon;
import com.xrwl.driver.module.publish.bean.Insurance;
import com.xrwl.driver.module.publish.bean.PublishBean;
import com.xrwl.driver.module.publish.dialog.CategoryDialog;
import com.xrwl.driver.module.publish.dialog.CouponDialog;
import com.xrwl.driver.module.publish.dialog.InsuranceDialog;
import com.xrwl.driver.module.publish.dialog.RemarkDialog;
import com.xrwl.driver.module.publish.mvp.PublishConfirmContract;
import com.xrwl.driver.module.publish.mvp.PublishConfirmPresenter;
import com.xrwl.driver.view.RegionNumberEditText;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/26 下午9:29.
 */

public class PublishConfirmActivity extends BaseActivity<PublishConfirmContract.IView, PublishConfirmPresenter> implements PublishConfirmContract.IView {

    public static final int RESULT_FRIEND_PAY = 111;//代付
    public static final int RESULT_FRIEND_GET = 222;//代收

    @BindView(R.id.pcSendBySelfCb)
    CheckBox mSendBySelfCb;
    @BindView(R.id.pcPickBySelfCb)
    CheckBox mPickBySelfCb;
    @BindView(R.id.pcBySelfLayout)
    View mBySelfLayout;

    @BindView(R.id.pcBottomLayout)
    View mBottomLayout;
    @BindView(R.id.pcRootLayout)
    View mRootLayout;
    @BindView(R.id.pcTotalPriceTv)
    TextView mTotalPriceTv;

    @BindView(R.id.pcPopLayout)
    View mPopView;

    @BindView(R.id.pcArrowIv)
    ImageView mArrowIv;

    @BindView(R.id.pcInsuranceTv)
    public TextView mInsuranceTv;//保险

    @BindView(R.id.pcReceiptCb)
    public CheckBox mReceiptCb;
    @BindView(R.id.pcReceiptLayout)
    public View mReceiptView;

    @BindView(R.id.pcHelpPayCb)
    public CheckBox mHelpPayCb;
    @BindView(R.id.pcHelpPayLayout)
    public View mHelpPayView;

    @BindView(R.id.pcHelpGetCb)
    public CheckBox mHelpGetCb;
    @BindView(R.id.pcHelpGetLayout)
    public View mHelpGetView;
    @BindView(R.id.pcHelpGetPriceEt)
    EditText mHelpGetPriceEt;//代收货款

    private PublishBean mPublishBean;

    @BindView(R.id.pcProductNameTv)
    TextView mProductNameTv;
    @BindView(R.id.pcStartPositionTv)
    TextView mStartPosTv;
    @BindView(R.id.pcEndPositionTv)
    TextView mEndPosTv;
    @BindView(R.id.pcTruckTv)
    TextView mTruckTv;//车型
    @BindView(R.id.pcAreaTv)
    TextView mAreaTv;
    @BindView(R.id.pcPriceTv)
    TextView mPriceTv;
    @BindView(R.id.pcWeightTv)
    TextView mWeightTv;
    @BindView(R.id.pcDistanceTv)
    TextView mDistanceTv;
    @BindView(R.id.pcNumTv)
    TextView mNumTv;

    @BindView(R.id.pcRecommendLayout)
    View mRecommendView;//如果是同城 隐藏推荐
    @BindView(R.id.pcFreightEt)
    RegionNumberEditText mFreightEt;//运费
    @BindView(R.id.pcRecommendPriceTv)
    TextView mRecommendPriceTv;//推荐运费
    @BindView(R.id.pcPriceHintTv)
    TextView mPriceHintTv;//价格区间提示

    @BindView(R.id.pcTaxNumEt)
    EditText mTaxNumEt;//税号
    @BindView(R.id.pcUnitNameEt)
    EditText mUnitNameEt;//单位名称
    @BindView(R.id.pcEmailEt)
    EditText mEmailEt;//邮箱

    @BindView(R.id.pcCouponTv)
    public TextView mCouponTv;

    @BindView(R.id.pcHelpPayTv)
    TextView mHelpPayTv;//代付运费

    @BindView(R.id.tongchengyouhuijuan)
    LinearLayout myouhuijuan;
    @BindView(R.id.pcHelpGetTv)
    TextView mHelpCollectTv;//代收货款
    private int mMinPrice = 900;
    private int mMaxPrice = 1000;
    private int mInsurancePrice;//保险价格
    private int mFull, mReduce;//满 减
    private int mFreightPrice;//运费价格
    private double mMindon = 0;
    private double mMaxdon= 1000;
    /**
     * 弹出款
     **/
    @BindView(R.id.pcPopFreightTv)
    TextView mPopFreightTv;//运费
    @BindView(R.id.pcPopInsuranceTv)
    TextView mPopInsuranceTv;//保费
    @BindView(R.id.pcPopCouponTv)
    TextView mPopCouponTv;//优惠券

    @BindView(R.id.pcRemarkTv)
    TextView mRemarkTv;//备注
    private RemarkDialog mRemarkDialog;

    private DateFormat mDateFormat;
    private ProgressDialog mPostDialog;

    // protected Account mAccount;




    @Override
    protected PublishConfirmPresenter initPresenter() {
        return new PublishConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publishconfirm_activity;
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        mPublishBean = (PublishBean) getIntent().getSerializableExtra("data");
        initData();
  }
  @Override
    protected void handleEvents() {
        mFreightEt.setRegion(mMaxPrice, mMinPrice);
        mFreightEt.setTextWatcher(new RegionNumberEditText.OnInputListener() {
            @Override
            public void onInput(int value) {
                mFreightPrice = value;
                if (mFreightPrice < mFull) {
                    mFull = 0;
                    mReduce = 0;
                    mCouponTv.setText("");
                }

                calculateTotalPrice();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        if ("0".equals(mAccount.getIscoupon()))
        { }
        else
        {
            myouhuijuan.setVisibility(View.GONE);
        }
        mProductNameTv.setText("货物：" + mPublishBean.productName);
        mStartPosTv.setText(mPublishBean.getStartPos());
        mEndPosTv.setText(mPublishBean.getEndPos());
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_SHORT.getValue()) {
            mTruckTv.setText("车型：" + mPublishBean.truck.getTitle());
        } else {
            mTruckTv.setVisibility(View.GONE);
        }
        mAreaTv.setText("体积：" + mPublishBean.getArea() + "方");
        int price = (int) mPublishBean.getPrice();
        mPriceTv.setText("价格：" + price + "元");
        mWeightTv.setText("重量：" + mPublishBean.getWeight() + "吨");

        mDistanceTv.setText("公里：" + mPublishBean.getDistance() + "公里");
        // mNumTv.setText("数量：" + mPublishBean.getNum() + "件");

        mRecommendPriceTv.setText(String.valueOf(price));
        mMinPrice = (int) (price * 0.9);
        mMaxPrice = (int) (price * 1.1);
        mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");

//        mHelpPayTv.setText(mPublishBean.consignee.getName());
//        mHelpCollectTv.setText(mPublishBean.consignee.getName());

        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_SHORT.getValue()) {
            //同城
            mRecommendView.setVisibility(View.VISIBLE);

            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));
            mFreightEt.setEnabled(true);

            String qin="";
            if(("0").equals(mAccount.getIscoupon()))
            {
                qin=String.valueOf(price*0.95);
            }
            else
            {
                qin=String.valueOf(price);
            }
            mTotalPriceTv.setText(qin);
        }
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche.getValue()) {
            //同城专车
            mRecommendView.setVisibility(View.VISIBLE);
            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));
            mFreightEt.setEnabled(true);

            String yan="";
            if(("0").equals(mAccount.getIscoupon()))
            {
                yan=String.valueOf(price*0.95);
            }
            else
            {
                yan=String.valueOf(price);
            }
            mTotalPriceTv.setText(yan);

        }
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue()) {
            mBySelfLayout.setVisibility(View.VISIBLE);
        }
    }
    @OnClick({R.id.pcPriceDetailLayout, R.id.pcPopLayout,
            R.id.pcInsuranceTv, R.id.pcReceiptCb, R.id.pcHelpPayCb,
            R.id.pcHelpGetCb, R.id.pcCouponTv, R.id.pcHelpGetLayout,
            R.id.pcHelpPayLayout, R.id.pcRemarkTv
    })
    public void onClick(View v) {
        if (v.getId() == R.id.pcPriceDetailLayout) {
            if (mPopView.isShown()) {
                mPopView.setVisibility(View.GONE);
                mArrowIv.setImageResource(R.drawable.publish_ic_arrow_down);
            } else {
                mPopFreightTv.setText(String.valueOf(mFreightPrice));
                mPopInsuranceTv.setText(String.valueOf(mInsurancePrice));
                //首次注册在这里显示95折优惠卷，这个是优惠的金额,优惠卷只限与同城配送订单中
                if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche.getValue()||mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_SHORT.getValue())
                {
                    String wei="";
                    if(("0").equals(mAccount.getIscoupon()))
                   {
                       wei=String.valueOf(-(mFreightPrice-(mFreightPrice*0.95)));
                   }
                   else
                   {
                       wei=String.valueOf(-(mFreightPrice-(mFreightPrice)));
                   }
                    mPopCouponTv.setText(wei.substring(0, wei.indexOf(".")));
                }
                else
                {
                    mPopCouponTv.setText(String.valueOf(-mReduce));
                }
                mPopView.setVisibility(View.VISIBLE);
                mArrowIv.setImageResource(R.drawable.publish_ic_arrow_up);
            }
        } else if (v.getId() == R.id.pcPopLayout) {
            mPopView.setVisibility(View.GONE);
            mArrowIv.setImageResource(R.drawable.publish_ic_arrow_down);
        } else if (v.getId() == R.id.pcOkBtn) {//确认提交
            startActivity(new Intent(this, OrderConfirmActivity.class));
        } else if (v.getId() == R.id.pcInsuranceTv) {//保险
            InsuranceDialog dialog = new InsuranceDialog();
            dialog.setOnInsuranceItemClickListener(new InsuranceDialog.OnInsuranceItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(Insurance insurance, int position) {
                    mInsuranceTv.setText(insurance.title + "(" + insurance.price + "元)");
                    mInsurancePrice = Integer.parseInt(insurance.price);
                    if (mInsurancePrice == 0) {
                        mPublishBean.insurance = "0";
                    } else {
                        mPublishBean.insurance = String.valueOf(position);
                    }

                    calculateTotalPrice();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        } else if (v.getId() == R.id.pcReceiptCb) {//发票
            if (mReceiptCb.isChecked()) {
                mReceiptView.setVisibility(View.VISIBLE);
            } else {
                mReceiptView.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.pcHelpPayCb) {//代付
            if (mHelpPayCb.isChecked()) {
                mHelpPayView.setVisibility(View.VISIBLE);
            } else {
                mHelpPayView.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.pcHelpGetCb) {//代取
            if (mHelpGetCb.isChecked()) {
                mHelpGetView.setVisibility(View.VISIBLE);
            } else {
                mHelpGetView.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.pcCouponTv) {//优惠券

            if (mFreightPrice == 0) {
                showToast("请先输入运费价格");
                return;
            }

            CouponDialog dialog = CouponDialog.newInstance(mFreightPrice);
            dialog.setOnCouponItemClickListener(new CouponDialog.OnCouponItemClickListener() {
                @Override
                public void onItemClick(Coupon coupon, int position) {
                    mFull = Integer.parseInt(coupon.full);
                    mReduce = Integer.parseInt(coupon.reduce);
                    mCouponTv.setText(getString(R.string.publish_confirm_fullreduce, coupon.full, coupon.reduce));
                    mPublishBean.coupon = String.valueOf(position);
                    calculateTotalPrice();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        } else if (v.getId() == R.id.pcHelpGetLayout) {//代收
            Intent intent = new Intent(this, FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET);
        } else if (v.getId() == R.id.pcHelpPayLayout) {//代付
            Intent intent = new Intent(this, FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_PAY);
        } else if (v.getId() == R.id.pcRemarkTv) {//备注
            if (mRemarkDialog == null) {
                mRemarkDialog = new RemarkDialog();
                mRemarkDialog.setOnRemarkConfirmListener(new RemarkDialog.OnRemarkConfirmListener() {
                    @Override
                    public void onRemarkConfirm(String content) {
                        mRemarkTv.setText(content);

                        //设置备注
                        mPublishBean.remark = content;
                    }
                });
            }
            mRemarkDialog.show(Objects.requireNonNull(getSupportFragmentManager()), "remark");
        }
    }

    @OnClick(R.id.pcOkBtn)
    public void postOrder() {
        if (mFreightPrice == 0) {
            showToast("请先输入运费价格");
            return;
        }
        mPublishBean.freight = String.valueOf(mFreightPrice);
        mPublishBean.isReceipt = mReceiptCb.isChecked() ? "1" : "0";
        if (mReceiptCb.isChecked()) {//是否要发票
            String taxNum = mTaxNumEt.getText().toString();
            if (TextUtils.isEmpty(taxNum)) {
                showToast("请输入税号");
                return;
            }
            String unitName = mUnitNameEt.getText().toString();
            if (TextUtils.isEmpty(unitName)) {
                showToast("请输入单位名称");
                return;
            }
            String email = mEmailEt.getText().toString();
            mPublishBean.taxNum = taxNum;
            mPublishBean.unitName = unitName;
            mPublishBean.email = email;
        }

        if (mHelpGetCb.isChecked()) {//是否要代收货款
            String getPrice = mHelpGetPriceEt.getText().toString();
            if (TextUtils.isEmpty(getPrice)) {
                showToast("请输入代收货款");
                return;
            }
            mPublishBean.getPrice = getPrice;
        }

        if (mHelpPayCb.isChecked() && TextUtils.isEmpty(mPublishBean.helpPayId)) {
            showToast("请选择代付人");
            return;
        }
        mPublishBean.isHelpPay = mHelpPayCb.isChecked() ? "1" : "0";
//        mPublishBean.helpPayId = mPublishBean.consignee.getId();

        if (mHelpGetCb.isChecked() && TextUtils.isEmpty(mPublishBean.helpGetId)) {
            showToast("请选择代收人");
            return;
        }
        mPublishBean.isHelpGet = mHelpGetCb.isChecked() ? "1" : "0";
//        mPublishBean.helpGetId = mPublishBean.consignee.getId();

        mPublishBean.finalPrice = mTotalPriceTv.getText().toString();
        mPublishBean.date = mDateFormat.format(new Date());

        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue()) {
            mPublishBean.isSendBySelf = mSendBySelfCb.isChecked() ? "1" : "0";
            mPublishBean.isPickBySelf = mPickBySelfCb.isChecked() ? "1" : "0";
        }

        mPostDialog = LoadingProgress.showProgress(this, "正在提交...");

        mPresenter.postOrder(mPublishBean.imagePaths, mPublishBean.getParams());

        if (mHelpPayCb.isChecked()) {
            mPresenter.postOrder1(mPublishBean.imagePaths, mPublishBean.getParams());
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateTotalPrice() {
        int totalPrice = mFreightPrice + mInsurancePrice - mReduce;
        mTotalPriceTv.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onRefreshSuccess(BaseEntity<PostOrder> entity) {

        mPostDialog.dismiss();

        Intent intent = new Intent(this, OrderConfirmActivity.class);
        intent.putExtra("publishBean", mPublishBean);
        intent.putExtra("postOrder", entity.getData());

        startActivity(intent);

        EventBus.getDefault().post(new PublishClearCacheEvent());

        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mPostDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RESULT_FRIEND_PAY) {//代付
            Friend friend = (Friend) data.getSerializableExtra("data");
            if (!friend.isRegister()) {
                showToast("好友未注册，暂无法提供代付服务");
                return;
            }
            mHelpPayTv.setText(friend.getName());
            mPublishBean.helpPayId = friend.getId();
        } else if (requestCode == RESULT_FRIEND_GET) {//代收
            Friend friend = (Friend) data.getSerializableExtra("data");
            if (!friend.isRegister()) {
                showToast("好友未注册，暂无法提供代收服务");
                return;
            }
            mHelpCollectTv.setText(friend.getName());
            mPublishBean.helpGetId = friend.getId();
        }
    }
}
