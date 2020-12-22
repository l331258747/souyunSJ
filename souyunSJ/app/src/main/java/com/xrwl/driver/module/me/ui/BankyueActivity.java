package com.xrwl.driver.module.me.ui;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseEventActivity;
import com.xrwl.driver.event.BankListRefreshEvent;
import com.xrwl.driver.event.DriverListRrefreshEvent;
import com.xrwl.driver.event.DriverOrderListRrefreshEvent;
import com.xrwl.driver.module.me.adapter.BankAdapter;
import com.xrwl.driver.module.me.adapter.BankyueAdapter;
import com.xrwl.driver.module.me.bean.Bank;
import com.xrwl.driver.module.me.bean.Tixianlist;
import com.xrwl.driver.module.me.mvp.BankContract;
import com.xrwl.driver.module.me.mvp.BankPresenter;
import com.xrwl.driver.module.me.mvp.BankyueContract;
import com.xrwl.driver.module.me.mvp.BankyuePresenter;
import com.xrwl.driver.module.publish.ui.AddressActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 余额
 * Created by www.longdw.com on 2018/4/5 上午12:11.
 */
public class BankyueActivity extends BaseEventActivity<BankyueContract.IView, BankyuePresenter> implements BankyueContract.IView {

    @BindView(R.id.bankRv)
    RecyclerView mRv;
    @BindView(R.id.totalPriceTv)
    TextView mPriceTv;
    @BindView(R.id.totalPriceEt)
    EditText mPriceEt;

    @BindView(R.id.addTixianBtn)
    Button mTixianBtn;

    @BindView(R.id.addJinduBtn)
    Button mJinduBtn;

    @BindView(R.id.quanbutixianBtn)
    Button mquanbutixianBtn;

    @BindView(R.id.daozhangyinhangkaTv)
    TextView mdaozhangyinhangkaTv;

    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位

    private BankyueAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private ProgressDialog mTixianDialog;
    private Button jindu_btn = null;
    private Float shengyujine;
    private static String yinhangkalo;
     @Override
    protected BankyuePresenter initPresenter() {
        return new BankyuePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bankyue_activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BankListRefreshEvent event) {
        getData();
    }

    @OnClick(R.id.addTixianBtn)
    public void tixian() {

        String price = mPriceTv.getText().toString();
        float priceet=0;
        if(TextUtils.isEmpty(mdaozhangyinhangkaTv.getText().toString().replace("点击选择","")))
        {
            new AlertDialog.Builder(this)
                    .setMessage("请选择到账的银行卡号")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            return;
        }
        if(!TextUtils.isEmpty(mPriceEt.getText().toString()))
        {
            priceet=Float.parseFloat(mPriceEt.getText().toString());
        }
        else
        {

            new AlertDialog.Builder(this)
                    .setMessage("请填写提现金额")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            return;
        }
        if (TextUtils.isEmpty(price) || price.equals("0") || price.equals("0.0")) {
            showToast("没有可提现金额");
            return;
        }
        if(mPriceEt.toString()==null)
        {
            showToast("请填写提现金额");
            return;
        }

        float pricezong=Float.parseFloat(mPriceTv.getText().toString());
        if(priceet<=pricezong)
        {
            price = mPriceEt.getText().toString();
            shengyujine=(pricezong-priceet);
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("您输入的提现金额大于您的总额，系统驳回")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();

            return;
        }
        mTixianDialog = LoadingProgress.showProgress(this, "正在提现...");
        mPresenter.tixian(price,yinhangkalo);
    }
    //到账银行卡
    @OnClick(R.id.daozhangyinhangkaTv)
    public void Daozhang() {
        startActivityForResult(new Intent(mContext,BankcanshuActivity.class), RESULT_POSITION_START);
    }
    @OnClick(R.id.addJinduBtn)
    public void Jindu() {
        startActivity(new Intent(mContext, BankyuejinduActivity.class));
    }
    @OnClick(R.id.quanbutixianBtn)
    public void quanbutixian() {
        String str1="";
        TextView editText1 =(TextView)findViewById(R.id.totalPriceTv);
        str1=editText1.getText().toString();
        EditText editText2 =(EditText)findViewById(R.id.totalPriceEt);
        editText2.setText(str1.toCharArray(), 0, str1.length());
    }
    @Override
    protected void getData() {
        mPresenter.getBankList();
        mPresenter.gettixianlist();
        //mPresenter.getTotalPrice();
        mPresenter.getTotalPriceBalance();
    }




    @Override
    protected void initViews() {
       initBaseRv(mRv);



        Intent intent = getIntent();

        String nidaye=intent.getStringExtra("pricehongbao");

        String orderid=intent.getStringExtra("orderid");
        if(!TextUtils.isEmpty(nidaye))
        {
            mPresenter.hongbao(nidaye,mAccount.getId(),orderid);
        }



        mAdapter = new BankyueAdapter(this, R.layout.bankyue_recycler_item, new ArrayList<Tixianlist>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View view = LayoutInflater.from(this).inflate(R.layout.bank_footer_view, mRv, false);

        mWrapper.addFootView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddBankActivity.class));
            }
        });


        mRv.setAdapter(mWrapper);

        getData();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Tixianlist>> entity) {
        mAdapter.setDatas(entity.getData());
        mWrapper.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            String title = data.getStringExtra("title");
            mdaozhangyinhangkaTv.setText(title);
             yinhangkalo=title;


    }
    @Override
    public void onRefreshError(Throwable e) {
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
    }

    @Override
    public void onTotalPriceSuccess(String price) {

        mPriceTv.setText(price);

        mTixianBtn.setEnabled(true);
        mJinduBtn.setEnabled(true);
    }

    @Override
    public void onTixianSuccess() {
        //mTixianDialog.dismiss();
        mPriceTv.setText(String.valueOf(shengyujine));


        new AlertDialog.Builder(this)
                .setMessage("提现成功,等待管理审核")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mContext, BankyueActivity.class));

                    }
                }).show();


        mWrapper.notifyDataSetChanged();

    }

    @Override
    public void onTixianError(BaseEntity entity) {
        mTixianDialog.dismiss();
        showToast("提现失败");
        handleError(entity);
    }

    @Override
    public void onTixianException(Throwable e) {
        mTixianDialog.dismiss();
        showNetworkError();
    }
}
