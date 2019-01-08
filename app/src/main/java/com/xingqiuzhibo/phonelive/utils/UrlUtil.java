package com.xingqiuzhibo.phonelive.utils;

import com.xingqiuzhibo.phonelive.AppConfig;

/**
 * Created by hx
 * Time 2019/1/4/004.
 * Java请求路径
 */

public class UrlUtil {
    //获取订单信息及支付列表
    public static String GET_ORDER_MSG = AppConfig.BASE_URL + "chargeconfig/payStyleList";
    //身份认证提交信息
    public static String UPDATE_PERSONAL_INFO = AppConfig.BASE_URL + "userauth/submit";
    //身份认证信息查询
    public static String SELECT_PERSONAL_INFO = AppConfig.BASE_URL + "userauth/info/";


    //查询用户收益基础信息
    public static String GET_USER_PROFIT_MSG = AppConfig.BASE_URL + "cmfUsersCashrecord/getUserCashInfo";
    //提现申请
    public static String USER_PROFIT_APPLY = AppConfig.BASE_URL + "cmfUsersCashrecord/save";
    //确认付款
    public static String CONFIRM_CHARGE_ORDER = AppConfig.BASE_URL + "cmfUsersCharge/save";


}
