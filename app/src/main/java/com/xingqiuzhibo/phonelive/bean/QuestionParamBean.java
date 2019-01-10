package com.xingqiuzhibo.phonelive.bean;

/**
 * Created by hx
 * Time 2019/1/9/009.
 * 注册问题参数bean
 */

public class QuestionParamBean {
    private Integer id;//": 1, // 密保问题ID
    private String title;//": "爸爸去哪儿？", // 密保问题
    private String answer;//": "没去哪" // 用户填写对应问题的答案

    public QuestionParamBean(Integer id, String title, String answer) {
        this.id = id;
        this.title = title;
        this.answer = answer;
    }

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
