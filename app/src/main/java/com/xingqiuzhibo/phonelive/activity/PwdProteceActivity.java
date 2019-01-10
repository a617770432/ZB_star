package com.xingqiuzhibo.phonelive.activity;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.QuestionBean;
import com.xingqiuzhibo.phonelive.bean.QuestionParamBean;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.im.ImPushUtil;
import com.xingqiuzhibo.phonelive.interfaces.CommonCallback;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;
import com.xingqiuzhibo.phonelive.utils.WheelViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class PwdProteceActivity extends AbsActivity {
    private EditText answer1, answer2;
    private String mAccount, mPwd, mPhone, mCode;
    private TextView tvQ1, tvQ2;
    private ArrayList<String> strings = new ArrayList<>();
    private List<QuestionBean> data = new ArrayList<>();
    private int q1 = 0, q2 = 1;
    private int mFirstLogin;//是否是第一次登录

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pwd_protect;
    }

    @Override
    protected void main() {
        acts.add(this);
        mAccount = getIntent().getStringExtra("m_account");
        mPwd = getIntent().getStringExtra("m_password");
        mPhone = getIntent().getStringExtra("m_phone");
        mCode = getIntent().getStringExtra("m_code");

        answer1 = findViewById(R.id.edit_answer_1);
        answer2 = findViewById(R.id.edit_answer_2);

        tvQ1 = findViewById(R.id.tv_show_question_1);
        tvQ2 = findViewById(R.id.tv_show_question_2);

        findViewById(R.id.layout_q_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密保问题一
                showDialog("问题一", data, 1);
            }
        });
        findViewById(R.id.layout_q_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密保问题二
                showDialog("问题二", data, 2);
            }
        });
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回点击事件
                finish();
            }
        });
        findViewById(R.id.tv_register_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提价注册信息
                doRegister();
            }
        });
        loadData();
    }

    private void doRegister() {
        if (TextUtils.isEmpty(answer1.getText().toString()) && TextUtils.isEmpty(answer2.getText().toString())) {
            ToastUtil.show("请设置密保问题");
            return;
        }
        List<QuestionParamBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(answer1.getText().toString()))
            list.add(new QuestionParamBean(data.get(q1).getId(), data.get(q1).getTitle(), answer1.getText().toString()));
        if (!TextUtils.isEmpty(answer2.getText().toString()))
            list.add(new QuestionParamBean(data.get(q2).getId(), data.get(q2).getTitle(), answer2.getText().toString()));
        Map<String, Object> map = new HashMap<>();
        map.put("user_login", mAccount);
        map.put("user_pass", mPwd);
        map.put("secret_questions", list);

        if (null != mPhone)
            map.put("mobile", mPhone);
        if (null != mCode)
            map.put("codes", mCode);
        map.put("source", "account");
        map.put("pushid", ImPushUtil.getInstance().getPushID());

        String jsonStr = JSON.toJSON(map).toString();
        Log.e("------------------>", jsonStr);

        NetWork.httpPost(UrlUtil.REGISTER_MSG_UPDATE, jsonStr, null, registerRequest);
    }

    private void loadData() {
        NetWork.httpParamGet(UrlUtil.REGISTER_PROTECT_QUESTION, null, null, request);
    }

    private void showDialog(final String title, List<QuestionBean> items, final int a) {
        strings.clear();
        for (int i = 0; i < data.size(); i++) {
            strings.add(data.get(i).getTitle());
        }

        WheelViewUtil.alertBottomWheelOption(PwdProteceActivity.this, title, strings, new WheelViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                if (a == 1) {
                    q1 = postion;
                    tvQ1.setText("问题一：" + strings.get(postion));
                } else {
                    q2 = postion;
                    tvQ2.setText("问题二：" + strings.get(postion));
                }
            }
        });

    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Log.e("----------->", responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONArray arr = jsb.getJSONArray("info");
                data.clear();
                data = JSON.parseArray(JSON.toJSONString(arr), QuestionBean.class);
                tvQ1.setText("问题一：" + data.get(q1).getTitle());
                if (data.size() > 1)
                    tvQ2.setText("问题二：" + data.get(q2).getTitle());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    RequestCallBack<String> registerRequest = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                HttpUtil.login(mAccount, mPwd, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        onLoginSuccess(code, msg, info);
                    }
                });
            } else {
                ToastUtil.show(jsb.getString("msg"));
            }


        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

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
            MobclickAgent.onProfileSignIn("account", uid);
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
                for (Activity act : acts) {
                    act.finish();
                }
            }
        });
    }
}
