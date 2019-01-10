package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class AppealHisBean {

    private long addtime;// (integer, optional): 申诉时间 ,
    private String appealContent;// (string, optional): 申诉内容 ,
    private String appealImg1;// (string, optional): 申诉图1 ,
    private String appealImg2;// (string, optional): 申诉图2 ,
    private String appealImg3;// (string, optional): 申诉图3 ,
    private String chargeId;// (integer, optional): 订单ID ,
    private long handlingtime;// (integer, optional): 回复时间 ,
    private Integer id;// (integer, optional),
    private String reply;// (string, optional): 管理员回复 ,
    private String status;// (integer, optional): 申诉状态 4处理中 5失败 6成功 ,
    private Integer uid;// (integer, optional): 用户ID

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getAppealContent() {
        return appealContent;
    }

    public void setAppealContent(String appealContent) {
        this.appealContent = appealContent;
    }

    public String getAppealImg1() {
        return appealImg1;
    }

    public void setAppealImg1(String appealImg1) {
        this.appealImg1 = appealImg1;
    }

    public String getAppealImg2() {
        return appealImg2;
    }

    public void setAppealImg2(String appealImg2) {
        this.appealImg2 = appealImg2;
    }

    public String getAppealImg3() {
        return appealImg3;
    }

    public void setAppealImg3(String appealImg3) {
        this.appealImg3 = appealImg3;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public long getHandlingtime() {
        return handlingtime;
    }

    public void setHandlingtime(long handlingtime) {
        this.handlingtime = handlingtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
