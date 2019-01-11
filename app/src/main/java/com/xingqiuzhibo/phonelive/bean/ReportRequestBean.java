package com.xingqiuzhibo.phonelive.bean;

public class ReportRequestBean {

    private Integer dealUid , termId ,uid ;
    private String reportContent , reportName ;

    public Integer getDealUid() {
        return dealUid;
    }

    public void setDealUid(Integer dealUid) {
        this.dealUid = dealUid;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
