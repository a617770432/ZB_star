package com.xingqiuzhibo.phonelive.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.TieZiTabAdapter;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.utils.GlideSimpleLoader;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CircleDetailActivity extends AbsActivity implements MessagePicturesLayout.Callback{

//    @BindView(R.id.expandable_text)
//    TextView expandableText;
//    @BindView(R.id.expand_collapse)
//    ImageButton expandCollapse;
//    @BindView(R.id.expand_text_view)
//    ExpandableTextView expandTextView;
    @BindView(R.id.head_recycler)
    RecyclerView headRecycler;
    @BindView(R.id.iv_head)
    AppCompatImageView ivHead;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void main() {
        super.main();
        unbinder = ButterKnife.bind(this);

        List<String> tabList = new ArrayList<>();
        tabList.add("全部");
        tabList.add("说说");
        tabList.add("图文");
        tabList.add("视频");
        TieZiTabAdapter adapter = new TieZiTabAdapter(getSupportFragmentManager(), tabList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(tabList.size()-1);
        slidingTab.setViewPager(viewPager);

        //  **************  动态 addView   **************
        iwHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setErrorImageRes(R.mipmap.error_picture);// 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API


    }

    @OnClick({R.id.iv_add, R.id.tv_focus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                break;
            case R.id.tv_focus:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (!iwHelper.handleBackPressed()) {
            super.onBackPressed();
        }

    }

    //图片点击放大
    private ImageWatcherHelper iwHelper;

    @Override
    public void onThumbPictureClick(ImageView i, SparseArray<ImageView> imageGroupList, List<Uri> urlList , int layoutPosition) {
        //图片点击放大点击事件
        iwHelper.show(i, imageGroupList, urlList);
        EventBus.getDefault().post(layoutPosition);

    }
}
