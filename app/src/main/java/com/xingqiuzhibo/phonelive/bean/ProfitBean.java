package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/7/007.
 * 用户收益基础信息
 */

public class ProfitBean {
    private Integer coin;//": 0,用户拥有钻石
    private Integer useCoin;//": 0,可提现钻石
    private Integer coinTotal;//": 0,钻石总收益
    private Integer votes;//": 0,用户拥有映票
    private Integer useVotes;//": 0,可提现映票
    private float votestotal;//": 0,总映票
    private float diamondRate;//": "0.8",钻石提现比例
    private String tickRate;//: "1",映票提现比例
    private String cashExplain;//": "温馨提示:每月1-31号可进行提现申请，收益将在次月1-5号统一发放，每月只可提现一次"
    private String account;//提箱账户
    private Integer cashAccountId;
    private Integer cashMin;//最低提现额度
    private Integer accountType;//  1表示支付宝，2表示微信，3表示银行卡

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getUseCoin() {
        return useCoin;
    }

    public void setUseCoin(Integer useCoin) {
        this.useCoin = useCoin;
    }

    public Integer getCoinTotal() {
        return coinTotal;
    }

    public void setCoinTotal(Integer coinTotal) {
        this.coinTotal = coinTotal;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getUseVotes() {
        return useVotes;
    }

    public void setUseVotes(Integer useVotes) {
        this.useVotes = useVotes;
    }

    public float getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(float votestotal) {
        this.votestotal = votestotal;
    }

    public float getDiamondRate() {
        return diamondRate;
    }

    public void setDiamondRate(float diamondRate) {
        this.diamondRate = diamondRate;
    }

    public String getTickRate() {
        return tickRate;
    }

    public void setTickRate(String tickRate) {
        this.tickRate = tickRate;
    }

    public String getCashExplain() {
        return cashExplain;
    }

    public void setCashExplain(String cashExplain) {
        this.cashExplain = cashExplain;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(Integer cashAccountId) {
        this.cashAccountId = cashAccountId;
    }

    public Integer getCashMin() {
        return cashMin;
    }

    public void setCashMin(Integer cashMin) {
        this.cashMin = cashMin;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
