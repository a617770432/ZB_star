package com.xingqiuzhibo.phonelive.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleAdapter;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ScreenDimenUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

/**
 * Created by cxf on 2018/10/10.
 */

public class LiveTxPlayViewHolder extends AbsViewHolder implements ITXLivePlayListener {

    private static final String TAG = "LivePlayViewHolder";
    private TXCloudVideoView mVideoView;
    private View mLoading;
    private ImageView mCover;
    private TXLivePlayer mPlayer;
    private boolean mPaused;//是否切后台了
    private boolean mStartPlay;//是否开始了播放
    private boolean mEndPlay;//是否结束了播放
    private boolean mClickPausePlay;//是否手动暂停了播放
    private boolean mPausePlay;//是否被动暂停了播放
    private boolean mFirstFrame;//第一帧播放成功


    public LiveTxPlayViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_play_tx;
    }

    @Override
    public void init() {
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLoading = findViewById(R.id.loading);
        mCover = (ImageView) findViewById(R.id.cover);
        mVideoView.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer = new TXLivePlayer(mContext);
        mPlayer.setPlayerView(mVideoView);
        mPlayer.setAutoPlay(true);
        mPlayer.enableHardwareDecode(false);
        mPlayer.setPlayListener(this);
//        TXLivePlayConfig config = new TXLivePlayConfig();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Referer", "qcloud.com");
//        config.setHeaders(headers);
//        config.setAutoAdjustCacheTime(true);
//        config.setMaxAutoAdjustCacheTime(5);
//        config.setMinAutoAdjustCacheTime(1);
//        mPlayer.setConfig(config);
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onResume() {
                if (mPaused && mPlayer != null) {
                    mPlayer.resume();
                }
                mPaused = false;
            }

            @Override
            public void onPause() {
                if (mPlayer != null) {
                    mPlayer.pause();
                }
                mPaused = true;
            }

            @Override
            public void onDestroy() {
                release();
            }
        };
    }

    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        if (mEndPlay) {
            return;
        }
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN://播放开始
                if (mLoading != null && mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                if (mLoading != null && mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME://第一帧
                hideCover();
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END://播放结束
                replay();
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION://获取视频宽高
                mFirstFrame = true;
                int width = bundle.getInt("EVT_PARAM1", 0);
                int height = bundle.getInt("EVT_PARAM2", 0);
                L.e(TAG, "流---width----->" + width);
                L.e(TAG, "流---height----->" + height);
                if (mVideoView != null && width >= height) {//横屏视频
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                    float rate = ((float) width) / height;
                    params.height = (int) (ScreenDimenUtil.getInstance().getScreenWdith() / rate);
                    mVideoView.requestLayout();
                }
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT://播放失败
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                ToastUtil.show(WordUtil.getString(R.string.live_play_error));
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }


    private void hideCover() {
        if (mCover != null) {
            mCover.animate().alpha(0).setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mCover != null) {
                                mCover.setVisibility(View.INVISIBLE);
                                mCover.setAlpha(1f);
                            }
                        }
                    })
                    .start();
        }
    }

    /**
     * 循环播放
     */
    private void replay() {
        if (mStartPlay && mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }

    public void setCover(String coverUrl) {
        ImgLoader.displayDrawable(coverUrl, new ImgLoader.DrawableCallback() {
            @Override
            public void callback(Drawable drawable) {
                if (!mStartPlay) {
                    if (mCover != null && mCover.getVisibility() == View.VISIBLE) {
                        mCover.setImageDrawable(drawable);
                    }
                }
            }
        });
    }

    /**
     * 开始播放
     *
     * @param url 流地址
     */
    public void play(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        int playType = -1;
        if (url.endsWith(".flv")) {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        } else if (url.startsWith("rtmp")) {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        } else if (url.endsWith(".m3u8")) {
            playType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
        } else if (url.endsWith(".mp4")) {
            playType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
        }
        if (playType == -1) {
            ToastUtil.show(WordUtil.getString(R.string.live_play_error_2));
            return;
        }
        mFirstFrame = false;
        mEndPlay = false;
        mClickPausePlay = false;
        mPausePlay = false;
        if (mStartPlay) {
            mPlayer.stopPlay(false);
        }
        int res = mPlayer.startPlay(url, playType);
        L.e(TAG, "play----res--->" + res);
        mStartPlay = true;
        L.e(TAG, "play----url--->" + url);
    }


    public void release() {
        mStartPlay = false;
        if (!mEndPlay) {
            if (mPlayer != null) {
                mPlayer.stopPlay(true);
            }
            if (mVideoView != null) {
                mVideoView.onDestroy();
            }
        }
        mEndPlay = true;
        L.e(TAG, "release------->");
    }


}
