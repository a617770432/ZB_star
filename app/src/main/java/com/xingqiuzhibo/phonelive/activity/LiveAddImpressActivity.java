package com.xingqiuzhibo.phonelive.activity;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.views.LiveAddImpressViewHolder;

/**
 * Created by cxf on 2018/10/15.
 * 添加印象
 */

public class LiveAddImpressActivity extends AbsActivity {

    private LiveAddImpressViewHolder mLiveAddImpressViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void main() {
        String toUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        mLiveAddImpressViewHolder = new LiveAddImpressViewHolder(mContext, (ViewGroup) findViewById(R.id.container));
        addLifeCycleListener(mLiveAddImpressViewHolder.getLifeCycleListener());
        mLiveAddImpressViewHolder.addToParent();
        mLiveAddImpressViewHolder.setToUid(toUid);
        mLiveAddImpressViewHolder.loadData();
    }


    @Override
    public void onBackPressed() {
        if (mLiveAddImpressViewHolder != null && mLiveAddImpressViewHolder.isUpdatedImpress()) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}