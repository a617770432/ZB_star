package com.xingqiuzhibo.phonelive.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.AppealHisBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class AppealHisAdapter extends BaseQuickAdapter<AppealHisBean, BaseViewHolder> {

    public AppealHisAdapter(int layoutResId, @Nullable List<AppealHisBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppealHisBean item) {
        helper.setIsRecyclable(false);
        helper.setText(R.id.tv_appeal_time, StringUtil.getDurationTextLineMinute(item.getAddtime()));
        helper.setText(R.id.tv_appeal_question, item.getAppealContent());

        ImageView img1 = helper.getView(R.id.img_1);
        ImageView img2 = helper.getView(R.id.img_2);
        ImageView img3 = helper.getView(R.id.img_3);

        RelativeLayout layout = helper.getView(R.id.layout_pic);

        List<ImageView> imgs = new ArrayList<>();
        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);

        List<String> urls = new ArrayList<>();
        if (item.getAppealImg1().length() > 0)
            urls.add(item.getAppealImg1());
        if (item.getAppealImg2().length() > 0)
            urls.add(item.getAppealImg2());
        if (item.getAppealImg3().length() > 0)
            urls.add(item.getAppealImg3());

        if (urls.size() == 0) {
            layout.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < urls.size(); i++) {
                ImgLoader.display(urls.get(i), imgs.get(i));
            }
        }

        if (null != item.getReply())
            helper.setText(R.id.tv_reply, item.getReply());

    }


}
