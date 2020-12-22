package com.xrwl.driver.module.order.owner.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.blankj.utilcode.util.AppUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.event.OwnerOrderListRrefreshEvent;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.Photo;
import com.xrwl.driver.utils.Constants;
import com.xrwl.driver.view.PhotoRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class OwnerOrderDetailActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>
        implements OwnerOrderContract.IDetailView {

    @BindView(R.id.detailStartPosTv)
    TextView mStartPosTv;
    @BindView(R.id.detailEndPosTv)
    TextView mEndPosTv;
    @BindView(R.id.detailProductNameTv)
    TextView mProductNameTv;
    @BindView(R.id.detailTruckTv)
    TextView mTruckTv;
    @BindView(R.id.detailAreaTv)
    TextView mAreaTv;
    @BindView(R.id.detailPriceTv)
    TextView mPriceTv;
    @BindView(R.id.detailWeightTv)
    TextView mWeightTv;
    @BindView(R.id.detailKiloTv)
    TextView mKiloTv;
    @BindView(R.id.detailNumTv)
    TextView mNumTv;

    @BindView(R.id.detailCancelBtn)
    Button mCancelBtn;
    @BindView(R.id.detailLocationBtn)
    Button mLocationBtn;
    @BindView(R.id.detailConfirmBtn)
    Button mConfirmBtn;

    @BindView(R.id.payLayout)
    View mPayView;//支付宝和微信支付

    @BindView(R.id.detailPhotoView)
    PhotoRecyclerView mPhotoView;

    @BindView(R.id.detailOrderIdTv)
    TextView mOrderIdTv;//订单编号

    private ProgressDialog mLoadingDialog;
    private List<Photo> mImagePaths;
    private String mId;
    private String mDriverId;
    private ProgressDialog mPostDialog;
    private OrderDetail mOrderDetail;
    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;

    @Override
    protected OwnerOrderDetailPresenter initPresenter() {
        return new OwnerOrderDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ownerorderdetail_activity;
    }

    @Override
    protected void initViews() {

        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mWXApi.registerApp(Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);

        mPhotoView.setOnPhotoRvControlListener(new PhotoRecyclerView.OnPhotoRvControlListener() {
            @Override
            public void onCamera() {
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(5)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        mId = getIntent().getStringExtra("id");
        mDriverId = getIntent().getStringExtra("driverid");
        mPhotoView.hideCamera();
        mLoadingDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getOrderDetail(mId);
    }

    @OnClick({R.id.detailCancelBtn, R.id.detailLocationBtn, R.id.detailConfirmBtn, R.id.wxPayLayout, R.id.aliPayLayout})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.detailCancelBtn) {

            new AlertDialog.Builder(this).setMessage("是否确定取消订单？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.cancelOrder(mId);
                        }
                    }).show();
        } else if (id == R.id.detailLocationBtn) {
            new AlertDialog.Builder(this)
                    .setMessage("您确定要定位司机吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.location(mId, mDriverId);
                        }
                    }).show();
        } else if (id == R.id.detailConfirmBtn) {
            new AlertDialog.Builder(this)
                    .setMessage("您确定要确认收货吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.confirmOrder(mId);
                        }
                    }).show();
        } else if (id == R.id.wxPayLayout) {
            mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
            Map<String, String> params = new HashMap<>();
            params.put("appname", AppUtils.getAppName());
            params.put("product_name", mOrderDetail.productName);
            params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
            params.put("orderid", mId);
            params.put("type", "1");
            params.put("price", mOrderDetail.price + "");

            mPresenter.wxPay(params);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {
        mOrderDetail = entity.getData();
        mStartPosTv.setText(mOrderDetail.startPos);
        mEndPosTv.setText(mOrderDetail.endPos);
        mProductNameTv.setText("货名：" + mOrderDetail.productName);
        mTruckTv.setText("车型：" + mOrderDetail.truck);
        mAreaTv.setText("体积：" + mOrderDetail.area + "方");
        mPriceTv.setText("价格：" + mOrderDetail.price + "元");
        mWeightTv.setText("重量：" + mOrderDetail.weight + "吨");
        mKiloTv.setText("公里：" + mOrderDetail.kilo + "公里");
      //  mNumTv.setText("数量：" + mOrderDetail.num + "件");

        mImagePaths = mOrderDetail.getPics();

        mOrderIdTv.setText("订单编号：" + mOrderDetail.orderId);

        mPhotoView.setDatas(mOrderDetail.getPics());

        if (mOrderDetail.type.equals("0")) {
            mLocationBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
        } else if (mOrderDetail.type.equals("1") || mOrderDetail.type.equals("2")) {
            mCancelBtn.setVisibility(View.GONE);
        } else {
            mCancelBtn.setVisibility(View.GONE);
            mLocationBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
        }

        if (!mOrderDetail.showPay()) {
            mPayView.setVisibility(View.GONE);
        }

        mLoadingDialog.dismiss();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mLoadingDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPostDialog != null) {
            mPostDialog.dismiss();
        }
        mLoadingDialog.dismiss();
        handleError(entity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            for (LocalMedia lm : selectList) {
                if (lm.isCompressed()) {
                    Photo photo = new Photo();
                    photo.setPath(lm.getCompressPath());
                    mImagePaths.add(photo);
                } else {
                    Photo photo = new Photo();
                    photo.setPath(lm.getPath());
                    mImagePaths.add(photo);
                }
            }
        }
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {
        mPostDialog.dismiss();
        showToast("取消订单成功");
        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onCancelOrderError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {
        mPostDialog.dismiss();
        showToast("收货成功");
        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onConfirmOrderError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {
        mPostDialog.dismiss();

        OrderDetail od = entity.getData();
        double lat = Double.parseDouble(od.lat);
        double lon = Double.parseDouble(od.lon);

        NaviPara naviPara = new NaviPara();
        //设置终点位置
        naviPara.setTargetPoint(new LatLng(lat, lon));
        //设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
        try {
            AMapUtils.openAMapNavi(naviPara, this);
        } catch (AMapException e) {
            e.printStackTrace();
            //如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(this);
        }

//        RoutePara routePara = new RoutePara();
//        routePara.setStartName("宁波");
//        routePara.setEndName("上海");
//        try {
//            AMapUtil.openAMapDrivingRoute(routePara, this);
//        } catch (AMapException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onLocationError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

        mPostDialog.dismiss();

        final PayResult pr = entity.getData();

        //发起微信或支付宝支付
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!mWXApi.isWXAppInstalled()) {
                    Toast.makeText(mContext, "没有安装微信哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mWXApi.isWXAppSupportAPI()) {
                    Toast.makeText(mContext, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
                    return;
                }
//                PayReq request = new PayReq();
//                request.appId = pr.appid;
//                request.partnerId = pr.partnerid;
//                request.prepayId = pr.prepayid;
//                request.packageValue = pr.packagestr;
//                request.nonceStr = pr.noncestr;
//                request.timeStamp = pr.timestamp;
//                request.sign = pr.sign;
////                mWXApi.registerApp(pr.appid);
//                mWXApi.sendReq(request);
//                showToast("启动微信中...");
            }
        });
    }

    @Override
    public void onPayError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    protected void onDestroy() {

        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }

        super.onDestroy();
    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                showToast("付款成功");

                EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
                finish();
            } else {
                showToast("付款失败");
            }
        }

    }
}
