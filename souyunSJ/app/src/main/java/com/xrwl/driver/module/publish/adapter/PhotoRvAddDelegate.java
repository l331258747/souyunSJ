package com.xrwl.driver.module.publish.adapter;

import android.view.View;

import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.driver.R;
import com.xrwl.driver.module.publish.bean.Photo;

/**
 * Created by www.longdw.com on 2018/4/4 下午3:04.
 */
public class PhotoRvAddDelegate implements ItemViewDelegate<Photo> {

    private final OnAddListener mOnAddListener;

    public PhotoRvAddDelegate(OnAddListener l) {
        mOnAddListener = l;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.add_showphotos_addphoto_layout;
    }

    @Override
    public boolean isForViewType(Photo item, int position) {
        return item.isAdd();
    }

    @Override
    public void convert(ViewHolder holder, Photo photo, int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnAddListener.onAdd();
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public interface OnAddListener {
        void onAdd();
    }
}
