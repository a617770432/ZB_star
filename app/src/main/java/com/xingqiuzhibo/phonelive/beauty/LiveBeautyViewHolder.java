package com.xingqiuzhibo.phonelive.beauty;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.ConfigBean;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.xingqiuzhibo.phonelive.views.AbsViewHolder;
import com.yunbao.beauty.bean.FilterBean;
import com.yunbao.beauty.bean.TieZhiBean;
import com.yunbao.beauty.custom.ItemDecoration2;
import com.yunbao.beauty.custom.TextSeekBar;


/**
 * Created by cxf on 2018/6/22.
 * 萌颜UI相关
 */

public class LiveBeautyViewHolder extends AbsViewHolder implements View.OnClickListener {

    private SparseArray<View> mSparseArray;
    private int mCurKey;
    private TieZhiAdapter mTieZhiAdapter;
    //private FilterAdapter mFilterAdapter;
    private FilterAdapter mFilterAdapter;
    //private RockAdapter mRockAdapter;
    private EffectListener mEffectListener;
    private VisibleListener mVisibleListener;
    private boolean mShowed;

    public LiveBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_beauty).setOnClickListener(this);
        findViewById(R.id.btn_beauty_shape).setOnClickListener(this);
        findViewById(R.id.btn_meng).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);
//        findViewById(R.id.btn_rock).setOnClickListener(this);
//        findViewById(R.id.btn_haha).setOnClickListener(this);
//        findViewById(R.id.btn_haha_0).setOnClickListener(this);
//        findViewById(R.id.btn_haha_1).setOnClickListener(this);
//        findViewById(R.id.btn_haha_2).setOnClickListener(this);
//        findViewById(R.id.btn_haha_3).setOnClickListener(this);
//        findViewById(R.id.btn_haha_4).setOnClickListener(this);
        mSparseArray = new SparseArray<>();
        mSparseArray.put(R.id.btn_beauty, findViewById(R.id.group_beauty));
        mSparseArray.put(R.id.btn_beauty_shape, findViewById(R.id.group_beauty_shape));
        mSparseArray.put(R.id.btn_meng, findViewById(R.id.group_meng));
        mSparseArray.put(R.id.btn_filter, findViewById(R.id.group_filter));
//        mSparseArray.put(R.id.btn_rock, findViewById(R.id.group_rock));
//        mSparseArray.put(R.id.btn_haha, findViewById(R.id.group_haha));
        mCurKey = R.id.btn_beauty;
        TextSeekBar.OnSeekChangeListener onSeekChangeListener = new TextSeekBar.OnSeekChangeListener() {
            @Override
            public void onProgressChanged(View view, int progress) {
                if (mEffectListener != null) {
                    switch (view.getId()) {
                        case R.id.seek_meibai:
                            mEffectListener.onMeiBaiChanged(progress);
                            break;
                        case R.id.seek_mopi:
                            mEffectListener.onMoPiChanged(progress);
                            break;
                        case R.id.seek_baohe:
                            mEffectListener.onBaoHeChanged(progress);
                            break;
                        case R.id.seek_fengnen:
                            mEffectListener.onFengNenChanged(progress);
                            break;
                        case R.id.seek_big_eye:
                            mEffectListener.onBigEyeChanged(progress);
                            break;
                        case R.id.seek_face:
                            mEffectListener.onFaceChanged(progress);
                            break;
                    }
                }
            }
        };

        TextSeekBar seekMeiBai = ((TextSeekBar) findViewById(R.id.seek_meibai));
        TextSeekBar seekMoPi = ((TextSeekBar) findViewById(R.id.seek_mopi));
        TextSeekBar seekBaoHe = ((TextSeekBar) findViewById(R.id.seek_baohe));
        TextSeekBar seekFenNen = ((TextSeekBar) findViewById(R.id.seek_fengnen));
        TextSeekBar seekBigEye = ((TextSeekBar) findViewById(R.id.seek_big_eye));
        TextSeekBar seekFace = ((TextSeekBar) findViewById(R.id.seek_face));
        seekMeiBai.setOnSeekChangeListener(onSeekChangeListener);
        seekMoPi.setOnSeekChangeListener(onSeekChangeListener);
        seekBaoHe.setOnSeekChangeListener(onSeekChangeListener);
        seekFenNen.setOnSeekChangeListener(onSeekChangeListener);
        seekBigEye.setOnSeekChangeListener(onSeekChangeListener);
        seekFace.setOnSeekChangeListener(onSeekChangeListener);
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean != null) {
            seekMeiBai.setProgress(configBean.getBeautyMeiBai());
            seekMoPi.setProgress(configBean.getBeautyMoPi());
            seekBaoHe.setProgress(configBean.getBeautyBaoHe());
            seekFenNen.setProgress(configBean.getBeautyFenNen());
            seekBigEye.setProgress(configBean.getBeautyBigEye());
            seekFace.setProgress(configBean.getBeautyFace());
        }
        //贴纸
        RecyclerView tieZhiRecyclerView = (RecyclerView) findViewById(R.id.tiezhi_recyclerView);
        tieZhiRecyclerView.setHasFixedSize(true);
        tieZhiRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 6, GridLayoutManager.VERTICAL, false));
        ItemDecoration2 decoration1 = new ItemDecoration2(mContext, 0x00000000, 8, 8);
        decoration1.setOnlySetItemOffsetsButNoDraw(true);
        tieZhiRecyclerView.addItemDecoration(decoration1);
        mTieZhiAdapter = new TieZhiAdapter(mContext);
        mTieZhiAdapter.setOnItemClickListener(new OnItemClickListener<TieZhiBean>() {
            @Override
            public void onItemClick(TieZhiBean bean, int position) {
                if (mEffectListener != null) {
                    mEffectListener.onTieZhiChanged(bean.getName());
                }
            }
        });
        tieZhiRecyclerView.setAdapter(mTieZhiAdapter);
        //滤镜
        RecyclerView filterRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclerView);
        filterRecyclerView.setHasFixedSize(true);
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mFilterAdapter = new FilterAdapter(mContext);
        mFilterAdapter.setOnItemClickListener(new OnItemClickListener<FilterBean>() {
            @Override
            public void onItemClick(FilterBean bean, int position) {
                if (mEffectListener != null) {
                    mEffectListener.onFilterChanged(bean.getTiFilterEnum());
                }
            }
        });
        filterRecyclerView.setAdapter(mFilterAdapter);
//        //抖音
//        RecyclerView rockRecyclerView = (RecyclerView) findViewById(R.id.rock_recyclerView);
//        rockRecyclerView.setHasFixedSize(true);
//        rockRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        mRockAdapter = new RockAdapter(mContext);
//        mRockAdapter.setActionListener(new RockAdapter.ActionListener() {
//            @Override
//            public void onItemClick(TiRockEnum tiRockEnum) {
//                if (mEffectListener != null) {
//                    mEffectListener.onRockChanged(tiRockEnum);
//                }
//            }
//        });
//        rockRecyclerView.setAdapter(mRockAdapter);
    }


    public void setEffectListener(EffectListener effectListener) {
        mEffectListener = effectListener;
    }

    public void show() {
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(true);
        }
        if (mParentView != null && mContentView != null) {
            ViewParent parent = mContentView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mContentView);
            }
            mParentView.addView(mContentView);
        }
        mShowed = true;
    }

    public void hide() {
        removeFromParent();
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(false);
        }
        mShowed = false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_beauty:
            case R.id.btn_beauty_shape:
            case R.id.btn_meng:
            case R.id.btn_filter:
//            case R.id.btn_rock:
//            case R.id.btn_haha:
                toggle(id);
                break;
            case R.id.btn_hide:
                hide();
                break;
//            case R.id.btn_haha_0:
//                if (mEffectListener != null) {
//                    mEffectListener.onHaHaChanged(TiDistortionEnum.NO_DISTORTION);
//                }
//                break;
//            case R.id.btn_haha_1:
//                if (mEffectListener != null) {
//                    mEffectListener.onHaHaChanged(TiDistortionEnum.ET_DISTORTION);
//                }
//                break;
//            case R.id.btn_haha_2:
//                if (mEffectListener != null) {
//                    mEffectListener.onHaHaChanged(TiDistortionEnum.PEAR_FACE_DISTORTION);
//                }
//                break;
//            case R.id.btn_haha_3:
//                if (mEffectListener != null) {
//                    mEffectListener.onHaHaChanged(TiDistortionEnum.SLIM_FACE_DISTORTION);
//                }
//                break;
//            case R.id.btn_haha_4:
//                if (mEffectListener != null) {
//                    mEffectListener.onHaHaChanged(TiDistortionEnum.SQUARE_FACE_DISTORTION);
//                }
            //break;
        }
    }

    private void toggle(int key) {
        if (mCurKey == key) {
            return;
        }
        mCurKey = key;
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            View v = mSparseArray.valueAt(i);
            if (mSparseArray.keyAt(i) == key) {
                if (v.getVisibility() != View.VISIBLE) {
                    v.setVisibility(View.VISIBLE);
                }
            } else {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public boolean isShowed() {
        return mShowed;
    }

    public void release() {
        if (mTieZhiAdapter != null) {
            mTieZhiAdapter.clear();
        }
    }


    public interface VisibleListener {
        void onVisibleChanged(boolean visible);
    }

    public void setVisibleListener(VisibleListener visibleListener) {
        mVisibleListener = visibleListener;
    }
}