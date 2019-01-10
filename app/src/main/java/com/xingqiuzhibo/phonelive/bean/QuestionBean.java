package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/9/009.
 */

public class QuestionBean {

    private Integer id;//":"2"
    private String title;//":"我的生日是哪一天？","sort":"2","status":"1"
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
