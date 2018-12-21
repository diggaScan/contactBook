package com.sunland.contactbook.bean.i_depList_bean;

import android.widget.BaseAdapter;

import com.sunland.contactbook.bean.BaseRequestBean;

public class DepsRequestBean extends BaseRequestBean {

    private String bmglm;

    public String getBmglm() {
        return bmglm;
    }

    public void setBmglm(String bmglm) {
        this.bmglm = bmglm;
    }
}
