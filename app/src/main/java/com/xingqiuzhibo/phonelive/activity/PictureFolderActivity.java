package com.xingqiuzhibo.phonelive.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.adapter.PictureFolderAdapter;
import com.xingqiuzhibo.phonelive.bean.PhotoFolder;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;
import com.xingqiuzhibo.phonelive.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureFolderActivity extends AbsActivity implements PictureFolderAdapter.OnItemClickListener{

    public static final int SOURCE_FOR_RADIO = 1;
    private List<PhotoFolder> list = new ArrayList<>(); //获取所有照片的路径
    private int source;
    private int left;
    private PictureFolderAdapter adapter;

    private PictureHandler pictureHandler;

    private Map<String, PhotoFolder> map;

    private ProgressDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture_folder;
    }

    @Override
    protected void main() {
        super.main();
        setTitle("选择照片");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        source = getIntent().getIntExtra("source", SOURCE_FOR_RADIO);
        left = getIntent().getIntExtra("left", -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PictureFolderAdapter(this, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        pictureHandler = new PictureHandler(this);
        dialog = new ProgressDialog(this);

        map = Utils.getPhotos(this);
        pictureHandler.sendEmptyMessage(map.isEmpty() ? 0 : 1);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("source", source);
        bundle.putInt("left", left);
        bundle.putStringArrayList("list", (ArrayList<String>) list.get(position).getList());
        StartActivityUtil.start(this, PictureActivity.class, bundle, Constants.REQUEST_CODE_FOR_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case Constants.REQUEST_CODE_FOR_SELECT_PHOTO:
                setResult(RESULT_OK, data);
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static class PictureHandler extends Handler {
        private WeakReference<PictureFolderActivity> weakReference;

        PictureHandler(PictureFolderActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PictureFolderActivity activity = weakReference.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    activity.dialog.dismiss();

                    Set<String> keys = activity.map.keySet();
                    for (String key : keys) {
                        if (!Constants.ALL_PHOTO.equals(key)) {
                            activity.list.add(activity.map.get(key));
                        }
                    }
                    activity.adapter.notifyItemRangeInserted(0, activity.list.size());
                    break;
                default:
                    activity.dialog.dismiss();
            }
        }
    }
}
