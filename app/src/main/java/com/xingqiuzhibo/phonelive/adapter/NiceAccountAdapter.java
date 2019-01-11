package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/11/011.
 */

public class NiceAccountAdapter extends BaseQuickAdapter<OnLineMallBean.NiceNum, BaseViewHolder> {

    public NiceAccountAdapter(int layoutResId, @Nullable List<OnLineMallBean.NiceNum> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OnLineMallBean.NiceNum item) {

        helper.setText(R.id.tv_id, item.getName());
        helper.setText(R.id.tv_cost, item.getCoin());
        helper.getView(R.id.tv_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coin = item.getCoin();
                String account = item.getName();

                String msg = "您将花费" + coin + "钻石，购买靓号ID:" + account;

                listener.onBuyAccountClick(msg, item.getId());
            }
        });
    }

    public interface OnBuyAccountListener {
        void onBuyAccountClick(String msg, String accountID);
    }

    private NiceAccountAdapter.OnBuyAccountListener listener;

    public void setOnBuyAccountListener(NiceAccountAdapter.OnBuyAccountListener listener) {
        this.listener = listener;
    }
}
