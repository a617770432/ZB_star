package com.xingqiuzhibo.phonelive.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.CommentBean;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;

import java.util.ArrayList;
import java.util.List;

public class DialogCommentsAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {


    public DialogCommentsAdapter(int layoutResId, @Nullable List<CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {


    }
}
