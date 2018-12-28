package com.xingqiuzhibo.phonelive.game.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.game.bean.GameLhRecordBean;
import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.umeng.commonsdk.debug.E;

import java.util.List;

/**
 * Created by cxf on 2018/11/5.
 */

public class GameLhSfAdapter extends RecyclerView.Adapter<GameLhSfAdapter.Vh> {
    private List<GameLhRecordBean> mArray;
    private LayoutInflater mInflater;
    private Context mContext;

    public GameLhSfAdapter(Context context, List<GameLhRecordBean> array) {
        mArray = array;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.game_item_lh_sf, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mArray.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView tv_bet_which;
        TextView tv_coin_num;
        TextView tv_which_win;
        TextView tv_balance;


        public Vh(View itemView) {
            super(itemView);
            tv_bet_which = itemView.findViewById(R.id.tv_bet_which);
            tv_coin_num = itemView.findViewById(R.id.tv_coin_num);
            tv_which_win = itemView.findViewById(R.id.tv_which_win);
            tv_balance = itemView.findViewById(R.id.tv_balance);
        }

        void setData(GameLhRecordBean bean, int position) {
            tv_bet_which.setText(bean.getVote());
            tv_coin_num.setText(bean.getCoin());
            tv_balance.setText(bean.getWincoin());
            tv_which_win.setText(bean.getWin());
            if (bean.getWintype()==1){
                tv_which_win.setTextColor(mContext.getResources().getColor(R.color.long_));
            }else if (bean.getWintype()==2){
                tv_which_win.setTextColor(mContext.getResources().getColor(R.color.hu));
            }else {
                tv_which_win.setTextColor(mContext.getResources().getColor(R.color.he));
            }
        }
    }
}
