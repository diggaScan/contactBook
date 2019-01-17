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

import com.github.ybq.android.spinkit.SpinKitView;
import com.sunland.contactbook.DataModel;
import com.sunland.contactbook.R;
import com.sunland.contactbook.V_config;
import com.sunland.contactbook.bean.i_depList_bean.DepGeneralInfo;
import com.sunland.contactbook.bean.i_depList_bean.DepsRequestBean;
import com.sunland.contactbook.bean.i_depList_bean.DepsResponseBean;
import com.sunland.contactbook.customView.CancelableEdit;
import com.sunland.contactbook.utils.WindowInfoUtils;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestManagerCancel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_main extends Ac_base implements Frg_deps_list.OnRvItemClickedListener
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
    @BindView(R.id.loading_icon)
    public SpinKitView loading_icon;

    private FragmentManager mFragmentManager;
    private boolean showSearchIcon;
    private int backStack_nums = 0;//通讯录跳转的层级

    private int backPressed_num = 0;//退出应用时计算backpress点击次数
    private String bmglm;
    private List<String> title_stack;
    private int pointer = 0;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_main);
        title_stack = new ArrayList<>();
        title_stack.add("通讯录");
        setToolbarTitle(title_stack.get(pointer));
        setNavVisible(false);
        mFragmentManager = getSupportFragmentManager();
        initSearchEdit();
        bmglm = DataModel.HANGZHOUSJ_ID;//杭州市局作为最高部门级别
        queryYdjwData(V_config.DEP_LIST);
    }

    public void queryYdjwData(String reqName) {
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(reqName), 15000);
        mRequestManager.postRequestWithoutDialog();
    }

    @Override
    public void onItemClicked(String bmglm, boolean ywxj, String bmmc,String rysl) {
        if (ywxj) {
            isLoading = true;
            loading_icon.setVisibility(View.VISIBLE);
            fl_deps_list_container.setVisibility(View.GONE);
            this.bmglm = bmglm;
            title_stack.add(bmmc);
            pointer++;
            setToolbarTitle(bmmc);
            queryYdjwData(V_config.DEP_LIST);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("bmglm", bmglm);
            bundle.putString("bmmc", bmmc);
            bundle.putString("rysl",rysl);
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

    private DepsRequestBean assembleRequestObj(String reqName) {
        switch (reqName) {
            case V_config.DEP_LIST:
                DepsRequestBean requestBean = new DepsRequestBean();
                assembleBasicRequest(requestBean);
                requestBean.setBmglm(bmglm);
                return requestBean;
        }
        return null;
    }

    @Override
    public void onDataResponse(String reqId, String reqName, ResultBase bean) {
        switch (reqName) {
            case V_config.DEP_LIST:
                isLoading = false;
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fl_deps_list_container.setVisibility(View.VISIBLE);
                                    loading_icon.setVisibility(View.GONE);
                                }
                            }, 300);

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
    public void onHttpRequestCancel() {
        if (backStack_nums == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (pointer > 0&&!isLoading) {
            title_stack.remove(pointer);
            pointer--;
            setToolbarTitle(title_stack.get(pointer));
        }

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
}
