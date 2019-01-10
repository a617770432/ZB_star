package com.xingqiuzhibo.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.CommonCallback;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import static android.text.TextUtils.isEmpty;

/**
 * Created by hx
 * Time 2018/12/29/029.
 */

public class LoginWayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login_way);
        AbsActivity.acts.add(LoginWayActivity.this);
        LinearLayout loginWay = findViewById(R.id.login_way);
        loginWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手机注册账号登录
                Intent intent = new Intent(LoginWayActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout loginVisitor = findViewById(R.id.login_visitor);
        loginVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //游客登录
                HttpUtil.visitorLogin(getMAC_IP(), new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        onLoginSuccess(code, msg, info);
                    }
                });
            }
        });

        findViewById(R.id.login_by_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账号注册账号登录
                Intent intent = new Intent(LoginWayActivity.this, LoginByAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getMAC_IP() {
        //wifi mac地址
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if (!isEmpty(wifiMac)) {
            Log.e("mac地址 : ", wifiMac);
            return wifiMac;
        }
        return null;
    }

    /**
     * 设置透明状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            System.exit(0);
        return false;
    }

    //登录成功！
    private void onLoginSuccess(int code, String msg, String[] info) {
        if (code == 0 && info.length > 0) {
            JSONObject obj = JSON.parseObject(info[0]);
            String uid = obj.getString("id");
            String token = obj.getString("token");
            AppConfig.getInstance().setLoginInfo(uid, token, true);
            getBaseUserInfo();
            //友盟统计登录
            MobclickAgent.onProfileSignIn("visitor", uid);
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
                MainActivity.forward(LoginWayActivity.this);
                finish();
            }
        });
    }

}
