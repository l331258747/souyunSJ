package com.xrwl.driver.module.order.owner.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldw.library.bean.BaseEntity;
import com.tencent.mm.opensdk.utils.Log;
import com.xrwl.driver.R;
import com.xrwl.driver.base.BaseActivity;
import com.xrwl.driver.bean.OrderDetail;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.driver.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.driver.module.publish.bean.PayResult;
import com.xrwl.driver.module.tab.activity.TabActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class ZhouGongActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>implements OwnerOrderContract.IDetailView {
   @BindView(R.id.addtitleEt)
   EditText maddtitleEt;
   @BindView(R.id.addConfirmBtn)
   Button mfanhuiqd;
   @Override
    protected OwnerOrderDetailPresenter initPresenter() { return new OwnerOrderDetailPresenter(this); }
    @Override
    protected int getLayoutId() {
        return R.layout.zhougong_activity;
    }
    @Override
    protected void initViews() { }
     //获取用户信息
     private void getUserInfo(String title) {
      String path = "https://api.shenjian.io/dream/query?appid=3c4bbbbb473c759a2040137475b80eb0&keyword="+title+"";
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
              .url(path)
              .build();
      Call call = client.newCall(request);
      call.enqueue(new Callback() {
       @Override
       public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onFailure: userinfo" + e.getMessage());
       }
       @Override
       public void onResponse(Call call, Response response) throws IOException {

        //Log.d(TAG, "onResponse: userinfo" + response.body().string());
        String str=response.body().string();
        try {
         JSONObject jsonObject=new JSONObject(str);

         String code = jsonObject.getString("title");
         String desc = jsonObject.getString("desc");




        } catch (JSONException e) {
         e.printStackTrace();
        }

       }
      });
     }
    @OnClick({ R.id.addConfirmBtn})
    public void onClick(View v) {
        if (v.getId() == R.id.addConfirmBtn) {
         String contentTypeFor = null;
         try {
          contentTypeFor = URLEncoder.encode(maddtitleEt.getText().toString(), "UTF-8");
         } catch (UnsupportedEncodingException e) {
          android.util.Log.e(TAG, e.getMessage(), e);
         }
              getUserInfo(contentTypeFor);
         }
        else if(v.getId() == R.id.baseTitleView)
        {
            startActivity(new Intent(mContext, TabActivity.class));
        }
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelOrderError(Throwable e) {

    }



    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmOrderError(Throwable e) {

    }



    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onLocationError(Throwable e) {

    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onPayError(Throwable e) {

    }



    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
