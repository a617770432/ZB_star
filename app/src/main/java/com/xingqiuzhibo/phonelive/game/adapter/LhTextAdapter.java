package com.xingqiuzhibo.phonelive.game.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.yunbao.game.R;
import com.yunbao.game.util.GameIconUtil;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.yunbao.game.util.GameStrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/31.
 */

public class LhTextAdapter extends RecyclerView.Adapter<LhTextAdapter.Vh> {
    private List<Integer> mList;
    private LayoutInflater mInflater;
    private int mW;
    private int mType;

    public LhTextAdapter(Context context, List<Integer> list, int w) {
        mList = list;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(context);
        mW = w;
    }

    public void setList(List<Integer> list, int type) {
        mList.clear();
        mList = list;
        mType = type;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_lhtext, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position));
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView mContent;
        RecyclerView.LayoutParams layoutParams;

        public Vh(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.text);
            layoutParams = (RecyclerView.LayoutParams) mContent.getLayoutParams();
            layoutParams.width = mW;
            layoutParams.height = mW;
            mContent.setLayoutParams(layoutParams);
        }

        void setData(Integer gameAction) {
            itemView.setTag(gameAction);
            if (gameAction != 0) {
                if (mType == 1) {
                    mContent.setBackgroundResource(GameIconUtil.getLeftLhMap(gameAction));
                    if (gameAction == 1) {
                        mContent.setText(WordUtil.getString(R.string.game_lh_l));
                    } else if (gameAction == 2) {
                        mContent.setText(WordUtil.getString(R.string.game_lh_hu));
                    } else if (gameAction == 3) {
                        mContent.setText(WordUtil.getString(R.string.game_lh_he));
                    }
                } else if (mType == 2) {
                    mContent.setBackgroundResource(GameIconUtil.getRightLhMap(gameAction));
                }
            }else {
                mContent.setBackground(null);
            }

        }
    }
}
