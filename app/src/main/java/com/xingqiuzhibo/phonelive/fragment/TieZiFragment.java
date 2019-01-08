package com.xingqiuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.TieZiDetailActivity;
import com.xingqiuzhibo.phonelive.adapter.TieZiAdapter;
import com.xingqiuzhibo.phonelive.bean.TieZiBean;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TieZiFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener
        , MessagePicturesLayout.ImageCallback , BaseQuickAdapter.OnItemClickListener , ReportFragment.ClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<TieZiBean> list = new ArrayList<>();//数据源
    private TieZiAdapter adapter;

    //九宫格布局被点击时所处的position
    private int picLayoutPosition;

    public TieZiFragment() {
        // Required empty public constructor
    }

    public static TieZiFragment newInstance() {
        TieZiFragment fragment = new TieZiFragment();
        return fragment;
    }


    @Override
    protected void initView() {
        MessagePicturesLayout.Callback callback = (MessagePicturesLayout.Callback) getActivity();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TieZiAdapter(R.layout.item_tiezi , list , callback , this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0 ; i < 5 ; i ++){
            list.add(new TieZiBean());
        }
        EventBus.getDefault().register(this);
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
}
