package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.ShareListBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class LvShareHisAdapter extends BaseAdapter {

    private Context context;
    private List<ShareListBean> data;

    public LvShareHisAdapter(Context context, List<ShareListBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share_his, null);
        ImageView imgHead = view.findViewById(R.id.img_user_icon);
        TextView tvName = view.findViewById(R.id.tv_user_name);
        ImageView imgSex = view.findViewById(R.id.img_user_sex);


        ShareListBean temp = data.get(position);

        ImgLoader.displayAvatar(temp.getAvatar(), imgHead);

        tvName.setText(temp.getUser_nicename());
        switch (temp.getSex()) {
            case "0":
                break;
            case "1":
                imgSex.setImageResource(R.mipmap.icon_man_qz);
                break;
            case "2":
                imgSex.setImageResource(R.mipmap.icon_woman_qz);
                break;
        }
        return view;
    }
}
