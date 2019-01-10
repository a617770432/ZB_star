package com.xingqiuzhibo.phonelive.bean;

import java.util.List;

public class RangeBean {

    private Integer rid , parentId , status , sort , recommended;
    private String rangeName , createTime , logo , notice;

    private List<RangeBean> children;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getRecommended() {
        return recommended;
    }

    public void setRecommended(Integer recommended) {
        this.recommended = recommended;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<RangeBean> getChildren() {
        return children;
    }

    public void setChildren(List<RangeBean> children) {
        this.children = children;
    }
}
