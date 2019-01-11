package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/11/011.
 */

public class BuyCarAdapter extends BaseQuickAdapter<OnLineMallBean.CarInfo, BaseViewHolder> {


    public BuyCarAdapter(int layoutResId, @Nullable List<OnLineMallBean.CarInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OnLineMallBean.CarInfo item) {
        ImageView imgCar = helper.getView(R.id.img_car);
        helper.setText(R.id.tv_cost, item.getNeedcoin() + "钻石/月");
        ImgLoader.display(item.getThumb(), imgCar);
        helper.getView(R.id.tv_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coin = item.getNeedcoin();
                String account = item.getName();

                String msg = "您将花费" + coin + "钻石，购买坐骑" + account;

                listener.onBuyCarClick(msg, item.getId());
            }
        });

    }

    public interface OnBuyCarListener {
        void onBuyCarClick(String msg, String accountID);
    }

    private BuyCarAdapter.OnBuyCarListener listener;

    public void setOnBuyCarListener(BuyCarAdapter.OnBuyCarListener listener) {
        this.listener = listener;
    }
}
