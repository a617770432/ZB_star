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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by cxf on 2018/9/29.
 * 选择图片 裁剪
 */

public class ProcessImageUtil2 extends ProcessResultUtil {

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
    private ContentResolver mContentResolver;

    public ProcessImageUtil2(FragmentActivity activity) {
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
                if (intent!=null){
                    cropAfterChoose(intent.getData());
                }
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
        mCorpResult = getNewFile();
        Intent intent = new Intent("com.android.camera.action.CROP");
//        Uri uri2 = null;
//        if (Build.VERSION.SDK_INT >= 24) {
//            uri2 = FileProvider.getUriForFile(mContext, WordUtil.getString(R.string.FILE_PROVIDER), mCorpResult);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        } else {
//            uri2 = Uri.fromFile(mCorpResult);
//        }
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCorpResult));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);// 输出图片大小
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent, mCropResultCallback);
    }

    /**
     * 打开相册，选择文件
     */
    private void chooseFile() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, WordUtil.getString(R.string.choose_flie)), mAlumbResultCallback);
    }

    /**
     * 选择后裁剪
     */
    private void cropAfterChoose(Uri uri) {
//        if(mContentResolver==null){
//            mContentResolver=mContext.getContentResolver();
//        }
//        Cursor cursor = mContentResolver.query(uri, new String[]{ MediaStore.Images.Media.DATA}, null, null, null);
//        String path=null;
//        try {
//            if(cursor!=null&&cursor.moveToFirst()){;
//                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                path = cursor.getString(columnIndex);
//                L.e("ProcessImageUtil---path-->"+uri);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(cursor!=null){
//                cursor.close();
//            }
//        }
//        if(TextUtils.isEmpty(path)){
//            ToastUtil.show("选择文件失败");
//            return;
//        }
//        if(isGifFile(new File(path)) ){
//            ToastUtil.show("不支持gif");
//            return;
//        }
//        mCorpResult = getNewFile();
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        if (Build.VERSION.SDK_INT >= 24) {
//            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCorpResult));
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 400);// 输出图片大小
//        intent.putExtra("outputY", 400);
//        intent.putExtra("scale", true);// 去黑边
//        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//        startActivityForResult(intent, mCropResultCallback);

        mCorpResult = getNewFile();
        Uri destinationUri = null;
//        if (Build.VERSION.SDK_INT >= 24) {
//            destinationUri = FileProvider.getUriForFile(mContext, WordUtil.getString(R.string.FILE_PROVIDER), mCorpResult);
//        } else {
//            destinationUri = Uri.fromFile(mCorpResult);
//        }
        destinationUri = Uri.fromFile(mCorpResult);

        //Crop.of(uri, destinationUri).asSquare().start((Activity) mContext);

        /*UCrop.of(uri, destinationUri)
                .withAspectRatio(1.0f,1.0f)
                .withMaxResultSize(400, 400)
                .start((Activity) mContext);*/
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