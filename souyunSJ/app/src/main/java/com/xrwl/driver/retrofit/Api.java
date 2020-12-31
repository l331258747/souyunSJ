package com.xrwl.driver.retrofit;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.bean.Address2;
import com.xrwl.driver.bean.Auth;
import com.xrwl.driver.bean.Business;
import com.xrwl.driver.bean.Distance;
import com.xrwl.driver.bean.GongAnAuth;
import com.xrwl.driver.bean.HistoryOrder;
import com.xrwl.driver.bean.MsgCode;
import com.xrwl.driver.bean.New;
import com.xrwl.driver.bean.Order;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.bean.PostOrder;
import com.xrwl.driver.bean.Receipt;
import com.xrwl.driver.bean.Register;
import com.xrwl.driver.bean.Update;
import com.xrwl.driver.module.friend.bean.Friend;
import com.xrwl.driver.module.me.bean.Bank;
import com.xrwl.driver.module.me.bean.Tixianlist;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.publish.bean.Truck;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @POST @Body  这种适合Content-Type:applicaiton/json;charset=utf-8
 * @POST @FieldMap 这种适合Content-Type:application/x-www-form-urlencoded
 * Created by www.longdw.com on 2018/4/8 下午8:17.
 */
public interface Api {


    /** 同步工会注册数据 */
    @GET("api/user/public/register")
    Observable<BaseEntity<Register>> registergonghui(@QueryMap Map<String, String> params);



    /** 红包钱存入余额 */
    @GET("hongbao/hongbaodriver")
    Observable<BaseEntity> hongbao(@QueryMap Map<String, String> params);


    @GET("User/Loginsiji")
    Observable<BaseEntity<Account>> login(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("User/Register")
    Observable<BaseEntity<Register>> register(@FieldMap Map<String, String> params);

    @GET("admin/map/sms")
    Observable<BaseEntity<MsgCode>> getCode(@QueryMap Map<String, String> params);

    @GET("User/ModifyPwd")
    Observable<BaseEntity> modify(@QueryMap Map<String, String> params);

    @GET("User/Retrieve")
    Observable<BaseEntity> retrieve(@QueryMap Map<String, String> params);



    /** 事实新闻 */
    @GET("Order/new")
    Observable<BaseEntity<List<New>>> getDatanew(@QueryMap Map<String, String> params);


    /** 事实新闻详情 */
    @GET("Order/newshow")
    Observable<BaseEntity<List<New>>> getDatanewshow(@QueryMap Map<String, String> params);


    /** 同城计算两点间距离 */
    @GET("admin/map")
    Observable<BaseEntity<Distance>> calculateDistance(@QueryMap Map<String, String> params);






    /** 长途计算两点间距离 */
    @GET("admin/map/compre")
    Observable<BaseEntity<Distance>> calculateLongDistance(@QueryMap Map<String, String> params);

    /** 长途计算两点间距离 */
    @GET("admin/map/retrans")
    Observable<BaseEntity<Distance>> requestCityLonLat(@QueryMap Map<String, String> params);

    /** 获取车型 */
    @GET("Car/ListCar")
    Observable<BaseEntity<List<Truck>>> getTruckList(@QueryMap Map<String, String> params);

    /** 提交订单 */
    @Multipart
    @POST("Order/AddOrder")
    Observable<BaseEntity<PostOrder>> postOrder(@PartMap Map<String, RequestBody> params);

    /** 发起支付 */
    @GET("Order/Paydriver")
    Observable<BaseEntity<PayResult>> pay(@QueryMap Map<String, String> params);



    /** 货主--->发货微信支付----php */
    @FormUrlEncoded
    @POST("api/wx_pay/recharge")
    Observable<BaseEntity<PayResult>> xrwlwxpay(@FieldMap Map<String, String> params);



    /** 发货完成确定订单的时候 线上支付 */
    @GET("Order/onlinePay")
    Observable<BaseEntity<PayResult>> onlinePay(@QueryMap Map<String, String> params);

    /** 获取好友列表 */
    @GET("Friend/ListFriend")
    Observable<BaseEntity<List<Friend>>> getFriendList(@QueryMap Map<String, String> params);

    /** 添加好友 */
    @FormUrlEncoded
    @POST("Friend/AddFriend")
    Observable<BaseEntity> addFriends(@FieldMap Map<String, String> params);

    /** 群发短信 */
    @GET("admin/map/arrsms")
    Observable<BaseEntity> sendMsg(@QueryMap Map<String, String> params);

    /** 添加银行卡 */
    @FormUrlEncoded
    @POST("Card/AddBankCard")
    Observable<BaseEntity> addBank(@FieldMap Map<String, String> params);

    @GET("Card/ListBankCard")
    Observable<BaseEntity<List<Bank>>> getBankList(@QueryMap Map<String, String> params);

    /** 提现 */
    @GET("Order/TiXian")
    Observable<BaseEntity> tixian(@QueryMap Map<String, String> params);


    /** 提现列表 */
    @GET("Order/tixianlist")
    Observable<BaseEntity<List<Tixianlist>>> gettixianlist(@QueryMap Map<String, String> params);


    /** 货主-->我的订单 */
    @GET("Order/ListOrder")
    Observable<BaseEntity<List<Order>>> getOwnerOrderList(@QueryMap Map<String, String> params);

    /** 货主--->我的订单--->订单详情 */
    @GET("Order/ListShow")
    Observable<BaseEntity<OrderDetail>> getOwnerOrderDetail(@QueryMap Map<String, String> params);

    /** 货主--->订单详情--->定位司机 */
    @GET("Order/Coordinates")
    Observable<BaseEntity<OrderDetail>> locationDriver(@QueryMap Map<String, String> params);

    /** 货主--->取消订单 */
    @GET("Order/DeleteOrder")
    Observable<BaseEntity<OrderDetail>> cancelOwnerOrder(@QueryMap Map<String, String> params);

    /** 货主--->确认收货 */
    @GET("Order/Confirmation")
    Observable<BaseEntity<OrderDetail>> confirmOwnerOrder(@QueryMap Map<String, String> params);

    /** 货主-->历史交易 */
    @GET("History/HistoryTradeList")
    Observable<BaseEntity<HistoryOrder>> getHistoryList(@QueryMap Map<String, String> params);

    /** 司机-->余额 */
    @GET("History/Balanceprice")
    Observable<BaseEntity<HistoryOrder>> getHistoryBalanceList(@QueryMap Map<String, String> params);



    /** 获取实名认证信息 */
    @GET("User/AuthenticationList")
    Observable<BaseEntity<Auth>> getAuthInfo(@QueryMap Map<String, String> params);

    /** 提交实名认证信息 */
    @Multipart
    @POST("User/Authentication")
    Observable<BaseEntity> postAuthInfo(@PartMap Map<String, RequestBody> params);

    /** 司机提交实名认证信息 */
    @Multipart
    @POST("User/DriverAuthor")
    Observable<BaseEntity> postDriverAuthInfo(@PartMap Map<String, RequestBody> params);

    /** 司机提交实名认证信息普通认证 */
    @GET("User/DriverAuthenticationputong")
    Observable<BaseEntity> postputongAuthInfo(@QueryMap Map<String, String> params);


    /** 司机提交实名认证信息普通认证---车型认证 */
    @GET("User/DriverAuthenticationputongchexing")
    Observable<BaseEntity> postputongAuthInfochexing(@QueryMap Map<String, String> params);

    /** 司机找货列表 */
    @GET("Order/DriverList")
    Observable<BaseEntity<List<Order>>> getFindList(@QueryMap Map<String, String> params);



    /** 司机找货列表默认显示的数据 */
    @GET("Order/DriverListmoren")
    Observable<BaseEntity<List<Order>>> getFindListmoren(@QueryMap Map<String, String> params);


    /** 司机找货列表sousuodezhi */
    @GET("Order/DriverListaddrss")
    Observable<BaseEntity<List<Order>>> getFindListaddrss(@QueryMap Map<String, String> params);


    /** 司机-->我的订单 */
    @GET("Order/ListOrderDriver")
    Observable<BaseEntity<List<Order>>> getDriverOrderList(@QueryMap Map<String, String> params);



    /** 司机-->订单详情 */
    @GET("Order/DriverShow")
    Observable<BaseEntity<OrderDetail>> getDriverOrderDetail(@QueryMap Map<String, String> params);



    /** 司机-->自动收货 */
    @GET("Order/Receiving")
    Observable<BaseEntity<OrderDetail>> getDriverReceiving(@QueryMap Map<String, String> params);

    /** 司机端每隔5min发送一次经纬度 */
    @FormUrlEncoded
    @POST("Order/Coordinate")
    Observable<BaseEntity> postLonLat(@FieldMap Map<String, String> params);

    /** 司机--->确认收货 */
    @GET("Order/DriverFinish")
    Observable<BaseEntity<OrderDetail>> confirmDriverOrder(@QueryMap Map<String, String> params);

    /** 司机--->确认收货前提示对方 */
    @GET("Order/DriverFinishqian")
    Observable<BaseEntity<OrderDetail>> confirmDriverqianOrder(@QueryMap Map<String, String> params);


    /** 司机--->取消订单 */
    @GET("Order/CancelOrder")
    Observable<BaseEntity<OrderDetail>> cancelDriverOrder(@QueryMap Map<String, String> params);



    /** 司机--->最后确认线下现金 */
    @GET("Order/Confirmationtixingxianjin")
    Observable<BaseEntity<OrderDetail>> cancelDriverOrdertixing(@QueryMap Map<String, String> params);



    /** 司机--->取消订单开始运输 */
    @GET("Order/CancelOrderkaishiyunshu")
    Observable<BaseEntity<OrderDetail>> cancelDriverkaishiyunshuOrder(@QueryMap Map<String, String> params);


    /** 司机--->导航 */
    @GET("Order/Navigation")
    Observable<BaseEntity<OrderDetail>> nav(@QueryMap Map<String, String> params);

    /** 开始运输 */
    @GET("Order/DriverTransportation")
    Observable<BaseEntity<OrderDetail>> trans(@QueryMap Map<String, String> params);


    /** 增加记录数----货主哪里查看记录 */
    @GET("Order/Hit")
    Observable<BaseEntity<OrderDetail>> hit(@QueryMap Map<String, String> params);




    /** 抢单 */
    @GET("Order/Grab")
    Observable<BaseEntity<OrderDetail>> grapOrder(@QueryMap Map<String, String> params);


    /** 私密抢单 */
    @GET("Order/Grabpwd")
    Observable<BaseEntity<OrderDetail>> grappwdOrder(@QueryMap Map<String, String> params);


    /** 司机--->订单详情--->上传图片 */
    @Multipart
    @POST("Order/DriverPic")
    Observable<BaseEntity<OrderDetail>> uploadDriverImages(@PartMap Map<String, RequestBody> params);

    /** 检查更新 */
    @GET("Other/UpdateDriver")
    Observable<BaseEntity<Update>> checkUpdate(@QueryMap Map<String, String> params);

    /** 下载apk */
    @GET
    Observable<ResponseBody> downloadApk(@Url String fileUrl);

    /** 添加发货地址 */
    @GET("Order/AddAddress")
    Observable<BaseEntity> addAddress(@QueryMap Map<String, String> params);

    /** 获取发货地址列表 */
    @GET("Order/ListAddress")
    Observable<BaseEntity<List<Address2>>> getAddressList(@QueryMap Map<String, String> params);

    /** 获取业务列表 */
    @GET("Order/xiaohongdian")
    Observable<BaseEntity<List<Business>>> getBusinessList(@QueryMap Map<String, String> params);

    /** 标记已读 */
    @GET("Order/showxiaohongdian")
    Observable<BaseEntity> markBusinessRead(@QueryMap Map<String, String> params);


    /** 添加发票 */
    @GET("Friend/Addreceipt")
    Observable<BaseEntity> addReceipt(@QueryMap Map<String, String> params);

    /** 获取发票列表 */
    @GET("Friend/Listreceipt")
    Observable<BaseEntity<List<Receipt>>> getReceiptList(@QueryMap Map<String, String> params);

    /** 短信点击按钮操作模式--货主和司机相互点击 */
    @GET("api/map/type_sms")
    Observable<BaseEntity<MsgCode>> getCodeButton(@QueryMap Map<String, String> params);

    /** 身份证 */
    @FormUrlEncoded
    @POST("admin/idcar/cardids")
    Observable<BaseEntity<GongAnAuth>> shenfenzheng(@FieldMap Map<String, String> params);


}