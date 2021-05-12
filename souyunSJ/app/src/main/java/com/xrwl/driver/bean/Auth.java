package com.xrwl.driver.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 实名认证
 * Created by www.longdw.com on 2018/4/29 下午8:43.
 */
public class Auth {
    /**
     * type_review=0  提交审核后，提交按钮由原来的   “ 确认”  改成  “审核中”      变成灰色，图片和认证的信息正常显示
     * type_review=1  审核通过后，提交按钮由原来的   “ 确认”  改成  “审核通过”   变成灰色，图片和认证的信息正常显示
     * type_review=2 审核未通过，提交按钮正常显示，图片和认证的信息都不显示
     */
    @SerializedName("type_review")
    public String review;

    public String name;//姓名
    public String invitePhones;//身份证号码
    public String category;//配送类型
    public String jiashizhenghaoma;//驾驶证号码

    @SerializedName("pic_id")
    public String picId;//身份证
    @SerializedName("pic_avatar")
    public String picAvatar;//本人照片

    @SerializedName("pic_driver")
    public String picDriver;//驾驶证
    @SerializedName("pic_driver_back")
    public String picDriverBack;//驾驶证反

    @SerializedName("pic_train")
    public String picBook;//行驶证
    @SerializedName("pic_train_back")
    public String picBookBack;//行驶证反

    @SerializedName("pic_car")
    public String picCar;//车辆照片
    public String chepaihaoma;//车牌号码
    public String renzhengchexing;//认证车型

    public String hedingzaizhiliang;//核定载质量
    public String yingyunzhenghaoma;//营运证号码
    public String cheliangzhoushu;//车 辆 轴 数
    public String zigezhenghaoma;//资格证号码

    //--------------无效
    @SerializedName("pic_licence")
    public String picLicence;//营业执照
    public String unit;
    public String picidshenfenzheng;
    @SerializedName("type_carload")
    public String carload;//是否是整车  0：不是  1：是
    public String jiashizheng;//驾驶证
    /**
     * 是否是整车
     * @return
     */
    public boolean isCarLoad() {
        if (!TextUtils.isEmpty(carload) && carload.equals("1")) {
            return true;
        }
        return false;
    }
}
