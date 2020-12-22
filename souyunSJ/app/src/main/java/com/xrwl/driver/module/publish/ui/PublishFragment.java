package com.xrwl.driver.module.publish.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseEventFragment;
import com.xrwl.driver.bean.Address;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.event.PublishClearCacheEvent;
import com.xrwl.driver.module.friend.bean.Friend;
import com.xrwl.driver.module.friend.ui.FriendActivity;
import com.xrwl.driver.module.publish.bean.PublishBean;
import com.xrwl.driver.module.publish.bean.Truck;
import com.xrwl.driver.module.publish.dialog.AuthDialog;
import com.xrwl.driver.module.publish.dialog.CategoryDialog;
import com.xrwl.driver.module.publish.dialog.ProductDialog;
import com.xrwl.driver.module.publish.map.SearchLocationActivity;
import com.xrwl.driver.module.publish.mvp.PublishContract;
import com.xrwl.driver.module.publish.mvp.PublishPresenter;
import com.xrwl.driver.module.publish.view.AreaSpinnerView;
import com.xrwl.driver.module.publish.view.PublishProductLongView;
import com.xrwl.driver.view.PhotoScrollView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 发布货源
 * Created by www.longdw.com on 2018/3/25 下午11:43.
 */

public class PublishFragment extends BaseEventFragment<PublishContract.IView, PublishPresenter> implements PublishContract.IView {

    public static final int RESULT_TRUCK = 111;//已选车型
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START_PHONE = 444;//发货电话
    public static final int RESULT_FRIEND_GET_PERSON = 555;//收货人
    public static final int RESULT_FRIEND_GET_PHONE = 666;//收货电话
//    public static final int RESULT_SELECT_START_LOCATION = 777;//选择发货位置
//    public static final int RESULT_SELECT_END_LOCATION = 888;//选择收货位置

    private CategoryDialog.CategoryEnum mCategory;

    @BindView(R.id.publishCategoryTv)
    TextView mCategoryTv;//配送类型

    @BindView(R.id.publishTruckTv)
    TextView mTruckTv;
    @BindView(R.id.publishTruckLayout)
    View mTruckLayout;
    @BindView(R.id.truckLine)
    View mTruckLineLayout;

    @BindView(R.id.publishProductTv)
    TextView mProductTv;

    @BindView(R.id.publishTimeTv)
    TextView mTimeTv;

    @BindView(R.id.publishProductDefaultLayout)
    View mProductDefaultLayout;
    //长途状态下货物重量体积
    @BindView(R.id.publishProductLongTotalLayout)
    PublishProductLongView mProductLongTotalLayout;

    @BindView(R.id.publishAddressDefaultLayout)
    View mAddressDefaultLayout;
    @BindView(R.id.publishAddressDefaultStartLocationTv)
    TextView mDefaultStartLocationTv;
    @BindView(R.id.publishAddressDefaultEndLocationTv)
    TextView mDefaultEndLocationTv;
    private double mDefaultStartLat, mDefaultStartLon;
    private double mDefaultEndLat, mDefaultEndLon;

    private Address mStartCityAddress, mEndCityAddress, mStartPro, mEndPro;

    /**
     * 同城配送状态下的 吨 方  件
     */
    @BindView(R.id.ppDefaultWeightEt)
    EditText mDefaultWeightEt;
    @BindView(R.id.ppDefaultAreaEt)
    EditText mDefaultAreaEt;
    @BindView(R.id.ppDefaultNumEt)
    EditText mDefaultNumEt;


    @BindView(R.id.publishStartPhoneEt)
    EditText mStartPhoneEt;//发货电话
    @BindView(R.id.publishGetPersonEt)
    EditText mGetPersonEt;//收货人
    @BindView(R.id.publishGetPhoneEt)
    EditText mGetPhoneEt;//收货电话

    /**
     * 长途状态下的发货和收货地址
     */
    @BindView(R.id.publishAddressLongTotalLayout)
    View mAddressLongTotalLayout;
    @BindView(R.id.paLongStartSpinnerLayout)
    AreaSpinnerView mStartSpinnerView;
    @BindView(R.id.paLongEndSpinnerLayout)
    AreaSpinnerView mEndSpinnerView;

    @BindView(R.id.photo_scrollview)
    PhotoScrollView mPhotoScrollView;
    private ArrayList<String> mImagePaths;

    private PublishBean mPublishBean;
    private ProgressDialog mGetDistanceDialog;

    private String mEndCity;
    private String mEndProvince;
    private String mStartCity;
    private String mStartProvince;

    @BindView(R.id.publishgoodspacking)
    EditText mGoodspackingEt;



    public static PublishFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        PublishFragment fragment = new PublishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PublishPresenter initPresenter() {
        return new PublishPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publish_fragment;
    }

    @Override
    protected void initView(View view) {
        mPublishBean = new PublishBean();
        mPhotoScrollView.setOnSelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PublishFragment.this).openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mStartSpinnerView.init(getFragmentManager());
        mStartSpinnerView.setOnAreaSpinnerSelectListener(new AreaSpinnerView.OnAreaSpinnerSelectListener() {
            @Override
            public void onProSelect(Address pro) {
                mStartPro = pro;
            }

            @Override
            public void onCitySelect(Address city) {
                //起点城市
                mStartCityAddress = city;
                checkLongLocation1();
            }
        });
        mEndSpinnerView.init(getFragmentManager());
        mEndSpinnerView.setOnAreaSpinnerSelectListener(new AreaSpinnerView.OnAreaSpinnerSelectListener() {
            @Override
            public void onProSelect(Address pro) {
                mEndPro = pro;
            }

            @Override
            public void onCitySelect(Address city) {
                //终点城市
                mEndCityAddress = city;
                checkLongLocation1();
            }
        });
    }

    @OnClick(R.id.publishCategoryLayout)
    public void categoryClick() {
        CategoryDialog dialog = new CategoryDialog();
        dialog.mListener = new CategoryDialog.OnCategoryCallbackListener() {
            @Override
            public void onCategoryCallback(CategoryDialog.CategoryEnum category, String des) {
                //长途整车还要判断下是否有相关权限
                if (category == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {

                    if (!mAccount.isAuth()) {
                        AuthDialog authDialog = new AuthDialog();
                        authDialog.show(getFragmentManager(), "auth");
                        return;
                    }
                }

                mCategory = category;
                mCategoryTv.setText(des);

                //设置配送类型
                mPublishBean.category = mCategory.getValue();
                if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL
                        || mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
                    mTruckLayout.setVisibility(View.VISIBLE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);

                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;

                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;

                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");

//                    mAddressLongTotalLayout.setVisibility(View.VISIBLE);
//                    mAddressDefaultLayout.setVisibility(View.GONE);
                } else {

                    mTruckLayout.setVisibility(View.VISIBLE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);

//                    mAddressLongTotalLayout.setVisibility(View.GONE);
//                    mAddressDefaultLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        dialog.mCategory = mCategory;
        dialog.show(getFragmentManager(), "category");
    }

    @OnClick(R.id.publishTruckLayout)
    public void truckClick() {
        if (mCategory == null) {
            showToast(getString(R.string.publish_category_hint));
            return;
        }
        Intent intent = new Intent(mContext, TruckActivity.class);
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {
            intent.putExtra("categoty", "0");
            intent.putExtra("title", getString(R.string.publish_category_short));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {
            intent.putExtra("categoty", "1");
            intent.putExtra("title", getString(R.string.publish_category_longtotal));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
            intent.putExtra("categoty", "2");
            intent.putExtra("title", getString(R.string.publish_category_longzero));
        }
       else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
            intent.putExtra("categoty", "5");
            intent.putExtra("title", getString(R.string.publish_category_zhuanche));
        }
        else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
            intent.putExtra("categoty", "6");
            intent.putExtra("title", getString(R.string.publish_category_Mineral));
        }
        startActivityForResult(intent, RESULT_TRUCK);
    }

    @OnClick({
            R.id.publishAddressDefaultStartLocationTv, R.id.publishAddressDefaultEndLocationTv,
            R.id.publishAddressDefaultStartLocationIv, R.id.publishAddressDefaultEndLocationIv
    })
    public void defaultLocationClick(View v) {
        Intent intent = new Intent(mContext, SearchLocationActivity.class);
        if (v.getId() == R.id.publishAddressDefaultStartLocationTv) {
            intent.putExtra("title", "请选择发货位置");
            startActivityForResult(intent, RESULT_POSITION_START);
        } else if (v.getId() == R.id.publishAddressDefaultEndLocationTv) {
            intent.putExtra("title", "请选择到货位置");
            startActivityForResult(intent, RESULT_POSITION_END);
        } else if (v.getId() == R.id.publishAddressDefaultStartLocationIv) {//选择发货位置

            startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_START);

        } else if (v.getId() == R.id.publishAddressDefaultEndLocationIv) {//选择到货位置

            startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_END);

        }
    }

    @OnClick({
            R.id.publishProductTv, R.id.publishStartPhoneIv,
            R.id.publishGetPersonIv, R.id.publishGetPhoneIv,
            R.id.publishTimeTv
    })
    public void onClick(View v) {
        if (v.getId() == R.id.publishProductTv) {
            ProductDialog dialog = new ProductDialog();
            dialog.setOnProductSelectListener(new ProductDialog.OnProductSelectListener() {
                @Override
                public void onProductSelect(String name) {
                    mProductTv.setText(name);

                    //设置货名
                    mPublishBean.productName = name;
                }
            });
            dialog.show(Objects.requireNonNull(getFragmentManager()), "product");
        } else if (v.getId() == R.id.publishStartPhoneIv) {//发货人电话
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_START_PHONE);
        } else if (v.getId() == R.id.publishGetPersonIv) {//收货人
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET_PERSON);
        } else if (v.getId() == R.id.publishGetPhoneIv) {//收货人号码
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET_PHONE);
        } else if (v.getId() == R.id.publishTimeTv) {//发货时间
            //获取一个现在时间
            final Time now = new Time();
            now.setToNow();

            new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker arg0, final int year, final int month, int day) {
                    now.year = year;
                    now.month = month;
                    now.monthDay = day;

                    new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener(){
                        @Override
                        public void onTimeSet(TimePicker arg0, int hour, int minute) {
                            now.hour = hour;
                            now.minute = minute;

                            String monthStr = String.valueOf(now.month + 1);
                            String dayStr = String.valueOf(now.monthDay);
                            String hourStr = String.valueOf(now.hour);
                            String minuteStr = String.valueOf(now.minute);

                            if (monthStr.length() == 1) {
                                monthStr = "0" + monthStr;
                            }
                            if (dayStr.length() == 1) {
                                dayStr = "0" + dayStr;
                            }
                            if (hourStr.length() == 1) {
                                hourStr = "0" + hourStr;
                            }
                            if (minuteStr.length() == 1) {
                                minuteStr = "0" + minuteStr;
                            }

                            String des = now.year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr;

                            //比较两个日期
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:MM");

//                            ParsePosition pos = new ParsePosition(0);

//                            if (Utils.compareDate("yyyy-M-d HH:MM", des, dateFormat.format(new Date()))) {
//                                showToast("时间不能小于当前时间");
//                                return;
//                            }

                            mTimeTv.setText(des);

                        }} , now.hour, now.minute, true).show();
                }}, now.year, now.month, now.monthDay).show();

        }
    }

    @OnClick(R.id.publishNextBtn)
    public void next() {

        if (mCategory == null) {
            showToast("请先选择配送类型");
            return;
        }

        //发货人手机
        mPublishBean.consignorPhone = mStartPhoneEt.getText().toString();
        mPublishBean.consigneeName = mGetPersonEt.getText().toString();
        mPublishBean.consigneePhone = mGetPhoneEt.getText().toString();
        //包装类型
        mPublishBean.packingType =  mGoodspackingEt.getText().toString();
      //  判断吨位和当前的车型吨位是否符合
        if( Double.parseDouble(mDefaultWeightEt.getText().toString())<= Double.parseDouble(mPublishBean.truck.capacity))
          {
                mPublishBean.defaultWeight = mDefaultWeightEt.getText().toString();
          }
          else
            {
                mDefaultWeightEt.setText("");
                showLongToast("您输入的吨位和当前车型不符合，重新输入");
                return;

            }
//        //  判断方位和当前的车型方位是否符合
//        if( Double.parseDouble(mDefaultWeightEt.getText().toString())<= Double.parseDouble(mPublishBean.truck.capacity))
//        {
//            mPublishBean.defaultWeight = mDefaultWeightEt.getText().toString();
//        }
//        else
//        {
//            mDefaultWeightEt.setText("");
//            showLongToast("您输入的吨位和当前车型不符合，重新输入");
//            return;
//            //showToast("您输入的吨位和当前车型不符合");
//        }


      //  mPublishBean.defaultWeight = mDefaultWeightEt.getText().toString();

        mPublishBean.defaultArea = mDefaultAreaEt.getText().toString();
        mPublishBean.defaultNum = mDefaultNumEt.getText().toString();

        mPublishBean.time = mTimeTv.getText().toString();

        mPublishBean.imagePaths = mImagePaths;

        if (mPublishBean.check()) {
            Intent intent = new Intent(mContext, PublishConfirmActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", mPublishBean);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            showToast("有选项没有填写");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            mImagePaths = new ArrayList<>();
            for (LocalMedia lm : selectList) {
                if (lm.isCompressed()) {
                    mImagePaths.add(lm.getCompressPath());
                } else {
                    mImagePaths.add(lm.getPath());
                }
            }

            mPhotoScrollView.setActivity(getActivity());
            mPhotoScrollView.setDatas(mImagePaths, selectList);

        } else if (requestCode == RESULT_TRUCK) {//已选车型
            Truck truck = (Truck) data.getSerializableExtra("data");
            mTruckTv.setText(truck.getTitle());
            //设置已选车型
//            mPublishBean.truckId = truck.getId();
//            mPublishBean.truckDes = truck.getTitle();
            mPublishBean.truck = truck;
        } else if (requestCode == RESULT_POSITION_START) {//发货定位
            String title = data.getStringExtra("title");
            mDefaultStartLocationTv.setText(title);

            mStartCity = data.getStringExtra("city");
            mStartProvince = data.getStringExtra("pro");
            mPublishBean.longStartCityDes = mStartCity;

            mPublishBean.startDesc = title;

            requestCityLonLat();

            if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {

                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城订单不能出现不同的城市，请重新选择城市");
                        mStartCity = null;
                        mDefaultStartLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;
                mPublishBean.defaultStartPosDes = title;

                checkDefaultLocation();
            }
            else if(mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche)//同城专车
            {
                if (mStartCity != null && mEndCity != null) {
                if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                    showLongToast("同城专车订单不能出现不同的城市，请重新选择城市");
                    mStartCity = null;
                    mDefaultStartLocationTv.setText("");
                    return;
                }
            }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;
                mPublishBean.defaultStartPosDes = title;

                checkDefaultLocation();
           }
            else {

                if (mStartCity != null && mEndCity != null) {
                    if (mStartCity.equals(mEndCity) || mStartCity.contains(mEndCity) || mEndCity.contains(mStartCity)) {
                        showLongToast("长途订单不能出现相同的城市，请重新选择城市");
                        mStartCity = null;
                        mDefaultStartLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;

                mPublishBean.longStartProvinceDes = mStartProvince;
                mPublishBean.longStartAreaDes = title;

                checkLongLocation();
            }
        } else if (requestCode == RESULT_POSITION_END) {//到货定位
            String title = data.getStringExtra("title");
            mDefaultEndLocationTv.setText(title);

            mEndCity = data.getStringExtra("city");
            mEndProvince = data.getStringExtra("pro");
            mPublishBean.longEndCityDes = mEndCity;

            mPublishBean.endDesc = title;

            requestCityLonLat();

            if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {

                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }

                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                //设置到货定位
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.defaultEndPosDes = title;

                checkDefaultLocation();
            }
            else if(mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche)
            {
                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城专车订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }
                 mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                //设置到货定位
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.defaultEndPosDes = title;

                checkDefaultLocation();
            }
            else {
                    if (mStartCity != null && mEndCity != null) {
                    if (mStartCity.equals(mEndCity) || mStartCity.contains(mEndCity) || mEndCity.contains(mStartCity)) {
                        showLongToast("长途订单不能出现相同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);
                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;

                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.longEndAreaDes = title;

                checkLongLocation();
            }
        } else if (requestCode == RESULT_FRIEND_START_PHONE) {//发货电话
            Friend friend = (Friend) data.getSerializableExtra("data");
            mStartPhoneEt.setText(friend.getPhone());

            //设置发货电话
            mPublishBean.consignorPhone = friend.getPhone();
        } else if (requestCode == RESULT_FRIEND_GET_PERSON) {//收货人
            Friend friend = (Friend) data.getSerializableExtra("data");
            mGetPersonEt.setText(friend.getName());
            mGetPhoneEt.setText(friend.getPhone());

            //设置收货人和收货电话
            mPublishBean.consigneeName = friend.getName();
            mPublishBean.consigneePhone = friend.getPhone();
        } else if (requestCode == RESULT_FRIEND_GET_PHONE) {//收货电话
            Friend friend = (Friend) data.getSerializableExtra("data");
            mGetPhoneEt.setText(friend.getPhone());

            //设置收货电话
            mPublishBean.consigneePhone = friend.getPhone();
        }
    }

    /**
     * 计算城市的经纬度
     */
    private void requestCityLonLat() {
        if (!TextUtils.isEmpty(mStartCity) && !TextUtils.isEmpty(mEndCity)) {
            mPresenter.requestCityLonLat(mStartCity, mEndCity);
        }
    }

    /**
     * 检查默认情况下（目前可特指同城配送）地理位置检查
     */
    private void checkDefaultLocation() {
        if (mDefaultStartLat != 0 && mDefaultStartLon != 0
                && mDefaultEndLat != 0 && mDefaultEndLon != 0) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            //发起请求以计算两点间最短距离
            mPresenter.calculateDistanceWithLonLat(mDefaultStartLon, mDefaultStartLat, mDefaultEndLon, mDefaultEndLat);
        }
    }

    /**
     * 目前该方法没啥用
     */
    private void checkLongLocation1() {
        if (mEndCityAddress != null && mStartCityAddress != null) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            mPresenter.calculateDistanceWithCityName(mStartCityAddress.getName(), mEndCityAddress.getName(), mStartPro.getName
                    (), mEndPro.getName());
        }
    }

    private void checkLongLocation() {
        if (mStartCity != null && mEndCity != null) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            mPresenter.calculateDistanceWithCityName(mStartCity, mEndCity, mStartProvince, mEndProvince);
        }
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Distance> entity) {
        mGetDistanceDialog.dismiss();
        Distance d = entity.getData();
        mPublishBean.distance = d.distance;
        mPublishBean.duration=d.duration;
        mPublishBean.end=d.end;
        mPublishBean.start=d.start;
        mPublishBean.singleTonPrice = d.ton;
        mPublishBean.singleAreaPrice = d.square;
    }

    @Override
    public void onRefreshError(Throwable e) {
        mGetDistanceDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mGetDistanceDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(PublishClearCacheEvent event) {
        mPublishBean = null;
        mPublishBean = new PublishBean();
        mCategory = null;
        mDefaultStartLat = 0;
        mDefaultStartLon = 0;
        mDefaultEndLat = 0;
        mDefaultEndLon = 0;
        mStartPro = null;
        mStartCityAddress = null;
        mEndPro = null;
        mEndCityAddress = null;


        mCategoryTv.setText("");
        mTruckTv.setText("");

        mDefaultStartLocationTv.setText("");
        mDefaultEndLocationTv.setText("");

        mStartSpinnerView.clear();
        mEndSpinnerView.clear();

        mProductTv.setText("");
        mDefaultWeightEt.setText("");
        mDefaultAreaEt.setText("");
        mDefaultNumEt.setText("");
        mStartPhoneEt.setText("");
        mGetPersonEt.setText("");
        mGetPhoneEt.setText("");
        mGoodspackingEt.setText("");
        mPhotoScrollView.setDatas(null, null);
        mImagePaths = null;
    }

    @Override
    public void onRequestCityLatLonSuccess(BaseEntity<Distance> entity) {
        Distance d = entity.getData();
        mPublishBean.startX = d.startX;
        mPublishBean.startY = d.startY;
        mPublishBean.endX = d.endX;
        mPublishBean.endY = d.endY;
    }
}
