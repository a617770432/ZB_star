package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.TimeDetailBean;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 * 直播时长明细适配器
 */

public class TimeDetailAdapter extends BaseQuickAdapter<TimeDetailBean, BaseViewHolder> {

    public TimeDetailAdapter(int layoutResId, @Nullable List<TimeDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeDetailBean item) {
        helper.setText(R.id.time_start, StringUtil.getDurationTextLineMinute(item.getStarttime()));
        helper.setText(R.id.tv_time_end, StringUtil.getDurationTextLineMinute(item.getEndtime()));

        helper.setText(R.id.tv_time_long, StringUtil.getDurationText(item.getEndtime() * 1000 - item.getStarttime() * 1000));

    }
}
