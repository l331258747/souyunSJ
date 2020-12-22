package com.xrwl.driver.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 提交订单后返回的数据
 * Created by www.longdw.com on 2018/4/19 下午1:30.
 */
public class PostOrder implements Serializable {
    public String date;
    public String truck;
    @SerializedName("start_pos")
    public String startPos;
    @SerializedName("end_pos")
    public String endPos;
    @SerializedName("order_id")
    public String orderId;
}
