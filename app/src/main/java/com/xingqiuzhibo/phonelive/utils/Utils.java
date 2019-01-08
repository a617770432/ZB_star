package com.xingqiuzhibo.phonelive.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.xingqiuzhibo.phonelive.Constants;
import com.xingqiuzhibo.phonelive.bean.PhotoFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    /**
     * 获取本地所有图片和图片文件夹
     *
     * @param context context
     * @return 图片和图片文件夹
     */
    public static Map<String, PhotoFolder> getPhotos(Context context) {

        Map<String, PhotoFolder> map = new HashMap<>();
        PhotoFolder folder = new PhotoFolder();
        folder.setName(Constants.ALL_PHOTO);
        folder.setDirPath(Constants.ALL_PHOTO);
        folder.setList(new ArrayList<String>());
        map.put(Constants.ALL_PHOTO, folder);

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri
                    , null
                    , MediaStore.Images.Media.MIME_TYPE + " in(?, ?)"
                    , new String[]{"image/jpeg", "image/png"}
                    , MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            if (cursor == null) {
                return map;
            }
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndex);
                File parentPath = new File(path).getParentFile();
                if (parentPath == null) {
                    continue;
                }
                String dirPath = parentPath.getAbsolutePath();
                if (map.containsKey(dirPath)) {
                    map.get(dirPath).getList().add(path);
                    map.get(Constants.ALL_PHOTO).getList().add(path);
                    continue;
                }
                PhotoFolder photoFolder = new PhotoFolder();
                List<String> list = new ArrayList<>();
                list.add(path);
                photoFolder.setList(list);
                photoFolder.setDirPath(dirPath);
                photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                map.put(dirPath, photoFolder);
                map.get(Constants.ALL_PHOTO).getList().add(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return map;
    }

    public static String getLocalImagePath(String path) {
        return "file://" + path;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
