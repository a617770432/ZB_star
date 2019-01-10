package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.CoinHisBean;
import com.xingqiuzhibo.phonelive.bean.WithDrawCashBean;
import com.xingqiuzhibo.phonelive.utils.StringUtil;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class ChangeCashAdapter extends BaseQuickAdapter<WithDrawCashBean, BaseViewHolder> {

    public ChangeCashAdapter(int layoutResId, @Nullable List<WithDrawCashBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashBean item) {
        helper.setIsRecyclable(false);
        String toAccount = "";
        int resId = 0;
        //1表示支付宝，2表示微信，3表示银行卡'4 USDT
        switch (item.getType()) {
            case 1:
                toAccount = "提现到支付宝";
                resId = R.mipmap.icon_alipay;
                break;
            case 2:
                toAccount = "提现到微信";
                resId = R.mipmap.icon_wechat;
                break;
            case 3:
                toAccount = "提现到银行卡";
                resId = R.mipmap.icon_bank;
                break;
            case 4:
                toAccount = "提现到USDT";
                resId = R.mipmap.icon_cash_usdt;
                break;
        }
        helper.setText(R.id.tv_account_type, toAccount);
        helper.setImageResource(R.id.img_account_type, resId);

        //状态 0审核中，1审核通过，2审核拒绝 ,
        String text = "";
        switch (item.getStatus()) {
            case 0:
                text = "审核中";
                helper.setTextColor(R.id.tv_status, 0xffff3300);
                break;
            case 1:
                text = "已到账";
                helper.setTextColor(R.id.tv_status, 0xff1296DB);
                break;
            case 2:
                text = "审核不通过";
                helper.setTextColor(R.id.tv_status, 0xff1296DB);
                break;
        }
        helper.setText(R.id.tv_status, text);

        //cashType (integer, optional): 提现分类 0银票提现 1钻石提现 ,
        if (item.getCashType() == 0) {
            helper.setText(R.id.tv_coin_num, "映票：" + item.getVotes());
        } else {
            helper.setText(R.id.tv_coin_num, "钻石：" + item.getVotes());
        }

        helper.setText(R.id.tv_get_cash, "到账金额：" + item.getMoney());
        helper.setText(R.id.tv_time, StringUtil.getDurationTextLineMinute(item.getAddtime()));
    }

}
