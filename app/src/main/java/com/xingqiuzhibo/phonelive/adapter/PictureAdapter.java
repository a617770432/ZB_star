package com.xingqiuzhibo.phonelive.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.PictureActivity;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    private Activity activity;
    private int source = 0;
    private int left = 0;
    private ArrayList<String> selectList;

    public PictureAdapter(Activity activity, Context context, List<String> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        selectList = new ArrayList<>();
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public ArrayList<String> getSelectList() {
        return selectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = list.get(position);
        Glide.with(activity).load(Utils.getLocalImagePath(url))
                .into(holder.ivPicture);

        if (source == PictureActivity.SOURCE_FOR_RADIO) {
            holder.ivCheck.setVisibility(View.GONE);
            return;
        }
        holder.ivCheck.setVisibility(View.VISIBLE);
        boolean is = selectList.contains(url);
        holder.ivCheck.setSelected(is);
        if (is) {
            holder.ivPicture.setColorFilter(Color.parseColor("#77000000"));
        } else {
            holder.ivPicture.clearColorFilter();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.iv_check)
        ImageView ivCheck;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    if (source == PictureActivity.SOURCE_FOR_RADIO) {
                        listener.onItemClick(position);
                        return;
                    }
                    if (selectList.contains(list.get(position))) {
                        ivCheck.setSelected(false);
                        selectList.remove(list.get(position));
                        ivPicture.setColorFilter(null);
                        listener.onItemClick(position);
                        return;
                    }
                    if (selectList.size() > left - 1) {
                        ToastUtil.show( "最多只能选择" + left + "张照片！");
                        return;
                    }
                    ivCheck.setSelected(true);
                    selectList.add(list.get(position));
                    ivPicture.setColorFilter(Color.parseColor("#77000000"));
                    listener.onItemClick(position);
                }
            });
        }
    }
}