
package com.xrwl.driver.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
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

    public static final int RESULT_ID = 100;//身份证-正面
    public static final int RESULT_AVATAR = 200;//身份证-反面
    public static final int RESULT_DRIVER = 300;//驾驶证-正面
    public static final int RESULT_DRIVER_BACK = 301;//驾驶证-反面
    public static final int RESULT_BOOK = 400;//行驶证-正面
    public static final int RESULT_BOOK_BACK = 401;//行驶证-反面
    public static final int RESULT_CAR = 500;//车辆照片

    //实名认证隐藏和显示的ly
    @BindView(R.id.topLY)
    RelativeLayout mtoply;
    @BindView(R.id.toprzLY)
    RelativeLayout mtoprzLY;
    @BindView(R.id.aliyun)
    TextView maliyun;//阿里云tip

    @BindView(R.id.authNameEt)
    TextView mNameEt;//姓名
    @BindView(R.id.shenfenzhengEt)
    TextView mshenfenzhengEt;//证件号
    @BindView(R.id.authSpinner)
    Spinner mAuthSpinner;//配送类型
    @BindView(R.id.jiashizhenghaomaEt)
    TextView mjiashizhenghaomaEt;//证件号码

    @BindView(R.id.authDesTv)
    TextView mauthDesTv;//证件照tip
    @BindView(R.id.diyiweiwanshanbt)
    Button mdiyiweiwanshanbt;//个人信息
    @BindView(R.id.dierweiwanshanbt)
    Button mdierweiwanshanbt;//身份证信息
    @BindView(R.id.disanweiwanshanbt)
    Button mdisanweiwanshanbt;//驾驶证信息
    @BindView(R.id.disiweiwanshanbt)
    Button mdisiweiwanshanbt;//车辆信息
    @BindView(R.id.diwuweiwanshanbt)
    Button mdiwuweiwanshanbt;//行驶证信息

    @BindView(R.id.authIdIv)
    ImageView mIdIv;//身份证-正面
    @BindView(R.id.authAvatarIv)
    ImageView mAvatarIv;//身份证-反面
    @BindView(R.id.authIdIvUn)
    RelativeLayout mauthIdIvUn;//身份证-正面-un
    @BindView(R.id.authAvatarIvUn)
    RelativeLayout mauthAvatarIvUn;//身份证-反面-un

    @BindView(R.id.authDriverIv)
    ImageView mDriverIv;//驾驶证-正面
    @BindView(R.id.authDriverBackIv)
    ImageView mDriverBackIv;//驾驶证-反面
    @BindView(R.id.authDriverIvUn)
    RelativeLayout mauthDriverIvUn;//驾驶证-正面-un
    @BindView(R.id.authDriverBackIvUn)
    RelativeLayout mauthDriverBackIvUn;//驾驶证-反面-un

    @BindView(R.id.authBookIv)
    ImageView mBookIv;//行驶证-正面
    @BindView(R.id.authBookBackIv)
    ImageView mBookBackIv;//行驶证-反面
    @BindView(R.id.authBookIvUn)
    RelativeLayout mauthBookIvUn;//行驶证-正面-un
    @BindView(R.id.authBookBackIvUn)
    RelativeLayout mauthBookBackIvUn;//行驶证-反面-un

    @BindView(R.id.jianchengs)
    Spinner mjianchengs;//车牌号码-省
    @BindView(R.id.chepaihaomaEt)
    TextView mchepaihaomaEt;//车牌号码
    @BindView(R.id.carSp)
    Spinner mcarSp;//车型
    @BindView(R.id.discarSp)
    TextView mdiscarsp;//认证车型
    @BindView(R.id.authCarpicIv)
    ImageView mauthCarpicIv;//车辆照片


    @BindView(R.id.xuantian)
    LinearLayout mxuantian;//选填
    @BindView(R.id.dagou)
    CheckBox mCheckBox;
    @BindView(R.id.hedingzaizhiliangEt)
    TextView mhedingzaizhiliangEt;//核定载质量
    @BindView(R.id.yingyunzhenghaomaEt)
    EditText myingyunzhenghaomaEt;//营运证号码
    @BindView(R.id.cheliangzhoushuEt)
    EditText mcheliangzhoushuEt;//车 辆 轴 数
    @BindView(R.id.zigezhenghaomaEt)
    EditText mzigezhenghaomaEt;//资格证号码

    @BindView(R.id.authConfirmBtn)
    Button mConfirmBtn;//提交

    private String mIdPath, mAvatarPath;//身份证
    private String mDriverPath,mDriverBackPath;//驾驶证
    private String mBookPath,mBookBackPath;//行驶证
    private String mCarPath;//车辆照片

    private String mCategory;//配送类型

    private ProgressDialog mLoadingDialog;
    int postType;//0身份证,1驾驶证，11驾驶证反面，2行驶证，21行驶证反面，3提交信息
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
        mDriverBackIv.setVisibility(View.GONE);
        mauthDriverBackIvUn.setVisibility(View.VISIBLE);

        //行驶证
        mBookIv.setVisibility(View.GONE);
        mauthBookIvUn.setVisibility(View.VISIBLE);
        mBookBackIv.setVisibility(View.GONE);
        mauthBookBackIvUn.setVisibility(View.VISIBLE);

        //选填的
        mxuantian.setVisibility(View.GONE);

        //提交
        mConfirmBtn.setVisibility(View.VISIBLE);

        //spanner 不可选，自动填充 车牌号-省
        mjianchengs.setEnabled(false);
        mjianchengs.setVisibility(View.GONE);

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
            if (TextUtils.isEmpty(mchepaihaomaEt.getText().toString()) || TextUtils.isEmpty(mhedingzaizhiliangEt.getText().toString())) {
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
        if (TextUtils.isEmpty(mDriverBackPath))
            return;

        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_drive", mDriverPath);
        picMaps.put("pic_drive_back", mDriverBackPath);
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

    //认证行驶证-反面
    private void checkPostxingshiBack() {
        if (TextUtils.isEmpty(mBookBackPath)) {
            return;
        }
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        picMaps.put("pic_train_back", mBookBackPath);
        params.put("type", "20");

        showLoading("上传行驶证...");
        postType = 21;
        //------------1

        mPresenter.shenfenzheng(mBookPath, "travelimgback", "3");
        mPresenter.postData(picMaps, params);
    }

    @OnClick({R.id.authIdIv, R.id.authIdIvUn,
            R.id.authAvatarIv, R.id.authAvatarIvUn,
            R.id.authDriverIv, R.id.authDriverIvUn,
            R.id.authDriverBackIv, R.id.authDriverBackIvUn,
            R.id.authBookIv, R.id.authBookIvUn,
            R.id.authBookBackIv, R.id.authBookBackIvUn,
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
        }  else if (id == R.id.authDriverBackIv || id == R.id.authDriverBackIvUn) {
            result = RESULT_DRIVER_BACK;
        } else if (id == R.id.authBookIv || id == R.id.authBookIvUn) {
            result = RESULT_BOOK;
        } else if (id == R.id.authBookBackIv || id == R.id.authBookBackIvUn) {
            result = RESULT_BOOK_BACK;
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
        } else if (requestCode == RESULT_DRIVER_BACK) {
            if (lm.isCompressed()) {
                mDriverBackPath = lm.getCompressPath();
            } else {
                mDriverBackPath = lm.getPath();
            }
            mauthDriverBackIvUn.setVisibility(View.GONE);
            mDriverBackIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mDriverBackPath).into(mDriverBackIv);
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
        } else if (requestCode == RESULT_BOOK_BACK) {
            if (lm.isCompressed()) {
                mBookBackPath = lm.getCompressPath();
            } else {
                mBookBackPath = lm.getPath();
            }
            Glide.with(mContext).load(mBookBackPath).into(mBookBackIv);
            mauthBookBackIvUn.setVisibility(View.GONE);
            mBookBackIv.setVisibility(View.VISIBLE);
            checkPostxingshiBack();
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
        } else if (postType == 11) {
            mauthDriverBackIvUn.setVisibility(View.VISIBLE);
            mDriverBackIv.setVisibility(View.GONE);
            mDriverBackPath = "";
            showToast("上传失败，请重新上传");
        } else if (postType == 2) {
            mauthBookIvUn.setVisibility(View.VISIBLE);
            mBookIv.setVisibility(View.GONE);
            mBookPath = "";
            showToast("上传失败，请重新上传");
        } else if (postType == 2) {
            mauthBookBackIvUn.setVisibility(View.VISIBLE);
            mBookBackIv.setVisibility(View.GONE);
            mBookBackPath = "";
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
        } else if (postType == 11) {

        } else if (postType == 2) {
            if (dd.plate_num.length() > 0) {
                mjianchengs.setSelection(getJianchengPos(dd.plate_num.substring(0, 1)));
                mchepaihaomaEt.setText(dd.plate_num);
            }
        } else if (postType == 21) {
            mhedingzaizhiliangEt.setText(dd.approved_load);
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

    public void setTextValue(TextView tv, String value){
        try {
            tv.setText(URLDecoder.decode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setTextValue(EditText tv, String value){
        try {
            tv.setText(URLDecoder.decode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //获取个人信息成功
    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        // mGetDialog.dismiss();
        Auth auth = entity.getData();
        if (auth != null) {

            //名字，身份证号码，驾驶证号码
            if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                mdiyiweiwanshanbt.setText("审核中");
                mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //身份证号码正面，身份证号码背面，身份证号码
            if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && (!TextUtils.isEmpty(auth.invitePhones))) {
                mdierweiwanshanbt.setText("审核中");
                mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //驾驶证，
            if ((!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                mdisanweiwanshanbt.setText("审核中");
                mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //车牌号码，
            if ((!TextUtils.isEmpty(auth.chepaihaoma))) {
                mdisiweiwanshanbt.setText("审核中");
                mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }

            //行驶证，
            if ((!TextUtils.isEmpty(auth.chepaihaoma))) {
                mdiwuweiwanshanbt.setText("审核中");
                mdiwuweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
            }


            if ("1".equals(auth.review)) {
                status = true;
                mtoply.setVisibility(View.GONE);
                mtoprzLY.setVisibility(View.VISIBLE);
                mConfirmBtn.setVisibility(View.GONE);

                //名字，身份证号码，驾驶证号码
                if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones)) && (!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                    mdiyiweiwanshanbt.setText("审核通过");
                    mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                    maliyun.setVisibility(View.GONE);
                }

                if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar) && (!TextUtils.isEmpty(auth.invitePhones))) {
                    mdierweiwanshanbt.setText("审核通过");
                    mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                    mauthDesTv.setVisibility(View.GONE);
                }
                if ((!TextUtils.isEmpty(auth.jiashizhenghaoma))) {
                    mdisanweiwanshanbt.setText("审核通过");
                    mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }
                if ((!TextUtils.isEmpty(auth.chepaihaoma))) {
                    mdisiweiwanshanbt.setText("审核通过");
                    mdisiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                    mCheckBox.setVisibility(View.GONE);
                }
                if ((!TextUtils.isEmpty(auth.chepaihaoma))) {
                    mdiwuweiwanshanbt.setText("审核通过");
                    mdiwuweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
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


            //姓名
            if (TextUtils.isEmpty(auth.name) || auth.name.equals("0")) {
                mNameEt.setText("");
            } else {
                setTextValue(mNameEt,auth.name);
            }
            //证件号码
            if (TextUtils.isEmpty(auth.invitePhones) || auth.invitePhones.length() == 0) {
                mshenfenzhengEt.setText("");
            } else {
                setTextValue(mshenfenzhengEt,auth.invitePhones);
            }
            // 驾驶证号码
            if (TextUtils.isEmpty(auth.jiashizhenghaoma) || auth.jiashizhenghaoma.length() == 0) {
                mjiashizhenghaomaEt.setText("");
            } else {
                setTextValue(mjiashizhenghaomaEt,auth.jiashizhenghaoma);
            }

            //车牌号码
            if (TextUtils.isEmpty(auth.chepaihaoma) || auth.chepaihaoma.length() == 0) {
                mchepaihaomaEt.setText("");
            } else {
                setTextValue(mchepaihaomaEt,auth.chepaihaoma);
            }

            //核定载质量
            if (TextUtils.isEmpty(auth.hedingzaizhiliang) || auth.hedingzaizhiliang.length() == 0) {
                mhedingzaizhiliangEt.setText("");
            } else {
                setTextValue(mhedingzaizhiliangEt,auth.hedingzaizhiliang);
            }
            //营运证号码
            if (TextUtils.isEmpty(auth.yingyunzhenghaoma) || auth.yingyunzhenghaoma.length() == 0) {
                myingyunzhenghaomaEt.setText("");
            } else {
                setTextValue(myingyunzhenghaomaEt,auth.yingyunzhenghaoma);
            }
            //车 辆 轴 数
            if (TextUtils.isEmpty(auth.cheliangzhoushu) || auth.cheliangzhoushu.length() == 0) {
                mcheliangzhoushuEt.setText("");
            } else {
                setTextValue(mcheliangzhoushuEt,auth.cheliangzhoushu);
            }
            //资格证号码
            if (TextUtils.isEmpty(auth.zigezhenghaoma) || auth.zigezhenghaoma.length() == 0) {
                mzigezhenghaomaEt.setText("");
            } else {
                setTextValue(mzigezhenghaomaEt,auth.zigezhenghaoma);
            }

            //认证车型
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
                mauthIdIvUn.setVisibility(View.GONE);
                mIdIv.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(auth.picAvatar)) {//身份证反
                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
                mauthAvatarIvUn.setVisibility(View.GONE);
                mAvatarIv.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(auth.picDriver)) {//驾驶证
                Glide.with(this).load(auth.picDriver).into(mDriverIv);
                mauthDriverIvUn.setVisibility(View.GONE);
                mDriverIv.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(auth.picDriverBack)) {//驾驶证反
                Glide.with(this).load(auth.picDriverBack).into(mDriverBackIv);
                mauthDriverBackIvUn.setVisibility(View.GONE);
                mDriverBackIv.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(auth.picBook)) {//行车本
                Glide.with(this).load(auth.picBook).into(mBookIv);
                mauthBookIvUn.setVisibility(View.GONE);
                mBookIv.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(auth.picBookBack)) {//行车本
                Glide.with(this).load(auth.picBookBack).into(mBookBackIv);
                mauthBookBackIvUn.setVisibility(View.GONE);
                mBookBackIv.setVisibility(View.VISIBLE);
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
