package com.sunland.contactbook.bean;

public class StaffListRequestBean extends BaseRequestBean {

    private int pageNo;
    private int pageIndex;
    private String bmglm;
    private String str;
    private int isUse;

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getBmglm() {
        return bmglm;
    }

    public void setBmglm(String bmglm) {
        this.bmglm = bmglm;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
