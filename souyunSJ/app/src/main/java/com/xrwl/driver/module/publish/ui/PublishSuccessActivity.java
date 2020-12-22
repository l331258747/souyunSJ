package com.xrwl.driver.module.publish.ui;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/3 下午10:39.
 */

public class PublishSuccessActivity extends BaseActivity {
    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publishsuccess_activity;
    }

    @Override
    protected void initViews() {
    }

    @OnClick(R.id.psOkBtn)
    public void onClick() {
        finish();
    }
}
