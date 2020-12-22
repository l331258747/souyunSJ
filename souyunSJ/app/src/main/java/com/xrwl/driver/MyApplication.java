package com.xrwl.driver;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.driver.bean.DaoMaster;
import com.xrwl.driver.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.io.File;

/**
 * Created by www.longdw.com on 2018/3/25 下午4:23.
 */

public class MyApplication extends Application {

    public static Context mContext;

    private DaoSession mDaoSession;

    private String rootPath = "/xrwl";
    private String downloadPath = "/download";

    public static IWXAPI mWXapi;
    public String WX_APP_ID ="wx040e47ac2a02473b";

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mContext = getApplicationContext();
        registerToWX();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "xrwl-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }
    private void registerToWX() {
        mWXapi = WXAPIFactory.createWXAPI(mContext, WX_APP_ID, false);
        mWXapi.registerApp(WX_APP_ID);
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void initExternalPath() {
        String root;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            root = Environment.getExternalStorageDirectory().getPath();
        } else {
            root = getCacheDir().getPath();
        }
        createPath(root);
    }

    public void initInnerPath() {
        String root = getCacheDir().getPath();
        createPath(root);
    }

    private void createPath(String root) {
        rootPath = root + rootPath;
        downloadPath = rootPath + downloadPath;

        File downloadFile = new File(downloadPath);
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
    }
    public String getDownloadPath() {
        return downloadPath;
    }
}
