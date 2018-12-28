package com.sunland.contactbook.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunland.contactbook.MyApplication;
import com.sunland.contactbook.R;
import com.sunland.contactbook.V_config;
import com.sunland.contactbook.bean.BaseRequestBean;
import com.sunland.contactbook.bean.i_depList_bean.DepsResponseBean;
import com.sunland.contactbook.bean.i_login_bean.LoginResBean;
import com.sunland.contactbook.bean.i_police_detail_bean.StaffListResponseBean;
import com.sunland.contactbook.bean.i_staff_list_bean.StaffDetailResponseBean;
import com.sunland.contactbook.utils.DialogUtils;
import com.sunland.contactbook.utils.WindowInfoUtils;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import cn.com.cybertech.pdk.OperationLog;

public abstract class Ac_base extends AppCompatActivity implements OnRequestCallback {

    public Toolbar toolbar;
    private ImageView nav_back;
    private LinearLayout container;
    private TextView title;
    public MyApplication mApplication;
    public DialogUtils dialogUtils;
    public RequestManager mRequestManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base);
        toolbar = findViewById(R.id.base_toolbar);
        nav_back = findViewById(R.id.nav_back);
        container = findViewById(R.id.container);
        title = findViewById(R.id.toolbar_title);
        mApplication = (MyApplication) getApplication();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (nav_back != null || nav_back.getVisibility() == View.VISIBLE) {
            nav_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        dialogUtils = DialogUtils.getInstance();
        mRequestManager = new RequestManager(this, this);
        initWindow();
    }

    public void setContentLayout(int layout) {
        getLayoutInflater().inflate(layout, container, true);
        ButterKnife.bind(this);

    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && setImmersive()) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            int actionBarHeight = (int) actionbarSizeTypedArray.getDimension(0, 0);
            actionbarSizeTypedArray.recycle();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, WindowInfoUtils.getStatusBarHeight(this) + actionBarHeight);
            toolbar.setLayoutParams(lp);
        }
    }

    public void setNavVisible(boolean isVisible) {
        if (isVisible) {
            nav_back.setVisibility(View.VISIBLE);
        } else {
            nav_back.setVisibility(View.INVISIBLE);
        }
    }

    public boolean setImmersive() {
        return true;
    }

    public void setToolbarTitle(String title) {
        this.title.setText(title);
    }

    public void assembleBasicRequest(BaseRequestBean requestBean) {
        requestBean.setYhdm(V_config.YHDM);
        requestBean.setImei(V_config.imei);
        requestBean.setImsi(V_config.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);
        requestBean.setGpsX(V_config.gpsX);
        requestBean.setGpsY(V_config.gpsY);
    }

    public void saveLog(int operateType, int operationResult, String operateCondition) {
        try {
            OperationLog.logging(this
                    , "51A6B10D512296FF5E73E42FBD82C1E7"
                    , getApplication().getPackageName()
                    , operateType
                    , operationResult
                    , 1
                    , operateCondition);
        } catch (Exception e) {
            //未适配Fileprovider
            e.printStackTrace();
        }

    }

    public String appendString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i != strings.length - 1) {
                sb.append("@");
            }
        }
        return sb.toString();
    }

    public void hop2Activity(Class<? extends Ac_base> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public abstract void onDataResponse(String reqId, String reqName, ResultBase bean);

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        onDataResponse(reqId, reqName, (ResultBase) bean);
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        switch (reqName) {
            case V_config.DEP_LIST:
                return DepsResponseBean.class;
            case V_config.STAFF_LIST:
                return StaffListResponseBean.class;
            case V_config.POLICE_DETAIL:
                return StaffDetailResponseBean.class;
            case V_config.USER_LOGIN:
            case V_config.MM_USER_LOGIN:
                return LoginResBean.class;
        }
        return null;
    }
}
