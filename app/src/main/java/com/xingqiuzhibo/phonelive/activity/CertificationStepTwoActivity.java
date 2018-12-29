package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.bean.UserBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.HttpCallback;
import com.xingqiuzhibo.phonelive.http.HttpUtil;
import com.xingqiuzhibo.phonelive.interfaces.ImageResultCallback;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.ProcessImageUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CertificationStepTwoActivity extends AbsActivity implements View.OnClickListener {

    private ProcessImageUtil mImageUtil;
    private AppCompatImageView ivZhengmian, ivBeimian, ivHand;
    private int clickType;
    private String zhengmianStr , beimianStr , handStr;
    private EditText etRealName , etPhone , etCard;
    private TextView tvRenzheng;

    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_step_two;
    }

    @Override
    protected void main() {

        setTitle("个人认证");

        dialog = DialogUitl.loadingDialog(this);

        ivZhengmian = findViewById(R.id.iv_zhengmian);
        ivBeimian = findViewById(R.id.iv_beimian);
        ivHand = findViewById(R.id.iv_hand);
        etRealName = findViewById(R.id.et_real_name);
        etCard = findViewById(R.id.et_id_card);
        etPhone = findViewById(R.id.et_phone);
        tvRenzheng = findViewById(R.id.tv_renzheng);
        tvRenzheng.setOnClickListener(this);
        ivZhengmian.setOnClickListener(this);
        ivBeimian.setOnClickListener(this);
        ivHand.setOnClickListener(this);

        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    if(clickType == 0){
                        ImgLoader.display(file, ivZhengmian);
                    }else if (clickType == 1){
                        ImgLoader.display(file, ivBeimian);
                    }else if (clickType == 2){
                        ImgLoader.display(file, ivHand);
                    }

                    dialog.show();
                    OkGo.<String>post("http://www.xingqiupindao.com/index.php?g=Appapi&m=Auth&a=upload")
                            .tag(this)
                            .params("file" , file)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    try {
                                        org.json.JSONObject jsonObject = new org.json.JSONObject(response.body());
                                        org.json.JSONObject data = jsonObject.optJSONObject("data");
                                        String url = data.optString("url");

                                        if(clickType == 0){
                                            zhengmianStr = url;
                                        }else if (clickType == 1){
                                            beimianStr = url;
                                        }else if (clickType == 2){
                                            handStr = url;
                                        }

                                        dialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_zhengmian:
                clickType = 0 ;
                break;
            case R.id.iv_beimian:
                clickType = 1 ;
                break;
            case R.id.iv_hand:
                clickType = 2 ;
                break;
            case R.id.tv_renzheng:
                if(TextUtils.isEmpty(etRealName.getText().toString())){
                    Toast.makeText(this , "真实姓名不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etPhone.getText().toString())){
                    Toast.makeText(this , "电话号码不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etCard.getText().toString())){
                    Toast.makeText(this , "身份证号不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(zhengmianStr)){
                    Toast.makeText(this , "证件正面照片不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(beimianStr)){
                    Toast.makeText(this , "证件背面照片不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(handStr)){
                    Toast.makeText(this , "手持证件正面照片不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();

                String uid = AppConfig.getInstance().getUid();
                OkGo.<String>get("http://www.xingqiupindao.com/index.php?g=Appapi&m=Auth&a=authsave")
                        .tag(this)
                        .params("uid" , uid)
                        .params("token" , AppConfig.getInstance().getToken())
                        .params("real_name" , etRealName.getText().toString().trim())
                        .params("mobile" , etPhone.getText().toString().trim())
                        .params("cer_no" , etCard.getText().toString().trim())
                        .params("front_view" , zhengmianStr)
                        .params("back_view" , beimianStr)
                        .params("handset_view" , handStr)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                dialog.dismiss();
                                String str = response.body();
                                try {
                                    org.json.JSONObject jsonObject = new org.json.JSONObject(str);
                                    Integer ret = jsonObject.optInt("ret");
                                    if(ret == 200){
                                        Toast.makeText(CertificationStepTwoActivity.this , "提交成功，请耐心等待审核" , Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CertificationStepTwoActivity.this , MainActivity.class);
                                        startActivity(intent);
                                        onBackPressed();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                return;
        }
        editAvatar();
    }
}
