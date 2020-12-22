package com.xrwl.driver.module.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseEventFragment;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.event.BusinessListRefreshEvent;
import com.xrwl.driver.event.BusinessTabCountEvent;
import com.xrwl.driver.module.business.adapter.BusinessAdapter;
import com.xrwl.driver.module.business.adapter.BusinessqianbaoAdapter;
import com.xrwl.driver.module.business.adapter.BusinessxitongAdapter;
import com.xrwl.driver.module.business.mvp.BusinessContract;
import com.xrwl.driver.module.business.mvp.BusinessPresenter;
import com.xrwl.driver.module.order.owner.ui.OwnerOrderActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/25 下午11:33.
 */

public class BusinessFragment extends BaseEventFragment<BusinessContract.IView, BusinessPresenter> implements BusinessContract.IView {

    @BindView(R.id.businessRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private BusinessAdapter mAdapter;
    private BusinessqianbaoAdapter mAdapterqianbao;
    private BusinessxitongAdapter mAdapterxitong;
    @BindView(R.id.xitonggengxinIv)
    ImageView mxitonggengxinIv;
    @BindView(R.id.dingdanIv)
    ImageView mdingdanIv;
    @BindView(R.id.qianbaoIv)
    ImageView mqianbaoIv;
    public String canshu="1";

//    @BindView(R.id.businessItemTimeTv)
//    TextView mbusinessItemTimeTv;
//    @BindView(R.id.businessItemTitleTv)
//    TextView mbusinessItemTitleTv;
//
//    @BindView(R.id.businessItemTimeshijianTv)
//    TextView mbusinessItemTimeshijianTv;
//    @BindView(R.id.businessItemTimejiageTv)
//    TextView mbusinessItemTimejiageTv;
//    @BindView(R.id.businessItemTimeTv)
//    TextView mbusinessItemTimeTv;
//    @BindView(R.id.businessItemTimeTv)
//    TextView mbusinessItemTimeTv;




    public static BusinessFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        BusinessFragment fragment = new BusinessFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @OnClick({R.id.xitonggengxinIv,R.id.dingdanIv,R.id.qianbaoIv})
    public void onClick(View v) {
        int id = v.getId();
        /**
         * classify=1 订单消息
         * classify=2 钱包消息
         * classify=3 系统更新*/
        Business business=new Business();
        if (id == R.id.xitonggengxinIv) {
            canshu="3";
            business.systemcanshu="3";
            mPresenter.requestData("3");
        }
        else if (id == R.id.dingdanIv)
        {
            canshu="1";
            business.systemcanshu="1";
            mPresenter.requestData("1");
        }
        else if (id == R.id.qianbaoIv)
        {
            canshu="2";
            business.systemcanshu="2";

            mPresenter.requestData("2");
        }
        else
        {
            canshu="1";
            business.systemcanshu="1";
            mPresenter.requestData("1");
        }
    }

    @Override
    protected BusinessPresenter initPresenter() {
        return new BusinessPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.business_fragment;
    }

    @Override
    protected void initView(View view) {
        initBaseRv(mRv);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestData(canshu);

            }
        });
        mPresenter.requestData(canshu);
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Business>> entity) {

        if (entity.getData() == null) {
            return;
        }

        EventBus.getDefault().post(new BusinessTabCountEvent(entity.getCount()));

        mRefreshLayout.setRefreshing(false);
        if (entity.getData().size() == 0) {
            showNoData();
            return;
        }
        if (mAdapter == null) {
            if(canshu.equals("1")) {
                mAdapter = new BusinessAdapter(mContext, R.layout.business_recycler_item, entity.getData());
            }
            else if(canshu.equals("2"))
            {
                mAdapter = new BusinessAdapter(mContext, R.layout.business_recycler_itemqianbao, entity.getData());
            }
            else if(canshu.equals("3"))
            {
                mAdapter = new BusinessAdapter(mContext, R.layout.business_recycler_itemxitong, entity.getData());
            }
            mRv.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(entity.getData());
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Business business = mAdapter.getDatas().get(position);
                Intent intent = new Intent(mContext, BusinessDetailActivity.class);

                if(canshu.equals("1"))
                {

                    intent.putExtra("product_name", business.product_name);
                    intent.putExtra("mget_person", business.get_person);
                    intent.putExtra("mget_phone", business.get_phone);

                    intent.putExtra("mpost_person", business.publish_person);
                    intent.putExtra("mpost_phone", business.publish_phone);


                    intent.putExtra("mstart_area", business.start_area);
                    intent.putExtra("mend_area", business.end_area);
                    intent.putExtra("mstart_desc", business.start_desc);
                    intent.putExtra("mend_desc", business.end_desc);
                    intent.putExtra("mend_province", business.end_province);
                    intent.putExtra("mstart_province", business.start_province);
                    intent.putExtra("mend_city", business.end_city);
                    intent.putExtra("mstart_city", business.start_city);
                    intent.putExtra("mtotal_price", business.total_price);
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("id", business.id);
                    intent.putExtra("pic", business.pic);


                    intent.putExtra("canshu", "1");
                    startActivity(intent);
                }
                else if(canshu.equals("2"))
                {

                    intent.putExtra("product_name", business.product_name);
                    intent.putExtra("mget_person", business.get_person);
                    intent.putExtra("mget_phone", business.get_phone);
                    intent.putExtra("mstart_area", business.start_area);
                    intent.putExtra("mend_area", business.end_area);
                    intent.putExtra("mstart_desc", business.start_desc);
                    intent.putExtra("mend_desc", business.end_desc);
                    intent.putExtra("mend_province", business.end_province);
                    intent.putExtra("mend_city", business.end_city);
                    intent.putExtra("mtotal_price", business.total_price);
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("id", business.id);
                    intent.putExtra("canshu","2");
                    intent.putExtra("pic", business.pic);
                    startActivity(intent);
                }
                else if(canshu.equals("3"))
                {
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("mcontent", business.content);
                    intent.putExtra("id", business.id);
                    intent.putExtra("pic", business.pic);
                    intent.putExtra("canshu","3");

                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        showContent();


    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BusinessListRefreshEvent event) {

        mPresenter.requestData(canshu);


    }

    @Override
    public void onRefreshError(Throwable e) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(BaseEntity entity) {
        mRefreshLayout.setRefreshing(false);
        handleError(entity);
    }

    @Override
    public void onLoadSuccess(BaseEntity<List<Business>> entity) {

    }

    @Override
    public void onLoadError(Throwable e) {

    }
}
