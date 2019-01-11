package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.ShowPictureAdapter;
import com.xingqiuzhibo.phonelive.bean.RangeBean;
import com.xingqiuzhibo.phonelive.bean.TermInfoEntity;
import com.xingqiuzhibo.phonelive.custom.ScrollGridLayoutManager;
import com.xingqiuzhibo.phonelive.fragment.SelectPhotoDialogFragment;
import com.xingqiuzhibo.phonelive.utils.BitmapUtils;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.FileUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishTieZiActivity extends AbsActivity implements SelectPhotoDialogFragment.ImageCropListener
        , ShowPictureAdapter.OnItemClickListener {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_video)
    EditText etVideo;
    @BindView(R.id.iv_video_pic)
    AppCompatImageView ivVideoPic;
    @BindView(R.id.rl_video_pic)
    RelativeLayout rlVideoPic;
    @BindView(R.id.iv_video)
    AppCompatImageView ivVideo;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.rl_video_bg)
    RelativeLayout rlVideoBg;
    @BindView(R.id.et_pic_content)
    EditText etPicContent;
    @BindView(R.id.pic_recycler)
    RecyclerView picRecycler;
    @BindView(R.id.rl_pic_text)
    RelativeLayout rlPicText;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_range)
    TextView tvRange;
    @BindView(R.id.tv_kind)
    TextView tvKind;
    @BindView(R.id.iv_tag1)
    AppCompatImageView ivTag1;
    @BindView(R.id.iv_tag2)
    AppCompatImageView ivTag2;
    @BindView(R.id.rl_kind)
    RelativeLayout rlKind;

    public final static int VEDIO_KU = 101;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private String path = "";//文件路径

    private Unbinder unbinder;

    private int openType = -1;// 0  1  2

    private List<String> listShow = new ArrayList<>();
    private ShowPictureAdapter showAdapter;
    private SelectPhotoDialogFragment selectPhotoDialogFragment;//..拍照或者选择图片的fragment
    public final static int TYPE_SHOW = 0;
    //调用照相机返回图片文件
    private File tempFile;

    //上传视频是的视频封面图片
    private String videoPic = "";
    private boolean isVideoClick = false;//是否为视频封面图点击
    private String videoPath = "";//视频地址

    //
    private OptionsPickerView pvOptions;
    List<String> optionsItems1 = new ArrayList<>(); //花费钻石
    List<String> optionsItems2 = new ArrayList<>(); //发布范围
    List<String> optionsItems3 = new ArrayList<>(); //发布栏目

    //一二级栏目的集合
    private List<RangeBean> rangeBeans = new ArrayList<>();

    private int onePosition;//钻石列表选中的下标
    private int secondPosition;//发布范围选中的下标
    private int thirdPosition;//发布栏目选中的下标

    //花费的钻石
    private String cost = "0";

    //加载dialog
    private Dialog dialog;

    //上传服务器的实体类
    private TermInfoEntity entity = new TermInfoEntity();

    //图文时的图片集合
    private List<String> imgList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_tie_zi;
    }

    @Override
    protected void main() {
        super.main();

        unbinder = ButterKnife.bind(this);

        dialog = DialogUitl.loadingDialog(this);

        openType = getIntent().getIntExtra("openType", -1);

        switch (openType) {
            case 0:
                setTitle("文章");
                etContent.setVisibility(View.VISIBLE);
                rlVideoBg.setVisibility(View.GONE);
                rlPicText.setVisibility(View.GONE);
                rlTitle.setVisibility(View.VISIBLE);
                break;
            case 1:
                setTitle("图文");
                rlPicText.setVisibility(View.VISIBLE);
                rlVideoBg.setVisibility(View.GONE);
                etContent.setVisibility(View.GONE);
                break;
            case 2:
                setTitle("视频");
                rlVideoBg.setVisibility(View.VISIBLE);
                rlPicText.setVisibility(View.GONE);
                etContent.setVisibility(View.GONE);
                break;
        }


        selectPhotoDialogFragment = new SelectPhotoDialogFragment();
        selectPhotoDialogFragment.setImagePathListener(this);
        listShow.add("");
        ScrollGridLayoutManager layoutManager1 = new ScrollGridLayoutManager(this, 4);
        layoutManager1.setScrollEnabled(false);
        picRecycler.setLayoutManager(layoutManager1);
        picRecycler.setItemAnimator(new DefaultItemAnimator());
        showAdapter = new ShowPictureAdapter(this, this, listShow);
        showAdapter.setSource(TYPE_SHOW);
        showAdapter.setOnItemClickListener(this);
        picRecycler.setAdapter(showAdapter);

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View view) {
                //返回的分别是三个级别的选中位置
                switch (view.getId()) {
                    case R.id.rl_cost:
                        onePosition = options1;
                        tvCost.setText(optionsItems1.get(options1) + " 钻石");
                        cost = optionsItems1.get(options1);
                        break;
                    case R.id.rl_range:
                        secondPosition = options1;
                        tvRange.setText(optionsItems2.get(options1));
                        if (rangeBeans.get(secondPosition).getChildren() == null || rangeBeans.get(secondPosition).getChildren().size() == 0) {
                            rlKind.setVisibility(View.GONE);
                        } else {
                            rlKind.setVisibility(View.VISIBLE);
                            for (int i = 0; i < rangeBeans.get(secondPosition).getChildren().size(); i++) {
                                optionsItems3.add(rangeBeans.get(secondPosition).getChildren().get(i).getRangeName());
                                tvKind.setText(optionsItems3.get(0));
                                thirdPosition = 0;
                            }
                        }
                        break;
                    case R.id.rl_kind:
                        thirdPosition = options1;
                        tvKind.setText(optionsItems3.get(options1));
                        break;
                }
            }
        })
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项
                .setCancelColor(Color.parseColor("#333333"))
                .setSubmitColor(Color.parseColor("#FFDD00"))
                .setTitleBgColor(getResources().getColor(R.color.color_white))//标题背景颜色 Night mode
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        getDiamond();
        getLanMuList();
    }

    private void getDiamond() {
        //获取钻石可选列表
        OkGo.<String>get(UrlUtil.DIAMOND_LIST)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String res = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Log.e("DIAMOND_LIST", jsonObject.toString());
                            if (jsonObject.optInt("code") == 0) {
                                JSONArray jsonArray = jsonObject.optJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    optionsItems1.add(jsonArray.get(i) + "");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getLanMuList() {
        //获取一二级栏目
        OkGo.<String>get(UrlUtil.RANGE_LIST)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String res = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Log.e("RANGE_LIST", jsonObject.toString());
                            if (jsonObject.optInt("code") == 0) {

                                rangeBeans.clear();

                                rangeBeans = new Gson().fromJson(jsonObject.optJSONArray("list").toString()
                                        , new TypeToken<List<RangeBean>>() {
                                        }.getType());

                                for (int i = 0; i < rangeBeans.size(); i++) {
                                    optionsItems2.add(rangeBeans.get(i).getRangeName());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void selectVideo() {
        // TODO 启动相册
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PublishTieZiActivity.VEDIO_KU);
    }

    /**
     * 选择回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            isVideoClick = false;
            return;
        }

        List<String> paths = new ArrayList<>();
        switch (requestCode) {
            // TODO 视频
            case PublishTieZiActivity.VEDIO_KU:
                Uri uri = data.getData();
                // 视频路径
                videoPath = FileUtil.getPath(PublishTieZiActivity.this, uri);
                // 视频大小
                long videoSize = 0;
                try {
                    videoSize = FileUtil.getFileSize(new File(videoPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("file_message", "videoPath== " + videoPath + " videoSize== " + videoSize);
                //转换文件大小类型
//                    if (videoSize > MAX_LENGTH) {
//                        showToast("大小超出限制，最大20MB");
//                        return;
//                    }
                // 通过视频路径获取bitmap
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                ivVideo.setImageBitmap(bitmap);
                ivTag2.setVisibility(View.GONE);
                uploadFile(2);
                break;
            // TODO 图片
            case Constants.REQUEST_CODE_FOR_SELECT_PHOTO_SHOW: {
                if (data == null) {
                    return;
                }

                if (isVideoClick) {
                    videoPic = data.getStringArrayListExtra("path").get(0);
                    Glide.with(this).load(videoPic).into(ivVideoPic);
                    ivTag1.setVisibility(View.GONE);
                    uploadFile(1);
                } else {
                    paths.addAll(data.getStringArrayListExtra("path"));
                    show(paths);
                }

                break;
            }
            case Constants.REQUEST_CODE_FOR_TAKE_PHOTO_SHOW: {

                if (isVideoClick) {
                    videoPic = BitmapUtils.compressImage(tempFile.toString());
                    Glide.with(this).load(videoPic).into(ivVideoPic);
                } else {
                    paths.add(BitmapUtils.compressImage(tempFile.toString()));
                    show(paths);
                }

                break;
            }
            case Constants.REQUEST_CODE_FOR_DELETE_PHOTO_SHOW: {
                if (data == null) {
                    return;
                }
                int position = data.getIntExtra("position", 1);
                showAdapter.notifyItemRemoved(position);
                listShow.remove(position);
                break;
            }
        }

    }

    private void show(List<String> paths) {
        int size = listShow.size();
        listShow.addAll(paths);
        showAdapter.notifyItemRangeInserted(size, listShow.size() - size);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.ll_video_pic, R.id.ll_video, R.id.rl_cost, R.id.rl_range, R.id.rl_kind, R.id.tv_publish, R.id.iv_video_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_video_pic:
            case R.id.iv_video_pic:
                //视频封面图

                selectPhotoDialogFragment.show(getSupportFragmentManager(), "1");
                selectPhotoDialogFragment.setPicSize(1);
                isVideoClick = true;

                break;
            case R.id.ll_video:
                //选择视频
                isVideoClick = false;
                selectVideo();
                break;
            case R.id.rl_cost:
                //花费钻石
                pvOptions.setTitleText("花费钻石");
                pvOptions.setPicker(optionsItems1);
                pvOptions.show(view);
                break;
            case R.id.rl_range:
                //发布范围
                pvOptions.setTitleText("发布范围");
                pvOptions.setPicker(optionsItems2);
                pvOptions.show(view);
                break;
            case R.id.rl_kind:
                //发布栏目
                pvOptions.setTitleText("发布栏目");
                pvOptions.setPicker(optionsItems3);
                pvOptions.show(view);
                break;
            case R.id.tv_publish:
                //发布
                judge();
                break;
        }
    }

    private void judge(){
        if(TextUtils.isEmpty(tvRange.getText().toString())){
            ToastUtil.show("发布范围不能为空");
            return;
        }

        switch (openType) {
            case 0:
//                        setTitle("文章");
                if(TextUtils.isEmpty(etTitle.getText().toString())){
                    ToastUtil.show("标题不能为空");
                    return;
                }
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    ToastUtil.show("内容不能为空");
                    return;
                }
                entity.setContent(etContent.getText().toString().trim());
                break;
            case 1:
//                        setTitle("图文");
                if(TextUtils.isEmpty(etPicContent.getText().toString())){
                    ToastUtil.show("内容不能为空");
                    return;
                }
                if( listShow.size() - 1 == 0 ){
                    ToastUtil.show("图片不能为空");
                    return;
                }
                uploadImages();
                break;
            case 2:
//                        setTitle("视频");
                if(TextUtils.isEmpty(etVideo.getText().toString())){
                    ToastUtil.show("内容不能为空");
                    return;
                }
                if(TextUtils.isEmpty(videoPic)){
                    ToastUtil.show("封面图片不能为空");
                    return;
                }
                if(TextUtils.isEmpty(videoPath)){
                    ToastUtil.show("视频文件不能为空");
                    return;
                }
                entity.setContent(etVideo.getText().toString().trim());
                entity.setVideo(videoPath);
                List<String> img = new ArrayList<String>();
                img.add(videoPic);
                entity.setImgList(img);
                break;
        }
        publish();
    }

    private void uploadImages(){
        //上传多张图片

        listShow.remove(0);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (int i = 0; i < listShow.size(); i++) {
            File file = new File(listShow.get(i));
            builder.addFormDataPart("fileList", file.getName(), RequestBody.create(null, file));
        }
        builder.addFormDataPart("type", "1");
        builder.addFormDataPart("userId", AppConfig.getInstance().getUid());
        RequestBody body = builder.build();
        OkGo.<String>post(UrlUtil.UPLOAD_IMAGE)
//        OkGo.<String>post("http://192.168.1.158:8585/xqpd/tdapp/base/file/uploadBatch")
                .tag(this)
                .upRequestBody(body)
                .params("type", 1)
                .params("userId", AppConfig.getInstance().getUid())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(response.body());
                            if (jsonObject.getInt("code") == 0) {
                                JSONArray jsonArray = jsonObject.optJSONArray("urlList");
                                for (int i = 0 ; i < jsonArray.length() ; i ++ ){
                                    imgList.add(jsonArray.get(i).toString());
                                }

                                entity.setContent(etPicContent.getText().toString().trim());
                                entity.setImgList(imgList);
                                publish();

                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }


    //上传视频
    private void uploadFile(final int type){

        dialog.show();

        File path;
        if(type == 1 ){
            path = new File(videoPic);
        }else {
            path = new File(videoPath);
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("type", type+"");
        builder.addFormDataPart("userId", AppConfig.getInstance().getUid());
        builder.addFormDataPart("file", path.getName(), RequestBody.create(null, path));
        RequestBody body = builder.build();

        OkGo.<String>post(UrlUtil.UPLOAD_FILE)
//        OkGo.<String>post("http://192.168.1.158:8585/xqpd/tdapp/base/file/upload")
                .tag(this)
                .upRequestBody(body)
                .params("type", type)
                .params("userId", AppConfig.getInstance().getUid())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(response.body());
                            if (jsonObject.getInt("code") != 0) {

                                if(type == 1){
                                    ToastUtil.show("团片上传失败！");
                                }else {
                                    ToastUtil.show("视频上传失败！");
                                }
                                return;
                            }
//                                            org.json.JSONObject data = jsonObject.optJSONObject("data");
                            String url = jsonObject.optString("url");
                            Log.e("视频提交返回数据：", url);

                            if(type == 1 ){
                                videoPic = url;
                            }else {
                                videoPath = url;
                            }

                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void publish(){
        //获取钻石可选列表

        dialog.show();

        entity.setUid(Long.parseLong(AppConfig.getInstance().getUid()));
        entity.setTitle(etTitle.getText().toString().trim());
        entity.setAmount(Integer.parseInt(cost));
        entity.setFiletype(openType);
        entity.setOneRid(rangeBeans.get(onePosition).getRid().longValue());
        entity.setOneRangeName(rangeBeans.get(onePosition).getRangeName());
        if(rangeBeans.get(secondPosition).getChildren() != null && rangeBeans.get(secondPosition).getChildren().size() > 0){
            entity.setTwoRid(rangeBeans.get(secondPosition).getChildren().get(thirdPosition).getRid().longValue());
            entity.setTwoRangeName(rangeBeans.get(secondPosition).getChildren().get(thirdPosition).getRangeName());
        }

        Log.e( "publish_json" , new Gson().toJson(entity));

        OkGo.<String>post(UrlUtil.PUBLISH)
//        OkGo.<String>post("http://192.168.1.158:8585/xqpd/tdapp/terminfo/save")
                .tag(this)
                .upJson(new Gson().toJson(entity))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        String res = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Log.e("PUBLISH", jsonObject.toString());

                            if(jsonObject.optInt("code") == 0){
                                ToastUtil.show("提交成功，请等待审核");
                                onBackPressed();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void setImagePath(File path) {
        this.tempFile = path;
    }

    @Override
    public void onItemClick(int position, int source) {
        if (position == 0) {
            if (listShow.size() == Constants.COUNT_MAX_SHOW_PICTURE) {
                ToastUtil.show("最多选取9张图片");
                return;
            }
            isVideoClick = false;
            selectPhotoDialogFragment.show(getSupportFragmentManager(), "1");
            selectPhotoDialogFragment.setPicSize(Constants.COUNT_MAX_SHOW_PICTURE - listShow.size());
        }
    }

    @Override
    public void onItemCancel(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除这张图片吗")
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listShow.remove(position);
                showAdapter.notifyDataSetChanged();
//                showAdapter.notifyItemRemoved(position);
//                listShow.remove(position);
            }
        }).create().show();
    }

}
