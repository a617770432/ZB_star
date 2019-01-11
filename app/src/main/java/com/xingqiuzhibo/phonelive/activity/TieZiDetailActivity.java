package com.xingqiuzhibo.phonelive.activity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.DialogCommentsAdapter;
import com.xingqiuzhibo.phonelive.adapter.TieZiAdapter;
import com.xingqiuzhibo.phonelive.bean.CommunityCommentEntity;
import com.xingqiuzhibo.phonelive.bean.ReportRequestBean;
import com.xingqiuzhibo.phonelive.bean.TermInfoEntity;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.fragment.CommentsFragment;
import com.xingqiuzhibo.phonelive.fragment.ReportFragment;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.GlideSimpleLoader;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TieZiDetailActivity extends AbsActivity implements ReportFragment.ClickListener,BaseQuickAdapter.OnItemChildClickListener
        , MessagePicturesLayout.Callback , MessagePicturesLayout.ImageCallback, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.top_recycler)
    RecyclerView topRecycler;
    @BindView(R.id.tv_comments_count)
    TextView tvCommentsCount;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_no_comment)
    TextView tvNoComment;

    private List<TermInfoEntity> list = new ArrayList<>();//数据源
    private TieZiAdapter adapter;

    //图片点击放大
    private ImageWatcherHelper iwHelper;

    //评论数据源
    private List<CommunityCommentEntity> commentEntityList = new ArrayList<>();
    private DialogCommentsAdapter dialogCommentsAdapter;
    private List<CommunityCommentEntity> dataList = new ArrayList<>();//接口得到的数据

    private int page = 1;
    private int pageSize = 15;
    private int totalPage = 0;

    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tie_zi_detail;
    }

    @Override
    protected void main() {
        super.main();
        ButterKnife.bind(this);

        setTitle("详情");

        dialog = DialogUitl.loadingDialog(this);

        TermInfoEntity termInfoEntity = getIntent().getParcelableExtra("TermInfoEntity");

        list.add(termInfoEntity);

        //  **************  动态 addView   **************
        iwHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setErrorImageRes(R.mipmap.error_picture);// 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API

        topRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TieZiAdapter(R.layout.item_tiezi , list , this , this , this , 1);
        topRecycler.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogCommentsAdapter = new DialogCommentsAdapter(R.layout.item_comments_dialog , commentEntityList);
        dialogCommentsAdapter.openLoadAnimation();
        recyclerView.setAdapter(dialogCommentsAdapter);
        dialogCommentsAdapter.setOnLoadMoreListener(this , recyclerView);
        dialogCommentsAdapter.setOnItemChildClickListener(this);

        getComments();
        addBrowse();
    }

    private void addBrowse(){
        //浏览数+1
        OkGo.<String>post(UrlUtil.ADD_BROWSE)
                .tag(this)
                .params("uid", AppConfig.getInstance().getUid())
                .params("termId", list.get(0).getTermId() )
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String res = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if(jsonObject.optInt("code") == 0){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getComments(){
        //获取评论
        OkGo.<String>post(UrlUtil.COMMENT_LIST)
                .tag(this)
                .params("uid", AppConfig.getInstance().getUid())
                .params("termId", list.get(0).getTermId() )
                .params("page", page)
                .params("limit", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String res = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if(jsonObject.optInt("code") == 0){

                                totalPage = jsonObject.optJSONObject("page").optInt("totalPage");

                                dataList.clear();
                                dataList = new Gson().fromJson(jsonObject.optJSONObject("page").optJSONArray("list").toString()
                                        , new TypeToken<List<CommunityCommentEntity>>() {}.getType());

                                commentEntityList.addAll(dataList);
                                dialogCommentsAdapter.notifyDataSetChanged();

                                tvCommentsCount.setText("最新评论 (" + commentEntityList.size() + "）");

                                if(commentEntityList.size() == 0){
                                    tvNoComment.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }else {
                                    tvNoComment.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @OnClick({R.id.tv_report, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_report:
                //举报
                ReportFragment fragment = new ReportFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.setClickListener(this);
                fragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.tv_send:
                //发表评论
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    ToastUtil.show("评论内容不能为空");
                    return;
                }
                dialog.show();
                OkGo.<String>post(UrlUtil.ADD_COMMENT)
                        .tag(this)
                        .params("uid", AppConfig.getInstance().getUid())
                        .params("termId", list.get(0).getTermId() )
                        .params("username", AppConfig.getInstance().getUserBean().getUserNiceName())
                        .params("content", etContent.getText().toString().trim())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                dialog.dismiss();
                                String res = response.body();

                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    if(jsonObject.optInt("code") == 0){
                                        ToastUtil.show("发布评论成功");
                                        onRefresh();
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

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setClick(String name , String content) {
        //举报列表的点击事件
        dialog.show();

        ReportRequestBean bean = new ReportRequestBean();
        bean.setDealUid(Integer.parseInt(AppConfig.getInstance().getUid()));
        bean.setReportName(name);
        bean.setReportContent(content);
        bean.setTermId(list.get(0).getTermId());
        bean.setUid(list.get(0).getUid().intValue());

        String json = new Gson().toJson(bean);
        Log.e("ReportJson" , json);

        OkGo.<String>post(UrlUtil.REPORT_SAVE)
                .tag(this)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.body());
                            if(jsonObject.optInt("code") == 0){
                                ToastUtil.show("举报成功，请等待审核");
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
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {

        switch (view.getId()){
            case R.id.ll_good:
                //给评论点赞
                String url;
                if(commentEntityList.get(position).getIsstar() == 0 ){
                    //点赞
//                    url = "http://192.168.1.75:8585/xqpd/tdapp/terminfo/addCommentStarTime";
                    url = UrlUtil.COMMENT_GOOD;
                }else {
                    //取消点赞
                    url = UrlUtil.CANCEL_COMMENT_GOOD;
                }
                dialog.show();
                OkGo.<String>post(url)
                        .tag(this)
                        .params("termId" , list.get(0).getTermId())
                        .params("uid", AppConfig.getInstance().getUid())
                        .params("commentId", commentEntityList.get(position).getCid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    dialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if(jsonObject.optInt("code") == 0){
                                        if(commentEntityList.get(position).getIsstar() == 0 ){
                                            //未点赞变为点赞
                                            commentEntityList.get(position).setIsstar(1);
                                            commentEntityList.get(position).setAssist(commentEntityList.get(position).getAssist() + 1);
                                        }else {
                                            //点赞变为未点赞
                                            commentEntityList.get(position).setIsstar(0);
                                            commentEntityList.get(position).setAssist(commentEntityList.get(position).getAssist() - 1);
                                        }
                                        dialogCommentsAdapter.notifyDataSetChanged();
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
                break;
            case R.id.tv_focus:
                //关注
                String focusUrl;
                if(list.get(position).getIsfocus() == 0 ){
                    //关注用户
                    focusUrl = UrlUtil.FOCUS_USER;
                }else {
                    //取消关注用户
                    focusUrl = UrlUtil.CANCEL_FOCUS;
                }
                dialog.show();
                OkGo.<String>post(focusUrl)
                        .tag(this)
                        .params("touid" , list.get(position).getUid())
                        .params("uid", AppConfig.getInstance().getUid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    dialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if(jsonObject.optInt("code") == 0){
                                        if(list.get(position).getIsfocus() == 0 ){
                                            //未关注变为关注
                                            list.get(position).setIsfocus(1);
                                        }else {
                                            //关注变为未关注
                                            list.get(position).setIsfocus(0);
                                        }
                                        adapter.notifyDataSetChanged();
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
                break;

        }
    }

    @Override
    public void onThumbPictureClick(ImageView i, SparseArray<ImageView> imageGroupList, List<Uri> urlList, int layoutPosition) {
        //图片点击放大点击事件
        iwHelper.show(i, imageGroupList, urlList);
    }

    @Override
    public void onImageClick(int imagePosition) {

    }

    public void onRefresh() {
        page = 1;
        commentEntityList.clear();
        getComments();
    }

    @Override
    public void onLoadMoreRequested() {
        //加载更多
        if(page < totalPage){
            dialogCommentsAdapter.addData(dataList);
            dialogCommentsAdapter.loadMoreComplete();
            dialogCommentsAdapter.notifyDataSetChanged();
            page++;
        }else {
            dialogCommentsAdapter.loadMoreEnd();
        }
    }
}
