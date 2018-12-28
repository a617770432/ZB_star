package com.xingqiuzhibo.phonelive.game.bean;

/**
 * Created by debug on 2018/12/19.
 */

public class GameLhRecordBean {

    /**
     * win : 龙赢
     * wincoin : -10
     * vote : 虎
     * coin : 10
     */

    private String win;
    private String wincoin;
    private int wintype;
    private String vote;
    private String coin;

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getWincoin() {
        return wincoin;
    }

    public void setWincoin(String wincoin) {
        this.wincoin = wincoin;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public int getWintype() {
        return wintype;
    }

    public void setWintype(int wintype) {
        this.wintype = wintype;
    }
}
