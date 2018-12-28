package com.xingqiuzhibo.phonelive.views;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautySpecialEffectsFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.LiveConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.ConfigBean;
import com.xingqiuzhibo.phonelive.beauty.EffectListener;
import com.xingqiuzhibo.phonelive.beauty.TiFilter;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleAdapter;
import com.xingqiuzhibo.phonelive.interfaces.LivePushListener;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import java.util.List;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.TiSDKManagerBuilder;
import cn.tillusory.sdk.bean.TiFilterEnum;

/**
 * Created by cxf on 2018/10/7.
 * 直播推流
 */

public class LivePushViewHolder extends AbsViewHolder implements LiveLinkMicViewHolder,
        KSYStreamer.OnInfoListener, KSYStreamer.OnErrorListener, StatsLogReport.OnLogEventListener {

    private static final String TAG = "LivePushViewHolder";
    private ViewGroup mBigContainer;
    private ViewGroup mSmallContainer;
    private ViewGroup mLeftContainer;
    private ViewGroup mRightContainer;
    private ViewGroup mPkContainer;
    private GLSurfaceView mPreView;
    private KSYStreamer mStreamer;//金山推流器
    private LivePushListener mLivePushListener;
    private boolean mCameraFront;//是否是前置摄像头
    private boolean mFlashOpen;//闪光灯是否开启了
    private boolean mOpenCamera;//是否选择了相机
    private boolean mPaused;
    private boolean mStartPush;
    //萌颜效果回调
    private EffectListener mEffectListener;
    //各种萌颜效果
    private TiSDKManager mTiSDKManager;
    //倒计时
    private TextView mCountDownText;
    private int mCountDownCount = 3;


    private ImgFilterBase mFilter;//美颜 滤镜
    private float mMopiVal;//磨皮
    private float mMeibaiVal;//美白
    private float mHongRunVal;//红润

    public LivePushViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_push;
    }

    @Override
    public void init() {
        mBigContainer = (ViewGroup) findViewById(R.id.big_container);
        mSmallContainer = (ViewGroup) findViewById(R.id.small_container);
        mLeftContainer = (ViewGroup) findViewById(R.id.left_container);
        mRightContainer = (ViewGroup) findViewById(R.id.right_container);
        mPkContainer = (ViewGroup) findViewById(R.id.pk_container);
        mPreView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mStreamer = new KSYStreamer(mContext);
        mStreamer.setPreviewFps(LiveConfig.PUSH_FRAME_RATE);//预览采集帧率
        mStreamer.setTargetFps(LiveConfig.PUSH_FRAME_RATE);//推流采集帧率
        mStreamer.setVideoKBitrate(LiveConfig.PUSH_VIDEO_BITRATE * 3 / 4, LiveConfig.PUSH_VIDEO_BITRATE, LiveConfig.PUSH_VIDEO_BITRATE / 4);//视频码率
        mStreamer.setAudioKBitrate(LiveConfig.PUSH_AUDIO_BITRATE);//音频码率
        mStreamer.setCameraCaptureResolution(LiveConfig.PUSH_CAP_RESOLUTION);//采集分辨率
        mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_1080P);//预览分辨率
        mStreamer.setTargetResolution(LiveConfig.PUSH_VIDEO_RESOLUTION);//推流分辨率
        mStreamer.setVideoCodecId(LiveConfig.PUSH_ENCODE_TYPE);//H264
        mStreamer.setEncodeMethod(LiveConfig.PUSH_ENCODE_METHOD);//软编
        mStreamer.setVideoEncodeScene(LiveConfig.PUSH_ENCODE_SCENE);//秀场模式
        mStreamer.setVideoEncodeProfile(LiveConfig.PUSH_ENCODE_PROFILE);
        mStreamer.setAudioChannels(2);//双声道推流
        mStreamer.setVoiceVolume(2f);
        mStreamer.setEnableRepeatLastFrame(false);  // 切后台的时候不使用最后一帧
        mStreamer.setEnableAutoRestart(true, 3000); // 自动重启推流
        mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);
        mCameraFront = true;
        mStreamer.setFrontCameraMirror(true);
        mStreamer.setOnInfoListener(this);
        mStreamer.setOnErrorListener(this);
        mStreamer.setOnLogEventListener(this);

        //萌颜的美颜
//        mTiSDKManager = new TiSDKManagerBuilder().build();
//        mTiSDKManager.setBeautyEnable(true);
//        mTiSDKManager.setFaceTrimEnable(true);
//        ConfigBean configBean = AppConfig.getInstance().getConfig();
//        if (configBean != null) {
//            mTiSDKManager.setSkinWhitening(configBean.getBeautyMeiBai());//美白
//            mTiSDKManager.setSkinBlemishRemoval(configBean.getBeautyMoPi());//磨皮
//            mTiSDKManager.setSkinSaturation(configBean.getBeautyBaoHe());//饱和
//            mTiSDKManager.setSkinTenderness(configBean.getBeautyFenNen());//粉嫩
//            mTiSDKManager.setEyeMagnifying(configBean.getBeautyBigEye());//大眼
//            mTiSDKManager.setChinSlimming(configBean.getBeautyFace());//瘦脸
//        } else {
//            mTiSDKManager.setSkinWhitening(0);//美白
//            mTiSDKManager.setSkinBlemishRemoval(0);//磨皮
//            mTiSDKManager.setSkinSaturation(0);//饱和
//            mTiSDKManager.setSkinTenderness(0);//粉嫩
//            mTiSDKManager.setEyeMagnifying(0);//大眼
//            mTiSDKManager.setChinSlimming(0);//瘦脸
//        }
//        mTiSDKManager.setSticker("");
//        mTiSDKManager.setFilterEnum(TiFilterEnum.NO_FILTER);
//        mStreamer.getImgTexFilterMgt().setFilter(new TiFilter(mTiSDKManager, mStreamer.getGLRender()));
        mStreamer.setDisplayPreview(mPreView);
        mStreamer.startCameraPreview();//启动预览
        setDefaultFilter();
        mLifeCycleListener = new LifeCycleAdapter() {

            @Override
            public void onReStart() {
                if (mOpenCamera) {
                    mOpenCamera = false;
                    mStreamer.startCameraPreview();//启动预览
                }
            }


            @Override
            public void onDestroy() {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.destroy();
//                }
                L.e(TAG, "LifeCycle------>onDestroy");
            }
        };

//        mEffectListener = new EffectListener() {
//            @Override
//            public void onFilterChanged(TiFilterEnum tiFilterEnum) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setFilterEnum(tiFilterEnum);
//                }
//            }
//
//            @Override
//            public void onMeiBaiChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setSkinWhitening(progress);
//                }
//            }
//
//            @Override
//            public void onMoPiChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setSkinBlemishRemoval(progress);
//                }
//            }
//
//            @Override
//            public void onBaoHeChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setSkinSaturation(progress);
//                }
//            }
//
//            @Override
//            public void onFengNenChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setSkinTenderness(progress);
//                }
//            }
//
//            @Override
//            public void onBigEyeChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setEyeMagnifying(progress);
//                }
//            }
//
//            @Override
//            public void onFaceChanged(int progress) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setChinSlimming(progress);
//                }
//            }
//
//            @Override
//            public void onTieZhiChanged(String tieZhiName) {
//                if (mTiSDKManager != null) {
//                    mTiSDKManager.setSticker(tieZhiName);
//                }
//            }
//        };
    }

    @Override
    public void onInfo(int what, int msg1, int msg2) {
        switch (what) {
            case 1000://初始化完毕
                L.e(TAG, "mStearm--->初始化完毕");
                if (mLivePushListener != null) {
                    mLivePushListener.onPreviewStart();
                }
                break;
            case 0://推流成功
                L.e(TAG, "mStearm--->推流成功");
                if(!mStartPush){
                    mStartPush = true;
                    if (mLivePushListener != null) {
                        mLivePushListener.onPushStart();
                    }
                }
                break;
        }
    }

    @Override
    public void onError(int what, int msg1, int msg2) {
        boolean needStopPushStream=false;//是否需要停止推流
        switch (what) {
            case -1009://推流url域名解析失败
                L.e(TAG, "mStearm--->推流url域名解析失败");
                break;
            case -1006://网络连接失败，无法建立连接
                L.e(TAG, "mStearm--->网络连接失败，无法建立连接");
                break;
            case -1010://跟RTMP服务器完成握手后,推流失败
                L.e(TAG, "mStearm--->跟RTMP服务器完成握手后,推流失败");
                break;
            case -1007://网络连接断开
                L.e(TAG, "mStearm--->网络连接断开");
                break;
            case -2004://音视频采集pts差值超过5s
                L.e(TAG, "mStearm--->音视频采集pts差值超过5s");
                break;
            case -1004://编码器初始化失败
                L.e(TAG, "mStearm--->编码器初始化失败");
                needStopPushStream=true;
                break;
            case -1003://视频编码失败
                L.e(TAG, "mStearm--->视频编码失败");
                needStopPushStream=true;
                break;
            case -1008://音频初始化失败
                L.e(TAG, "mStearm--->音频初始化失败");
                needStopPushStream=true;
                break;
            case -1011://音频编码失败
                L.e(TAG, "mStearm--->音频编码失败");
                needStopPushStream=true;
                break;
            case -2001: //摄像头未知错误
                L.e(TAG, "mStearm--->摄像头未知错误");
                needStopPushStream=true;
                break;
            case -2002://打开摄像头失败
                L.e(TAG, "mStearm--->打开摄像头失败");
                needStopPushStream=true;
                break;
            case -2003://录音开启失败
                L.e(TAG, "mStearm--->录音开启失败");
                needStopPushStream=true;
                break;
            case -2005://录音开启未知错误
                L.e(TAG, "mStearm--->录音开启未知错误");
                needStopPushStream=true;
                break;
            case -2006://系统Camera服务进程退出
                L.e(TAG, "mStearm--->系统Camera服务进程退出");
                needStopPushStream=true;
                break;
            case -2007://Camera服务异常退出
                L.e(TAG, "mStearm--->Camera服务异常退出");
                needStopPushStream=true;
                break;
        }
        if (mStreamer != null) {
            if(needStopPushStream){
                mStreamer.stopCameraPreview();
            }
            if (mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        }
    }

    @Override
    public void onLogEvent(StringBuilder singleLogContent) {
        //打印推流信息
        //L.e("mStearm--->" + singleLogContent.toString());
    }

    public void setLivePushListener(LivePushListener livePushListener) {
        mLivePushListener = livePushListener;
    }


    /**
     * 切换镜头
     */
    public void toggleCamera() {
        if (mStreamer != null) {
            if (mFlashOpen) {
                toggleFlash();
            }
            mStreamer.switchCamera();
            mCameraFront = !mCameraFront;
        }
    }

    /**
     * 打开关闭闪光灯
     */
    public void toggleFlash() {
        if (mCameraFront) {
            ToastUtil.show(R.string.live_open_flash);
            return;
        }
        if (mStreamer != null) {
            CameraCapture capture = mStreamer.getCameraCapture();
            Camera.Parameters parameters = capture.getCameraParameters();
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {//如果闪光灯已开启
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//设置成关闭的
                mFlashOpen = false;
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//设置成开启的
                mFlashOpen = true;
            }
            capture.setCameraParameters(parameters);
        }
    }

    /**
     * 开始推流
     *
     * @param pushUrl 推流地址
     */
    public void startPush(String pushUrl) {
        if (mStreamer != null) {
            mStreamer.setUrl(pushUrl);
            mStreamer.startStream();
        }
        startCountDown();
    }

    public void pause() {
        mPaused = true;
        if (mStartPush && mStreamer != null) {
            mStreamer.onPause();
            // 切后台时，将SDK设置为离屏推流模式，继续采集camera数据
            mStreamer.setOffscreenPreview(mStreamer.getPreviewWidth(), mStreamer.getPreviewHeight());
        }
    }

    public void resume() {
        if (mPaused && mStartPush && mStreamer != null) {
            mStreamer.onResume();
        }
        mPaused = false;
    }

    public void release() {
        if (mCountDownText != null) {
            mCountDownText.clearAnimation();
        }
        mLivePushListener = null;
        if (mStreamer != null) {
            mStreamer.stopStream();
            mStreamer.stopCameraPreview();
            mStreamer.release();
            mStreamer.setOnInfoListener(null);
            mStreamer.setOnErrorListener(null);
            mStreamer.setOnLogEventListener(null);
        }
        mStreamer = null;
    }

    public EffectListener getEffectListener() {
        return mEffectListener;
    }


    public void setOpenCamera(boolean openCamera) {
        mOpenCamera = openCamera;
    }

    public KSYStreamer getStreamer() {
        return mStreamer;
    }


    @Override
    public ViewGroup getSmallContainer() {
        return mSmallContainer;
    }


    @Override
    public ViewGroup getRightContainer() {
        return mRightContainer;
    }

    @Override
    public ViewGroup getPkContainer() {
        return mPkContainer;
    }


    @Override
    public void changeToLeft() {
        if (mPreView != null && mLeftContainer != null) {
            ViewParent parent = mPreView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(mPreView);
            }
            int h = mPreView.getHeight() / 2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mPreView.getWidth() / 2, h);
            params.setMargins(0, (DpUtil.dp2px(250) - h) / 2, 0, 0);
            mPreView.setLayoutParams(params);
            mLeftContainer.addView(mPreView);
        }
    }

    @Override
    public void changeToBig() {
        if (mPreView != null && mBigContainer != null) {
            ViewParent parent = mPreView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(mPreView);
            }
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPreView.setLayoutParams(layoutParams);
            mBigContainer.addView(mPreView);
        }
    }


    /**
     * 开播的时候 3 2 1倒计时
     */
    private void startCountDown() {
        ViewGroup parent = (ViewGroup) mContentView;
        mCountDownText = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_count_down, parent, false);
        parent.addView(mCountDownText);
        mCountDownText.setText(String.valueOf(mCountDownCount));
        ScaleAnimation animation = new ScaleAnimation(3, 1, 3, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCountDownText!=null){
                    ViewGroup parent = (ViewGroup) mCountDownText.getParent();
                    if (parent != null) {
                        parent.removeView(mCountDownText);
                        mCountDownText = null;
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mCountDownCount--;
                if (mCountDownText!=null){
                    mCountDownText.setText(String.valueOf(mCountDownCount));
                }
            }
        });
        if (mCountDownText!=null){
            mCountDownText.startAnimation(animation);
        }
    }



    /*********************金山自带的美颜*******************/

    /**
     * 设置默认滤镜
     */
    public void setDefaultFilter() {
        //设置美颜模式
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO3);
        if (mFilter == null) {
            List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
            if (filters != null && filters.size() > 0) {
                mFilter = filters.get(0);
                mMopiVal = mFilter.getGrindRatio();
                mMeibaiVal = mFilter.getWhitenRatio();
                mHongRunVal = mFilter.getRuddyRatio();
            }
        } else {
            List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
            if (filters != null && filters.size() > 0) {
                mFilter = filters.get(0);
            }
            mFilter.setGrindRatio(mMopiVal);
            mFilter.setWhitenRatio(mMeibaiVal);
            mFilter.setRuddyRatio(mHongRunVal);
        }
    }

    /**
     * 创建特殊滤镜  等
     *
     * @param id 1 原图 2 清新 3 靓丽 4 甜美 5 怀旧
     */
    public void setSpecialFilter(int id) {
        if (id == 1) {
            setDefaultFilter();
            return;
        }
        int type = 0;
        switch (id) {
            case 2:
                type = ImgBeautySpecialEffectsFilter.KSY_SPECIAL_EFFECT_FRESHY;
                break;
            case 3:
                type = ImgBeautySpecialEffectsFilter.KSY_SPECIAL_EFFECT_BEAUTY;
                break;
            case 4:
                type = ImgBeautySpecialEffectsFilter.KSY_SPECIAL_EFFECT_SWEETY;
                break;
            case 5:
                type = ImgBeautySpecialEffectsFilter.KSY_SPECIAL_EFFECT_SEPIA;
                break;
        }
        ImgBeautySpecialEffectsFilter effectsFilter = new ImgBeautySpecialEffectsFilter(
                mStreamer.getGLRender(), mContext, type);
        effectsFilter.setGrindRatio(mMopiVal);
        effectsFilter.setWhitenRatio(mMeibaiVal);
        effectsFilter.setRuddyRatio(mHongRunVal);
        mStreamer.getImgTexFilterMgt().setFilter(effectsFilter);
    }

    /**
     * 获取当前美颜的数值
     *
     * @return
     */
    public int[] getBeautyData() {
        int[] data = null;
        if (mFilter != null) {
            data = new int[3];
            data[0] = (int) (mMopiVal * 100);//磨皮
            data[1] = (int) (mMeibaiVal * 100);//美白
            data[2] = (int) (mHongRunVal * 100);//红润
        }
        return data;
    }

    /**
     * 设置美颜数值
     *
     * @param type 美颜的类型   0 磨皮 1 美白 2 红润
     * @param val  0~1 的float数值
     */
    public void setBeautyData(int type, float val) {
        switch (type) {
            case 0:
                mMopiVal = val;
                L.e("磨皮--->" + mMopiVal);
                mFilter.setGrindRatio(mMopiVal);
                break;
            case 1:
                mMeibaiVal = val;
                L.e("美白--->" + mMeibaiVal);
                mFilter.setWhitenRatio(mMeibaiVal);
                break;
            case 2:
                mHongRunVal = val;
                L.e("红润--->" + mHongRunVal);
                mFilter.setRuddyRatio(mHongRunVal);
                break;
        }

    }

}
