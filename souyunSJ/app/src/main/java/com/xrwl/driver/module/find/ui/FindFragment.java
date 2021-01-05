package com.xrwl.driver.module.find.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.LoadMoreWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.CustomLoadMoreView;
import com.ldw.library.view.TitleView;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseEventFragment;
import com.xrwl.driver.bean.Address;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.ProductSearch;
import com.xrwl.driver.event.DriverListRrefreshEvent;
import com.xrwl.driver.module.find.adapter.FindAdapter;
import com.xrwl.driver.module.find.dialog.ChooseAddressDialog;
import com.xrwl.driver.module.find.dialog.ProductSearchDialog;
import com.xrwl.driver.module.find.mvp.FindContract;
import com.xrwl.driver.module.find.mvp.FindPresenter;
import com.xrwl.driver.module.order.driver.ui.DriverOrderDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 司机找货
 * Created by www.longdw.com on 2018/4/5 上午9:17.
 */
public class FindFragment extends BaseEventFragment<FindContract.IView, FindPresenter>
        implements FindContract.IView {
//public class FindFragment extends BaseEventFragment<FindContract.IView, FindPresenter> implements ChooseAddressDialog
//        .OnSelectListener, FindContract.IView, ProductSearchDialog.OnProductSearchListener {

    @BindView(R.id.baseRv)
    RecyclerView mRv;

    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;

    @BindView(R.id.findStartTv)
    TextView mStartTv;
    @BindView(R.id.findStartIv)
    ImageView mStartIv;
    @BindView(R.id.findEndTv)
    TextView mEndTv;
    @BindView(R.id.findEndIv)
    ImageView mEndIv;

    @BindView(R.id.addBankNumEt)
    EditText mNumEt;//厂家密钥
    @BindView(R.id.drivertelEt)
    EditText mdrivertelEt;//厂家密钥
//    Map<String, String> params = new HashMap<>();
    @BindView(R.id.putong)
    LinearLayout mputong;
    private CityPicker mCP;
    @BindView(R.id.miyaodingdan)
    View mmiyaodingdan;
    private List<Address> mSelectedEndAddressList;
    private ChooseAddressDialog mStartAddressDialog;
    private ChooseAddressDialog mEndAddressDialog;
    private ProductSearchDialog mProductSearchDialog;
    private FindAdapter mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private ProductSearch mProductSearch;
    private static String morendequ;

    //字段
    String type = "0";
    String locationStart = "全国";
    String locationEnd = "全国";
    String locationStartSheng = "";
    String locationStartShi = "";
    String locationStartXian = "";
    String locationEndSheng = "";
    String locationEndShi = "";
    String locationEndXian = "";

    public static FindFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FindPresenter initPresenter() {
        return new FindPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_fragment;
    }

    @Override
    protected void initView(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS},
                        0);
            }
        }
        initBaseRv(mRv);
        mRefreshLayout.setOnRefreshListener(() -> getData());
        mputong.setVisibility(View.VISIBLE);
        initLocation();
    }

    @OnClick({R.id.findStartTv, R.id.findStartIv, R.id.findEndTv, R.id.findEndIv})
    public void onCityClick(View v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEndTv.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.findStartTv:
            case R.id.findStartIv:
                mYunCityPicher(true);
                mCP.show();
                break;
            case R.id.findEndTv:
            case R.id.findEndIv:
                mYunCityPicher(false);
                mCP.show();
                break;
        }
    }

    @OnClick({R.id.rb_owner, R.id.rb_driver})
    public void onTabClick(View v) {
        switch (v.getId()) {
            case R.id.rb_owner:
                type = "0";
                getData();
                break;
            case R.id.rb_driver:
                type = "6";
                getData();
                break;
        }
    }

    //这是三级联动
    public void mYunCityPicher(boolean isStart) {
        mCP = new CityPicker.Builder(getContext())
                .textSize(20)
                //地址选择
                .title("地址选择")
                .backgroundPop(0xa0000000)
                //文字的颜色
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滑轮文字的颜色
                .textColor(R.color.colorPrimary)
                //省滑轮是否循环显示
                .provinceCyclic(true)
                //市滑轮是否循环显示
                .cityCyclic(false)
                //地区（县）滑轮是否循环显示
                .districtCyclic(false)
                //滑轮显示的item个数
                .visibleItemsCount(7)
                //滑轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();

        //监听
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省
                String province = citySelected[0];
                //市
                String city = citySelected[1];
                //区。县。（两级联动，必须返回空）
                String district = citySelected[2];
                //邮证编码
                String code = citySelected[3];

                if(isStart){
                    locationStart = province + city + district;
                    locationStartSheng = province;
                    locationStartShi = city;
                    locationStartXian = district;
                    mStartTv.setText(locationStart);
                }else{
                    locationEnd = province + city + district;
                    locationEndSheng = province;
                    locationEndShi = city;
                    locationEndXian = district;
                    mEndTv.setText(locationEnd);
                }

                getData();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation.getErrorCode() == 0) {
                String province = aMapLocation.getProvince();
                String city = aMapLocation.getCity();
                String district = aMapLocation.getDistrict();

                locationStart = province + city + district;
                locationStartSheng = province;
                locationStartShi = city;
                locationStartXian = district;
                mStartTv.setText(locationStart);

//                params.put("start", city);
//                String sijicity = null;
//                try {
//                    sijicity = URLEncoder.encode(city, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                morendequ = sijicity;
//                params.put("current_city", sijicity);
            } else {
//                params.put("start", "全国");
                locationStart = "全国";
                mStartTv.setText(locationStart);
            }

            mLocationClient.stopLocation();

            getData();

        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(DriverListRrefreshEvent event) {
        getData();
    }

    @Override
    protected void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("ksaddress", locationStart);
        params.put("jsaddress", locationEnd);

        params.put("kaishisheng", locationStartSheng);
        params.put("kaishishi", locationStartShi);
        params.put("kaishixian", locationStartXian);
        params.put("daodasheng", locationEndSheng);
        params.put("daodashi", locationEndShi);
        params.put("daodaxian", locationEndXian);

        params.put("type", type);
        mPresenter.getData(params);
    }

    private void getMoreData() {
        Map<String, String> params = new HashMap<>();
        params.put("ksaddress", locationStart);
        params.put("jsaddress", locationEnd);

        params.put("kaishisheng", locationStartSheng);
        params.put("kaishishi", locationStartShi);
        params.put("kaishixian", locationStartXian);
        params.put("daodasheng", locationEndSheng);
        params.put("daodashi", locationEndShi);
        params.put("daodaxian", locationEndXian);

        if(!TextUtils.isEmpty(type))
            params.put("type", type);
        mPresenter. getMoreData(params);
    }

//    @OnClick({R.id.findStartLayout, R.id.findEndLayout, R.id.findSearchLayout,R.id.rb_owner,R.id.rb_driver,R.id.addBtn})
//    public void onClick(View v) {
//        if (v.getId() == R.id.findStartLayout) {
//            if (mStartAddressDialog != null) {
//                mStartAddressDialog.dismiss();
//                return;
//            }
//           // mStartAddressDialog = ChooseAddressDialog.newInstance(mTitleView.getHeight() + v.getHeight(), true);
//          //  mStartAddressDialog.setOnSelectListener(this);
//          //  mStartAddressDialog.show(getChildFragmentManager(), "start");
//        } else if (v.getId() == R.id.findEndLayout) {
//            if (mEndAddressDialog != null) {
//                mEndAddressDialog.dismiss();
//                return;
//            }
//            mEndAddressDialog = ChooseAddressDialog.newInstance(mTitleView.getHeight() + v.getHeight(), false);
//            mEndAddressDialog.setOnSelectListener(this);
//            mEndAddressDialog.setCurrentZones(mSelectedEndAddressList);
//            mEndAddressDialog.show(getChildFragmentManager(), "end");
//        } else if(v.getId() == R.id.findSearchLayout) {//高级搜索
//            if (mProductSearchDialog != null) {
//                mProductSearchDialog.dismiss();
//                return;
//            }
//            mProductSearchDialog = ProductSearchDialog.newInstance(mTitleView.getHeight() + v.getHeight(), mProductSearch);
//            mProductSearchDialog.setOnDismissListener(this);
//            mProductSearchDialog.show(getChildFragmentManager(), "search");
//        }
//        else if(v.getId()==R.id.rb_owner)
//        {
//            mputong.setVisibility(View.VISIBLE);
//            mmiyaodingdan.setVisibility(View.INVISIBLE);
//            // startActivity(new Intent(mContext, FindFragment.class));
//
//        }
//        else if(v.getId()==R.id.rb_driver)
//        {
//            mputong.setVisibility(View.INVISIBLE);
//            mmiyaodingdan.setVisibility(View.VISIBLE);
//        }
//        else if(v.getId()==R.id.addBtn)
//        {
//            String num = mNumEt.getText().toString();
//            if (TextUtils.isEmpty(num)) {
//                showToast("请输入正确的密钥");
//                return;
//            }
//            if (TextUtils.isEmpty(mdrivertelEt.getText().toString()) || mdrivertelEt.getText().toString().length() <11) {
//                showToast("请输入正确的手机号");
//                return;
//            }
//            mPresenter.grappwdOrder(num);
//            startActivity(new Intent(mContext, DriverOrderActivity.class));
//        }
//    }
//    /**
//     * 起点单选回调
//     */
//    @Override
//    public void onSingleSelect(Address address) {
//        mStartTv.setText(address.getName());
//
//        String sijicity = null;
//        try {
//            sijicity = URLEncoder.encode(address.getName(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        params.put("start_area", sijicity);
//        getData();
//    }
//    /**
//     * 终点多选回调
//     */
//    @Override
//    public void onMultiSelect(List<Address> datas) {
//        mSelectedEndAddressList = datas;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0, size = datas.size(); i < size; i++) {
//            Address a = datas.get(i);
//            sb.append(a.getName());
//            if (i < size - 1) {
//                sb.append(",");
//            }
//        }
//        mEndTv.setText(sb.toString());
//
//
//        String ends = sb.toString();
//        try {
//            ends = URLEncoder.encode(sb.toString(), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        params.put("end_area", ends);
//
//        getData();
//    }

//    /**
//     * 高级搜索回调
//     */
//    @Override
//    public void onProductSearch(ProductSearch ps) {
//
//        String canshuo = "0";
//        String encodeParme = null;
//        try {
//            encodeParme = URLEncoder.encode(ps.productName, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        params.put("product_name", encodeParme);
//        params.put("short_truck", chexing(ps.shortTrucks));
//
//        params.put("long_truck", ps.longTrucks);
//
//        if ("0".equals(String.valueOf(ps.category + ""))) {
//            canshuo = "0";
//        } else if ("1".equals(String.valueOf(ps.category + ""))) {
//            canshuo = "5";
//        } else if ("2".equals(String.valueOf(ps.category + ""))) {
//            canshuo = "6";
//        } else if ("3".equals(String.valueOf(ps.category + ""))) {
//            canshuo = "1";
//        } else if ("4".equals(String.valueOf(ps.category + ""))) {
//            canshuo = "2";
//        }
//        params.put("pstype", canshuo);
//
//        params.put("start_date", ps.startDate);
//        params.put("end_date", ps.endDate);
//        mProductSearch = ps;
//        getData();
//    }
//
//    protected String leixing(String cs) {
//        String abcd = "";
//        if (cs == "0") {
//            abcd = "0";
//        } else if (cs == "1") {
//            abcd = "5";
//        } else if (cs == "2") {
//            abcd = "6";
//        } else if (cs == "3") {
//            abcd = "1";
//        } else if (cs == "4") {
//            abcd = "2";
//        }
//        return abcd;
//    }
//
//    @Override
//    public void onDialogDismiss() {
//        mStartAddressDialog = null;
//        mEndAddressDialog = null;
//        mProductSearchDialog = null;
//    }

    @Override
    public void onRefreshSuccess(final BaseEntity<List<Order>> entity) {

        mRefreshLayout.setRefreshing(false);
        if (entity.getData().size() == 0) {
            showNoData();
            return;
        } else {
            if (mAdapter == null) {
                mAdapter = new FindAdapter(mContext, R.layout.find_recycler_item, entity.getData());
                mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
                mLoadMoreWrapper.setLoadMoreView(new CustomLoadMoreView());
                mRv.setAdapter(mLoadMoreWrapper);

                mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        Intent intent = new Intent(mContext, DriverOrderDetailActivity.class);
                        intent.putExtra("id", mAdapter.getDatas().get(position).id);
                        startActivity(intent);
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });

                mLoadMoreWrapper.setOnLoadMoreListener(() -> getMoreData());
            } else {
                mAdapter.setDatas(entity.getData());
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            mLoadMoreWrapper.setEnableLoadMore(entity.hasNext());
            showContent();
        }

    }

    @Override
    public void onRefreshError(Throwable e) {
        mRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            showError();
        }
    }

    @Override
    public void onError(BaseEntity entity) {
        mRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            showError();
        }
        handleError(entity);
    }

    @Override
    public void onLoadSuccess(BaseEntity<List<Order>> entity) {

        mAdapter.setDatas(entity.getData());
        mLoadMoreWrapper.notifyDataSetChanged();
        if (!entity.hasNext()) {
            mAdapter.setDatas(entity.getData());
            mLoadMoreWrapper.loadMoreEnd();

        } else {
            mLoadMoreWrapper.loadMoreComplete();
        }
    }

    @Override
    public void onLoadError(Throwable e) {
        mLoadMoreWrapper.loadMoreFail();
    }

    @Override
    public void onDestroyView() {

        if (mLocationClient != null) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    protected String chexing(String che) {
        String xingString = "";
        if ("三轮车".equals(che)) {
            xingString = "2";
        } else if ("小型面包".equals(che)) {
            xingString = "3";
        } else if ("金杯".equals(che)) {
            xingString = "4";
        } else if ("厢货".equals(che)) {
            xingString = "5";
        } else if ("3.8米".equals(che)) {
            xingString = "3";
        } else if ("小型平板".equals(che)) {
            xingString = "6";
        } else if ("大型平板".equals(che)) {
            xingString = "7";
        } else if ("4.2米".equals(che)) {
            xingString = "27";
        } else if ("6.2米".equals(che)) {
            xingString = "28";
        } else if ("6.8米".equals(che)) {
            xingString = "29";
        } else if ("7.2米".equals(che)) {
            xingString = "30";
        } else if ("8.2米".equals(che)) {
            xingString = "31";
        } else if ("9.6米".equals(che)) {
            xingString = "32";
        } else if ("16.5米".equals(che)) {
            xingString = "33";
        } else if ("17.5米".equals(che)) {
            xingString = "34";
        } else if ("13米".equals(che)) {
            xingString = "35";
        } else if ("矿车".equals(che)) {
            xingString = "44";
        } else {
            xingString = "4";
        }
        return xingString;
    }
}
