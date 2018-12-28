package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.LiveBean;
import com.xingqiuzhibo.phonelive.bean.LiveGuardInfo;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.beauty.LiveBeautyViewHolder;
import com.xingqiuzhibo.phonelive.beauty.LiveEffectFragment;
import com.xingqiuzhibo.phonelive.dialog.LiveFunctionDialogFragment;
import com.xingqiuzhibo.phonelive.dialog.LiveLinkMicListDialogFragment;
import com.xingqiuzhibo.phonelive.event.GameWindowEvent;
import com.xingqiuzhibo.phonelive.event.LoginInvalidEvent;
import com.xingqiuzhibo.phonelive.game.GamePresenter;
import com.xingqiuzhibo.phonelive.game.bean.GameParam;
import com.xingqiuzhibo.phonelive.game.dialog.GameDialogFragment;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.LiveFunctionClickListener;
import com.xingqiuzhibo.phonelive.interfaces.LivePushListener;
import com.xingqiuzhibo.phonelive.music.LiveMusicDialogFragment;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicAnchorPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicPkPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicPresenter;
import com.xingqiuzhibo.phonelive.socket.SocketClient;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.xingqiuzhibo.phonelive.views.LiveAnchorViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveEndViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveMusicViewHolder;
import com.xingqiuzhibo.phonelive.views.LivePushViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveReadyViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveRoomViewHolder;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by cxf on 2018/10/7.
 * 主播直播间
 */

public class LiveAnchorActivity extends LiveActivity implements LiveFunctionClickListener {

    private ViewGroup mRoot;
    private ViewGroup mContainerWrap;
    private LivePushViewHolder mLivePushViewHolder;
    public LiveReadyViewHolder mLiveReadyViewHolder;
    //    private LiveBeautyViewHolder mLiveBeautyViewHolder;
    private LiveAnchorViewHolder mLiveAnchorViewHolder;
    private LiveMusicViewHolder mLiveMusicViewHolder;
    private boolean mStartPreview;//是否开始预览
    private boolean mStartLive;//是否开始了直播
    private List<Integer> mGameList;//游戏开关
    private LiveEffectFragment mLiveEffectFragment;//美颜滤镜


    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_anchor;
    }

    @Override
    protected void main() {
        super.main();
        mRoot = (ViewGroup) findViewById(R.id.root);
        mSocketUserType = Constants.SOCKET_USER_TYPE_ANCHOR;
        UserBean u = AppConfig.getInstance().getUserBean();
        mLiveUid = u.getId();
        mLiveBean = new LiveBean();
        mLiveBean.setUid(mLiveUid);
        mLiveBean.setUserNiceName(u.getUserNiceName());
        mLiveBean.setAvatar(u.getAvatar());
        mLiveBean.setAvatarThumb(u.getAvatarThumb());
        mLiveBean.setLevelAnchor(u.getLevelAnchor());
        mLiveBean.setGoodNum(u.getLiang().getName());
        mLiveBean.setCity(u.getCity());
        //添加推流预览控件
        mLivePushViewHolder = new LivePushViewHolder(mContext, (ViewGroup) findViewById(R.id.preview_container));
        mLivePushViewHolder.setLivePushListener(new LivePushListener() {
            @Override
            public void onPreviewStart() {
                //开始预览回调
                mStartPreview = true;
            }

            @Override
            public void onPushStart() {
                //开始推流回调
                HttpUtil.changeLive(mStream);
            }

            @Override
            public void onPushFailed() {
                //推流失败回调
                if (mLiveRoomViewHolder != null) {
                    ToastUtil.show(R.string.live_push_failed);
                }

            }
        });
        mLivePushViewHolder.addToParent();
        addLifeCycleListener(mLivePushViewHolder.getLifeCycleListener());
        mContainerWrap = (ViewGroup) findViewById(R.id.container_wrap);
        mContainer = (ViewGroup) findViewById(R.id.container);
        //添加开播前设置控件
        mLiveReadyViewHolder = new LiveReadyViewHolder(mContext, mContainer);
        mLiveReadyViewHolder.addToParent();
        addLifeCycleListener(mLiveReadyViewHolder.getLifeCycleListener());
        mLiveLinkMicPresenter = new LiveLinkMicPresenter(mContext, mLivePushViewHolder, true, mContainer);
        mLiveLinkMicAnchorPresenter = new LiveLinkMicAnchorPresenter(mContext, mLivePushViewHolder, true, mContainer);
        mLiveLinkMicPkPresenter = new LiveLinkMicPkPresenter(mContext, mLivePushViewHolder, true, mContainer);
    }

    public boolean isStartPreview() {
        return mStartPreview;
    }

    /**
     * 主播直播间功能按钮点击事件
     *
     * @param functionID
     */
    @Override
    public void onClick(int functionID) {
        switch (functionID) {
            case Constants.LIVE_FUNC_BEAUTY://美颜
                beauty();
                break;
            case Constants.LIVE_FUNC_CAMERA://切换镜头
                toggleCamera();
                break;
            case Constants.LIVE_FUNC_FLASH://切换闪光灯
                toggleFlash();
                break;
            case Constants.LIVE_FUNC_MUSIC://伴奏
                openMusicWindow();
                break;
            case Constants.LIVE_FUNC_SHARE://分享
                openShareWindow();
                break;
            case Constants.LIVE_FUNC_GAME://游戏
                openGameWindow();
                break;
            case Constants.LIVE_FUNC_RED_PACK://红包
                openRedPackSendWindow();
                break;
            case Constants.LIVE_FUNC_LINK_MIC://连麦
                openLinkMicAnchorWindow();
                break;
        }
    }

    //打开相机前执行
    public void beforeCamera() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.setOpenCamera(true);
        }
    }


    /**
     * 切换镜头
     */
    public void toggleCamera() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.toggleCamera();
        }
    }

    /**
     * 切换闪光灯
     */
    public void toggleFlash() {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.toggleFlash();
        }
    }

    /**
     * 设置美颜
     */
    public void beauty() {
//        if (mLiveBeautyViewHolder == null) {
//            mLiveBeautyViewHolder = new LiveBeautyViewHolder(mContext, mContainer);
//            mLiveBeautyViewHolder.setVisibleListener(new LiveBeautyViewHolder.VisibleListener() {
//                @Override
//                public void onVisibleChanged(boolean visible) {
//                    if (mLiveReadyViewHolder != null) {
//                        if (visible) {
//                            mLiveReadyViewHolder.hide();
//                        } else {
//                            mLiveReadyViewHolder.show();
//                        }
//                    }
//                }
//            });
//            if (mLivePushViewHolder != null) {
//                mLiveBeautyViewHolder.setEffectListener(mLivePushViewHolder.getEffectListener());
//            }
//        }
//        mLiveBeautyViewHolder.show();
        if (mLiveEffectFragment == null) {
            mLiveEffectFragment = new LiveEffectFragment();
        }
        mLiveEffectFragment.show(getSupportFragmentManager(), "LiveEffectFragment");
        if (mLiveReadyViewHolder != null) {
            mLiveReadyViewHolder.hide();
        }
    }

    /**
     * 打开音乐窗口
     */
    private void openMusicWindow() {
        LiveMusicDialogFragment fragment = new LiveMusicDialogFragment();
        fragment.setActionListener(new LiveMusicDialogFragment.ActionListener() {
            @Override
            public void onChoose(String musicId) {
                if (mLivePushViewHolder != null) {
                    if (mLiveMusicViewHolder == null) {
                        mLiveMusicViewHolder = new LiveMusicViewHolder(mContext, mContainer, mLivePushViewHolder.getStreamer());
                        addLifeCycleListener(mLiveMusicViewHolder.getLifeCycleListener());
                        mLiveMusicViewHolder.addToParent();
                        mLiveMusicViewHolder.setCloseCallback(new Runnable() {
                            @Override
                            public void run() {
                                if (mLiveMusicViewHolder != null) {
                                    mLiveMusicViewHolder.release();
                                }
                                mLiveMusicViewHolder = null;
                            }
                        });
                    }
                    mLiveMusicViewHolder.play(musicId);
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "LiveMusicDialogFragment");
    }

    /**
     * 打开功能弹窗
     */
    public void showFunctionDialog() {
        LiveFunctionDialogFragment fragment = new LiveFunctionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.HAS_GAME, mGameList.size() > 0);
        fragment.setArguments(bundle);
        fragment.setFunctionClickListener(this);
        fragment.show(getSupportFragmentManager(), "LiveFunctionDialogFragment");
    }

    /**
     * 打开主播连麦窗口
     */
    private void openLinkMicAnchorWindow() {
        if (mLiveLinkMicAnchorPresenter != null && !mLiveLinkMicAnchorPresenter.canOpenLinkMicAnchor()) {
            return;
        }
        LiveLinkMicListDialogFragment fragment = new LiveLinkMicListDialogFragment();
        fragment.show(getSupportFragmentManager(), "LiveLinkMicListDialogFragment");
    }


    /**
     * 打开选择游戏窗口
     */
    private void openGameWindow() {
        if (isLinkMic() || isLinkMicAnchor()) {
            ToastUtil.show(R.string.live_link_mic_cannot_game);
            return;
        }
        if (mGamePresenter != null) {
            GameDialogFragment fragment = new GameDialogFragment();
            fragment.setGamePresenter(mGamePresenter);
            fragment.show(getSupportFragmentManager(), "GameDialogFragment");
        }
    }

    /**
     * 关闭游戏
     */
    public void closeGame() {
        if (mGamePresenter != null) {
            mGamePresenter.closeGame();
        }
    }


    /**
     * 开播成功
     *
     * @param data createRoom返回的数据
     */
    public void startLiveSuccess(String data, int liveType, int liveTypeVal) {
        mLiveType = liveType;
        mLiveTypeVal = liveTypeVal;
        //处理createRoom返回的数据
        JSONObject obj = JSON.parseObject(data);
        mStream = obj.getString("stream");
        mDanmuPrice = obj.getString("barrage_fee");
        mShutTime = obj.getString("shut_time");
        String playUrl = obj.getString("pull");
        mLiveBean.setPull(playUrl);
        //移除开播前的设置控件，添加直播间控件
        if (mLiveReadyViewHolder != null) {
            mLiveReadyViewHolder.removeFromParent();
            mLiveReadyViewHolder.release();
        }
        mLiveReadyViewHolder = null;
        if (mLiveRoomViewHolder == null) {
            mLiveRoomViewHolder = new LiveRoomViewHolder(mContext, mContainer, (GifImageView) findViewById(R.id.gift_gif));
            mLiveRoomViewHolder.addToParent();
            addLifeCycleListener(mLiveRoomViewHolder.getLifeCycleListener());
            mLiveRoomViewHolder.setLiveInfo(mLiveUid, mStream, obj.getIntValue("userlist_time") * 1000);
            mLiveRoomViewHolder.setVotes(obj.getString("votestotal"));
            UserBean u = AppConfig.getInstance().getUserBean();
            if (u != null) {
                mLiveRoomViewHolder.setRoomNum(u.getLiangNameTip());
                mLiveRoomViewHolder.setName(u.getUserNiceName());
                mLiveRoomViewHolder.setAvatar(u.getAvatar());
                mLiveRoomViewHolder.setAnchorLevel(u.getLevelAnchor());
            }
        }
        if (mLiveAnchorViewHolder == null) {
            mLiveAnchorViewHolder = new LiveAnchorViewHolder(mContext, mContainer);
            mLiveAnchorViewHolder.addToParent();
            mLiveAnchorViewHolder.setUnReadCount(((LiveActivity) mContext).getImUnReadCount());
        }
        mLiveBottomViewHolder = mLiveAnchorViewHolder;

        //连接socket
        if (mSocketClient == null) {
            mSocketClient = new SocketClient(obj.getString("chatserver"), this);
            if (mLiveLinkMicPresenter != null) {
                mLiveLinkMicPresenter.setSocketClient(mSocketClient);
            }
            if (mLiveLinkMicAnchorPresenter != null) {
                mLiveLinkMicAnchorPresenter.setSocketClient(mSocketClient);
                mLiveLinkMicAnchorPresenter.setPlayUrl(playUrl);
            }
            if (mLiveLinkMicPkPresenter != null) {
                mLiveLinkMicPkPresenter.setSocketClient(mSocketClient);
                mLiveLinkMicPkPresenter.setLiveUid(mLiveUid);
            }
        }
        mSocketClient.connect(mLiveUid, mStream);

        //开始推流
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.startPush(obj.getString("push"));
        }
        //开始显示直播时长
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.startAnchorLiveTime();
        }
        mStartLive = true;
        mLiveRoomViewHolder.startRefreshUserList();

        //守护相关
        mLiveGuardInfo = new LiveGuardInfo();
        int guardNum = obj.getIntValue("guard_nums");
        mLiveGuardInfo.setGuardNum(guardNum);
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.setGuardNum(guardNum);
        }

        //游戏相关
        mGameList = JSON.parseArray(obj.getString("game_switch"), Integer.class);
        try {
            GameParam param = new GameParam();
            param.setContext(mContext);
            param.setParentView(mContainerWrap);
            param.setTopView(mContainer);
            param.setInnerContainer(mLiveRoomViewHolder.getInnerContainer());
            param.setSocketClient(mSocketClient);
            param.setLiveUid(mLiveUid);
            param.setStream(mStream);
            param.setAnchor(true);
            param.setCoinName(mCoinName);
            param.setObj(obj);
            mGamePresenter = new GamePresenter(param);
            mGamePresenter.setGameList(mGameList);
        } catch (Exception e) {

        }

    }

    /**
     * 关闭直播
     */
    public void closeLive() {
        DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_end_live), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                endLive();
            }
        });
    }

    /**
     * 结束直播
     */
    public void endLive() {
        //断开socket
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }
        //请求关播的接口
        HttpUtil.stopLive(mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (mLiveEndViewHolder == null) {
                        mLiveEndViewHolder = new LiveEndViewHolder(mContext, mRoot);
                        addLifeCycleListener(mLiveEndViewHolder.getLifeCycleListener());
                        mLiveEndViewHolder.addToParent();
                        mLiveEndViewHolder.showData(mLiveBean, mStream);
                    }
                    release();
                    mStartLive = false;
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.live_end_ing));
            }
        });
    }


    @Override
    public void onBackPressed() {
//        if (mLiveBeautyViewHolder != null && mLiveBeautyViewHolder.isShowed()) {
//            mLiveBeautyViewHolder.hide();
//            return;
//        }
        if (mStartLive) {
            if (!canBackPressed()) {
                return;
            }
            closeLive();
        } else {
            if (mLivePushViewHolder != null) {
                mLivePushViewHolder.release();
            }
            if (mLiveLinkMicPresenter != null) {
                mLiveLinkMicPresenter.release();
            }
            mLivePushViewHolder = null;
            mLiveLinkMicPresenter = null;
            superBackPressed();
        }
    }

    public void superBackPressed() {
        super.onBackPressed();
    }

    public void release() {
        HttpUtil.cancel(HttpConsts.CHANGE_LIVE);
        HttpUtil.cancel(HttpConsts.STOP_LIVE);
        HttpUtil.cancel(HttpConsts.LIVE_PK_CHECK_LIVE);
        if (mLiveReadyViewHolder != null) {
            mLiveReadyViewHolder.release();
        }
        if (mLiveMusicViewHolder != null) {
            mLiveMusicViewHolder.release();
        }
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.release();
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.release();
        }
//        if (mLiveBeautyViewHolder != null) {
//            mLiveBeautyViewHolder.release();
//        }
        if (mGamePresenter != null) {
            mGamePresenter.release();
        }
        mLiveMusicViewHolder = null;
        mLiveReadyViewHolder = null;
        mLivePushViewHolder = null;
        mLiveLinkMicPresenter = null;
//        mLiveBeautyViewHolder = null;
        mGamePresenter = null;
        super.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("LiveAnchorActivity-------onDestroy------->");
    }


    @Override
    protected void onPause() {
        if (mLiveMusicViewHolder != null) {
            mLiveMusicViewHolder.pause();
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.pause50s();
        }
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.pause();
        }
        super.onPause();
        sendSystemMessage(WordUtil.getString(R.string.live_anchor_leave));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.resume();
        }
        if (mLiveMusicViewHolder != null) {
            mLiveMusicViewHolder.resume();
        }
        //主播在50秒之内回来了
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.resume50s();
        }
        sendSystemMessage(WordUtil.getString(R.string.live_anchor_come_back));
    }

    /**
     * 超管关闭直播间
     */
    @Override
    public void onSuperCloseLive() {
        endLive();
        DialogUitl.showSimpleTipDialog(mContext, WordUtil.getString(R.string.live_illegal));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginInvalidEvent(LoginInvalidEvent e) {
        release();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameWindowEvent(GameWindowEvent e) {
        L.e("-----GameWindowEvent>>>" + e.isOpen());
        if (mLiveAnchorViewHolder != null) {
            mLiveAnchorViewHolder.setGameBtnVisible(e.isOpen());
        }
    }

    public void setBtnFunctionDark() {
        if (mLiveAnchorViewHolder != null) {
            mLiveAnchorViewHolder.setBtnFunctionDark();
        }
    }

    /**
     * 主播与主播连麦  主播收到其他主播发过来的连麦申请
     */
    @Override
    public void onLinkMicAnchorApply(UserBean u, String stream) {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorApply(u, stream);
        }
    }

    /**
     * 主播与主播连麦  对方主播拒绝连麦的回调
     */
    @Override
    public void onLinkMicAnchorRefuse() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorRefuse();
        }
    }

    /**
     * 主播与主播连麦  对方主播无响应的回调
     */
    @Override
    public void onLinkMicAnchorNotResponse() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicNotResponse();
        }
    }

    /**
     * 主播与主播连麦  对方主播正在忙的回调
     */
    @Override
    public void onLinkMicAnchorBusy() {
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.onLinkMicAnchorBusy();
        }
    }

    /**
     * 发起主播连麦申请
     *
     * @param pkUid  对方主播的uid
     * @param stream 对方主播的stream
     */
    public void linkMicAnchorApply(final String pkUid, String stream) {
        HttpUtil.livePkCheckLive(pkUid, stream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (mLiveLinkMicAnchorPresenter != null) {
                        mLiveLinkMicAnchorPresenter.applyLinkMicAnchor(pkUid, mStream);
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 设置连麦pk按钮是否可见
     */
    public void setPkBtnVisible(boolean visible) {
        if (mLiveAnchorViewHolder != null) {
            if (visible) {
                if (mLiveLinkMicAnchorPresenter.isLinkMic()) {
                    mLiveAnchorViewHolder.setPkBtnVisible(true);
                }
            } else {
                mLiveAnchorViewHolder.setPkBtnVisible(false);
            }
        }
    }

    /**
     * 发起主播连麦pk
     */
    public void applyLinkMicPk() {
        String pkUid = null;
        if (mLiveLinkMicAnchorPresenter != null) {
            pkUid = mLiveLinkMicAnchorPresenter.getPkUid();
        }
        if (!TextUtils.isEmpty(pkUid) && mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.applyLinkMicPk(pkUid, mStream);
        }
    }

    /**
     * 主播与主播PK  主播收到对方主播发过来的PK申请的回调
     *
     * @param u      对方主播的信息
     * @param stream 对方主播的stream
     */
    @Override
    public void onLinkMicPkApply(UserBean u, String stream) {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkApply(u, stream);
        }
    }

    /**
     * 主播与主播PK  对方主播拒绝pk的回调
     */
    @Override
    public void onLinkMicPkRefuse() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkRefuse();
        }
    }

    /**
     * 主播与主播PK   对方主播正在忙的回调
     */
    @Override
    public void onLinkMicPkBusy() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkBusy();
        }
    }

    /**
     * 主播与主播PK   对方主播无响应的回调
     */
    @Override
    public void onLinkMicPkNotResponse() {
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.onLinkMicPkNotResponse();
        }
    }


    /**
     * 获取美颜数值
     *
     * @return
     */
    public int[] getBeautyData() {
        if (mLivePushViewHolder != null) {
            return mLivePushViewHolder.getBeautyData();
        }
        return null;
    }

    /**
     * 设置美颜数值
     *
     * @param type
     * @param val
     */
    public void setBeautyData(int type, float val) {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.setBeautyData(type, val);
        }
    }

    /**
     * 设置特殊滤镜
     *
     * @param id
     */
    public void setSpecialFilter(int id) {
        if (mLivePushViewHolder != null) {
            mLivePushViewHolder.setSpecialFilter(id);
        }
    }


}
