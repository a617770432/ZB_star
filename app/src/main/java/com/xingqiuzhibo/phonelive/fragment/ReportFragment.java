package com.xingqiuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.DialogCommentsAdapter;
import com.xingqiuzhibo.phonelive.adapter.ReportAdapter;
import com.xingqiuzhibo.phonelive.bean.CommentBean;
import com.xingqiuzhibo.phonelive.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReportFragment extends DialogFragment implements BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;

    Unbinder unbinder;
    private ClickListener listener;

    private List<ReportBean> list = new ArrayList<>();//数据源
    private ReportAdapter adapter;

    private int selectPosition = 0;//选中的下标默认第一个。

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        list.clear();
        ReportBean reportBean1 = new ReportBean();
        reportBean1.setName("垃圾广告");
        reportBean1.setCheck(true);
        list.add(reportBean1);

        ReportBean reportBean2 = new ReportBean();
        reportBean2.setName("欺骗信息");
        list.add(reportBean2);

        ReportBean reportBean3 = new ReportBean();
        reportBean3.setName("有害信息");
        list.add(reportBean3);

        ReportBean reportBean4 = new ReportBean();
        reportBean4.setName("人身攻击");
        list.add(reportBean4);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReportAdapter(R.layout.item_report , list);
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close, R.id.tv_cancel, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                break;
            case R.id.tv_cancel:
                break;
            case R.id.tv_confirm:
                listener.setClick(list.get(selectPosition).getName() , etContent.getText().toString().trim());
                break;
        }
        dismiss();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        for (int i = 0 ; i < list.size() ; i ++ ){
            if(i == position){
                list.get(i).setCheck(true);
            }else {
                list.get(i).setCheck(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void setClick(String name , String content);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

}
