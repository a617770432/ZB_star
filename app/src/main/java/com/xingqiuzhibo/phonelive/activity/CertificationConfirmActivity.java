package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.view.View;

import com.xingqiuzhibo.phonelive.R;

/**
 * Created by hx
 * Time 2019/1/7/007.
 */

public class CertificationConfirmActivity extends AbsActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_confirm;
    }

    @Override
    protected void main() {
        setTitle("个人认证");
    }
}
