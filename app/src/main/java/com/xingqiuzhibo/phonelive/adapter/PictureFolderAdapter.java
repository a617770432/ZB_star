package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.PhotoFolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureFolderAdapter extends RecyclerView.Adapter<PictureFolderAdapter.ViewHolder> {

    private List<PhotoFolder> list;
    private Context context;
    private OnItemClickListener listener;

    public PictureFolderAdapter(Context context, List<PhotoFolder> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_picture_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoFolder folder = list.get(position);
        holder.title.setText(folder.getName());
        holder.count.setText(String.valueOf(folder.getList().size()));

        Glide.with(context).load("file://" + (folder.getList() == null || folder.getList().size() == 0 ? "" : folder.getList().get(0)))
                .into(holder.image);

        holder.divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.divider)
        View divider;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());
                }
            });
        }
    }
}
