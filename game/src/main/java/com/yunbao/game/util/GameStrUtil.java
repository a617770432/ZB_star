package com.yunbao.game.util;

import android.support.annotation.NonNull;
import android.util.SparseIntArray;

import com.yunbao.game.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by debug on 2018/12/18.
 */

public class GameStrUtil {
    private static SparseIntArray mLhResultStr;
    static {
        mLhResultStr=new SparseIntArray();
        mLhResultStr.put(1, R.string.game_lh_l);
        mLhResultStr.put(1, R.string.game_lh_hu);
        mLhResultStr.put(1, R.string.game_lh_he);
    }

    public static int getLhResultStr(int key){
        return mLhResultStr.get(key);
    }
}
