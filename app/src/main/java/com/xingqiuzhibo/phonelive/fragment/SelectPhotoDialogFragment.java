package com.xingqiuzhibo.phonelive.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.BuildConfig;
import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.activity.PictureFolderActivity;
import com.xingqiuzhibo.phonelive.adapter.ShowPictureAdapter;
import com.xingqiuzhibo.phonelive.utils.FileUtil;
import com.xingqiuzhibo.phonelive.utils.StartActivityUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPhotoDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R.id.tv_select_album)
    TextView tvSelectAlbum;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private int type;

    private View view;
    private Activity activity;

    //调用照相机返回图片文件
    private File tempFile;

    //请求相机
    private static final int REQUEST_CAPTURE = 105;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    private ImageCropListener listener;

    private int open_type; // 1.拍照多张  2.性别

    private int pic_size;//...用于记录发布动态页面已有图片数量

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        type = Integer.parseInt(getTag());
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_photo_dialog , null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView(){
        open_type = type;
    }

    public void setPicSize(int pic_size){
        this.pic_size = pic_size;
    }

    @OnClick({R.id.tv_take_photo, R.id.tv_select_album, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_album:
                if(type == 1 || type == 3){
                    //权限判断
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请READ_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_EXTERNAL_STORAGE_REQUEST_CODE);
                    }else {
                        //跳转到相册
                        if(open_type == 1){
                            Bundle bundle = new Bundle();
                            bundle.putInt("left", pic_size);
                            bundle.putInt("source", 0);
                            StartActivityUtil.start(activity, PictureFolderActivity.class, bundle , Constants.REQUEST_CODE_FOR_SELECT_PHOTO_SHOW );
                        }else {
                            gotoPhoto();
                        }
                    }
                }
                break;
            case R.id.tv_take_photo:
                if(type == 1){
                    //权限判断
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED )  {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        //跳转到调用系统相机
                        gotoCamera();
                    }
                }
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }

    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + Constants.PICTURE_PATH), System.currentTimeMillis() + ".jpg");

        listener.setImagePath(tempFile);

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, "com.xingqiuzhibo.phonelive.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        activity.startActivityForResult(intent,open_type == 1 ? Constants.REQUEST_CODE_FOR_TAKE_PHOTO_SHOW : REQUEST_CAPTURE);
    }

    public interface ImageCropListener{
        void setImagePath(File path);
    }

    public void setImagePathListener(ImageCropListener listener){
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
