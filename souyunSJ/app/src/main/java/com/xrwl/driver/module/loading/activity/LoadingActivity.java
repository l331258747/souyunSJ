package com.xrwl.driver.module.loading.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xrwl.driver.MyApplication;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.base.PermissionDialog;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.Update;
import com.xrwl.driver.module.account.activity.LoginActivity;
import com.xrwl.driver.module.loading.mvp.LoadingContract;
import com.xrwl.driver.module.loading.mvp.LoadingPresenter;
import com.xrwl.driver.module.tab.activity.TabActivity;
import com.xrwl.driver.retrofit.file.JsDownloadListener;
import com.xrwl.driver.utils.AccountUtil;
import com.xrwl.driver.utils.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

//import com.ldw.library.utils.Utils;

/**
 * Created by www.longdw.com on 2018/3/25 下午3:40.
 */

public class LoadingActivity extends BaseActivity<LoadingContract.IView, LoadingPresenter> implements LoadingContract.IView,
        PermissionDialog.IPermissionCallback, JsDownloadListener {
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    @BindView(R.id.loadingVersionTv)
    TextView mVersionTv;
    @BindView(R.id.loadingRootView)
    View mRootView;
    @BindView(R.id.loadingProgressBar)
    ProgressBar mProgressBar;
    private boolean shouldBack;
    private Disposable mDisposable;
    private static final int REQUEST_CODE_INSTALL_PERMISSION = 107;
    public String versionqin;
    @Override
    protected LoadingPresenter initPresenter() {
        return new LoadingPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.loading_activity;
    }
    //广播
    public static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
    //获取类名
    public static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }

    String orderId;
    private  void getScheme(){
        String action = getIntent().getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = getIntent().getData();
            if(uri != null){
                orderId = uri.getQueryParameter("uid");
            }
        }
        LogUtils.e(orderId);
    }

    @Override
    protected void initViews() {

        getScheme();

        mProgressBar.setVisibility(View.INVISIBLE);

        MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.huanying);
        mediaPlayer.start();
        mVersionTv.setText(getString(R.string.loading_version, Utils.getVersionName(this)));
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.loading_alpha_anim);
        mRootView.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPresenter.copyDB();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private synchronized void finishPage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(Boolean granted) {
                    if (granted) {
                        ((MyApplication) getApplication()).initExternalPath();
                    } else {
                        ((MyApplication) getApplication()).initInnerPath();
                    }
                    checkUpdate();
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            ((MyApplication) getApplication()).initExternalPath();
            checkUpdate();
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldBack) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
        finishPage();
    }

    @Override
    public void onRefreshError(Throwable e) {
    }

    @Override
    public void onError(BaseEntity entity) {
    }

    /**
     * 所有权限都处理完毕 PermissionDialog 回调
     */
    @Override
    public void onCallback() {
        if (mAccount == null) {
            mAccount = new Account();
        }
        mAccount.setFirst(false);
        AccountUtil.saveAccount(this, mAccount);
        checkUpdate();

    }
    private void go() {
        if (mAccount != null && mAccount.isLogin()) {
            Intent intent = new Intent();
            intent.setClass(mContext,TabActivity.class);
            intent.putExtra("orderId",orderId);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
        overridePendingTransition(0, 0);
        finish();
    }

    private void checkUpdate() {
        Account account = AccountUtil.getAccount(mContext);
        if (account == null) {
            account = new Account();
            account.setDownloadPath(((MyApplication) getApplication()).getDownloadPath());
            AccountUtil.saveAccount(mContext, account);
        }
        Map<String, String> params = new HashMap<>();
        params.put("version", AppUtils.getAppVersionName());
        mPresenter.checkUpdate(params);
    }

    @Override
    public void onUpdateSuccess(BaseEntity<Update> entity) {

        final Update update = entity.getData();
        versionqin=update.url;

        new AlertDialog.Builder(this).setMessage(update.remark)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        go();
                    }
                })
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(update.url));
//                        startActivity(intent);
//                        mPresenter.downloadApk(update.url, LoadingActivity.this);
//                        mProgressBar.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(mContext).setMessage("请先打开权限设置")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            startInstallPermissionSettingActivity();
                                        }

                                        mPresenter.downloadApk(update.url, LoadingActivity.this);
                                        mProgressBar.setVisibility(View.VISIBLE);

                                    }
                                }).show();

                    }
                }).setCancelable(false).show();
    }

    @Override
    public void onUpdateError() {
        go();
    }

    @Override
    public void onApkDownloadSuccess(String apkPath) {
        FileUtil.installApk(this, apkPath);
        shouldBack = true;
    }

    @Override
    public void onApkDownloadError() {
        go();
    }

    @Override
    public void onStartDownload() {
        Log.e(TAG, "开始下载");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onFinishDownload() {
        Log.e(TAG, "下载完成");
    }

    @Override
    public void onFail(String errorInfo) {
        go();
        showToast("下载失败");
    }




    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }
    /**
     * 安装应用的流程  大于8.0需要用户手动打开未知来源安装权限
     * 需要在清单文件中加入权限  REQUEST_INSTALL_PACKAGES
     */
    private void installProcess() {

        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //没有未知来源安装权限权限

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("安装应用需要打开未知来源权限，请去设置中开启应用权限，以允许安装来自此来源的应用，如果未打开可能会出现安装失败。");
                builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startInstallPermissionSettingActivity();
                        }
                    }
                });
                builder.show();
                return;//防止系统执行默认的方法会跳转页面后弹窗提示，所以会重复 return 掉
            }
        }

        FileUtil.installApk(this,versionqin);
    }

//    /**
//     * 有权限，开始安装应用程序
//     */
//    private void installApk() {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//                          /*  intent.setDataAndType(Uri.fromFile(new File(getDownloadTempFileName())),
//                            "application/vnd.android.package-archive");*/
//        //适配Android 7.0 FileProvider文件访问，这里可能需要你修改成自己的访问方式
//        FileProvider7.setIntentDataAndType(CompulsiveHelperActivity.this,
//                intent, "application/vnd.android.package-archive", new File(getDownloadTempFileName()), true);
//        startActivity(intent);
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, REQUEST_CODE_INSTALL_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_INSTALL_PERMISSION) {
            installProcess();//再次执行安装流程，包含权限判等
        }
        if(requestCode==NOT_NOTICE){
            myRequetPermission();//由于不知道是否选择了允许所以需要再次判断
        }

    }

    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doRequestPermissionsResult(requestCode, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {

                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){//用户选择了禁止不再询问
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }

                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);

                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();

                    }else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(LoadingActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }

    private void doRequestPermissionsResult(int requestCode, int[] grantResults) {
    }

}
