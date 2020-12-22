package com.xrwl.driver.module.find.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.driver.R;
import com.xrwl.driver.bean.Order;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/5 下午5:52.
 */
public class FindAdapter extends CommonAdapter<Order> {
    public FindAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Order order, int position) {
        //ImageView callIv = holder.getView(R.id.findItemCallIv);
//        callIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//
//                    new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent();
//                                    intent.setData(Uri.parse("tel:" + order.getPhone()));
//                                    intent.setAction(Intent.ACTION_CALL);
//                                    mContext.startActivity(intent);
//                                }
//                            }).show();
//                } else {
//                    new AlertDialog.Builder(mContext).setMessage("请先打开电话权限")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    PermissionUtils.openAppSettings();
//                                }
//                            }).show();
//                }
//            }
//        });
//
//        if (TextUtils.isEmpty(order.getPhone())) {
//            callIv.setVisibility(View.GONE);
//        } else {
//            callIv.setVisibility(View.VISIBLE);
//        }

//        String dundun="0";String fangfang="0";String jianjian="0";
//        if(TextUtils.isEmpty(order.getWeight())||order.getWeight().length()==0)
//        {
//            dundun="0";
//        }
//        else
//        {
//            dundun=order.getWeight();
//        }
//        if(TextUtils.isEmpty(order.getArea())||order.getArea().length()==0)
//        {
//            fangfang="0";
//        }
//
//        else
//        {
//            fangfang=order.getArea();
//        }
//        if(TextUtils.isEmpty(order.getNum())||order.getNum().length()==0)
//        {
//            jianjian="0";
//        }
//        else
//        {
//            jianjian=order.getNum();
//        }
//
//        holder.setText(R.id.findItemStartTv, order.getStartPos());
//        holder.setText(R.id.findItemEndTv, order.getEndPos());
//       // holder.setText(R.id.findItemProductNameTv, "货名：" + order.getProductName());
//        holder.setText(R.id.myOrderItemProductNameTv, "货名：" + myOrder.getProductName());
//        if(TextUtils.isEmpty(order.getTruck()))
//        {
//            holder.setText(R.id.findItemTruckTv, "车型：无车型需求");
//        }
//        else
//        {
//            holder.setText(R.id.findItemTruckTv, "车型：" + order.getTruck());
//        }
//
//
//        holder.setText(R.id.findItemPropertyTv, dundun + "吨/" + fangfang + "方/" + jianjian + "件");
//        // holder.setText(R.id.findItemPropertyTv, order.getWeight() + "吨/" + order.getArea() + "方/" );
//        holder.setText(R.id.findItemDateTv, order.getDate());



        holder.setText(R.id.myOrderItemStartTv, order.getStart_provinces()+order.getStart_poss()+order.getStart_areas());
        holder.setText(R.id.myOrderItemEndTv, order.getEnd_provinces()+order.getEnd_poss()+order.getEnd_areas());

        holder.setText(R.id.myOrderItemProductNameTv, "货名：" + order.getProductName());

        //View dotViewdriver = holder.getView(R.id.myOrderItemDriverTv);





           if (TextUtils.isEmpty(order.getTruck())) {
               holder.setText(R.id.myOrderItemTruckTv, "车型：无车型需求");
           } else {
               holder.setText(R.id.myOrderItemTruckTv, "车型：" + order.getTruck());
           }


        //holder.setText(R.id.myOrderItemPropertyTv, myOrder.getWeight() + "吨/" + myOrder.getArea() + "方/" + myOrder.getNum() + "件");
        String dundun="0";String fangfang="0";String jianjian="0";
        if(TextUtils.isEmpty(order.getWeight())||order.getWeight().length()==0)
        {
            dundun="0";
        }
        else
        {
            dundun=order.getWeight();
        }
        if(TextUtils.isEmpty(order.getArea())||order.getArea().length()==0)
        {
            fangfang="0";
        }
        else
        {
            fangfang=order.getArea();
        }
        if(TextUtils.isEmpty(order.getNum())||order.getNum().length()==0)
        {
            jianjian="0";
        }
        else
        {
            jianjian=order.getNum();
        }


        if(dundun.equals("0") && fangfang.equals("0") && jianjian.equals("0"))
        {
            holder.setText(R.id.myOrderItemPropertyTv, "整车");
        }
        else
        {
            holder.setText(R.id.myOrderItemPropertyTv, dundun+"吨/"+fangfang+"方/"+jianjian+"件");
        }
//        TextView driverTv = holder.getView(R.id.myOrderItemDriverTv);
//
//            try {
//                driverTv.setText(URLDecoder.decode(myOrder.getDriver(),"UTF-8"));
//                isEmpty(myOrder.getDriver());
//                if (TextUtils.isEmpty(myOrder.getDriver()) || myOrder.getDriver().equals("") ) {
//
//                    driverTv.setVisibility(View.GONE);
//                } else {
//                    driverTv.setVisibility(View.VISIBLE);
//                }
//
//                } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }


        holder.setText(R.id.myOrderItemDateTv, order.getDate());

        holder.setText(R.id.kilo, order.getKilo()+" 公里");

        holder.setText(R.id.freight, order.getFreight()+" 元");

        holder.setText(R.id.start_descss, order.getStart_descss());

        holder.setText(R.id.end_descss, order.getEnd_descss());
    }
}
