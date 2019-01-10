package com.xingqiuzhibo.phonelive.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cxf on 2018/9/28.
 */

public class StringUtil {
    private static DecimalFormat sDecimalFormat;
    private static DecimalFormat sDecimalFormat2;
    // private static Pattern sPattern;
    private static Pattern sIntPattern;


    static {
        sDecimalFormat = new DecimalFormat("#.#");
        sDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        sDecimalFormat2 = new DecimalFormat("#.##");
        sDecimalFormat2.setRoundingMode(RoundingMode.HALF_UP);
        //sPattern = Pattern.compile("[\u4e00-\u9fa5]");
        sIntPattern = Pattern.compile("^[-\\+]?[\\d]*$");
    }

    public static String format(double value) {
        return sDecimalFormat.format(value);
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d) + "W";
    }


    /**
     * 把数字转化成多少万
     */
    public static String toWan2(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d);
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan3(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat2.format(num / 10000d) + "w";
    }

//    /**
//     * 判断字符串中是否包含中文
//     */
//    public static boolean isContainChinese(String str) {
//        Matcher m = sPattern.matcher(str);
//        if (m.find()) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 判断一个字符串是否是数字
     */
    public static boolean isInt(String str) {
        return sIntPattern.matcher(str).matches();
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        String s = "";
        if (hours > 0) {
            if (hours < 10) {
                s += "0" + hours + ":";
            } else {
                s += hours + ":";
            }
        }
        if (minutes > 0) {
            if (minutes < 10) {
                s += "0" + minutes + ":";
            } else {
                s += minutes + ":";
            }
        } else {
            s += "00" + ":";
        }
        if (seconds > 0) {
            if (seconds < 10) {
                s += "0" + seconds;
            } else {
                s += seconds;
            }
        } else {
            s += "00";
        }
        return s;
    }

    /**
     * 把一个long类型的总毫秒数转成时长连接符-
     */
    public static String getDurationTextLine(long mms) {
        Date date = new Date();
        //格式里的时如果用hh表示用12小时制，HH表示用24小时制。MM必须是大写!
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.setTime(mms * 1000);//java里面应该是按毫秒
        return sdf.format(date);
    }

    /**
     * 把一个long类型的总毫秒数转成时长连接符-到小时分
     */
    public static String getDurationTextLineMinute(long mms) {
        Date date = new Date();
        //格式里的时如果用hh表示用12小时制，HH表示用24小时制。MM必须是大写!
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        date.setTime(mms * 1000);//java里面应该是按毫秒
        return sdf.format(date);
    }

    /**
     * 检查电话号码，如果为11位数字返回true，否则false
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        if (null == phoneNumber)
            return false;
        String regex = "[0-9]{11}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        boolean b = matcher.matches();
        return b;
    }

    /**
     * 检查电验证码，如果为6位数字返回true，否则false
     */
    public static boolean checkCodeNumber(String code) {
        if (null == code)
            return false;
        String regex = "[0-9]{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        boolean b = matcher.matches();
        return b;
    }

    /**
     * 检查密码
     */
    public static boolean checkPassword(String password) {
        String regex = "[a-zA-Z0-9]{6,15}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        String regex1 = "[a-z]{6,15}";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(password);
        String regex2 = "[A-Z]{6,15}";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(password);
        String regex3 = "[0-9]{6,15}";
        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(password);
        boolean b = matcher.matches();
        boolean b1 = matcher1.matches();
        boolean b2 = matcher2.matches();
        boolean b3 = matcher3.matches();
        if (b1 || b2 || b3) {
            ToastUtil.show("密码为数字和字母组合！");
            b = false;
        }
        return b;
    }

    /**
     * 检查两个String是否内容相同
     */
    public static boolean checkEqual(String a, String b) {
        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 收支行为 收支行为：agentProfit=>上级收益,deposit_return=>下庄退回,set_deposit=>上庄,
     * sendgift=>赠送礼物,sendbarrage=>弹幕,loginbonus=>登录奖励,buyvip=>购买VIP,buycar=>购买坐骑,
     * buyliang=>购买靓号,sharereward=>分享奖励,game_bet=>游戏下注,game_return=>游戏返还,game_win=>游戏获胜,
     * game_banker=>庄家收益,roomcharge=>房间扣费,timecharge=>计时扣费,sendred=>发送红包,robred=>抢红包,
     * buyguard=>开通守护,firstimage=>首次发布图文奖励,firstvedio=>首次发布视频奖励，viewimage=>观看图文收费，
     * viewvedio=>观看视频收费,completeinfo=>完善个人资料奖励，completeregister=>注册奖励 ,
     *
     * @param action
     * @return
     */
    public static String getActionName(String action) {
        String name = "";
        switch (action) {
            case "AgentProfit":
                name = "上级收益";
                break;
            case "deposit_return":
                name = "下庄退回";
                break;
            case "set_deposit":
                name = "上庄";
                break;
            case "sendgift":
                name = "赠送礼物";
                break;
            case "sendbarrage":
                name = "弹幕";
                break;
            case "loginbonus":
                name = "登录奖励";
                break;
            case "buyvip":
                name = "购买VIP";
                break;
            case "buycar":
                name = "购买坐骑";
                break;
            case "buyliang":
                name = "购买靓号";
                break;
            case "sharereward":
                name = "分享奖励";
                break;
            case "game_bet":
                name = "游戏下注";
                break;
            case "game_return":
                name = "游戏返还";
                break;
            case "game_win":
                name = "游戏获胜";
                break;
            case "game_banker":
                name = "庄家收益";
                break;
            case "roomcharge":
                name = "房间扣费";
                break;
            case "timecharge":
                name = "计时扣费";
                break;
            case "sendred":
                name = "发送红包";
                break;
            case "robred":
                name = "抢红包";
                break;
            case "buyguard":
                name = "开通守护";
                break;
            case "firstimage":
                name = "首次发布图文奖励";
                break;
            case "firstvedio":
                name = "首次发布视频奖励";
                break;
            case "viewimage":
                name = "观看图文收费";
                break;
            case "viewvedio":
                name = "观看视频收费";
                break;
            case "completeinfo":
                name = "完善个人资料奖励";
                break;
            case "completeregister":
                name = "注册奖励";
                break;
        }
        return name;
    }

}
