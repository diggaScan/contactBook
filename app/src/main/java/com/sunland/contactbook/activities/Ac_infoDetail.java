package com.sunland.contactbook.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunland.contactbook.GlideApp;
import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.PoliceInfo;
import com.sunland.contactbook.bean.StaffDetailRequestBean;
import com.sunland.contactbook.bean.StaffDetailResponseBean;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.OnRequestManagerCancel;
import com.sunland.netmodule.network.RequestManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_infoDetail extends Ac_base implements OnRequestCallback
        , OnRequestManagerCancel {

    @BindView(R.id.tx)
    public ImageView iv_tx;
    @BindView(R.id.ch)
    public TextView tv_ch;
    @BindView(R.id.jh)
    public TextView tv_jh;
    @BindView(R.id.yddh)
    public TextView tv_yddh;
    @BindView(R.id.bghm)
    public TextView tv_bghm;
    @BindView(R.id.dzyx)
    public TextView tv_dzyx;
    @BindView(R.id.bmmc)
    public TextView tv_bmmc;
    @BindView(R.id.xnhm)
    public TextView tv_xnhm;
    @BindView(R.id.bgsh)
    public TextView tv_bgsh;

    public String idcard;
    private String tx;
    private String bmmc;
    public PoliceInfo policeInfo;
    private RequestManager mRequestManager;
    private Bitmap bitmap_tx;

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            bitmap_tx = resource;
            iv_tx.setImageBitmap(bitmap_tx);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_info_detail);
        setNavVisible(true);
        setToolbarTitle("详情");
        handleIntent();
        queryStaffDetail();
        getTx();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                idcard = bundle.getString("idcard");
                tx = bundle.getString("tx");
                bmmc = bundle.getString("bmmc");
            }
        }
    }

    private void queryStaffDetail() {
        mRequestManager = new RequestManager(this, this);
        String reqName = "queryPoliceDetail";
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(), 15000);
        mRequestManager.postRequest();
    }

    private void getTx() {

//        GlideApp.with(this).asBitmap()
//                .load("http://" + Global.ip + ":" + Global.port + tx)
//                .placeholder(R.drawable.tx_default)
//                .error(R.drawable.tx_default)
//                .into(target);

        GlideApp.with(this).asBitmap()
                .load("http://" + Global.ip + ":" + Global.port + tx)
                .placeholder(R.drawable.tx_default)
                .error(R.drawable.tx_default)
                .into(iv_tx);

    }

    private StaffDetailRequestBean assembleRequestObj() {
        StaffDetailRequestBean requestBean = new StaffDetailRequestBean();
        requestBean.setYhdm("test");
        requestBean.setImei(Global.imei);
        requestBean.setImsi(Global.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);
        requestBean.setIdcard(idcard);
        return requestBean;
    }

    @OnClick({R.id.xnhm_call_icon, R.id.xnhm_msg_icon, R.id.yddh_call_icon, R.id.yddh_msg_icon
            , R.id.bghm_call_icon, R.id.bghm_msg_icon, R.id.tx})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tx:
                showIcon();
                break;
            case R.id.xnhm_call_icon:
                dial(policeInfo.getXnhm());
                break;
            case R.id.yddh_call_icon:
                dial(policeInfo.getYdhm());
                break;
            case R.id.bghm_call_icon:
                dial(policeInfo.getBghm());
                break;
            case R.id.xnhm_msg_icon:
                send_msg(policeInfo.getXnhm());
                break;
            case R.id.yddh_msg_icon:
                send_msg(policeInfo.getYdhm());
                break;
            case R.id.bghm_msg_icon:
                send_msg(policeInfo.getBghm());
                break;
        }
    }

    public void showIcon() {
        if (bitmap_tx == null)
            return;

        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dlg_icon_layout, null);
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageBitmap(bitmap_tx);
        LinearLayout linearLayout = view.findViewById(R.id.dialog_container);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(view);
        window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void dial(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void send_msg(String number) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        StaffDetailResponseBean responseBean = (StaffDetailResponseBean) bean;
        if (responseBean != null) {
            if (responseBean.getCode().equals("0")) {
                PoliceInfo info = responseBean.getPoliceInfo();
                if (info != null) {
                    policeInfo = info;
                    tv_ch.setText(info.getCh());
                    tv_bghm.setText(info.getBghm());
                    tv_bgsh.setText(info.getBgsh());

                    if (info.getBmmc() == null) {
                        tv_bmmc.setText(bmmc);
                    } else {
                        if (info.getBmmc().contains(bmmc)) {
                            tv_bmmc.setText(info.getBmmc());
                        } else {
                            tv_bmmc.setText(new StringBuilder().append(bmmc)
                                    .append(info.getBmmc()).toString());
                        }
                    }


                    tv_dzyx.setText(info.getDzyx());
                    tv_jh.setText(info.getJh());
                    tv_xnhm.setText(info.getXnhm());
                    tv_yddh.setText(info.getYdhm());
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
        return StaffDetailResponseBean.class;
    }

    @Override
    public void onHttpRequestCancel() {
        finish();
    }
}
