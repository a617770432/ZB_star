package com.xingqiuzhibo.phonelive.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.QuestionBean;
import com.xingqiuzhibo.phonelive.bean.QuestionParamBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;
import com.xingqiuzhibo.phonelive.utils.WheelViewUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/10/010.
 */

public class FindAccountPwdActivity extends AbsActivity {
    private ArrayList<String> strings = new ArrayList<>();
    private List<QuestionBean> data = new ArrayList<>();
    private EditText etAccount, etAnswer, etPwd;
    private TextView tvQuestion;
    private Integer qPosition = null;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_account_pwd;
    }

    @Override
    protected void main() {
        etAccount = findViewById(R.id.edit_account);
        etAnswer = findViewById(R.id.et_answer2);
        etPwd = findViewById(R.id.edit_change_pwd);
        tvQuestion = findViewById(R.id.tv_question);

        //返回箭头点击事件
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选择密保问题点击事件
        findViewById(R.id.layout_q).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog("请选择您设置的密保问题", data);
            }
        });


        findViewById(R.id.tv_update_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String answer = etAnswer.getText().toString();
                String pwd = etPwd.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    etAccount.setError(WordUtil.getString(R.string.login_input_account));
                    etAccount.requestFocus();
                    return;
                }
                if (qPosition == null || TextUtils.isEmpty(answer)) {
                    ToastUtil.show("请选择密保问题或输入密保问题答案");
                    return;
                }
                if (!StringUtil.checkPassword(pwd)) {
                    ToastUtil.show("请输入合理密码");
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("user_login", account);
                map.put("user_pass", pwd);
                List<QuestionParamBean> list = new ArrayList<>();
                list.add(new QuestionParamBean(data.get(qPosition).getId(), data.get(qPosition).getTitle(), etAnswer.getText().toString()));
                map.put("secret_questions", list);
                String params = JSON.toJSON(map).toString();
                NetWork.httpPost(UrlUtil.FIND_PWD_UPDATE, params, null, request);

            }
        });

        loadData();
    }

    private void showQuestionDialog(final String title, List<QuestionBean> items) {
        strings.clear();
        for (int i = 0; i < data.size(); i++) {
            strings.add(data.get(i).getTitle());
        }

        WheelViewUtil.alertBottomWheelOption(FindAccountPwdActivity.this, title, strings, new WheelViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                qPosition = postion;
                tvQuestion.setText(strings.get(postion));
            }
        });

    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            Log.e(">>>>>>>>>>>", responseInfo.result);
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                ToastUtil.show("密码设置成功！");
                finish();
            } else {
                ToastUtil.show(jsb.getString("msg"));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void loadData() {
        NetWork.httpParamGet(UrlUtil.REGISTER_PROTECT_QUESTION, null, null, questionRequest);
    }

    RequestCallBack<String> questionRequest = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONArray arr = jsb.getJSONArray("info");
                data.clear();
                data = JSON.parseArray(JSON.toJSONString(arr), QuestionBean.class);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

}
