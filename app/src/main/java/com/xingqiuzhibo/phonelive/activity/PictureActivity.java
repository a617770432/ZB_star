package com.xingqiuzhibo.phonelive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.PictureAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends AbsActivity implements PictureAdapter.OnItemClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static final int SOURCE_FOR_RADIO = 1;
    private List<String> list = new ArrayList<>(); //获取所有照片的路径
    private int source;
    private int left;
    private PictureAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    protected void main() {
        super.main();

        setTitle("选择照片");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        source = getIntent().getIntExtra("source", SOURCE_FOR_RADIO);
        left = getIntent().getIntExtra("left", -1);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PictureAdapter(this, this, list);
        adapter.setOnClickListener(this);
        adapter.setSource(source);
        adapter.setLeft(left);
        recyclerView.setAdapter(adapter);

        list.addAll(getIntent().getStringArrayListExtra("list"));
        adapter.notifyItemRangeInserted(0, list.size());

        findViewById(R.id.iv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("path", adapter.getSelectList());
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        if (source == SOURCE_FOR_RADIO) {
            Intent intent = new Intent();
            intent.putExtra("path", list.get(position));
            setResult(RESULT_OK, intent);
            onBackPressed();
            return;
        }
    }

}
