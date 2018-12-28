package com.xingqiuzhibo.phonelive.beauty;

import cn.tillusory.sdk.bean.TiFilterEnum;

/**
 * Created by cxf on 2018/12/13.
 */

public interface EffectListener {
    void onFilterChanged(TiFilterEnum tiFilterEnum);
    // void onRockChanged(TiRockEnum tiRockEnum);

    void onMeiBaiChanged(int progress);

    void onMoPiChanged(int progress);

    void onBaoHeChanged(int progress);

    void onFengNenChanged(int progress);

    void onBigEyeChanged(int progress);

    void onFaceChanged(int progress);

    void onTieZhiChanged(String tieZhiName);
}
