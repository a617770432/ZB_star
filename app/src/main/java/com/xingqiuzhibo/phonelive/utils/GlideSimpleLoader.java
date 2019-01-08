package com.xingqiuzhibo.phonelive.utils;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.ielse.imagewatcher.ImageWatcher;

public class GlideSimpleLoader implements ImageWatcher.Loader{

    @Override
    public void load(Context context, Uri uri, final ImageWatcher.LoadCallback loadCallback) {
        Glide.with(context).load(uri)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        loadCallback.onResourceReady(resource);
                    }
                });
    }
}
