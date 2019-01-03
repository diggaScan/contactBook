package com.sunland.contactbook.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.sunland.contactbook.V_config;
import com.sunland.contactbook.bean.BaseRequestBean;
import com.sunland.contactbook.bean.i_login_bean.LoginResBean;
import com.sunland.contactbook.bean.i_mm_login_bean.LoginMMRequestBean;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;

import cn.com.cybertech.models.User;
import cn.com.cybertech.pdk.OperationLog;


public class Ac_splash extends CheckSelfPermissionActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //独立App版
        if (mApplication.isIsoApp()) {
            hop2Activity(Ac_login.class);
            return;
        }
        //广达App版，免密登录
        if (mApplication.isAppCyber()) {
            User user = cn.com.cybertech.pdk.UserInfo.getUser(this);
            try {
                V_config.YHDM = user.getAccount();
            } catch (NullPointerException e) {
                Toast.makeText(this, "无法获取警号", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            queryYdjwData(V_config.MM_USER_LOGIN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void queryYdjwData(String reqName) {
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(), 15000);
        mRequestManager.postRequestWithoutDialog();
    }

    public BaseRequestBean assembleRequestObj() {
        // TODO: 2018/12/21/021 修改参数
        LoginMMRequestBean loginBean = new LoginMMRequestBean();
        assembleBasicRequest(loginBean);
        loginBean.setDlmk(V_config.APP_NAME);
        loginBean.setSjpp(V_config.BRAND);
        loginBean.setSjxx(V_config.MODEL);
        loginBean.setZzxt(V_config.OS);
        return loginBean;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        LoginResBean loginResBean = (LoginResBean) bean;
        if (loginResBean == null) {
            Toast.makeText(this, "服务异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!loginResBean.getCode().equals("0")) {
            saveLog(0, OperationLog.OperationResult.CODE_SUCCESS, appendString(V_config.YHDM, V_config.BRAND, V_config.MODEL));
            hop2Activity(Ac_main.class);
        } else {
            saveLog(0, OperationLog.OperationResult.CODE_FAILURE,
                    appendString(V_config.YHDM, V_config.BRAND, V_config.MODEL));
            Toast.makeText(this, loginResBean.getMessage(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
