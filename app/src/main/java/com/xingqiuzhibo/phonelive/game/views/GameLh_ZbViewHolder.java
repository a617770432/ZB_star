package com.xingqiuzhibo.phonelive.game.views;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingqiuzhibo.phonelive.event.GameWindowEvent;
import com.xingqiuzhibo.phonelive.utils.GameDialogUils;
import com.xingqiuzhibo.phonelive.utils.WordUtil;
import com.umeng.commonsdk.debug.W;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.game.GameSoundPool;
import com.xingqiuzhibo.phonelive.game.bean.GameParam;
import com.xingqiuzhibo.phonelive.game.socket.SocketGameUtil;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpConsts;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.utils.L;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by debug on 2018/12/10.
 * 龙虎 游戏 主播
 */

public class GameLh_ZbViewHolder extends AbsGameLhViewHolder {
    private TextView[] mCanWinNums;
    private TextView[] mBetSums;
    private TextView[] mBetPers;
    private TextView[] mWinView;
    private TextView mBtnCloseGame, mBtnStartOrBalance;
    private int mWin = -1;//龙虎和123

    public GameLh_ZbViewHolder(GameParam param, GameSoundPool gameSoundPool) {
        super(param, gameSoundPool);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.game_view_lhh_zb;
    }

    @Override
    public void init() {
        super.init();
        mCanWinNums = new TextView[3];
        mBetSums = new TextView[3];
        mBetPers = new TextView[3];
        mWinView = new TextView[3];
        mCanWinNums[0] = (TextView) findViewById(R.id.can_wins_l);
        mCanWinNums[1] = (TextView) findViewById(R.id.can_wins_hu);
        mCanWinNums[2] = (TextView) findViewById(R.id.can_wins_he);
        mBetSums[0] = (TextView) findViewById(R.id.coin_l);
        mBetSums[1] = (TextView) findViewById(R.id.coin_hu);
        mBetSums[2] = (TextView) findViewById(R.id.coin_he);

        mBetPers[0] = (TextView) findViewById(R.id.persons_l);
        mBetPers[1] = (TextView) findViewById(R.id.persons_hu);
        mBetPers[2] = (TextView) findViewById(R.id.persons_he);

        mWinView[0] = (TextView) findViewById(R.id.l_win);
        mWinView[1] = (TextView) findViewById(R.id.hu_win);
        mWinView[2] = (TextView) findViewById(R.id.he_win);

        mBtnCloseGame = (TextView) findViewById(R.id.exit_game);
        mBtnStartOrBalance = (TextView) findViewById(R.id.start_or_balance);
        mBtnCloseGame.setOnClickListener(this);
        mBtnStartOrBalance.setOnClickListener(this);
        for (View v : mWinView) {
            v.setOnClickListener(this);
        }
    }


    /**
     * 处理socket回调的数据
     */
    public void handleSocket(int action, JSONObject obj) {
        if (mEnd) {
            return;
        }
        switch (action) {
            case SocketGameUtil.GAME_ACTION_LH_OPENVIEW://打开游戏窗口
                onGameWindowShow(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_STARTBET://开始下注
                onGameBetStart(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BETING://收到下注消息
                onGameBetChanged(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BETEND://下注结束
                onBetEnd(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BANLANCE_RESULT://收到游戏结果揭晓的的消息
                onGameResult(obj);
                break;
            case SocketGameUtil.GAME_ACTION_LH_BANLANCE_RESULT2:
                onGameResultView(obj.getJSONObject("ct"));
                break;
            case SocketGameUtil.GAME_ACTION_LH_CLOSE://主播关闭游戏
                onGameClose();
                break;
        }
    }


    /**
     * 主播点开始按钮，请求接口，创建游戏
     */
    @Override
    public void anchorCreateGame() {

        HttpUtil.dragonStart(mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    mBtnCloseGame.setEnabled(false);
                }
                ToastUtil.show(msg);
            }
        });

    }

    @Override
    protected void onGameBetStart(JSONObject obj) {
        super.onGameBetStart(obj);
        gameStart();
    }

    /**
     * 所有人收到下注的观众socket，更新下注金额
     */
    @Override
    protected void onGameBetChanged(JSONObject obj) {
        JSONObject objCt = obj.getJSONObject("ct");
        String uid = objCt.getString("uid");
        int part = objCt.getIntValue("part");
        int index = part - 1;
        boolean isSelf = uid.equals(AppConfig.getInstance().getUid());
        if (isSelf) {//自己下的注
            playGameSound(GameSoundPool.GAME_SOUND_BET_SUCCESS);
        }
        if (index >= 0 && index < 4) {
            if (mBetSums != null) {
                if (mBetSums[index] != null) {
                    setView(mBetSums[index], objCt.getString("coinall_" + part));
                }
            }
            if (mBetPers != null) {
                if (mBetPers[index] != null) {
                    setView(mBetPers[index], objCt.getString("num_" + part));
                }
            }
        }
    }

    @Override
    protected void onBetEnd(JSONObject object) {
        mBetEndJSONObject = object;
        if (mBetCount > 0) {
            return;
        }
        if (object != null) {
            JSONObject objCt = object.getJSONObject("ct");
            setCanWinNums(objCt.getIntValue("will_1"), objCt.getIntValue("will_2"), objCt.getIntValue("will_3"));
        }
        isCanBalance = true;
        mBetStarted = false;
        mBtnStartOrBalance.setText(WordUtil.getString(R.string.game_balance));
        for (View v : mWinView) {
            v.setEnabled(true);
        }
    }

    @Override
    protected void onGameResult(JSONObject object) {
        isCanBalance = false;
        mBtnStartOrBalance.setText(WordUtil.getString(R.string.game_start));
        setCanWinNums(0, 0, 0);
        mBtnCloseGame.setEnabled(true);
        for (View v : mWinView) {
            v.setEnabled(false);
        }
        onGameResultDialog(object.getJSONObject("ct"));
    }

    @Override
    public void anchorCloseGame() {
        if (mBetStarted) {
            ToastUtil.show(R.string.game_wait_end);
            return;
        }
        HttpUtil.dragonOver(mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void setLastCoin(String coin) {
    }

    @Override
    protected void betCountDown() {
        super.betCountDown();
        if (!mBetStarted) {
            if (mBetEndJSONObject != null) {
                onBetEnd(mBetEndJSONObject);
                mBetEndJSONObject = null;
            }
        }
    }

    /**
     * 结算
     */
    private void gameBalance() {
        if (mWin == 1) {
            gameBalanceDialog(WordUtil.getString(R.string.game_l_win_confirm), WordUtil.getString(R.string.game_l_win));
        } else if (mWin == 2) {
            gameBalanceDialog(WordUtil.getString(R.string.game_hu_win_confirm), WordUtil.getString(R.string.game_hu_win));
        } else {
            gameBalanceDialog(WordUtil.getString(R.string.game_he_win_confirm), WordUtil.getString(R.string.game_he_win));

        }
    }

    private void gameBalanceDialog(String tit, final String content) {
        GameDialogUils.showSelectGameResultDialog(mContext, tit, content, new GameDialogUils.OnCliCkConfirmListener() {
            @Override
            public void onConfirm() {
                L.e("--socket5---dragonEnd>>>" + "---mStream>>" + mStream + "---mWin>>" + mWin);
                HttpUtil.dragonEnd(mStream, mWin, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        L.e("--socket5---dragonEnd>>>" + info + msg + code);
                        ToastUtil.show(msg);
                    }
                });
            }
        });
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
    }

    @Override
    public void release() {
        mEnd = true;
        HttpUtil.cancel(HttpConsts.GET_COIN);
        HttpUtil.cancel(HttpConsts.GAME_LUCK_PAN_CREATE);
        HttpUtil.cancel(HttpConsts.GAME_LUCK_PAN_BET);
        HttpUtil.cancel(HttpConsts.GAME_SETTLE);
        super.release();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.l_win:
                mWin = 1;
                gameBalance();
                break;
            case R.id.he_win:
                mWin = 3;
                gameBalance();
                break;
            case R.id.hu_win:
                mWin = 2;
                gameBalance();
                break;
            case R.id.exit_game:
                anchorCloseGame();
                break;
            case R.id.start_or_balance:
                //开始
                if (!mBetStarted && !isCanBalance) {

                    anchorCreateGame();
                    return;
                }
                //等待
                if (mBetStarted) {
                    ToastUtil.show(WordUtil.getString(R.string.game_lh_wait_balance));
                    return;
                }
                //结算
                if (isCanBalance) {
                    ToastUtil.show(WordUtil.getString(R.string.game_lh_balancing));
                    return;
                }
                break;
        }
    }

    private void setView(TextView betView, String str) {
        betView.setText(str);
    }

    private void setCanWinNums(int will_1, int will_2, int will_3) {
        mCanWinNums[0].setText(WordUtil.getString(R.string.game_can_win) + will_1);
        mCanWinNums[1].setText(WordUtil.getString(R.string.game_can_win) + will_2);
        mCanWinNums[2].setText(WordUtil.getString(R.string.game_can_win) + will_3);
    }

    private void gameStart() {
        mBtnStartOrBalance.setText(WordUtil.getString(R.string.game_waiting));
        setCanWinNums(0, 0, 0);
        for (TextView v : mBetSums) {
            setView(v, "0");
        }
        for (TextView v : mBetPers) {
            setView(v, "0");
        }

    }
}
