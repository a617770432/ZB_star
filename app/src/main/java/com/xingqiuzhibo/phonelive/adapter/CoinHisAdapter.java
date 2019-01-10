package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.CoinHisBean;
import com.xingqiuzhibo.phonelive.utils.StringUtil;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class CoinHisAdapter extends BaseQuickAdapter<CoinHisBean, BaseViewHolder> {

    public CoinHisAdapter(int layoutResId, @Nullable List<CoinHisBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinHisBean item) {
        helper.setIsRecyclable(false);
        //taskStatus (integer, optional): 分成处理状态 1未结算 2已结算 ,
        if (item.getTaskStatus() == 1) {
            helper.setImageResource(R.id.img_action_status, R.mipmap.cash_apply);
        } else if (item.getTaskStatus() == 2) {
            helper.setImageResource(R.id.img_action_status, R.mipmap.cash_done);
        }
        helper.setText(R.id.tv_id_num, "ID号：" + item.getId());
        helper.setText(R.id.tv_get_time, StringUtil.getDurationTextLine(item.getAddtime()));
        helper.setText(R.id.tv_get_action, item.getActionName());
        helper.setText(R.id.tv_get_way, item.getMark()+"");
        if (item.getType().equals("income")) {
            helper.setText(R.id.tv_action_value, "+" + item.getTotalcoin());
        } else {
            helper.setText(R.id.tv_action_value, "-" + item.getTotalcoin());
        }

    }
}
