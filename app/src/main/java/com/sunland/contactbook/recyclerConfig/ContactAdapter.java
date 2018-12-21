package com.sunland.contactbook.recyclerConfig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.i_depList_bean.DepGeneralInfo;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewholder> {

    private Context mContext;
    private List<DepGeneralInfo> dataSet;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public ContactAdapter(Context context, List<DepGeneralInfo> dataSet) {
        super();
        this.mContext = context;
        this.dataSet = dataSet;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_layout, viewGroup, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewholder myViewholder, final int i) {

        final DepGeneralInfo info = dataSet.get(i);
        myViewholder.item.setText(info.getBmmc());
        if (!info.isYwxj()) {
            StringBuilder builder = new StringBuilder();
            builder.append("(").append(info.getXjsl()).append(")");
            myViewholder.xjsl.setText(builder.toString());
        } else {
            myViewholder.xjsl.setText("");
        }
        myViewholder.rl_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(info.getBmglm(), info.isYwxj(), info.getBmmc(), i);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder {
        RelativeLayout rl_container;
        TextView item;
        TextView xjsl;

        MyViewholder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            xjsl = itemView.findViewById(R.id.xjsl);
            rl_container = itemView.findViewById(R.id.container);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(String bmglm, boolean ywxj, String bmmmc, int position);
    }
}
