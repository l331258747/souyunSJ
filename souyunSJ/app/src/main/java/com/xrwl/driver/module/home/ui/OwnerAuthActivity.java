package com.xrwl.driver.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xrwl.driver.module.home.mvp.AuthContract;
import com.xrwl.driver.module.home.mvp.AuthPresenter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 货主实名认证
 * Created by www.longdw.com on 2018/4/4 下午10:29.
 */
public class OwnerAuthActivity extends BaseActivity<AuthContract.IView, AuthPresenter> implements AuthContract.IView {

    public static final int RESULT_ID = 100;
    public static final int RESULT_AVATAR = 200;
    public static final int RESULT_LICENSE = 300;

    @BindView(R.id.authIdIv)
    ImageView mIdIv;//身份证
    @BindView(R.id.authNameEt)
    EditText mNameEt;//姓名
    @BindView(R.id.authAvatarIv)
    ImageView mAvatarIv;//本人照片

    @BindView(R.id.authCb)
    CheckBox mCheckBox;
    @BindView(R.id.authTotalLayout)
    View mTotalView;
    @BindView(R.id.authUnitNameEt)
    EditText mUnitNameEt;//单位名称
    @BindView(R.id.authLicenseIv)
    ImageView mLicenseIv;//营业执照
    @BindView(R.id.authDesTv)
    TextView mDesTv;
    @BindView(R.id.authCheckBoxLayout)
    View mCheckBoxView;

    @BindView(R.id.authConfirmBtn)
    Button mConfirmBtn;

    private String mIdPath, mAvatarPath, mLicensePath;
    private ProgressDialog mGetDialog;
    private ProgressDialog mPostDialog;

    @Override
    protected AuthPresenter initPresenter() {
        return new AuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.owner_auth_activity;
    }

    @Override
    protected void initViews() {
        getData();
    }

    @Override
    protected void getData() {
        mGetDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getData();
    }

    @OnClick({R.id.authCb, R.id.authConfirmBtn})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.authCb) {
            if (mCheckBox.isChecked()) {
                mTotalView.setVisibility(View.VISIBLE);
            } else {
                mTotalView.setVisibility(View.GONE);
            }
        } else if (id == R.id.authConfirmBtn) {
            checkPost();
        }
    }

    private void checkPost() {
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        if (mCheckBox.isChecked()) {
            params.put("type", "1");  params.put("check", "0");
            if (TextUtils.isEmpty(mIdPath)) {
                showToast("请上传身份证照片");
                return;
            }
            picMaps.put("pic_id", mIdPath);
            String name = mNameEt.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("请输入姓名");
                return;
            }
            params.put("username", name);
            if (TextUtils.isEmpty(mAvatarPath)) {
                showToast("请上传本人照片");
                return;
            }
            picMaps.put("pic_avatar", mAvatarPath);
            String unit = mUnitNameEt.getText().toString();
            if (TextUtils.isEmpty(unit)) {
                showToast("请输入单位名称");
                return;
            }
            params.put("unit", unit);
            if (TextUtils.isEmpty(mLicensePath)) {
                showToast("请上传营业执照照片");
                return;
            }
            picMaps.put("pic_licence", mLicensePath);
        }
        else
        {
            params.put("type", "0");
            params.put("check", "0");
            if (TextUtils.isEmpty(mIdPath)) {
                showToast("请上传身份证照片");
                return;
            }
            picMaps.put("pic_id", mIdPath);
            String name = mNameEt.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("请输入姓名");
                return;
            }
            params.put("username", name);
            if (TextUtils.isEmpty(mAvatarPath)) {
                showToast("请上传本人照片");
                return;
            }
            picMaps.put("pic_avatar", mAvatarPath);
        }
        mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
        mPresenter.postData(picMaps, params);
    }

    @OnClick({R.id.authIdIv, R.id.authAvatarIv, R.id.authLicenseIv})
    public void camera(View v) {
        int id = v.getId();
        int result = RESULT_ID;
        if (id == R.id.authIdIv) {
            result = RESULT_ID;
        } else if (id == R.id.authAvatarIv) {
            result = RESULT_AVATAR;
        } else if (id == R.id.authLicenseIv) {
            result = RESULT_LICENSE;
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

            Glide.with(mContext).load(Uri.fromFile(new File(mIdPath))).into(mIdIv);
        } else if (requestCode == RESULT_AVATAR) {
            if (lm.isCompressed()) {
                mAvatarPath = lm.getCompressPath();
            } else {
                mAvatarPath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mAvatarPath))).into(mAvatarIv);
        } else if (requestCode == RESULT_LICENSE) {
            if (lm.isCompressed()) {
                mLicensePath = lm.getCompressPath();
            } else {
                mLicensePath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mLicensePath))).into(mLicenseIv);
        }

    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("提交成功");
        finish();
    }

    @Override
    public void onPostError(BaseEntity entity) {
        mPostDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        mGetDialog.dismiss();
        Auth auth = entity.getData();
        if (auth != null) {

            if (TextUtils.isEmpty(auth.review)) {
                return;
            }

            if ("2".equals(auth.review)) {
                showToast("上次提交的信息未通过审核，请重新提交");
                return;
            }

            if ("0".equals(auth.review)) {
                mConfirmBtn.setEnabled(false);
                mConfirmBtn.setText("审核中");
            } else if ("1".equals(auth.review)) {
                mConfirmBtn.setEnabled(false);
                mConfirmBtn.setText("审核通过");
            }


           // mUnitNameEt.setEnabled(false);
            mCheckBox.setEnabled(true);
            //mDesTv.setVisibility(View.GONE);
           // mCheckBoxView.setVisibility(View.GONE);
            mTotalView.setPadding(0, 0, 0, 0);

            mNameEt.setText(auth.name);
            mUnitNameEt.setText(auth.unit);
            if (!TextUtils.isEmpty(auth.picId)) {//身份证
                Glide.with(this).load(auth.picId).into(mIdIv);
            }
            if (!TextUtils.isEmpty(auth.picAvatar)) {//本人
                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
            }
            if (auth.isCarLoad()) {
                mCheckBox.setChecked(true);
                mTotalView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(auth.picLicence)) {//营业执照
                    Glide.with(this).load(auth.picLicence).into(mLicenseIv);
                }
            }
        }
    }

    @Override
    public void onRefreshError(Throwable e) {
        mGetDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
        mGetDialog.dismiss();
    }
}
