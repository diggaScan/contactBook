package com.sunland.contactbook.bean;

import com.sunland.netmodule.def.bean.result.ResultBase;

import java.util.List;

public class StaffListResponseBean extends ResultBase {
    private List<StaffGeneralInfo> staffGeneralInfos;

    private int totalCount;

    private int totalPage;

    public List<StaffGeneralInfo> getStaffGeneralInfo() {
        return staffGeneralInfos;
    }

    public void setStaffGeneralInfo(List<StaffGeneralInfo> staffGeneralInfo) {
        this.staffGeneralInfos = staffGeneralInfo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
