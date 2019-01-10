package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.utils.NetworkStateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VideoFragment extends NewBaseFragment implements NetworkFragment.ClickListener {

    @BindView(R.id.txv_video)
    TXCloudVideoView txvVideo;
    @BindView(R.id.iv_play_thun)
    ImageView ivPlayThun;
    @BindView(R.id.iv_play)
    AppCompatImageView ivPlay;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.ll_cost)
    LinearLayout llCost;
    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.iv_good)
    AppCompatImageView ivGood;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.tv_comments)
    TextView tvComments;
    @BindView(R.id.iv_head)
    AppCompatImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_remind_money)
    TextView tvRemindMoney;
    @BindView(R.id.ll_no)
    LinearLayout llNo;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;

    private TXVodPlayer mVodPlayer;
    private String url;
    public static final String URL = "URL";

    private ImmersionBar mImmersionBar; //沉浸式

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {

        url = getArguments().getString(URL);
        //创建player对象
        mVodPlayer = new TXVodPlayer(getActivity());
        //关键player对象与界面view
        mVodPlayer.setPlayerView(txvVideo);

        Glide.with(getActivity())
                .load("http://p2.so.qhimgs1.com/sdr/200_200_/t0165f50cc9801ae9fe.jpg")
                .into(ivPlayThun);
    }

    @Override
    protected void loadData() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();

        if (NetworkStateUtil.isNetWorkConnected(Objects.requireNonNull(getActivity())) && NetworkStateUtil.isMobileConnected(getActivity())) {
            NetworkFragment fragment = new NetworkFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            fragment.setClickListener(this);
            fragment.show(getChildFragmentManager(), "");
        } else {
            mVodPlayer.startPlay(url);
            ivPlay.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mVodPlayer == null) {
            return;
        }
        if (isVisibleToUser) {
            mVodPlayer.resume();
        } else {
            mVodPlayer.pause();
        }

    }

    @Override
    public void onResume() {

        super.onResume();
        if (mVodPlayer != null) {
            mVodPlayer.resume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVodPlayer != null) {
            mVodPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVodPlayer != null) {
            // true代表清除最后一帧画面
            mVodPlayer.stopPlay(true);
        }
        if (txvVideo != null) {
            txvVideo.onDestroy();
        }
    }

    @Override
    public void setClick(int type) {
        //判断用户点击的是暂停还是继续。
        if (type == 0) {
            //暂停
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            //继续
            mVodPlayer.startPlay(url);
            ivPlay.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_report, R.id.ll_good, R.id.tv_comments, R.id.tv_focus
            , R.id.iv_play , R.id.iv_back , R.id.iv_back_white , R.id.tv_get , R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_report:
                break;
            case R.id.ll_good:
                break;
            case R.id.tv_comments:
                //评论
                CommentsFragment fragment = new CommentsFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.show(getChildFragmentManager(), "");
                break;
            case R.id.tv_focus:
                break;
            case R.id.iv_play:
                mVodPlayer.startPlay(url);
                ivPlay.setVisibility(View.GONE);
                break;
            case R.id.iv_back:
            case R.id.iv_back_white:
                EventBus.getDefault().post(1);
                if (mVodPlayer != null) {
                    // true代表清除最后一帧画面
                    mVodPlayer.stopPlay(true);
                }
                if (txvVideo != null) {
                    txvVideo.onDestroy();
                }
                break;
            case R.id.tv_get:
                //赚取钻石
                break;
            case R.id.tv_pay:
                //充值钻石
                break;
        }
    }
}
