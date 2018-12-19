package com.sunland.contactbook.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.contactbook.DataModel;
import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.DepGeneralInfo;
import com.sunland.contactbook.bean.DepsRequestBean;
import com.sunland.contactbook.bean.DepsResponseBean;
import com.sunland.contactbook.customView.CancelableEdit;
import com.sunland.contactbook.utils.WindowInfoUtils;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.OnRequestManagerCancel;
import com.sunland.netmodule.network.RequestManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_main extends CheckSelfPermissionActivity implements OnRequestCallback
        , Frg_deps_list.OnRvItemClickedListener
        , OnRequestManagerCancel {


    @BindView(R.id.content)
    public View container;
    @BindView(R.id.app_search)
    public CancelableEdit ce_search;
    @BindView(R.id.searchContainer)
    public LinearLayout searchContainer;
    @BindView(R.id.enter_query)
    public TextView tv_query;
    @BindView(R.id.deps_list_container)
    public FrameLayout fl_deps_list_container;

    private FragmentManager mFragmentManager;
    private boolean showSearchIcon;
    private int backStack_nums = 0;//通讯录跳转的层级
    //    private List<DepGeneralInfo> rv_dataset;
//    private ContactAdapter adapter;
    private int backPressed_num = 0;//退出应用时计算backpress点击次数
    private RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_main);
        setToolbarTitle("通讯录");
        setNavVisible(false);

        mFragmentManager = getSupportFragmentManager();
        initSearchEdit();
        initData();
    }

    @Override
    public void onItemClicked(String bmglm, boolean ywxj, String bmmc) {
        if (ywxj) {
            String reqName = "queryDepList";
            mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(reqName, bmglm), 15000);
            mRequestManager.postRequest();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("bmglm", bmglm);
            bundle.putString("bmmc", bmmc);
            hop2Activity(Ac_StaffList.class, bundle);
        }
    }

    private void initSearchEdit() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ce_search.setOnTextChangeListener(new CancelableEdit.OnTextChangeListener() {
            @Override
            public void beforeTextChange() {

            }

            @Override
            public void onTextChange() {

            }

            @Override
            public void afterTextChange() {
                final int ce_search_width = ce_search.getWidth();
                if (!showSearchIcon) {
                    ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
                    animator.setDuration(300);
                    animator.start();
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            float value = (float) animation.getAnimatedValue();
                            showSearchIcon = true;
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ce_search_width - (int) (tv_query.getWidth() * value), ce_search.getHeight());
                            lp.gravity = Gravity.CENTER_VERTICAL;
                            lp.leftMargin = WindowInfoUtils.dp2px(Ac_main.this, 8);
                            lp.rightMargin = WindowInfoUtils.dp2px(Ac_main.this, 8);
                            ce_search.setLayoutParams(lp);

                        }
                    });
                }
                String q = ce_search.getText().toString();
                if (q.equals("")) {
                    if (showSearchIcon) {
                        ValueAnimator animator2 = ValueAnimator.ofFloat(0f, 1.0f);
                        animator2.setDuration(300);
                        animator2.start();
                        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                showSearchIcon = false;
                                float value = (float) animation.getAnimatedValue();
                                LinearLayout.LayoutParams lp;
                                if (1 - value < 0.0000001) {
                                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ce_search.getHeight());
                                } else {
                                    lp = new LinearLayout.LayoutParams(ce_search_width + (int) (tv_query.getWidth() * (value))
                                            , ce_search.getHeight());
                                }
                                lp.gravity = Gravity.CENTER_VERTICAL;
                                lp.leftMargin = WindowInfoUtils.dp2px(Ac_main.this, 8);
                                lp.rightMargin = WindowInfoUtils.dp2px(Ac_main.this, 8);
                                ce_search.setLayoutParams(lp);
                            }
                        });
                    }
                }
            }
        });
    }

    public void initData() {
        String reqName = "queryDepList";
        mRequestManager = new RequestManager(this, this);
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(reqName, DataModel.HANGZHOUSJ_ID), 15000);
        mRequestManager.postRequest();

    }


    @OnClick(R.id.enter_query)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.enter_query:
                String str = ce_search.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("str", str);
                hop2Activity(Ac_StaffList.class, bundle);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (backStack_nums > 1) {
            mFragmentManager.popBackStack();
            backStack_nums--;
        } else {
            if (backPressed_num != 1) {
                backPressed_num++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressed_num--;
                    }
                }, 2500);
                Toast.makeText(this, "再按一次，退出应用", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    private DepsRequestBean assembleRequestObj(String reqName, String bmglm) {
        switch (reqName) {
            case "queryDepList":
                DepsRequestBean requestBean = new DepsRequestBean();
                requestBean.setYhdm("test");
                requestBean.setImei(Global.imei);
                requestBean.setImsi(Global.imsi1);
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String pda_time = simpleDateFormat.format(date);
                requestBean.setPdaTime(pda_time);
                requestBean.setBmglm(bmglm);
                return requestBean;
        }
        return null;

    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        switch (reqName) {
            case "queryDepList":
                DepsResponseBean depsBean = (DepsResponseBean) bean;
                if (depsBean != null) {
                    if (depsBean.getCode().equals("0")) {
                        List<DepGeneralInfo> list = depsBean.getDepGeneralInfo();
                        if (list == null || list.isEmpty()) {
                            //show emptiness
                        } else {
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            Frg_deps_list frg_deps_list = new Frg_deps_list();
                            frg_deps_list.setRv_dataset(list);
                            transaction.add(R.id.deps_list_container, frg_deps_list);
                            transaction.show(frg_deps_list);
                            transaction.addToBackStack("tag");
                            transaction.commit();
                            backStack_nums++;
                        }
                    } else {
                        Toast.makeText(this, "服务异常，无法获取数据", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Toast.makeText(this, "数据接入错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        switch (reqName) {
            case "queryDepList":
                return DepsResponseBean.class;
        }
        return null;
    }

    @Override
    public void onHttpRequestCancel() {
        if (backStack_nums == 0) {
            finish();
        }
    }
}
