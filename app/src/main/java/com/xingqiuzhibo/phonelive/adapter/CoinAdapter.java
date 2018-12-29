package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.AppealContentActivity;
import com.xingqiuzhibo.phonelive.activity.MoreWebViewActivity;
import com.xingqiuzhibo.phonelive.activity.WebViewActivity;
import com.xingqiuzhibo.phonelive.bean.CoinBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/23.
 */

public class CoinAdapter extends RecyclerView.Adapter {
    public static final int CHARGE = 0;
    public static final int ORDER = 1;
    private List<CoinBean> mList;
    private View.OnClickListener mOnClickListener;
    private String mGiveString;
    private String mCoinName;
    private OnItemClickListener<CoinBean> mOnItemClickListener;
    private int mType;//0充值 1订单
    private int mLoadMoreHeight;
    private RecyclerView mRecyclerView;
    private Context mContext;
    protected LayoutInflater mInflater;
    private String mUid;
    private String mToken;

    public CoinAdapter(Context context, String coinName) {
        mContext = context;
        mList = new ArrayList<>();
        mGiveString = WordUtil.getString(R.string.coin_give);
        mCoinName = coinName;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((CoinBean) tag, 0);
                }
            }
        };
        mLoadMoreHeight = DpUtil.dp2px(50);
        mUid = AppConfig.getInstance().getUid();
        mToken = AppConfig.getInstance().getToken();
    }

    public void insertList(List<CoinBean> list) {
        int p = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        //mRecyclerView.scrollToPosition(p);
        mRecyclerView.scrollBy(0, mLoadMoreHeight);
    }

    public void setList(List<CoinBean> list, int type) {
        mType = type;
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removePos(int pos) {
        mList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, mList.size());
    }


    /**
     * 取消订单
     *
     * @param orderno
     * @param pos
     */
    public void cancelOrder(String orderno, final int pos) {
        HttpUtil.cancelOrder(orderno, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
            }
        });
        removePos(pos);
    }

    /**
     * 取消申诉
     *
     * @param appeal_id
     * @param pos
     */
    public void cancelAppeal(String appeal_id, final int pos) {
        HttpUtil.cancelAppeal(appeal_id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
            }
        });
        removePos(pos);
    }

    public void setOnItemClickListener(OnItemClickListener<CoinBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CHARGE) {
            return new Vh(mInflater.inflate(R.layout.item_coin, parent, false));
        } else {
            return new OrderVh(mInflater.inflate(R.layout.item_order, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
        } else {
            ((OrderVh) vh).setData(mList.get(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mType == 0) {
            return CHARGE;
        } else {
            return ORDER;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {
        TextView mCoin;
        TextView mMoney;
        TextView mGive;

        public Vh(View itemView) {
            super(itemView);
            mCoin = itemView.findViewById(R.id.coin);
            mMoney = itemView.findViewById(R.id.money);
            mGive = itemView.findViewById(R.id.give);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(CoinBean bean) {
            itemView.setTag(bean);
            mCoin.setText(bean.getCoin());
            mMoney.setText("￥" + bean.getMoney());
            if (!"0".equals(bean.getGive())) {
                if (mGive.getVisibility() != View.VISIBLE) {
                    mGive.setVisibility(View.VISIBLE);
                }
                mGive.setText(mGiveString + bean.getGive() + mCoinName);
            } else {
                if (mGive.getVisibility() == View.VISIBLE) {
                    mGive.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class OrderVh extends RecyclerView.ViewHolder {
        private TextView tv_orderid;
        private TextView tv_addtime;
        private ImageView iv_coin;
        private TextView tv_charge_num;
        private TextView tv_charge_rmb;
        private TextView tv_num;
        private TextView tv_status;
        private TextView tv_details, tv_free, tv_free2;
        private CoinBean mCoinBean;
        private int mPos;

        public OrderVh(View itemView) {
            super(itemView);
            tv_orderid = itemView.findViewById(R.id.tv_orderid);
            tv_addtime = itemView.findViewById(R.id.tv_addtime);
            iv_coin = itemView.findViewById(R.id.iv_coin);
            tv_charge_num = itemView.findViewById(R.id.tv_charge_num);
            tv_charge_rmb = itemView.findViewById(R.id.tv_charge_rmb);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_details = itemView.findViewById(R.id.tv_details);
            tv_free = itemView.findViewById(R.id.tv_free);
            tv_free2 = itemView.findViewById(R.id.tv_free2);
            tv_free.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCoinBean.getStatus() == 0) {//未付款取消订单接口
                        cancelOrder(mCoinBean.getOrderno(), mPos);
                    } else if (mCoinBean.getStatus() == 2 || mCoinBean.getStatus() == 4) {//订单申诉h5
//                        Intent intent = new Intent(mContext, AppealContentActivity.class);
//                        mContext.startActivity(intent);
                        MoreWebViewActivity.forward(mContext, AppConfig.HOST + "/index.php?g=Appapi&m=Diamonds&a=appeal&uid=" + mUid + "&token=" + mToken + "&orderno=" + mCoinBean.getOrderno());
                    }
                }
            });
            tv_free2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCoinBean.getStatus() == 4) {//取消申诉接口
                        cancelAppeal(mCoinBean.getId(), mPos);
                    }
                }
            });
            tv_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.forward(mContext, AppConfig.HOST + "/index.php?g=Appapi&m=Diamonds&a=index&uid=" + mUid + "&token=" + mToken + "&orderno=" + mCoinBean.getOrderno());
                }
            });
        }

        void setData(CoinBean bean, int pos) {
            mCoinBean = bean;
            mPos = pos;
            tv_orderid.setText(bean.getOrderno());
            tv_addtime.setText(bean.getAddtime());
            ImgLoader.display(bean.getCover(), iv_coin);
            tv_charge_num.setText(bean.getName());
            tv_charge_rmb.setText("￥" + bean.getMoney());
            tv_num.setText(WordUtil.getString(R.string.num) + bean.getCoin());
            int status = bean.getStatus();
            tv_status.setText(bean.getStatus_text());
            if (status == 0) {
                tv_free.setVisibility(View.VISIBLE);
                tv_free2.setVisibility(View.GONE);
                tv_free.setTextColor(mContext.getResources().getColor(R.color.textColor));
                tv_free.setBackgroundResource(R.drawable.bg_btn_coin_us);
                tv_free.setText(WordUtil.getString(R.string.cancal_order));
            } else if (status == 1) {
                tv_free.setVisibility(View.GONE);
                tv_free2.setVisibility(View.GONE);
            } else if (status == 2) {
                tv_free.setVisibility(View.VISIBLE);
                tv_free2.setVisibility(View.GONE);
                tv_free.setTextColor(mContext.getResources().getColor(R.color.orange));
                tv_free.setBackgroundResource(R.drawable.bg_btn_coin_s);
                tv_free.setText(WordUtil.getString(R.string.order_appeal));
            } else if (status == 4) {
                tv_free2.setVisibility(View.VISIBLE);//取消申诉
                tv_free.setVisibility(View.VISIBLE);//继续申诉
                tv_free.setTextColor(mContext.getResources().getColor(R.color.textColor));
                tv_free.setBackgroundResource(R.drawable.bg_btn_coin_us);
                tv_free2.setText(WordUtil.getString(R.string.cancal_appeal));
                tv_free.setText(WordUtil.getString(R.string.continue_appeal));
            }
        }

    }


}
