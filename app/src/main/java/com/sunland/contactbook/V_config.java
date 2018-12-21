package com.sunland.contactbook;

import android.os.Build;

public final class V_config {

    //  部门列表查询接口
    public final static String DEP_LIST = "queryDepList";
    //  人员列表查询
    public final static String STAFF_LIST = "queryStaffList";
    //  人员详情查询
    public final static String POLICE_DETAIL = "queryPoliceDetail";
    //  登录接口
    public final static String USER_LOGIN = "userLogin";
    //  免密登录接口
    public final static String MM_USER_LOGIN = "userMMLogin";

    //  本机信息
    public final static String BRAND = Build.BRAND;//手机品牌
    public final static String MODEL = Build.MODEL + " " + Build.VERSION.SDK_INT;//手机型号
    public final static String OS = "android" + Build.VERSION.SDK_INT;//手机操作系统
    public static String imei = "";
    public static String imsi1 = " ";
    public static String imsi2 = "";
    public static String gpsX = "";//经度
    public static String gpsY = "";//纬度
    public static String gpsinfo = gpsX + gpsY;
    //用户代码
    public static String YHDM;

}
