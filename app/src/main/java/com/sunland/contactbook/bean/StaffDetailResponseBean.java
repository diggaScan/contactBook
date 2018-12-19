package com.sunland.contactbook.bean;

import com.sunland.netmodule.def.bean.result.ResultBase;

public class StaffDetailResponseBean extends ResultBase {
    private PoliceInfo policeInfo;

    public PoliceInfo getPoliceInfo() {
        return policeInfo;
    }

    public void setPoliceInfo(PoliceInfo policeInfo) {
        this.policeInfo = policeInfo;
    }
}
