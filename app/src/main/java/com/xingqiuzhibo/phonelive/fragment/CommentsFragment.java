package com.xingqiuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.DialogCommentsAdapter;
import com.xingqiuzhibo.phonelive.bean.CommentBean;
import com.xingqiuzhibo.phonelive.utils.ToastUtil;
import com.xingqiuzhibo.phonelive.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommentsFragment extends BottomSheetDialogFragment {


    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;

    private Unbinder unbinder;

    private List<CommentBean> list = new ArrayList<>();//数据源
    private DialogCommentsAdapter adapter;

    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        list.clear();
        for (int i = 0 ; i < 5 ; i ++){
            list.add(new CommentBean());
        }

        if(list.size() >= 4){
            ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
            lp.height = Utils.dip2px(Objects.requireNonNull(getActivity()), 400);
            recyclerView.setLayoutParams(lp);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DialogCommentsAdapter(R.layout.item_comments_dialog , list);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_send:
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    ToastUtil.show("评论内容不能为空");
                    return;
                }
                break;
        }
    }
}
