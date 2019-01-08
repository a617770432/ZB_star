package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.OrderDetailActivity;
import com.xingqiuzhibo.phonelive.bean.OrderMsgBean;

import java.util.List;

public class OrderPayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<OrderMsgBean.PayStyle> list;

    public OrderPayAdapter(Context context, List<OrderMsgBean.PayStyle> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;

        OrderMsgBean.PayStyle bean = list.get(position);
        //账户名
        holder.tvName.setText(bean.getUserName());
        //1表示支付宝，2表示微信，3表示银行卡'
        switch (bean.getType()) {
            case 1:
                holder.ivKind.setImageResource(R.mipmap.icon_alipay);
                holder.tvKind.setText("支付宝");
                break;
            case 2:
                holder.ivKind.setImageResource(R.mipmap.icon_wechat);
                holder.tvKind.setText("微信");
                break;
            case 3:
                holder.ivKind.setImageResource(R.mipmap.icon_bank);
                holder.tvKind.setText("银行卡");
                holder.tvBankName.setVisibility(View.VISIBLE);
                holder.tvBankName.setText(bean.getAccountBank());
                holder.llSee.setVisibility(View.GONE);
                break;
        }
        //设置备注信息
        if (null == bean.getRemark()) {
            holder.tvRemark.setText("切勿备注任何信息");
        } else {
            holder.tvRemark.setText(bean.getRemark());
        }
        //设置账号信息
        holder.tvNum.setText(bean.getAccount());

        holder.llSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCodeClick(position);
            }
        });

        holder.llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCopyClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onCodeClick(int position);

        void onCopyClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvKind, tvBankName, tvNum, tvCopy, tvRemark;
        private LinearLayout llSee, llCopy;
        private AppCompatImageView ivKind;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvKind = itemView.findViewById(R.id.tv_kind);
            tvBankName = itemView.findViewById(R.id.tv_bank_name);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvCopy = itemView.findViewById(R.id.tv_copy);
            tvRemark = itemView.findViewById(R.id.tv_remark);
            llSee = itemView.findViewById(R.id.ll_see);
            llCopy = itemView.findViewById(R.id.ll_copy);
            ivKind = itemView.findViewById(R.id.iv_kind);
        }
    }

}
