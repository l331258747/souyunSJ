package com.xrwl.driver.module.order.driver.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.AppUtils;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.driver.Frame.auxiliary.RetrofitManager;
import com.xrwl.driver.Frame.retrofitapi.NetService;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.DhBenn;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.event.DriverListRrefreshEvent;
import com.xrwl.driver.event.DriverOrderListRrefreshEvent;
import com.xrwl.driver.module.home.ui.CustomDialog;
import com.xrwl.driver.module.home.ui.HongbaolistActivity;
import com.xrwl.driver.module.home.ui.OnRedPacketDialogClickListener;
import com.xrwl.driver.module.home.ui.RedPacketEntity;
import com.xrwl.driver.module.home.ui.RedPacketViewHolder;
import com.xrwl.driver.module.order.driver.mvp.DriverOrderContract;
import com.xrwl.driver.module.order.driver.mvp.DriverOrderDetailPresenter;
import com.xrwl.driver.module.order.driver.ui.ui.route.DriveRouteOverlay;
import com.xrwl.driver.module.order.driver.ui.ui.route.WalkRouteOverlay;
import com.xrwl.driver.module.order.driver.ui.util.AMapUtil;
import com.xrwl.driver.module.order.driver.ui.util.RandomUntil;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.Photo;
import com.xrwl.driver.module.tab.activity.TabActivity;
import com.xrwl.driver.utils.ActivityCollect;
import com.xrwl.driver.utils.Constants;
import com.xrwl.driver.utils.DecimalUtil;
import com.xrwl.driver.view.PhotoRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by www.longdw.com on 2018/4/5 下午9:52.
 */
public class DriverOrderDetailActivity extends BaseActivity<DriverOrderContract.IDetailView, DriverOrderDetailPresenter>
        implements DriverOrderContract.IDetailView, AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {


    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private DriveRouteResult mDriveRouteResult;

    public Double aalat;
    public Double aalon;

    public Double bbmylat;
    public Double bbmylon;
    private final int ROUTE_TYPE_WALK = 3;

    private ProgressDialog progDialog = null;// 搜索时进度条


    public static float abcdefa = 0;
    private final Handler mHandler = new Handler();
    public static float weikuan = 0;

    private int GPS_REQUEST_CODE = 1;

    private String chaju;
    public String zidongcategory;
    public String canshu;
    public String ddzhuangtais;
    @BindView(R.id.detailStartPosTv)
    TextView mStartPosTv;
    @BindView(R.id.detailEndPosTv)
    TextView mEndPosTv;

    @BindView(R.id.detailStartPosTvs)
    TextView mStartPosTvs;
    @BindView(R.id.detailEndPosTvs)
    TextView mEndPosTvs;


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
    @BindView(R.id.detailNavBtn)
    Button mNavBtn;
    @BindView(R.id.detailGrabBtn)
    Button mGrabBtn;
    @BindView(R.id.detailTransBtn)
    Button mTransBtn;
    @BindView(R.id.detailConfirmBtn)
    Button mConfirmBtn;
    @BindView(R.id.detailUploadBtn)
    Button mUploadBtn;

    @BindView(R.id.detailConfirmxianjinBtn)
    Button mdetailConfirmxianjinBtn;


    @BindView(R.id.detailPhotoView) //图片上传和放大
            PhotoRecyclerView mPhotoView;

    @BindView(R.id.detailConsignorTv)
    TextView mConsignorTv;//发货电话

    @BindView(R.id.detailConsigneeTv)
    TextView mConsigneeTv;//收货电话

    @BindView(R.id.detailRemarkTv)
    TextView mRemarkTv;//备注

    @BindView(R.id.payLayout)
    LinearLayout mPayLayout;

    @BindView(R.id.detailOrderIdTv)
    TextView mOrderIdTv;//订单编号

    @BindView(R.id.detailBaozhuangTv)
    TextView mBaozhuangTv;
    @BindView(R.id.paytv)
    TextView ispaytv;


    @BindView(R.id.kuangchan)
    FrameLayout iskuangchan;

    @BindView(R.id.keladunTV)
    TextView mkeladunTV;


    @BindView(R.id.yifujineTV)
    TextView myifujineTV;

    private ProgressDialog mLoadingDialog;
    private List<Photo> mImagePaths;
    private ProgressDialog mOperationDialog;
    private ProgressDialog isRangeDialog;
    private String mId;

    private String mConsignorPhone;
    private String mConsignorPhoneda;
    private String mConsigneePhone;
    private OrderDetail mOrderDetail;
    private ProgressDialog mPayDialog;
    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private boolean mConfirmClick;


    public Double mylat;
    public Double mylon;

    public Double readmylat;
    public Double readmylon;

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;


    @BindView(R.id.payhuokuantv)
    TextView mpayhuokuantv;

    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;


    @BindView(R.id.homeIntroRv)
    RecyclerView mHomeIntroRv;


    @BindView(R.id.homeViewPager)
    ViewPager mViewPager;
    @BindView(R.id.homeServiceRv)
    RecyclerView mHomeServiceRv;

    public double nidayex;
    public double nidayey;
    private OrderDetail od;
    public LatLonPoint mStartPoint;
    public LatLonPoint mEndPoint;
    private RetrofitManager retrofitManager;
    private String time;
    private int num;
    private String num1;


    @Override
    protected DriverOrderDetailPresenter initPresenter() {
        return new DriverOrderDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.driverorderdetail_activity;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        initLocation();


        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(bundle);
        init();

        //   searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
    }

    @Override
    protected void initViews() {
        mPhotoView.setOnPhotoRvControlListener(new PhotoRecyclerView.OnPhotoRvControlListener() {
            @Override
            public void onCamera() {
                PictureSelector.create(DriverOrderDetailActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
        mId = getIntent().getStringExtra("id");
        mPresenter.getOrderDetail(mId);
        mPresenter.hit(mId);

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    String city = aMapLocation.getCity();
                    String chuangcity = aMapLocation.getCity();
                    mylat = aMapLocation.getLatitude();
                    mylon = aMapLocation.getLongitude();
                    nidayex = aMapLocation.getLatitude();
                    nidayey = aMapLocation.getLongitude();

                } else {

                }
                mLocationClient.stopLocation();
            }
        });
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient.startLocation();


//        mLocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation.getErrorCode() == 0) {
//                    String city = aMapLocation.getCity();
//                    String chuangcity = aMapLocation.getCity();
//                    mylat = aMapLocation.getLatitude();
//                    mylon = aMapLocation.getLongitude();
//                    nidayex = aMapLocation.getLatitude();
//                    nidayey = aMapLocation.getLongitude();
//
//                } else {
//
//                }
//
//                mLocationClient.stopLocation();
//
//
//            }
//        });
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    public void showRedPacketDialog(RedPacketEntity entity) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(this, R.layout.dialog_red_packet, null);
            mRedPacketViewHolder = new RedPacketViewHolder(this, mRedPacketDialogView);
            mRedPacketDialog = new CustomDialog(this, mRedPacketDialogView, R.style.custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }

        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, HongbaolistActivity.class);
                        intent.putExtra("orderid", mId);
                        intent.putExtra("nums", od.Actual_price);
                        startActivity(intent);
                        mRedPacketDialog.dismiss();
                        // finish();
                    }
                }, 1000);

            }
        });
        mRedPacketDialog.show();
    }

    @Override
    public void showLoading() {
        // mLoadingDialog = LoadingProgress.showProgress(this, "正在加载...");
    }

    @Override

    public void onWxPaySuccess(BaseEntity<PayResult> entity) {
        //mPayDialog.dismiss();
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
//                //  mWXApi.registerApp(pr.appid);
//                mWXApi.sendReq(request);
//                // showToast("启动微信中...");
            }
        });
    }

    @Override
    public void onWxPayError(Throwable e) {
        // mPayDialog.dismiss();
        //  showToast("网络异常，支付失败");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {
        od = entity.getData();
//        mOrderDetail = od;
//        起点
        mStartPoint = new LatLonPoint(Double.valueOf(od.startLat), Double.valueOf(od.startLon));
        //终点
        mEndPoint = new LatLonPoint(Double.valueOf(od.endLat), Double.valueOf(od.endLon));


//        if(od.type.equals("0"))
//        {
//
//        }
//        else
//        {
//            mPresenter.nav(mId);
//        }


//        showToast(od.startLat + od.startLon + od.endLat + od.endLon);
//
//
//        showToast(mStartPoint + "0000000" + mEndPoint);


        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));

//        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.DRIVING_SINGLE_DEFAULT);

        readmylon = Double.parseDouble(od.endLon);
        readmylat = Double.parseDouble(od.endLat);


        zidongcategory = od.zidongzhicategory;
        canshu = od.dingwei;
        ddzhuangtais = od.type;

        if ("7".equals(od.category)) {
            mAreaTv.setVisibility(View.INVISIBLE);
            mWeightTv.setVisibility(View.INVISIBLE);

            mNumTv.setVisibility(View.INVISIBLE);
        } else {
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);

            mNumTv.setVisibility(View.VISIBLE);
        }


        if ("3".equals(od.type)) {
            if ("0".equals(od.renmibi)) {
                if (!od.driverhongbao.equals("1")) {
                    if ("1".equals(od.Receiving)) //确认收货后显示红包
                    {
                        hongbao();
                    }

                }
            }
        }
        if (od.driver == null || "0".equals(od.driver)) {

        } else {
            if (mAccount.getId().equals(od.driver)) {

            } else {
                new AlertDialog.Builder(this)
                        .setMessage("该订单已被其他司机接单，请尝试其他订单！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, TabActivity.class);
                                intent.putExtra("title", "找货");
                                startActivity(intent);
                            }
                        }).show();

            }
        }
        if (od.total_prices == null) {
            mpayhuokuantv.setVisibility(View.GONE);
        } else {
            if (od.daishoutype.equals("0")) {
                mpayhuokuantv.setText("全付");
                //   mpayhuokuantv.setTextColor(Color.BLUE);
            } else {
                mpayhuokuantv.setText("未付");
            }
        }


        mStartPosTv.setText(od.startPos);
        mStartPosTvs.setText(od.start_desc);
        mEndPosTv.setText(od.endPos);
        mEndPosTvs.setText(od.end_desc);


        mProductNameTv.setText(od.productName);
        mTruckTv.setText(od.truck);
        if (!TextUtils.isEmpty(od.area)) {
            mAreaTv.setText("体积：" + od.area + "方");
        } else {
            mAreaTv.setText("体积：" + "0方");
        }
        if (od.category.equals("0")) {
//            weikuan= (Float.parseFloat(od.price) * 90) / 100;
//            float startPrice = Float.parseFloat(od.price);
//            float abcd=0;
//            abcd=(startPrice-(startPrice * 8)/100);
//            abcdefa=abcd;
            mPriceTv.setText(od.Actual_price + "元");
            iskuangchan.setVisibility(View.GONE);
        } else if (od.category.equals("5")) {
            //   weikuan= (Float.parseFloat(od.price) * 90) / 100;
//            float startPrice = Float.parseFloat(od.price);
//            float abcd=0;
//            abcd=(startPrice-(startPrice * 8)/100);
//            abcdefa=abcd;
            mPriceTv.setText(od.Actual_price);
            iskuangchan.setVisibility(View.GONE);
        } else if (od.category.equals("1")) {
//            weikuan= (Float.parseFloat(od.price) * 90) / 100;
//            float startPrice = Float.parseFloat(od.price);
//            float abcd=0;
//            abcd=startPrice-(startPrice * 6)/100;
//            abcdefa=abcd;
            mPriceTv.setText(od.Actual_price + "元");
            iskuangchan.setVisibility(View.GONE);
        } else if (od.category.equals("7")) {
//            weikuan= (Float.parseFloat(od.price) * 90) / 100;
//            float startPrice = Float.parseFloat(od.price);
//            float abcd=0;
//            abcd=startPrice-(startPrice * 6)/100;
//            abcdefa=abcd;
            mPriceTv.setText(od.Actual_price + "元");
            iskuangchan.setVisibility(View.GONE);
        } else if (od.category.equals("2")) {

//            weikuan= (Float.parseFloat(od.price) * 90) / 100;
//            float startPrice = Float.parseFloat(od.price);
//            float abcd=0;
//            abcd=startPrice-(startPrice * 4)/100;
//            abcdefa=abcd;
            mPriceTv.setText(od.Actual_price + "元");
            iskuangchan.setVisibility(View.GONE);
        } else if (od.category.equals("6")) {
            weikuan = 0;
            float startPrice = Float.parseFloat(od.price);
            float overtotal_prices = Float.parseFloat(od.overtotal_price);
            float dunshu = Float.parseFloat(od.weight);
            float abcd = 0;
            abcd = startPrice - (startPrice * 4) / 100;
            abcdefa = abcd;
            mPriceTv.setText(abcd + "元");
            iskuangchan.setVisibility(View.VISIBLE);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String p = decimalFormat.format(overtotal_prices / startPrice);
            String kelap = decimalFormat.format((overtotal_prices / startPrice) * dunshu);

            mkeladunTV.setText(kelap + "吨");
            myifujineTV.setText(od.overtotal_price + "元");
        } else {
            weikuan = Float.parseFloat(od.price);
            iskuangchan.setVisibility(View.GONE);
            //abcdefa=od.price;
            mPriceTv.setText("价格：" + "价格异常");
        }

        if (!TextUtils.isEmpty(od.weight)) {
            mWeightTv.setText("重量：" + od.weight + "吨");
        } else {
            mWeightTv.setText("重量：" + "0吨");
        }

        mKiloTv.setText(od.kilo + "公里");
        if (!TextUtils.isEmpty(od.num)) {
            mNumTv.setText(od.num + "件");
        } else {
            mNumTv.setText("数量：" + "0件");
        }
        mBaozhuangTv.setText(od.packingtype);

        if (od.tixing.equals("2")) {
            //   ispaytv.setTextColor(Color.BLUE);
            ispaytv.setText("已结清");
        } else {
            if (od.isbzj_type.equals("0") && od.istype.equals("0")) {
                //    ispaytv.setTextColor(Color.BLUE);
                ispaytv.setText("已付");
                // hongbao();
                if (od.category.equals("0") || od.category.equals("5")) {
                    if ("3".equals(od.type)) {
                        if (!od.hongbao.equals("1")) {
                            if ("1".equals(od.Receiving)) {
                                RedPacketEntity entitys = new RedPacketEntity("16飕云", "http://www.16souyun.com/xcx/hongbao.png", "订单完成，红包返现");
                                showRedPacketDialog(entitys);
                                mHomeIntroRv.setFocusableInTouchMode(false);
                                mHomeIntroRv.setHasFixedSize(true);
                                mHomeIntroRv.setNestedScrollingEnabled(false);
                                mHomeServiceRv.setFocusableInTouchMode(false);
                                mHomeServiceRv.setHasFixedSize(true);
                                mHomeServiceRv.setNestedScrollingEnabled(false);
                                mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                                mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                                mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                                mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                            }
                        }
                    }
                }


            } else if (od.isbzj_type.equals("0") && od.istype.equals("1")) {
                ispaytv.setText("已付保证金");
            } else if (od.isbzj_type.equals("1") && od.istype.equals("0")) {
//                ispaytv.setTextColor(Color.BLUE);
                ispaytv.setText("已付");
                if (od.category.equals("0") || od.category.equals("5")) {
                    if ("3".equals(od.type)) {
                        if (!od.hongbao.equals("1")) {
//                        RedPacketEntity entitys = new RedPacketEntity("16飕云", "http://www.16sbj.com/xcx/hongbao.png", "订单完成，红包返现");
//                        showRedPacketDialog(entitys);
                            if ("1".equals(od.Receiving)) {
                                mHomeIntroRv.setFocusableInTouchMode(false);
                                mHomeIntroRv.setHasFixedSize(true);
                                mHomeIntroRv.setNestedScrollingEnabled(false);
                                mHomeServiceRv.setFocusableInTouchMode(false);
                                mHomeServiceRv.setHasFixedSize(true);
                                mHomeServiceRv.setNestedScrollingEnabled(false);
                                mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                                mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                                mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                                mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                            }
                        }
                    }


                }
            } else if (od.isbzj_type.equals("1") && od.istype.equals("1")) {
                ispaytv.setText("未付");
            }
        }

        mRemarkTv.setText(od.remark);

        mImagePaths = od.getPics();


        mOrderIdTv.setText(od.ddbh);

        mPhotoView.setDatas(od.getPics());

        //mLoadingDialog.dismiss();


        if (od.type.equals("0")) {
            mGrabBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setVisibility(View.GONE);
            mNavBtn.setVisibility(View.GONE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);


            /**
             * 以往虎山行  拨开云雾见光明
             */

//            mConsignorTv.setText(od.consignorPhone.substring(0, 3) + "****" + od.consignorPhone.substring(7, od.consignorPhone.length()));
//            mConsigneeTv.setText(od.consigneePhone.substring(0, 3) + "****" + od.consigneePhone.substring(7, od.consigneePhone.length()));

        } else if (od.type.equals("1")) {
            mCancelBtn.setVisibility(View.VISIBLE);
            mNavBtn.setVisibility(View.VISIBLE);
            mTransBtn.setVisibility(View.VISIBLE);
            mGrabBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mUploadBtn.setVisibility(View.VISIBLE);

            mConsignorTv.setText((mConsignorPhone = od.consignorPhone.replace("-", "")));
            mConsigneeTv.setText((mConsigneePhone = od.consigneePhone.replace("-", "")));

        } else if (od.type.equals("2")) {
            mNavBtn.setVisibility(View.VISIBLE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.VISIBLE);
            mGrabBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mUploadBtn.setVisibility(View.VISIBLE);
            mConsignorTv.setText("发货电话：" + (mConsignorPhone = od.consignorPhone.replace("-", "")));
            mConsigneeTv.setText("收货电话：" + (mConsigneePhone = od.consigneePhone.replace("-", "")));

        } else if (od.type.equals("3")) {

            if (od.tixing.equals("2")) {
                mdetailConfirmxianjinBtn.setVisibility(View.GONE);
            } else {
                mdetailConfirmxianjinBtn.setVisibility(View.GONE);
            }
            mNavBtn.setVisibility(View.GONE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mGrabBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mPhotoView.setVisibility(View.VISIBLE);
            mUploadBtn.setVisibility(View.VISIBLE);


            mConsignorTv.setText("发货电话：" + (mConsignorPhone = od.consignorPhone.replace("-", "")));
            mConsigneeTv.setText("收货电话：" + (mConsigneePhone = od.consigneePhone.replace("-", "")));


            if (od.isbzj_type.equals("0")) {
                if (od.tixing.equals("1")) {
                    float startPriceaa = (Float.parseFloat(od.price) * 90) / 100;

                    new AlertDialog.Builder(this).setMessage("提醒：收货方/发货方请求尾款进行线下支付，运费尾款还剩余     " + startPriceaa + "元     未支付")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }


        } else if (od.type.equals("4")) {
            mPhotoView.setVisibility(View.VISIBLE);
            mUploadBtn.setVisibility(View.VISIBLE);
            mConsignorTv.setText("发货电话：" + (mConsignorPhone = od.consignorPhone.replace("-", "")));
            mConsigneeTv.setText("收货电话：" + (mConsigneePhone = od.consigneePhone.replace("-", "")));
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBus.getDefault().post(new DriverListRrefreshEvent());

                            finish();
                        }
                    }).show();
        }

//        if (od.canUpload()) {
//            mUploadBtn.setVisibility(View.VISIBLE);
//            mPhotoView.hideCamera();
//        }

        if (!od.showOnPay()) {
            mPayLayout.setVisibility(View.GONE);
        } else {
            if (mConfirmClick) {
                wxPay();
                mConfirmClick = false;
            }
        }


        //  mPresenter.getOrderDetailReceiving(od.category);


    }

    @Override
    public void onRefreshError(Throwable e) {
        // mLoadingDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mOperationDialog != null) {
            // mOperationDialog.dismiss();
        }
        //mLoadingDialog.dismiss();
        handleError(entity);
    }

    public static String getPrecisionStandardTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date = new Date(System.currentTimeMillis());
        String time = sdf.format(date);
        return time;
    }

    //TODO M5d
    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    RandomUntil randomUntil = new RandomUntil();


    //打电话请外乎接口
    private void Driverpositioning() {
        num1 = String.valueOf(RandomUntil.getNum(5));
        String s = md5("OX3CY3Q5SJRQGEZVAILJJ1TS" + "703" + num1 + time + "18210342734" + od.consignorPhone);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", "703");
        hashMap.put("seqId", num1);
        hashMap.put("timestamp", time);
        hashMap.put("fm", "18210342734");
        hashMap.put("tm", od.consignorPhone);
        hashMap.put("bindTime", "0");
        hashMap.put("sign", s);

        retrofitManager = RetrofitManager.getInstance();
        retrofitManager.createReq(NetService.class)
                .DhBenn(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DhBenn>() {

                    private String zbblat;
                    private String zbblon;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DhBenn dhBenn) {
                        String virtualMobile = dhBenn.getVirtualMobile();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + virtualMobile);
                        intent.setData(data);
                        startActivity(intent);
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("www", e.getMessage().toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //字符串转时间戳
    public static String getTime(String timeString) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    @OnClick({R.id.detailConsignorTv, R.id.detailConsigneeTv})
    public void call(View v) {
        int id = v.getId();
        if (id == R.id.detailConsignorTv) {
            //todo dddddddddddddddddddddddddd

            String precisionStandardTime = getPrecisionStandardTime();
            Log.e("sjc", precisionStandardTime.toString());
            time = getTime(precisionStandardTime);
            Log.e("SS", time.toString());
            Driverpositioning();
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            Uri data = Uri.parse("tel:" + od.consignorPhone);
////            intent.setData(data);
////            startActivity(intent);


//            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                if (!TextUtils.isEmpty(mConsignorPhone)) {
//                    Intent intent2 = new Intent();
//                    intent2.setData(Uri.parse("tel:" + mConsignorPhone));
//                    intent2.setAction(Intent.ACTION_CALL);
//                    startActivity(intent2);
//                }
//            } else {
//                new AlertDialog.Builder(this).setMessage("请先打开电话权限")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                PermissionUtils.openAppSettings();
//                            }
//                        }).show();
//            }
        } else if (id == R.id.detailConsigneeTv) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + od.consigneePhone);
            intent.setData(data);
            startActivity(intent);


//            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                if (!TextUtils.isEmpty(mConsigneePhone)) {
//
//                    new AlertDialog.Builder(this).setMessage("是否拨打电话？")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent2 = new Intent();
//                                    intent2.setData(Uri.parse("tel:" + mConsigneePhone));
//                                    intent2.setAction(Intent.ACTION_CALL);
//                                    startActivity(intent2);
//                                }
//                            }).show();
//                }
//            } else {
//                new AlertDialog.Builder(this).setMessage("请先打开电话权限")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                PermissionUtils.openAppSettings();
//                            }
//                        }).show();
//            }
        }
    }

    @OnClick({R.id.detailCancelBtn, R.id.detailNavBtn, R.id.detailTransBtn, R.id.detailConfirmBtn, R.id.detailGrabBtn,
            R.id.detailUploadBtn, R.id.detailConfirmxianjinBtn})
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.detailCancelBtn) {//取消订单
            new AlertDialog.Builder(this).setMessage("是否确定取消订单？取消订单后，其他司机可以正常接单，同时对您在app中承接其他订单的信誉度会大大降低，请慎重！")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.cancelOrder(mId);
                        }
                    }).show();
            return;
        } else if (id == R.id.detailConfirmxianjinBtn)//现金线下支付
        {
            new AlertDialog.Builder(this)
                    .setMessage("请您确认已收到尾款      " + weikuan + " 元      ")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.cancelOrdertixing(mId);
                        }
                    }).show();
        } else if (id == R.id.detailNavBtn) {//导航
            new AlertDialog.Builder(this)
                    .setMessage("您确定要发起线路导航吗？导航方便您能准确定位发货的位置在哪里，大大提升您找到货物位置的效率")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.nav(mId);
                        }
                    }).show();
        } else if (id == R.id.detailTransBtn) {//开始运输

            new AlertDialog.Builder(this)
//                            .setMessage("请提示发货方确认开始运输")
                    .setMessage("是否确认开始运输")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", (dialog, which) -> mPresenter.cancelDriverkaishiyunshu(mId)).show();
            return;

//            if (od.type.equals("6")) {
//                new AlertDialog.Builder(this)
//                        .setMessage("请提示发货方在电脑端操作")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).show();
//
//            } else {
//                if (!od.huozhudianji.equals("1")) {
//                    new AlertDialog.Builder(this)
////                            .setMessage("请提示发货方确认开始运输")
//                            .setMessage("是否确认开始运输")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确认", (dialog, which) -> mPresenter.cancelDriverkaishiyunshu(mId)).show();
//                    return;
//                }
//
//                if (od.huozhudianji.equals("1")) {
//                    new AlertDialog.Builder(this)
//                            .setMessage("您确定要开始运输吗？开始运输后，方便收货方、发货方能准确查询当前货物的位置")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
////                                    mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                                    mPresenter.trans(mId);
//
//                                }
//                            }).show();
//                } else {
//                    new AlertDialog.Builder(this)
//                            .setMessage("发货方未确认开始运输，您没有权限点击，请联系发货方确认")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            }).show();
//                }
//            }

        } else if (id == R.id.detailConfirmBtn) {//确认
//            ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
//            shippingNoteInfo.setShippingNoteNumber("123");
//            shippingNoteInfo.setSerialNumber("123");
//            shippingNoteInfo.setStartCountrySubdivisionCode("123");
//            shippingNoteInfo.setEndCountrySubdivisionCode("123");
//
//            shippingNoteInfo.setSendCount(Integer.parseInt("123"));
//            shippingNoteInfo.setAlreadySendCount(Integer.parseInt("123"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);


            SimpleDateFormat formattera = new SimpleDateFormat("HHmmss");
            Date curDatea = new Date(System.currentTimeMillis());
            String stra = formatter.format(curDatea);


            ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
            shippingNoteInfo.setShippingNoteNumber("TYDH2020" + mId);
            shippingNoteInfo.setSerialNumber("0000");
            shippingNoteInfo.setStartCountrySubdivisionCode(str);
            shippingNoteInfo.setEndCountrySubdivisionCode(str);

            shippingNoteInfo.setSendCount(Integer.parseInt(2020 + mId));
            shippingNoteInfo.setAlreadySendCount(Integer.parseInt(2006 + mId));
            ShippingNoteInfo[] shippingNoteInfos = new ShippingNoteInfo[1];
            int s = shippingNoteInfos.length;
            shippingNoteInfos[0] = shippingNoteInfo;
            //停止服务。context 必须为 activity。
            LocationOpenApi.stop(this, shippingNoteInfos, new OnResultListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {
                    Toast.makeText(mContext, s1.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            //这个位置主要是获取司机当前的坐标信息 ，如果司机没有开启定位信息，请提示司机开启定位信息，然后再进行定位，最终是要判断结果数据
            //什么类别下面的，多少公里数的操作
            //  2公里  1公里   500米   所有的类别都是这样操作，这个开关放在后台操作
            //模拟测试位置信息

            openGPSSEtting();
            if(nidayey <= 0){
                showToast("请打开定位权限再进入订单详情");
                return;
            }
            isRangeDialog = LoadingProgress.showProgress(DriverOrderDetailActivity.this, "正在提交...");
            mPresenter.calculateDistanceWithLonLat(Double.parseDouble(od.endLon), Double.parseDouble(od.endLat), nidayey, nidayex);

//            new AlertDialog.Builder(this)
//                    .setMessage("是否确认到达")
//                    .setNegativeButton("取消", null)
//                    .setPositiveButton("确定", (dialog, which) -> {
////                        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
////                        Date curDate1 = new Date(System.currentTimeMillis());
////                        String str1 = formatter1.format(curDate1);
//                        try {
//                            mPresenter.getCodeButton("4", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), od.consignorPhone);
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                        mConfirmClick = true;
////                        mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                        mPresenter.confirmOrder(mId);
//                    }).show();


//            if (od.type.equals("6")) {
//                new AlertDialog.Builder(this)
//                        .setMessage("请提示发货方或收货方在电脑端操作")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        }).show();
//            } else {
//                if (od.sijidianji.equals("0")) {
//                    new AlertDialog.Builder(this)
//                            .setMessage("请提示发货方确认到达")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("提示", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                                    Date curDate = new Date(System.currentTimeMillis());
//                                    String str = formatter.format(curDate);
//                                    try {
//                                        mPresenter.getCodeButton("4", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), od.consignorPhone);
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
//                                    mPresenter.confirmqianOrder(mId);
//                                }
//                            }).show();
//                    return;
//                }
//                if (od.sijidianji.equals("2")) {
//                    new AlertDialog.Builder(this)
//                            .setMessage("您确定要货物到达吗？到达后双方互相评价，为您下一次接单提供优先匹配服务")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //wxPay();
//                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                                    Date curDate = new Date(System.currentTimeMillis());
//                                    String str = formatter.format(curDate);
//                                    try {
//                                        mPresenter.getCodeButton("4", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), od.consignorPhone);
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
//                                    mConfirmClick = true;
//                                    mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                                    mPresenter.confirmOrder(mId);
//                                }
//                            }).show();
//                } else {
//                    new AlertDialog.Builder(this)
//                            .setMessage("请提示发货方确认到达")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("提示", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // mPresenter.cancelDriverkaishiyunshu(mId);
//                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                                    Date curDate = new Date(System.currentTimeMillis());
//                                    String str = formatter.format(curDate);
//                                    try {
//                                        mPresenter.getCodeButton("4", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), od.consignorPhone);
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
//                                    EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//                                    Intent intent = new Intent(mContext, DriverOrderActivity.class);
//                                    intent.putExtra("position", 1);
//                                    startActivity(intent);
//                                    finish();
//
//                                }
//                            }).show();
//                    return;
//                }
//            }


        } else if (id == R.id.detailGrabBtn) {//抢单


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);


            SimpleDateFormat formattera = new SimpleDateFormat("HHmmss");
            Date curDatea = new Date(System.currentTimeMillis());
            String stra = formatter.format(curDatea);


            ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
            shippingNoteInfo.setShippingNoteNumber("TYDH2020" + mId);
            shippingNoteInfo.setSerialNumber("0000");
            shippingNoteInfo.setStartCountrySubdivisionCode(str);
            shippingNoteInfo.setEndCountrySubdivisionCode(str);

            shippingNoteInfo.setSendCount(Integer.parseInt(2020 + mId));
            shippingNoteInfo.setAlreadySendCount(Integer.parseInt(2006 + mId));


            ShippingNoteInfo[] shippingNoteInfos = new ShippingNoteInfo[1];
            int s = shippingNoteInfos.length;
            shippingNoteInfos[0] = shippingNoteInfo;
            //启用服务。context 必须为 activity。

            LocationOpenApi.start(this, shippingNoteInfos, new OnResultListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {
                    Toast.makeText(mContext, s1.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            //判断司机的车载和未接单的重量是否一致
            // String mobile,String type,String start_city,String end_city,String order_sn,String surname,String logistics,String name_name


            if (od.driver == null || "0".equals(od.driver)) {
                new AlertDialog.Builder(DriverOrderDetailActivity.this)
                        .setMessage("您确定要接单吗？抢单成功后便可获得运输指令，方便联系货主尽快完成运输")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                                Date curDate = new Date(System.currentTimeMillis());
                                String str = formatter.format(curDate);
                                try {
                                    mPresenter.getCodeButton(od.consignorPhone, "3", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), "暂无物流公司", mAccount.phone);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                mOperationDialog = LoadingProgress.showProgress(DriverOrderDetailActivity.this, "正在提交...");
                                mPresenter.grapOrder(mId);

//                            Intent intent = new Intent(mContext, DriverOrderFragment.class);
//                            intent.putExtra("type", "1");
//                             startActivity(intent);
////

                            }
                        }).show();
            } else {
                if (mAccount.getId().equals(od.driver)) {
                    new AlertDialog.Builder(this)
                            .setMessage("您确定要接单吗？抢单成功后便可获得运输指令，方便联系货主尽快完成运输")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                                    Date curDate = new Date(System.currentTimeMillis());
                                    String str = formatter.format(curDate);


                                    try {
                                        mPresenter.getCodeButton(od.consignorPhone, "3", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), "暂无物流公司", mAccount.phone);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    mPresenter.grapOrder(mId);

                                }
                            }).show();
                    refresh();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("该订单已被其他司机接单，请尝试其他订单！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, TabActivity.class);
                                    intent.putExtra("title", "找货");
                                    startActivity(intent);
                                }
                            }).show();

                }
            }
        } else if (id == R.id.detailUploadBtn) {//上传图片
            boolean canUpload = false;
            for (Photo photo : mImagePaths) {
                if (photo.isCanDelete()) {
                    canUpload = true;
                    break;
                }
            }
            if (canUpload) {
                mOperationDialog = LoadingProgress.showProgress(this, "正在提交...");
                mPresenter.uploadImages(mId, mImagePaths);
            } else {
                showToast("请先选择图片后再上传");
            }
        }
    }

    @OnClick({R.id.wxPayLayout, R.id.aliPayLayout})
    public void pay(View v) {
        if (v.getId() == R.id.wxPayLayout) {
            wxPay();
        } else if (v.getId() == R.id.aliPayLayout) {

        }
    }

    private void wxPay() {
        Map<String, String> params = new HashMap<>();
        params.put("appname", AppUtils.getAppName());
        params.put("product_name", od.productName);
        params.put("describe", od.weight + "吨" + od.area + "方");
        params.put("orderid", mId);
        params.put("type", "1");
        params.put("price", od.price + "");

        mPayDialog = LoadingProgress.showProgress(this, "正在发起支付...");
        mPresenter.pay(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == GPS_REQUEST_CODE) {
            openGPSSEtting();
        }

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
                    photo.setCanDelete(true);

                    photo.setPath(lm.getCompressPath());
                    mImagePaths.add(photo);
                } else {
                    Photo photo = new Photo();
                    photo.setCanDelete(true);
                    photo.setPath(lm.getPath());
                    mImagePaths.add(photo);
                }
            }

            mPhotoView.setDatas(mImagePaths);
        }
    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {
        //mOperationDialog.dismiss();
        OrderDetail od = entity.getData();
//
//        Intent intent = new Intent(mContext, GPSNaviActivity.class);
//
//        if ("1".equals(od.category)) {
//            intent.putExtra("x", od.startLat);
//            intent.putExtra("y", od.startLon);
//            intent.putExtra("xxx", od.endLat);
//            intent.putExtra("yyy", od.endLon);
//
//
//        } else if ("2".equals(od.category)) {
//            intent.putExtra("x", od.endLat);
//            intent.putExtra("y", od.endLon);
//            intent.putExtra("xxx", od.startLat);
//            intent.putExtra("yyy", od.startLon);
//        }
//
//        startActivity(intent);
    }

    @Override
    public void onNavError(Throwable e) {
        // mOperationDialog.dismiss();
        showToast("导航失败");
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {
        // mOperationDialog.dismiss();
        showToast("取消订单成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onCancelOrderError(Throwable e) {
        // mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onCancelOrdertixingSuccess(BaseEntity<OrderDetail> entity) {
        //  mOperationDialog.dismiss();
        showToast("操作成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onCancelOrdertixingError(Throwable e) {
        //mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {
//        showToast("提示成功");
//        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//        Intent intent = new Intent(this, DriverOrderActivity.class);
//        intent.putExtra("position", 1);
//        startActivity(intent);
//        finish();

        showToast("提交成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        ActivityCollect.getAppCollect().finishAllNotHome();
        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);
    }

    @Override
    public void onCancelDriverkaishiyunshuError(Throwable e) {

    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {
        //mOperationDialog.dismiss();
//        showToast("提交成功");
//
//        mPresenter.getOrderDetail(mId);
//        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//        Intent intent = new Intent(this, DriverOrderActivity.class);
//        intent.putExtra("position", 2);
//        startActivity(intent);
//        finish();

        showToast("提交成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        ActivityCollect.getAppCollect().finishAllNotHome();
        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("position", 2);
        startActivity(intent);
    }

    @Override
    public void onConfirmOrderError(Throwable e) {
        // mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmqianOrderSuccess(BaseEntity<OrderDetail> entity) {
        showToast("提示成功");

        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);
        finish();
    }

    @Override

    public void onConfirmqianOrderError(Throwable e) {

    }

    @Override
    public void onLocationDriverSuccess(BaseEntity<OrderDetail> entity) {
        //mOperationDialog.dismiss();


        new AlertDialog.Builder(this)
                .setMessage(entity.getMsg() + "16飕云")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();


        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onLocationDriverError(Throwable e) {
        //mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onTransSuccess(BaseEntity<OrderDetail> entity) {
        // mOperationDialog.dismiss();
        handleError(entity);
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());

        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);

        finish();
    }

    @Override
    public void onTransError(Throwable e) {
        //mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onHitSuccess(BaseEntity<OrderDetail> entity) {
//        handleError(entity);
//        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//        finish();
    }

    @Override
    public void onHitError(Throwable e) {
        //showNetworkError();
    }

    @Override
    public void onGrapOrderSuccess(BaseEntity<OrderDetail> entity) {
        //mOperationDialog.dismiss();

//        OrderDetail od = entity.getData();
//        if (!TextUtils.isEmpty(od.orderStatus) && od.orderStatus.equals("0")) {
//            new AlertDialog.Builder(this)
//                    .setMessage(entity.getMsg())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }).show();
//        } else {
//
//            if (od.category.equals("6")) {
//                mPresenter.getOrderDetail(mId);
//                startActivity(new Intent(this, GrapSuccessActivity.class));
//                finish();
//            } else {
//                mPresenter.getOrderDetail(mId);
//            }
//        }
//        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//        EventBus.getDefault().post(new DriverListRrefreshEvent());


        Intent intent = new Intent(this, GrapSuccessActivity.class);
        intent.putExtra("mid", mId);
        startActivity(intent);
        finish();

    }

    @Override
    public void onGrapOrderError(Throwable e) {
        //mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onGrappwdOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onGrappwdOrderError(Throwable e) {

    }

    @Override
    public void onUploadImagesSuccess(BaseEntity<OrderDetail> entity) {
        if(mOperationDialog!=null && mOperationDialog.isShowing())
            mOperationDialog.dismiss();
        mPresenter.getOrderDetail(mId);
    }

    @Override
    public void onUploadImagesError(BaseEntity e) {
        if(mOperationDialog!=null && mOperationDialog.isShowing())
            mOperationDialog.dismiss();
        showToast(e.getMsg());
    }

    @Override
    public void onUploadImagesError(Throwable e) {
        if(mOperationDialog!=null && mOperationDialog.isShowing())
            mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {
        MsgCode mc = entity.getData();
        showToast("短信已发送");
    }

    @Override
    public void onGetCodeError(Throwable e) {
        showToast("发送失败，请重试");
    }

    @Override
    public void oncalculateDistanceSuccess(BaseEntity<Distance> entity) {
        if(isRangeDialog != null && isRangeDialog.isShowing())
            isRangeDialog.dismiss();
        Distance distance = entity.getData();
        chaju = distance.distance;
        //这个目前在运输中到已完成点击后自动操作的步骤
        if ("2".equals(ddzhuangtais)) {
//            if (Double.parseDouble(chaju) * 1000 <= Double.parseDouble(canshu)) {
            if (Double.parseDouble(chaju) <= Double.parseDouble(canshu)) {
                new AlertDialog.Builder(this)
                        .setMessage("运输中转已完成：您已到达指定目的地区域，请确认是否完成本单运输。注：运费将在货主支付完成后，支付至余额中，请确认查收。")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                                Date curDate = new Date(System.currentTimeMillis());
//                                String str = formatter.format(curDate);
                                try {
                                    mPresenter.getCodeButton("4", URLDecoder.decode(od.startPos, "UTF-8"), URLDecoder.decode(od.endPos, "UTF-8"), od.ddbh, URLDecoder.decode(mAccount.name, "UTF-8"), od.consignorPhone);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                mConfirmClick = true;
//                                mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                mPresenter.confirmOrder(mId);
                            }
                        }).show();

//                EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//                Intent intent = new Intent(mContext, DriverOrderActivity.class);
//                intent.putExtra("position", 2);
//                startActivity(intent);
//                finish();
            } else {
                float juli = DecimalUtil.subtract(Float.parseFloat(chaju),Float.parseFloat(canshu));
                new AlertDialog.Builder(this)
                        .setMessage("距离目地的还有"+juli+"公里，到达目的地3公里范围内，便可点击完成订单。")
                        .setPositiveButton("确定", null).show();
            }
        } else if ("1".equals(ddzhuangtais)) {
            //这个目前是从已接单到运输中
//            if (Double.parseDouble(chaju) * 1000 <= Double.parseDouble(canshu)) {
            if (Double.parseDouble(chaju) <= Double.parseDouble(canshu)) {
                new AlertDialog.Builder(this)
                        .setMessage("已接单转运输中：您已到达指定地点区域，请确认是否开始运输。")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                mPresenter.trans(mId);
                            }
                        }).show();
//                EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
//                Intent intent = new Intent(mContext, DriverOrderActivity.class);
//                intent.putExtra("position", 1);
//                startActivity(intent);
//                finish();
            } else {
                float juli = DecimalUtil.subtract(Float.parseFloat(chaju),Float.parseFloat(canshu));
                new AlertDialog.Builder(this)
                        .setMessage("距离目地的还有"+juli+"公里，到达目的地3公里范围内，便可点击完成订单。")
                        .setPositiveButton("确定", null).show();
            }
        }

    }

    @Override
    public void oncalculateDistanceError(BaseEntity entity) {
        if(isRangeDialog != null && isRangeDialog.isShowing())
            isRangeDialog.dismiss();
        showToast(entity.getMsg());
    }

    @Override
    public void oncalculateDistanceError(Throwable e) {
        if(isRangeDialog != null && isRangeDialog.isShowing())
            isRangeDialog.dismiss();
        showNetworkError();
    }

    //这个是自动收货的设置点
    @Override
    public void onOrderDetailReceivingSuccess(BaseEntity<OrderDetail> entity) {


    }

    @Override
    public void onOrderDetailReceivingError(Throwable e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);

        }
        mHandler.removeCallbacksAndMessages(null);

        if(mapView != null)
            mapView.onDestroy();

    }


    protected void hongbao() {

        RedPacketEntity entitys = new RedPacketEntity("16飕云", "http://www.16souyun.com/xcx/hongbao.png", "订单完成，红包返现");
        showRedPacketDialog(entitys);
        mHomeIntroRv.setFocusableInTouchMode(false);
        mHomeIntroRv.setHasFixedSize(true);
        mHomeIntroRv.setNestedScrollingEnabled(false);
        mHomeServiceRv.setFocusableInTouchMode(false);
        mHomeServiceRv.setHasFixedSize(true);
        mHomeServiceRv.setNestedScrollingEnabled(false);
        mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
        mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                showToast("付款成功");

                mPresenter.getOrderDetail(mId);
                //                finish();
            } else {
                showToast("付款失败");
            }
        }

    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            //Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this).setTitle("请打开定位权限")
                    .setMessage("打开位置信息")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(DriverOrderDetailActivity.this, "close", Toast.LENGTH_SHORT).show();
                            // 关闭dialog
                            // dialogInterface.dismiss();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    /**
     * 刷新, 这样的刷新方法，仅仅有一个Activity实例。
     */
    public void refresh() {
        onCreate(null);
    }


    /****
     * 起点终点图标设置
     */
//    private void setfromandtoMarker() {
//        aMap.addMarker(new MarkerOptions()
//                .position(AMapUtil.convertToLatLng(mStartPoint))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
//        aMap.addMarker(new MarkerOptions()
//                .position(AMapUtil.convertToLatLng(mEndPoint))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
//    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }


    /**
     * 开始搜索路线规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(this, "起点未设置", Toast.LENGTH_LONG).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(this, "终点未设置", Toast.LENGTH_LONG).show();
        }
        //     showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {
//            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
//            mRouteSearch.calculateWalkRouteAsyn(query);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "");
            mRouteSearch.calculateDriveRouteAsyn(query);
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath walkPath = mDriveRouteResult.getPaths()
                            .get(0);
                    DriveRouteOverlay walkRouteOverlay = new DriveRouteOverlay(
                            this, aMap, walkPath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos());
                    walkRouteOverlay.getWalkColor();//轨迹颜色修改
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(this, R.string.no_result, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.no_result, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.getWalkColor();//轨迹颜色修改
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(this, R.string.no_result, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.no_result, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            //progDialog.dismiss();
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }
}
