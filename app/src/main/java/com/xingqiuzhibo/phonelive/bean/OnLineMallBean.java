package com.xingqiuzhibo.phonelive.bean;

import java.util.List;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class OnLineMallBean {
    private User user;//用户信息
    private List<VIPList> vip_list;//vip商城信息
    private List<NiceNum> liang_list;//靓号信息
    private List<CarInfo> car_list;//坐骑信息

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<VIPList> getVip_list() {
        return vip_list;
    }

    public void setVip_list(List<VIPList> vip_list) {
        this.vip_list = vip_list;
    }

    public List<NiceNum> getLiang_list() {
        return liang_list;
    }

    public void setLiang_list(List<NiceNum> liang_list) {
        this.liang_list = liang_list;
    }

    public List<CarInfo> getCar_list() {
        return car_list;
    }

    public void setCar_list(List<CarInfo> car_list) {
        this.car_list = car_list;
    }

    /**
     * 用户信息
     */
    public class User {
        private String id;//": "22572", // 用户id
        private String user_nicename;//: "bin", // 用户昵称
        private String coin;//": "88100" // 用户钻石余额
        private Integer user_vip_status;//":1 0没有vip 1有vip
        private String user_vip_endtime;//vip到期时间

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public Integer getUser_vip_status() {
            return user_vip_status;
        }

        public void setUser_vip_status(Integer user_vip_status) {
            this.user_vip_status = user_vip_status;
        }

        public String getUser_vip_endtime() {
            return user_vip_endtime;
        }

        public void setUser_vip_endtime(String user_vip_endtime) {
            this.user_vip_endtime = user_vip_endtime;
        }
    }

    /**
     * vip 信息
     */
    public class VIPList {
        private String id;
        private String coin;
        private String length_text;//"3个月"
        private boolean check = false;

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getLength_text() {
            return length_text;
        }

        public void setLength_text(String length_text) {
            this.length_text = length_text;
        }
    }

    /**
     * 靓号信息
     */
    public class NiceNum {
        private String id;//": "13", // 靓号ID
        private String name;//": "888888", // 靓号
        private String length;//": "6", // 长度
        private String coin;//": "8000000", // 价格
        private String coin_date;//": "8,000,000钻石" // 价格text

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getCoin_date() {
            return coin_date;
        }

        public void setCoin_date(String coin_date) {
            this.coin_date = coin_date;
        }
    }

    /**
     * 坐骑信息
     */
    public class CarInfo {
        private String id;//":"2",
        private String name;//":"小毛驴",
        private String thumb;//":"http:\/\/www.xingqiupindao.com\/data\/upload\/20171031\/59f7e822ae0d6.png",
        private String swf;//":"http:\/\/www.xingqiupindao.com\/data\/upload\/20171031\/59f7e82b2aafa.gif",
        private String swftime;//":"4.00",
        private String needcoin;//":"200",
        private String orderno;//":"0",
        private String addtime;//":"1500455559",
        private String words;//":"骑着小毛驴进场了",
        private String uptime;//":"0"

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getSwf() {
            return swf;
        }

        public void setSwf(String swf) {
            this.swf = swf;
        }

        public String getSwftime() {
            return swftime;
        }

        public void setSwftime(String swftime) {
            this.swftime = swftime;
        }

        public String getNeedcoin() {
            return needcoin;
        }

        public void setNeedcoin(String needcoin) {
            this.needcoin = needcoin;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }
    }
}
