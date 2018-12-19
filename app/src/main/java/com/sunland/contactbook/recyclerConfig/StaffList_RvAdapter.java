package com.sunland.contactbook.recyclerConfig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunland.contactbook.GlideApp;
import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.StaffGeneralInfo;
import com.sunland.netmodule.Global;
import com.sunland.netmodule.network.RequestManager;

import java.util.List;


public class StaffList_RvAdapter extends RecyclerView.Adapter<StaffList_RvAdapter.MyViewHolder> {

    private Context context;
    private List<StaffGeneralInfo> list;
    private LayoutInflater inflater;
    private OnItemClickedListener onItemClickedListener;
    private RequestManager requestManager;


    public StaffList_RvAdapter(Context context, List<StaffGeneralInfo> list, RequestManager requestManager) {
        super();
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.requestManager = requestManager;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public StaffList_RvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.rv_item_staff_info, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StaffList_RvAdapter.MyViewHolder myViewholder, final int i) {
        StaffGeneralInfo info = list.get(i);
        myViewholder.tv_ch.setText(info.getCh());
        myViewholder.tv_zw.setText(info.getZw());
        final String idcard = info.getIdcard();
        final String img_url = info.getTx();
        final String bmmc = info.getBmmc();

        GlideApp.with(context).asBitmap()
                .load("http://" + Global.ip + ":" + Global.port + img_url)
                .placeholder(R.drawable.tx_default)
                .error(R.drawable.tx_default)
                .into(myViewholder.iv_tx);

        myViewholder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClicked(idcard, img_url, bmmc, i);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_tx;
        TextView tv_ch;
        TextView tv_zw;
        LinearLayout ll_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_tx = itemView.findViewById(R.id.tx);
            tv_ch = itemView.findViewById(R.id.name);
            tv_zw = itemView.findViewById(R.id.zw);
            ll_container = itemView.findViewById(R.id.container);
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(String idcard, String img_url, String bmmc, int position);
    }
}
