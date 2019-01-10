package com.xingqiuzhibo.phonelive.activity;

import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.LvShareHisAdapter;
import com.xingqiuzhibo.phonelive.bean.ShareListBean;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/8/008.
 */

public class ShareListActivity extends AbsActivity {
    private ListView lvHis;
    private TextView tvCount;
    private List<ShareListBean> data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_list;
    }

    @Override
    protected void main() {
        setTitle("邀请成员");
        lvHis = findViewById(R.id.lv_share_his);
        tvCount = findViewById(R.id.tv_total_count);
        loadData();
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("g", "Appapi");
        map.put("m", "Agent");
        map.put("a", "agentSubordinate");
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        NetWork.httpParamGet(UrlUtil.SHARE_TASK_MSG, map, this, request);
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            Integer code = jsb.getInteger("code");
            if (code == 0) {
                JSONObject dataJson = jsb.getJSONObject("info");
                Integer count = dataJson.getInteger("count");
                tvCount.setText("共" + count + "人");

                JSONArray arr = dataJson.getJSONArray("list");
                data = JSON.parseArray(JSON.toJSONString(arr), ShareListBean.class);

                lvHis.setAdapter(new LvShareHisAdapter(ShareListActivity.this, data));
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}
