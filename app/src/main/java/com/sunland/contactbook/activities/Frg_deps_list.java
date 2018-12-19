package com.sunland.contactbook.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunland.contactbook.R;
import com.sunland.contactbook.bean.DepGeneralInfo;
import com.sunland.contactbook.recyclerConfig.ContactAdapter;
import com.sunland.contactbook.recyclerConfig.Rv_Item_decoration;

import java.util.List;

public class Frg_deps_list extends Fragment {

    private RecyclerView rv_deps_list;
    private ContactAdapter contactAdapter;
    private List<DepGeneralInfo> rv_dataset;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_deps_list, container, false);
        intiRlc(view);
        return view;
    }

    private void intiRlc(View view) {
        rv_deps_list = view.findViewById(R.id.deps_list);
        contactAdapter = new ContactAdapter(context, rv_dataset);
//        contactAdapter.setHasStableIds(true);
        contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String bmglm, boolean ywxj, String bmmc, int position) {
                ((OnRvItemClickedListener) context).onItemClicked(bmglm, ywxj, bmmc);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rv_deps_list.setAdapter(contactAdapter);
        rv_deps_list.setLayoutManager(manager);
        rv_deps_list.addItemDecoration(new Rv_Item_decoration(context));
    }

    public void setRv_dataset(List<DepGeneralInfo> rv_dataset) {
        this.rv_dataset = rv_dataset;
    }

    interface OnRvItemClickedListener {
        void onItemClicked(String bmglm, boolean ywxj, String bmmc);
    }
}
