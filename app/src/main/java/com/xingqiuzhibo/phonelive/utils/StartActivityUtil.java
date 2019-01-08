package com.xingqiuzhibo.phonelive.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class StartActivityUtil {


    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Activity activity, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Fragment fragment, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        fragment.startActivity(intent);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Fragment fragment, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        fragment.startActivity(intent);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Fragment fragment, Class<?> cls, Uri bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setData(bundle);
        intent.setClass(activity, cls);
        fragment.startActivityForResult(intent , requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Fragment fragment, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        fragment.startActivityForResult(intent, requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Fragment fragment, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        fragment.startActivityForResult(intent, requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }

    public static void start(Activity activity, Class<?> cls, Uri bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setData(bundle);
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent , requestCode);
        ActivityTransitionUtil.startActivityTransition(activity);
    }
}
