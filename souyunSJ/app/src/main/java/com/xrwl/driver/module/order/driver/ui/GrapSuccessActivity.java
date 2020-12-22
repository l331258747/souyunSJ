package com.xrwl.driver.module.order.driver.ui;

import android.content.Intent;
import android.media.MediaPlayer;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;

import butterknife.OnClick;

/**
 * 抢单成功
 * Created by www.longdw.com on 2018/4/3 下午10:39.
 */

public class GrapSuccessActivity extends BaseActivity {

    protected String mid;
    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grapsuccess_activity;
    }

    @Override
    protected void initViews() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.gongxininqiangdanchenggong);
        mediaPlayer.start();




        mid = getIntent().getStringExtra("mid");



    }

    @OnClick(R.id.psOkBtn)
    public void onClick() {



        Intent intent = new Intent(this,DriverOrderDetailActivity.class);
        intent.putExtra("id",mid);
        startActivity(intent);
    }
}
