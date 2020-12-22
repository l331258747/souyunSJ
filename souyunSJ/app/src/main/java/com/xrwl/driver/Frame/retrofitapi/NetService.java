package com.xrwl.driver.Frame.retrofitapi;


import com.xrwl.driver.bean.Cljbxx;
import com.xrwl.driver.bean.DhBenn;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * retrofit请求参数封装与rxjava相结合
 * author:zbb
 * 2018/8/30
 **/
public interface NetService {


    /**
     * M每个注解都是想对象的 请参考以下接口
     */




    /**
     * 无车车辆基本信息
     */
    @FormUrlEncoded
    @POST("WLHY_CL1001")
    Observable<Cljbxx> Cljbxx(@FieldMap Map<String, String> params);
    /**
     *匿名电话
     */
    @FormUrlEncoded
    @POST("autoCallTransferForSp.do")
    Observable<DhBenn> DhBenn(@FieldMap Map<String, String> params);



}
