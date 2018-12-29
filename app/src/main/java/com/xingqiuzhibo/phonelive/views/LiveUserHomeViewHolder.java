package com.xingqiuzhibo.phonelive.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.AbsActivity;
import com.xingqiuzhibo.phonelive.activity.ChatRoomActivity;
import com.xingqiuzhibo.phonelive.activity.FansActivity;
import com.xingqiuzhibo.phonelive.activity.FollowActivity;
import com.xingqiuzhibo.phonelive.activity.LiveContributeActivity;
import com.xingqiuzhibo.phonelive.activity.LiveGuardListActivity;
import com.xingqiuzhibo.phonelive.activity.UserHomeActivity;
import com.xingqiuzhibo.phonelive.adapter.NewViewPagerAdapter;
import com.xingqiuzhibo.phonelive.bean.ImpressBean;
import com.xingqiuzhibo.phonelive.bean.LevelBean;
import com.xingqiuzhibo.phonelive.bean.LiveBean;
import com.xingqiuzhibo.phonelive.bean.SearchUserBean;
import com.xingqiuzhibo.phonelive.bean.UserHomeConBean;
import com.xingqiuzhibo.phonelive.custom.MyTextView;
import com.xingqiuzhibo.phonelive.custom.ViewPagerIndicator;
import com.xingqiuzhibo.phonelive.dialog.LiveShareDialogFragment;
import com.xingqiuzhibo.phonelive.event.FollowEvent;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.CommonCallback;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleAdapter;
import com.xingqiuzhibo.phonelive.interfaces.LifeCycleListener;
import com.xingqiuzhibo.phonelive.presenter.CheckLivePresenter;
import com.xingqiuzhibo.phonelive.presenter.UserHomeSharePresenter;
import com.xingqiuzhibo.phonelive.utils.IconUtil;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import com.xingqiuzhibo.phonelive.utils.TextRender;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/18.
 * 用户个人主页
 */

public class LiveUserHomeViewHolder extends AbsLivePageViewHolder implements AppBarLayout.OnOffsetChangedListener, LiveShareDialogFragment.ActionListener {

    private LiveRecordViewHolder mLiveRecordViewHolder;
    private List<LifeCycleListener> mLifeCycleListeners;
    private LayoutInflater mInflater;
    private View mBottom;
    private AppBarLayout mAppBarLayout;
    private ImageView mAvatarBg;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mSex;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mID;
    private TextView mBtnEnter;
    private TextView mBtnEnter2;
    private TextView mBtnFans;
    private TextView mBtnFollow;
    private TextView mBtnFollow2;
    private TextView mSign;
    private LinearLayout mImpressGroup;
    private View mNoImpressTip;
    private TextView mVotesName;
    private LinearLayout mConGroup;
    private TextView mTitleView;
    private ImageView mBtnBack;
    private ImageView mBtnShare;
    private TextView mBtnBlack;
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private String mToUid;
    private boolean mSelf;
    private float mRate;
    private int[] mWhiteColorArgb;
    private int[] mBlackColorArgb;
    private View.OnClickListener mAddImpressOnClickListener;
    private UserHomeSharePresenter mUserHomeSharePresenter;
    private SearchUserBean mSearchUserBean;
    private CheckLivePresenter mCheckLivePresenter;
    private LiveBean mLiveBean;

    //改
    private TextView tvGet;
    private TextView tvSend;
    private FragmentManager fm;
    private LinearLayout llFans , llFollow;

    public LiveUserHomeViewHolder( Context context, ViewGroup parentView , FragmentManager fm) {
        super(context, parentView,fm);
//        //改
//        this.fm = fm;
    }

    @Override
    protected void processArguments(Object... args) {
        super.processArguments(args);
        this.fm = (FragmentManager) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_user_home;
    }

    @Override
    public void init() {
        super.init();
        mInflater = LayoutInflater.from(mContext);

        //改
        tvGet = (TextView) findViewById(R.id.tv_get);
        tvSend = (TextView) findViewById(R.id.tv_send);
        llFans = (LinearLayout) findViewById(R.id.ll_fans);
        llFollow = (LinearLayout) findViewById(R.id.ll_follow);
        llFans.setOnClickListener(this);
        llFollow.setOnClickListener(this);

        mBottom = findViewById(R.id.bottom);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mAvatarBg = (ImageView) findViewById(R.id.bg_avatar);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSex = (ImageView) findViewById(R.id.sex);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        mBtnEnter = (TextView) findViewById(R.id.btn_enter);
        mBtnEnter2 = (TextView) findViewById(R.id.btn_enter2);
        mBtnFans = (TextView) findViewById(R.id.btn_fans);
        mBtnFollow = (TextView) findViewById(R.id.btn_follow);
        mBtnFollow2 = (TextView) findViewById(R.id.btn_follow_2);
        mBtnBlack = (TextView) findViewById(R.id.btn_black);
        mSign = (TextView) findViewById(R.id.sign);
        mImpressGroup = (LinearLayout) findViewById(R.id.impress_group);
        mNoImpressTip = findViewById(R.id.no_impress_tip);
        mVotesName = (TextView) findViewById(R.id.votes_name);
        mConGroup = (LinearLayout) findViewById(R.id.con_group);
        mTitleView = (TextView) findViewById(R.id.titleView);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mBtnShare = (ImageView) findViewById(R.id.btn_share);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mLiveRecordViewHolder = new LiveRecordViewHolder(mContext, mViewPager);
        mLiveRecordViewHolder.addToParent();
        mLifeCycleListener = new LifeCycleAdapter() {
            @Override
            public void onDestroy() {
                HttpUtil.cancel(HttpConsts.GET_USER_HOME);
                HttpUtil.cancel(HttpConsts.SET_ATTENTION);
                HttpUtil.cancel(HttpConsts.SET_BLACK);
            }
        };
        mLifeCycleListeners = new ArrayList<>();
        mLifeCycleListeners.add(mLifeCycleListener);
        mLifeCycleListeners.add(mLiveRecordViewHolder.getLifeCycleListener());
        List<View> viewList = new ArrayList<>();
        viewList.add(mLiveRecordViewHolder.getContentView());


        mBtnShare.setOnClickListener(this);
//        mBtnFans.setOnClickListener(this);
//        mBtnFollow.setOnClickListener(this);
        mBtnFollow2.setOnClickListener(this);
        mBtnBlack.setOnClickListener(this);
        mBtnEnter2.setOnClickListener(this);
        findViewById(R.id.btn_pri_msg).setOnClickListener(this);
        findViewById(R.id.con_group_wrap).setOnClickListener(this);
        findViewById(R.id.guard_group_wrap).setOnClickListener(this);
        mWhiteColorArgb = getColorArgb(0xffffffff);
        mBlackColorArgb = getColorArgb(0xff323232);
        mAddImpressOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImpress();
            }
        };
        mUserHomeSharePresenter = new UserHomeSharePresenter(mContext);
        EventBus.getDefault().register(this);
    }

    public List<LifeCycleListener> getLifeCycleListenerList() {
        return mLifeCycleListeners;
    }

    public void setToUid(String toUid) {
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        mToUid = toUid;
        mSelf = mToUid.equals(AppConfig.getInstance().getUid());

        //改---------------
        NewViewPagerAdapter adapter = new NewViewPagerAdapter(fm , mToUid);
        mViewPager.setAdapter(adapter);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mIndicator.setVisibleChildCount(2);
        mIndicator.setTitles(new String[]{ "映票贡献榜" , WordUtil.getString(R.string.guard_list)});

        mIndicator.setViewPager(mViewPager);

        //改---------------------------
//        if (mSelf) {
//            if (mBottom.getVisibility() == View.VISIBLE) {
//                mBottom.setVisibility(View.GONE);
//            }
//        } else {
//            if (mBottom.getVisibility() != View.VISIBLE) {
//                mBottom.setVisibility(View.VISIBLE);
//            }
//        }
    }

    public void isVisBtnEnter(boolean isVis) {
        mBtnEnter.setVisibility(isVis ? View.VISIBLE : View.GONE);
        mBtnEnter2.setVisibility(isVis ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loadData() {
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        HttpUtil.getUserHome(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    SearchUserBean userBean = JSON.toJavaObject(obj, SearchUserBean.class);
                    mSearchUserBean = userBean;
                    mLiveRecordViewHolder.loadData(userBean);
                    String avatar = userBean.getAvatar();
                    ImgLoader.displayBlur(avatar, mAvatarBg);
                    ImgLoader.displayAvatar(avatar, mAvatar);
                    String toName = userBean.getUserNiceName();
                    mName.setText(toName);
                    mTitleView.setText(toName);
                    mSex.setImageResource(IconUtil.getSexIcon(userBean.getSex()));
                    AppConfig appConfig = AppConfig.getInstance();
                    LevelBean levelAnchor = appConfig.getAnchorLevel(userBean.getLevelAnchor());
                    ImgLoader.display(levelAnchor.getThumb(), mLevelAnchor);
                    LevelBean level = appConfig.getLevel(userBean.getLevel());
                    ImgLoader.display(level.getThumb(), mLevel);
                    mID.setText(userBean.getLiangNameTip());
                    String fansNum = StringUtil.toWan(userBean.getFans());

                    //改了----------------------------------------------------------------
                    mBtnFans.setText(fansNum);
                    mBtnFollow.setText(StringUtil.toWan(userBean.getFollows()));
                    if(TextUtils.isEmpty(userBean.getSignature())){
                        mSign.setText("这家伙很懒，什么都没留下");
                    }else {
                        mSign.setText(userBean.getSignature());
                    }

                    tvGet.setText( TextRender.renderLiveUserDialogData(Long.parseLong(userBean.getVotestotal())) );
                    tvSend.setText( TextRender.renderLiveUserDialogData(Long.parseLong(userBean.getConsumption())) );
                    //改了----------------------------------------------------------------

                    mBtnFollow2.setText(obj.getIntValue("isattention") == 1 ? R.string.following : R.string.follow);
                    mBtnBlack.setText(obj.getIntValue("isblack") == 1 ? R.string.black_ing : R.string.black);
//                    mIndicator.setTitleText(0, WordUtil.getString(R.string.live) + " " + obj.getString("livenums"));
                    showImpress(obj.getString("label"));
                    mVotesName.setText(appConfig.getVotesName() + WordUtil.getString(R.string.live_user_home_con));
                    mUserHomeSharePresenter.setToUid(mToUid).setToName(toName).setAvatarThumb(userBean.getAvatarThumb()).setFansNum(fansNum);
                    showContribute(obj.getString("contribute"));
                    if (mSearchUserBean.getIslive() == 1) {
                        mBtnEnter.setEnabled(true);
                        mLiveBean = JSON.parseObject(obj.getString("liveinfo"), LiveBean.class);
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 显示印象
     */
    private void showImpress(String impressJson) {
        List<ImpressBean> list = JSON.parseArray(impressJson, ImpressBean.class);
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        if (!mSelf) {
            //改
//            ImpressBean lastBean = new ImpressBean();
//            lastBean.setName("+ " + WordUtil.getString(R.string.impress_add));
//            lastBean.setColor("#ffdd00");
//            list.add(lastBean);
        } else {
            if (list.size() == 0) {
                mNoImpressTip.setVisibility(View.VISIBLE);
                return;
            }
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            MyTextView myTextView = (MyTextView) mInflater.inflate(R.layout.view_impress_item_3, mImpressGroup, false);
            ImpressBean bean = list.get(i);
            if (mSelf) {
                bean.setCheck(1);
            } else {
                if (i == size - 1) {
//                    myTextView.setOnClickListener(mAddImpressOnClickListener);
                } else {
                    bean.setCheck(1);
                }
            }
            myTextView.setBean(bean);
            mImpressGroup.addView(myTextView);
        }
        findViewById(R.id.add_impress).setOnClickListener(mAddImpressOnClickListener);
    }


    /**
     * 显示贡献榜
     */
    private void showContribute(String conJson) {
        List<UserHomeConBean> list = JSON.parseArray(conJson, UserHomeConBean.class);
        if (list.size() == 0) {
            return;
        }
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            ImageView imageView = (ImageView) mInflater.inflate(R.layout.view_user_home_con, mConGroup, false);
            ImgLoader.display(list.get(i).getAvatar(), imageView);
            mConGroup.addView(imageView);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float totalScrollRange = appBarLayout.getTotalScrollRange();
        float rate = -1 * verticalOffset / totalScrollRange * 2;
        if (rate >= 1) {
            rate = 1;
        }
        if (mRate != rate) {
            mRate = rate;
            mTitleView.setAlpha(rate);
            int a = (int) (mWhiteColorArgb[0] * (1 - rate) + mBlackColorArgb[0] * rate);
            int r = (int) (mWhiteColorArgb[1] * (1 - rate) + mBlackColorArgb[1] * rate);
            int g = (int) (mWhiteColorArgb[2] * (1 - rate) + mBlackColorArgb[2] * rate);
            int b = (int) (mWhiteColorArgb[3] * (1 - rate) + mBlackColorArgb[3] * rate);
            int color = Color.argb(a, r, g, b);
            mBtnBack.setColorFilter(color);
            mBtnShare.setColorFilter(color);
        }
    }

    /**
     * 获取颜色的argb
     */
    private int[] getColorArgb(int color) {
        return new int[]{Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)};
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_share:
                share();
                break;
//            case R.id.btn_fans:
            case R.id.ll_fans:
                forwardFans();
                break;
//            case R.id.btn_follow:
            case R.id.ll_follow:
                forwardFollow();
                break;
            case R.id.btn_follow_2:
                follow();
                break;
            case R.id.con_group_wrap:
                forwardContribute();
                break;
            case R.id.guard_group_wrap:
                forwardGuardList();
                break;
            case R.id.btn_pri_msg:
                forwardMsg();
                break;
            case R.id.btn_black:
                setBlack();
                break;
            case R.id.btn_enter2:
                if (mSearchUserBean.getIslive() == 1) {
                    watchLive();
                } else {
                    ToastUtil.show(WordUtil.getString(R.string.nolive));
                }
                break;
        }
    }

    /**
     * 观看直播
     */
    public void watchLive() {
        if (mLiveBean != null) {
            if (mCheckLivePresenter == null) {
                mCheckLivePresenter = new CheckLivePresenter(mContext);
            }
            mCheckLivePresenter.watchLive(mLiveBean);
        }
    }

    private void back() {
        if (mContext instanceof UserHomeActivity) {
            ((UserHomeActivity) mContext).onBackPressed();
        }
    }

    /**
     * 关注
     */
    private void follow() {
        HttpUtil.setAttention(Constants.FOLLOW_FROM_HOME, mToUid, new CommonCallback<Integer>() {
            @Override
            public void callback(Integer isAttention) {
                onAttention(isAttention);
            }
        });
    }

    /**
     * 私信
     */
    private void forwardMsg() {
        if (mSearchUserBean != null) {
            ChatRoomActivity.forward(mContext, mSearchUserBean, mSearchUserBean.getAttention() == 1);
        }
    }

    private void onAttention(int isAttention) {
        if (mBtnFollow2 != null) {
            mBtnFollow2.setText(isAttention == 1 ? R.string.following : R.string.follow);
        }
        if (mBtnBlack != null) {
            if (isAttention == 1) {
                mBtnBlack.setText(R.string.black);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (e.getToUid().equals(mToUid)) {
            int isAttention = e.getIsAttention();
            if (mSearchUserBean != null) {
                mSearchUserBean.setAttention(isAttention);
            }
            onAttention(isAttention);
        }
    }

    /**
     * 添加印象
     */
    private void addImpress() {
        if (mContext instanceof UserHomeActivity) {
            ((UserHomeActivity) mContext).addImpress(mToUid);
        }
    }

    /**
     * 刷新印象
     */
    public void refreshImpress() {
        HttpUtil.getUserHome(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (mImpressGroup != null) {
                        mImpressGroup.removeAllViews();
                    }
                    showImpress(obj.getString("label"));
                }
            }
        });
    }

    /**
     * 查看贡献榜
     */
    private void forwardContribute() {
        Intent intent = new Intent(mContext, LiveContributeActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }

    /**
     * 查看守护榜
     */
    private void forwardGuardList() {
        LiveGuardListActivity.forward(mContext, mToUid);
    }

    /**
     * 前往TA的关注
     */
    private void forwardFollow() {
        Intent intent = new Intent(mContext, FollowActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }

    /**
     * 前往TA的粉丝
     */
    private void forwardFans() {
        Intent intent = new Intent(mContext, FansActivity.class);
        intent.putExtra(Constants.TO_UID, mToUid);
        mContext.startActivity(intent);
    }

    /**
     * 拉黑，解除拉黑
     */
    private void setBlack() {
        HttpUtil.setBlack(mToUid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    boolean isblack = JSON.parseObject(info[0]).getIntValue("isblack") == 1;
                    mBtnBlack.setText(isblack ? R.string.black_ing : R.string.black);
                    if (isblack) {
                        mBtnFollow2.setText(R.string.follow);
                    }
                }
            }
        });
    }

    /**
     * 分享
     */
    private void share() {
        LiveShareDialogFragment fragment = new LiveShareDialogFragment();
        fragment.setActionListener(this);
        fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "LiveShareDialogFragment");
    }


    @Override
    public void onItemClick(String type) {
        if (Constants.LINK.equals(type)) {
            copyLink();
        } else {
            shareHomePage(type);
        }
    }

    /**
     * 复制页面链接
     */
    private void copyLink() {
        if (mUserHomeSharePresenter != null) {
            mUserHomeSharePresenter.copyLink();
        }
    }


    /**
     * 分享页面链接
     */
    private void shareHomePage(String type) {
        if (mUserHomeSharePresenter != null) {
            mUserHomeSharePresenter.shareHomePage(type);
        }
    }

    @Override
    public void release() {
        super.release();
        EventBus.getDefault().unregister(this);
        if (mUserHomeSharePresenter != null) {
            mUserHomeSharePresenter.release();
        }
    }


}
