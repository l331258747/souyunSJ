//package com.xrwl.driver.module.home.ui;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//
//import com.bumptech.glide.Glide;
//import com.ldw.library.bean.BaseEntity;
//import com.ldw.library.view.dialog.LoadingProgress;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.xrwl.driver.R;
//import com.xrwl.driver.base.BaseActivity;
//import com.xrwl.driver.bean.Auth;
//import com.xrwl.driver.module.home.mvp.DriverAuthContract;
//import com.xrwl.driver.module.home.mvp.DriverAuthPresenter;
//import com.xrwl.driver.module.tab.activity.TabActivity;
//
//import android.text.TextWatcher;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.Flowable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Action;
//import io.reactivex.functions.Consumer;
//
//import java.io.ByteArrayOutputStream;
//import java.util.concurrent.TimeUnit;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//import android.widget.TextView;
//
///**
// * 司机实名认证
// * Created by www.longdw.com on 2018/4/4 下午10:29.
// */
//public class DriverAuthActivity extends BaseActivity<DriverAuthContract.IView, DriverAuthPresenter> implements DriverAuthContract
//        .IView {
//
//    public static final int COUNT_DOWN = 59;
//    private long firstTime=0;
//    private static Handler handler = new Handler();
//
//    private long TIME_LISTEN = 1000;//设置2秒的延迟
//    private ProgressDialog mGetCodeDialog;
//    private Disposable mDisposable;
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    doSomeThing(mNameEt.getText().toString(),mshenfenzhengEt.getText().toString(),mchepaihaomaEt.getText().toString(),mhedingzaizhiliangEt.getText().toString(),myingyunzhenghaomaEt.getText().toString(),mcheliangzhoushuEt.getText().toString(),mjiashizhenghaomaEt.getText().toString(),mzigezhenghaomaEt.getText().toString());
//                }
//            });
//        }
//    };
//
//
//    public static final int RESULT_ID = 100;
//    public static final int RESULT_AVATAR = 200;
//    public static final int RESULT_DRIVER = 300;
//    public static final int RESULT_BOOK = 400;
//    public static final int RESULT_CAR = 500;
//     private String canshu;
//    @BindView(R.id.authCarpicIv)
//    ImageView mauthCarpicIv;//车辆照片
//
//
//    @BindView(R.id.authIdIv)
//    ImageView mIdIv;//身份证
//    @BindView(R.id.authNameEt)
//    EditText mNameEt;//姓名
//    @BindView(R.id.authAvatarIv)
//    ImageView mAvatarIv;//本人照片
//
//    @BindView(R.id.authDriverIv)
//    ImageView mDriverIv;//驾驶证
//    @BindView(R.id.authBookIv)
//    ImageView mBookIv;//行车本
//
//    @BindView(R.id.authConfirmBtn)
//    Button mConfirmBtn;
//
//    @BindView(R.id.authSpinner)
//    Spinner mAuthSpinner;
//
//    @BindView(R.id.carSp)
//    Spinner mcarSp;
//
//
//    @BindView(R.id.tellEt)
//    EditText mtellEt;//电话
//
//    Button mCodeBtn;
//    private String mCode;
//    @BindView(R.id.shenfenzhengEt)
//    EditText mshenfenzhengEt;//姓名
//    @BindView(R.id.chepaihaomaEt)
//    EditText mchepaihaomaEt;//姓名
//    @BindView(R.id.hedingzaizhiliangEt)
//    EditText mhedingzaizhiliangEt;//姓名
//    @BindView(R.id.yingyunzhenghaomaEt)
//    EditText myingyunzhenghaomaEt;//姓名
//    @BindView(R.id.cheliangzhoushuEt)
//    EditText mcheliangzhoushuEt;//姓名
//    @BindView(R.id.jiashizhenghaomaEt)
//    EditText mjiashizhenghaomaEt;//姓名
//    @BindView(R.id.zigezhenghaomaEt)
//    EditText mzigezhenghaomaEt;//姓名
//
//
//    @BindView(R.id.discarSp)
//    TextView mdiscarsp;//姓名
//
//
//    private String mIdPath, mAvatarPath, mDriverPath, mBookPath,mCarPath;
//    private ProgressDialog mGetDialog;
//    private ProgressDialog mPostDialog;
//    private String mCategory;
//
//
//    @BindView(R.id.jiashiRlay)
//    RelativeLayout mjiashiRlay;
//
//    @BindView(R.id.cheLay)
//    LinearLayout mcheLay;
//
//
//
//    @BindView(R.id.chepaihaomaly)
//    LinearLayout mchepaihaomaly;
//
//
//    @BindView(R.id.cheliangzhaopianly)
//    LinearLayout mcheliangzhaopianly;
//
//
//
//    @BindView(R.id.xiayibu)
//    Button mxiayibu;
//
//
//
//
//    //实名认证隐藏和显示的ly
//    @BindView(R.id.topLY)
//    LinearLayout mtoply;
//    @BindView(R.id.toprzLY)
//    LinearLayout mtoprzLY;
//
//
//    @BindView(R.id.diyiLY)
//    LinearLayout mdiyily;
//    @BindView(R.id.dierLY)
//    LinearLayout mdierly;
//    @BindView(R.id.disanLY)
//    LinearLayout mdisanly;
//    @BindView(R.id.disiLY)
//    LinearLayout mdisily;
//
//    @BindView(R.id.xuantian)
//    LinearLayout mxuantian;
//
//
//    @BindView(R.id.diyiduiyingLY)
//    LinearLayout mdiyiduiyingly;
//    @BindView(R.id.dierduiyingLY)
//    LinearLayout mdierduiyingLY;
//    @BindView(R.id.disanduiyingLY)
//    LinearLayout mdisanduiyingLY;
//    @BindView(R.id.disiduiyingLY)
//    LinearLayout mdisiduiyingLY;
//    @BindView(R.id.diyiweiwanshanbt)
//    Button mdiyiweiwanshanbt;
//    @BindView(R.id.dierweiwanshanbt)
//    Button mdierweiwanshanbt;
//    @BindView(R.id.disanweiwanshanbt)
//    Button mdisanweiwanshanbt;
//    @BindView(R.id.disiweiwanshanbt)
//    Button mdisiweiwanshanbt;
//   @BindView(R.id.jiantoufanhui)
//    Button mjiantoufanhui;
//     //单独返回键
//    @BindView(R.id.fanhuijian)
//    ImageView mfanhuijian;
//    @BindView(R.id.fanhuizhujiemian)
//    LinearLayout mfanhuizhujiemian;
//
//    @BindView(R.id.neibufanhuijiemian)
//    LinearLayout mneibufanhuijiemian;
//
//    @BindView(R.id.zhaopianyi)
//    Button mzhaopianyi;
//
//
//    @BindView(R.id.zhaopianer)
//    Button mzhaopianer;
//
//
//    @BindView(R.id.aliyun)
//    TextView maliyun;
//
//    @BindView(R.id.dagou)
//    CheckBox mCheckBox;
//    @Override
//    protected DriverAuthPresenter initPresenter() {
//        return new DriverAuthPresenter(this);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.driver_auth_activity;
//    }
//
//    @Override
//    protected void initViews() {
//        mzhaopianyi.setVisibility(View.GONE);
//        mzhaopianer.setVisibility(View.GONE);
//        mtoprzLY.setVisibility(View.GONE);
//        mfanhuizhujiemian.setVisibility(View.VISIBLE);
//        mneibufanhuijiemian.setVisibility(View.GONE);
//        mjiantoufanhui.setVisibility(View.GONE);
//        mtoply.setVisibility(View.VISIBLE);
//        mdiyiduiyingly.setVisibility(View.INVISIBLE);
//        mdierduiyingLY.setVisibility(View.INVISIBLE);
//        mdisanduiyingLY.setVisibility(View.INVISIBLE);
//        mdisiduiyingLY.setVisibility(View.INVISIBLE);
//        mCheckBox.setChecked(false);
//        mxuantian.setVisibility(View.INVISIBLE);
//
////        mjiashiRlay.setVisibility(View.VISIBLE);
////        mcheLay.setVisibility(View.GONE);
//        mAuthSpinner.setAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
//                new String[]{ "同城零担","同城专车","长途整车", "长途零担" , "大宗货物"}));
//        mAuthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mCategory = (position) + "";
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        getData();
//    }
//
//    @Override
//    protected void getData() {
//
//        mNameEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//     mshenfenzhengEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//        mchepaihaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mhedingzaizhiliangEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        myingyunzhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mcheliangzhoushuEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mjiashizhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//        mzigezhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//       // mGetDialog = LoadingProgress.showProgress(this, "正在加载...");
//        mPresenter.getData();
//    }
//    //执行操作
//    private void doSomeThing(String usernames,String invitePhones,String chepaihaoma,String hedingzaizhiliang,String yingyunzhenghaoma,String cheliangzhoushu,String jiashizhenghaoma,String zigezhenghaoma) {
//        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        String usernamesa="";
//        try {
//            usernamesa= URLEncoder.encode(usernames,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String invitePhonesa="";
//        try {
//            invitePhonesa= URLEncoder.encode(invitePhones,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//
//        String chepaihaomaa="";
//        try {
//            chepaihaomaa= URLEncoder.encode(chepaihaoma,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String hedingzaizhilianga="";
//        try {
//            hedingzaizhilianga= URLEncoder.encode(hedingzaizhiliang,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String yingyunzhenghaomaa="";
//        try {
//            yingyunzhenghaomaa= URLEncoder.encode(yingyunzhenghaoma,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        String cheliangzhoushua="";
//        try {
//            cheliangzhoushua= URLEncoder.encode(cheliangzhoushu,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//        String jiashizhenghaomaa="";
//        try {
//            jiashizhenghaomaa= URLEncoder.encode(jiashizhenghaoma,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//        String zigezhenghaomaa="";
//        try {
//            zigezhenghaomaa= URLEncoder.encode(zigezhenghaoma,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Map<String, String> picMaps = new HashMap<>();
//        Map<String, String> params = new HashMap<>();
//        params.put("check", "0");
//        params.put("username",usernamesa);
//        params.put("userid",mAccount.getId());
//        params.put("invitePhones",invitePhonesa);
//
//        params.put("chepaihaoma",chepaihaomaa);
//        params.put("hedingzaizhiliang",hedingzaizhilianga);
//        params.put("yingyunzhenghaoma",yingyunzhenghaomaa);
//        params.put("cheliangzhoushu",cheliangzhoushua);
//        params.put("jiashizhenghaoma",jiashizhenghaomaa);
//        params.put("zigezhenghaoma",zigezhenghaomaa);
//
//        mPresenter.postputongData(params);
//    }
//
//
//
//    @OnClick({R.id.authConfirmBtn,R.id.xiayibu,R.id.diyiweiwanshanbt,R.id.dierweiwanshanbt,R.id.disanweiwanshanbt,R.id.disiweiwanshanbt,R.id.fanhuijian,R.id.loginCodeBtn,R.id.jiantoufanhui,R.id.fanhuizhujiemian,R.id.neibufanhuijiemian,R.id.zhaopianyi,R.id.zhaopianer,R.id.dagou})
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.authConfirmBtn) {
//            checkPost();
//        }
//        else if(id==R.id.dagou)
//        {
//            if (mCheckBox.isChecked()) {
//                mxuantian.setVisibility(View.VISIBLE);
//            } else {
//                mxuantian.setVisibility(View.GONE);
//            }
//        }
//        else if(id==R.id.xiayibu)
//        {
//            mjiashiRlay.setVisibility(View.GONE);
//            mcheLay.setVisibility(View.VISIBLE);
//        }
//        else if(id==R.id.diyiweiwanshanbt){
//            maliyun.setVisibility(View.VISIBLE);
//            mzhaopianyi.setVisibility(View.INVISIBLE);
//            mzhaopianer.setVisibility(View.INVISIBLE);
//            mfanhuizhujiemian.setVisibility(View.GONE);
//            mneibufanhuijiemian.setVisibility(View.VISIBLE);
//            mtoply.setVisibility(View.GONE);
//            mtoprzLY.setVisibility(View.GONE);
//            mjiantoufanhui.setVisibility(View.VISIBLE);
//            mdiyiduiyingly.setVisibility(View.VISIBLE);
//            mdierduiyingLY.setVisibility(View.GONE);
//            mdisanduiyingLY.setVisibility(View.GONE);
//            mdisiduiyingLY.setVisibility(View.GONE);
//            mdiyily.setVisibility(View.GONE); mdierly.setVisibility(View.GONE); mdisanly.setVisibility(View.GONE);mdisily.setVisibility(View.GONE);
//        }
//        else if(id==R.id.dierweiwanshanbt){
//            maliyun.setVisibility(View.VISIBLE);
//            mzhaopianyi.setVisibility(View.VISIBLE);
//            mzhaopianer.setVisibility(View.GONE);
//            mfanhuizhujiemian.setVisibility(View.GONE);
//            mneibufanhuijiemian.setVisibility(View.VISIBLE);
//            mtoply.setVisibility(View.GONE);
//            mtoprzLY.setVisibility(View.GONE);
//            mjiantoufanhui.setVisibility(View.VISIBLE);
//            mdiyiduiyingly.setVisibility(View.GONE);
//            mdierduiyingLY.setVisibility(View.VISIBLE);
//            mdisanduiyingLY.setVisibility(View.GONE);
//            mdisiduiyingLY.setVisibility(View.GONE);
//            mdiyily.setVisibility(View.GONE); mdierly.setVisibility(View.GONE); mdisanly.setVisibility(View.GONE);mdisily.setVisibility(View.GONE);
//        }
//        else if(id==R.id.disanweiwanshanbt){
//            maliyun.setVisibility(View.VISIBLE);
//            mzhaopianyi.setVisibility(View.GONE);
//            mzhaopianer.setVisibility(View.VISIBLE);
//            mfanhuizhujiemian.setVisibility(View.GONE);
//            mneibufanhuijiemian.setVisibility(View.VISIBLE);
//            mtoply.setVisibility(View.GONE);
//            mtoprzLY.setVisibility(View.GONE);
//            mjiantoufanhui.setVisibility(View.VISIBLE);
//            mdiyiduiyingly.setVisibility(View.GONE);
//            mdierduiyingLY.setVisibility(View.GONE);
//            mdisanduiyingLY.setVisibility(View.VISIBLE);
//            mdisiduiyingLY.setVisibility(View.GONE);
//            mdiyily.setVisibility(View.GONE); mdierly.setVisibility(View.GONE); mdisanly.setVisibility(View.GONE);mdisily.setVisibility(View.GONE);
//        }
//        else if(id==R.id.disiweiwanshanbt){
//
//            String chexing="";
//            try {
//                chexing= URLDecoder.decode(mcarSp.getSelectedItem().toString(),"UTF-8");
//                //showToast(chexing);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
////           if("跑腿".equals(chexing))
////           {
////               maliyun.setVisibility(View.VISIBLE);
////               mzhaopianyi.setVisibility(View.INVISIBLE);
////               mzhaopianer.setVisibility(View.INVISIBLE);
////               mfanhuizhujiemian.setVisibility(View.GONE);
////               mneibufanhuijiemian.setVisibility(View.VISIBLE);
////               mtoply.setVisibility(View.GONE);
////               mtoprzLY.setVisibility(View.GONE);
////               mjiantoufanhui.setVisibility(View.VISIBLE);
////               mdiyiduiyingly.setVisibility(View.GONE);
////               mdierduiyingLY.setVisibility(View.GONE);
////               mdisanduiyingLY.setVisibility(View.GONE);
////               mdisiduiyingLY.setVisibility(View.VISIBLE);
////               mCheckBox.setVisibility(View.GONE);
////               mchepaihaomaly.setVisibility(View.GONE);
////               mdiyily.setVisibility(View.GONE); mdierly.setVisibility(View.GONE); mdisanly.setVisibility(View.GONE);mdisily.setVisibility(View.GONE);
////           }
////           else
////           {
//               maliyun.setVisibility(View.VISIBLE);
//               mzhaopianyi.setVisibility(View.INVISIBLE);
//               mzhaopianer.setVisibility(View.INVISIBLE);
//               mfanhuizhujiemian.setVisibility(View.GONE);
//               mneibufanhuijiemian.setVisibility(View.VISIBLE);
//               mtoply.setVisibility(View.GONE);
//               mtoprzLY.setVisibility(View.GONE);
//               mjiantoufanhui.setVisibility(View.VISIBLE);
//               mdiyiduiyingly.setVisibility(View.GONE);
//               mdierduiyingLY.setVisibility(View.GONE);
//               mdisanduiyingLY.setVisibility(View.GONE);
//               mdisiduiyingLY.setVisibility(View.VISIBLE);
//               mdiyily.setVisibility(View.GONE); mdierly.setVisibility(View.GONE); mdisanly.setVisibility(View.GONE);mdisily.setVisibility(View.GONE);
//         // }
//
//        }
//        else if(id==R.id.fanhuijian)
//        {
//            startActivity(new Intent(mContext, TabActivity.class));
//        }
//        else if(id==R.id.loginCodeBtn)
//        {
//            getCode();
//        }
//        else if(id==R.id.jiantoufanhui)
//        {
//            String chexing="";
//            try {
//                chexing= URLEncoder.encode(mcarSp.getSelectedItem().toString(),"UTF-8");
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            Map<String, String> params = new HashMap<>();
//            params.put("userid",mAccount.getId());
//
//            params.put("chexing",chexing);//车型上传
//
//            mPresenter.postputongchexingData(params);
//            startActivity(new Intent(mContext, DriverAuthActivity.class));
//        }
//        else if(id==R.id.fanhuizhujiemian)
//        {
//            startActivity(new Intent(mContext, TabActivity.class));
//        }
//        else if(id==R.id.neibufanhuijiemian)
//        {
//            startActivity(new Intent(mContext, DriverAuthActivity.class));
//        }
//        else if(id==R.id.zhaopianyi)
//        {
//            checkPost();
//        }
//        else if(id==R.id.zhaopianer)
//        {
//            checkPostzhizhao();
//        }
//    }
//    public void getCode() {
//        String phone = mtellEt.getText().toString();
//        if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
//            showToast("请输入正确的手机号码");
//            return;
//        }
//       mPresenter.getCode(phone);
//       mDisposable =  Flowable.intervalRange(0, COUNT_DOWN, 0, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        String msg = COUNT_DOWN - aLong + "后重新获取";
//                       // mCodeBtn.setText(msg);
//                    }
//                })
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                       // mCodeBtn.setEnabled(true);
//                       // mCodeBtn.setText("获取验证码");
//                    }
//                })
//                .subscribe();
//    }
//    /**
//     * 将图片转换成Base64编码的字符串
//     * @param path
//     * @return base64编码的字符串
//     */
//    public static String imageToBase64(String path){
//        if(TextUtils.isEmpty(path)){
//            return null;
//        }
//        InputStream is = null;
//        byte[] data = null;
//        String result = null;
//        try{
//            is = new FileInputStream(path);
//            //创建一个字符流大小的数组。
//            data = new byte[is.available()];
//            //写入数组
//            is.read(data);
//            //用默认的编码格式进行编码
//            result = Base64.encodeToString(data,Base64.NO_WRAP);
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            if(null !=is){
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return result;
//    }
//
//
//    private void checkPost() {
//        Map<String, String> picMaps = new HashMap<>();
//        Map<String, String> params = new HashMap<>();
//        if (TextUtils.isEmpty(mIdPath)) {
//            showToast("请上传身份证照片");
//            return;
//        }
//        if (TextUtils.isEmpty(mAvatarPath)) {
//            showToast("请上传身份证反面");
//            return;
//        }
//
//
//        if (TextUtils.isEmpty(mDriverPath)) {
//            showToast("请上传驾驶证");
//            return;
//        }
//
//
//        picMaps.put("pic_id", mIdPath);
//        picMaps.put("pic_avatar", mAvatarPath);
//        picMaps.put("pic_drive", mDriverPath);
//        params.put("type", "10");
//
//        //mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
//        mPresenter.postData(picMaps, params);
//
//
//    }
//
//
//    private void checkPostzhizhao() {
//        Map<String, String> picMaps = new HashMap<>();
//        Map<String, String> params = new HashMap<>();
//
//
//
//        if (TextUtils.isEmpty(mBookPath)) {
//            showToast("请上传行车本");
//            return;
//        }
//        picMaps.put("pic_train", mBookPath);
//         params.put("type", "20");
//
//        mPresenter.postData(picMaps, params);
//
//
//    }
//
//
//    @OnClick({R.id.authIdIv, R.id.authAvatarIv, R.id.authDriverIv, R.id.authBookIv,R.id.authCarpicIv})
//    public void camera(View v) {
//        int id = v.getId();
//        int result = RESULT_ID;
//        if (id == R.id.authIdIv) {
//            result = RESULT_ID;
//        } else if (id == R.id.authAvatarIv) {
//            result = RESULT_AVATAR;
//        } else if (id == R.id.authDriverIv) {
//            result = RESULT_DRIVER;
//        } else if (id == R.id.authBookIv) {
//            result = RESULT_BOOK;
//        } else if (id == R.id.authCarpicIv) {
//            result = RESULT_CAR;
//        }
//        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
//                .selectionMode(PictureConfig.SINGLE)
//                .previewImage(true)
//                .isCamera(true)
//                .compress(true)
//                .circleDimmedLayer(true)
//                .forResult(result);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//        if (selectList == null || selectList.size() == 0) {
//            return;
//        }
//        LocalMedia lm = selectList.get(0);
//        if (requestCode == RESULT_ID) {
//            if (lm.isCompressed()) {
//                mIdPath = lm.getCompressPath();
//            } else {
//                mIdPath = lm.getPath();
//            }
//
//            Glide.with(mContext).load(Uri.fromFile(new File(mIdPath))).into(mIdIv);
//        } else if (requestCode == RESULT_AVATAR) {
//            if (lm.isCompressed()) {
//                mAvatarPath = lm.getCompressPath();
//            } else {
//                mAvatarPath = lm.getPath();
//            }
//            Glide.with(mContext).load(Uri.fromFile(new File(mAvatarPath))).into(mAvatarIv);
//        } else if (requestCode == RESULT_DRIVER) {
//            if (lm.isCompressed()) {
//                mDriverPath = lm.getCompressPath();
//            } else {
//                mDriverPath = lm.getPath();
//            }
//            Glide.with(mContext).load(Uri.fromFile(new File(mDriverPath))).into(mDriverIv);
//        } else if (requestCode == RESULT_BOOK) {
//            if (lm.isCompressed()) {
//                mBookPath = lm.getCompressPath();
//            } else {
//                mBookPath = lm.getPath();
//            }
//            Glide.with(mContext).load(Uri.fromFile(new File(mBookPath))).into(mBookIv);
//        }
//        else if (requestCode == RESULT_CAR) {
//            if (lm.isCompressed()) {
//                mCarPath = lm.getCompressPath();
//            } else {
//                mCarPath = lm.getPath();
//            }
//            Glide.with(mContext).load(Uri.fromFile(new File(mCarPath))).into(mauthCarpicIv);
//        }
//    }
//
//    @Override
//    public void onPostSuccess(BaseEntity entity) {
//       // mPostDialog.dismiss();
//       // showToast("提交成功");
//       // finish();
//    }
//
//    @Override
//    public void onPostError(BaseEntity entity) {
//       // mPostDialog.dismiss();
//        handleError(entity);
//    }
//
//    @Override
//    public void onPostError(Throwable e) {
//        //mPostDialog.dismiss();
//        showNetworkError();
//    }
//
//    @Override
//    public void onRefreshSuccess(BaseEntity<Auth> entity) {
//       // mGetDialog.dismiss();
//        Auth auth = entity.getData();
//        if (auth != null) {
//
//
//            if((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma)))
//            {
//                mdiyiweiwanshanbt.setText("审核中");
//                mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//            }
//            if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && !TextUtils.isEmpty(auth.picDriver))
//            {
//                mdierweiwanshanbt.setText("审核中");
//                mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//            }
//            if ((!TextUtils.isEmpty(auth.picBook)))
//            {
//                mdisanweiwanshanbt.setText("审核中");
//                mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//            }
//            if ((!TextUtils.isEmpty(auth.chepaihaoma)) && (!TextUtils.isEmpty(auth.hedingzaizhiliang)) && (!TextUtils.isEmpty(auth.yingyunzhenghaoma)) && (!TextUtils.isEmpty(auth.cheliangzhoushu)) && (!TextUtils.isEmpty(auth.zigezhenghaoma)))
//            {
//                 mdisiweiwanshanbt.setText("审核中");
//                 mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//            }
//
//
//            if ("1".equals(auth.review)) {
//
//                mtoply.setVisibility(View.GONE);
//                mtoprzLY.setVisibility(View.VISIBLE);
//                if((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma)))
//                {
//                    mdiyiweiwanshanbt.setText("审核通过");
//                    mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//                }
//                if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && !TextUtils.isEmpty(auth.picDriver))
//                {
//                    mdierweiwanshanbt.setText("审核通过");
//                    mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//                }
//                if ((!TextUtils.isEmpty(auth.picBook)) && (!TextUtils.isEmpty(auth.picBook)))
//                {
//                    mdisanweiwanshanbt.setText("审核通过");
//                    mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//                }
//                if ((!TextUtils.isEmpty(auth.chepaihaoma)) && (!TextUtils.isEmpty(auth.hedingzaizhiliang)) && (!TextUtils.isEmpty(auth.yingyunzhenghaoma)) && (!TextUtils.isEmpty(auth.cheliangzhoushu)) && (!TextUtils.isEmpty(auth.zigezhenghaoma)))
//                {
//                    mdisiweiwanshanbt.setText("审核通过");
//                    mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
//                }
//            }
//
//
//
//
//
//            if(TextUtils.isEmpty(auth.name)||auth.name.length()==0)
//            {
//                mNameEt.setText("");
//            }
//            else
//            {
//                if(auth.name.equals("0"))
//                {
//                    mNameEt.setText("");
//                }
//                else
//                {
//                    try {
//                        mNameEt.setText(URLDecoder.decode(auth.name,"UTF-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//           if(TextUtils.isEmpty(auth.invitePhones)||auth.invitePhones.length()==0)
//            {
//                mshenfenzhengEt.setText("");
//            }
//            else
//            {
//                try {
//                    mshenfenzhengEt.setText(URLDecoder.decode(auth.invitePhones,"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(TextUtils.isEmpty(auth.chepaihaoma)||auth.chepaihaoma.length()==0)
//            {
//                mchepaihaomaEt.setText("");
//            }
//            else
//            {
//                try {
//                    mchepaihaomaEt.setText(URLDecoder.decode(auth.chepaihaoma,"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(TextUtils.isEmpty(auth.hedingzaizhiliang)||auth.hedingzaizhiliang.length()==0)
//            {
//                mhedingzaizhiliangEt.setText("");
//            }
//            else
//            {
//                try {
//                    mhedingzaizhiliangEt.setText(URLDecoder.decode(auth.hedingzaizhiliang,"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(TextUtils.isEmpty(auth.yingyunzhenghaoma)||auth.yingyunzhenghaoma.length()==0)
//           {
//               myingyunzhenghaomaEt.setText("");
//           }
//           else
//           {
//               try {
//                   myingyunzhenghaomaEt.setText(URLDecoder.decode(auth.yingyunzhenghaoma,"UTF-8"));
//               } catch (UnsupportedEncodingException e) {
//                   e.printStackTrace();
//               }
//           }
//            if(TextUtils.isEmpty(auth.cheliangzhoushu)||auth.cheliangzhoushu.length()==0)
//           {
//               mcheliangzhoushuEt.setText("");
//           }
//           else
//           {
//               try {
//                   mcheliangzhoushuEt.setText(URLDecoder.decode(auth.cheliangzhoushu,"UTF-8"));
//               } catch (UnsupportedEncodingException e) {
//                   e.printStackTrace();
//               }
//           }
//
//            if(TextUtils.isEmpty(auth.jiashizhenghaoma)||auth.jiashizhenghaoma.length()==0)
//            {
//                mjiashizhenghaomaEt.setText("");
//            }
//            else
//            {
//                try {
//                    mjiashizhenghaomaEt.setText(URLDecoder.decode(auth.jiashizhenghaoma,"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if(TextUtils.isEmpty(auth.zigezhenghaoma)||auth.zigezhenghaoma.length()==0)
//           {
//               mzigezhenghaomaEt.setText("");
//           }
//           else
//           {
//               try {
//                   mzigezhenghaomaEt.setText(URLDecoder.decode(auth.zigezhenghaoma,"UTF-8"));
//               } catch (UnsupportedEncodingException e) {
//                   e.printStackTrace();
//               }
//           }
//
//          if(TextUtils.isEmpty(auth.renzhengchexing) || null==auth.renzhengchexing)
//            {
//                mcarSp.setVisibility(View.VISIBLE);
//                mdiscarsp.setVisibility(View.GONE);
//            }
//            else
//            {
//                mcarSp.setVisibility(View.GONE);
//                mdiscarsp.setVisibility(View.VISIBLE);
//                try {
//                    mdiscarsp.setText(URLDecoder.decode(auth.renzhengchexing,"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//           mAuthSpinner.setEnabled(false);
//            if ("0".equals(auth.category)) {
//                mAuthSpinner.setSelection(0);
//            } else if ("1".equals(auth.category)) {
//                mAuthSpinner.setSelection(1);
//            } else {
//                mAuthSpinner.setSelection(2);
//            }
//
//            if (!TextUtils.isEmpty(auth.picId)) {//身份证
//                Glide.with(this).load(auth.picId).into(mIdIv);
//            }
//            if (!TextUtils.isEmpty(auth.picAvatar)) {//本人
//                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
//            }
//            if (!TextUtils.isEmpty(auth.picDriver)) {//驾驶证
//                Glide.with(this).load(auth.picDriver).into(mDriverIv);
//            }
//            if (!TextUtils.isEmpty(auth.picBook)) {//行车本
//                Glide.with(this).load(auth.picBook).into(mBookIv);
//            }
//            if (!TextUtils.isEmpty(auth.picCar)) {//车辆照片
//                Glide.with(this).load(auth.picCar).into(mauthCarpicIv);
//            }
//        }
//    }
//
//    @Override
//    public void onRefreshError(Throwable e) {
//        //mGetDialog.dismiss();
//        showNetworkError();
//    }
//
//    @Override
//    public void onError(BaseEntity entity) {
//        handleError(entity);
//       // mGetDialog.dismiss();
//    }
//}

package com.xrwl.driver.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.GongAnAuth;
import com.xrwl.driver.module.home.mvp.DriverAuthContract;
import com.xrwl.driver.module.home.mvp.DriverAuthPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 司机实名认证
 * Created by www.longdw.com on 2018/4/4 下午10:29.
 */
public class DriverAuthActivity extends BaseActivity<DriverAuthContract.IView, DriverAuthPresenter> implements DriverAuthContract
        .IView {

    public static final int COUNT_DOWN = 59;
    private long firstTime = 0;
    private static Handler handler = new Handler();

//    private long TIME_LISTEN = 1000;//设置2秒的延迟
//    private ProgressDialog mGetCodeDialog;
//    private Disposable mDisposable;
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    doSomeThing(mNameEt.getText().toString(),mshenfenzhengEt.getText().toString(),mchepaihaomaEt.getText().toString(),mhedingzaizhiliangEt.getText().toString(),myingyunzhenghaomaEt.getText().toString(),mcheliangzhoushuEt.getText().toString(),mjiashizhenghaomaEt.getText().toString(),mzigezhenghaomaEt.getText().toString());
//                }
//            });
//        }
//    };


    public static final int RESULT_ID = 100;
    public static final int RESULT_AVATAR = 200;
    public static final int RESULT_DRIVER = 300;
    public static final int RESULT_BOOK = 400;
    public static final int RESULT_CAR = 500;
    private String canshu;
    @BindView(R.id.authCarpicIv)
    ImageView mauthCarpicIv;//车辆照片

    @BindView(R.id.authIdIv)
    ImageView mIdIv;//身份证
    @BindView(R.id.authNameEt)
    TextView mNameEt;//姓名
    @BindView(R.id.authAvatarIv)
    ImageView mAvatarIv;//本人照片
    @BindView(R.id.authConfirmBtn)
    Button mConfirmBtn;
    @BindView(R.id.authSpinner)
    Spinner mAuthSpinner;
    @BindView(R.id.carSp)
    Spinner mcarSp;

    //    Button mCodeBtn;
//    private String mCode;
    @BindView(R.id.shenfenzhengEt)
    TextView mshenfenzhengEt;//姓名
    @BindView(R.id.chepaihaomaEt)
    TextView mchepaihaomaEt;//姓名
    @BindView(R.id.hedingzaizhiliangEt)
    EditText mhedingzaizhiliangEt;//姓名
    @BindView(R.id.yingyunzhenghaomaEt)
    EditText myingyunzhenghaomaEt;//姓名
    @BindView(R.id.cheliangzhoushuEt)
    EditText mcheliangzhoushuEt;//姓名
    @BindView(R.id.jiashizhenghaomaEt)
    TextView mjiashizhenghaomaEt;//姓名
    @BindView(R.id.zigezhenghaomaEt)
    EditText mzigezhenghaomaEt;//姓名
    @BindView(R.id.discarSp)
    TextView mdiscarsp;//姓名
    private String mIdPath, mAvatarPath, mDriverPath, mBookPath, mCarPath;
    //    private ProgressDialog mGetDialog;
//    private ProgressDialog mPostDialog;
    private String mCategory;

    @BindView(R.id.chepaihaomaly)
    LinearLayout mchepaihaomaly;
    @BindView(R.id.cheliangzhaopianly)
    LinearLayout mcheliangzhaopianly;
    //实名认证隐藏和显示的ly
    @BindView(R.id.topLY)
    RelativeLayout mtoply;
    @BindView(R.id.toprzLY)
    RelativeLayout mtoprzLY;
    @BindView(R.id.diyiLY)
    LinearLayout mdiyily;
    @BindView(R.id.dierLY)
    LinearLayout mdierly;
    @BindView(R.id.disanLY)
    LinearLayout mdisanly;
    @BindView(R.id.disiLY)
    LinearLayout mdisily;
    @BindView(R.id.xuantian)
    LinearLayout mxuantian;
    @BindView(R.id.diyiweiwanshanbt)
    Button mdiyiweiwanshanbt;
    @BindView(R.id.dierweiwanshanbt)
    Button mdierweiwanshanbt;
    @BindView(R.id.disanweiwanshanbt)
    Button mdisanweiwanshanbt;
    @BindView(R.id.disiweiwanshanbt)
    Button mdisiweiwanshanbt;
    //单独返回键
    @BindView(R.id.fanhuijian)
    ImageView mfanhuijian;

    @BindView(R.id.aliyun)
    TextView maliyun;

    @BindView(R.id.dagou)
    CheckBox mCheckBox;
    @BindView(R.id.authDesTv)
    TextView mauthDesTv;
    @BindView(R.id.authIdIvUn)
    RelativeLayout mauthIdIvUn;
    @BindView(R.id.authAvatarIvUn)
    RelativeLayout mauthAvatarIvUn;
    @BindView(R.id.authDriverIvUn)
    RelativeLayout mauthDriverIvUn;
    @BindView(R.id.authBookIvUn)
    RelativeLayout mauthBookIvUn;
    @BindView(R.id.authDriverIv)
    ImageView mDriverIv;//驾驶证
    @BindView(R.id.authBookIv)
    ImageView mBookIv;//行车本
    @BindView(R.id.jianchengs)
    Spinner mjianchengs;//行车本

    private ProgressDialog mLoadingDialog;
    int postType;//0身份证,1驾驶证，2行驶证，3提交信息
    boolean status;

    @Override
    protected DriverAuthPresenter initPresenter() {
        return new DriverAuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.driver_auth_activity1;
    }

    @Override
    protected void initViews() {

        mtoply.setVisibility(View.VISIBLE);
        mtoprzLY.setVisibility(View.GONE);

        maliyun.setVisibility(View.VISIBLE);

        mauthIdIvUn.setVisibility(View.VISIBLE);
        mIdIv.setVisibility(View.GONE);
        mauthAvatarIvUn.setVisibility(View.VISIBLE);
        mAvatarIv.setVisibility(View.GONE);
        mauthDesTv.setVisibility(View.VISIBLE);

        mDriverIv.setVisibility(View.GONE);
        mauthDriverIvUn.setVisibility(View.VISIBLE);
        mBookIv.setVisibility(View.GONE);
        mauthBookIvUn.setVisibility(View.VISIBLE);

        mxuantian.setVisibility(View.GONE);

        mConfirmBtn.setVisibility(View.VISIBLE);

        mjianchengs.setEnabled(false);

        mCategory = "0";
        mAuthSpinner.setAdapter(new ArrayAdapter<>(this,
                R.layout.my_simple_spinner_dropdown_item, android.R.id.text1,
                new String[]{"同城零担", "同城专车", "长途整车", "长途零担", "大宗货物"}));
        mAuthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (position) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getData();
    }

    @Override
    protected void getData() {
//        mNameEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//        mshenfenzhengEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//        mchepaihaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mhedingzaizhiliangEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        myingyunzhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mcheliangzhoushuEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//
//        mjiashizhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
//
//        mzigezhenghaomaEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                handler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!"".equals(s.toString().trim())) {
//
//                    handler.postDelayed(runnable, TIME_LISTEN);
//
//                }
//            }
//        });
        // mGetDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getData();
    }

    //执行操作
    private void doSomeThing(String usernames, String invitePhones, String chepaihaoma,
                             String hedingzaizhiliang, String yingyunzhenghaoma, String cheliangzhoushu,
                             String jiashizhenghaoma, String zigezhenghaoma,String mCategory) {
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        String usernamesa = "";
        try {
            usernamesa = URLEncoder.encode(usernames, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String invitePhonesa = "";
        try {
            invitePhonesa = URLEncoder.encode(invitePhones, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String chepaihaomaa = "";
        try {
            chepaihaomaa = URLEncoder.encode(chepaihaoma, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String hedingzaizhilianga = "";
        try {
            hedingzaizhilianga = URLEncoder.encode(hedingzaizhiliang, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String yingyunzhenghaomaa = "";
        try {
            yingyunzhenghaomaa = URLEncoder.encode(yingyunzhenghaoma, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String cheliangzhoushua = "";
        try {
            cheliangzhoushua = URLEncoder.encode(cheliangzhoushu, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String jiashizhenghaomaa = "";
        try {
            jiashizhenghaomaa = URLEncoder.encode(jiashizhenghaoma, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String zigezhenghaomaa = "";
        try {
            zigezhenghaomaa = URLEncoder.encode(zigezhenghaoma, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String mCategorya = "";
        try {
            mCategorya = URLEncoder.encode(mCategory, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //------------3
        postType = 3;
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("check", "0");
        params.put("username", usernamesa);
        params.put("userid", mAccount.getId());
        params.put("invitePhones", invitePhonesa);

        params.put("chepaihaoma", chepaihaomaa);
        params.put("hedingzaizhiliang", hedingzaizhilianga);
        params.put("yingyunzhenghaoma", yingyunzhenghaomaa);
        params.put("cheliangzhoushu", cheliangzhoushua);
        params.put("jiashizhenghaoma", jiashizhenghaomaa);
        params.put("zigezhenghaoma", zigezhenghaomaa);
        params.put("category", mCategorya);

        mPresenter.postputongData(params);
    }


    @OnClick({R.id.authConfirmBtn, R.id.fanhuijian, R.id.dagou})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dagou) {
            if (mCheckBox.isChecked()) {
                mxuantian.setVisibility(View.VISIBLE);
            } else {
                mxuantian.setVisibility(View.GONE);
            }
        } else if (id == R.id.fanhuijian) {
            finish();
        }
//        else if (id == R.id.loginCodeBtn) {
//            getCode();
//        }
//        else if (id == R.id.jiantoufanhui) {
//            String chexing = "";
//            try {
//                chexing = URLEncoder.encode(mcarSp.getSelectedItem().toString(), "UTF-8");
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            Map<String, String> params = new HashMap<>();
//            params.put("userid", mAccount.getId());
//
//            params.put("chexing", chexing);//车型上传
//
//            mPresenter.postputongchexingData(params);
//            startActivity(new Intent(mContext, DriverAuthActivity.class));
//        }
//        else if (id == R.id.zhaopianyi) {
//            checkPost();
//        } else if (id == R.id.zhaopianer) {
//            checkPostzhizhao();
//        }
        else if (id == R.id.authConfirmBtn) {
            if(TextUtils.isEmpty(mNameEt.getText().toString()) || TextUtils.isEmpty(mshenfenzhengEt.getText().toString())){
                showToast("请上传身份证");
                return;
            }
            if(TextUtils.isEmpty(mjiashizhenghaomaEt.getText().toString())){
                showToast("请上传驾驶证&行驶证");
                return;
            }
            if(TextUtils.isEmpty(mchepaihaomaEt.getText().toString())){
                showToast("请输入车牌号码");
                return;
            }
            if(TextUtils.isEmpty(mCategory)){
                showToast("请选择配送类型");
            }

            doSomeThing(mNameEt.getText().toString(),mshenfenzhengEt.getText().toString(),mchepaihaomaEt.getText().toString(),
                    mhedingzaizhiliangEt.getText().toString(), myingyunzhenghaomaEt.getText().toString(), mcheliangzhoushuEt.getText().toString(),
                    mjiashizhenghaomaEt.getText().toString(), mzigezhenghaomaEt.getText().toString(),mCategory);

        }
    }

//    public void getCode() {
//        String phone = mtellEt.getText().toString();
//        if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
//            showToast("请输入正确的手机号码");
//            return;
//        }
//        mPresenter.getCode(phone);
//        mDisposable = Flowable.intervalRange(0, COUNT_DOWN, 0, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        String msg = COUNT_DOWN - aLong + "后重新获取";
//                        // mCodeBtn.setText(msg);
//                    }
//                })
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        // mCodeBtn.setEnabled(true);
//                        // mCodeBtn.setText("获取验证码");
//                    }
//                })
//                .subscribe();
//    }

//    /**
//     * 将图片转换成Base64编码的字符串
//     *
//     * @param path
//     * @return base64编码的字符串
//     */
//    public static String imageToBase64(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return null;
//        }
//        InputStream is = null;
//        byte[] data = null;
//        String result = null;
//        try {
//            is = new FileInputStream(path);
//            //创建一个字符流大小的数组。
//            data = new byte[is.available()];
//            //写入数组
//            is.read(data);
//            //用默认的编码格式进行编码
//            result = Base64.encodeToString(data, Base64.NO_WRAP);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return result;
//    }

    public void showLoading(String msg) {
        mLoadingDialog = LoadingProgress.showProgress(this, msg);
    }

    public void dismissLoading(){
        if(mLoadingDialog!=null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    private void checkPost() {
        if (TextUtils.isEmpty(mIdPath))
            return;
        if (TextUtils.isEmpty(mAvatarPath))
            return;


        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_id", mIdPath);
        picMaps.put("pic_avatar", mAvatarPath);
        params.put("type", "10");

        showLoading("上传身份证...");
        postType = 0;
        //------------1
        mPresenter.postData(picMaps, params);

        mPresenter.shenfenzheng(mIdPath,"face_cardimg","1");

    }

    private void checkPostjiashi(){
        if (TextUtils.isEmpty(mDriverPath))
            return;

        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_drive", mDriverPath);
        params.put("type", "10");
        showLoading("上传驾驶证...");
        postType = 1;
        //------------1
        mPresenter.postData(picMaps, params);

        mPresenter.shenfenzheng(mDriverPath,"driverimg","2");
    }


    private void checkPostxingshi() {
        if (TextUtils.isEmpty(mBookPath)) {
            return;
        }
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_train", mBookPath);
        params.put("type", "20");

        showLoading("上传行驶证...");
        postType = 2;
        //------------1
        mPresenter.postData(picMaps, params);

        mPresenter.shenfenzheng(mBookPath,"travelimg","3");

    }


    @OnClick({R.id.authIdIv, R.id.authIdIvUn,
            R.id.authAvatarIv,R.id.authAvatarIvUn,
            R.id.authDriverIv, R.id.authDriverIvUn,
            R.id.authBookIv, R.id.authBookIvUn,
            R.id.authCarpicIv})
    public void camera(View v) {
        if(status) return;
        int id = v.getId();
        int result = RESULT_ID;
        if (id == R.id.authIdIv || id == R.id.authIdIvUn) {
            result = RESULT_ID;
        } else if (id == R.id.authAvatarIv || id == R.id.authAvatarIvUn) {
            result = RESULT_AVATAR;
        } else if (id == R.id.authDriverIv || id == R.id.authDriverIvUn) {
            result = RESULT_DRIVER;
        } else if (id == R.id.authBookIv || id == R.id.authBookIvUn) {
            result = RESULT_BOOK;
        } else if (id == R.id.authCarpicIv) {
            result = RESULT_CAR;
        }
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .compress(true)
                .circleDimmedLayer(true)
                .forResult(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        LocalMedia lm = selectList.get(0);
        if (requestCode == RESULT_ID) {
            if (lm.isCompressed()) {
                mIdPath = lm.getCompressPath();
            } else {
                mIdPath = lm.getPath();
            }

            mauthIdIvUn.setVisibility(View.GONE);
            mIdIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mIdPath).into(mIdIv);
            checkPost();

        } else if (requestCode == RESULT_AVATAR) {
            if (lm.isCompressed()) {
                mAvatarPath = lm.getCompressPath();
            } else {
                mAvatarPath = lm.getPath();
            }
            mauthAvatarIvUn.setVisibility(View.GONE);
            mAvatarIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mAvatarPath).into(mAvatarIv);
            checkPost();
        } else if (requestCode == RESULT_DRIVER) {
            if (lm.isCompressed()) {
                mDriverPath = lm.getCompressPath();
            } else {
                mDriverPath = lm.getPath();
            }
            mauthDriverIvUn.setVisibility(View.GONE);
            mDriverIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mDriverPath).into(mDriverIv);
            checkPostjiashi();
        } else if (requestCode == RESULT_BOOK) {
            if (lm.isCompressed()) {
                mBookPath = lm.getCompressPath();
            } else {
                mBookPath = lm.getPath();
            }
            Glide.with(mContext).load(mBookPath).into(mBookIv);
            mauthBookIvUn.setVisibility(View.GONE);
            mBookIv.setVisibility(View.VISIBLE);
            checkPostxingshi();
        } else if (requestCode == RESULT_CAR) {
            if (lm.isCompressed()) {
                mCarPath = lm.getCompressPath();
            } else {
                mCarPath = lm.getPath();
            }
            Glide.with(mContext).load(mCarPath).into(mauthCarpicIv);
        }
    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        //------------1.1
        showToast("提交成功");
        dismissLoading();

        //------------2
        if(postType == 3){
            getData();
        }
    }

    @Override
    public void onPostError(BaseEntity entity) {
        //------------1.2
        if(postType == 0){
            mauthIdIvUn.setVisibility(View.VISIBLE);
            mIdIv.setVisibility(View.GONE);
            mIdPath = "";
            mauthAvatarIvUn.setVisibility(View.VISIBLE);
            mAvatarIv.setVisibility(View.GONE);
            mAvatarPath = "";
            showToast("上传失败，请重新上传");
        }else if(postType == 1){
            mauthDriverIvUn.setVisibility(View.VISIBLE);
            mDriverIv.setVisibility(View.GONE);
            mDriverPath = "";
            showToast("上传失败，请重新上传");
        }else if(postType == 2){
            mauthBookIvUn.setVisibility(View.VISIBLE);
            mBookIv.setVisibility(View.GONE);
            mBookPath = "";
            showToast("上传失败，请重新上传");
        } else if(postType == 3){
            handleError(entity);
        }

        dismissLoading();
    }

    @Override
    public void onPostError(Throwable e) {
        dismissLoading();
        showNetworkError();
    }

    @Override
    public void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity) {
        GongAnAuth dd = entity.getData();

        //------------2.1
        if(postType == 0){
            mNameEt.setText(dd.name);
            mshenfenzhengEt.setText(dd.num);
        }else if(postType == 1){
            mjiashizhenghaomaEt.setText(dd.num);
        }else if(postType == 2){
            if(dd.plate_num.length() > 0){
                mjianchengs.setSelection(getJianchengPos(dd.plate_num.substring(0,1)));
                mchepaihaomaEt.setText(dd.plate_num.substring(1));
            }
        }
        showToast("验证成功");
    }

    public int getJianchengPos(String str){
        Resources res =getResources();
        String[] city=res.getStringArray(R.array.jiancheng);

        for (int i=0;i<city.length;i++){
            if(city[i].equals(str)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void shenfenzhengError(BaseEntity entity) {
        //------------2.2
        showToast("验证失败，请重新上传");
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        // mGetDialog.dismiss();
        Auth auth = entity.getData();
        if (auth != null) {


            if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                mdiyiweiwanshanbt.setText("审核中");
                mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }
            if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && !TextUtils.isEmpty(auth.picDriver)) {
                mdierweiwanshanbt.setText("审核中");
                mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }
            if ((!TextUtils.isEmpty(auth.picBook))) {
                mdisanweiwanshanbt.setText("审核中");
                mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }
            if ((!TextUtils.isEmpty(auth.chepaihaoma)) && (!TextUtils.isEmpty(auth.hedingzaizhiliang)) && (!TextUtils.isEmpty(auth.yingyunzhenghaoma)) && (!TextUtils.isEmpty(auth.cheliangzhoushu)) && (!TextUtils.isEmpty(auth.zigezhenghaoma))) {
                mdisiweiwanshanbt.setText("审核中");
                mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }


            if ("1".equals(auth.review)) {

                status = true;

                mtoply.setVisibility(View.GONE);
                mtoprzLY.setVisibility(View.VISIBLE);

                mConfirmBtn.setVisibility(View.GONE);

                if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                    mdiyiweiwanshanbt.setText("审核通过");
                    mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);

                    maliyun.setVisibility(View.GONE);
                }
                if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && !TextUtils.isEmpty(auth.picDriver)) {
                    mdierweiwanshanbt.setText("审核通过");
                    mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);

                    mauthDesTv.setVisibility(View.GONE);
                }
                if ((!TextUtils.isEmpty(auth.picBook)) && (!TextUtils.isEmpty(auth.picBook))) {
                    mdisanweiwanshanbt.setText("审核通过");
                    mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }
                if ((!TextUtils.isEmpty(auth.chepaihaoma)) && (!TextUtils.isEmpty(auth.hedingzaizhiliang)) && (!TextUtils.isEmpty(auth.yingyunzhenghaoma)) && (!TextUtils.isEmpty(auth.cheliangzhoushu)) && (!TextUtils.isEmpty(auth.zigezhenghaoma))) {
                    mdisiweiwanshanbt.setText("审核通过");
                    mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);

                    mCheckBox.setVisibility(View.GONE);
                }

                mAuthSpinner.setEnabled(false);
                if ("0".equals(auth.category)) {
                    mAuthSpinner.setSelection(0);
                } else if ("1".equals(auth.category)) {
                    mAuthSpinner.setSelection(1);
                } else {
                    mAuthSpinner.setSelection(2);
                }

                mxuantian.setVisibility(View.VISIBLE);
            }



            if (TextUtils.isEmpty(auth.name) || auth.name.length() == 0) {
                mNameEt.setText("");
            } else {
                if (auth.name.equals("0")) {
                    mNameEt.setText("");
                } else {
                    try {
                        mNameEt.setText(URLDecoder.decode(auth.name, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (TextUtils.isEmpty(auth.invitePhones) || auth.invitePhones.length() == 0) {
                mshenfenzhengEt.setText("");
            } else {
                try {
                    mshenfenzhengEt.setText(URLDecoder.decode(auth.invitePhones, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(auth.chepaihaoma) || auth.chepaihaoma.length() == 0) {
                mchepaihaomaEt.setText("");
            } else {
                try {
                    mchepaihaomaEt.setText(URLDecoder.decode(auth.chepaihaoma, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(auth.hedingzaizhiliang) || auth.hedingzaizhiliang.length() == 0) {
                mhedingzaizhiliangEt.setText("");
            } else {
                try {
                    mhedingzaizhiliangEt.setText(URLDecoder.decode(auth.hedingzaizhiliang, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(auth.yingyunzhenghaoma) || auth.yingyunzhenghaoma.length() == 0) {
                myingyunzhenghaomaEt.setText("");
            } else {
                try {
                    myingyunzhenghaomaEt.setText(URLDecoder.decode(auth.yingyunzhenghaoma, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(auth.cheliangzhoushu) || auth.cheliangzhoushu.length() == 0) {
                mcheliangzhoushuEt.setText("");
            } else {
                try {
                    mcheliangzhoushuEt.setText(URLDecoder.decode(auth.cheliangzhoushu, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (TextUtils.isEmpty(auth.jiashizhenghaoma) || auth.jiashizhenghaoma.length() == 0) {
                mjiashizhenghaomaEt.setText("");
            } else {
                try {
                    mjiashizhenghaomaEt.setText(URLDecoder.decode(auth.jiashizhenghaoma, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (TextUtils.isEmpty(auth.zigezhenghaoma) || auth.zigezhenghaoma.length() == 0) {
                mzigezhenghaomaEt.setText("");
            } else {
                try {
                    mzigezhenghaomaEt.setText(URLDecoder.decode(auth.zigezhenghaoma, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (TextUtils.isEmpty(auth.renzhengchexing) || null == auth.renzhengchexing) {
                mcarSp.setVisibility(View.VISIBLE);
                mdiscarsp.setVisibility(View.GONE);
            } else {
                mcarSp.setVisibility(View.GONE);
                mdiscarsp.setVisibility(View.VISIBLE);
                try {
                    mdiscarsp.setText(URLDecoder.decode(auth.renzhengchexing, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(auth.picId)) {//身份证
                Glide.with(this).load(auth.picId).into(mIdIv);
            }
            if (!TextUtils.isEmpty(auth.picAvatar)) {//本人
                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
            }
            if (!TextUtils.isEmpty(auth.picDriver)) {//驾驶证
                Glide.with(this).load(auth.picDriver).into(mDriverIv);
            }
            if (!TextUtils.isEmpty(auth.picBook)) {//行车本
                Glide.with(this).load(auth.picBook).into(mBookIv);
            }
            if (!TextUtils.isEmpty(auth.picCar)) {//车辆照片
                Glide.with(this).load(auth.picCar).into(mauthCarpicIv);
            }
        }
    }

    @Override
    public void onRefreshError(Throwable e) {
        //mGetDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
        dismissLoading();
        // mGetDialog.dismiss();
    }
}
