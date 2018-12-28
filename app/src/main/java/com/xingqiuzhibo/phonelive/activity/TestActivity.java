package com.xingqiuzhibo.phonelive.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.custom.ItemDecoration;
import com.xingqiuzhibo.phonelive.game.adapter.LhTextAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/19.
 */

public class TestActivity extends AbsActivity {
    private RecyclerView recyclerView;
    private LhTextAdapter mLhTextAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_2;
    }

    @Override
    protected void main() {
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 6, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext,getResources().getColor(R.color.line), 1, 1);
        decoration.setOnlySetItemOffsetsButNoDraw(false);
        recyclerView.addItemDecoration(decoration);
        initData();
    }

    private void initData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            list.add(i);
        }
        if (mLhTextAdapter == null) {
            mLhTextAdapter = new LhTextAdapter(mContext, list,10);
            recyclerView.setAdapter(mLhTextAdapter);
        }
    }


}
