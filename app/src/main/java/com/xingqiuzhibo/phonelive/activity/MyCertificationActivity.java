package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

public class MyCertificationActivity extends AbsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_certification;
    }

    @Override
    protected void main() {
        setTitle("个人认证");

        findViewById(R.id.tv_renzheng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCertificationActivity.this , CertificationStepTwoActivity.class);
                startActivity(intent);
            }
        });

    }
}
