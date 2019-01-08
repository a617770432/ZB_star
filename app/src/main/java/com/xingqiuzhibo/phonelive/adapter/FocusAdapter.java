package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.FocusBean;

import java.util.List;

public class FocusAdapter extends BaseQuickAdapter<FocusBean, BaseViewHolder> {

    public FocusAdapter(int layoutResId, @Nullable List<FocusBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FocusBean item) {
        helper.addOnClickListener(R.id.tv_focus);
    }
}
