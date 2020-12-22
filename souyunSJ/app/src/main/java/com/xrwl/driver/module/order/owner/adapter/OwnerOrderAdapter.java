package com.xrwl.driver.module.order.owner.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.driver.R;
import com.xrwl.driver.bean.Order;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:20.
 */
public class OwnerOrderAdapter extends CommonAdapter<Order> {
    private String mType;
    public OwnerOrderAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }
    public void setType(String type) {
        mType = type;
    }
    @Override
    protected void convert(ViewHolder holder, Order myOrder, int position) {
        holder.setText(R.id.myOrderItemStartTv, myOrder.getStartPos());
        holder.setText(R.id.myOrderItemEndTv, myOrder.getEndPos());
        holder.setText(R.id.myOrderItemProductNameTv, "货名：" + myOrder.getProductName());
        if(TextUtils.isEmpty(myOrder.getTruck()))
        {
            holder.setText(R.id.myOrderItemTruckTv, "车型：无车型需求");
        }
        else
        {
            holder.setText(R.id.myOrderItemTruckTv, "车型：" + myOrder.getTruck());
        }

        //holder.setText(R.id.myOrderItemPropertyTv, myOrder.getWeight() + "吨/" + myOrder.getArea() + "方/" + myOrder.getNum() + "件");


        holder.setText(R.id.myOrderItemPropertyTv, myOrder.getWeight() + "吨/" + myOrder.getArea() + "方/");


        TextView driverTv = holder.getView(R.id.myOrderItemDriverTv);
        if (!TextUtils.isEmpty(myOrder.getDriver())) {
            driverTv.setVisibility(View.VISIBLE);
            try {
                driverTv.setText(URLDecoder.decode(myOrder.getDriver(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            driverTv.setVisibility(View.GONE);
            driverTv.setText("");
        }
        holder.setText(R.id.myOrderItemDateTv, myOrder.getDate());

//        ImageView statusIv = holder.getView(R.id.myOrderItemStatusIv);
//        if (mType.equals("0")) {//未接单
//            statusIv.setImageResource(R.drawable.ic_order_default);
//        } else if (mType.equals("1")) {//已接单
//            statusIv.setImageResource(R.drawable.ic_order_received);
//        } else if (mType.equals("2")) {//运输中
//            statusIv.setImageResource(R.drawable.ic_order_transport);
//        } else if (mType.equals("3")) {//已完成
//            statusIv.setImageResource(R.drawable.ic_order_finished);
//        }
    }
}
