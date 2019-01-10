package com.xingqiuzhibo.phonelive.bean;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/8/008.
 * 分销（分享）任务bean
 */

public class ShareTaskBean {
    private String code;//": "384768",
    private String qrcode_url;//": "http:\/\/local.xingqiupindao-web.com\/index.php?g=Appapi&m=Agent&a=qrcode&code=384768",
    private String share_url;
    private String count;//": "0",
    private String one_profit;//": "0",
    private String spread_list;//": "推广任务",
    private String noviciate_list;//": "新手任务",
    private String release_list;//": "发帖赚取收益",

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOne_profit() {
        return one_profit;
    }

    public void setOne_profit(String one_profit) {
        this.one_profit = one_profit;
    }

    public String getSpread_list() {
        return spread_list;
    }

    public void setSpread_list(String spread_list) {
        this.spread_list = spread_list;
    }

    public String getNoviciate_list() {
        return noviciate_list;
    }

    public void setNoviciate_list(String noviciate_list) {
        this.noviciate_list = noviciate_list;
    }

    public String getRelease_list() {
        return release_list;
    }

    public void setRelease_list(String release_list) {
        this.release_list = release_list;
    }
}
