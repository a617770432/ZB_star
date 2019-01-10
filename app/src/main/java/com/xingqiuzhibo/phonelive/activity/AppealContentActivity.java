package com.xingqiuzhibo.phonelive.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.interfaces.ImageResultCallback;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.ProcessImageUtil;
import com.xingqiuzhibo.phonelive.utils.StringUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx
 * Time 2018/12/28/028.
 * 申诉文件上传
 */

public class AppealContentActivity extends AbsActivity implements View.OnClickListener {
    private ImageView img1, img2, img3;
    private TextView tv1, tv2, tv3;
    private ProcessImageUtil mImageUtil;
    private Dialog dialog;
    private Integer clickType = 0;
    private String picurl1, picurl2, picurl3;
    private EditText etInput;
    private Integer orderId;

//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
//        setContentView(R.layout.activity_appeal_content);
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appeal_content;
    }

    @Override
    protected void main() {
        acts.add(this);
        orderId = getIntent().getIntExtra("order_id", -1);
        dialog = DialogUitl.loadingDialog(this);
        img1 = findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = findViewById(R.id.img_3);

        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);

        etInput = findViewById(R.id.et_input);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    if (clickType == 0) {
                        tv1.setVisibility(View.GONE);
                        img1.setVisibility(View.VISIBLE);
                        ImgLoader.display(file, img1);
                    } else if (clickType == 1) {
                        tv2.setVisibility(View.GONE);
                        img2.setVisibility(View.VISIBLE);
                        ImgLoader.display(file, img2);
                    } else if (clickType == 2) {
                        tv3.setVisibility(View.GONE);
                        img3.setVisibility(View.VISIBLE);
                        ImgLoader.display(file, img2);
                    }

                    dialog.show();
                    OkGo.<String>post(AppConfig.BASE_URL + "base/file/upload")
                            .tag(this)
                            .params("file", file)
                            .params("type", 1)
                            .params("userId", AppConfig.getInstance().getUid())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    try {
                                        org.json.JSONObject jsonObject = new org.json.JSONObject(response.body());
                                        if (jsonObject.getInt("code") != 0) {
                                            ToastUtil.show("图片上传失败！");
                                            return;
                                        }
//                                            org.json.JSONObject data = jsonObject.optJSONObject("data");
                                        String url = jsonObject.optString("url");
                                        Log.e("图片提交返回数据：", url);
                                        if (clickType == 0) {
                                            picurl1 = url;
                                        } else if (clickType == 1) {
                                            picurl2 = url;
                                        } else if (clickType == 2) {
                                            picurl3 = url;
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

        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etInput.getText())) {
                    ToastUtil.show("请详细说明申诉原因！");
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                if (null != picurl1)
                    map.put("imgPath1", picurl1);
                if (null != picurl2)
                    map.put("imgPath2", picurl2);
                if (null != picurl3)
                    map.put("imgPath3", picurl3);
                map.put("uid", AppConfig.getInstance().getUid());
                if (orderId == -1)
                    return;
                map.put("chargeId", String.valueOf(orderId));
                map.put("content", etInput.getText().toString());
                NetWork.httpPost(UrlUtil.APPEAL_MSG_UPADTE, map, AppealContentActivity.this, request);
            }
        });

        findViewById(R.id.tv_appeal_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppealContentActivity.this, AppealHisActivity.class));
            }
        });

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                clickType = 0;
                editAvatar();
                break;
            case R.id.tv_2:
                clickType = 1;
                editAvatar();
                break;
            case R.id.tv_3:
                clickType = 2;
                editAvatar();
                break;
        }
    }


    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                ToastUtil.show("信息提交成功！");
                for (Activity act : acts) {
                    act.finish();
                }
            } else {
                ToastUtil.show(jsb.getString("msg"));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

}
