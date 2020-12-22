package com.xrwl.driver.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by www.longdw.com on 2018/5/15 上午8:40.
 *
 * https://github.com/MZCretin/ExternalMapUtils/
 */
public class AMapUtil {

    public static boolean hasGaodeApp(Context context) {
        return isAppInstalled(context, "com.autonavi.minimap");
    }

    private static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
            installed =true;
        } catch(PackageManager.NameNotFoundException e) {
            installed =false;
        }
        return installed;
    }

    /**
     * 调起高德客户端 路径规划
     * 支持版本 V4.2.1 起。
     *
     * @param activity
     * @param sLongitude
     * @param sLatitude
     * @param sName
     * @param dLongitude
     * @param dLatitude
     * @param dName
     * @param appName
     */
    public static void openGaodeRouteMap(Context activity, String sLongitude, String sLatitude, String sName,
                                         String dLongitude, String dLatitude, String dName, String appName) {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("amapuri://route/plan/?sourceApplication=" + appName +
                        "&sid=&slat=" + sLatitude + "&slon=" +
                        sLongitude + "&sname=" + sName + "&did=&dlat=" +
                        dLatitude + "&dlon=" + dLongitude + "&dname=" + dName + "&dev=1&t=0"));
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }

    /**
     * 调起高德客户端 导航
     * 输入终点，以用户当前位置为起点开始路线导航，提示用户每段行驶路线以到达目的地。支持版本V4.1.3 起。
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     *
     * @param activity
     * @param appName
     */
    public static void openGaodeNaviMap(Context activity, String appName, String poiname,
                                        String latitude, String longitude) {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("androidamap://navi?sourceApplication=" + appName + "&poiname=" +
                        poiname + "&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2"));
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }

}
