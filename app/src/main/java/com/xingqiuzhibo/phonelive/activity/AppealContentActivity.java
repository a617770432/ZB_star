package com.xingqiuzhibo.phonelive.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.xingqiuzhibo.phonelive.R;

/**
 * Created by hx
 * Time 2018/12/28/028.
 * 申诉文件上传
 */

public class AppealContentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_appeal_content);
    }
}
