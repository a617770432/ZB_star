package com.xingqiuzhibo.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2018/9/29.
 */

public class SearchUserBean extends UserBean {

    private int attention;
    private int islive;

    @JSONField(name = "isattention")
    public int getAttention() {
        return attention;
    }

    @JSONField(name = "isattention")
    public void setAttention(int attention) {
        this.attention = attention;
    }


    public int getIslive() {
        return islive;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }

    public SearchUserBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.attention);
        dest.writeInt(this.islive);
    }

    public SearchUserBean(Parcel in) {
        super(in);
        this.attention = in.readInt();
        this.islive = in.readInt();
    }

    public static final Creator<SearchUserBean> CREATOR = new Creator<SearchUserBean>() {
        @Override
        public SearchUserBean[] newArray(int size) {
            return new SearchUserBean[size];
        }

        @Override
        public SearchUserBean createFromParcel(Parcel in) {
            return new SearchUserBean(in);
        }
    };
}
