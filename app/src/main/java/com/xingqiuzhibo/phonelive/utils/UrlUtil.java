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
    //订单申诉
    public static String APPEAL_MSG_UPADTE = AppConfig.BASE_URL + "cmfUserssAppeald/save";
    //订单申诉历史记录
    public static String APPEAL_HISTORY_LIST = AppConfig.BASE_URL + "cmfUserssAppeald/getHistoryLogList";


    //钻石明细
    public static String COIN_DETAIL_LIST = AppConfig.BASE_URL + "chargeconfig/getConsumelistByUid";
    //提现记录
    public static String CHANGE_CASH_HISTORY = AppConfig.BASE_URL + "cmfUsersCashrecord/getUsersCashRecordList";
    //收礼物明细
    public static String GET_GIFT_DETAIL = AppConfig.BASE_URL + "chargeconfig/getGiftlistByUid";
    //直播时长
    public static String GET_TIME_DETAIL_MSG = AppConfig.BASE_URL + "chargeconfig/getShowlistByUid";

    //分销任务明细
    public static String SHARE_TASK_MSG = AppConfig.HOST + "/index.php";

    //注册密保问题
    public static String REGISTER_PROTECT_QUESTION = AppConfig.HOST + "/api/public/?service=Login.secretQuestion";
    //账号注册
    public static String REGISTER_MSG_UPDATE = AppConfig.HOST + "/api/public/?service=Login.userRegister";
    //密码找回
    public static String FIND_PWD_UPDATE = AppConfig.HOST + "/api/public/?service=Login.userFindPassword";

    //获取动态列表
    public static String GET_TIE_ZI = AppConfig.BASE_URL + "terminfo/list";
    //可选钻石列表
    public static String DIAMOND_LIST = AppConfig.BASE_URL + "terminfo/diamondList";
    //获取一二级栏目
    public static String RANGE_LIST = AppConfig.BASE_URL + "termrange/list";
    //发布动态
    public static String PUBLISH = AppConfig.BASE_URL + "terminfo/save";
    //上传文件
    public static String UPLOAD_FILE = AppConfig.BASE_URL + "base/file/upload";

}
