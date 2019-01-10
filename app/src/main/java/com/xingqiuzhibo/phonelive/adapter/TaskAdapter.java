package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.EditProfileActivity;
import com.xingqiuzhibo.phonelive.bean.TaskMsgBean;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private List<TaskMsgBean> data;

    public TaskAdapter(Context context, List<TaskMsgBean> data) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_msg, null);
        TextView tvName = view.findViewById(R.id.tv_task_name);
        TextView tvCoin = view.findViewById(R.id.tv_can_get_coin);
        TextView tvDo = view.findViewById(R.id.tv_to_task);

        final TaskMsgBean temp = data.get(position);

        tvName.setText(temp.getConfig_name());
        tvCoin.setText(temp.getConfig_value());
        //1是未完成，2是已完成
        if (temp.getConfig_status().equals("1")) {
            tvDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (temp.getId()) {
                        case "3":
                            //修改昵称，完善个人资料
                            context.startActivity(new Intent(context, EditProfileActivity.class));
                            break;
                        case "4":
                            //发第一篇带有图文的贴，并审核成功
                            break;
                        case "5":
                            //发第一篇带有视频的贴，并审核成功
                            break;
                    }
                }
            });
        } else {
            tvDo.setBackgroundResource(R.drawable.background_done_15dp);
            tvDo.setText("完成");
        }

        switch (temp.getId()) {
            case "1":
                //每成功邀请1位用户，下载APP并登录
                tvDo.setVisibility(View.GONE);
                break;
            case "2":
                //完成账号注册
                tvDo.setBackgroundResource(R.drawable.background_done_15dp);
                tvDo.setText("完成");
                break;
            case "3":
                //修改昵称，完善个人资料
                break;
            case "4":
                //发第一篇带有图文的贴，并审核成功
                break;
            case "5":
                //发第一篇带有视频的贴，并审核成功
                break;
        }

        return view;
    }
}
