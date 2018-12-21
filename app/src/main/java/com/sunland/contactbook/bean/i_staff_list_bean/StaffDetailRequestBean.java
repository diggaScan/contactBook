package com.sunland.contactbook.bean.i_staff_list_bean;

import com.sunland.contactbook.bean.BaseRequestBean;

public class StaffDetailRequestBean extends BaseRequestBean {
    private String idcard;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
