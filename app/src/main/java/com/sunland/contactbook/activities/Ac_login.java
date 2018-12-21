package com.sunland.contactbook.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunland.contactbook.R;
import com.sunland.contactbook.V_config;
import com.sunland.contactbook.bean.BaseRequestBean;
import com.sunland.contactbook.bean.i_login_bean.LoginRequestBean;
import com.sunland.contactbook.bean.i_login_bean.LoginResBean;
import com.sunland.contactbook.utils.DialogUtils;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.cybertech.pdk.OperationLog;

/**
 * A login screen that offers login via email/password.
 */
public class Ac_login extends Ac_base {

    private final String TAG = "LoginActivity";
    @BindView(R.id.user_name)
    public EditText et_username;
    @BindView(R.id.password)
    public EditText et_password;
    @BindView(R.id.email_sign_in_button)

    public Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_login);
        toolbar.setVisibility(View.GONE);
        et_password.requestFocus();
    }

    @Override
    public boolean setImmersive() {
        return false;
    }

    public void queryYdjwData(String reqName) {
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(reqName), 15000);
        mRequestManager.postRequestWithoutDialog();
    }

    private BaseRequestBean assembleRequestObj(String reqName) {
        // TODO: 2018/12/21/021 修改参数
        switch (reqName) {
            case V_config.USER_LOGIN:
                LoginRequestBean requestBean = new LoginRequestBean();
                requestBean.setYhdm(et_username.getText().toString());
                requestBean.setImei("1");
                requestBean.setImsi("1");
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String pda_time = simpleDateFormat.format(date);
                requestBean.setPdaTime(pda_time);
                requestBean.setGpsX("gpsx");
                requestBean.setGpsY("gpsy");
                requestBean.setPassword(et_password.getText().toString());
                requestBean.setDlmk("1");
                requestBean.setSjpp("1");
                requestBean.setSjxx("1");
                requestBean.setZzxt("1");
                return requestBean;
        }
        return null;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        dialogUtils.dialogDismiss();

        LoginResBean loginResBean = (LoginResBean) bean;
        if (loginResBean == null) {
            Toast.makeText(this, "服务异常", Toast.LENGTH_SHORT).show();
            return;
        }

        //code 0 允许登录
        //code 1 登录失败
        if (loginResBean.getCode().equals("0")) {
            V_config.YHDM = et_username.getText().toString();
            saveLog(0, OperationLog.OperationResult.CODE_SUCCESS, appendString(V_config.YHDM, V_config.BRAND,
                    V_config.MODEL));//yhdm,手机品牌，手机型号，警号
            hop2Activity(Ac_main.class);
        } else {
            saveLog(0, OperationLog.OperationResult.CODE_FAILURE,
                    appendString(V_config.YHDM, V_config.BRAND, V_config.MODEL));
            Toast.makeText(this, loginResBean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.email_sign_in_button)
    public void onClick(View view) {
        if (et_username.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        dialogUtils.showDialog(Ac_login.this, "登录中...", DialogUtils.TYPE_PROGRESS, new DialogUtils.OnCancelClickListener() {
            @Override
            public void onCancel() {
                mRequestManager.cancelAll();
            }
        }, null);

        queryYdjwData(V_config.USER_LOGIN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

