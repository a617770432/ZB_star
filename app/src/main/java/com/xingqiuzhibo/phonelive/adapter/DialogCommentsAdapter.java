package com.xingqiuzhibo.phonelive.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.CommentBean;
import com.xingqiuzhibo.phonelive.bean.CommunityCommentEntity;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;

import java.util.ArrayList;
import java.util.List;

public class DialogCommentsAdapter extends BaseQuickAdapter<CommunityCommentEntity, BaseViewHolder> {


    public DialogCommentsAdapter(int layoutResId, @Nullable List<CommunityCommentEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityCommentEntity item) {
        helper.setText(R.id.tv_name , item.getFullName())
                .setText(R.id.tv_content , item.getContent())
                .setText(R.id.tv_time , item.getCreateTime())
                .setText(R.id.tv_good , item.getAssist()+"");

        if(item.getIsstar() == 0){
            helper.setImageResource(R.id.iv_good , R.mipmap.icon_praise);
        }else {
            helper.setImageResource(R.id.iv_good , R.mipmap.icon_praise_sel);
        }

        helper.addOnClickListener(R.id.ll_good);

    }
}
