package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class ShareListBean {
    private String id;//": "22521", // 用户ID
    private String user_nicename;//": "手机用户2356", // 用户昵称
    private String avatar;//": "\/default.jpg", // 用户头像
    private String avatar_thumb;//": "\/default_thumb.jpg", // 小头像
    private String sex;//": "2", // 性别；0：保密，1：男；2：女
    private String token;//": "14d1f2448eb5d8b1faea02a61b1ec6a4" // 用户token

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
