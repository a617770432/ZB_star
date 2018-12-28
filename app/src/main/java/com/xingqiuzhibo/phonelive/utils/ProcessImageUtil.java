package com.xingqiuzhibo.phonelive.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;


import com.xingqiuzhibo.phonelive.AppConfig;
import com.xingqiuzhibo.phonelive.R;
import com.xingqiuzhibo.phonelive.interfaces.ActivityResultCallback;
import com.xingqiuzhibo.phonelive.interfaces.ImageResultCallback;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by cxf on 2018/9/29.
 * 选择图片 裁剪
 */

public class ProcessImageUtil extends ProcessResultUtil {

    private Context mContext;
    private String[] mCameraPermissions;
    private String[] mAlumbPermissions;
    private Runnable mCameraPermissionCallback;
    private Runnable mAlumbPermissionCallback;
    private ActivityResultCallback mCameraResultCallback;
    private ActivityResultCallback mAlumbResultCallback;
    private ActivityResultCallback mCropResultCallback;
    private File mCameraResult;//拍照后得到的图片
    private File mCorpResult;//裁剪后得到的图片
    private ImageResultCallback mResultCallback;
    private boolean mNeedCrop;//是否需要裁剪
    public ProcessImageUtil(FragmentActivity activity) {
        super(activity);
        mContext = activity;
        mCameraPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        mAlumbPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        mCameraPermissionCallback = new Runnable() {
            @Override
            public void run() {
                takePhoto();
            }
        };
        mAlumbPermissionCallback = new Runnable() {
            @Override
            public void run() {
                chooseFile();
            }
        };
        mCameraResultCallback = new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (mNeedCrop) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        cropAfterCamera(FileProvider.getUriForFile(mContext, WordUtil.getString(R.string.FILE_PROVIDER), mCameraResult));
                    } else {
                        cropAfterCamera(Uri.fromFile(mCameraResult));
                    }
                } else {
                    if (mResultCallback != null) {
                        mResultCallback.onSuccess(mCameraResult);
                    }
                }
            }
            @Override
            public void onFailure() {
                ToastUtil.show(R.string.img_camera_cancel);
            }
        };
        mAlumbResultCallback = new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                cropAfterChoose(intent.getData());
            }
            @Override
            public void onFailure() {
                ToastUtil.show(R.string.img_alumb_cancel);
            }
        };
        mCropResultCallback = new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (mResultCallback != null) {
                    mResultCallback.onSuccess(mCorpResult);
                }
            }
            @Override
            public void onFailure() {
                ToastUtil.show(R.string.img_crop_cancel);
            }
        };
    }
    /**
     * 拍照获取图片
     */
    public void getImageByCamera(boolean needCrop) {
        mNeedCrop = needCrop;
        requestPermissions(mCameraPermissions, mCameraPermissionCallback);
    }
    /**
     * 拍照获取图片
     */
    public void getImageByCamera() {
        getImageByCamera(true);
    }
    /**
     * 相册获取图片
     */
    public void getImageByAlumb() {
        requestPermissions(mAlumbPermissions, mAlumbPermissionCallback);
    }
    /**
     * 开启摄像头，执行照相
     */
    private void takePhoto() {
        if (mResultCallback != null) {
            mResultCallback.beforeCamera();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraResult = getNewFile();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(mContext, WordUtil.getString(R.string.FILE_PROVIDER), mCameraResult);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(mCameraResult);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, mCameraResultCallback);
    }
    private File getNewFile() {
        // 裁剪头像的绝对路径
        File dir = new File(AppConfig.CAMERA_IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, DateFormatUtil.getCurTimeString() + ".png");
    }
    /**
     * 拍照后裁剪
     */
    private void cropAfterCamera(Uri uri) {
        cropAfterChoose(uri);
    }
    /**
     * 打开相册，选择文件
     */
//    private void chooseFile() {
//        Intent intent = new Intent();
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_PICK);
//            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, WordUtil.getString(R.string.choose_flie)), mAlumbResultCallback);

//    }


    /**
     * 打开相册，选择文件
     */
    private void chooseFile() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        // startActivityForResult(Intent.createChooser(intent, WordUtil.getString(R.string.choose_flie)), mAlumbResultCallback);
        startActivityForResult(intent, mAlumbResultCallback);
    }

    /**
     * 选择后裁剪
     */
    private void cropAfterChoose(Uri uri) {
        mCorpResult = getNewFile();
        try{
            Uri resultUri = Uri.fromFile(mCorpResult);
            if (resultUri == null || mFragment == null || mContext == null) {
                return;
            }
            UCrop uCrop = UCrop.of(uri, resultUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(400, 400);
            Intent intent = uCrop.getIntent(mContext);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, mCropResultCallback);
        }catch (Exception e){
            try {
                Uri resultUri = null;
                if (Build.VERSION.SDK_INT >= 24) {
                    resultUri = FileProvider.getUriForFile(mContext, WordUtil.getString(R.string.FILE_PROVIDER), mCorpResult);
                }
                if (resultUri == null || mFragment == null || mContext == null) {
                    return;
                }
                UCrop uCrop = UCrop.of(uri, resultUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(400, 400);
                Intent intent = uCrop.getIntent(mContext);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, mCropResultCallback);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

    }
    public void setImageResultCallback(ImageResultCallback resultCallback) {
        mResultCallback = resultCallback;
    }
    @Override
    public void release() {
        super.release();
        mResultCallback = null;
    }


    private boolean isGifFile(File file) {
        FileInputStream inputStream=null;
        try {
            inputStream = new FileInputStream(file);
            int[] flags = new int[5];
            flags[0] = inputStream.read();
            flags[1] = inputStream.read();
            flags[2] = inputStream.read();
            flags[3] = inputStream.read();
            inputStream.skip(inputStream.available() - 1);
            flags[4] = inputStream.read();
            inputStream.close();
            return flags[0] == 71 && flags[1] == 73 && flags[2] == 70 && flags[3] == 56 && flags[4] == 0x3B;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
