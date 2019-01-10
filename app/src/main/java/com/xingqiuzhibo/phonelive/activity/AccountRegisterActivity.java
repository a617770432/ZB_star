package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class AccountRegisterActivity extends AbsActivity {

    private EditText etAccount, etPwd, etPhone, etCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_register;
    }

    @Override
    protected void main() {
        acts.add(this);
        etAccount = findViewById(R.id.edit_account);
        etPwd = findViewById(R.id.edit_account_pwd);
        etPhone = findViewById(R.id.edit_account_phone);
        etCode = findViewById(R.id.edit_account_invite_code);

        findViewById(R.id.tv_register_next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //下一步按钮点击事件
    private void nextStep() {
        //输入内容验证
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
        String phone = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            if (!StringUtil.checkPhoneNumber(phone)) {
                ToastUtil.show("请输入合理手机号！");
                return;
            }
        }
        String code = etCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            if (!StringUtil.checkCodeNumber(code)) {
                ToastUtil.show("请输入正确邀请码！");
                return;
            }
        }

        Intent intent = new Intent(AccountRegisterActivity.this, PwdProteceActivity.class);
        intent.putExtra("m_account", account);
        intent.putExtra("m_password", pwd);
        intent.putExtra("m_phone", phone);
        intent.putExtra("m_code", code);
        startActivity(intent);
    }
}
