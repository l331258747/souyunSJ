package com.xrwl.driver.module.publish.bean;

import android.text.TextUtils;

import com.xrwl.driver.bean.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 发布货源 所有字段
 * Created by www.longdw.com on 2018/4/16 下午9:31.
 */
public class PublishBean implements Serializable {

    public int category = -1;//配送类型
    //    public String truckDes;//车型描述
//    public String truckId;//已选车型
    public Truck truck;
    /**
     * 同城配送状态下 发货经纬度
     */
    public double defaultStartLon;
    public double defaultStartLat;
    /**
     * 同城配送状态下 到货经纬度
     */
    public double defaultEndLon;
    public double defaultEndLat;

    /**
     * 同城配送状态下 发货和收货位置描述信息
     */
    public String defaultStartPosDes;
    public String defaultEndPosDes;

    public String productName;//货物名称
    /**
     * 吨 方 件
     */
    public String defaultWeight;
    public String defaultArea;
    public String defaultNum;

    public String consignorName;//发货人
    public String consignorId;//发货人id
    public String consignorPhone;//发货人号码

    public String consigneeName;//收货人
    public String consigneeId;//收货人id
    public String consigneePhone;//收货人号码
    public String remark;//备注

    /**
     * 长途状态下 件
     */
    public String longNum;
    /**
     * 长途状态下 发货地址 省市区的id
     */
    public int longStartProvinceId = -1;
    public int longStartCityId = -1;
    public int longStartAreaId = -1;
    /**
     * 长途状态下 收货地址 省市区的id
     */
    public int longEndProvinceId = -1;
    public int longEndCityId = -1;
    public int longEndAreaId = -1;

    /**
     * 长途状态下 发货和收货省市区描述信息
     */
    public String longStartProvinceDes;
    public String longStartCityDes;
    public String longStartAreaDes;
    public String longEndProvinceDes;
    public String longEndCityDes;
    public String longEndAreaDes;

    public String startDesc;//发货详细地址
    public String endDesc;//收货详细地址

    public String distance;//公里数 由服务器计算得出
    public String duration;//用的时间 由服务器计算得出
    public String start;//公里数 由服务器计算得出
    public String end;//用的时间 由服务器计算得出
    /**
     * 长途状态下 货物重量：吨、单价、金额
     */
    public String longWeight;
    public String singleTonPrice;
    public String totalTonPrice;
    /**
     * 长途状态下 货物体积：方、单价、金额
     */
    public String longArea;
    public String singleAreaPrice;
    public String totalAreaPrice;

    public String freight;//运费
    public String isReceipt;//是否需要发票 0或1
    public String taxNum;//税号
    public String unitName;//单位名称
    public String email;

    public String isHelpPay;//是否代付  0或1
    public String helpPayId;//代付人id

    public String isHelpGet;//是否代收  0或1
    public String helpGetId;//代收人id
    public String getPrice;//代收货款

    public String insurance = "0";//0：没有保险 50元，以此类推
    public String coupon = "0";//0：不优惠 1：满1000减50，以此类推

    public String finalPrice;//最终价格 合计价格
    public String date;//订单日期

    public String isSendBySelf;//0、1 自送
    public String isPickBySelf;//0、1 自提

    /**
     * 计算城市的经纬度 发货起始点
     */
    public String startX;//经度
    public String startY;//纬度
    public String endX;
    public String endY;

    public String time;

    public ArrayList<String> imagePaths;

    public String packingType;

    public String capacity;

    public Account mAccount;

    public boolean check() {
        if (category == -1) {
            return false;
        }
        if (category == 0 && truck == null) {
            return false;
        }
        if (category == 5 && truck == null) {
            return false;
        }
        if (TextUtils.isEmpty(productName)) {
            return false;
        }
        if (TextUtils.isEmpty(consigneeName) || TextUtils.isEmpty(consigneePhone) ||
                TextUtils.isEmpty(consignorPhone)) {
            return false;
        }

        if (TextUtils.isEmpty(defaultWeight) && TextUtils.isEmpty(defaultArea)) {
            return false;
        }

//        if (TextUtils.isEmpty(defaultNum)) {
//            return false;
//        }

        if (TextUtils.isEmpty(time)) {
            return false;
        }

        if (category == 0) {
            if (defaultStartLat == 0 || defaultStartLon == 0) {
                return false;
            }
            if (defaultEndLat == 0 || defaultEndLon == 0) {
                return false;
            }

            return true;
        }
        else if(category==5)
        {
            if (defaultStartLat == 0 || defaultStartLon == 0) {
                return false;
            }
            if (defaultEndLat == 0 || defaultEndLon == 0) {
                return false;
            }

            return true;
        }
        else {
            if (longStartProvinceDes == null || longStartCityDes == null || longStartAreaDes == null) {
                return false;
            }
            if (longEndProvinceDes == null || longEndCityDes == null || longEndAreaDes == null) {
                return false;
            }
            return true;
        }
    }

    public String getStartPos() {
        if (category == 0) {
            return defaultStartPosDes;
        }
        else if(category==5)
        {
            return defaultStartPosDes;
        }
        else {
            return longStartProvinceDes + longStartCityDes + longStartAreaDes;
        }
    }

    public String getEndPos() {
        if (category == 0) {
            return defaultEndPosDes;
        }
        else if(category==5)
        {
            return  defaultEndPosDes;
        }
        else {
            return longEndProvinceDes + longEndCityDes + longEndAreaDes;
        }
    }

    public String getWeight() {
//        if (category == 0) {
//            return defaultWeight;
//        } else {
//            return longWeight;
//        }
        return defaultWeight;
    }

    public String getArea() {
//        if (category == 0) {
//            return defaultArea;
//        } else {
//            return longArea;
//        }
        return defaultArea;
    }

    public String getDistance() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return distance;
    }
    public String getDuration() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return duration;
    }
    public String getStart() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return start;
    }
    public String getEnd() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return end;
    }
    private float getDistanceFloat() {
        if (TextUtils.isEmpty(distance)) {
            distance = "0";
        }
        return Float.parseFloat(distance);
    }
    private float getDurationFloat() {
        if (TextUtils.isEmpty(duration)) {
            duration = "0";
        }
        return Float.parseFloat(duration.replace("小时",""));
    }
    private float getStartFloat() {
        if (TextUtils.isEmpty(start)) {
            start = "0";
        }
        return Float.parseFloat(start);
    }
    private float getEndFloat() {
        if (TextUtils.isEmpty(end)) {
            end = "0";
        }
        return Float.parseFloat(end);
    }
    public String getNum() {
//        if (category == 0) {
//            return defaultNum;
//        } else {
//            return longNum;
//        }
        return defaultNum;
    }

    public float getPrice() {
        if (category == 0) {//同城计算方式
            float startPrice = 45;//起步价格
            float duration =getDurationFloat();
            float weight = Float.parseFloat(defaultWeight);//获取输入的吨位
            String startqu=getStart();
            String endqu=getEnd();
            float jiagejieguo=0;
            float qinyanwei=0;
            if(startqu.equals(endqu))
            {
                float panduanjieguo=((duration * 16*weight+(36)));
                if(panduanjieguo<=45.0)
                {
                    jiagejieguo=45;
                }
                else
                {
                    jiagejieguo=panduanjieguo;
                }
            }
            else
            {
                float panduanjieguo=(duration * 20*weight+(45));
                if(panduanjieguo<=60.0)
                {
                    jiagejieguo=60;
                }
                else
                {
                    jiagejieguo=panduanjieguo;
                }

            }
            return  jiagejieguo;

        }
        else if(category==5)
        {
            //同城专车
            //计算方式为：
            //起步价45元+总共用时比如为1个小时*20*总吨位
            //20是个基数，每个小时20元
            //大于一个小时计算方式
            float startPrice = Float.parseFloat(truck.getStartPrice());
            float startKilometre = Float.parseFloat(truck.getStartKilometre());
            float exceedPrice = Float.parseFloat(truck.getExceedPrice());
            float distance = getDistanceFloat();
            if (distance < startKilometre) {
                return startPrice;
            }
             return (distance - startKilometre) * exceedPrice + (startPrice);

        }
        else if (category == 2) {//长途零担
            float num = Float.parseFloat(defaultNum);
            if (!TextUtils.isEmpty(defaultWeight)) {
                float weight = Float.parseFloat(defaultWeight);
                if (getDistanceFloat() <= 100) {
                    return ((weight * 13) / 10) * getDistanceFloat();
                } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                    return ((weight * 11) / 10) * getDistanceFloat();
                } else if (200 < getDistanceFloat() && getDistanceFloat() <= 500) {
                    return ((weight * 6) / 10) * getDistanceFloat();
                } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                    return ((weight * 56) / 100) * getDistanceFloat();
                } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1200) {
                    return ((weight * 5) / 10) * getDistanceFloat();
                } else if (1200 < getDistanceFloat() && getDistanceFloat() <= 1600) {
                    return ((weight * 45) / 100) * getDistanceFloat();
                } else {
                    return ((weight * 4) / 10) * getDistanceFloat();
                }
            } else {
                float area = Float.parseFloat(defaultArea);
                float singleArea = Float.parseFloat(singleAreaPrice);

                if (getDistanceFloat() <= 100) {
                    return ((((area * 13) / 10) * getDistanceFloat()) * 3) / 10;
                } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                    return ((((area * 11) / 10) * getDistanceFloat()) * 3) / 10;
                } else if (200 < getDistanceFloat() && getDistanceFloat() <= 500) {
                    return ((((area * 6) / 10) * getDistanceFloat()) * 3) / 10;
                } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                    return ((((area * 56) / 100) * getDistanceFloat()) * 3) / 10;
                } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1200) {
                    return ((((area * 5) / 10) * getDistanceFloat()) * 3) / 10;
                } else if (1200 < getDistanceFloat() && getDistanceFloat() <= 1600) {
                    return ((((area * 45) / 100) * getDistanceFloat() * 3) / 10);
                } else {
                    return ((((area * 4) / 10) * getDistanceFloat() * 3)) / 10;
                }
                //  return (area * singleArea)  * getDistanceFloat();
            }

        } else {


            //长途整车
            float num = Float.parseFloat(defaultNum);
            if (!TextUtils.isEmpty(defaultWeight)) {
                float zcdunkilo = 0;
                float weight = 0;
                float mandun = Float.parseFloat(truck.capacity);
                if (Double.parseDouble(defaultWeight) <= Double.parseDouble(truck.capacity)) {
                    zcdunkilo = Float.parseFloat(truck.zcdunkilo);
                    weight = Float.parseFloat(defaultWeight);
                }
                if (truck.getId().equals("27"))//4.2米/每车/公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 150;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return ((66 / 10) * getDistanceFloat()) + 150;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (63 / 10) * getDistanceFloat();
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (47 / 10) * getDistanceFloat();
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (458 / 100) * getDistanceFloat();
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (448 / 100) * getDistanceFloat();
                    } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1000) {
                        return (43 / 10) * getDistanceFloat();
                    } else if (1000 < getDistanceFloat() && getDistanceFloat() <= 1500) {
                        return (39 / 10) * getDistanceFloat();
                    } else if (1500 < getDistanceFloat() && getDistanceFloat() <= 2000) {
                        return (38 / 10) * getDistanceFloat();
                    } else {
                        return (38 / 10) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("28"))//6.2米/按照/公里/每车
                {
                    if (getDistanceFloat() <= 20) {
                        return 300;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return ((80 / 10) * getDistanceFloat()) + 300;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (75 / 10) * getDistanceFloat();
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (61 / 10) * getDistanceFloat();
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 400) {
                        return (58 / 10) * getDistanceFloat();
                    } else if (400 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (55 / 10) * getDistanceFloat();
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (53 / 10) * getDistanceFloat();
                    } else {
                        return (51/10) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("29"))//6.8米 每车/每公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 300;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (( 80 / 10) * getDistanceFloat()) + 300;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (75 / 10) * getDistanceFloat();
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return ( 61 / 10) * getDistanceFloat();
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 400) {
                        return ( 58 / 10) * getDistanceFloat();
                    } else if (400 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return ( 55 / 10) * getDistanceFloat();
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return ( 53 / 10) * getDistanceFloat();
                    } else {
                        return ( 51 / 10) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("30"))//7.2米 每车/每公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 300;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (( 80 / 10) * getDistanceFloat()) + 300;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return ( 75 / 10) * getDistanceFloat();
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return ( 61 / 10) * getDistanceFloat();
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 400) {
                        return ( 58 / 10) * getDistanceFloat();
                    } else if (400 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (55 / 10) * getDistanceFloat();
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return ( 53 / 10) * getDistanceFloat();
                    } else {
                        return (51 / 10) * getDistanceFloat();
                    }
                }
                else if (truck.getId().equals("31"))//8.2米 每车/每公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 300;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return ((80 / 10) * getDistanceFloat()) + 300;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (75 / 10) * getDistanceFloat();
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (61 / 10) * getDistanceFloat();
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 400) {
                        return ( 58 / 10) * getDistanceFloat();
                    } else if (400 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return ( 55 / 10) * getDistanceFloat();
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return ( 53 / 10) * getDistanceFloat();
                    } else {
                        return ( 51 / 10) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("32"))//9.6米 按照公里算/T
                {
                    if (getDistanceFloat() <= 20) {
                        return 500;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (((18 * 75) / 100) * getDistanceFloat()) + 300;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (((18 * 7) / 10) * getDistanceFloat());
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (((18 * 4) / 10) * getDistanceFloat());
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (((18 * 4) / 10) * getDistanceFloat());
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (((18 * 35) / 100) * getDistanceFloat());
                    } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1000) {
                        return (((18 * 345) / 1000) * getDistanceFloat());
                    } else if (1000 < getDistanceFloat() && getDistanceFloat() <= 1500) {
                        return (((18 * 33) / 100) * getDistanceFloat());
                    } else if (1500 < getDistanceFloat() && getDistanceFloat() <= 2000) {
                        return (((18 * 28) / 100) * getDistanceFloat());
                    } else {
                        return ((18 * 28) / 100) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("33"))//16.5米 按照吨算/公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 800;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (((30 * 12)/100) * getDistanceFloat()) + 800;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (((30 * 43) / 100) * getDistanceFloat());
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1000) {
                        return (((30 * 325) / 1000) * getDistanceFloat());
                    } else if (1000 < getDistanceFloat() && getDistanceFloat() <= 1500) {
                        return (((30 * 31) / 100) * getDistanceFloat());
                    } else if (1500 < getDistanceFloat() && getDistanceFloat() <= 2000) {
                        return (((30 * 29) / 100) * getDistanceFloat());
                    } else {
                        return ((30 * 28) / 100) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("34"))//17.5米
                {
                    if (getDistanceFloat() <= 20) {
                        return 800;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (((30 * 12)/100) * getDistanceFloat()) + 800;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (((30 * 43) / 100) * getDistanceFloat());
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (((30 * 33) / 100) * getDistanceFloat());
                    } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1000) {
                        return (((30 * 325) / 1000) * getDistanceFloat());
                    } else if (1000 < getDistanceFloat() && getDistanceFloat() <= 1500) {
                        return (((30 * 31) / 100) * getDistanceFloat());
                    } else if (1500 < getDistanceFloat() && getDistanceFloat() <= 2000) {
                        return (((30 * 29) / 100) * getDistanceFloat());
                    } else {
                        return ((30 * 28) / 100) * getDistanceFloat();
                    }
                } else if (truck.getId().equals("35"))//13米  T/公里
                {
                    if (getDistanceFloat() <= 20) {
                        return 650;
                    } else if (20 < getDistanceFloat() && getDistanceFloat() <= 100) {
                        return (((33 * 75) / 100) * getDistanceFloat()) + 650;
                    } else if (100 < getDistanceFloat() && getDistanceFloat() <= 200) {
                        return (((33 * 38) / 100) * getDistanceFloat());
                    } else if (200 < getDistanceFloat() && getDistanceFloat() <= 300) {
                        return (((33 * 28) / 100) * getDistanceFloat());
                    } else if (300 < getDistanceFloat() && getDistanceFloat() <= 500) {
                        return (((33 * 28) / 100) * getDistanceFloat());
                    } else if (500 < getDistanceFloat() && getDistanceFloat() <= 800) {
                        return (((33 * 28) / 100) * getDistanceFloat());
                    } else if (800 < getDistanceFloat() && getDistanceFloat() <= 1000) {
                        return (((30 * 275) / 1000) * getDistanceFloat());
                    } else if (1000 < getDistanceFloat() && getDistanceFloat() <= 1500) {
                        return (((33 * 26) / 100) * getDistanceFloat());
                    } else if (1500 < getDistanceFloat() && getDistanceFloat() <= 2000) {
                        return (((33 * 24) / 100) * getDistanceFloat());
                    } else {
                        return ((33 * 24) / 100) * getDistanceFloat();
                    }
                }
                else
                {
                    return ((33 * 24) / 100) * getDistanceFloat();
                }
            } else {
                float zcfangkilo = Float.parseFloat(truck.zcfangkilo);
                float area = Float.parseFloat(defaultArea);
                return (area * zcfangkilo) * getDistanceFloat();
            }
        }
    }


    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("category", category + "");//配送类型
        params.put("start_lon", defaultStartLon + "");//发货起点经度
        params.put("start_lat", defaultStartLat + "");//发货起点纬度
        params.put("end_lon", defaultEndLon + "");//到货终点经度
        params.put("end_lat", defaultEndLat + "");//到货终点纬度

        if (category == 0) {
            params.put("truckid", truck.getId());//已选车型
            params.put("start_city", longStartCityDes);
            params.put("end_city", longEndCityDes);
        }
        else if(category==5)
        {
            params.put("truckid", truck.getId());//已选车型
            params.put("start_city", longStartCityDes);
            params.put("end_city", longEndCityDes);
            params.put("shijian",getDuration());//路程用时
        }
        else {
            params.put("truckid", truck.getId());//已选车型
            params.put("start_province", longStartProvinceDes);
            params.put("start_city", longStartCityDes);
            params.put("end_province", longEndProvinceDes);
            params.put("end_city", longEndCityDes);
        }

        params.put("start", getStart());//同城起始的县或区
        params.put("end", getEnd());//同城终点的县或者区



        params.put("weight", defaultWeight);//同城吨
        params.put("area", defaultArea);//同城方
        // params.put("num", defaultNum);//同城件

        params.put("product_name", productName);//货物名称
        params.put("publish_phone", consignorPhone);//发货电话
        params.put("get_person", consigneeName);//收货人
        params.put("get_phone", consigneePhone);//收货电话
        params.put("remark", remark);
        params.put("freight", freight);
        params.put("insurance", insurance);//保险
        params.put("is_receipt", isReceipt);//是否要发票
        params.put("taxnum", taxNum);//税号
        params.put("unitname", unitName);//单位名称
        params.put("email", email);//email

        params.put("is_helppay", isHelpPay);//是否代付
        params.put("helppay_id", helpPayId);//代付人id
        params.put("is_helpget", isHelpGet);//是否代收
        params.put("helpget_id", helpGetId);//代收人id
        params.put("helpget_price", getPrice);//代收货款

        params.put("coupon", coupon);//优惠券
        params.put("total_price", finalPrice);//合计总价
        params.put("kilo", getDistance());//公里数


        params.put("is_sendbyself", isSendBySelf);//自送
        params.put("is_pickbyself", isPickBySelf);//自提

        params.put("statx", startX);
        params.put("staty", startY);
        params.put("endx", endX);
        params.put("endy", endY);

        params.put("start_desc", startDesc);
        params.put("end_desc", endDesc);

        params.put("addtime", time);

        params.put("packingtype", packingType);

        params.put("capacity", capacity);
        return params;

    }
}
