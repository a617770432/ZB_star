package com.xingqiuzhibo.phonelive.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.TieZiDetailActivity;
import com.xingqiuzhibo.phonelive.adapter.TieZiAdapter;
import com.xingqiuzhibo.phonelive.bean.ReportRequestBean;
import com.xingqiuzhibo.phonelive.bean.TermInfoEntity;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.LoadingView;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.utils.DialogUitl;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TieZiFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener
        , MessagePicturesLayout.ImageCallback , BaseQuickAdapter.OnItemClickListener
        , ReportFragment.ClickListener , BaseQuickAdapter.RequestLoadMoreListener , SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.load_view)
    LoadingView loadingView;

    private List<TermInfoEntity> list = new ArrayList<>();//数据源
    private TieZiAdapter adapter;

    //九宫格布局被点击时所处的position
    private int picLayoutPosition;

    private int page = 1;
    private int pageSize = 15;
    private int totalPage = 0;

    private List<TermInfoEntity> dataList = new ArrayList<>();//接口得到的数据

    private int openType;

    private int reportPosition;//举报点击所对应的列表下标

    private Dialog dialog;

    public TieZiFragment() {
        // Required empty public constructor
    }

    public static TieZiFragment newInstance(int openType) {
        TieZiFragment fragment = new TieZiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("openType" , openType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if(getArguments() != null){
            openType = getArguments().getInt("openType");
        }

    }

    @Override
    protected void initView() {
        MessagePicturesLayout.Callback callback = (MessagePicturesLayout.Callback) getActivity();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TieZiAdapter(R.layout.item_tiezi , list , callback , this , getActivity() , 0);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadMoreListener(this , recyclerView);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light
                , android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);

        dialog = DialogUitl.loadingDialog(getActivity());

        onRefresh();
    }

    private void getTieZi(){

        refreshLayout.setRefreshing(false);

        if(openType == 2 || openType == 3){
            OkGo.<String>get(UrlUtil.GET_TIE_ZI)
                    .tag(this)
                    .params("uid", AppConfig.getInstance().getUid())
                    .params("token", AppConfig.getInstance().getToken())
                    .params("page", page)
                    .params("limit", pageSize)
                    .params("filetype" , openType - 1)
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
                                            , new TypeToken<List<TermInfoEntity>>() {}.getType());

                                    list.addAll(dataList);
                                    adapter.notifyDataSetChanged();

                                    if(list.size() == 0 ){
                                        loadingView.noData(R.mipmap.icon_no_release , "暂无帖子");
                                    }else {
                                        loadingView.setGone();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else if( openType == 0 ){
            OkGo.<String>get(UrlUtil.GET_TIE_ZI)
                    .tag(this)
                    .params("uid", AppConfig.getInstance().getUid())
                    .params("token", AppConfig.getInstance().getToken())
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
                                            , new TypeToken<List<TermInfoEntity>>() {}.getType());

                                    list.addAll(dataList);
                                    adapter.notifyDataSetChanged();

                                    if(list.size() == 0 ){
                                        loadingView.noData(R.mipmap.icon_no_release , "暂无帖子");
                                    }else {
                                        loadingView.setGone();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else if(openType == 1){
            OkGo.<String>get(UrlUtil.FOCUS_LIST)
//            OkGo.<String>get("http://192.168.1.75:8585/xqpd/tdapp/terminfo/getFocusTermList")
                    .tag(this)
                    .params("uid", AppConfig.getInstance().getUid())
                    .params("token", AppConfig.getInstance().getToken())
                    .params("page", page)
                    .params("limit", pageSize)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            String res = response.body();
                            Log.e("FOCUS_LIST" , res);
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                if(jsonObject.optInt("code") == 0){

                                    totalPage = jsonObject.optJSONObject("page").optInt("totalPage");

                                    dataList.clear();
                                    dataList = new Gson().fromJson(jsonObject.optJSONObject("page").optJSONArray("list").toString()
                                            , new TypeToken<List<TermInfoEntity>>() {}.getType());

                                    list.addAll(dataList);
                                    adapter.notifyDataSetChanged();

                                    if(list.size() == 0 ){
                                        loadingView.noData(R.mipmap.icon_no_release , "暂无帖子");
                                    }else {
                                        loadingView.setGone();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    @Override
    protected void loadData() {
//        getTieZi();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tiezi;
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()){
            case R.id.pic_layout:
                //九宫格布局点击

                break;
            case R.id.ll_report:
                //举报

                reportPosition = position;

                ReportFragment fragment = new ReportFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.setClickListener(this);
                fragment.show(getChildFragmentManager(), "");
                break;
            case R.id.ll_goods:
                String url;
                if(list.get(position).getIsstar() == 0 ){
                    //点赞
                    url = UrlUtil.CLICK_GOOD;
                }else {
                    //取消点赞
                    url = UrlUtil.CANCEL_GOOD;
                }
                dialog.show();
                OkGo.<String>post(url)
                        .tag(this)
                        .params("termId" , list.get(position).getTermId())
                        .params("uid", AppConfig.getInstance().getUid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    dialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    if(jsonObject.optInt("code") == 0){
                                        if(list.get(position).getIsstar() == 0 ){
                                            //未点赞变为点赞
                                            list.get(position).setIsstar(1);
                                            list.get(position).setAssist(list.get(position).getAssist() + 1);
                                        }else {
                                            //点赞变为未点赞
                                            list.get(position).setIsstar(0);
                                            list.get(position).setAssist(list.get(position).getAssist() - 1);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer event) {
        //处理逻辑
        picLayoutPosition = event;
    }

    @Override
    public void onImageClick(int imagePosition) {
        Toast.makeText( getActivity() , "被点击的是第" + picLayoutPosition +"行的第" + imagePosition + "张图片" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //帖子点击事件 跳转到帖子详情

        if(list.size() != 0){
            Bundle bundle = new Bundle();
            bundle.putParcelable("TermInfoEntity" , list.get(position));
            StartActivityUtil.start(getActivity() , TieZiDetailActivity.class , bundle);
        }

    }

    @Override
    public void setClick(String name , String content) {
        //举报列表点击事件回调
        dialog.show();

        ReportRequestBean bean = new ReportRequestBean();
        bean.setDealUid(Integer.parseInt(AppConfig.getInstance().getUid()));
        bean.setReportName(name);
        bean.setReportContent(content);
        bean.setTermId(list.get(reportPosition).getTermId());
        bean.setUid(list.get(reportPosition).getUid().intValue());

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
    public void onLoadMoreRequested() {
        //加载更多
        if(page < totalPage){
            adapter.addData(dataList);
            adapter.loadMoreComplete();
            adapter.notifyDataSetChanged();
            page++;
        }else {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        list.clear();
        getTieZi();
    }
}
