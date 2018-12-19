package com.sunland.contactbook.recyclerConfig;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunland.contactbook.R;
import com.sunland.contactbook.activities.Ac_infoDetail;

import java.util.List;

public class SearchResult_RvAdapter extends RecyclerView.Adapter<SearchResult_RvAdapter.MyViewHolder> {

    private Context context;
    private List<String> dataSet;
    private LayoutInflater inflater;

    public SearchResult_RvAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.dataSet = list;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public SearchResult_RvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.rv_item_search, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResult_RvAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(dataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Ac_infoDetail.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
