package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.OnLineMallBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class BuyVipAdapter extends BaseAdapter {
    private Context context;
    private List<OnLineMallBean.VIPList> data = new ArrayList<>();

    public BuyVipAdapter(Context context, List<OnLineMallBean.VIPList> data) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_gv_vip, null);
        TextView tv = view.findViewById(R.id.tv_vip_type);
        tv.setText(data.get(position).getLength_text());
        if (data.get(position).isCheck()) {
            tv.setBackgroundResource(R.drawable.background_yellow_15dp);
            tv.setTextColor(0xffffffff);
        } else {
            tv.setBackgroundResource(R.drawable.background_gray_line_15dp);
            tv.setTextColor(0xff646464);
        }
        return view;
    }
}
