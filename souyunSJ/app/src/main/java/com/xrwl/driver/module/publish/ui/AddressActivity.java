package com.xrwl.driver.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.Address2;
import com.xrwl.driver.module.publish.adapter.Address2Adapter;
import com.xrwl.driver.module.publish.map.SearchLocationActivity;
import com.xrwl.driver.module.publish.mvp.AddressContract;
import com.xrwl.driver.module.publish.mvp.AddressPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 地址列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class AddressActivity extends BaseActivity<AddressContract.IView, AddressPresenter> implements AddressContract.IView {

    public static final int RESULT_POSITION = 100;

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    private Address2Adapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<Address2> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected AddressPresenter initPresenter() {
        return new AddressPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.address_layout;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new Address2Adapter(this, R.layout.address_recycler_item, new ArrayList<Address2>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.address_add_layout, mRv, false);
        mWrapper.addFootView(footerView);
        mRv.setAdapter(mWrapper);



        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mAdapter.setSelectedPos(position);

                Address2 item = mDatas.get(position);

                Intent intent = new Intent();
                intent.putExtra("title", item.des);

                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {



                return false;
            }
        });

        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String des = data.getStringExtra("title");



        HashMap<String, String> params = new HashMap<>();
        params.put("des", des);
        mPostDialog = LoadingProgress.showProgress(this, "正在添加");
        mPresenter.postData(params);
    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("添加成功");
        mPresenter.getData();
    }

    @Override
    public void onPostError(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("添加失败");
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Address2>> entity) {
        if (entity.getData() != null && entity.getData().size() > 0) {
            mAdapter.setDatas(entity.getData());
            mWrapper.notifyDataSetChanged();
            mDatas = entity.getData();
        }

    }

    @Override
    public void onRefreshError(Throwable e) {
    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
