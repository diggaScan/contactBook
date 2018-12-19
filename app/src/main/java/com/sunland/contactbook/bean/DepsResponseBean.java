package com.sunland.contactbook.bean;

import com.sunland.netmodule.def.bean.result.ResultBase;

import java.util.List;

public class DepsResponseBean extends ResultBase {

    private String totalCount;

    private List<DepGeneralInfo> depGeneralInfo;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<DepGeneralInfo> getDepGeneralInfo() {
        return depGeneralInfo;
    }

    public void setDepGeneralInfo(List<DepGeneralInfo> depGeneralInfo) {
        this.depGeneralInfo = depGeneralInfo;
    }
}
