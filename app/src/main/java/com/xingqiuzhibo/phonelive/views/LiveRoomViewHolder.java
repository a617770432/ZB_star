package com.xingqiuzhibo.phonelive.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.LiveActivity;
import com.xingqiuzhibo.phonelive.activity.LiveAnchorActivity;
import com.xingqiuzhibo.phonelive.activity.LiveAudienceActivity;
import com.xingqiuzhibo.phonelive.activity.MyCoinActivity;
import com.xingqiuzhibo.phonelive.adapter.LiveChatAdapter;
import com.xingqiuzhibo.phonelive.adapter.LiveUserAdapter;
import com.xingqiuzhibo.phonelive.bean.LevelBean;
import com.xingqiuzhibo.phonelive.bean.LiveBuyGuardMsgBean;
import com.xingqiuzhibo.phonelive.bean.LiveChatBean;
import com.xingqiuzhibo.phonelive.bean.LiveDanMuBean;
import com.xingqiuzhibo.phonelive.bean.LiveEnterRoomBean;
import com.xingqiuzhibo.phonelive.bean.LiveReceiveGiftBean;
import com.xingqiuzhibo.phonelive.bean.LiveUserGiftBean;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.custom.TopGradual;
import com.xingqiuzhibo.phonelive.dialog.LiveUserDialogFragment;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.CommonCallback;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleAdapter;
import com.xingqiuzhibo.phonelive.interfaces.OnItemClickListener;
import com.xingqiuzhibo.phonelive.presenter.LiveDanmuPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveEnterRoomAnimPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveGiftAnimPresenter;
import com.xingqiuzhibo.phonelive.presenter.LiveLightAnimPresenter;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.DpUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.umeng.commonsdk.debug.D;

import java.lang.ref.WeakReference;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by cxf on 2018/10/9.
 * 直播间公共逻辑
 */

public class LiveRoomViewHolder extends AbsViewHolder implements View.OnClickListener {

    public static int sOffsetY = 0;
    private ViewGroup mRoot;
    private ImageView mAvatar;
    private ImageView mLevelAnchor;
    private TextView mName;
    private TextView mID;
    private View mBtnFollow;
    private TextView mVotesName;//映票名称
    private TextView mVotes;
    private TextView mGuardNum;//守护人数
    private RecyclerView mUserRecyclerView;
    private RecyclerView mChatRecyclerView;
    private LiveUserAdapter mLiveUserAdapter;
    private LiveChatAdapter mLiveChatAdapter;
    private View mBtnRedPack;
    private String mLiveUid;
    private String mStream;
    private LiveLightAnimPresenter mLightAnimPresenter;
    private LiveEnterRoomAnimPresenter mLiveEnterRoomAnimPresenter;
    private LiveDanmuPresenter mLiveDanmuPresenter;
    private LiveGiftAnimPresenter mLiveGiftAnimPresenter;
    private LiveRoomHandler mLiveRoomHandler;
    private HttpCallback mRefreshUserListCallback;
    private HttpCallback mTimeChargeCallback;
    protected int mUserListInterval;//用户列表刷新时间的间隔
    private GifImageView mGifImageView;
    private TextView mLiveTimeTextView;//主播的直播时长
    private long mAnchorLiveTime;//主播直播时间
    private View mGiftGroup2;
    private RelativeLayout.LayoutParams mGiftLp;
    private RelativeLayout.LayoutParams mGiftLp2;
    private ViewStub mViewStub;
    private ViewGroup mInnerViewGrop;
    public LiveRoomViewHolder(Context context, ViewGroup parentView, GifImageView gifImageView) {
        super(context, parentView);
        mGifImageView = gifImageView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_room;
    }

    @Override
    public void init() {
        mRoot = (ViewGroup) findViewById(R.id.root);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mName = (TextView) findViewById(R.id.name);
        mID = (TextView) findViewById(R.id.id_val);
        mBtnFollow = findViewById(R.id.btn_follow);
        mVotesName = (TextView) findViewById(R.id.votes_name);
        mVotes = (TextView) findViewById(R.id.votes);
        mGuardNum = (TextView) findViewById(R.id.guard_num);
        mGiftGroup2 = findViewById(R.id.gift_group_2);
        //用户头像列表
        mUserRecyclerView = (RecyclerView) findViewById(R.id.user_recyclerView);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mLiveUserAdapter = new LiveUserAdapter(mContext);
        mLiveUserAdapter.setOnItemClickListener(new OnItemClickListener<UserBean>() {
            @Override
            public void onItemClick(UserBean bean, int position) {
                showUserDialog(bean.getId());
            }
        });
        mUserRecyclerView.setAdapter(mLiveUserAdapter);
        //聊天栏
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerView);
        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mChatRecyclerView.addItemDecoration(new TopGradual());
        mLiveChatAdapter = new LiveChatAdapter(mContext);
        mLiveChatAdapter.setOnItemClickListener(new OnItemClickListener<LiveChatBean>() {
            @Override
            public void onItemClick(LiveChatBean bean, int position) {
                showUserDialog(bean.getId());
            }
        });
        mChatRecyclerView.setAdapter(mLiveChatAdapter);
        mVotesName.setText(AppConfig.getInstance().getVotesName());
        mBtnFollow.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        findViewById(R.id.btn_votes).setOnClickListener(this);
        findViewById(R.id.btn_guard).setOnClickListener(this);
        mBtnRedPack = findViewById(R.id.btn_red_pack);
        mBtnRedPack.setOnClickListener(this);
        if (mContext instanceof LiveAudienceActivity) {
            mRoot.setOnClickListener(this);
        } else {
            mLiveTimeTextView = (TextView) findViewById(R.id.live_time);
            mLiveTimeTextView.setVisibility(View.VISIBLE);
        }
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_USER_LIST);
                HttpUtil.cancel(HttpConsts.TIME_CHARGE);
                HttpUtil.cancel(HttpConsts.SET_ATTENTION);
                L.e("LiveRoomViewHolder-------->onDestroy");
            }
        };
        mLightAnimPresenter = new LiveLightAnimPresenter(mContext, mParentView);
        mLiveEnterRoomAnimPresenter = new LiveEnterRoomAnimPresenter(mContext, mContentView);
        mLiveRoomHandler = new LiveRoomHandler(this);
        mRefreshUserListCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (mLiveUserAdapter != null) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        List<LiveUserGiftBean> list = JSON.parseArray(obj.getString("userlist"), LiveUserGiftBean.class);
                        mLiveUserAdapter.refreshList(list);
                    }
                }
            }
        };
        mTimeChargeCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (mContext instanceof LiveAudienceActivity) {
                    final LiveAudienceActivity liveAudienceActivity = (LiveAudienceActivity) mContext;
                    if (code == 0) {
                        liveAudienceActivity.roomChargeUpdateVotes();
                    } else {
                        if (mLiveRoomHandler != null) {
                            mLiveRoomHandler.removeMessages(LiveRoomHandler.WHAT_TIME_CHARGE);
                        }
                        liveAudienceActivity.pausePlay();
                        if (code == 1008) {//余额不足
                            liveAudienceActivity.setCoinNotEnough(true);
                            DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.live_coin_not_enough), false,
                                    new DialogUitl.SimpleCallback2() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog, String content) {
                                            MyCoinActivity.forward(mContext);
                                        }

                                        @Override
                                        public void onCancelClick() {
                                            liveAudienceActivity.exitLiveRoom();
                                        }
                                    });
                        }
                    }
                }
            }
        };

        mGiftLp = (RelativeLayout.LayoutParams) mGiftGroup2.getLayoutParams();
        mGiftLp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(60));
        mGiftLp2.setMargins(0, DpUtil.dp2px(100), 0, DpUtil.dp2px(60));
    }

    /**
     * 显示主播头像
     */
    public void setAvatar(String url) {
        if (mAvatar != null) {
            ImgLoader.displayAvatar(url, mAvatar);
        }
    }

    /**
     * 显示主播等级
     */
    public void setAnchorLevel(int anchorLevel) {
        if (mLevelAnchor != null) {
            LevelBean levelBean = AppConfig.getInstance().getAnchorLevel(anchorLevel);
            if (levelBean != null) {
                ImgLoader.display(levelBean.getThumbIcon(), mLevelAnchor);
            }
        }
    }


    /**
     * 显示用户名
     */
    public void setName(String name) {
        if (mName != null) {
            mName.setText(name);
        }
    }

    /**
     * 显示房间号
     */
    public void setRoomNum(String roomNum) {
        if (mID != null) {
            mID.setText(roomNum);
        }
    }

    /**
     * 显示是否关注
     */
    public void setAttention(int attention) {
        if (mBtnFollow != null) {
            if (attention == 0) {
                if (mBtnFollow.getVisibility() != View.VISIBLE) {
                    mBtnFollow.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnFollow.getVisibility() == View.VISIBLE) {
                    mBtnFollow.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 显示刷新直播间用户列表
     */
    public void setUserList(List<LiveUserGiftBean> list) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.refreshList(list);
        }
    }

    /**
     * 显示主播映票数
     */
    public void setVotes(String votes) {
        if (mVotes != null) {
            mVotes.setText(votes);
        }
    }

    /**
     * 显示主播守护人数
     */
    public void setGuardNum(int guardNum) {
        if (mGuardNum != null) {
            if (guardNum > 0) {
                mGuardNum.setText(guardNum + WordUtil.getString(R.string.ren));
            } else {
                mGuardNum.setText(R.string.main_list_no_data);
            }
        }
    }

    public void setLiveInfo(String liveUid, String stream, int userListInterval) {
        mLiveUid = liveUid;
        mStream = stream;
        mUserListInterval = userListInterval;
    }


    /**
     * 守护信息发生变化
     */
    public void onGuardInfoChanged(LiveBuyGuardMsgBean bean) {
        setGuardNum(bean.getGuardNum());
        setVotes(bean.getVotes());
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.onGuardChanged(bean.getUid(), bean.getGuardType());
        }
    }

    /**
     * 设置红包按钮是否可见
     */
    public void setRedPackBtnVisible(boolean visible) {
        if (mBtnRedPack != null) {
            if (visible) {
                if (mBtnRedPack.getVisibility() != View.VISIBLE) {
                    mBtnRedPack.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnRedPack.getVisibility() == View.VISIBLE) {
                    mBtnRedPack.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void setGiftLayoutParam(boolean isChange) {
        if (isChange) {
            mGiftGroup2.setLayoutParams(mGiftLp2);
        } else {
            mGiftGroup2.setLayoutParams(mGiftLp);
        }
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.avatar:
                showAnchorUserDialog();
                break;
            case R.id.btn_follow:
                follow();
                break;
            case R.id.btn_votes:
                openContributeWindow();
                break;
            case R.id.btn_guard:
                ((LiveActivity) mContext).openGuardListWindow();
                break;
            case R.id.root:
                light();
                break;
            case R.id.btn_red_pack:
                ((LiveActivity) mContext).openRedPackListWindow();
                break;
        }
    }

    /**
     * 关注主播
     */
    private void follow() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        HttpUtil.setAttention(Constants.FOLLOW_FROM_LIVE, mLiveUid, new CommonCallback<Integer>() {
            @Override
            public void callback(Integer isAttention) {
                if (isAttention == 1) {
                    ((LiveActivity) mContext).sendSystemMessage(
                            AppConfig.getInstance().getUserBean().getUserNiceName() + WordUtil.getString(R.string.live_follow_anchor));
                }
            }
        });
    }

    /**
     * 用户进入房间，用户列表添加该用户
     */
    public void insertUser(LiveUserGiftBean bean) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.insertItem(bean);
        }
    }

    /**
     * 用户进入房间，添加僵尸粉
     */
    public void insertUser(List<LiveUserGiftBean> list) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.insertList(list);
        }
    }

    /**
     * 用户离开房间，用户列表删除该用户
     */
    public void removeUser(String uid) {
        if (mLiveUserAdapter != null) {
            mLiveUserAdapter.removeItem(uid);
        }
    }

    /**
     * 刷新用户列表
     */
    private void refreshUserList() {
        if (!TextUtils.isEmpty(mLiveUid) && mRefreshUserListCallback != null && mLiveUserAdapter != null) {
            HttpUtil.cancel(HttpConsts.GET_USER_LIST);
            HttpUtil.getUserList(mLiveUid, mStream, mRefreshUserListCallback);
            startRefreshUserList();
        }
    }

    /**
     * 开始刷新用户列表
     */
    public void startRefreshUserList() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_REFRESH_USER_LIST, mUserListInterval > 0 ? mUserListInterval : 60000);
        }
    }

    /**
     * 请求计时收费的扣费接口
     */
    private void requestTimeCharge() {
        if (!TextUtils.isEmpty(mLiveUid) && mTimeChargeCallback != null) {
            HttpUtil.cancel(HttpConsts.TIME_CHARGE);
            HttpUtil.timeCharge(mLiveUid, mStream, mTimeChargeCallback);
            startRequestTimeCharge();
        }
    }

    /**
     * 开始请求计时收费的扣费接口
     */
    public void startRequestTimeCharge() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_TIME_CHARGE, 60000);
        }
    }

    /**
     * 主播50s暂停
     */
    public void pause50s(){
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.LIVE_PAUSE, 50*1000);
        }
    }

    /**
     * 主播50s内返回了
     */
    public void resume50s(){
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.removeMessages(LiveRoomHandler.LIVE_PAUSE);
        }
    }


    /**
     * 添加聊天消息到聊天栏
     */
    public void insertChat(LiveChatBean bean) {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.insertItem(bean);
        }
    }

    /**
     * 播放飘心动画
     */
    public void playLightAnim() {
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.play();
        }
    }

    /**
     * 点亮
     */
    private void light() {
        ((LiveAudienceActivity) mContext).light();
    }


    /**
     * 键盘高度变化
     */
    public void onKeyBoardChanged(int visibleHeight, int keyBoardHeight) {
        if (mRoot != null) {
            if (keyBoardHeight == 0) {
                mRoot.setTranslationY(0);
                return;
            }
            if (sOffsetY == 0) {
                mRoot.setTranslationY(-keyBoardHeight);
                return;
            }
            if (sOffsetY > 0 && sOffsetY < keyBoardHeight) {
                mRoot.setTranslationY(sOffsetY - keyBoardHeight);
            }
        }
    }

    /**
     * 聊天栏滚到最底部
     */
    public void chatScrollToBottom() {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.scrollToBottom();
        }
    }

    /**
     * 用户进入房间 金光一闪,坐骑动画
     */
    public void onEnterRoom(LiveEnterRoomBean bean) {
        if (bean == null) {
            return;
        }
        if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.enterRoom(bean);
        }
    }

    /**
     * 显示弹幕
     */
    public void showDanmu(LiveDanMuBean bean) {
        if (mVotes != null) {
            mVotes.setText(bean.getVotes());
        }
        if (mLiveDanmuPresenter == null) {
            mLiveDanmuPresenter = new LiveDanmuPresenter(mContext, mParentView);
        }
        mLiveDanmuPresenter.showDanmu(bean);
    }

    /**
     * 显示主播的个人资料弹窗
     */
    private void showAnchorUserDialog() {
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        showUserDialog(mLiveUid);
    }

    /**
     * 显示个人资料弹窗
     */
    private void showUserDialog(String toUid) {
        if (!TextUtils.isEmpty(mLiveUid) && !TextUtils.isEmpty(toUid)) {
            LiveUserDialogFragment fragment = new LiveUserDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LIVE_UID, mLiveUid);
            bundle.putString(Constants.TO_UID, toUid);
            fragment.setArguments(bundle);
            fragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "LiveUserDialogFragment");
        }
    }

    /**
     * 直播间贡献榜窗口
     */
    private void openContributeWindow() {
        ((LiveActivity) mContext).openContributeWindow();
    }


    /**
     * 显示礼物动画
     */
    public void showGiftMessage(LiveReceiveGiftBean bean) {
        mVotes.setText(bean.getVotes());
        if (mLiveGiftAnimPresenter == null) {
            mLiveGiftAnimPresenter = new LiveGiftAnimPresenter(mContext, mContentView, mGifImageView);
        }
        mLiveGiftAnimPresenter.showGiftAnim(bean);
    }

    /**
     * 增加主播映票数
     *
     * @param deltaVal 增加的映票数量
     */
    public void updateVotes(String deltaVal) {
        if (mVotes == null) {
            return;
        }
        String votesVal = mVotes.getText().toString().trim();
        if (TextUtils.isEmpty(votesVal)) {
            return;
        }
        try {
            double votes = Double.parseDouble(votesVal);
            double addVotes = Double.parseDouble(deltaVal);
            votes += addVotes;
            mVotes.setText(StringUtil.format(votes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewGroup getInnerContainer() {
        mViewStub = (ViewStub) findViewById(R.id.inner_container);
        if (mViewStub!=null){
            mInnerViewGrop=(ViewGroup) mViewStub.inflate();
        }
        return mInnerViewGrop;
    }

    /**
     * 主播显示直播时间
     */
    private void showAnchorLiveTime() {
        if (mLiveTimeTextView != null) {
            mAnchorLiveTime += 1000;
            mLiveTimeTextView.setText(StringUtil.getDurationText(mAnchorLiveTime));
            startAnchorLiveTime();
        }
    }

    public void startAnchorLiveTime() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.sendEmptyMessageDelayed(LiveRoomHandler.WHAT_ANCHOR_LIVE_TIME, 1000);
        }
    }

    public void endLive(){
        ((LiveAnchorActivity)mContext).endLive();
    }

    public void release() {
        if (mLiveRoomHandler != null) {
            mLiveRoomHandler.release();
        }
        mLiveRoomHandler = null;
        if (mLightAnimPresenter != null) {
            mLightAnimPresenter.release();
        }
        if (mLiveEnterRoomAnimPresenter != null) {
            mLiveEnterRoomAnimPresenter.release();
        }
        if (mLiveGiftAnimPresenter != null) {
            mLiveGiftAnimPresenter.release();
        }
        mRefreshUserListCallback = null;
        mTimeChargeCallback = null;
        mViewStub=null;
        mInnerViewGrop=null;
    }


    private static class LiveRoomHandler extends Handler {

        private LiveRoomViewHolder mLiveRoomViewHolder;
        private static final int WHAT_REFRESH_USER_LIST = 1;
        private static final int WHAT_TIME_CHARGE = 2;//计时收费房间定时请求接口扣费
        private static final int WHAT_ANCHOR_LIVE_TIME = 3;//直播间主播计时
        //直播暂停（主播直播暂停50秒后关闭直播）
        protected static final int LIVE_PAUSE = 4;

        public LiveRoomHandler(LiveRoomViewHolder liveRoomViewHolder) {
            mLiveRoomViewHolder = new WeakReference<>(liveRoomViewHolder).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mLiveRoomViewHolder != null) {
                switch (msg.what) {
                    case WHAT_REFRESH_USER_LIST:
                        mLiveRoomViewHolder.refreshUserList();
                        break;
                    case WHAT_TIME_CHARGE:
                        mLiveRoomViewHolder.requestTimeCharge();
                        break;
                    case WHAT_ANCHOR_LIVE_TIME:
                        mLiveRoomViewHolder.showAnchorLiveTime();
                        break;
                    case LIVE_PAUSE:
                        mLiveRoomViewHolder.endLive();
                        break;
                }
            }
        }

        public void release() {
            removeCallbacksAndMessages(null);
            mLiveRoomViewHolder = null;
        }
    }
}