package com.xrwl.driver.module.order.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.xrwl.driver.R;
import com.xrwl.driver.module.order.driver.ui.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2017/11/25 上午8:27.
 */

public class PoiAdapter extends BaseAdapter {

    private OnPoiItemClickListener mListener;

    public interface OnPoiItemClickListener {
        void poiItemClick(PoiItem pi);
    }

    private Context mContext;
    private List<PoiItem> mDatas = new ArrayList<>();

    public PoiAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<PoiItem> datas) {
        mDatas = datas;
//        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_poi_item, null);
        }

        final PoiItem pi = getItem(position);

        TextView titleTv = ViewHolder.get(convertView, R.id.item_poi_title_tv);
        TextView subTitleTv = ViewHolder.get(convertView, R.id.item_poi_subtitle_tv);

        titleTv.setText(pi.getTitle());
        subTitleTv.setText(pi.getSnippet());

        Button selectBtn = ViewHolder.get(convertView, R.id.item_poi_select_btn);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.poiItemClick(pi);
                }
            }
        });

        return convertView;
    }

    public void setOnPoiItemClickListener(OnPoiItemClickListener l) {
        mListener = l;
    }

}
