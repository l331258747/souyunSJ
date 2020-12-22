package com.xrwl.driver.module.me.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseFragment;
import com.xrwl.driver.bean.Account;
import com.xrwl.driver.module.account.activity.ModifyPwdActivity;
import com.xrwl.driver.module.account.activity.WebActivity;

import com.xrwl.driver.module.home.ui.DriverAuthActivity;
import com.xrwl.driver.module.home.ui.OwnerAuthActivity;
import com.xrwl.driver.module.me.adapter.MeAdapter;
import com.xrwl.driver.module.me.bean.Me;
import com.xrwl.driver.module.me.dialog.ExitDialog;
import com.xrwl.driver.module.order.driver.ui.DriverOrderActivity;
import com.xrwl.driver.module.order.driver.ui.SiLiaoOrderActivity;
import com.xrwl.driver.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.driver.module.publish.ui.AddressActivity;
import com.xrwl.driver.module.publish.ui.ReceiptActivity;
import com.xrwl.driver.utils.AccountUtil;
import com.xrwl.driver.utils.Constants;
import com.xrwl.driver.module.me.ui.BankyueActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/3/31 下午2:39.
 */

public class MeFragment extends BaseFragment {
    public static final int RESULT_POSITION = 100;
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    @BindView(R.id.meItemNameTv)
    TextView appyonghuming;

    @BindView(R.id.meItemPhoneTv)
    TextView appdianhua;

    @BindView(R.id.meItemRenzhengchexingTv)
    TextView apprenzhengchexing;

    @BindView(R.id.yhj)
    TextView appyhj;

    @BindView(R.id.yhk)
    TextView appyhk;

    @BindView(R.id.ye)
    TextView appye;


   @BindView(R.id.myorders)
    TextView wodedingdan;

    @BindView(R.id.myrenzheng)
    TextView shimingrenzheng;

    @BindView(R.id.jine)
    TextView renminbi;

    @BindView(R.id.lianxikefu)
    TextView wodekefu;

    @BindView(R.id.xiugaimima)
    TextView wodexiugaimima;

    @BindView(R.id.tuichudenglu)
    TextView wotuichudenglu;

    @BindView(R.id.yhjpic)
    ImageView youhuijuanpic;

    @BindView(R.id.yhkpic)
    ImageView yinhangkapic;

    @BindView(R.id.yepic)
    ImageView yuepic;

    @BindView(R.id.gerenxinxi)
    LinearLayout wodexinxi;

    @BindView(R.id.wddd)
    LinearLayout appwodedingdan;

    @BindView(R.id.smdd)
    LinearLayout appsmdd;


    @BindView(R.id.smrz)
    LinearLayout appshimingrenzheng;

    @BindView(R.id.je)
    LinearLayout appjine;

    @BindView(R.id.lxkf)
    LinearLayout applianxikefu;

    @BindView(R.id.xgmm)
    LinearLayout appxiugaimima;

    @BindView(R.id.tcdl)
    LinearLayout apptuichudenglu;



    @BindView(R.id.addressTv)//地址
     TextView appaddressTv;

    @BindView(R.id.invoiceTv)//发票
     TextView appinvoiceTv;



    private Account mAccount;

    public static MeFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }


    protected int getLayoutId() {
        return R.layout.me_fragment;
    }


    protected void initView(View view) {
        mAccount = AccountUtil.getAccount(mContext);

        if(TextUtils.isEmpty(mAccount.getName()))
        {
            appyonghuming.setText("");
        }
        else
        {
            try {
                appyonghuming.setText(URLDecoder.decode(mAccount.getName(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if(TextUtils.isEmpty(mAccount.getPhone()))
        {
            appdianhua.setText("电话：");
        }
        else
        {
            appdianhua.setText("电话："+mAccount.getPhone());
        }

         String rzcx="";
        if(TextUtils.isEmpty(mAccount.getRenzhengchexing())||mAccount.getRenzhengchexing().length()==0)
        {
            rzcx="请认证";
        }
        else
        {
            rzcx=mAccount.getRenzhengchexing();
        }
        if(rzcx.equals("0"))
        {
            apprenzhengchexing.setText("车型：");
        }
        else
        {
            apprenzhengchexing.setText("车型："+rzcx);
        }

        youhuijuanpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //优惠卷pic
                Intent intent = new Intent(mContext, CouponActivity.class);
                intent.putExtra("title", "优惠卷");
                startActivity(intent);
            }
        });
        yinhangkapic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡pic
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "绑定银行卡");
                startActivity(intent);
            }
        });
        yuepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //鱼额pic
                Intent intent = new Intent(mContext, BankyueActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });

        appyhj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //优惠卷pic
                Intent intent = new Intent(mContext, CouponActivity.class);
                intent.putExtra("title", "优惠卷");
                startActivity(intent);
            }
        });
        appyhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡pic
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "绑定银行卡");
                startActivity(intent);
            }
        });
        appsmdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //面对面订单
                Intent intent = new Intent(mContext,SiLiaoOrderActivity.class);
                intent.putExtra("title", "企业订单");
                startActivity(intent);
            }
        });
        appye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //鱼额pic
                Intent intent = new Intent(mContext, BankyueActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });
        appwodedingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAccount.isOwner()) {
                    Intent intent = new Intent(mContext, OwnerOrderActivity.class);
                    intent.putExtra("title", "我的订单");
                    startActivity(intent);
                } else if (mAccount.isDriver()) {
                    Intent intent = new Intent(mContext, DriverOrderActivity.class);
                    intent.putExtra("title", "我的订单");
                    startActivity(intent);
                }
            }
        });
        appaddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_START);
            }
        });
        appinvoiceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ReceiptActivity.class), RESULT_POSITION_START);
            }
        });
        appshimingrenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DriverAuthActivity.class));
            }
        });
        appjine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });
        applianxikefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent();
                                    intent2.setData(Uri.parse("tel:" + Constants.PHONE_SERVICE));
                                    intent2.setAction(Intent.ACTION_CALL);
                                    startActivity(intent2);
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(mContext).setMessage("请先打开电话权限")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionUtils.openAppSettings();
                                }
                            }).show();
                }
            }
        });
        appxiugaimima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ModifyPwdActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("title", "修改密码");
                startActivity(intent);
            }
        });
        apptuichudenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog dialog = new ExitDialog();
                dialog.show(getFragmentManager(), "exit");
            }
        });
        wodexinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MeInfoActivity.class);
                intent.putExtra("title", "个人信息");
                startActivity(intent);

            }
        });
    }
}
