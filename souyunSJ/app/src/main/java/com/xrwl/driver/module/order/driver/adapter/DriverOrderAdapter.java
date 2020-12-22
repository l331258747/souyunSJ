package com.xrwl.driver.module.order.driver.adapter;

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
public class DriverOrderAdapter extends CommonAdapter<Order> {

    private String mType;

    public DriverOrderAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }

    public void setType(String type) {
        mType = type;
    }

//    protected String isfukuan(String cs)
//    {
//        String abcd="0";
//        if(cs.toString()=="1")
//        {
//            abcd="货主已确认收货";
//        }
//        else
//        {
//            abcd="货主未确认收货";
//        }
//        return  abcd;
//    }
    @Override
    protected void convert(ViewHolder holder, Order myOrder, int position) {
//        holder.setText(R.id.myOrderItemStartTv, myOrder.getStartPos());
//        holder.setText(R.id.myOrderItemEndTv, myOrder.getEndPos());
//        holder.setText(R.id.myOrderItemProductNameTv, "货名：" + myOrder.getProductName());
//        holder.setText(R.id.myOrderItemTruckTv, "车型：" + myOrder.getTruck());
//       // holder.setText(R.id.myOrderItemPropertyTv, myOrder.getWeight() + "吨/" + myOrder.getArea() + "方/" + myOrder.getNum() + "件");
//        String dundun="0";String fangfang="0";String jianjian="0";
//        if(TextUtils.isEmpty(myOrder.getWeight())||myOrder.getWeight().length()==0)
//        {
//            dundun="0";
//        }
//        else
//        {
//            dundun=myOrder.getWeight();
//        }
//        if(TextUtils.isEmpty(myOrder.getArea())||myOrder.getArea().length()==0)
//        {
//            fangfang="0";
//        }
//        else
//        {
//            fangfang=myOrder.getArea();
//        }
//        if(TextUtils.isEmpty(myOrder.getNum())||myOrder.getNum().length()==0)
//        {
//            jianjian="0";
//        }
//        else
//        {
//            jianjian=myOrder.getNum();
//        }
//
//        holder.setText(R.id.myOrderItemPropertyTv, dundun + "吨/" + fangfang + "方/"+jianjian+"件");
//
//
//        TextView driverTv = holder.getView(R.id.myOrderItemDriverTv);
//        if (!TextUtils.isEmpty(myOrder.getDriver())) {
//            driverTv.setVisibility(View.VISIBLE);
//            try {
//                driverTv.setText(URLDecoder.decode(myOrder.getDriver(),"UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//
//        } else {
//            driverTv.setVisibility(View.GONE);
//            driverTv.setText("");
//        }
//
//
//
//        holder.setText(R.id.myOrderItemDateTv, myOrder.getDate());
//
//        ImageView statusIv = holder.getView(R.id.myOrderItemStatusIv);
        if (mType.equals("1")) {//已接单
            holder.setText(R.id.isjiedanTv, "已接单");
           // statusIv.setImageResource(R.drawable.ic_order_received);
        } else if (mType.equals("2")) {//运输中
            holder.setText(R.id.isjiedanTv, "运输中");
           // statusIv.setImageResource(R.drawable.ic_order_transport);
        } else if (mType.equals("3")) {//已完成
            holder.setText(R.id.isjiedanTv, "已完成");
            //holder.setText(R.id.myOrderItemquerenTv, isfukuan(myOrder.getIsquerenshouhuo()));
           // statusIv.setImageResource(R.drawable.ic_order_finished);
        }


        holder.setText(R.id.myOrderItemStartTv, myOrder.getStart_provinces()+myOrder.getStart_poss()+myOrder.getStart_areas());
        holder.setText(R.id.myOrderItemEndTv, myOrder.getEnd_provinces()+myOrder.getEnd_poss()+myOrder.getEnd_areas());

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
        String dundun="0";String fangfang="0";String jianjian="0";
        if(TextUtils.isEmpty(myOrder.getWeight())||myOrder.getWeight().length()==0)
        {
            dundun="0";
        }
        else
        {
            dundun=myOrder.getWeight();
        }
        if(TextUtils.isEmpty(myOrder.getArea())||myOrder.getArea().length()==0)
        {
            fangfang="0";
        }
        else
        {
            fangfang=myOrder.getArea();
        }
        if(TextUtils.isEmpty(myOrder.getNum())||myOrder.getNum().length()==0)
        {
            jianjian="0";
        }
        else
        {
            jianjian=myOrder.getNum();
        }


        if(dundun.equals("0") && fangfang.equals("0") && jianjian.equals("0"))
        {
            holder.setText(R.id.myOrderItemPropertyTv, "整车");
        }
        else
        {
            holder.setText(R.id.myOrderItemPropertyTv, dundun+"吨/"+fangfang+"方/"+jianjian+"件");
        }

        holder.setText(R.id.myOrderItemDateTv, myOrder.getDate());

        holder.setText(R.id.kilo, myOrder.getKilo()+" 公里");

        holder.setText(R.id.freight, myOrder.getFreight()+" 元");

        holder.setText(R.id.start_descss, myOrder.getStart_descss());

        holder.setText(R.id.end_descss, myOrder.getEnd_descss());
    }
}
