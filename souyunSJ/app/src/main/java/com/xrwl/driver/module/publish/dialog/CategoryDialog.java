package com.xrwl.driver.module.publish.dialog;

import android.view.View;
import android.widget.RadioButton;

import com.xrwl.driver.R;
import com.xrwl.driver.base.BasePopDialog;

/**
 * Created by www.longdw.com on 2018/3/31 下午4:08.
 */

public class CategoryDialog extends BasePopDialog implements View.OnClickListener {

    /**
     * 同城配送
     * 冷链运输
     * 长途整车
     * 长途零担
     * 矿产运输
     * 铁路运输
     * 航空运输
     */
    public enum CategoryEnum {
        TYPE_SHORT(0),TYPE_LONG_zhuanche(5),TYPE_LONG_TOTAL(1), TYPE_LONG_ZERO(2),Type_Mineral(6),TYPE_LONG_Railway(3),TYPE_LONG_aviation(4);

        private int value;

        CategoryEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface OnCategoryCallbackListener {
        void onCategoryCallback(CategoryEnum category, String des);
    }

    private RadioButton mShortRb;
    private RadioButton mcategoryzhuancheRb;
    private RadioButton mLongTotalRb;
    private RadioButton mLongZeroRb;
    private RadioButton mMineralRb;
    private RadioButton mLongRailwayRb;
    private RadioButton mLongAviationRb;

    public OnCategoryCallbackListener mListener;
    public CategoryEnum mCategory;

    @Override
    protected int getLayoutId() {
        return R.layout.publish_category_dialog;
    }

    @Override
    protected void initView() {
        mShortRb = findViewById(R.id.categoryShortRb);
        mLongTotalRb = findViewById(R.id.categoryLongTotalRb);
        mLongZeroRb = findViewById(R.id.categoryLongZeroRb);
        mLongRailwayRb = findViewById(R.id.categoryRailwayRb);
        mLongAviationRb = findViewById(R.id.categoryAviationRb);
        mcategoryzhuancheRb=findViewById(R.id.categoryzhuancheRb);
        mMineralRb=findViewById(R.id.categoryMineralRb);
        mShortRb.setOnClickListener(this);
        mLongTotalRb.setOnClickListener(this);
        mLongZeroRb.setOnClickListener(this);
        mcategoryzhuancheRb.setOnClickListener(this);
        mMineralRb.setOnClickListener(this);
//        mLongRailwayRb.setOnClickListener(this);
//        mLongAviationRb.setOnClickListener(this);
        if (mCategory != null) {
            if (mCategory == CategoryEnum.TYPE_SHORT) {
                mShortRb.setChecked(true);
            } else if (mCategory == CategoryEnum.TYPE_LONG_TOTAL) {
                mLongTotalRb.setChecked(true);
            } else if (mCategory == CategoryEnum.TYPE_LONG_ZERO) {
                mLongZeroRb.setChecked(true);
           }else if (mCategory == CategoryEnum.TYPE_LONG_zhuanche) {
                mcategoryzhuancheRb.setChecked(true);
            }
            else if (mCategory == CategoryEnum.Type_Mineral) {
                mMineralRb.setChecked(true);
            }
// else if (mCategory == CategoryEnum.TYPE_LONG_Railway) {
//                mLongZeroRb.setChecked(true);
//            }else if (mCategory == CategoryEnum.TYPE_LONG_aviation) {
//                mLongZeroRb.setChecked(true);
//            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.categoryShortRb) {//同城配送
            mListener.onCategoryCallback(CategoryEnum.TYPE_SHORT, getString(R.string.publish_category_short));
        } else if (id == R.id.categoryLongTotalRb) {//长途整车
            mListener.onCategoryCallback(CategoryEnum.TYPE_LONG_TOTAL, getString(R.string.publish_category_longtotal));
        } else if (id == R.id.categoryLongZeroRb) {//长途零担
            mListener.onCategoryCallback(CategoryEnum.TYPE_LONG_ZERO, getString(R.string.publish_category_longzero));
        }else if (id == R.id.categoryzhuancheRb) {//同城专车
            mListener.onCategoryCallback(CategoryEnum.TYPE_LONG_zhuanche, getString(R.string.publish_category_zhuanche));
        }
        else if (id == R.id.categoryMineralRb) {//矿产运输
            mListener.onCategoryCallback(CategoryEnum.Type_Mineral, getString(R.string.publish_category_Mineral));
        }
//        else if (id == R.id.categoryRailwayRb) {//铁路运输
//            mListener.onCategoryCallback(CategoryEnum.TYPE_LONG_Railway, getString(R.string.publish_category_railway));
//        } else if (id == R.id.categoryAviationRb) {//航空运输
//            mListener.onCategoryCallback(CategoryEnum.TYPE_LONG_aviation, getString(R.string.publish_category_aviation));
//        }

        dismiss();
    }
}
