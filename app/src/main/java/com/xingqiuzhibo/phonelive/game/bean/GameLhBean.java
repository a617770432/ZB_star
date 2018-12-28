package com.xingqiuzhibo.phonelive.game.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by debug on 2018/12/19.
 */

public class GameLhBean {

    /**
     * id : 0
     * status : 2
     * time : 0
     * coin_1 : 0
     * coin_2 : 0
     * coin_3 : 0
     * rate_1 : 1
     * rate_2 : 1
     * rate_3 : 1
     * coinall_1 : 0
     * coinall_2 : 0
     * coinall_3 : 0
     * num_1 : 0
     * num_2 : 0
     * num_3 : 0
     * mainarr : []
     * logarray : []
     * long : 0
     * hu : 0
     * he : 0
     */

    private String id;
    private int status;
    private int time;
    private String coin_1;
    private String coin_2;
    private String coin_3;
    private String rate_1;
    private String rate_2;
    private String rate_3;
    private String coinall_1;
    private String coinall_2;
    private String coinall_3;
    private String num_1;
    private String num_2;
    private String num_3;
    @SerializedName("long")
    private String longX;
    private String hu;
    private String he;
    private String mainarr;
    private String logarray;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCoin_1() {
        return coin_1;
    }

    public void setCoin_1(String coin_1) {
        this.coin_1 = coin_1;
    }

    public String getCoin_2() {
        return coin_2;
    }

    public void setCoin_2(String coin_2) {
        this.coin_2 = coin_2;
    }

    public String getCoin_3() {
        return coin_3;
    }

    public void setCoin_3(String coin_3) {
        this.coin_3 = coin_3;
    }

    public String getRate_1() {
        return rate_1;
    }

    public void setRate_1(String rate_1) {
        this.rate_1 = rate_1;
    }

    public String getRate_2() {
        return rate_2;
    }

    public void setRate_2(String rate_2) {
        this.rate_2 = rate_2;
    }

    public String getRate_3() {
        return rate_3;
    }

    public void setRate_3(String rate_3) {
        this.rate_3 = rate_3;
    }

    public String getCoinall_1() {
        return coinall_1;
    }

    public void setCoinall_1(String coinall_1) {
        this.coinall_1 = coinall_1;
    }

    public String getCoinall_2() {
        return coinall_2;
    }

    public void setCoinall_2(String coinall_2) {
        this.coinall_2 = coinall_2;
    }

    public String getCoinall_3() {
        return coinall_3;
    }

    public void setCoinall_3(String coinall_3) {
        this.coinall_3 = coinall_3;
    }

    public String getNum_1() {
        return num_1;
    }

    public void setNum_1(String num_1) {
        this.num_1 = num_1;
    }

    public String getNum_2() {
        return num_2;
    }

    public void setNum_2(String num_2) {
        this.num_2 = num_2;
    }

    public String getNum_3() {
        return num_3;
    }

    public void setNum_3(String num_3) {
        this.num_3 = num_3;
    }
    @JSONField(name = "long")
    public String getLongX() {
        return longX;
    }

    @JSONField(name = "long")
    public void setLongX(String longX) {
        this.longX = longX;
    }

    public String getHu() {
        return hu;
    }

    public void setHu(String hu) {
        this.hu = hu;
    }

    public String getHe() {
        return he;
    }

    public void setHe(String he) {
        this.he = he;
    }

    public String getMainarr() {
        return mainarr;
    }

    public void setMainarr(String mainarr) {
        this.mainarr = mainarr;
    }

    public String getLogarray() {
        return logarray;
    }

    public void setLogarray(String logarray) {
        this.logarray = logarray;
    }
}
