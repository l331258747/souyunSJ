package com.xrwl.driver.module.me.ui;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.event.BankListRefreshEvent;
import com.xrwl.driver.module.me.mvp.AddBankPresenter;
import com.xrwl.driver.module.me.mvp.BankContract;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 面对面建群订单
 * Created by www.longdw.com on 2018/4/5 上午12:24.
 */
public class SiLiaoOrderActivity extends BaseActivity<BankContract.IAddView, AddBankPresenter> implements BankContract.IAddView {

    @BindView(R.id.addBankNumEt)
    EditText mNumEt;//厂家密钥
    @BindView(R.id.drivertelEt)
    EditText mdrivertelEt;//厂家密钥
    private ProgressDialog mProgressDialog;

    @Override
    protected AddBankPresenter initPresenter() {
        return new AddBankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.addjianqun_activity;
    }

    @Override
    protected void initViews() {
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
      //  mProgressDialog.dismiss();
        EventBus.getDefault().post(new BankListRefreshEvent());
        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        //mProgressDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        //mProgressDialog.dismiss();
        handleError(entity);
    }

    @OnClick(R.id.addBankConfirmBtn)
    public void confirm() {
        String num = mNumEt.getText().toString();
        if (TextUtils.isEmpty(num) || num.length() < 6) {
            showToast("请输入正确的密钥");
            return;
        }
        if (TextUtils.isEmpty(mdrivertelEt.getText().toString()) || mdrivertelEt.getText().toString().length() <11) {
            showToast("请输入正确的手机号");
            return;
        }
        mProgressDialog = LoadingProgress.showProgress(this, "正在接活...");

        Map<String, String> params = new HashMap<>();
        params.put("id", num);
        params.put("userid", mAccount.getId());


        mPresenter.addBank(params);
    }
}
