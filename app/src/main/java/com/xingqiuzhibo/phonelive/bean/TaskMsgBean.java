package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class TaskMsgBean {
    private String id;
    private String config_name;//": "每成功邀请1位用户，下载APP并登录",
    private String config_type;//": "INVITE_USERS_COIN_NUM",
    private String config_value;//": "500",
    private String config_status;//": 1--1是未完成，2是已完成

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfig_name() {
        return config_name;
    }

    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    public String getConfig_type() {
        return config_type;
    }

    public void setConfig_type(String config_type) {
        this.config_type = config_type;
    }

    public String getConfig_value() {
        return config_value;
    }

    public void setConfig_value(String config_value) {
        this.config_value = config_value;
    }

    public String getConfig_status() {
        return config_status;
    }

    public void setConfig_status(String config_status) {
        this.config_status = config_status;
    }
}
