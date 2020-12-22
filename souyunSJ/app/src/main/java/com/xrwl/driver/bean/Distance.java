package com.xrwl.driver.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by www.longdw.com on 2018/4/25 上午10:06.
 */
public class Distance {
    public String distance;
    public String duration;//用的总时间
    public String start;//开始的县或者区
    public String end;//结束的县或者区
    public String ton;//每公里每吨的价格
    public String square;//每公里每方的价格

    @SerializedName("start_x")
    public String startX;
    @SerializedName("start_y")
    public String endX;
    @SerializedName("end_x")
    public String startY;
    @SerializedName("end_y")
    public String endY;
}
