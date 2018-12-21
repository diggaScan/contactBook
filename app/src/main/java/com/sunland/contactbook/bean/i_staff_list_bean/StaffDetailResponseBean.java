package com.sunland.contactbook.bean.i_staff_list_bean;

import com.sunland.contactbook.bean.i_police_detail_bean.PoliceInfo;
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
