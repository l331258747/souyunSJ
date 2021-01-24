package com.xrwl.driver.module.home.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.library.banner.BannerLayout;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.utils.Utils;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseFragment;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.HomeItem;
import com.xrwl.driver.event.TabCheckEvent;
import com.xrwl.driver.module.account.activity.LoadingWebActivity;
import com.xrwl.driver.module.account.activity.SingleNumberQueryActivity;
import com.xrwl.driver.module.account.activity.WebActivity;
import com.xrwl.driver.module.home.adapter.HomeAdAdapter;
import com.xrwl.driver.module.home.adapter.HomeRecyclerAdapter;
import com.xrwl.driver.module.home.adapter.HomesAdAdapter;
import com.xrwl.driver.module.home.adapter.WebBannerAdapter;
import com.xrwl.driver.module.order.driver.ui.DriverOrderActivity;
import com.xrwl.driver.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.driver.module.order.owner.ui.QianDaoActivity;
import com.xrwl.driver.module.order.owner.ui.ZhouGongActivity;
import com.xrwl.driver.module.publish.ui.NewsActivity;
import com.xrwl.driver.module.tab.activity.TabActivity;
import com.xrwl.driver.retrofit.BaseSimpleObserver;
import com.xrwl.driver.retrofit.RxSchedulers;
import com.xrwl.driver.utils.AccountUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/3/25 下午10:58.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.homeIntroRv)
    RecyclerView mHomeIntroRv;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager;
    @BindView(R.id.homeServiceRv)
    RecyclerView mHomeServiceRv;
    @BindView(R.id.homesViewPager)
    ViewPager mViewsPager;
    @BindView(R.id.homePublishOrFindTv)
    TextView mPublishOrFindTv;
    @BindView(R.id.recycler)
    BannerLayout mrecycler;
    @BindView(R.id.ll_order)
    LinearLayout ll_order;
    private Account mAccount;
    private Disposable mDisposable;
    private HomeAdAdapter mHomeAdAdapter;

    private HomesAdAdapter mHomesAdAdapter;
    @BindView(R.id.saoyisaoBtn)
    LinearLayout msaoyisaoBtn;


    public static HomeFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView(View view) {
        mHomeIntroRv.setNestedScrollingEnabled(false);
        mHomeServiceRv.setNestedScrollingEnabled(false);

        mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mAccount = AccountUtil.getAccount(mContext);
        if (mAccount.isDriver()) {
            mPublishOrFindTv.setText("接货");
        }

        try {
            JSONObject jsonObject = new JSONObject(Utils.getAssetsString(mContext, "home.json"));
            final HomeItem homeItem = HomeItem.parseJson(jsonObject);

            HomeRecyclerAdapter introAdapter = new HomeRecyclerAdapter(mContext, R.layout.home_recycler_item, homeItem.getData1());
            mHomeIntroRv.setAdapter(introAdapter);
            introAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    HomeItem hi = homeItem.getData1().get(position);
                    if (hi.getUrl().equals("1")) {
                        startActivity(new Intent(mContext, NearLocationActivity.class));
                    } else if (hi.getUrl().equals("2")) {
                        EventBus.getDefault().post(new TabCheckEvent());
                        //startActivity(new Intent(mContext, FindFragment.class));
                    } else if (hi.getUrl().equals("3")) {
                        startActivity(new Intent(mContext, CalendarView.class));
                    } else if (hi.getUrl().equals("5")) {
                        startActivity(new Intent(mContext, ZhouGongActivity.class));
                    } else if (hi.getUrl().equals("14")) {
                        //头条新闻
                        startActivity(new Intent(mContext, NewsActivity.class));
                    } else if (hi.getUrl().equals("12")) {
                        startActivity(new Intent(mContext, SingleNumberQueryActivity.class));

                    } else if (hi.getUrl().equals("11")) {
                        showToast("敬请期待...");
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("title", hi.getTitle());
                        intent.putExtra("url", hi.getUrl());
                        intent.setClass(mContext, WebActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mHomeAdAdapter = new HomeAdAdapter(mContext, mViewPager, homeItem.getAd(), new HomeAdAdapter.OnHomeAdListener() {
                @Override
                public void onHomeAddClick(int position) {

                    if (position == 0) {//实名认证
                        if (mAccount.isOwner()) {
                            startActivity(new Intent(mContext, OwnerAuthActivity.class));
                        } else {
                            startActivity(new Intent(mContext, DriverAuthActivity.class));
                        }
                    } else if (position == 1) {

                    } else if (position == 2) {
                        //startActivity(new Intent(mContext, MainActivity.class));
                        //7日签到
                        startActivity(new Intent(mContext, QianDaoActivity.class));
                    } else if (position == 3) {
                        startActivity(new Intent(mContext, LoadingWebActivity.class));
//                        Uri uri = Uri.parse("http://xrygh.16souyun.cn/website/index.aspx");
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
                    } else if (position == 4) {
                        Uri uri = Uri.parse("https://mp.weixin.qq.com/s?__biz=MzA3ODExNzcyNw==&mid=2649690837&idx=1&sn=e43f89827fef1c4a213e6036b27d5c5f&chksm=875c1ce5b02b95f3907221a18d2bdbdc05e8696491a7a653baee45bd442c153765455a2fc3bb&mpshare=1&scene=1&srcid=0916aH7HI8CkyWgpDEmljYyU&pass_ticket=7UOXlojkwlQb%2Foh2Ujuxo%2BBnvd39xzAOvN8hSwAtkqn9ezATmXWL2Xg9RxA3Vts3#rd");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });
            mViewPager.setAdapter(mHomeAdAdapter);


            //开启定时器
            loopAd();

            BannerLayout recyclerBanner = mrecycler;

            List<String> list = new ArrayList<>();
            list.add("http://www.16souyun.com/applunbo/tianqi.png");
            list.add("http://www.16souyun.com/applunbo/dangjiandj.png");
            list.add("http://www.16souyun.com/applunbo/gonghui.png");

            WebBannerAdapter webBannerAdapter = new WebBannerAdapter(mContext, list);
            webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {
                        Intent intent = new Intent();
                        intent.putExtra("title", "天气预报");
                        intent.putExtra("url", "http://www.help.bj.cn/weather/1d.html?id=101100701");
                        intent.setClass(mContext, WebActivity.class);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent();
                        intent.putExtra("title", "云党建");
//                        intent.putExtra("url", "http://ydj.16souyun.com/website/index.aspx");
                        intent.putExtra("url", "http://16xrydj.16souyun.com:810");
                        intent.setClass(mContext, WebActivity.class);
                        startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent();
                        intent.putExtra("title", "云工会");
                        intent.putExtra("jiekou", "1");
//                        intent.putExtra("url", "http://ygh.16souyun.com/website/index.aspx");
                        intent.putExtra("url", "http://16xrygh.16souyun.com:810");
                        intent.setClass(mContext, LoadingWebActivity.class);
                        startActivity(intent);
                    }

                }
            });
            recyclerBanner.setAdapter(webBannerAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环播放广告
     */
    private void loopAd() {
        final int totalSize = mHomeAdAdapter.getCount();
        Observable.interval(3000, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>compose())
                .subscribe(new BaseSimpleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    protected void onHandleSuccess(Long entity) {
                        if (mViewPager.getCurrentItem() == totalSize - 1) {
                            mViewPager.setCurrentItem(0);
                        } else {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                        }
                    }


                    @Override
                    protected void onHandleError(Throwable e) {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroyView();
    }

    @OnClick({R.id.homePublishLayout, R.id.homeMyOrderLayout, R.id.homeHistoryLayout, R.id.homeAuthLayout, R.id.saoyisaoBtn, R.id.ll_order})
    public void onClick(View v) {
        if (v.getId() == R.id.homePublishLayout) {

            EventBus.getDefault().post(new TabCheckEvent());

        } else if (v.getId() == R.id.homeMyOrderLayout) {

            if ("0".equals(mAccount.getType())) {
                startActivity(new Intent(mContext, OwnerOrderActivity.class));
            } else {
                startActivity(new Intent(mContext, DriverOrderActivity.class));
            }

        } else if (v.getId() == R.id.homeHistoryLayout) {
            startActivity(new Intent(mContext, HistoryActivity.class));
        } else if (v.getId() == R.id.homeAuthLayout) {
            if (mAccount.isOwner()) {
                startActivity(new Intent(mContext, OwnerAuthActivity.class));
            } else {
                startActivity(new Intent(mContext, DriverAuthActivity.class));
            }
        } else if (v.getId() == R.id.saoyisaoBtn) {
            startActivity(new Intent(mContext, saoyisaoActivity.class));
        } else if (v.getId() == R.id.ll_order) {
            ((TabActivity)getActivity()).setCurrent(2);
        }
    }
}
