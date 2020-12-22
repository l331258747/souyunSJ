package com.xrwl.driver.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.xrwl.driver.module.publish.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:08.
 */
public class OrderDetail {

    public String categoryReceiving;//自动收货类别
    public String canshu;//自动收货参数
    /**
     * 如果是货主，按如下规则
     * 0--取消订单显示，定位司机和确认收获不显示
     * 1--取消订单不显示，定位司机和确认收获显示
     * 2--取消订单不显示，定位司机和确认收获显示
     * 3--都不显示
     *
     * 如果是司机，按如下规则
     * 0--抢单显示，其他不显示
     * 1--取消、线路导航、中转显示，确认到货、抢单不显示
     * 2--线路导航、中转、确认到货显示，抢单、取消订单不显示
     * 3--都不显示
     */
    public String type;
    public String isbzj_type;
    public String istype;
    @SerializedName("start_pos")
    public String startPos;
    @SerializedName("end_pos")
    public String endPos;
    @SerializedName("product_name")
    public String productName;
    public String truck;
    @SerializedName("total_price")
    public String price;
    public String weight;
    public String area;
    public String num;
    public String kilo;
    public String pic;
    @SerializedName("Receiving")
    public String Receiving;
    public String upload;//司机端 是否显示上传图片

    public String lon;
    public String lat;

    public String times;//获得高德地图的时间
    public String huozhudianji;
    public String overtotal_price;//获得客户付钱的记录
    public String total_prices;//代收货款金额

    public String Actual_price;
    /** 以下针对司机导航 */
    @SerializedName("category")
    public String category;
    @SerializedName("start_lon")
    public String startLon;
    @SerializedName("start_lat")
    public String startLat;
    @SerializedName("end_lon")
    public String endLon;
    @SerializedName("end_lat")
    public String endLat;
    /** ~~~~~~ */

    @SerializedName("publish_phone")
    public String consignorPhone;//发货电话
    @SerializedName("get_phone")
    public String consigneePhone;//收货电话

    public String remark;//备注

    public String pay;//是否显示支付

    public String onpay;//只针对司机详情是否显示微信或支付宝支付

    @SerializedName("id")
    public String orderId;//订单编号

    @SerializedName("status")
    public String orderStatus;//抢单是否成功1：成功 0：有人已经抢走订单了

    public String packingtype;//备注

    public String  sijidianji;//确认到达

    public String date;
    public String tixing;
    public String ddbh;
    public String daishoutype;

    public String driver;

    public String hongbao;
    public String renmibi;
    public String driverhongbao;


    public String zidongzhicategory;
    public String zidongzhicanshu;

    public String start_desc;
    public String end_desc;

    public List<Photo> getPics() {
        if (!TextUtils.isEmpty(pic)) {

            String[] paths = pic.split(",");
            List<Photo> datas = new ArrayList<>();
            for (String path : paths) {
                Photo photo = new Photo();

                photo.setPath(path);
                datas.add(photo);
            }
            return datas;
        }
        return new ArrayList<>();
    }

    public boolean canUpload() {
        return !"0".equals(upload);
    }

    public boolean showPay() {
        return "1".equals(pay);
    }

    public boolean showOnPay() {
        return "1".equals(onpay);
    }


}
