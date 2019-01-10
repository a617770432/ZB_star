package com.xingqiuzhibo.phonelive.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.TaskAdapter;
import com.xingqiuzhibo.phonelive.bean.ShareTaskBean;
import com.xingqiuzhibo.phonelive.bean.TaskMsgBean;
import com.xingqiuzhibo.phonelive.glide.ImgLoader;
import com.xingqiuzhibo.phonelive.http.NetWork;
import com.xingqiuzhibo.phonelive.utils.SystemUtils;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;
import com.xingqiuzhibo.phonelive.utils.WordUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hx
 * Time 2019/1/8/008.
 * 我的分销界面
 */

public class MyShareActivity extends AbsActivity implements View.OnClickListener {

    private ImageView imgCode;
    private TextView tvCode, saveQrCode, copyCode, totalCoin, toCoinList, shareNum, shareHis;
    private TextView taskName1, taskName2, taskName3;
    private ListView lvTask1, lvTask2, lvTask3;
    private List<TaskMsgBean> taskList1, taskList2, taskList3;
    private String codeUrl, inviteCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_share;
    }

    @Override
    protected void main() {
        setTitle("分销任务");
        imgCode = findViewById(R.id.img_share_qr_code);
        tvCode = findViewById(R.id.tv_share_code);
        saveQrCode = findViewById(R.id.tv_save_img_code);
        copyCode = findViewById(R.id.tv_copy_text_code);
        toCoinList = findViewById(R.id.tv_to_coin_list);
        totalCoin = findViewById(R.id.tv_total_coin_income);
        shareNum = findViewById(R.id.tv_share_num);
        lvTask1 = findViewById(R.id.lv_share_task);
        lvTask2 = findViewById(R.id.lv_new_task);
        lvTask3 = findViewById(R.id.lv_msg_task);
        shareHis = findViewById(R.id.tv_to_share_his);
        event();
        loadData();
    }

    private void event() {
        saveQrCode.setOnClickListener(this);
        copyCode.setOnClickListener(this);
        shareHis.setOnClickListener(this);
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("g", "Appapi");
        map.put("m", "Agent");
        map.put("a", "getDistributionInfo");
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        NetWork.httpParamGet(UrlUtil.SHARE_TASK_MSG, map, this, request);
    }

    RequestCallBack<String> request = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            JSONObject jsb = JSON.parseObject(responseInfo.result);
            JSONObject dataJson = jsb.getJSONObject("info");
            ShareTaskBean infoBean = JSON.parseObject(JSON.toJSONString(dataJson), ShareTaskBean.class);
            //设置基础信息
            setTaskMsg(infoBean);

            JSONObject lisJson = dataJson.getJSONObject("task_info");
            JSONArray arr1 = lisJson.getJSONArray("spread_list");
            JSONArray arr2 = lisJson.getJSONArray("noviciate_list");
            JSONArray arr3 = lisJson.getJSONArray("release_list");

            taskList1 = JSON.parseArray(JSON.toJSONString(arr1), TaskMsgBean.class);
            lvTask1.setAdapter(new TaskAdapter(MyShareActivity.this, taskList1));
            setListViewHeightBasedOnChildren(lvTask1);
            taskList2 = JSON.parseArray(JSON.toJSONString(arr2), TaskMsgBean.class);
            lvTask2.setAdapter(new TaskAdapter(MyShareActivity.this, taskList2));
            setListViewHeightBasedOnChildren(lvTask2);
            taskList3 = JSON.parseArray(JSON.toJSONString(arr3), TaskMsgBean.class);
            lvTask3.setAdapter(new TaskAdapter(MyShareActivity.this, taskList3));
            setListViewHeightBasedOnChildren(lvTask3);


        }


        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void setTaskMsg(ShareTaskBean bean) {
        tvCode.setText(bean.getCode());
        totalCoin.setText(bean.getOne_profit());
        shareNum.setText(bean.getCount());
        codeUrl = bean.getShare_url();
        inviteCode = bean.getCode();
        ImgLoader.display(bean.getQrcode_url(), imgCode);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;

        listView.setLayoutParams(params);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_img_code:
                //保存二维码图片
                saveQrcodeToGallery();
                break;
            case R.id.tv_copy_text_code:
                //复制地址
                WordUtil.CopyToClipboard(this, codeUrl);
                ToastUtil.show("已成功复制链接到剪切板");
                break;
            case R.id.tv_to_share_his:
                //分享历史
                Intent intent = new Intent(this, ShareListActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void saveQrcodeToGallery() {
        //创建视图
        View qrcodeView = getLayoutInflater().inflate(R.layout.qrcode_page, null, false);
        ((ImageView) qrcodeView.findViewById(R.id.img_qr_code)).setImageDrawable(imgCode.getDrawable());
        TextView tv = qrcodeView.findViewById(R.id.tv_invite_code);
        tv.setText(inviteCode);

        //计算视图大小
        DisplayMetrics displayMetrics = SystemUtils.getWindowDisplayMetrics(mContext);
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels - SystemUtils.getStatusBarHeight(mContext) - SystemUtils.getActionBarHeight(mContext) - getResources().getDimensionPixelSize(R.dimen.default_bottom_bar_height);
        //将视图生成图片
        Bitmap image = SystemUtils.generateImageFromView(qrcodeView, width, height);
        //将图片保存到系统相册
        boolean isSuccess = SystemUtils.saveImageToGallery(MyShareActivity.this, image);
        image.recycle();
        if (isSuccess) {
            ToastUtil.show("已保存到系统相册！");
        } else {
            ToastUtil.show("保存到系统相册失败！");
        }
    }
}
