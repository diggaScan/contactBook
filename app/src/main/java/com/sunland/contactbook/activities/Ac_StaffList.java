package com.sunland.contactbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.StaffGeneralInfo;
import com.sunland.contactbook.bean.StaffListRequestBean;
import com.sunland.contactbook.bean.StaffListResponseBean;
import com.sunland.contactbook.recyclerConfig.Rv_Item_decoration;
import com.sunland.contactbook.recyclerConfig.StaffList_RvAdapter;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.OnRequestManagerCancel;
import com.sunland.netmodule.network.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class Ac_StaffList extends Ac_base implements OnRequestCallback
        , OnRequestManagerCancel {

    @BindView(R.id.recyclerView)
    public RecyclerView rv_staff_list;
    @BindView(R.id.noData_container)
    public RelativeLayout rl_no_data;

    private StaffList_RvAdapter adapter;
    private String str;//检索关键字
    private String bmglm;
    private String bmmc;
    private List<StaffGeneralInfo> dataSet;
    private RequestManager mRequestManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_staff_list);

        setNavVisible(true);
        mRequestManager = new RequestManager(this, this);
        handleIntent();
        if (bmmc == null || bmmc.isEmpty()) {
            setToolbarTitle("警员列表");
        } else {
            setToolbarTitle(bmmc);
        }
        initRecycler();
        queryStaffList();

    }

    private void initRecycler() {
        dataSet = new ArrayList<>();
        adapter = new StaffList_RvAdapter(this, dataSet, mRequestManager);
        adapter.setOnItemClickedListener(new StaffList_RvAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(String idcard, String img_url, String bmmc, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("idcard", idcard);
                bundle.putString("tx", img_url);
                bundle.putString("bmmc", bmmc);
                hop2Activity(Ac_infoDetail.class, bundle);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_staff_list.setLayoutManager(manager);
        rv_staff_list.setAdapter(adapter);
        rv_staff_list.addItemDecoration(new Rv_Item_decoration(this));
    }

    private void handleIntent() {
        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                str = bundle.getString("str");
                bmglm = bundle.getString("bmglm");
                bmmc = bundle.getString("bmmc");
            }
        }
    }

    private void queryStaffList() {
        String reqName = "queryStaffList";
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(reqName), 15000);
        mRequestManager.postRequest();
    }


    private StaffListRequestBean assembleRequestObj(String reqName) {
        StaffListRequestBean staffListRequestBean = new StaffListRequestBean();
        staffListRequestBean.setYhdm("test");
        staffListRequestBean.setImei(Global.imei);
        staffListRequestBean.setImsi(Global.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        staffListRequestBean.setPdaTime(pda_time);
        staffListRequestBean.setPageNo(100);
        staffListRequestBean.setPageIndex(1);

        if (bmglm != null) {
            staffListRequestBean.setBmglm(bmglm);
            staffListRequestBean.setIsUse(1);
        } else {
            staffListRequestBean.setBmglm("");
        }

        if (str != null) {
            staffListRequestBean.setIsUse(0);
            staffListRequestBean.setStr(str);
        } else {
            staffListRequestBean.setStr("");
        }
        return staffListRequestBean;
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        StaffListResponseBean staffListResponseBean = (StaffListResponseBean) bean;
        if (staffListResponseBean != null) {
            if (staffListResponseBean.getCode().equals("0")) {
                if (staffListResponseBean != null) {
                    List<StaffGeneralInfo> list = staffListResponseBean.getStaffGeneralInfo();
                    if (list == null || list.isEmpty()) {
                        rl_no_data.setVisibility(View.VISIBLE);
                    } else {
                        dataSet.clear();
                        dataSet.addAll(staffListResponseBean.getStaffGeneralInfo());
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                Toast.makeText(this, "服务异常，无法获取数据", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "数据接入错误", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return StaffListResponseBean.class;
    }

    @Override
    public void onHttpRequestCancel() {
        finish();
    }
}
