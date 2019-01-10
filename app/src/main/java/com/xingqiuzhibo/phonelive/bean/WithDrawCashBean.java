package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class WithDrawCashBean {

    private String account;// (string, optional): 账号 ,
    private String accountBank;// (string, optional): 银行名称 ,
    private Integer addtime;// (integer, optional): 申请时间 ,
    private Integer cashType;// (integer, optional): 提现分类 0银票提现 1钻石提现 ,
    private Integer id;// (integer, optional),
    private Integer money;// (integer, optional): 提现金额 ,
    private String name;// (string, optional): 姓名 ,
    private String orderno;// (string, optional): 订单号 ,
    private Integer status;// (integer, optional): 状态 0审核中，1审核通过，2审核拒绝 ,
    private String tradeNo;// (string, optional): 三方订单号 ,
    private Integer type;// (integer, optional): 账号类型 ,1表示支付宝，2表示微信，3表示银行卡'4 USDT
    private Integer uid;// (integer, optional): 用户ID ,
    private Integer uptime;// (integer, optional): 更新 ,
    private Integer userType;// (integer, optional): 用户类型 1会员 2代理公司 ,
    private Integer votes;// (integer, optional): 提现映票数/钻石数

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }

    public Integer getCashType() {
        return cashType;
    }

    public void setCashType(Integer cashType) {
        this.cashType = cashType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getUptime() {
        return uptime;
    }

    public void setUptime(Integer uptime) {
        this.uptime = uptime;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
