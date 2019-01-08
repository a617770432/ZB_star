package com.xingqiuzhibo.phonelive.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.custom.MessagePicturesLayout;
import com.xingqiuzhibo.phonelive.fragment.CommentsFragment;
import com.xingqiuzhibo.phonelive.fragment.ReportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TieZiDetailActivity extends AbsActivity implements ReportFragment.ClickListener{

    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.iv_head)
    AppCompatImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_views)
    TextView tvViews;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.pic_layout)
    MessagePicturesLayout picLayout;
    @BindView(R.id.iv_video)
    AppCompatImageView ivVideo;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.rl_kind)
    RelativeLayout rlKind;
    @BindView(R.id.tv_comments_count)
    TextView tvCommentsCount;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    private Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tie_zi_detail;
    }

    @Override
    protected void main() {
        super.main();
        unbinder = ButterKnife.bind(this);

        setTitle("详情");

    }

    @OnClick({R.id.tv_report, R.id.tv_focus, R.id.tv_send})
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
            case R.id.tv_focus:
                break;
            case R.id.tv_send:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setClick(int position) {
        //举报列表的点击事件

    }
}
