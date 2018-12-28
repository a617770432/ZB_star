package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.LiveGuardInfo;
import com.xingqiuzhibo.phonelive.bean.LiveUserGiftBean;
import com.xingqiuzhibo.phonelive.custom.MyViewPager;
import com.xingqiuzhibo.phonelive.dialog.LiveGiftDialogFragment;
import com.xingqiuzhibo.phonelive.game.GamePresenter;
import com.xingqiuzhibo.phonelive.game.bean.GameParam;
import com.xingqiuzhibo.phonelive.game.views.GameLh_GzViewHolder;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicAnchorPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicPkPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveLinkMicPresenter;
import com.xingqiuzhibo.phonelive.socket.SocketChatUtil;
import com.xingqiuzhibo.phonelive.socket.SocketClient;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.RandomUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.xingqiuzhibo.phonelive.views.LiveAudienceViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveEndViewHolder;
import com.xingqiuzhibo.phonelive.views.LivePlayViewHolder;
import com.xingqiuzhibo.phonelive.views.LiveRoomViewHolder;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by cxf on 2018/10/10.
 */

public class LiveAudienceActivity extends LiveActivity {

    private MyViewPager mViewPager;
    private ViewGroup mPage;
    private FrameLayout mContainerWrap;
    private LivePlayViewHolder mLivePlayViewHolder;
    private LiveAudienceViewHolder mLiveAudienceViewHolder;
    private boolean mLighted;
    private long mLastLightClickTime;
    private boolean mEnd;
    private boolean mCoinNotEnough;//余额不足

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_audience;
    }

    @Override
    protected void main() {
        super.main();
        mLivePlayViewHolder = new LivePlayViewHolder(mContext, (ViewGroup) findViewById(R.id.play_container));
        mLivePlayViewHolder.addToParent();

        addLifeCycleListener(mLivePlayViewHolder.getLifeCycleListener());
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);
        mPage = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.view_audience_page, mViewPager, false);
        mContainerWrap = mPage.findViewById(R.id.container_wrap);
        mContainer = mPage.findViewById(R.id.container);
        mLiveRoomViewHolder = new LiveRoomViewHolder(mContext, mContainer, (GifImageView) mPage.findViewById(R.id.gift_gif));
        mLiveRoomViewHolder.addToParent();
        addLifeCycleListener(mLiveRoomViewHolder.getLifeCycleListener());
        mLiveAudienceViewHolder = new LiveAudienceViewHolder(mContext, mContainer);
        mLiveAudienceViewHolder.addToParent();
        mLiveAudienceViewHolder.setUnReadCount(getImUnReadCount());
        mLiveBottomViewHolder = mLiveAudienceViewHolder;
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                if (position == 0) {
                    View view = new View(mContext);
                    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    container.addView(view);
                    return view;
                } else {
                    container.addView(mPage);
                    return mPage;
                }
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            }
        });
        mViewPager.setCurrentItem(1);
        Intent intent = getIntent();
        mLiveType = intent.getIntExtra(Constants.LIVE_TYPE, Constants.LIVE_TYPE_NORMAL);
        mLiveTypeVal = intent.getIntExtra(Constants.LIVE_TYPE_VAL, 0);
        mLiveBean = intent.getParcelableExtra(Constants.LIVE_BEAN);
        mLiveUid = mLiveBean.getUid();
        mStream = mLiveBean.getStream();
        mLivePlayViewHolder.setCover(mLiveBean.getThumb());
        mLivePlayViewHolder.play(mLiveBean.getPull());
        mLiveAudienceViewHolder.setLiveInfo(mLiveUid, mStream);
        mLiveRoomViewHolder.setAvatar(mLiveBean.getAvatar());
        mLiveRoomViewHolder.setAnchorLevel(mLiveBean.getLevelAnchor());
        mLiveRoomViewHolder.setName(mLiveBean.getUserNiceName());
        mLiveRoomViewHolder.setRoomNum(mLiveBean.getLiangNameTip());
        mLiveLinkMicPresenter = new LiveLinkMicPresenter(mContext, mLivePlayViewHolder, false, mLiveAudienceViewHolder.getContentView());
        mLiveLinkMicAnchorPresenter = new LiveLinkMicAnchorPresenter(mContext, mLivePlayViewHolder, false, null);
        mLiveLinkMicPkPresenter = new LiveLinkMicPkPresenter(mContext, mLivePlayViewHolder, false, null);
        mLiveLinkMicPkPresenter.setLiveUid(mLiveUid);
        enterRoom();
    }

    private void enterRoom() {
        HttpUtil.enterRoom(mLiveUid, mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mDanmuPrice = obj.getString("barrage_fee");
                    mShutTime = obj.getString("shut_time");
                    mSocketUserType = obj.getIntValue("usertype");
                    //连接socket
                    if (mSocketClient == null) {
                        mSocketClient = new SocketClient(obj.getString("chatserver"), LiveAudienceActivity.this);
                        if (mLiveLinkMicPresenter != null) {
                            mLiveLinkMicPresenter.setSocketClient(mSocketClient);
                        }
                    }
                    mSocketClient.connect(mLiveUid, mStream);
                    if (mLiveRoomViewHolder != null) {
                        mLiveRoomViewHolder.setLiveInfo(mLiveUid, mStream, obj.getIntValue("userlist_time") * 1000);
                        mLiveRoomViewHolder.setVotes(obj.getString("votestotal"));
                        mLiveRoomViewHolder.setAttention(obj.getIntValue("isattention"));
                        List<LiveUserGiftBean> list = JSON.parseArray(obj.getString("userlists"), LiveUserGiftBean.class);
                        mLiveRoomViewHolder.setUserList(list);
                        mLiveRoomViewHolder.startRefreshUserList();
                        if (mLiveType == Constants.LIVE_TYPE_TIME) {//计时收费
                            mLiveRoomViewHolder.startRequestTimeCharge();
                        }
                    }
                    //判断是否有连麦，要显示连麦窗口
                    String linkMicUid = obj.getString("linkmic_uid");
                    String linkMicPull = obj.getString("linkmic_pull");
                    if (!TextUtils.isEmpty(linkMicUid) && !"0".equals(linkMicUid) && !TextUtils.isEmpty(linkMicPull)) {
                        if (mLiveLinkMicPresenter != null) {
                            mLiveLinkMicPresenter.onLinkMicPlay(linkMicUid, linkMicPull);
                        }
                    }
                    //判断是否有主播连麦
                    JSONObject pkInfo = JSON.parseObject(obj.getString("pkinfo"));
                    if (pkInfo != null) {
                        String pkUid = pkInfo.getString("pkuid");
                        if (!TextUtils.isEmpty(pkUid) && !"0".equals(pkUid)) {
                            String pkPull = pkInfo.getString("pkpull");
                            if (!TextUtils.isEmpty(pkPull)) {
                                if (mLiveLinkMicAnchorPresenter != null) {
                                    mLiveLinkMicAnchorPresenter.onLinkMicAnchorPlayUrl(pkUid, pkPull);
                                }
                            }
                            if (pkInfo.getIntValue("ifpk") == 1) {//pk开始了
                                mLiveLinkMicPkPresenter.onEnterRoomPkStart(pkUid, pkInfo.getLongValue("pk_gift_liveuid"), pkInfo.getLongValue("pk_gift_pkuid"), pkInfo.getIntValue("pk_time") * 1000);
                            }
                        }
                    }

                    //守护相关
                    mLiveGuardInfo = new LiveGuardInfo();
                    int guardNum = obj.getIntValue("guard_nums");
                    mLiveGuardInfo.setGuardNum(guardNum);
                    JSONObject guardObj = obj.getJSONObject("guard");
                    if (guardObj != null) {
                        mLiveGuardInfo.setMyGuardType(guardObj.getIntValue("type"));
                        mLiveGuardInfo.setMyGuardEndTime(guardObj.getString("endtime"));
                    }
                    if (mLiveRoomViewHolder != null) {
                        mLiveRoomViewHolder.setGuardNum(guardNum);
                        //红包相关
                        mLiveRoomViewHolder.setRedPackBtnVisible(obj.getIntValue("isred") == 1);
                    }
                    try {
                        //游戏相关
                        GameParam param = new GameParam();
                        param.setContext(mContext);
                        param.setParentView(mContainerWrap);
                        param.setTopView(mContainer);
                        param.setInnerContainer(mLiveRoomViewHolder.getInnerContainer());
                        param.setSocketClient(mSocketClient);
                        param.setLiveUid(mLiveUid);
                        param.setStream(mStream);
                        param.setAnchor(false);
                        param.setCoinName(mCoinName);
                        param.setObj(obj);
                        mGamePresenter = new GamePresenter(param);
                    }catch (Exception e){
                    }
                }
            }
        });
    }

    /**
     * 打开礼物窗口
     */
    public void openGiftWindow() {
        if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mStream)) {
            return;
        }
        LiveGiftDialogFragment fragment = new LiveGiftDialogFragment();
        fragment.setLiveGuardInfo(mLiveGuardInfo);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LIVE_UID, mLiveUid);
        bundle.putString(Constants.LIVE_STREAM, mStream);
        fragment.setArguments(bundle);
        fragment.show(((LiveAudienceActivity) mContext).getSupportFragmentManager(), "LiveGiftDialogFragment");
    }

    /**
     * 结束观看
     */
    private void endPlay() {
        HttpUtil.cancel(HttpConsts.ENTER_ROOM);
        if (mEnd) {
            return;
        }
        mEnd = true;
        HttpUtil.cancel(HttpConsts.ENTER_ROOM);
        //断开socket
        if (mSocketClient != null) {
            mSocketClient.disConnect();
        }
        mSocketClient = null;
        //结束播放
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.release();
        }
        mLivePlayViewHolder = null;
        release();
    }


    /**
     * 观众收到直播结束消息
     */
    @Override
    public void onLiveEnd() {
        super.onLiveEnd();
        if (mViewPager != null) {
            if (mViewPager.getCurrentItem() != 1) {
                mViewPager.setCurrentItem(1, false);
            }
            mViewPager.setCanScroll(false);
        }
        endPlay();
        if (mLiveEndViewHolder == null) {
            mLiveEndViewHolder = new LiveEndViewHolder(mContext, mPage);
            addLifeCycleListener(mLiveEndViewHolder.getLifeCycleListener());
            mLiveEndViewHolder.addToParent();
            mLiveEndViewHolder.showData(mLiveBean, mStream);
        }
    }


    /**
     * 观众收到踢人消息
     */
    @Override
    public void onKick(String touid) {
        if (!TextUtils.isEmpty(touid) && touid.equals(AppConfig.getInstance().getUid())) {//被踢的是自己
            exitLiveRoom();
            ToastUtil.show(WordUtil.getString(R.string.live_kicked_2));
        }
    }

    /**
     * 观众收到禁言消息
     */
    @Override
    public void onShutUp(String touid, String content) {
        if (!TextUtils.isEmpty(touid) && touid.equals(AppConfig.getInstance().getUid())) {
            DialogUitl.showSimpleDialog(mContext, content, null);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mEnd && !canBackPressed()) {
            return;
        }
        exitLiveRoom();
    }

    /**
     * 退出直播间
     */
    public void exitLiveRoom() {
        endPlay();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("LiveAudienceActivity-------onDestroy------->");
    }

    /**
     * 点亮
     */
    public void light() {
        if (!mLighted) {
            mLighted = true;
            int guardType = mLiveGuardInfo != null ? mLiveGuardInfo.getMyGuardType() : Constants.GUARD_TYPE_NONE;
            SocketChatUtil.sendLightMessage(mSocketClient, 1 + RandomUtil.nextInt(6), guardType);
        } else {
            long cutTime = System.currentTimeMillis();
            if (cutTime - mLastLightClickTime < 5000) {
                if (mLiveRoomViewHolder != null) {
                    mLiveRoomViewHolder.playLightAnim();
                }
            } else {
                mLastLightClickTime = cutTime;
                SocketChatUtil.sendFloatHeart(mSocketClient);
            }
        }
    }


    /**
     * 计时收费更新主播映票数
     */
    public void roomChargeUpdateVotes() {
        sendUpdateVotesMessage(mLiveTypeVal);
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.pausePlay();
        }
    }

    /**
     * 恢复播放
     */
    public void resumePlay() {
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.resumePlay();
        }
    }

    /**
     * 充值成功
     */
    public void onChargeSuccess() {
        if (mLiveType == Constants.LIVE_TYPE_TIME) {
            if (mCoinNotEnough) {
                mCoinNotEnough = false;
                HttpUtil.roomCharge(mLiveUid, mStream, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            roomChargeUpdateVotes();
                            if (mLiveRoomViewHolder != null) {
                                resumePlay();
                                mLiveRoomViewHolder.startRequestTimeCharge();
                            }
                        } else {
                            if (code == 1008) {//余额不足
                                mCoinNotEnough = true;
                                DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_coin_not_enough), false,
                                        new DialogUitl.SimpleCallback2() {
                                            @Override
                                            public void onConfirmClick(Dialog dialog, String content) {
                                                MyCoinActivity.forward(mContext);
                                            }

                                            @Override
                                            public void onCancelClick() {
                                                exitLiveRoom();
                                            }
                                        });
                            }
                        }
                    }
                });
            }
        }
    }

    public void setCoinNotEnough(boolean coinNotEnough) {
        mCoinNotEnough = coinNotEnough;
    }
}
