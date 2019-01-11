package com.xingqiuzhibo.phonelive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.TermInfoEntity;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;

import java.util.ArrayList;
import java.util.List;

public class TieZiAdapter extends BaseQuickAdapter<TermInfoEntity, BaseViewHolder> {

    private MessagePicturesLayout.Callback callback;
    private MessagePicturesLayout.ImageCallback imageCallback;
    private Context context;

    //由于很多地方都复用了，大部分逻辑可以用，但是在详情里面需要去掉底部点赞那一条布局  暂定1是详情
    private int openType;

    public TieZiAdapter(int layoutResId, @Nullable List<TermInfoEntity> data , MessagePicturesLayout.Callback callback
            , MessagePicturesLayout.ImageCallback imageCallback , Context context , int openType) {
        super(layoutResId, data);
        this.callback = callback;
        this.imageCallback = imageCallback;
        this.context = context;
        this.openType = openType;
    }

    @Override
    protected void convert(BaseViewHolder helper, TermInfoEntity item) {

        helper.setText(R.id.tv_menu , item.getOneRangeName())
                .setText(R.id.tv_comments , item.getCommentCount()+"")
                .setText(R.id.tv_goods , item.getAssist()+"")
                .setText(R.id.tv_views , item.getHits()+"次浏览  收费：" + item.getAmount() +"钻石")
                .setText(R.id.tv_content , item.getContent())
                .setText(R.id.tv_name , item.getUname())
                .setText(R.id.tv_num , item.getHits()+"次播放");

        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView tvContent = helper.getView(R.id.tv_content);
        RelativeLayout rlVideo = helper.getView(R.id.rl_video);
        ImageView ivVideo = helper.getView(R.id.iv_video);

        LinearLayout llBottom = helper.getView(R.id.ll_bottom);
        if(openType == 1){
            llBottom.setVisibility(View.GONE);
        }else {
            llBottom.setVisibility(View.VISIBLE);
        }

        if(item.getIsstar() == 0){
            helper.setImageResource(R.id.iv_goods , R.mipmap.icon_praise);
        }else {
            helper.setImageResource(R.id.iv_goods , R.mipmap.icon_praise_01sel);
        }

        if(item.getIsfocus() == 0 ){
            helper.setText(R.id.tv_focus , "+ 关注");
        }else {
            helper.setText(R.id.tv_focus , "已关注");
        }

        if(item.getFiletype() == 0 ){
            //文章
            tvTitle.setVisibility(View.VISIBLE);
            tvContent.setTextColor(Color.parseColor("#666666"));
            tvTitle.setText(item.getTitle());
            rlVideo.setVisibility(View.GONE);


        }else if(item.getFiletype() == 1 ){
            //图文
            tvTitle.setVisibility(View.GONE);
            tvContent.setTextColor(Color.parseColor("#333333"));
            rlVideo.setVisibility(View.GONE);

            MessagePicturesLayout picLayout = helper.getView(R.id.pic_layout);
            picLayout.setCallback(callback);
            picLayout.setImageCallback(imageCallback);
            List<Uri> uriList = new ArrayList<>();
            for (int i = 0 ; i < item.getImgList().size() ; i ++ ){
                uriList.add(Uri.parse(item.getImgList().get(i)));
            }
            //0  免费  1 收费

            if(item.getIsFree() == 0 ){
                picLayout.set(uriList , uriList ,helper.getLayoutPosition() , false , null);
            }else {
                //收费 要变模糊，但是点击了一遍以后就不要模糊了。
                picLayout.set(uriList , uriList ,helper.getLayoutPosition() , true , null);
            }

        }else {
            //视频
            tvTitle.setVisibility(View.GONE);
            tvContent.setTextColor(Color.parseColor("#333333"));
            rlVideo.setVisibility(View.VISIBLE);

            Glide.with(context).load(item.getImgList().get(0)).into(ivVideo);
        }

        helper.addOnClickListener(R.id.ll_report)
                .addOnClickListener(R.id.iv_play)
                .addOnClickListener(R.id.ll_goods)
                .addOnClickListener(R.id.tv_focus);


    }
}
