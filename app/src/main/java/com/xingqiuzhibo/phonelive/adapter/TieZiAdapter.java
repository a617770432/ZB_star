package com.xingqiuzhibo.phonelive.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;

import java.util.ArrayList;
import java.util.List;

public class TieZiAdapter extends BaseQuickAdapter<TieZiBean , BaseViewHolder> {

    private MessagePicturesLayout.Callback callback;
    private MessagePicturesLayout.ImageCallback imageCallback;

    public TieZiAdapter(int layoutResId, @Nullable List<TieZiBean> data , MessagePicturesLayout.Callback callback, MessagePicturesLayout.ImageCallback imageCallback) {
        super(layoutResId, data);
        this.callback = callback;
        this.imageCallback = imageCallback;
    }

    @Override
    protected void convert(BaseViewHolder helper, TieZiBean item) {

        MessagePicturesLayout picLayout = helper.getView(R.id.pic_layout);
        picLayout.setCallback(callback);
        picLayout.setImageCallback(imageCallback);
        List<Uri> uriList = new ArrayList<>();
        uriList.add(Uri.parse("http://pic65.nipic.com/file/20150425/13839354_210311767000_2.jpg"));
        uriList.add(Uri.parse("http://pic65.nipic.com/file/20150425/13839354_210311767000_2.jpg"));
        picLayout.set(uriList , uriList ,helper.getLayoutPosition() , true , new boolean[]{true , false});

        helper.addOnClickListener(R.id.tv_report);

    }
}
