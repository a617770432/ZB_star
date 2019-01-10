package com.xingqiuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xingqiuzhibo.phonelive.bean.TermInfoEntity;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;
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
        , ReportFragment.ClickListener , BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<TermInfoEntity> list = new ArrayList<>();//数据源
    private TieZiAdapter adapter;

    //九宫格布局被点击时所处的position
    private int picLayoutPosition;

    private int page = 1;
    private int pageSize = 15;
    private int totalPage = 0;

    private List<TermInfoEntity> dataList = new ArrayList<>();//接口得到的数据

    private int openType;

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
        adapter = new TieZiAdapter(R.layout.item_tiezi , list , callback , this , getActivity());
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
//        recyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadMoreListener(this , recyclerView);

    }

    private void getTieZi(){

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
        getTieZi();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tiezi;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.pic_layout:
                //九宫格布局点击

                break;
            case R.id.tv_report:
                //举报
                ReportFragment fragment = new ReportFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragment.setClickListener(this);
                fragment.show(getChildFragmentManager(), "");
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
        Bundle bundle = new Bundle();
        StartActivityUtil.start(getActivity() , TieZiDetailActivity.class , bundle);
    }

    @Override
    public void setClick(int position) {
        //举报列表点击事件回调

    }

    private void initData(){


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
}
