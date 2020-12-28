package com.xrwl.driver.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.utils.statusbar.Eyes;
import com.ldw.library.view.RetryView;
import com.ldw.library.view.TitleView;
import com.xrwl.driver.R;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.utils.AccountUtil;
import com.xrwl.driver.utils.ActivityCollect;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by www.longdw.com on 2017/10/28 上午9:58.
 */

public abstract class BaseActivity<V extends BaseMVP.IBaseView, P extends BaseMVP.BasePresenter<V>> extends AppCompatActivity {

    /*
     * 解决Vector兼容性问题
     * 这里是涉及金额 应该 在本金的 基础去先减少20  再去判断  后面的逻辑
     * First up, this functionality was originally released in 23.2.0,
     * but then we found some memory usage and Configuration updating
     * issues so we it removed in 23.3.0. In 23.4.0 (technically a fix
     * release) we’ve re-added the same functionality but behind a flag
     * which you need to manually enable.w
     *
     * http://www.jianshu.com/p/e3614e7abc03
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public String TAG;
    protected Activity mContext;

    protected P mPresenter;
    protected RetryView mRetryView;
    private Unbinder mUnBinder;

    protected Account mAccount;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getName();

        mAccount = AccountUtil.getAccount(this);

        //初始化Presenter
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }

        setContentView(getLayoutId());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;

        initTitleView();
        initRetryView();
        initViews();
        initViews(savedInstanceState);

        handleEvents();

        ActivityCollect.getAppCollect().addActivity(this);
    }

    public void setTitleText(String text) {
        TextView titleTv = findViewById(R.id.baseTitleTv);
        if (titleTv != null) {
            titleTv.setText(text);
        }
    }

    public void setTitleBgColor(String color) {
        View titleView  = findViewById(R.id.baseTitleView);
        if (titleView != null) {
            titleView.setBackgroundColor(Color.parseColor(color));
        }
    }
    protected void initTitleView() {
        TitleView titleView = findViewById(R.id.baseTitleView);
        if (titleView != null) {
            titleView.setOnTitleViewListener(new TitleView.TitleViewListener() {
                @Override
                public void onBack() {
                    finish();
                }
                @Override
                public void onWebExit() {
                    finish();
                }
            });
            String title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                titleView.setTitle(title);
            }
        }
    }

    private void initRetryView() {
        mRetryView = findViewById(R.id.baseRetryView);
        if (mRetryView != null) {
            mRetryView.setOnRetryListener(new RetryView.OnRetryListener() {
                @Override
                public void onRetry() {
                    getData();
                }
            });
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showNetworkError() {
        showToast(getString(R.string.toast_network_exception));
    }

    @Override
    protected void onResume() {
        if (mPresenter != null) {
            mPresenter.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        ActivityCollect.getAppCollect().finishActivity(this);
    }

    public void handleError(BaseEntity entity) {
        if (entity == null) {
            showToast(getString(R.string.toast_network_exception));
            return;
        }
        if (entity.isTokenInvalid()) {//授权过期
//            logout();
//            EventBus.getDefault().post(new LogoutEvent());
        } else {
            if (TextUtils.isEmpty(entity.getMsg())) {
                showToast(getString(R.string.toast_network_exception));
            } else {
                new AlertDialog.Builder(mContext)
                        .setMessage(entity.getMsg())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                //showToast(entity.getMsg());
            }
        }
    }

    protected void showLoading() {
        if (mRetryView != null) {
            mRetryView.showLoading();
            getData();
        }
    }

    protected void showContent() {
        if (mRetryView != null) {
            mRetryView.showContent();
        }
    }

    protected void showNoData() {
        if (mRetryView != null) {
            mRetryView.showNoData();
        }
    }

    protected void showError() {
        if (mRetryView != null) {
            mRetryView.showError();
        }
    }

    protected void initBaseRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    protected void setLightBar() {
        Eyes.setStatusBarLightMode(this, Color.WHITE);
    }

    protected void initViews(Bundle savedInstanceState) { }

    protected abstract P initPresenter();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    /** 处理UI事件 */
    protected void handleEvents() {}

    /** 从网络获数据 */
    protected void getData() {}
}
