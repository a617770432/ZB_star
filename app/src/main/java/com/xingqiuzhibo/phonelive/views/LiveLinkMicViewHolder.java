package com.xingqiuzhibo.phonelive.views;

import android.view.ViewGroup;

/**
 * Created by cxf on 2018/10/25.
 */

public interface LiveLinkMicViewHolder {

    ViewGroup getSmallContainer();

    ViewGroup getRightContainer();

    ViewGroup getPkContainer();

    void changeToLeft();

    void changeToBig();
}
