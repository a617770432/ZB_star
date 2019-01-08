package com.xingqiuzhibo.phonelive.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.VerticalViewPagerAdapter;
import com.xingqiuzhibo.phonelive.custom.SeeVerticalViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class SeeActivity extends AppCompatActivity {

    @BindView(R.id.vvp_back_play)
    SeeVerticalViewPager vvpBackPlay;
    @BindView(R.id.srl_page)
    SmartRefreshLayout srlPage;
    private List<String> urlList;
    private VerticalViewPagerAdapter pagerAdapter;

    private ImmersionBar mImmersionBar; //沉浸式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        addListener();
    }

    private void addListener() {
        srlPage.setEnableAutoLoadMore(false);
        srlPage.setEnableLoadMore(false);
        srlPage.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srlPage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        urlList.addAll(urlList);
                        pagerAdapter.setUrlList(urlList);
                        pagerAdapter.notifyDataSetChanged();

                        srlPage.finishLoadMore();
                    }
                }, 2000);

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }

    private void initView() {
        makeData();

        mImmersionBar = ImmersionBar.with(this);

        pagerAdapter = new VerticalViewPagerAdapter(getSupportFragmentManager());
        vvpBackPlay.setVertical(true);
        vvpBackPlay.setOffscreenPageLimit(10);
        pagerAdapter.setUrlList(urlList);
        vvpBackPlay.setAdapter(pagerAdapter);
        vvpBackPlay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == urlList.size() - 1) {
                    srlPage.setEnableAutoLoadMore(true);
                    srlPage.setEnableLoadMore(true);
                } else {
                    srlPage.setEnableAutoLoadMore(false);
                    srlPage.setEnableLoadMore(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();

        EventBus.getDefault().unregister(this);
    }

    private void makeData() {
        urlList = new ArrayList<>();
        urlList.add("http://ips.ifeng.com/video19.ifeng.com/video09/2018/03/22/23144063-102-009-101713.mp4?vid=f42214fd-d4b8-41ad-9d50-c55a11020796&uid=XAC9bf&from=v_Free&pver=vHTML5Player_v2.0.0&sver=&se=%E7%A7%91%E6%8A%80%E5%BE%AE%E8%BF%85&cat=118-123&ptype=118&platform=pc&sourceType=h5&dt=1521684973000&gid=nKY0UWgnmNgH&sign=fc3fddd61afb4e8b5e64a6e7ef0a93e4&tm=1546852554762");
        urlList.add("http://ips.ifeng.com/video19.ifeng.com/video09/2018/03/22/23144063-102-009-101713.mp4?vid=f42214fd-d4b8-41ad-9d50-c55a11020796&uid=XAC9bf&from=v_Free&pver=vHTML5Player_v2.0.0&sver=&se=%E7%A7%91%E6%8A%80%E5%BE%AE%E8%BF%85&cat=118-123&ptype=118&platform=pc&sourceType=h5&dt=1521684973000&gid=nKY0UWgnmNgH&sign=fc3fddd61afb4e8b5e64a6e7ef0a93e4&tm=1546852554762");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer event) {
        //处理逻辑
        if(event == 1) onBackPressed();
    }

}
