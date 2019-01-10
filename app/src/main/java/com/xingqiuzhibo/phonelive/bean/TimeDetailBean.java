package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/8/008.
 * 直播时长明细bean
 */

public class TimeDetailBean {
    private String city;// (string, optional): 城市 ,
    private long endtime;// (integer, optional): 结束时间 ,
    private Integer id;// (integer, optional),
    private String lat;// (string, optional): 维度 ,
    private Integer liveclassid;// (integer, optional): 直播分类ID ,
    private String lng;// (string, optional): 经度 ,
    private Integer nums;// (integer, optional): 关播时人数 ,
    private String province;// (string, optional): 省份 ,
    private Integer showid;// (integer, optional): 直播标识 ,
    private long starttime;// (integer, optional): 开始时间 ,
    private String stream;// (string, optional): 流名 ,
    private String thumb;// (string, optional): 封面图 ,
    private String time;// (string, optional): 格式日期 ,
    private String title;// (string, optional): 标题 ,
    private Integer type;// (integer, optional): 直播类型 ,
    private String typeVal;// (string, optional): 类型值 ,
    private Integer uid;// (integer, optional): 用户ID ,
    private String votes;// (string, optional): 本次直播收益


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Integer getLiveclassid() {
        return liveclassid;
    }

    public void setLiveclassid(Integer liveclassid) {
        this.liveclassid = liveclassid;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getShowid() {
        return showid;
    }

    public void setShowid(Integer showid) {
        this.showid = showid;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
