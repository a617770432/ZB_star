package com.xingqiuzhibo.phonelive.activity;

import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/5/005.
 */

public class CertificationSuccessActivity extends AbsActivity {
    TextView tvInfo, tvID;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_success;
    }

    @Override
    protected void main() {
        tvInfo = findViewById(R.id.tv_personal_info);
        tvID = findViewById(R.id.tv_id_no);
        setTitle("认证成功");
        Map<String, Object> map = new HashMap<>();
        map.put("id", AppConfig.getInstance().getUid());
        NetWork.httpParamGet(UrlUtil.SELECT_PERSONAL_INFO + "/" + AppConfig.getInstance().getUid(), null, this, request);
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                Log.e("TAG", responseInfo.result);
                JSONObject dataJson = jsb.getJSONObject("info");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String time = sdf.format(dataJson.getLong("uptime"));
                tvInfo.setText("姓名\t" + dataJson.getString("realName") + "\n手机\t" + dataJson.getString("mobile")
                        + "\n日期\t" + time);
                tvID.setText("证件号码\t" + dataJson.getString("cerNo"));
            }


        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}
