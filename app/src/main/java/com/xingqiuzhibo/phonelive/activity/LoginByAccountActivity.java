package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.AppContext;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.CommonCallback;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class LoginByAccountActivity extends AbsActivity {
    private EditText etAccount, etPwd;
    private String mLoginType = "account";//登录方式
    private int mFirstLogin;//是否是第一次登录

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_by_account;
    }

    @Override
    protected void main() {
        acts.add(this);
        etAccount = findViewById(R.id.edit_account);
        etPwd = findViewById(R.id.edit_account_pwd);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击返回箭头关闭当前窗口
                finish();
            }
        });
        findViewById(R.id.tv_login_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击“开启您的直播”登录
                login();
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击注册按钮
                register();
            }
        });
        findViewById(R.id.btn_forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击忘记密码
                forgetPwd();
            }
        });

    }

    public static void forward() {
        Intent intent = new Intent(AppContext.sInstance, LoginByAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AppContext.sInstance.startActivity(intent);
    }

    //注册
    private void register() {
        startActivity(new Intent(mContext, AccountRegisterActivity.class));
    }

    //忘记密码
    private void forgetPwd() {
        //
        startActivity(new Intent(mContext, FindAccountPwdActivity.class));
    }


    //账号密码登录
    private void login() {
        String account = etAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            etAccount.setError(WordUtil.getString(R.string.login_input_account));
            etAccount.requestFocus();
            return;
        }
        if (etAccount.length() < 6) {
            etAccount.setError(WordUtil.getString(R.string.login_account_error));
            etAccount.requestFocus();
            return;
        }
        String pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            etPwd.setError(WordUtil.getString(R.string.login_input_pwd));
            etPwd.requestFocus();
            return;
        }
        HttpUtil.login(account, pwd, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                onLoginSuccess(code, msg, info);
            }
        });
    }

    //登录成功！
    private void onLoginSuccess(int code, String msg, String[] info) {
        if (code == 0 && info.length > 0) {
            JSONObject obj = JSON.parseObject(info[0]);
            String uid = obj.getString("id");
            String token = obj.getString("token");
            mFirstLogin = obj.getIntValue("isreg");
            AppConfig.getInstance().setLoginInfo(uid, token, true);
            getBaseUserInfo();
            //友盟统计登录
            MobclickAgent.onProfileSignIn(mLoginType, uid);
        } else {
            ToastUtil.show(msg);
        }
    }

    /**
     * 获取用户信息
     */
    private void getBaseUserInfo() {
        HttpUtil.getBaseInfo(new CommonCallback<UserBean>() {
            @Override
            public void callback(UserBean bean) {
                if (mFirstLogin == 1) {
                    RecommendActivity.forward(mContext);
                } else {
                    MainActivity.forward(mContext);
                }
                finish();
            }
        });
    }
}
