package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends BaseQuickAdapter<ReportBean, BaseViewHolder> {

    public ReportAdapter(int layoutResId, @Nullable List<ReportBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportBean item) {

        helper.setText(R.id.tv_title , item.getName());

        AppCompatImageView ivCheck = helper.getView(R.id.iv_check);
        if(item.isCheck()){
            ivCheck.setImageResource(R.mipmap.icon_red_dot);
        }else {
            ivCheck.setImageResource(R.mipmap.icon_not_selected);
        }

    }
}
