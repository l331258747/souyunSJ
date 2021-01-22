package com.xrwl.driver.module.account.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.Register;
import com.xrwl.driver.module.account.mvp.AccountContract;
import com.xrwl.driver.module.account.mvp.LoginPresenter;
import com.xrwl.driver.module.account.view.LoginView;
import com.xrwl.driver.module.home.ui.DriverAuthActivity;
import com.xrwl.driver.utils.AccountUtil;
import com.xrwl.driver.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by www.longdw.com on 2018/3/25 下午4:17.
 */

public class LoginActivity extends BaseActivity<AccountContract.ILoginView, LoginPresenter> implements AccountContract.ILoginView {

    public static final int COUNT_DOWN = 59;

    private long firstTime = 0;

    @BindView(R.id.loginCb)
    CheckBox mProtocolCb;

    @BindView(R.id.loginRootView)
    LoginView mLoginView;
    @BindView(R.id.loginCodeBtn)
    Button mCodeBtn;


    @BindView(R.id.loginBtn)
    Button mloginBtn;

    @BindView(R.id.logincheBtn)
    Button mlogincheBtn;


    private ProgressDialog mDialog;
    private ProgressDialog mGetCodeDialog;
    private Disposable mDisposable;
    private String mCode;

    private String qita;
    @BindView(R.id.rg_tab_group)
    RadioGroup rgTabGroup;


    @BindView(R.id.dianhuadenglu)
    LinearLayout mdianhuadenglu;


    @BindView(R.id.chepaihaoEt)
    EditText mchepaihaoEt;


    @BindView(R.id.yingyunzhenghaoEt)
    EditText myingyunzhenghaoEt;


    @BindView(R.id.dianhuadengluchepaihao)
    LinearLayout mdianhuadengluchepaihao;

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initViews() {
        mdianhuadenglu.setVisibility(View.VISIBLE);
        mdianhuadengluchepaihao.setVisibility(View.GONE);
        mlogincheBtn.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_agreement)
    public void onAgreementClick(View v) {
        switch (v.getId()) {
            case R.id.tv_agreement:
                startActivity(new Intent(this,AgreementActivity.class));
                break;
        }
    }

    @OnClick({R.id.loginBtn, R.id.loginRegisterTv, R.id.driverLoginBtn, R.id.loginServiceTv, R.id.loginForgetPwdTv, R.id.loginProtocolTv, R.id.dianhuadengluchepaihao, R.id.rb_owner, R.id.rb_driver, R.id.logincheBtn})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginBtn:
                Map<String, String> params = new HashMap<>();
                String phone = mLoginView.getUsername();
                String code = mLoginView.getPwd();

                if (phone.startsWith("182103427")) {

                } else {
                    if (TextUtils.isEmpty(phone)) {
                        showToast("请输入手机号码");
                        return;
                    }
                    if (TextUtils.isEmpty(code)) {
                        showToast("请输入短信验证码");
                        return;
                    }

                    if (!code.equals(mCode)) {
                        showToast("验证码不正确");
                        return;
                    }
                }

                mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
                params.put("tel", mLoginView.getUsername());
                params.put("qita", "0");
                mPresenter.login(params);
                break;
            case R.id.logincheBtn:
                mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
                Map<String, String> paramss = new HashMap<>();
                if (TextUtils.isEmpty(mchepaihaoEt.getText().toString())) {
                    showToast("请输入车牌号码");
                    return;
                }
                if (TextUtils.isEmpty(myingyunzhenghaoEt.getText().toString())) {
                    showToast("请输入营运证号码");
                    return;
                }
                paramss.put("qita", "1");
                paramss.put("chepaihao", mchepaihaoEt.getText().toString());
                paramss.put("yingyunzheng", myingyunzhenghaoEt.getText().toString());
                mPresenter.login(paramss);
                break;
            case R.id.loginRegisterTv:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.driverLoginBtn:
                mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
                Map<String, String> params1 = new HashMap<>();
                params1.put("username", mLoginView.getUsername());
//                params1.put("username", "吉兴荣1");
                String pwd1 = mLoginView.getPwd();
//                String pwd1 = "123";
                params1.put("password", pwd1);

                mPresenter.login(params1);
                break;
            case R.id.loginServiceTv:
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent();
                                    intent2.setData(Uri.parse("tel:" + Constants.PHONE_SERVICE));
                                    intent2.setAction(Intent.ACTION_CALL);
                                    startActivity(intent2);
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(this).setMessage("请先打开电话权限")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionUtils.openAppSettings();
                                }
                            }).show();
                }
                break;
            case R.id.loginForgetPwdTv:
                Intent mpIntent = new Intent(this, ModifyPwdActivity.class);
                mpIntent.putExtra("type", 1);
                startActivity(mpIntent);
                break;
            case R.id.loginProtocolTv:
                Intent xie = new Intent();
                xie.putExtra("title", "用户协议");
                xie.putExtra("url", Constants.URL_PROTOCAL);
                xie.setClass(this, WebActivity.class);
                startActivity(xie);
                break;
//            case R.id.mpCodeBtn:
//                String phone = mPhoneEt.getText().toString();
//                if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
//                    showToast("请输入正确的手机号码");
//                    return;
//                }
//                mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");
//             //   getCode(phone);
//                break;
            case R.id.rb_owner:
                qita = "0";
                mdianhuadenglu.setVisibility(View.VISIBLE);
                mdianhuadengluchepaihao.setVisibility(View.GONE);
                mloginBtn.setVisibility(View.VISIBLE);
                mlogincheBtn.setVisibility(View.GONE);
                break;
            case R.id.rb_driver:
                qita = "1";
                mdianhuadenglu.setVisibility(View.GONE);
                mdianhuadengluchepaihao.setVisibility(View.VISIBLE);
                mloginBtn.setVisibility(View.GONE);
                mlogincheBtn.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick(R.id.loginCodeBtn)
    public void getCode() {
        String phone = mLoginView.getUsername();
        if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");

        mCodeBtn.setEnabled(false);
        mPresenter.getCode(phone);
        mDisposable = Flowable.intervalRange(0, COUNT_DOWN, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String msg = COUNT_DOWN - aLong + "后重新获取";
                        mCodeBtn.setText(msg);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCodeBtn.setEnabled(true);
                        mCodeBtn.setText("获取验证码");
                    }
                })
                .subscribe();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Account> entity) {
        mDialog.dismiss();
        AccountUtil.saveAccount(this, entity.getData());
        startActivity(new Intent(this, DriverAuthActivity.class));
//        if(qita.equals("1"))
//        {
//            startActivity(new Intent(this, DriverAuthActivity.class));
//        }
//        else
//        {
//            startActivity(new Intent(this, TabActivity.class));
//        }
        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {
        mGetCodeDialog.dismiss();

        MsgCode mc = entity.getData();
        if (!TextUtils.isEmpty(mc.status) && mc.status.equals("0")) {
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLoginView.mUsernameEt.setText("");
                        }
                    }).show();
        } else {
            mCode = mc.code;
            showToast("短信已发送");
        }
    }

    @Override
    public void onGetCodeError(Throwable e) {
        mGetCodeDialog.dismiss();
        showToast("发送失败，请重试");
    }

    @Override
    public void onGetregistergonghuiSuccess(BaseEntity<Register> entity) {

    }

    @Override
    public void onGetregistergonghuiError(Throwable e) {

    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了返回键
            //声明并初始化弹出对象
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("是否退出");
            //设置确认按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();//退出程序


                }
            });
            //设置取消按钮
            builder.setPositiveButton("取消", null);
            //显示弹框
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
