package com.xrwl.driver.module.me.ui;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseEventActivity;
import com.xrwl.driver.event.BankListRefreshEvent;
import com.xrwl.driver.module.me.adapter.BankAdapter;
import com.xrwl.driver.module.me.bean.Bank;
import com.xrwl.driver.module.me.mvp.BankContract;
import com.xrwl.driver.module.me.mvp.BankPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡
 * Created by www.longdw.com on 2018/4/5 上午12:11.
 */
public class BankActivity extends BaseEventActivity<BankContract.IView, BankPresenter> implements BankContract.IView {

    @BindView(R.id.bankRv)
    RecyclerView mRv;
    @BindView(R.id.totalPriceTv)
    TextView mPriceTv;
    @BindView(R.id.addTixianBtn)
    Button mTixianBtn;

    @BindView(R.id.tianjiall)
    LinearLayout mtianjia;

    private BankAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private ProgressDialog mTixianDialog;

    @Override
    protected BankPresenter initPresenter() {
        return new BankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bank_activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BankListRefreshEvent event) {
        getData();
    }

        @OnClick(R.id.tianjiall)
        public void tixian() {
            startActivity(new Intent(mContext, AddBankActivity.class));
        }
    @Override
    protected void getData() {
        mPresenter.getBankList();
        mPresenter.getTotalPrice();
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new BankAdapter(this, R.layout.bank_recycler_item, new ArrayList<Bank>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);


        mRv.setAdapter(mWrapper);

        getData();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Bank>> entity) {
        mAdapter.setDatas(entity.getData());
        mWrapper.notifyDataSetChanged();
    }

    @Override
    public void onRefreshError(Throwable e) {
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
    }

    @Override
    public void onTotalPriceSuccess(String price) {
        mPriceTv.setText(price);
        mTixianBtn.setEnabled(true);
    }

    @Override
    public void onTixianSuccess() {
        mTixianDialog.dismiss();
        mPriceTv.setText("0");
        showToast("提现成功");
    }

    @Override
    public void onTixianError(BaseEntity entity) {
        mTixianDialog.dismiss();
        showToast("提现失败");
        handleError(entity);
    }

    @Override
    public void onTixianException(Throwable e) {
        mTixianDialog.dismiss();
        showNetworkError();
    }
}
