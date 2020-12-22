package com.xrwl.driver.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.xrwl.driver.R;
import com.xrwl.driver.module.publish.adapter.PhotoRvAdapter;
import com.xrwl.driver.module.publish.adapter.PhotoRvAddDelegate;
import com.xrwl.driver.module.publish.adapter.PhotoRvItemDelegate;
import com.xrwl.driver.module.publish.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:35.
 */
public class PhotoRecyclerView extends RelativeLayout {

    private RecyclerView mRv;

    private List<Photo> mDatas = new ArrayList<>();
    private OnPhotoRvControlListener mListener;
    private PhotoRvAdapter mAdapter;
    private boolean mHideCamera;

    public PhotoRecyclerView(Context context) {
        super(context);
    }

    public PhotoRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        mRv = findViewById(R.id.addShowPhotosRv);
        mRv.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 4));

        mDatas.add(new Photo(Photo.SECTION_ADD));
    }

    public void hideCamera() {
        mHideCamera = true;
        if (mDatas.size() > 0) {
            Photo photo = mDatas.get(mDatas.size() - 1);
            if (photo.getType() == Photo.SECTION_ADD) {
                mDatas.remove(photo);
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDatas(final List<Photo> imagePaths) {
        if (imagePaths == null || imagePaths.size() == 0) {
            return;
        }
        mDatas.clear();
        for (Photo photo : imagePaths) {
            photo.setType(Photo.SECTION_ITEM);
//            Photo photo = new Photo(Photo.SECTION_ITEM);
//            photo.setPath(path);
            mDatas.add(photo);
        }

        if (!mHideCamera) {
            mDatas.add(new Photo(Photo.SECTION_ADD));
        }

        mAdapter = new PhotoRvAdapter(getContext(), mDatas, new PhotoRvAddDelegate.OnAddListener() {
            @Override
            public void onAdd() {
                if (mListener != null) {
                    mListener.onCamera();
                }
            }
        }, new PhotoRvItemDelegate.OnItemDeleteListener() {
            @Override
            public void onItemDelete(Photo photo) {
                mDatas.remove(photo);
                imagePaths.remove(photo);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRv.setAdapter(mAdapter);
    }

    public void setOnPhotoRvControlListener(OnPhotoRvControlListener l) {
        mListener = l;
    }

    public interface OnPhotoRvControlListener {
        void onCamera();
    }
}
