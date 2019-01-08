package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;

/**
 * Created by hx
 * Time 2019/1/5/005.
 * 主播身份认证失败界面
 */

public class CertificationFailActivity extends AbsActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_fail;
    }

    @Override
    protected void main() {
        setTitle("认证结果");
        TextView tv = findViewById(R.id.tv_authentication_again);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificationFailActivity.this, CertificationStepTwoActivity.class);
                startActivity(intent);
            }
        });
    }
}
