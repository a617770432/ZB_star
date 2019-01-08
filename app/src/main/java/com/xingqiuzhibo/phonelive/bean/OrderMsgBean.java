package com.xingqiuzhibo.phonelive.bean;

import java.util.List;

public class OrderMsgBean {
    private OrderInfo orderInfo;
    private List<PayStyle> payStyle;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<PayStyle> getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(List<PayStyle> payStyle) {
        this.payStyle = payStyle;
    }

    public class OrderInfo {
        private Integer id;//": 15,
        private Integer uid;//": 22376,
        private Integer touid;//": 22376,
        private float money;//": 0.01,
        private Integer coin;//": 600,
        private Integer coinGive;//": 0,
        private String orderno;//": "22376_20181208184819386",
        private String tradeNo;//": "",
        private Integer status;//": 0,
        private Integer addtime;//": 1544266099,
        private Integer type;//": 2,
        private Integer ambient;//": 0,
        private Integer paymode;//": 0,
        private String receivables;//": ""

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Integer getTouid() {
            return touid;
        }

        public void setTouid(Integer touid) {
            this.touid = touid;
        }

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }

        public Integer getCoin() {
            return coin;
        }

        public void setCoin(Integer coin) {
            this.coin = coin;
        }

        public Integer getCoinGive() {
            return coinGive;
        }

        public void setCoinGive(Integer coinGive) {
            this.coinGive = coinGive;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getAddtime() {
            return addtime;
        }

        public void setAddtime(Integer addtime) {
            this.addtime = addtime;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getAmbient() {
            return ambient;
        }

        public void setAmbient(Integer ambient) {
            this.ambient = ambient;
        }

        public Integer getPaymode() {
            return paymode;
        }

        public void setPaymode(Integer paymode) {
            this.paymode = paymode;
        }

        public String getReceivables() {
            return receivables;
        }

        public void setReceivables(String receivables) {
            this.receivables = receivables;
        }
    }

    public class PayStyle {
        private Integer id;//":10,
        private Integer uid;//":1,
        private String userName;//":"拉克丝",
        private Integer type;//":3,
        private String accountBank;//":"光辉宝库",
        private String account;//":"123456789",
        private String qrcode;//":"asffafasas",
        private Integer dayLimit;//":1000,
        private Integer monthLimit;//":1000,
        private String createTime;//":"2019-01-04 14:36:45",
        private Integer sort;//":1,
        private Integer dayAmount;//":1000,
        private Integer monthAmount;//":1000,
        private String remark;//":null

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getAccountBank() {
            return accountBank;
        }

        public void setAccountBank(String accountBank) {
            this.accountBank = accountBank;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public Integer getDayLimit() {
            return dayLimit;
        }

        public void setDayLimit(Integer dayLimit) {
            this.dayLimit = dayLimit;
        }

        public Integer getMonthLimit() {
            return monthLimit;
        }

        public void setMonthLimit(Integer monthLimit) {
            this.monthLimit = monthLimit;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public Integer getDayAmount() {
            return dayAmount;
        }

        public void setDayAmount(Integer dayAmount) {
            this.dayAmount = dayAmount;
        }

        public Integer getMonthAmount() {
            return monthAmount;
        }

        public void setMonthAmount(Integer monthAmount) {
            this.monthAmount = monthAmount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
