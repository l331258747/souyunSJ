
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

    private long firstTime = 0;
    private static Handler handler = new Handler();

    public static final int RESULT_ID = 100;
    public static final int RESULT_AVATAR = 200;
    public static final int RESULT_DRIVER = 300;
    public static final int RESULT_BOOK = 400;
    public static final int RESULT_CAR = 500;

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
    private String mCategory;

    //实名认证隐藏和显示的ly
    @BindView(R.id.topLY)
    RelativeLayout mtoply;
    @BindView(R.id.toprzLY)
    RelativeLayout mtoprzLY;
    @BindView(R.id.xuantian)
    LinearLayout mxuantian;
    @BindView(R.id.diyiweiwanshanbt)
    Button mdiyiweiwanshanbt;
    @BindView(R.id.dierweiwanshanbt)
    Button mdierweiwanshanbt;
    @BindView(R.id.disanweiwanshanbt)
    Button mdisanweiwanshanbt;
    @BindView(R.id.diwuweiwanshanbt)
    Button mdiwuweiwanshanbt;
    @BindView(R.id.disiweiwanshanbt)
    Button mdisiweiwanshanbt;

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
        return R.layout.driver_auth_activity2;
    }

    @Override
    protected void initViews() {

        //个人信息待完善
        mtoply.setVisibility(View.VISIBLE);
        //个人信息已完善
        mtoprzLY.setVisibility(View.GONE);
        //个人信息阿里云提示
        maliyun.setVisibility(View.VISIBLE);

        //身份证
        mauthIdIvUn.setVisibility(View.VISIBLE);
        mIdIv.setVisibility(View.GONE);
        mauthAvatarIvUn.setVisibility(View.VISIBLE);
        mAvatarIv.setVisibility(View.GONE);
        mauthDesTv.setVisibility(View.VISIBLE);

        //驾驶证
        mDriverIv.setVisibility(View.GONE);
        mauthDriverIvUn.setVisibility(View.VISIBLE);

        //行驶证
        mBookIv.setVisibility(View.GONE);
        mauthBookIvUn.setVisibility(View.VISIBLE);

        //选填的
        mxuantian.setVisibility(View.GONE);

        //提交
        mConfirmBtn.setVisibility(View.VISIBLE);

        //spanner 不可选，自动填充
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

        //获取个人信息
        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getData();
    }

    //执行操作
    private void doSomeThing(String usernames, String invitePhones, String chepaihaoma,
                             String hedingzaizhiliang, String yingyunzhenghaoma, String cheliangzhoushu,
                             String jiashizhenghaoma, String zigezhenghaoma, String mCategory) {
        //------------3
        postType = 3;
        Map<String, String> picMaps = new HashMap<>();

        Map<String, String> params = new HashMap<>();
        params.put("check", "0");
        params.put("username", usernames);
        params.put("userid", mAccount.getId());
        params.put("invitePhones", invitePhones);

        params.put("chepaihaoma", chepaihaoma);
        params.put("hedingzaizhiliang", hedingzaizhiliang);
        params.put("yingyunzhenghaoma", yingyunzhenghaoma);
        params.put("cheliangzhoushu", cheliangzhoushu);
        params.put("jiashizhenghaoma", jiashizhenghaoma);
        params.put("zigezhenghaoma", zigezhenghaoma);
        params.put("category", mCategory);

        params.put("zhunjiachexing", dd.vehicle_type);
        params.put("fazhengjiguan", dd.issue_date);
        params.put("youxiaoqizi", dd.start_date);
        params.put("youxiaoqizhi", dd.end_date);
        params.put("cheliangshibiedaima", dd.vin);
        params.put("cheliangnengyuanleixing", "A");
        params.put("cheliangleixing", dd.vehicle_type);
        params.put("guachepaizhao", dd.plate_num);
        params.put("cheliangzongzhiliang", dd.gross_mass);
        params.put("shiyongxingzhi", dd.use_character);

        /**
         todo 驾驶证 行驶证信息
         zhunjiachexing 准驾车型    vehicle_type
         fazhengjiguan 发证日期     issue_date
         youxiaoqizi 有效期自       start_date
         youxiaoqizhi 有效期至      end_date
         cheliangshibiedaima 车辆识别代码     vin
         cheliangnengyuanleixing 车辆能源类型 A
         cheliangleixing 车辆类型           vehicle_type
         guachepaizhao 挂车牌照             plate_num
         cheliangzongzhiliang 车辆总质量    gross_mass
         shiyongxingzhi 使用性质            use_character
         */

        Map<String, String> postParams = new HashMap<>();
        for (String key : params.keySet()) {
            try {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    String encodedParam = URLEncoder.encode(value, "UTF-8");
                    postParams.put(key, encodedParam);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

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
        else if (id == R.id.authConfirmBtn) {
            if (TextUtils.isEmpty(mNameEt.getText().toString()) || TextUtils.isEmpty(mshenfenzhengEt.getText().toString())) {
                showToast("请上传身份证");
                return;
            }
            if (TextUtils.isEmpty(mjiashizhenghaomaEt.getText().toString())) {
                showToast("请上传驾驶证");
                return;
            }
            if (TextUtils.isEmpty(mchepaihaomaEt.getText().toString())) {
                showToast("请上传行驶证");
                return;
            }
            if (TextUtils.isEmpty(mCategory)) {
                showToast("请选择配送类型");
            }

            doSomeThing(mNameEt.getText().toString(), mshenfenzhengEt.getText().toString(), mchepaihaomaEt.getText().toString(),
                    mhedingzaizhiliangEt.getText().toString(), myingyunzhenghaomaEt.getText().toString(), mcheliangzhoushuEt.getText().toString(),
                    mjiashizhenghaomaEt.getText().toString(), mzigezhenghaomaEt.getText().toString(), mCategory);

        }
    }

    //显示对话框
    public void showLoading(String msg) {
        mLoadingDialog = LoadingProgress.showProgress(this, msg);
    }

    //隐藏对话框
    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    //认证身份证
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

        mPresenter.shenfenzheng(mIdPath, "face_cardimg", "1");

        mPresenter.postData(picMaps, params);

    }

    //认证驾驶证
    private void checkPostjiashi() {
        if (TextUtils.isEmpty(mDriverPath))
            return;

        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_drive", mDriverPath);
        params.put("type", "10");
        showLoading("上传驾驶证...");
        postType = 1;
        //------------1

        mPresenter.shenfenzheng(mDriverPath, "driverimg", "2");

        mPresenter.postData(picMaps, params);

    }

    //认证行驶证
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

        mPresenter.shenfenzheng(mBookPath, "travelimg", "3");

        mPresenter.postData(picMaps, params);

    }


    @OnClick({R.id.authIdIv, R.id.authIdIvUn,
            R.id.authAvatarIv, R.id.authAvatarIvUn,
            R.id.authDriverIv, R.id.authDriverIvUn,
            R.id.authBookIv, R.id.authBookIvUn,
            R.id.authCarpicIv})
    public void camera(View v) {
//        if (status) return;
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
        if (postType == 3) {
            getData();
        }
    }

    @Override
    public void onPostError(BaseEntity entity) {
        //------------1.2
        if (postType == 0) {
            mauthIdIvUn.setVisibility(View.VISIBLE);
            mIdIv.setVisibility(View.GONE);
            mIdPath = "";
            mauthAvatarIvUn.setVisibility(View.VISIBLE);
            mAvatarIv.setVisibility(View.GONE);
            mAvatarPath = "";
            showToast("上传失败，请重新上传");
        } else if (postType == 1) {
            mauthDriverIvUn.setVisibility(View.VISIBLE);
            mDriverIv.setVisibility(View.GONE);
            mDriverPath = "";
            showToast("上传失败，请重新上传");
        } else if (postType == 2) {
            mauthBookIvUn.setVisibility(View.VISIBLE);
            mBookIv.setVisibility(View.GONE);
            mBookPath = "";
            showToast("上传失败，请重新上传");
        } else if (postType == 3) {
            handleError(entity);
        }

        dismissLoading();
    }

    @Override
    public void onPostError(Throwable e) {
        dismissLoading();
        showNetworkError();
    }

    GongAnAuth dd;
    @Override
    public void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity) {
        dd = entity.getData();

        //------------2.1
        if (postType == 0) {
            mNameEt.setText(dd.name);
            mshenfenzhengEt.setText(dd.num);
        } else if (postType == 1) {
            mjiashizhenghaomaEt.setText(dd.num);
        } else if (postType == 2) {
            if (dd.plate_num.length() > 0) {
                mjianchengs.setSelection(getJianchengPos(dd.plate_num.substring(0, 1)));
                mchepaihaomaEt.setText(dd.plate_num.substring(1));
            }
        }
        showToast("验证成功");
    }

    public int getJianchengPos(String str) {
        Resources res = getResources();
        String[] city = res.getStringArray(R.array.jiancheng);

        for (int i = 0; i < city.length; i++) {
            if (city[i].equals(str)) {
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

    //获取个人信息成功
    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        // mGetDialog.dismiss();
        Auth auth = entity.getData();
        if (auth != null) {

            //名字，invitePhones，驾驶证号码
            if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                mdiyiweiwanshanbt.setText("审核中");
                mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }
            //身份证号码，本人照片，驾驶证
            if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && !TextUtils.isEmpty(auth.picDriver)) {
                mdierweiwanshanbt.setText("审核中");
                mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //行车本，
            if ((!TextUtils.isEmpty(auth.picBook))) {
                mdisanweiwanshanbt.setText("审核中");
                mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //车牌号码，
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
